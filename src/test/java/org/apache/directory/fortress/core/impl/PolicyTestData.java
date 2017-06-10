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
package org.apache.directory.fortress.core.impl;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import junit.framework.TestCase;

import org.apache.directory.fortress.core.model.PwPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Description of the Class
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */

public class PolicyTestData extends TestCase
{
    /**
     * Description of the Field
     */
    private static final Logger LOG = LoggerFactory.getLogger( PolicyTestData.class.getName() );

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface TP1
    {
        String id();


        String message();
    }

    @MyAnnotation(name = "POLICIES_BASE", value = "PLCY BASE")
    final public static String[][] POLICIES_BASE =
        {

            {
                "Test1", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "7776000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "4", /* MINLEN_COL */
                "2592000", /* EXPIREWARN_COL */
                "3", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "FALSE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
            },
        };


    /**
     * Test Case TP1:
     */
    @MyAnnotation(name = "POLICIES_TP1", value = "PLCY TP1")
    final public static String[][] POLICIES_TP1 =
        {

            {
                "oamTP1Policy1", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "2", /* MINAGE_COL */
                "60000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "60000", /* EXPIREWARN_COL */
                "3", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
        },
            {
                "oamTP1Policy2", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "3", /* MINAGE_COL */
                "60000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "60000", /* EXPIREWARN_COL */
                "3", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
    },
            {
                "oamTP1Policy3", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "5", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "9", /* EXPIREWARN_COL */
                "0", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "FALSE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy4", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "2", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "1", /* EXPIREWARN_COL */
                "0", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "FALSE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy5", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "60000", /* MAXAGE_COL */
                "10", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "60000", /* EXPIREWARN_COL */
                "3", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "10", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy6", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "60000", /* MAXAGE_COL */
                "100", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "60000", /* EXPIREWARN_COL */
                "3", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy7", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "60000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "10", /* MINLEN_COL */
                "60000", /* EXPIREWARN_COL */
                "3", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy8", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "60000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "70000", /* EXPIREWARN_COL */
                "3", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy9", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "4", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "2", /* EXPIREWARN_COL */
                "0", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy10", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "7", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "3", /* EXPIREWARN_COL */
                "0", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy11", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "6", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "3", /* EXPIREWARN_COL */
                "10", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy12", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "5", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "4", /* EXPIREWARN_COL */
                "5", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy13", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "600000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "0", /* EXPIREWARN_COL */
                "0", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "10", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy14", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "600000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "0", /* EXPIREWARN_COL */
                "0", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "3", /* LOCKDURATION_COL */
                "5", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy15", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "600000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "0", /* EXPIREWARN_COL */
                "0", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "4", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy16", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "6000000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "0", /* EXPIREWARN_COL */
                "0", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "5", /* LOCKDURATION_COL */
                "5", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy17", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "6000000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "50000", /* EXPIREWARN_COL */
                "0", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy18", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "6000000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "10000", /* EXPIREWARN_COL */
                "3", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "0", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy19", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "600000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "1000", /* EXPIREWARN_COL */
                "3", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "6", /* MAXFAIL_COL */
                "5", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy20", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "6000000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "0", /* EXPIREWARN_COL */
                "0", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "10000", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "7", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy21", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "6000000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "0", /* EXPIREWARN_COL */
                "0", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "8", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy22", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "600000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "10000", /* EXPIREWARN_COL */
                "3", /* GRACE_COL */
                "FALSE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy23", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "6000000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "0", /* EXPIREWARN_COL */
                "3", /* GRACE_COL */
                "FALSE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "FALSE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy24", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "6000000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "10000", /* EXPIREWARN_COL */
                "0", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy25", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "600000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "0", /* EXPIREWARN_COL */
                "3", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "TRUE" /* SAFEMODIFY_COL */
},
            {
                "oamTP1Policy26", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "600000", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "1000", /* EXPIREWARN_COL */
                "0", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "30", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
}
    };

