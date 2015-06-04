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
package org.apache.directory.fortress.core.util.time;


import org.apache.directory.api.util.DateUtils;

import java.text.ParseException;
import java.util.*;


/**
 * Utility class to convert current time/date into internal format, {@link Time}, used for {@link org.apache.directory.fortress.core.model.Constraint} checks {@link org.apache.directory.fortress.core.util.VUtil#validateConstraints(org.apache.directory.fortress.core.model.Session, org.apache.directory.fortress.core.util.VUtil.ConstraintType, boolean)}.
 * This utility processes custom date formats and should not be used by external programs.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class TUtil
{
    /**
     * Private constructor
     *
     */
    private TUtil()
    {
    }

    /**
     * Get the curent timestamp from Java and convert to {@link Time} format.
     *
     * @return Time
     */
    public static Time getCurrentTime()
    {
        Time time = new Time();
        GregorianCalendar gc = new GregorianCalendar();
        String szMinute = "" + gc.get( GregorianCalendar.MINUTE );
        String szHour = "" + gc.get( GregorianCalendar.HOUR_OF_DAY );

        time.day = "" + gc.get( GregorianCalendar.DAY_OF_WEEK );
        String szDay = "" + gc.get( GregorianCalendar.DAY_OF_MONTH );
        int month = gc.get( GregorianCalendar.MONTH );
        String szMonth = "" + ( month + 1 );
        String szYear = "" + gc.get( GregorianCalendar.YEAR );

        if ( szMinute.length() == 1 )
        {
            szMinute = "0" + szMinute;
        }

        if ( szHour.length() == 1 )
        {
            szHour = "0" + szHour;
        }

        if ( szDay.length() == 1 )
        {
            szDay = "0" + szDay;
        }

        if ( szMonth.length() == 1 )
        {
            szMonth = "0" + szMonth;
        }

        String szCurrentTime = szHour + szMinute;

        time.currentTime = Integer.valueOf( szCurrentTime );
        time.date = szYear + szMonth + szDay;
        return time;
    }


    /**
     * Convert from raw ldap generalized time format to {@link java.util.Date}.
     * to decode the string.
     *
     * @param inputString containing raw ldap generalized time formatted string.
     * @return converted to {@link java.util.Date}.
     */
    public static java.util.Date decodeGeneralizedTime(String inputString) throws ParseException
    {
        java.util.Date aDate;
        aDate = DateUtils.getDate( inputString );
        return aDate;
    }


    /**
     * Convert from java date {@link java.util.Date} format to raw ldap generalized time format.
     * to encode the string.
     *
     * @param date reference to standard java date.
     * @return converted to standardized ldap generalized time format.
     */
    public static String encodeGeneralizedTime(java.util.Date date)
    {
        String szTime;
        szTime = DateUtils.getGeneralizedTime( date );
        return szTime;
    }
}
