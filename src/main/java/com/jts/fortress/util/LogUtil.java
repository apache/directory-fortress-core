/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.util;

import org.apache.log4j.Logger;

/**
 * Contains a simple wrapper for log4j that is used by test utilities.
 *
 * @author Shawn McKinney
 * @created January 28, 2010
 */
public class LogUtil
{
    final private static Logger log = Logger.getLogger(LogUtil.class.getName());


    /**
     * Write a message out to the appropriate log4j level.
     *
     * @param msg Contains message to write out to log.
     */
    public static void logIt(String msg)
    {
        //Priority pri = log.getEffectiveLevel();
        log.log(log.getEffectiveLevel(), msg);
	}
}

