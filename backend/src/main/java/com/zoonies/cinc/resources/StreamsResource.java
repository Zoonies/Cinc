/*
 * Copyright (C) 2007-2014 Birst, Inc. All rights reserved. BIRST PROPRIETARY/CONFIDENTIAL. Use is
 * subject to license terms.
 */
package com.zoonies.cinc.resources;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;


import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;
import org.skife.jdbi.v2.ResultIterator;

import rx.Observable;
import rx.functions.Func1;

import com.google.common.collect.Lists;
import com.zoonies.cinc.core.MeasuredDoubleEvent;
import com.zoonies.cinc.core.MeasuredEvent;
import com.zoonies.cinc.core.MeasuredIntEvent;
import com.zoonies.cinc.core.MeasuredStringEvent;
import com.zoonies.cinc.core.StreamInfo;
import com.zoonies.cinc.db.DataSetsDal;
import com.zoonies.cinc.rx.IteratorOnSubscribe;

/**
 * @author <a href="mailto:pauld@birst.com">Patrick Auld</a>
 * Dec 6, 2014
 */
//@Path("/api/streams") /api/ set in the CincApplication 
@Path("/streams")
public class StreamsResource {

  final static Logger logger = Logger.getLogger(StreamsResource.class.getCanonicalName());

  private final class MeasureToArray implements Func1<MeasuredEvent<?>, List<Object>> {
    public List<Object> call(MeasuredEvent<?> t1) {
      return Lists.<Object>newArrayList(t1.getTimestamp(), t1.getMeasure());
    }
  }

  private final DBI jdbi;

  public StreamsResource(DBI jdbi) {
    super();
    this.jdbi = jdbi;
  }

  @GET
  public Response getStreams() {
    DataSetsDal dal = jdbi.onDemand(DataSetsDal.class);
    ResultIterator<StreamInfo> streams = dal.getStreams();
    try {
      GenericEntity<List<StreamInfo>> entity =
          new GenericEntity<List<StreamInfo>>(Lists.newArrayList(streams)) {};
      return Response.ok(entity).build();
    } finally {
      streams.close();
    }
  }

  @GET
  @Path("{id}")
  public Response getRawData(@PathParam("id") String id, @Context UriInfo info) {
    DataSetsDal dal = jdbi.onDemand(DataSetsDal.class);
    Handle handle = jdbi.open();
    try {
      ResultIterator<MeasuredEvent<?>> results = getDataForStream(id, dal, handle);
      Iterable<List<Object>> iterable =
          Observable.create(new IteratorOnSubscribe<MeasuredEvent<?>>(results))
              .map(new MeasureToArray()).toBlocking().toIterable();
      return Response.ok(iterable).build();
    } finally {
      handle.close();
    }
  }

  // @GET
  // @Path("user/happiness")
  // public Response getRawData(@QueryParam("t") Integer id) {
  //   DataSetsDal dal = jdbi.onDemand(DataSetsDal.class);
  //   Handle handle = jdbi.open();
  //   try {
  //     ResultIterator<MeasuredEvent<?>> results = getDataForStreamWithUser("happiness", id, dal, handle);
  //     Iterable<List<Object>> iterable =
  //         Observable.create(new IteratorOnSubscribe<MeasuredEvent<?>>(results))
  //             .map(new MeasureToArray()).toBlocking().toIterable();
  //     return Response.ok(iterable).build();
  //   } finally {
  //     handle.close();
  //   }
  // }

  /**
   * @param id
   * @param dal
   * @param handle
   * @return
   */
  private ResultIterator<MeasuredEvent<?>> getDataForStream(String id, DataSetsDal dal,
      Handle handle) {
    ResultIterator<StreamInfo> streamInfoItr = dal.getStreamInfo(id);
    try {
      if (!streamInfoItr.hasNext()) {
        throw new WebApplicationException(404);
      }
      StreamInfo stream = streamInfoItr.next();
      // Yay SQL injections!
      Query<Map<String, Object>> query =
          handle.createQuery("SELECT dateTime, measure FROM dev.stream_" + id);
      ResultIterator<MeasuredEvent<?>> results = mapStreamMeasureType(stream, query);
      return results;
    } finally {
      streamInfoItr.close();
    }
  }

  /**
   * @param id
   * @param dal
   * @param handle
   * @return
   */
  private ResultIterator<MeasuredEvent<?>> getDataForStreamWithUser(String id, Integer userId, DataSetsDal dal,
      Handle handle) {
    ResultIterator<StreamInfo> streamInfoItr = dal.getStreamInfo(id);
    try {
      if (!streamInfoItr.hasNext()) {
        throw new WebApplicationException(404);
      }
      StreamInfo stream = streamInfoItr.next();
      // Yay SQL injections!
      Query<Map<String, Object>> query =
          handle.createQuery("SELECT dateTime, measure FROM dev."+id+" where dev."+id+".userId = "+userId);
      ResultIterator<MeasuredEvent<?>> results = mapStreamMeasureType(stream, query);
      return results;
    } finally {
      streamInfoItr.close();
    }
  }

  /**
   * @param stream
   * @param query
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private ResultIterator<MeasuredEvent<?>> mapStreamMeasureType(StreamInfo stream,
      Query<Map<String, Object>> query) {
    ResultIterator<MeasuredEvent<?>> results;
    switch (stream.getMeasureType()) {
     case INTEGER: {
        Query<MeasuredEvent<?>> mapped = (Query) query.mapTo(MeasuredIntEvent.class);
        results = mapped.iterator();
        break;
      }
      case DOUBLE: 
      case FLOAT: { 
        Query<MeasuredEvent<?>> mapped = (Query) query.mapTo(MeasuredDoubleEvent.class);
        results = mapped.iterator();
        break;
      }
      case STRING: {
        Query<MeasuredEvent<?>> mapped = (Query) query.mapTo(MeasuredStringEvent.class);
        results = mapped.iterator();
        break;
      }
      default:
        throw new WebApplicationException(400);
    }
    return results;
  }
}
