/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
//@Target(ElementType.FIELD)
//@Target(ElementType.TYPE)
public @interface MyAnnotation {
    public String name();
    public String value();
}

