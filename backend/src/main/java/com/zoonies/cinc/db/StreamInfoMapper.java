/*
 * Copyright (C) 2007-2014 Zoonies All rights reserved. ZOONIES PROPRIETARY/CONFIDENTIAL. Use is
 * subject to license terms.
 */
package com.zoonies.cinc.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.zoonies.cinc.core.MeasureType;
import com.zoonies.cinc.core.StreamInfo;

/**
 * @author <a href="mailto:patrick@patrickauld.com">Patrick Auld</a>
 * Dec 6, 2014
 */
public class StreamInfoMapper implements ResultSetMapper<StreamInfo> {
  
  final static Logger logger = Logger.getLogger(StreamInfoMapper.class.getCanonicalName());
  @Override
  public StreamInfo map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    String id = r.getString("id");
    String label = r.getString("label");
    String measureType = r.getString("measureType");
    logger.info("id=" + id + " label=" + label + "measureType=" + measureType);
    MeasureType type = MeasureType.valueOf(measureType);
    return new StreamInfo(id, label, type);
  }

}
