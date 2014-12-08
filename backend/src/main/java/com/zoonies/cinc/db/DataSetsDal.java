/*
 * Copyright (C) 2007-2014 Zoonies All rights reserved. 
 * ZOONIES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.zoonies.cinc.db;

import org.skife.jdbi.v2.ResultIterator;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import com.zoonies.cinc.core.StreamInfo;

/**
 * @author <a href="mailto:patrick@patrickauld.com">Patrick Auld</a>
 * Dec 6, 2014
 */
public interface DataSetsDal {

  @SqlQuery("SELECT * FROM dev.streams")
  ResultIterator<StreamInfo> getStreams();
  
  @SqlQuery("SELECT * FROM dev.streams WHERE id = :id LIMIT 1")
  ResultIterator<StreamInfo> getStreamInfo(@Bind("id") String id);
  
  void close();
}
