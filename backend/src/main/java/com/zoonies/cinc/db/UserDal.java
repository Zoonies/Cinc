/*
 * Copyright (C) 2007-2014 Zoonies All rights reserved. 
 * ZOONIES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.zoonies.cinc.db;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author <a href="mailto:patrick@patrickauld.com">Patrick Auld</a>
 * Dec 6, 2014
 */
public interface UserDal {

  @SqlUpdate("insert into happiness (userId, happiness) values (:userId, :happiness)")
  void setUserHappiness(@Bind("userId") String userId, @Bind("happiness") int happiness);
  
  void close();
}
