/*
 * Copyright (C) 2007-2014 Birst, Inc. All rights reserved. 
 * BIRST PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.zoonies.cinc.db;

import org.skife.jdbi.v2.ResultIterator;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import com.zoonies.cinc.core.StreamInfo;

/**
 * @author <a href="mailto:pauld@birst.com">Patrick Auld</a>
 * Dec 6, 2014
 */
public interface DatasetsDal {

  @SqlQuery("SELECT * FROM dev.streams")
  ResultIterator<StreamInfo> getStreams();
  
  void close();
}
