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


import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.model.AdminRole;
import org.apache.directory.fortress.core.model.Constraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description of the Class
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AdminRoleTestData extends TestCase
{
    private static final String CLS_NM = AdminRoleTestData.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    public static final String[][] AROLES_SUPER =
        {
            {
                "fortress-core-super-admin", /* NAME_COL */
                "fortress core super admin role", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "DEV0", /* OSU_COL */
                "APP0" /* OSP_COL */
        }
    };
    /**
      * Test Case TR1:
     */
    public static final String[][] AROLES_TR1 =
        {
            {
                "oamAdmin1", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                "oamT14Role1", /* BEGIN_RANGE_COL (child) */
                "oamT14Role2", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "oamT1UOrg1,oamT2UOrg1", /* OSU_COL */
                "oamT3POrg1,oamT4POrg1" /* OSP_COL */
        },
            {
                "oamAdmin2", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                "oamT14Role2", /* BEGIN_RANGE_COL (child) */
                "oamT14Role3", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "oamT1UOrg2,oamT2UOrg2", /* OSU_COL */
                "oamT3POrg2,oamT4POrg2" /* OSP_COL */
    },
            {
                "oamAdmin3", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                "oamT14Role3", /* BEGIN_RANGE_COL (child) */
                "oamT14Role4", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "oamT1UOrg3,oamT2UOrg3", /* OSU_COL */
                "oamT3POrg3,oamT4POrg3" /* OSP_COL */
},
            {
                "oamAdmin4", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMre.getBeginRange(EOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                "oamT14Role4", /* BEGIN_RANGE_COL (child) */
                "oamT14Role5", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "oamT1UOrg4,oamT2UOrg4", /* OSUre.getBeginRange(_COL */
                "oamT3POrg4,oamT4POrg4" /* OSP_COL */
},
            {
                "oamAdmin5", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOre.getBeginRange(CKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                "oamT14Role5", /* BEGIN_RANGE_COL (child) */
                "oamT14Role6", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "oamT1UOrg5,oamT2UOrg5", /* OSU_COL */
                "oamT3POrg5,oamT4POrg5" /* OSP_COL */
},
            {
                "oamAdmin6", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                "oamT14Role6", /* BEGIN_RANGE_COL (child) */
                "oamT14Role7", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "oamT1UOrg6,oamT2UOrg6", /* OSU_COL */
                "oamT3POrg6,oamT4POrg6" /* OSP_COL */
},
            {
                "oamAdmin7", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                "oamT14Role7", /* BEGIN_RANGE_COL (child) */
                "oamT14Role8", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "oamT1UOrg7,oamT2UOrg7", /* OSU_COL */
                "oamT3POrg7,oamT4POrg7" /* OSP_COL */
},
            {
                "oamAdmin8", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                "oamT14Role8", /* BEGIN_RANGE_COL (child) */
                "oamT14Role9", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "oamT1UOrg8,oamT2UOrg8", /* OSU_COL */
                "oamT3POrg8,oamT4POrg8" /* OSP_COL */
},
            {
                "oamAdmin9", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                "oamT14Role9", /* BEGIN_RANGE_COL (child) */
                "oamT14Role10", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "oamT1UOrg9,oamT2UOrg9", /* OSU_COL */
                "oamT3POrg9,oamT4POrg9" /* OSP_COL */
},
            {
                "oamAdmin10", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                "oamT14Role1", /* BEGIN_RANGE_COL (child) */
                "oamT14Role10", /* END_RANGE_COL (parent) */
                "F", /* IS_BEGIN_INCLUSIVE */
                "F", /* IS_END_INCLUSIVE */
                "oamT1UOrg10,oamT2UOrg10", /* OSU_COL */
                "oamT3POrg10,oamT4POrg10" /* OSP_COL */
},
    };

    public static final String[][] AROLES_TR2 =
        {
            {
                "oamT2UAdmin1", /* NAME_COL */
                "Test Case TR2", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ), /* BEGIN_RANGE_COL (child) */
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ), /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ), /* OSU_COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ) /* OSP_COL */
        },
            {
                "oamT2UAdmin2", /* NAME_COL */
                "Test Case TR2", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ), /* BEGIN_RANGE_COL (child) */
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ), /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "F", /* IS_END_INCLUSIVE */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ), /* OSU_COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ) /* OSP_COL */
    },
            {
                "oamT2UAdmin3", /* NAME_COL */
                "Test Case TR2", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ), /* BEGIN_RANGE_COL (child) */
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ), /* END_RANGE_COL (parent) */
                "F", /* IS_BEGIN_INCLUSIVE */
                "F", /* IS_END_INCLUSIVE */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ), /* OSU_COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ) /* OSP_COL */
},
            {
                "oamT2UAdmin4", /* NAME_COL */
                "Test Case TR2", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ), /* BEGIN_RANGE_COL (child) */
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ), /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "F", /* IS_END_INCLUSIVE */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ), /* OSU_COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ) /* OSP_COL */
},
            {
                "oamT2UAdmin5", /* NAME_COL */
                "Test Case TR2", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ), /* BEGIN_RANGE_COL (child) */
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ), /* END_RANGE_COL (parent) */
                "F", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ), /* OSU_COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ) /* OSP_COL */
}
    };

    public static final String[][] AROLES_TR3 =
        {
            {
                "T3AdmRle1", /* NAME_COL */
                "Admin Role1 TR3", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
        },
            {
                "T3AdmRle2", /* NAME_COL */
                "Admin Role2 TR3", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
    },
            {
                "T3AdmRle3", /* NAME_COL */
                "Admin Role3 TR3", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
}
    };

    public static final String[][] AROLES_TR3_UPD =
        {
            {
                "T3AdmRle1", /* NAME_COL */
                "Admin Role1 TR3 Updated", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "0800", /* BTIME_COL */
                "1700", /* ETIME_COL */
                "20110101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "23456", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
        },
            {
                "T3AdmRle2", /* NAME_COL */
                "Admin Role1 TR3 Updated", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "1700", /* BTIME_COL */
                "2300", /* ETIME_COL */
                "20110501", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "12345", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
    },
            {
                "T3AdmRle3", /* NAME_COL */
                "Admin Role3 TR3 Updated", /* DESC_COL */
                "45", /* TIMEOUT_COL */
                "2300", /* BTIME_COL */
                "0700", /* ETIME_COL */
                "20110301", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "34567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
}
    };

    public static final String[][] AROLES_TR4_ASC =
        {
            {
                "T4AdmRle1", /* NAME_COL */
                "Admin Role1 TR4 ASC", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "0800", /* BTIME_COL */
                "1700", /* ETIME_COL */
                "20110101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "23456", /* DAYMASK_COL */
                "T4AdmRle2,T4AdmRle3", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "C", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
        },
            {
                "T4AdmRle2", /* NAME_COL */
                "Admin Role2 TR4 ASC", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "0800", /* BTIME_COL */
                "1700", /* ETIME_COL */
                "20110101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "23456", /* DAYMASK_COL */
                "T4AdmRle4,T4AdmRle5", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "A", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
    },
            {
                "T4AdmRle3", /* NAME_COL */
                "Admin Role3 TR4 ASC", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "0800", /* BTIME_COL */
                "1700", /* ETIME_COL */
                "20110101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "23456", /* DAYMASK_COL */
                "T4AdmRle6,T4AdmRle7", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "A", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
},
    };

    public static final String[][] AROLES_TR5_DSC =
        {
            {
                "T5AdmRle1", /* NAME_COL */
                "Admin Role1 TR5 DSC", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "0800", /* BTIME_COL */
                "1700", /* ETIME_COL */
                "20110101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "23456", /* DAYMASK_COL */
                "T5AdmRle2,T5AdmRle3", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "D", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
        },
            {
                "T5AdmRle2", /* NAME_COL */
                "Admin Role2 TR5 DSC", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "0800", /* BTIME_COL */
                "1700", /* ETIME_COL */
                "20110101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "23456", /* DAYMASK_COL */
                "T5AdmRle4,T5AdmRle5", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "D", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
    },
            {
                "T5AdmRle3", /* NAME_COL */
                "Admin Role3 TR5 DSC", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "0800", /* BTIME_COL */
                "1700", /* ETIME_COL */
                "20110101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "23456", /* DAYMASK_COL */
                "T5AdmRle6,T5AdmRle7", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "D", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
},
    };

    public static final String[][] AROLES_TR6_HIER =
        {
            {
                "T6AdmRle1", /* NAME_COL */
                "Admin Role1 TR6 HIER", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "0800", /* BTIME_COL */
                "1700", /* ETIME_COL */
                "20110101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "23456", /* DAYMASK_COL */
                "T6AdmRle2", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
        },
            {
                "T6AdmRle2", /* NAME_COL */
                "Admin Role2 TR6 HIER", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "0800", /* BTIME_COL */
                "1700", /* ETIME_COL */
                "20110101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "23456", /* DAYMASK_COL */
                "T6AdmRle3", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
    },
            {
                "T6AdmRle3", /* NAME_COL */
                "Admin Role3 TR6 HIER", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "0800", /* BTIME_COL */
                "1700", /* ETIME_COL */
                "20110101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "23456", /* DAYMASK_COL */
                "T6AdmRle4", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
},
            {
                "T6AdmRle4", /* NAME_COL */
                "Admin Role4 TR6 HIER", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "0800", /* BTIME_COL */
                "1700", /* ETIME_COL */
                "20110101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "23456", /* DAYMASK_COL */
                "T6AdmRle5", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
},
            {
                "T6AdmRle5", /* NAME_COL */
                "Admin Role5 TR6 HIER", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "0800", /* BTIME_COL */
                "1700", /* ETIME_COL */
                "20110101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "23456", /* DAYMASK_COL */
                "T6AdmRle6", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
},
            {
                "T6AdmRle6", /* NAME_COL */
                "Admin Role6 TR6 HIER", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "0800", /* BTIME_COL */
                "1700", /* ETIME_COL */
                "20110101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "23456", /* DAYMASK_COL */
                "T6AdmRle7", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
},
            {
                "T6AdmRle7", /* NAME_COL */
                "Admin Role7 TR6 HIER", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "0800", /* BTIME_COL */
                "1700", /* ETIME_COL */
                "20110101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "23456", /* DAYMASK_COL */
                "T6AdmRle8", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
},
            {
                "T6AdmRle8", /* NAME_COL */
                "Admin Role8 TR6 HIER", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "0800", /* BTIME_COL */
                "1700", /* ETIME_COL */
                "20110101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "23456", /* DAYMASK_COL */
                "T6AdmRle9", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
},
            {
                "T6AdmRle9", /* NAME_COL */
                "Admin Role9 TR6 HIER", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "0800", /* BTIME_COL */
                "1700", /* ETIME_COL */
                "20110101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "23456", /* DAYMASK_COL */
                "T6AdmRle10", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
},
            {
                "T6AdmRle10", /* NAME_COL */
                "Admin Role10 TR6 HIER", /* DESC_COL */
                "15", /* TIMEOUT_COL */
                "0800", /* BTIME_COL */
                "1700", /* ETIME_COL */
                "20110101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "23456", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "", /* INHERITANCE_FLAG_COL */
                "", /* BEGIN_RANGE_COL (child) */
                "", /* END_RANGE_COL (parent) */
                "T", /* IS_BEGIN_INCLUSIVE */
                "T", /* IS_END_INCLUSIVE */
                "", /* OSU_COL */
                "" /* OSP_COL */
},
    };

    /**
    * The Fortress test data for junit uses 2-dimensional arrays.
    */
    /*
    private final static int NAME_COL = 0;
    private final static int DESC_COL = 1;
    private final static int TIMEOUT_COL = 2;
    private final static int BTIME_COL = 3;
    private final static int ETIME_COL = 4;
    private final static int BDATE_COL = 5;
    private final static int EDATE_COL = 6;
    private final static int BLOCKDATE_COL = 7;
    private final static int ELOCKDATE_COL = 8;
    private final static int DAYMASK_COL = 9;
    private final static int RELATIONSHIP_COL = 10;
    private final static int INHERITANCE_COL = 11;
    private final static int INHERITANCE_FLAG_COL = 12;
    */
    private final static int BEGIN_RANGE_COL = 13;
    private final static int END_RANGE_COL = 14;
    private final static int IS_BEGIN_INCLUSIVE = 15;
    private final static int IS_END_INCLUSIVE = 16;
    private final static int OSU = 17;
    private final static int OSP = 18;


    private static Set<String> getOsUSet(String[] rle)
    {
        Set<String> members = new HashSet<>();
        if ( StringUtils.isNotEmpty( rle[OSU] ) )
        {
            StringTokenizer charSetTkn = new StringTokenizer( rle[OSU], TestUtils.DELIMITER_TEST_DATA );
            if ( charSetTkn.countTokens() > 0 )
            {
                while ( charSetTkn.hasMoreTokens() )
                {
                    String member = charSetTkn.nextToken();
                    members.add( member );
                }
            }
        }
        return members;
    }


    private static Set<String> getOsPSet(String[] rle)
    {
        Set<String> members = new HashSet<>();
        if ( StringUtils.isNotEmpty( rle[OSP] ) )
        {
            StringTokenizer charSetTkn = new StringTokenizer( rle[OSP], TestUtils.DELIMITER_TEST_DATA );
            if ( charSetTkn.countTokens() > 0 )
            {
                while ( charSetTkn.hasMoreTokens() )
                {
                    String member = charSetTkn.nextToken();
                    members.add( member );
                }
            }
        }
        return members;
    }


    private static String getBeginRange( String[] rle )
    {
        return rle[BEGIN_RANGE_COL];
    }


    private static String getEndRange( String[] rle )
    {
        return rle[END_RANGE_COL];
    }


    private static boolean isBeginInclusive( String[] rle )
    {
        boolean result = false;
        if ( StringUtils.isNotEmpty( rle[IS_BEGIN_INCLUSIVE] )
            && rle[IS_BEGIN_INCLUSIVE].equalsIgnoreCase( "T" ) )
        {
            result = true;
        }

        return result;
    }


    private static boolean isEndInclusive( String[] rle )
    {
        boolean result = false;
        if ( StringUtils.isNotEmpty( rle[IS_END_INCLUSIVE] ) && rle[IS_END_INCLUSIVE].equalsIgnoreCase( "T" ) )
        {
            result = true;
        }

        return result;
    }


    public static AdminRole getRole( String[] rle )
    {
        AdminRole role = ( AdminRole ) getRoleConstraint( rle );
        role.setName( RoleTestData.getName( rle ) );
        role.setDescription( RoleTestData.getDescription( rle ) );
        role.setOsUSet( getOsUSet( rle ) );
        role.setOsPSet( getOsPSet( rle ) );
        role.setBeginRange( getBeginRange( rle ) );
        role.setEndRange( getEndRange( rle ) );
        role.setBeginInclusive( isBeginInclusive( rle ) );
        role.setEndInclusive( isEndInclusive( rle ) );
        return role;
    }


    private static Constraint getRoleConstraint( String[] rle )
    {
        AdminRole role = new AdminRole();
        role.setBeginDate( RoleTestData.getBeginDate( rle ) );
        role.setEndDate( RoleTestData.getEndDate( rle ) );
        role.setBeginLockDate( RoleTestData.getBeginLockDate( rle ) );
        role.setEndLockDate( RoleTestData.getEndLockDate( rle ) );
        role.setBeginTime( RoleTestData.getBeginTime( rle ) );
        role.setEndTime( RoleTestData.getEndTime( rle ) );
        role.setDayMask( RoleTestData.getDayMask( rle ) );
        role.setTimeout( RoleTestData.getTimeOut( rle ) );
        return role;
    }


    /**
     *
     * @param role
     * @param rle
     */
    public static void assertEquals( AdminRole role, String[] rle )
    {
        RoleTestData.assertEquals( role, rle );
        LOG.debug( "assertEquals [" + role.getName() + "] successful" );
    }
}
