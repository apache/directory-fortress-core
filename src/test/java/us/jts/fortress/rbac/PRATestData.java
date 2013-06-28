/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;


import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.jts.fortress.util.attr.VUtil;


/**
 * Description of the Class
 *
 * @author Shawn McKinney
 */
public class PRATestData extends TestCase
{
    private static final String CLS_NM = PRATestData.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    public final static String[][] PRA_T1 =
        {
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "T", /* CAN_ASSIGN COL */
        },
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "T", /* CAN_ASSIGN COL */
    },
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin1", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin2", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin3", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin4", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[0] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[1] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[2] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[3] ),
                "F", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[4] ),
                "T", /* CAN_ASSIGN COL */
},
            {
                "oamT2UAdmin5", /* AROLE COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                RoleTestData.getName( RoleTestData.ROLES_TR15_ARBAC[5] ),
                "F", /* CAN_ASSIGN COL */
}
    };

    /**
    * The Fortress test data for junit uses 2-dimensional arrays.
    */
    private final static int AROLE_COL = 0;
    private final static int POU_COL = 1;
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
    public static String getPou( String[] ura )
    {
        return ura[POU_COL];
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
    public static boolean isCanGrant( String[] ura )
    {
        boolean isCanGrant = false;
        if ( VUtil.isNotNullOrEmpty( ura[CAN_ASSIGN_COL] ) && ura[CAN_ASSIGN_COL].equalsIgnoreCase( "T" ) )
        {
            isCanGrant = true;
        }
        return isCanGrant;
    }


    public static boolean isCanRevoke( String[] ura )
    {
        boolean isCanRevoke = false;
        if ( us.jts.fortress.util.attr.VUtil.isNotNullOrEmpty( ura[CAN_ASSIGN_COL] )
            && ura[CAN_ASSIGN_COL].equalsIgnoreCase( "T" ) )
        {
            isCanRevoke = true;
        }
        return isCanRevoke;
    }


    public static PRA getPra( String[] ura )
    {
        return new PRA( getArole( ura ), getPou( ura ), getUrole( ura ), isCanGrant( ura ) );
    }


    /**
     *
     * @param uras
     * @return
     */
    public static Map<PRA, PRA> getPRAs( String[][] uras )
    {
        Map<PRA, PRA> listUras = new HashMap<>();
        for ( String[] szUra : uras )
        {
            PRA ura = getPra( szUra );
            listUras.put( ura, ura );
        }
        return listUras;
    }
}
