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


import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.model.RoleConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Description of the Class
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class URATestData extends TestCase
{
    private static final String CLS_NM = URATestData.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    public static final String[][] URA_T1 =
        {
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "T", /* CAN_ASSIGN COL */
        },
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "T", /* CAN_ASSIGN COL */
    },
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
}
    };

    public static final String[] URC_T1 =
    {
        "TPASET1", // CONSTRAINT_PASET_NM
        "FILTER", //CONSTAINT_TYPE
        "TPASET1AttributeName1=testattributevalue" //CONSTAIN_VALUE
    };
    
    public static final String[] URC_T2 =
    {
        "TPASET1", // CONSTRAINT_PASET_NM
        "FILTER", //CONSTAINT_TYPE
        "TPASET1AttributeName1=testattributevalue2" //CONSTAIN_VALUE
    };
    
    public static final String[] URC_T3 =
    {
        "TPASET1", // CONSTRAINT_PASET_NM
        "FILTER", //CONSTAINT_TYPE
        "TPASET1AttributeName1=testattributevalue3" //CONSTAIN_VALUE
    };
    
    public static final String[] URC_T1_INVALID =
    {
        "TPASETNAMENOTEXIST", // CONSTRAINT_PASET_NM
        "FILTER", //CONSTAINT_TYPE
        "TPASET1AttributeName1=testattributevalue" //CONSTAIN_VALUE
    };
    
    /**
    * The Fortress test data for junit uses 2-dimensional arrays.
    */
    private final static int AROLE_COL = 0;
    private final static int UOU_COL = 1;
    private final static int UROLE_COL = 2;
    private final static int CAN_ASSIGN_COL = 3;


    /**
     *
     * @param ura
     * @return
     */
    public static String getArole( String[] ura )
    {
        return ura[AROLE_COL];
    }


    /**
     *
     * @param ura
     * @return
     */
    public static String getUou( String[] ura )
    {
        return ura[UOU_COL];
    }


    /**
     *
     * @param ura
     * @return
     */
    public static String getUrole( String[] ura )
    {
        return ura[UROLE_COL];
    }


    /**
     *
     * @param ura
     * @return
     */
    public static boolean isCanAssign( String[] ura )
    {
        boolean isCanAssign = false;
        if ( StringUtils.isNotEmpty( ura[CAN_ASSIGN_COL] ) && ura[CAN_ASSIGN_COL].equalsIgnoreCase( "T" ) )
        {
            isCanAssign = true;
        }
        return isCanAssign;
    }


    public static boolean isCanDeassign( String[] ura )
    {
        boolean isCanDeassign = false;
        if ( StringUtils.isNotEmpty( ura[CAN_ASSIGN_COL] ) && ura[CAN_ASSIGN_COL].equalsIgnoreCase( "T" ) )
        {
            isCanDeassign = true;
        }
        return isCanDeassign;
    }


    public static URA getUra( String[] ura )
    {
        return new URA( getArole( ura ), getUou( ura ), getUrole( ura ), isCanAssign( ura ) );
    }


    /**
     * 
     * @param uras
     * @return
     */
    public static Map<URA, URA> getURAs( String[][] uras )
    {
        Map<URA, URA> listUras = new HashMap<>();
        for ( String[] szUra : uras )
        {
            URA ura = getUra( szUra );
            listUras.put( ura, ura );
        }
        return listUras;
    }
    
    public static RoleConstraint getRC( String[] rc )
    {
        RoleConstraint urc = new RoleConstraint();
        urc.setKey( rc[0] );
        urc.setType( RoleConstraint.RCType.valueOf( rc[1] ) );
        urc.setValue(rc[2]);
        return urc;
    }
}
