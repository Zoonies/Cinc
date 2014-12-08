/*
 * Copyright (C) 2007-2014 Birst, Inc. All rights reserved. BIRST PROPRIETARY/CONFIDENTIAL. Use is
 * subject to license terms.
 */
package com.zoonies.cinc.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zoonies.cinc.core.MeasuredIntEvent;
import com.zoonies.cinc.core.SendSms;

/**
 * @author <a href="mailto:patrick@patrickauld.com">Patrick Auld</a>
 * Dec 6, 2014
 */
@Path("/user")
public class UsersResource {

  final static Logger logger = LoggerFactory.getLogger(UsersResource.class);
  private final DBI jdbi;

  public UsersResource(DBI jdbi) {
    super();
    this.jdbi = jdbi;
  }

  @POST
  @Path("happiness")
  public Response updateUserHappiness(@QueryParam("t") Integer id, HappinessUpdateRequest request) {
    Handle handle = jdbi.open();
    try {
      handle.update("insert into happiness (userId, measure, zip) values (?, ?, 0)", id,
          request.getHappiness());
      return Response.noContent().build();
    } finally {
      handle.close();
    }
  }

  @GET
  @Path("happiness")
  public Response getUserHappiness(@QueryParam("t") Integer id) {
    Handle handle = jdbi.open();
    try {
      List<MeasuredIntEvent> events =
          handle.createQuery("select dateTime, measure, zip from happiness where userId = ? ORDER BY dateTime")
          .bind(0, id)
          .mapTo(MeasuredIntEvent.class)
          .list();
      return Response.ok(events).build();
    } finally {
      handle.close();
    }
  }

  @POST
  @Path("signup")
  public Response createUserRecord(@QueryParam("n") Integer id) {
    logger.info("Creating user record for id " + String.valueOf(id));
    Handle handle = jdbi.open();
    try {
      handle.update("insert into users (userId) values (0)", id);
      SendSms.sendSms(id.toString());
    } catch(Exception e) {
      logger.error("Error encountered while attempting signup", e.toString());
    } finally {
      handle.close();
    }
    return Response.noContent().build();
  }

}
