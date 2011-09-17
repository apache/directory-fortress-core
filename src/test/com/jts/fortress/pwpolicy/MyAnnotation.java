/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.pwpolicy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
//@Target(ElementType.FIELD)
//@Target(ElementType.TYPE)
public @interface MyAnnotation {
    public String name();
    public String value();
}

