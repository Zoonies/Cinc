/*
 * Copyright (C) 2007-2014 Zoonies All rights reserved. ZOONIES PROPRIETARY/CONFIDENTIAL. Use is
 * subject to license terms.
 */
package com.zoonies.cinc.core;

/**
 * @author <a href="mailto:patrick@patrickauld.com">Patrick Auld</a>
 * Dec 6, 2014
 */
public class StreamInfo {

  private final String id;
  private final String label;
  private final MeasureType measureType;

  public StreamInfo(String id, String label, MeasureType measureType) {
    super();
    this.id = id;
    this.label = label;
    this.measureType = measureType;
  }

  public String getId() {
    return id;
  }

  public String getLabel() {
    return label;
  }

  public MeasureType getMeasureType() {
    return measureType;
  }
}