    @MyAnnotation(name = "POLICIES_TP2", value = "PLCY TP2")
    final public static String[][] POLICIES_TP2 =
        {

            {
                "oamTP2Policy1", /* NAME_COL */
                "userPassword", /* ATTR_COL */
                "0", /* MINAGE_COL */
                "0", /* MAXAGE_COL */
                "5", /* HISTORY_COL */
                "2", /* CHKQUAL_COL */
                "5", /* MINLEN_COL */
                "0", /* EXPIREWARN_COL */
                "0", /* GRACE_COL */
                "TRUE", /* LOCKOUT_COL */
                "0", /* LOCKDURATION_COL */
                "3", /* MAXFAIL_COL */
                "0", /* INTERVAL_COL */
                "TRUE", /* MUSTCHG_COL */
                "TRUE", /* ALLOWCHG_COL */
                "FALSE" /* SAFEMODIFY_COL */
        },
    };


    /**
     * @param policy
     * @param plcy
     */
    public static void assertEquals( PwPolicy policy, String[] plcy )
    {
        assertEquals( PolicyTestData.class.getName() + ".assertEquals failed compare policy name", getName( plcy ),
            policy.getName() );
        //assertEquals(PolicyTestData.class.getName() + ".assertEquals failed compare policy attribute", getAttribute(plcy), policy.getAttribute());
        assertEquals( PolicyTestData.class.getName() + ".assertEquals failed compare policy minAge", getMinAge( plcy ),
            policy.getMinAge() );
        assertEquals( PolicyTestData.class.getName() + ".assertEquals failed compare policy maxAge", getMaxAge( plcy ),
            policy.getMaxAge() );
        assertEquals( PolicyTestData.class.getName() + ".assertEquals failed compare policy inHistory",
            getInHistory( plcy ), policy.getInHistory() );
        assertEquals( PolicyTestData.class.getName() + ".assertEquals failed compare policy ", getCheckQuality( plcy ),
            policy.getCheckQuality() );
        assertEquals( PolicyTestData.class.getName() + ".assertEquals failed compare policy minLength",
            getMinLength( plcy ), policy.getMinLength() );
        assertEquals( PolicyTestData.class.getName() + ".assertEquals failed compare policy expireWarning",
            getExpireWarning( plcy ), policy.getExpireWarning() );
        assertEquals( PolicyTestData.class.getName() + ".assertEquals failed compare policy graceLoginLimit",
            getGraceLoginLimit( plcy ), policy.getGraceLoginLimit() );
        assertEquals( PolicyTestData.class.getName() + ".assertEquals failed compare policy lockout",
            getLockout( plcy ), policy.getLockout() );
        assertEquals( PolicyTestData.class.getName() + ".assertEquals failed compare policy lockoutDuration",
            getLockoutDuration( plcy ), policy.getLockoutDuration() );
        assertEquals( PolicyTestData.class.getName() + ".assertEquals failed compare policy maxFailure",
            getMaxFailure( plcy ), policy.getMaxFailure() );
        assertEquals( PolicyTestData.class.getName() + ".assertEquals failed compare policy failureCountInterval",
            getFailureCountInterval( plcy ), policy.getFailureCountInterval() );
        assertEquals( PolicyTestData.class.getName() + ".assertEquals failed compare policy mustChange",
            getMustChange( plcy ), policy.getMustChange() );
        assertEquals( PolicyTestData.class.getName() + ".assertEquals failed compare policy allowUserChange",
            getAllowUserChange( plcy ), policy.getAllowUserChange() );
        assertEquals( PolicyTestData.class.getName() + ".assertEquals failed compare policy safeModify",
            getSafeModify( plcy ), policy.getSafeModify() );
        LOG.debug( PolicyTestData.class.getName() + ".assertEquals [" + policy.getName() + "] successful" );
    }


    /**
     * @param plcy
     * @return
     */
    public static PwPolicy getPolicy( String[] plcy )
    {
        PwPolicy policy = new PwPolicy();
        policy.setName( getName( plcy ) );
        //policy.setAttribute(getAttribute(plcy));
        policy.setMinAge( getMinAge( plcy ) );
        policy.setMaxAge( getMaxAge( plcy ) );
        policy.setInHistory( getInHistory( plcy ) );
        policy.setCheckQuality( getCheckQuality( plcy ) );
        policy.setMinLength( getMinLength( plcy ) );
        policy.setExpireWarning( getExpireWarning( plcy ) );
        policy.setGraceLoginLimit( getGraceLoginLimit( plcy ) );
        policy.setLockout( getLockout( plcy ) );
        policy.setLockoutDuration( getLockoutDuration( plcy ) );
        policy.setMaxFailure( getMaxFailure( plcy ) );
        policy.setFailureCountInterval( getFailureCountInterval( plcy ) );
        policy.setMustChange( getMustChange( plcy ) );
        policy.setAllowUserChange( getAllowUserChange( plcy ) );
        policy.setSafeModify( getSafeModify( plcy ) );
        return policy;
    }

