/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.util;

import org.slf4j.LoggerFactory;

/**
 * Contains a simple wrapper for log4j that is used by test utilities.
 *
 * @author Shawn McKinney
 */
public class LogUtil
{
    //final private static Logger log = Logger.getLogger(LogUtil.class.getName());
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger( LogUtil.class.getName() );


    /**
     * Write a message out to the appropriate log level.
     *
     * @param msg Contains message to write out to log.
     */
    public static void logIt(String msg)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug( msg );
        }
        else if(LOG.isInfoEnabled())
        {
            LOG.info( msg );
        }
        else if(LOG.isWarnEnabled())
        {
            LOG.warn( msg );
        }
        else if(LOG.isErrorEnabled())
        {
            LOG.error( msg );
        }
	}
}

