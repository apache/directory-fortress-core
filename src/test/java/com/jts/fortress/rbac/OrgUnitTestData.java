/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.util.attr.VUtil;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Description of the Class
 *
 * @author Shawn McKinney
 */
public class OrgUnitTestData extends TestCase
{
    private static final String CLS_NM = OrgUnitTestData.class.getName();
    final protected static Logger log = Logger.getLogger(CLS_NM);

    public final static String[][] ORGS_USR_DEV0 = {
         {
             "DEV0",                                   /* NAME_COL */
             "DEV Case T0",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
             "",                                       /* RELATIONSHIP_COL */
             "",                                       /* INHERITANCE_COL */
             "T"                                       /* INHERITANCE_FLAG_COL */
         },
     };

    public final static String[][] ORGS_PRM_APP0 = {
         {
             "APP0",                                   /* NAME_COL */
             "APP Case T0",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
             "",                                       /* RELATIONSHIP_COL */
             "",                                       /* INHERITANCE_COL */
             "T"                                       /* INHERITANCE_FLAG_COL */
         },
     };

    final static String[][] ORGS_DEV1 = {
         {
             "DEV1",                                   /* NAME_COL */
             "DEV Case T1",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
             "",                                       /* RELATIONSHIP_COL */
             "",                                       /* INHERITANCE_COL */
             "T"                                       /* INHERITANCE_FLAG_COL */
         },
        {
            "DEV2",                                    /* NAME_COL */
            "DEV Case T2",                             /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "DEV3",                                    /* NAME_COL */
            "DEV Case T3",                             /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "DEV4",                                    /* NAME_COL */
            "DEV Case T4",                             /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "DEV5",                                    /* NAME_COL */
            "DEV Case T5",                             /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "DEV6",                                    /* NAME_COL */
            "DEV Case T6",                             /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "DEV7",                                    /* NAME_COL */
            "DEV Case T7",                             /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "DEV8",                                    /* NAME_COL */
            "DEV Case T8",                             /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "DEV9",                                    /* NAME_COL */
            "DEV Case T9",                             /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "DEV10",                                   /* NAME_COL */
            "DEV Case T10",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        }
     };

    final static String[][] ORGS_APP1 = {
         {
             "APP1",                                   /* NAME_COL */
             "APP Case T1",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
             "",                                       /* RELATIONSHIP_COL */
             "",                                       /* INHERITANCE_COL */
             "T"                                       /* INHERITANCE_FLAG_COL */
         },
        {
            "APP2",                                    /* NAME_COL */
            "APP Case T2",                             /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "APP3",                                    /* NAME_COL */
            "APP Case T3",                             /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "APP4",                                    /* NAME_COL */
            "APP Case T4",                             /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "APP5",                                    /* NAME_COL */
            "APP Case T5",                             /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "APP6",                                    /* NAME_COL */
            "APP Case T6",                             /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "APP7",                                    /* NAME_COL */
            "APP Case T7",                             /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "APP8",                                    /* NAME_COL */
            "APP Case T8",                             /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "APP9",                                    /* NAME_COL */
            "APP Case T9",                             /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "APP10",                                   /* NAME_COL */
            "APP Case T10",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        }
     };

    final static String[][] ORGS_TO1 = {
         {
             "oamT1UOrg1",                             /* NAME_COL */
             "Test Case T1",                           /* DESC_COL */
             "U",                                      /* TYPE COL */
             "",                                       /* RELATIONSHIP_COL */
             "",                                       /* INHERITANCE_COL */
             "T"                                       /* INHERITANCE_FLAG_COL */
         },
        {
            "oamT1UOrg2",                              /* NAME_COL */
            "Test Case T1",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT1UOrg3",                              /* NAME_COL */
            "Test Case T1",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT1UOrg4",                              /* NAME_COL */
            "Test Case T1",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT1UOrg5",                              /* NAME_COL */
            "Test Case T1",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT1UOrg6",                              /* NAME_COL */
            "Test Case T1",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT1UOrg7",                              /* NAME_COL */
            "Test Case T1",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT1UOrg8",                              /* NAME_COL */
            "Test Case T1",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT1UOrg9",                              /* NAME_COL */
            "Test Case T1",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT1UOrg10",                             /* NAME_COL */
            "Test Case T1",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        }
     };

