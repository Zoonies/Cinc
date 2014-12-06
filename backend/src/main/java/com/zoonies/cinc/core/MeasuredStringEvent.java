/*
 * Copyright (C) 2007-2014 Birst, Inc. All rights reserved. 
 * BIRST PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.zoonies.cinc.core;

import org.joda.time.DateTime;

/**
 * @author <a href="mailto:pauld@birst.com">Patrick Auld</a>
 * Dec 6, 2014
 */
public class MeasuredStringEvent extends MeasuredEvent<String>{
  public MeasuredStringEvent(DateTime timestamp, String measure) {
    super(timestamp, measure);
  }
}
