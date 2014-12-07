/*
 * Copyright (C) 2007-2014 Birst, Inc. All rights reserved. BIRST PROPRIETARY/CONFIDENTIAL. Use is
 * subject to license terms.
 */
package com.zoonies.cinc.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

/**
 * @author <a href="mailto:pauld@birst.com">Patrick Auld</a>
 * Dec 6, 2014
 */
//@Path("/api/user") /api/ set in the CincApplication 
@Path("/user")
public class UsersResource {

  private final DBI jdbi;

  public UsersResource(DBI jdbi) {
    super();
    this.jdbi = jdbi;
  }
  
  @POST
  @Path("happiness")
  public Response updateUserHappiness(@QueryParam("t") Integer id,  
      HappinessUpdateRequest request) {
    Handle handle = jdbi.open();
    try {
      System.out.printf("-----------Integer : %d\n",id);

      handle.update("insert into happiness (userId, measure, zip) values (?, ?, 0)", id, request.getHappiness());
      return Response.noContent().build();
    } finally {
      handle.close();
    }
  }
}