    final static String[][] ORGS_USR_TO2 = {
         {
             "oamT2UOrg1",                             /* NAME_COL */
             "Test Case T2",                           /* DESC_COL */
             "U",                                      /* TYPE COL */
             "oamT2UOrg2",                             /* RELATIONSHIP_COL */
             "",                                       /* INHERITANCE_COL */
             "A"                                       /* INHERITANCE_FLAG_COL */
         },
        {
            "oamT2UOrg2",                              /* NAME_COL */
            "Test Case T2",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "oamT2UOrg3",                              /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT2UOrg3",                              /* NAME_COL */
            "Test Case T2",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "oamT2UOrg4",                              /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT2UOrg4",                              /* NAME_COL */
            "Test Case T2",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "oamT2UOrg5",                              /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT2UOrg5",                              /* NAME_COL */
            "Test Case T2",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "oamT2UOrg6",                              /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT2UOrg6",                              /* NAME_COL */
            "Test Case T2",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "oamT2UOrg7",                              /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT2UOrg7",                              /* NAME_COL */
            "Test Case T2",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "oamT2UOrg8",                              /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT2UOrg8",                              /* NAME_COL */
            "Test Case T2",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "oamT2UOrg9",                              /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT2UOrg9",                              /* NAME_COL */
            "Test Case T2",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "oamT2UOrg10",                             /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT2UOrg10",                             /* NAME_COL */
            "Test Case T2",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        }
     };

    final static String[][] ORGS_PRM_TO3 = {
         {
             "oamT3POrg1",                             /* NAME_COL */
             "Test Case T3",                           /* DESC_COL */
             "P",                                      /* TYPE COL */
             "",                                       /* RELATIONSHIP_COL */
             "",                                       /* INHERITANCE_COL */
             "T"                                       /* INHERITANCE_FLAG_COL */
         },
        {
            "oamT3POrg2",                              /* NAME_COL */
            "Test Case T3",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT3POrg3",                              /* NAME_COL */
            "Test Case T3",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT3POrg4",                              /* NAME_COL */
            "Test Case T3",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT3POrg5",                              /* NAME_COL */
            "Test Case T3",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT3POrg6",                              /* NAME_COL */
            "Test Case T3",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT3POrg7",                              /* NAME_COL */
            "Test Case T3",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT3POrg8",                              /* NAME_COL */
            "Test Case T3",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT3POrg9",                              /* NAME_COL */
            "Test Case T3",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT3POrg10",                             /* NAME_COL */
            "Test Case T3",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "T"                                        /* INHERITANCE_FLAG_COL */
        }
     };

    final static String[][] ORGS_PRM_TO4 = {
         {
             "oamT4POrg1",                             /* NAME_COL */
             "Test Case T4",                           /* DESC_COL */
             "P",                                      /* TYPE COL */
             "oamT4POrg2",                             /* RELATIONSHIP_COL */
             "",                                       /* INHERITANCE_COL */
             "A"                                       /* INHERITANCE_FLAG_COL */
         },
        {
            "oamT4POrg2",                              /* NAME_COL */
            "Test Case T4",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "oamT4POrg3",                              /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT4POrg3",                              /* NAME_COL */
            "Test Case T4",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "oamT4POrg4",                              /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT4POrg4",                              /* NAME_COL */
            "Test Case T4",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "oamT4POrg5",                              /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT4POrg5",                              /* NAME_COL */
            "Test Case T4",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "oamT4POrg6",                              /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT4POrg6",                              /* NAME_COL */
            "Test Case T4",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "oamT4POrg7",                              /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT4POrg7",                              /* NAME_COL */
            "Test Case T4",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "oamT4POrg8",                              /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT4POrg8",                              /* NAME_COL */
            "Test Case T4",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "oamT4POrg9",                              /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT4POrg9",                              /* NAME_COL */
            "Test Case T4",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "oamT4POrg10",                             /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "oamT4POrg10",                             /* NAME_COL */
            "Test Case T4",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "",                                        /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        }
     };

    public final static String[][] ORGS_USR_TO5 = {
         {
             "T5UOrg1",                                /* NAME_COL */
             "Test Case T5",                           /* DESC_COL */
             "U",                                      /* TYPE COL */
             "",                                       /* RELATIONSHIP_COL */
             "",                                       /* INHERITANCE_COL */
             "A"                                       /* INHERITANCE_FLAG_COL */
         },
        {
            "T5UOrg2",                                 /* NAME_COL */
            "Test Case T5",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "T5UOrg1",                                 /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "D"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "T5UOrg3",                                 /* NAME_COL */
            "Test Case T5",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "T5UOrg1",                                 /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "D"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "T5UOrg4",                                 /* NAME_COL */
            "Test Case T5",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "T5UOrg2",                                 /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "D"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "T5UOrg5",                                 /* NAME_COL */
            "Test Case T5",                            /* DESC_COL */
             "U",                                      /* TYPE COL */
            "T5UOrg2",                                 /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "D"                                        /* INHERITANCE_FLAG_COL */
        },
     };