    /**
     * The Fortress test data for junit uses 2-dimensional arrays.
     */
    private final static int NAME_COL = 0;
    private final static int ATTR_COL = 1;
    private final static int MINAGE_COL = 2;
    private final static int MAXAGE_COL = 3;
    private final static int HISTORY_COL = 4;
    private final static int CHKQUAL_COL = 5;
    private final static int MINLEN_COL = 6;
    private final static int EXPIREWARN_COL = 7;
    private final static int GRACE_COL = 8;
    private final static int LOCKOUT_COL = 9;
    private final static int LOCKDURATION_COL = 10;
    private final static int MAXFAIL_COL = 11;
    private final static int INTERVAL_COL = 12;
    private final static int MUSTCHG_COL = 13;
    private final static int ALLOWCHG_COL = 14;
    private final static int SAFEMODIFY_COL = 15;


    /*
    private String name;
    private String attribute;
    private Integer minAge;
    private Integer maxAge;
    private Short inHistory;
    private Short checkQuality;
    private Short minLength;
    private Integer expireWarning;
    private Short graceLoginLimit;
    private Boolean lockout;
    private Integer lockoutDuration;
    private Short maxFailure;
    private Short failureCountInterval;
    private Boolean mustChange;
    private Boolean allowUserChange;
    private Boolean safeModify;
    */

    public static String getName( String[] plcy )
    {
        return plcy[NAME_COL];
    }


    public static String getAttribute( String[] plcy )
    {
        return plcy[ATTR_COL];
    }


    public static Integer getMinAge( String[] plcy )
    {
        return new Integer( plcy[MINAGE_COL] );
    }


    public static Long getMaxAge( String[] plcy )
    {
        return new Long( plcy[MAXAGE_COL] );
    }


    public static Short getInHistory( String[] plcy )
    {
        return new Short( plcy[HISTORY_COL] );
    }


    public static Short getCheckQuality( String[] plcy )
    {
        return new Short( plcy[CHKQUAL_COL] );
    }


    public static Short getMinLength( String[] plcy )
    {
        return new Short( plcy[MINLEN_COL] );
    }


    public static Long getExpireWarning( String[] plcy )
    {
        return new Long( plcy[EXPIREWARN_COL] );
    }


    public static Short getGraceLoginLimit( String[] plcy )
    {
        return new Short( plcy[GRACE_COL] );
    }


    public static Boolean getLockout( String[] plcy )
    {
        //return new Boolean(plcy[LOCKOUT_COL]);
        return Boolean.valueOf( plcy[LOCKOUT_COL] );
    }


    public static Integer getLockoutDuration( String[] plcy )
    {
        return new Integer( plcy[LOCKDURATION_COL] );
    }


    public static Short getMaxFailure( String[] plcy )
    {
        return new Short( plcy[MAXFAIL_COL] );
    }


    public static Short getFailureCountInterval( String[] plcy )
    {
        return new Short( plcy[INTERVAL_COL] );
    }


    public static Boolean getMustChange( String[] plcy )
    {
        //return new Boolean(plcy[MUSTCHG_COL]);
        return Boolean.valueOf( plcy[MUSTCHG_COL] );
    }


    public static Boolean getAllowUserChange( String[] plcy )
    {
        //return new Boolean(plcy[ALLOWCHG_COL]);
        return Boolean.valueOf( plcy[ALLOWCHG_COL] );
    }


    public static Boolean getSafeModify( String[] plcy )
    {
        //return new Boolean(plcy[SAFEMODIFY_COL]);
        return Boolean.valueOf( plcy[SAFEMODIFY_COL] );
    }
}