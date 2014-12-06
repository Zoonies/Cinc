/*
 * Copyright (C) 2007-2014 Birst, Inc. All rights reserved. BIRST PROPRIETARY/CONFIDENTIAL. Use is
 * subject to license terms.
 */
package com.zoonies.cinc.resources;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.ResultIterator;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.functions.Func1;

import com.google.common.collect.Lists;
import com.zoonies.cinc.core.MeasuredDoubleEvent;
import com.zoonies.cinc.core.StreamInfo;
import com.zoonies.cinc.db.DatasetsDal;
import com.zoonies.cinc.rx.IteratorOnSubscribe;

/**
 * @author <a href="mailto:pauld@birst.com">Patrick Auld</a>
 * Dec 6, 2014
 */
@Path("/api/streams")
public class StreamsResource {
  
  final static Logger logger = Logger.getLogger(StreamsResource.class.getCanonicalName());

  private final class MeasureToArray implements Func1<MeasuredDoubleEvent, List<Object>> {
    public List<Object> call(MeasuredDoubleEvent t1) {
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
    DatasetsDal dal = jdbi.onDemand(DatasetsDal.class);
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
  public Response getRawData(@PathParam("id") String id) {
    Handle handle = jdbi.open();
    try {
      ResultIterator<MeasuredDoubleEvent> results =
          //Yay SQL injections!
          handle.createQuery("SELECT dateTime, measure FROM dev.stream_" + id)
              .mapTo(MeasuredDoubleEvent.class).iterator();
      Iterable<List<Object>> iterable = Observable.create(
          new IteratorOnSubscribe<MeasuredDoubleEvent>(results))
          .map(new MeasureToArray())
          .toBlocking().toIterable();
      return Response.ok(iterable).build();
    } finally {
      handle.close();
    }
  }
}
