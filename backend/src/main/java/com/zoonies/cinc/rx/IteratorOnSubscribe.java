/*
 * Copyright (C) 2007-2014 Zoonies All rights reserved. 
 * ZOONIES PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.zoonies.cinc.rx;

import java.util.Iterator;

import rx.Observable.OnSubscribe;
import rx.Subscriber;

/**
 * @author <a href="mailto:patrick@patrickauld.com">Patrick Auld</a>
 * Dec 6, 2014
 */
public class IteratorOnSubscribe<T> implements OnSubscribe<T>{

  private final Iterator<T> itr;
  
  public IteratorOnSubscribe(Iterator<T> itr) {
    super();
    this.itr = itr;
  }

  @Override
  public void call(Subscriber<? super T> t1) {
    while (this.itr.hasNext()) {
      T t = this.itr.next();
      t1.onNext(t);
    }
    t1.onCompleted();
  }
}
