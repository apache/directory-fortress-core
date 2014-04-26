/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
 */
package org.openldap.fortress.util.time;

/**
 * Class contains a custom timestamp that is processed by {@link Validator} to check {@link Constraint}.
 *
 * @author Shawn McKinney
 */
public class Time
{
    /**
     * Stored as {@code System.out.getCurrentMillis()} format.
     */
    public Integer currentTime;

    /**
     * Stored in '1234567' format for Sun, Mon, Tue, Wed, Thur, Fri, Sat respectively.  i.e. '23456' is Mon-Friday.
     */
    public String day;

    /**
     * Stored in 'YYYYMMDD' format.  i.e. '20110101' is January 1, 2011.
     */
    public String date;
}

