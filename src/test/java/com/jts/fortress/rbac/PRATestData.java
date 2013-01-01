/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.util.attr.VUtil;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Description of the Class
 *
 * @author Shawn McKinney
 */
class PRATestData extends TestCase
{
    private static final String CLS_NM = PRATestData.class.getName();
    final protected static Logger log = Logger.getLogger(CLS_NM);

    final static String[][] PRA_T1 = {
         {
             "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
             "T",                                      /* CAN_ASSIGN COL */
         },
         {
             "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
             "T",                                      /* CAN_ASSIGN COL */
         },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin1",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin2",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin3",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin4",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[0]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[1]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[2]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[3]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[0]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[1]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[2]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[3]),
            "F",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[4]),
            "T",                                      /* CAN_ASSIGN COL */
        },
        {
            "oamT2UAdmin5",                           /* AROLE COL */
             OrgUnitTestData.getName(OrgUnitTestData.ORGS_PRM_TO5[4]),
             RoleTestData.getName(RoleTestData.ROLES_TR15_ARBAC[5]),
            "F",                                      /* CAN_ASSIGN COL */
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
    public static String getArole(String[] ura)
    {
        return ura[AROLE_COL];
    }

    /**
     *
     * @param ura
     * @return
     */
    public static String getPou(String[] ura)
    {
        return ura[POU_COL];
    }

    /**
     *
     * @param ura
     * @return
     */
    public static String getUrole(String[] ura)
    {
        return ura[UROLE_COL];
    }


    /**
     *
     * @param ura
     * @return
     */
    public static boolean isCanGrant(String[] ura)
    {
        boolean isCanGrant = false;
        if(VUtil.isNotNullOrEmpty(ura[CAN_ASSIGN_COL]) && ura[CAN_ASSIGN_COL].equalsIgnoreCase("T"))
        {
            isCanGrant = true;
        }
        return isCanGrant;
    }


    public static boolean isCanRevoke(String[] ura)
    {
        boolean isCanRevoke = false;
        if(com.jts.fortress.util.attr.VUtil.isNotNullOrEmpty(ura[CAN_ASSIGN_COL]) && ura[CAN_ASSIGN_COL].equalsIgnoreCase("T"))
        {
            isCanRevoke = true;
        }
        return isCanRevoke;
    }


    public static PRA getPra(String[] ura)
    {
        PRA entity = new PRA(getArole(ura), getPou(ura), getUrole(ura), isCanGrant(ura));
        return entity;
    }


    /**
     *
     * @param uras
     * @return
     */
    public static Map<PRA,PRA> getPRAs(String[][] uras)
    {
        Map<PRA,PRA> listUras = new HashMap<PRA,PRA>();
        for(String[] szUra : uras)
        {
            PRA ura = getPra(szUra);
            listUras.put(ura, ura);
        }
        return listUras;
    }
}
