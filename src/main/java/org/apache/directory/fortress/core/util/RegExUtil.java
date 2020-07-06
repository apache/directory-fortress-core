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


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.directory.api.util.Strings;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *  Regular expression utilities to perform data validations on Fortress attributes.  These utils use the standard
 * java regular expression library.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
final class RegExUtil
{
    private static final String CLS_NM = RegExUtil.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private String SAFE_TEXT_PATTERN_STRING;
    private static Pattern safeTextPattern;
    
    private static volatile RegExUtil sINSTANCE = null;

    public static RegExUtil getInstance()
    {
        if(sINSTANCE == null)
        {
            synchronized (RegExUtil.class)
            {
                if(sINSTANCE == null)
                {
                    sINSTANCE = new RegExUtil();
                }
            }
        }
        return sINSTANCE;
    }
    
    private void init() 
    {
        SAFE_TEXT_PATTERN_STRING = Config.getInstance().getProperty( GlobalIds.REG_EX_SAFE_TEXT );
    
        if ( ( SAFE_TEXT_PATTERN_STRING != null ) && ( SAFE_TEXT_PATTERN_STRING.length() != 0 ) )
        {
            safeTextPattern = Pattern.compile( SAFE_TEXT_PATTERN_STRING );
        }
    }

    /**
     * Private constructor
     *
     */
    private RegExUtil()
    {
        init();
    }

    /**
     *  Perform safe text validation on character string.
     *
     * @param  value Contains the string to check.
     * @exception org.apache.directory.fortress.core.ValidationException  In the event the data validation fails.
     */
    void safeText( String value ) throws ValidationException
    {
        if ( Strings.isEmpty( SAFE_TEXT_PATTERN_STRING ) )
        {
            LOG.debug( "safeText can't find safeText regular expression pattern.  Check your Fortress cfg" );
        }
        else
        {
            Matcher safeTextMatcher = safeTextPattern.matcher( value );
            
            if ( !safeTextMatcher.find() )
            {
                String error = "safeText has detected invalid value [" + value + "]";
                throw new ValidationException( GlobalErrIds.CONST_INVLD_TEXT, error );
            }
        }
    }
}