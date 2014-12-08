/*
 * Copyright (C) 2007-2014 Zoonies All rights reserved. ZOONIES PROPRIETARY/CONFIDENTIAL. Use is
 * subject to license terms.
 */
package com.zoonies.cinc.core;

import org.joda.time.DateTime;

/**
 * @author <a href="mailto:patrick@patrickauld.com">Patrick Auld</a>
 * Dec 6, 2014
 */
public abstract class MeasuredEvent<T> {

  private final DateTime timestamp;
  private final T measure;

  public MeasuredEvent(DateTime timestamp, T measure) {
    super();
    this.timestamp = timestamp;
    this.measure = measure;
  }

  public DateTime getTimestamp() {
    return timestamp;
  }
  
  public T getMeasure() {
    return measure;
  }

}
