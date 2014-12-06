/*
 * Copyright (C) 2007-2014 Birst, Inc. All rights reserved. 
 * BIRST PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.zoonies.cinc.db;

import java.util.Iterator;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

import com.zoonies.cinc.core.MeasuredDoubleEvent;

/**
 * @author <a href="mailto:pauld@birst.com">Patrick Auld</a>
 * Dec 6, 2014
 */
public interface DataSetsDal {

  @SqlQuery("SELECT dataTime, measure FROM :tableName")
  Iterator<MeasuredDoubleEvent> getDoubleData(@Bind("tableName") String tableName);
  
  void close();
}
