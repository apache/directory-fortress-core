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

package org.openldap.fortress.util;

import org.slf4j.LoggerFactory;
import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.util.attr.VUtil;

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
        msg = getContext() + " " + msg;
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

    public static String getContext()
    {
        String contextId = null;
        String tenant = System.getProperty( GlobalIds.TENANT );
        if ( VUtil.isNotNullOrEmpty( tenant ) && !tenant.equals( "${tenant}" ) )
        {
            contextId = tenant;
        }
        return contextId;
    }
}

