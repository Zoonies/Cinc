/*
 * Copyright (C) 2007-2014 Birst, Inc. All rights reserved. BIRST PROPRIETARY/CONFIDENTIAL. Use is
 * subject to license terms.
 */
package com.zoonies.cinc.db;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.zoonies.cinc.core.MeasuredIntEvent;

/**
 * @author <a href="mailto:pauld@birst.com">Patrick Auld</a>
 * Dec 6, 2014
 */
public class MeasuredIntegerEventMapper implements ResultSetMapper<MeasuredIntEvent> {
  @Override
  public MeasuredIntEvent map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    Date date = r.getDate("dateTime");
    DateTime realDate = new DateTime(date.getTime());
    int measure = r.getInt("measure");
    return new MeasuredIntEvent(realDate, measure);
  }

}
