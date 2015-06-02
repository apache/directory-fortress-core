/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.fortress.core.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.directory.fortress.core.GlobalIds;

/**
 * Contains a simple wrapper for log4j that is used by test utilities.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class LogUtil
{
    private static final Logger LOG = LoggerFactory.getLogger( LogUtil.class.getName() );

    /**
     * Private constructor
     *
     */
    private LogUtil()
    {
    }

    /**
     * Write a message out to the appropriate log level.
     *
     * @param msg Contains message to write out to log.
     */
    public static void logIt(String msg)
    {
        if ( LOG.isDebugEnabled() || LOG.isInfoEnabled() || LOG.isWarnEnabled() || LOG.isErrorEnabled() )
        {
            if ( StringUtils.isNotEmpty( getContext() ) )
            {
                msg = getContext() + " " + msg;
            }
    
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( msg );
            }
            else if( LOG.isInfoEnabled() )
            {
                LOG.info( msg );
            }
            else if ( LOG.isWarnEnabled() )
            {
                LOG.warn( msg );
            }
            else if ( LOG.isErrorEnabled() )
            {
                LOG.error( msg );
            }
        }
    }

    public static String getContext()
    {
        String contextId = null;
        String tenant = System.getProperty( GlobalIds.TENANT );
        
        if ( StringUtils.isNotEmpty( tenant ) && !tenant.equals( "${tenant}" ) )
        {
            contextId = tenant;
        }

        return contextId;
    }
}

