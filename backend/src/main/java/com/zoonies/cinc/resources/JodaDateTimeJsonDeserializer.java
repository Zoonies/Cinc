package com.zoonies.cinc.resources;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author <a href="mailto:pauld@birst.com">Patrick Auld</a>
 * Jul 24, 2014
 */
public final class JodaDateTimeJsonDeserializer extends JsonDeserializer<DateTime> {

  @Override
  public DateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
      JsonProcessingException {
    String text = jp.getText();
    return DateTime.parse(text, JodaDateTimeJsonSerializer.FORMATTER)
        .withZone(DateTimeZone.UTC);
  }
}
