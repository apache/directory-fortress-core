/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */
package us.jts.fortress.util.time;

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

