/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */
package com.jts.fortress.util.time;

/**
 * Class contains a custom timestamp that is processed by {@link Validator} to check {@link Constraint}.
 *
 * @author Shawn McKinney
 * @created February 13, 2010
 * @version $Revision: 777748 $ $Date: 2009-05-22 20:00:44 -0400 (Fri, 22 May 2009) $
 * @since Pool 1.0

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

