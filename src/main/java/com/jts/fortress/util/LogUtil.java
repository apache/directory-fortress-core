/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package com.jts.fortress.util;

import org.apache.log4j.Logger;

/**
 * Contains a simple wrapper for log4j that is used by test utilities.
 *
 * @author Shawn McKinney
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

