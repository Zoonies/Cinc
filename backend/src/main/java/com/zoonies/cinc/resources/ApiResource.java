/*
 * Copyright (C) 2007-2014 Birst, Inc. All rights reserved. BIRST PROPRIETARY/CONFIDENTIAL. Use is
 * subject to license terms.
 */
package com.zoonies.cinc.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.ResultIterator;

import rx.Observable;
import rx.functions.Func1;

import com.google.common.collect.Lists;
import com.zoonies.cinc.core.MeasuredDoubleEvent;
import com.zoonies.cinc.rx.IteratorOnSubscribe;

/**
 * @author <a href="mailto:pauld@birst.com">Patrick Auld</a>
 * Dec 6, 2014
 */
@Path("/api")
public class ApiResource {

  private final class MeasureToArray implements Func1<MeasuredDoubleEvent, List<Object>> {
    public List<Object> call(MeasuredDoubleEvent t1) {
      return Lists.<Object>newArrayList(t1.getTimestamp(), t1.getMeasure());
    }
  }

  private final DBI jdbi;

  public ApiResource(DBI jdbi) {
    super();
    this.jdbi = jdbi;
  }
  
  @POST
  @Path("users/{id}/happiness")
  public Response updateUserHappiness(@PathParam("id") Integer id, HappinessUpdateRequest request) {
    Handle handle = jdbi.open();
    try {
      handle.update("insert into happiness (userId, measure, zip) values (?, ?, 0)", id, request.getHappiness());
      return Response.noContent().build();
    } finally {
      handle.close();
    }
  }
  
  @GET
  @Path("dataSources/{id}")
  public Response getRawData(@PathParam("id") String id) {
    Handle handle = jdbi.open();
    try {
      ResultIterator<MeasuredDoubleEvent> results =
          //Yay SQL injections!
          handle.createQuery("SELECT dateTime, measure FROM " + id)
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