    public final static String[][] ORGS_PRM_TO5 = {
         {
             "T5POrg1",                                /* NAME_COL */
             "Test Case T5",                           /* DESC_COL */
             "P",                                      /* TYPE COL */
             "",                                       /* RELATIONSHIP_COL */
             "",                                       /* INHERITANCE_COL */
             "A"                                       /* INHERITANCE_FLAG_COL */
         },
        {
            "T5POrg2",                                 /* NAME_COL */
            "Test Case T5",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "T5POrg1",                                 /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "D"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "T5POrg3",                                 /* NAME_COL */
            "Test Case T5",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "T5POrg1",                                 /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "D"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "T5POrg4",                                 /* NAME_COL */
            "Test Case T5",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "T5POrg2",                                 /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "D"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "T5POrg5",                                 /* NAME_COL */
            "Test Case T5",                            /* DESC_COL */
             "P",                                      /* TYPE COL */
            "T5POrg2",                                 /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "D"                                        /* INHERITANCE_FLAG_COL */
        },
     };
    

    final static String[][] ORGS_USR_TO6_DSC = {
         {
             "T6UOrg1",                                /* NAME_COL */
             "Test Case T6U",                          /* DESC_COL */
             "U",                                      /* TYPE COL */
             "T6UOrg2,T6UOrg3",                        /* RELATIONSHIP_COL */
             "",                                       /* INHERITANCE_COL */
             "D"                                       /* INHERITANCE_FLAG_COL */
         },
        {
            "T6UOrg2",                                 /* NAME_COL */
            "Test Case T6U",                           /* DESC_COL */
             "U",                                      /* TYPE COL */
            "T6UOrg4,T6UOrg5",                         /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "D"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "T6UOrg3",                                 /* NAME_COL */
            "Test Case T6U",                           /* DESC_COL */
             "U",                                      /* TYPE COL */
            "T6UOrg6,T6UOrg7",                         /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "D"                                        /* INHERITANCE_FLAG_COL */
        }
     };

    final static String[][] ORGS_PRM_TO6_DSC = {
         {
             "T6POrg1",                                /* NAME_COL */
             "Test Case T6P",                          /* DESC_COL */
             "P",                                      /* TYPE COL */
             "T6POrg2,T6POrg3",                        /* RELATIONSHIP_COL */
             "",                                       /* INHERITANCE_COL */
             "D"                                       /* INHERITANCE_FLAG_COL */
         },
        {
            "T6POrg2",                                 /* NAME_COL */
            "Test Case T6P",                           /* DESC_COL */
             "P",                                      /* TYPE COL */
            "T6POrg4,T6POrg5",                         /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "D"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "T6POrg3",                                 /* NAME_COL */
            "Test Case T6P",                           /* DESC_COL */
             "P",                                      /* TYPE COL */
            "T6POrg6,T6POrg7",                         /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "D"                                        /* INHERITANCE_FLAG_COL */
        }
     };

    final static String[][] ORGS_USR_TO7_ASC = {
         {
             "T7UOrg1",                                /* NAME_COL */
             "Test Case T6U",                          /* DESC_COL */
             "U",                                      /* TYPE COL */
             "T7UOrg2,T7UOrg3",                        /* RELATIONSHIP_COL */
             "",                                       /* INHERITANCE_COL */
             "C"                                       /* INHERITANCE_FLAG_COL */
         },
        {
            "T7UOrg2",                                 /* NAME_COL */
            "Test Case T2U",                           /* DESC_COL */
             "U",                                      /* TYPE COL */
            "T7UOrg4,T7UOrg5",                         /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "T7UOrg3",                                 /* NAME_COL */
            "Test Case T2U",                           /* DESC_COL */
             "U",                                      /* TYPE COL */
            "T7UOrg6,T7UOrg7",                         /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        }
     };

    final static String[][] ORGS_PRM_TO7_ASC = {
         {
             "T7POrg1",                                /* NAME_COL */
             "Test Case T6P",                          /* DESC_COL */
             "P",                                      /* TYPE COL */
             "T7POrg2,T7POrg3",                        /* RELATIONSHIP_COL */
             "",                                       /* INHERITANCE_COL */
             "C"                                       /* INHERITANCE_FLAG_COL */
         },
        {
            "T7POrg2",                                 /* NAME_COL */
            "Test Case T2P",                           /* DESC_COL */
             "P",                                      /* TYPE COL */
            "T7POrg4,T7POrg5",                         /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        },
        {
            "T7POrg3",                                 /* NAME_COL */
            "Test Case T2P",                           /* DESC_COL */
             "P",                                      /* TYPE COL */
            "T7POrg6,T7POrg7",                         /* RELATIONSHIP_COL */
            "",                                        /* INHERITANCE_COL */
            "A"                                        /* INHERITANCE_FLAG_COL */
        }
     };

