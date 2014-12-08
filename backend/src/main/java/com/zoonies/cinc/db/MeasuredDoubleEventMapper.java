/*
 * Copyright (C) 2007-2014 Zoonies All rights reserved. ZOONIES PROPRIETARY/CONFIDENTIAL. Use is
 * subject to license terms.
 */
package com.zoonies.cinc.db;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.zoonies.cinc.core.MeasuredDoubleEvent;

/**
 * @author <a href="mailto:patrick@patrickauld.com">Patrick Auld</a>
 * Dec 6, 2014
 */
public class MeasuredDoubleEventMapper implements ResultSetMapper<MeasuredDoubleEvent> {
  @Override
  public MeasuredDoubleEvent map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    Date date = r.getDate("dateTime");
    DateTime realDate = new DateTime(date.getTime());
    double measure = r.getDouble("measure");
    return new MeasuredDoubleEvent(realDate, measure);
  }

}
