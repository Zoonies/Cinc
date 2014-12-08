/*
 * Copyright (C) 2007-2014 Zoonies All rights reserved. 
 * ZOONIES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.zoonies.cinc.core;

import org.joda.time.DateTime;

/**
 * @author <a href="mailto:patrick@patrickauld.com">Patrick Auld</a>
 * Dec 6, 2014
 */
public class MeasuredIntEvent extends MeasuredEvent<Integer>{
  public MeasuredIntEvent(DateTime timestamp, Integer measure) {
    super(timestamp, measure);
  }
}