     /**
     * The Fortress test data for junit uses 2-dimensional arrays.
     */
    private final static int NAME_COL = 0;
    private final static int DESC_COL = 1;
    private final static int TYPE_COL = 2;
    private final static int RELATIONSHIP_COL = 3;
    private final static int INHERITANCE_COL = 4;
    private final static int INHERITANCE_FLAG_COL = 5;


    /**
     *
     * @param ou
     * @param ole
     */
    public static void assertEquals(OrgUnit ou, String[] ole)
    {
        assertEquals(CLS_NM + ".assertEquals failed compare ou name", getName(ole), ou.getName());
        assertEquals(CLS_NM + ".assertEquals failed compare ou desc", getDescription(ole), ou.getDescription());
        assertEquals(CLS_NM + ".assertEquals failed compare ou type", getType(ole), ou.getType());
        log.debug(CLS_NM + ".assertEquals [" + ou.getName() + "] successful");
    }


    /**
     *
     * @param ole
     * @return
     */
    public static String getName(String[] ole)
    {
        return ole[NAME_COL];
    }

    /**
     *
     * @param ole
     * @return
     */
    public static String getDescription(String[] ole)
    {
        return ole[DESC_COL];
    }


    /**
     *
     * @param ole
     * @return
     */
    public static OrgUnit.Type getType(String[] ole)
    {
        OrgUnit.Type type = OrgUnit.Type.USER;
        String szType = ole[TYPE_COL];
        if(szType.equalsIgnoreCase("P"))
            type = OrgUnit.Type.PERM;
        return type;
    }


    /**
     *
     * @param ole
     * @return
     */
    public static Set<String> getRelationships(String[] ole)
    {
        Set<String> parents = null;
        if(VUtil.isNotNullOrEmpty(ole[RELATIONSHIP_COL]))
        {
            parents = new HashSet<String>();
            StringTokenizer charSetTkn = new StringTokenizer(ole[RELATIONSHIP_COL], ",");
            if (charSetTkn.countTokens() > 0)
            {
                while (charSetTkn.hasMoreTokens())
                {
                    String pRole = charSetTkn.nextToken();
                    parents.add(pRole);
                }
            }
        }
        return parents;
    }


    /**
     *
     * @param ole
     * @return
     */
    public static List<String> getRelationshipList(String[] ole)
    {
        List<String> parents = new ArrayList<String>();
        if(VUtil.isNotNullOrEmpty(ole[RELATIONSHIP_COL]))
        {
            StringTokenizer charSetTkn = new StringTokenizer(ole[RELATIONSHIP_COL], ",");
            if (charSetTkn.countTokens() > 0)
            {
                while (charSetTkn.hasMoreTokens())
                {
                    String pOrg = charSetTkn.nextToken();
                    parents.add(pOrg);
                }
            }
        }
        return parents;
    }


    /**
     *
     * @param ole
     * @return
     */
    public static Set<String> getInheritances(String[] ole)
    {
        Set<String> rels = new HashSet<String>();
        if(com.jts.fortress.util.attr.VUtil.isNotNullOrEmpty(ole[INHERITANCE_COL]))
        {
            StringTokenizer charSetTkn = new StringTokenizer(ole[INHERITANCE_COL], ",");
            if (charSetTkn.countTokens() > 0)
            {
                while (charSetTkn.hasMoreTokens())
                {
                    String pOrg = charSetTkn.nextToken();
                    rels.add(pOrg);
                }
            }
        }
        return rels;
    }

    /**
     *
     * @param ole
     * @return
     */
    public static boolean isCreate(String[] ole)
    {
        boolean isCreate = false;
        if(VUtil.isNotNullOrEmpty(ole[INHERITANCE_FLAG_COL]) && ole[INHERITANCE_FLAG_COL].equalsIgnoreCase("C"))
        {
            isCreate = true;
        }

        return isCreate;
    }

    /**
     *
     * @param ole
     * @return
     */
    public static boolean isTree(String[] ole)
    {
        boolean isTree = false;
        if(VUtil.isNotNullOrEmpty(ole[INHERITANCE_FLAG_COL]) && ole[INHERITANCE_FLAG_COL].equalsIgnoreCase("T"))
        {
            isTree = true;
        }

        return isTree;
    }

    public static OrgUnit getOrgUnit(String[] ole)
    {
        OrgUnit ou = new OrgUnit();
        ou.setName(getName(ole));
        ou.setDescription(getDescription(ole));
        ou.setType(getType(ole));
        return ou;
    }
}
