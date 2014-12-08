/*
 * Copyright (C) 2007-2014 Zoonies All rights reserved. 
 * ZOONIES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.zoonies.cinc.resources;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author <a href="mailto:patrick@patrickauld.com">Patrick Auld</a>
 * Jul 24, 2014
 */
public final class JodaDateTimeJsonSerializer extends JsonSerializer<DateTime> {

  protected static final DateTimeFormatter FORMATTER = ISODateTimeFormat.dateTime().withZoneUTC();
  
  @Override
  public void serialize(DateTime value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonProcessingException {
    String dateTimeStr = FORMATTER.print(value);
    jgen.writeString(dateTimeStr);
  }
}
