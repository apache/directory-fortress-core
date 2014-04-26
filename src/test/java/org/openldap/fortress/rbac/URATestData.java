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

package org.openldap.fortress.rbac;


import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openldap.fortress.util.attr.VUtil;


/**
 * Description of the Class
 *
 * @author Shawn McKinney
 */
public class URATestData extends TestCase
{
    private static final String CLS_NM = URATestData.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    public final static String[][] URA_T1 =
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
        if ( VUtil.isNotNullOrEmpty( ura[CAN_ASSIGN_COL] ) && ura[CAN_ASSIGN_COL].equalsIgnoreCase( "T" ) )
        {
            isCanAssign = true;
        }
        return isCanAssign;
    }


    public static boolean isCanDeassign( String[] ura )
    {
        boolean isCanDeassign = false;
        if ( VUtil.isNotNullOrEmpty( ura[CAN_ASSIGN_COL] ) && ura[CAN_ASSIGN_COL].equalsIgnoreCase( "T" ) )
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
}
