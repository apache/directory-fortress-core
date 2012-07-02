/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */
package com.jts.fortress.rbac;

import com.jts.fortress.arbac.OrgUnitTestData;
import com.jts.fortress.pwpolicy.MyAnnotation;
import com.jts.fortress.util.AlphabeticalOrder;
import com.jts.fortress.util.attr.VUtil;
import com.jts.fortress.util.time.Constraint;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Description of the Class
 * M
 *
 * @author Shawn McKinney
 * @created January 28, 2010
 */
public class UserTestData extends TestCase
{

    public final static String[][] USERS_TU0 = {
        {
            "oamTestAdminUser",           /* USERID_COL */
            "password1",                  /* PASSWORD_COL */
            "Test1",                      /* PW POLICY ATTR */
            "Test Case TU1",              /* DESC_COL */
            "fnameone",                   /* CN_COL */
            "lnameone",                   /* SN_COL */
            "oamTestAdminUser@jts.com",   /* EMAIL_COL */
            "501-555-1212",               /* PHONE_COL */
            "0000",                       /* BTIME_COL */
            "0000",                       /* ETIME_COL */
            "20090101",                   /* BDATE_COL */
            "21000101",                   /* EDATE_COL */
            "20500101",                   /* BLOCKDATE_COL */
            "20500115",                   /* ELOCKDATE_COL */
            "1234567",                    /* DAYMASK_COL */
            "DEV0",                       /* ORG_COL */
            "100",                        /* TIMEOUT_COL */
            "",                           /* ASSGND_ROLES_COL */
            "",                           /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
    };

    /**
     * Test Case TU1:
     */
    final static String[][] USERS_TU1 = {
        {
            "oamUser1",             /* USERID_COL */
            "password1",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU1",        /* DESC_COL */
            "fnameone",             /* CN_COL */
            "lnameone",             /* SN_COL */
            "oamUser1@jts.com",     /* EMAIL_COL */
            "501-555-1212",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20090101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "20500101",             /* BLOCKDATE_COL */
            "20500115",             /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV1",                 /* ORG_COL */
            "10",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser2",             /* USERID_COL */
            "password2",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU1",        /* DESC_COL */
            "fnametwo",             /* CN_COL */
            "lnametwo",             /* SN_COL */
            "oamUser1@jts.com",     /* EMAIL_COL */
            "501-555-1212",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20090101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "20500101",             /* BLOCKDATE_COL */
            "20500115",             /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV1",                 /* ORG_COL */
            "10",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser3",            /* USERID_COL */
            "password3",           /* PASSWORD_COL */
            "Test1",               /* PW POLICY ATTR */
            "Test Case TU1",       /* DESC_COL */
            "fnamethree",          /* CN_COL */
            "lnametree",           /* SN_COL */
            "oamUser1@jts.com",    /* EMAIL_COL */
            "501-555-1212",        /* PHONE_COL */
            "0000",                /* BTIME_COL */
            "0000",                /* ETIME_COL */
            "20090101",            /* BDATE_COL */
            "21000101",            /* EDATE_COL */
            "20500101",            /* BLOCKDATE_COL */
            "20500115",            /* ELOCKDATE_COL */
            "1234567",             /* DAYMASK_COL */
            "DEV1",                /* ORG_COL */
            "10",                  /* TIMEOUT_COL */
            "",                    /* ASSGND_ROLES_COL */
            "",                    /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser4",           /* USERID_COL */
            "password4",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU1",      /* DESC_COL */
            "fnamefour",          /* CN_COL */
            "lnamefour",          /* SN_COL */
            "oamUser1@jts.com",   /* EMAIL_COL */
            "501-555-1212",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20090101",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20500101",           /* BLOCKDATE_COL */
            "20500115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "10",                 /* TIMEOUT_COL */
            "",                   /* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser5",           /* USERID_COL */
            "password5",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU1",      /* DESC_COL */
            "fnamefive",          /* CN_COL */
            "lnamefive",          /* SN_COL */
            "oamUser1@jts.com",   /* EMAIL_COL */
            "501-555-1212",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20090101",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20500101",           /* BLOCKDATE_COL */
            "20500115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "10",                 /* TIMEOUT_COL */
            "",                   /* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser6",           /* USERID_COL */
            "password6",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU1",      /* DESC_COL */
            "fnamesix",           /* CN_COL */
            "lnamesix",           /* SN_COL */
            "oamUser1@jts.com",   /* EMAIL_COL */
            "501-555-1212",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20090101",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20500101",           /* BLOCKDATE_COL */
            "20500115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV2",               /* ORG_COL */
            "10",                 /* TIMEOUT_COL */
            "",                   /* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser7",           /* USERID_COL */
            "password7",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU1",      /* DESC_COL */
            "fnameseven",         /* CN_COL */
            "lnameseven",         /* SN_COL */
            "oamUser1@jts.com",   /* EMAIL_COL */
            "501-555-1212",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20090101",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20500101",           /* BLOCKDATE_COL */
            "20500115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV2",               /* ORG_COL */
            "10",                 /* TIMEOUT_COL */
            "",                   /* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser8",           /* USERID_COL */
            "password8",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU1",      /* DESC_COL */
            "fnameeight",         /* CN_COL */
            "lnameeight",         /* SN_COL */
            "oamUser1@jts.com",   /* EMAIL_COL */
            "501-555-1212",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20090101",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20500101",           /* BLOCKDATE_COL */
            "20500115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV2",               /* ORG_COL */
            "10",                 /* TIMEOUT_COL */
            "",                   /* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser9",           /* USERID_COL */
            "password9",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU1",      /* DESC_COL */
            "fnamenine",          /* CN_COL */
            "lnamenine",          /* SN_COL */
            "oamUser1@jts.com",   /* EMAIL_COL */
            "501-555-1212",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20090101",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20500101",           /* BLOCKDATE_COL */
            "20500115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV2",               /* ORG_COL */
            "10",                 /* TIMEOUT_COL */
            "",                   /* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser10",          /* USERID_COL */
            "password10",         /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU1",      /* DESC_COL */
            "fnameten",           /* CN_COL */
            "lnameten",           /* SN_COL */
            "oamUser1@jts.com",   /* EMAIL_COL */
            "501-555-1212",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20090101",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20500101",           /* BLOCKDATE_COL */
            "20500115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV2",               /* ORG_COL */
            "10",                 /* TIMEOUT_COL */
            "",                   /* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
    };

    /**
     * Test Case TU1 updated:
     */
    public final static String[][] USERS_TU1_UPD = {
        {
            "oamUser1",           /* USERID_COL */
            "passw0rd1",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU2",      /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "oamUser1UPD@jts.com",/* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                 /* TIMEOUT_COL */
            "",                   /* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser2",           /* USERID_COL */
            "passw0rd2",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU2",      /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "oamUser2UPD@jts.com",/* EMAIL_COL */
            "501-111-2222",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                 /* TIMEOUT_COL */
            "",                   /* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser3",           /* USERID_COL */
            "passw0rd3",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU2",      /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "oamUser3UPD@jts.com",/* EMAIL_COL */
            "501-111-3333",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                 /* TIMEOUT_COL */
            "",                   /* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser4",           /* USERID_COL */
            "passw0rd4",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU2",      /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "oamUser4UPD@jts.com",/* EMAIL_COL */
            "501-111-4444",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                 /* TIMEOUT_COL */
            "",                   /* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser5",           /* USERID_COL */
            "passw0rd5",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU2",      /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "oamUser5UPD@jts.com",/* EMAIL_COL */
            "501-111-5555",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                 /* TIMEOUT_COL */
            "",                   /* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser6",           /* USERID_COL */
            "passw0rd6",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU2",      /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "oamUser6UPD@jts.com",/* EMAIL_COL */
            "501-111-6666",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV2",               /* ORG_COL */
            "15",                 /* TIMEOUT_COL */
            "",                   /* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser7",           /* USERID_COL */
            "passw0rd7",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU2",      /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "oamUser7UPD@jts.com",/* EMAIL_COL */
            "501-111-7777",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV2",               /* ORG_COL */
            "15",                 /* TIMEOUT_COL */
            "",                   /* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser8",           /* USERID_COL */
            "passw0rd8",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU2",      /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "oamUser8UPD@jts.com",/* EMAIL_COL */
            "501-111-8888",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV2",               /* ORG_COL */
            "15",                 /* TIMEOUT_COL */
            "",                   /* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser9",           /* USERID_COL */
            "passw0rd9",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU2",      /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "oamUser9UPD@jts.com",/* EMAIL_COL */
            "501-111-9999",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV2",               /* ORG_COL */
            "15",                 /* TIMEOUT_COL */
            "",                   /* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamUser10",           /* USERID_COL */
            "passw0rd10",          /* PASSWORD_COL */
            "Test1",               /* PW POLICY ATTR */
            "Test Case TU2",       /* DESC_COL */
            "fnameoneupd",         /* CN_COL */
            "lnameoneupd",         /* SN_COL */
            "oamUser10UPD@jts.com",/* EMAIL_COL */
            "501-111-0000",        /* PHONE_COL */
            "0000",                /* BTIME_COL */
            "0000",                /* ETIME_COL */
            "20091001",            /* BDATE_COL */
            "21000101",            /* EDATE_COL */
            "20300101",            /* BLOCKDATE_COL */
            "20300115",            /* ELOCKDATE_COL */
            "1234567",             /* DAYMASK_COL */
            "DEV2",                /* ORG_COL */
            "15",                 /* TIMEOUT_COL */
            "",                   /* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        }
    };

    /**
     * Test Case TU2:
     */
    final static String[][] USERS_TU2 = {
        {
            "oamTU2User1",          /* USERID_COL */
            "password1",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userOne",              /* CN_COL */
            "lastOne",              /* SN_COL */
            "mimsTest2UR1@jts.com", /* EMAIL_COL */
            "501-222-1111",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV1",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User2",          /* USERID_COL */
            "password2",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userTwo",              /* CN_COL */
            "lastTwo",              /* SN_COL */
            "mimsTest2UR2@jts.com", /* EMAIL_COL */
            "501-222-2222",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV2",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User3",          /* USERID_COL */
            "password3",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userThree",            /* CN_COL */
            "lastThree",            /* SN_COL */
            "mimsTest2UR3@jts.com", /* EMAIL_COL */
            "501-222-3333",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV3",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User4",          /* USERID_COL */
            "password4",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userFour",             /* CN_COL */
            "lastFour",             /* SN_COL */
            "mimsTest2UR4@jts.com", /* EMAIL_COL */
            "501-222-4444",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV4",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User5",          /* USERID_COL */
            "password5",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userFive",             /* CN_COL */
            "lastFive",             /* SN_COL */
            "mimsTest2UR5@jts.com", /* EMAIL_COL */
            "501-222-5555",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV5",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User6",          /* USERID_COL */
            "password6",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userSix",              /* CN_COL */
            "lastSix",              /* SN_COL */
            "mimsTest2UR6@jts.com", /* EMAIL_COL */
            "501-222-6666",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV6",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User7",          /* USERID_COL */
            "password7",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userSeven",            /* CN_COL */
            "lastSeven",            /* SN_COL */
            "mimsTest2UR7@jts.com", /* EMAIL_COL */
            "501-222-7777",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV7",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User8",          /* USERID_COL */
            "password8",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userEight",            /* CN_COL */
            "lastEight",            /* SN_COL */
            "mimsTest2UR8@jts.com", /* EMAIL_COL */
            "501-222-8888",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV8",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User9",          /* USERID_COL */
            "password9",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userNine",             /* CN_COL */
            "lastNine",             /* SN_COL */
            "mimsTest2UR9@jts.com", /* EMAIL_COL */
            "501-222-9999",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV9",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User10",         /* USERID_COL */
            "password10",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userTen",              /* CN_COL */
            "lastTen",              /* SN_COL */
            "mimsTest2UR10@jts.com",/* EMAIL_COL */
            "501-222-0000",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
    };


    /**
     * Test Case TU2:
     */
    final static String[][] USERS_TU2_RST = {
        {
            "oamTU2User1",          /* USERID_COL */
            "newpasswd",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userOne",              /* CN_COL */
            "lastOne",              /* SN_COL */
            "mimsTest2UR1@jts.com", /* EMAIL_COL */
            "501-222-1111",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV1",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User2",          /* USERID_COL */
            "newpasswd",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userTwo",              /* CN_COL */
            "lastTwo",              /* SN_COL */
            "mimsTest2UR2@jts.com", /* EMAIL_COL */
            "501-222-2222",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV2",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User3",          /* USERID_COL */
            "newpasswd",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userThree",            /* CN_COL */
            "lastThree",            /* SN_COL */
            "mimsTest2UR3@jts.com", /* EMAIL_COL */
            "501-222-3333",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV3",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User4",          /* USERID_COL */
            "newpasswd",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userFour",             /* CN_COL */
            "lastFour",             /* SN_COL */
            "mimsTest2UR4@jts.com", /* EMAIL_COL */
            "501-222-4444",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV4",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User5",          /* USERID_COL */
            "newpasswd",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userFive",             /* CN_COL */
            "lastFive",             /* SN_COL */
            "mimsTest2UR5@jts.com", /* EMAIL_COL */
            "501-222-5555",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV5",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User6",          /* USERID_COL */
            "newpasswd",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userSix",              /* CN_COL */
            "lastSix",              /* SN_COL */
            "mimsTest2UR6@jts.com", /* EMAIL_COL */
            "501-222-6666",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV6",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User7",          /* USERID_COL */
            "newpasswd",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userSeven",            /* CN_COL */
            "lastSeven",            /* SN_COL */
            "mimsTest2UR7@jts.com", /* EMAIL_COL */
            "501-222-7777",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV7",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User8",          /* USERID_COL */
            "newpasswd",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userEight",            /* CN_COL */
            "lastEight",            /* SN_COL */
            "mimsTest2UR8@jts.com", /* EMAIL_COL */
            "501-222-8888",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV8",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User9",          /* USERID_COL */
            "newpasswd",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userNine",             /* CN_COL */
            "lastNine",             /* SN_COL */
            "mimsTest2UR9@jts.com", /* EMAIL_COL */
            "501-222-9999",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV9",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User10",         /* USERID_COL */
            "newpasswd",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userTen",              /* CN_COL */
            "lastTen",              /* SN_COL */
            "mimsTest2UR10@jts.com",/* EMAIL_COL */
            "501-222-0000",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
    };


    public final static String[][] USERS_TU2_CHG = {
        {
            "oamTU2User1",          /* USERID_COL */
            "passw0rd1",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userOne",              /* CN_COL */
            "lastOne",              /* SN_COL */
            "mimsTest2UR1@jts.com", /* EMAIL_COL */
            "501-222-1111",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV1",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User2",          /* USERID_COL */
            "passw0rd2",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userTwo",              /* CN_COL */
            "lastTwo",              /* SN_COL */
            "mimsTest2UR2@jts.com", /* EMAIL_COL */
            "501-222-2222",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV2",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User3",          /* USERID_COL */
            "passw0rd3",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userThree",            /* CN_COL */
            "lastThree",            /* SN_COL */
            "mimsTest2UR3@jts.com", /* EMAIL_COL */
            "501-222-3333",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV3",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User4",          /* USERID_COL */
            "passw0rd4",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userFour",             /* CN_COL */
            "lastFour",             /* SN_COL */
            "mimsTest2UR4@jts.com", /* EMAIL_COL */
            "501-222-4444",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV4",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User5",          /* USERID_COL */
            "passw0rd5",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userFive",             /* CN_COL */
            "lastFive",             /* SN_COL */
            "mimsTest2UR5@jts.com", /* EMAIL_COL */
            "501-222-5555",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV5",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User6",          /* USERID_COL */
            "passw0rd6",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userSix",              /* CN_COL */
            "lastSix",              /* SN_COL */
            "mimsTest2UR6@jts.com", /* EMAIL_COL */
            "501-222-6666",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV6",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User7",          /* USERID_COL */
            "passw0rd7",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userSeven",            /* CN_COL */
            "lastSeven",            /* SN_COL */
            "mimsTest2UR7@jts.com", /* EMAIL_COL */
            "501-222-7777",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV7",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User8",          /* USERID_COL */
            "passw0rd8",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userEight",            /* CN_COL */
            "lastEight",            /* SN_COL */
            "mimsTest2UR8@jts.com", /* EMAIL_COL */
            "501-222-8888",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV8",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User9",          /* USERID_COL */
            "passw0rd9",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userNine",             /* CN_COL */
            "lastNine",             /* SN_COL */
            "mimsTest2UR9@jts.com", /* EMAIL_COL */
            "501-222-9999",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV9",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU2User10",          /* USERID_COL */
            "passw0rd1-",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU2",        /* DESC_COL */
            "userTen",              /* CN_COL */
            "lastTen",              /* SN_COL */
            "mimsTest2UR10@jts.com",/* EMAIL_COL */
            "501-222-0000",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
    };


    // Test Case TU3:
    public final static String[][] USERS_TU3 = {
        {
            "oamTU3User1",          /* USERID_COL */
            "password1",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU3",        /* DESC_COL */
            "userOne",              /* CN_COL */
            "lastOne",              /* SN_COL */
            "mimsTest3UR1@jts.com", /* EMAIL_COL */
            "501-555-1111",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV1",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU3User2",          /* USERID_COL */
            "password2",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU3",        /* DESC_COL */
            "userTwo",              /* CN_COL */
            "lastTwo",              /* SN_COL */
            "mimsTest3UR2@jts.com", /* EMAIL_COL */
            "501-555-2222",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV2",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU3User3",          /* USERID_COL */
            "password3",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU3",        /* DESC_COL */
            "userThree",            /* CN_COL */
            "lastThree",            /* SN_COL */
            "mimsTest3UR3@jts.com", /* EMAIL_COL */
            "501-555-3333",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV3",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU3User4",          /* USERID_COL */
            "password4",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU3",        /* DESC_COL */
            "userFour",             /* CN_COL */
            "lastFour",             /* SN_COL */
            "mimsTest3UR4@jts.com", /* EMAIL_COL */
            "501-555-4444",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV4",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU3User5",          /* USERID_COL */
            "password5",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU3",        /* DESC_COL */
            "userFive",             /* CN_COL */
            "lastFive",             /* SN_COL */
            "mimsTest3UR5@jts.com", /* EMAIL_COL */
            "501-555-5555",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV5",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU3User6",          /* USERID_COL */
            "password6",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU3",        /* DESC_COL */
            "userSix",              /* CN_COL */
            "lastSix",              /* SN_COL */
            "mimsTest3UR6@jts.com", /* EMAIL_COL */
            "501-555-6666",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV6",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU3User7",          /* USERID_COL */
            "password7",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU3",        /* DESC_COL */
            "userSeven",            /* CN_COL */
            "lastSeven",            /* SN_COL */
            "mimsTest3UR7@jts.com", /* EMAIL_COL */
            "501-555-7777",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV7",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU3User8",          /* USERID_COL */
            "password8",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU3",        /* DESC_COL */
            "userEight",            /* CN_COL */
            "lastEight",            /* SN_COL */
            "mimsTest3UR8@jts.com", /* EMAIL_COL */
            "501-555-8888",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV8",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU3User9",          /* USERID_COL */
            "password9",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU3",        /* DESC_COL */
            "userNine",             /* CN_COL */
            "lastNine",             /* SN_COL */
            "mimsTest3UR9@jts.com", /* EMAIL_COL */
            "501-555-9999",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV9",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU3User10",         /* USERID_COL */
            "password10",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU3",        /* DESC_COL */
            "userTen",              /* CN_COL */
            "lastTen",              /* SN_COL */
            "mimsTest3UR10@jts.com",/* EMAIL_COL */
            "501-555-0000",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
    };


    // Test Case TU4:
    public final static String[][] USERS_TU4 = {
        {
            "oamTU4User1",          /* USERID_COL */
            "password1",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU4",        /* DESC_COL */
            "userOne",              /* CN_COL */
            "lastOne",              /* SN_COL */
            "mimsTest4UR1@jts.com", /* EMAIL_COL */
            "501-555-1111",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV1",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU4User2",          /* USERID_COL */
            "password2",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU4",        /* DESC_COL */
            "userTwo",              /* CN_COL */
            "lastTwo",              /* SN_COL */
            "mimsTest4UR2@jts.com", /* EMAIL_COL */
            "501-555-2222",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV2",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU4User3",          /* USERID_COL */
            "password3",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU4",        /* DESC_COL */
            "userThree",            /* CN_COL */
            "lastThree",            /* SN_COL */
            "mimsTest4UR3@jts.com", /* EMAIL_COL */
            "501-555-3333",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV3",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU4User4",          /* USERID_COL */
            "password4",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU4",        /* DESC_COL */
            "userFour",             /* CN_COL */
            "lastFour",             /* SN_COL */
            "mimsTest4UR4@jts.com", /* EMAIL_COL */
            "501-555-4444",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV4",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU4User5",          /* USERID_COL */
            "password5",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU4",        /* DESC_COL */
            "userFive",             /* CN_COL */
            "lastFive",             /* SN_COL */
            "mimsTest4UR5@jts.com", /* EMAIL_COL */
            "501-555-5555",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV5",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU4User6",          /* USERID_COL */
            "password6",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU4",        /* DESC_COL */
            "userSix",              /* CN_COL */
            "lastSix",              /* SN_COL */
            "mimsTest4UR6@jts.com", /* EMAIL_COL */
            "501-555-6666",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV6",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU4User7",          /* USERID_COL */
            "password7",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU4",        /* DESC_COL */
            "userSeven",            /* CN_COL */
            "lastSeven",            /* SN_COL */
            "mimsTest4UR7@jts.com", /* EMAIL_COL */
            "501-555-7777",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV7",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU4User8",          /* USERID_COL */
            "password8",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU4",        /* DESC_COL */
            "userEight",            /* CN_COL */
            "lastEight",            /* SN_COL */
            "mimsTest4UR8@jts.com", /* EMAIL_COL */
            "501-555-8888",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV8",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU4User9",          /* USERID_COL */
            "password9",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU4",        /* DESC_COL */
            "userNine",             /* CN_COL */
            "lastNine",             /* SN_COL */
            "mimsTest4UR9@jts.com", /* EMAIL_COL */
            "501-555-9999",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV9",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU4User10",         /* USERID_COL */
            "password10",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU4",        /* DESC_COL */
            "userTen",              /* CN_COL */
            "lastTen",              /* SN_COL */
            "mimsTest4UR10@jts.com",/* EMAIL_COL */
            "501-555-0000",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
    };


    // Test Case TU5:
    @MyAnnotation(name = "USERS_TU5", value = "USR TU5")
    final public static String[][] USERS_TU5 = {
        {
            "oamTU5User1",          /* USERID_COL */
            "password1",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userOne",              /* CN_COL */
            "lastOne",              /* SN_COL */
            "mimsTest5UR1@jts.com", /* EMAIL_COL */
            "501-555-1111",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV1",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User2",          /* USERID_COL */
            "password2",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userTwo",              /* CN_COL */
            "lastTwo",              /* SN_COL */
            "mimsTest5UR2@jts.com", /* EMAIL_COL */
            "501-555-2222",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV2",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User3",          /* USERID_COL */
            "password3",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userThree",            /* CN_COL */
            "lastThree",            /* SN_COL */
            "mimsTest5UR3@jts.com", /* EMAIL_COL */
            "501-555-3333",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV3",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User4",          /* USERID_COL */
            "password4",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userFour",             /* CN_COL */
            "lastFour",             /* SN_COL */
            "mimsTest5UR4@jts.com", /* EMAIL_COL */
            "501-555-4444",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV4",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User5",          /* USERID_COL */
            "password5",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userFive",             /* CN_COL */
            "lastFive",             /* SN_COL */
            "mimsTest5UR5@jts.com", /* EMAIL_COL */
            "501-555-5555",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV5",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User6",          /* USERID_COL */
            "password6",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userSix",              /* CN_COL */
            "lastSix",              /* SN_COL */
            "mimsTest5UR6@jts.com", /* EMAIL_COL */
            "501-555-6666",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV6",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User7",          /* USERID_COL */
            "password7minlength",   /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userSeven",            /* CN_COL */
            "lastSeven",            /* SN_COL */
            "mimsTest5UR7@jts.com", /* EMAIL_COL */
            "501-555-7777",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV7",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User8",          /* USERID_COL */
            "password8",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userEight",            /* CN_COL */
            "lastEight",            /* SN_COL */
            "mimsTest5UR8@jts.com", /* EMAIL_COL */
            "501-555-8888",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV8",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User9",          /* USERID_COL */
            "password9",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userNine",             /* CN_COL */
            "lastNine",             /* SN_COL */
            "mimsTest5UR9@jts.com", /* EMAIL_COL */
            "501-555-9999",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV9",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User10",         /* USERID_COL */
            "password10",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userTen",              /* CN_COL */
            "lastTen",              /* SN_COL */
            "mimsTest5UR10@jts.com",/* EMAIL_COL */
            "501-555-1010",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User11",         /* USERID_COL */
            "password11",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userEleven",           /* CN_COL */
            "lastEleven",           /* SN_COL */
            "mimsTest5UR11@jts.com",/* EMAIL_COL */
            "501-555-1111",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User12",         /* USERID_COL */
            "password12",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userTwelve",           /* CN_COL */
            "lastTwelve",           /* SN_COL */
            "mimsTest5UR12@jts.com",/* EMAIL_COL */
            "501-555-1212",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User13",         /* USERID_COL */
            "password13",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userThirteen",         /* CN_COL */
            "lastThirteen",         /* SN_COL */
            "mimsTest5UR13@jts.com",/* EMAIL_COL */
            "501-555-1313",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User14",         /* USERID_COL */
            "password14",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userFourteen",         /* CN_COL */
            "lastFourteen",         /* SN_COL */
            "mimsTest5UR14@jts.com",/* EMAIL_COL */
            "501-555-1414",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User15",         /* USERID_COL */
            "password15",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userFifteen",          /* CN_COL */
            "lastFifteen",          /* SN_COL */
            "mimsTest5UR15@jts.com",/* EMAIL_COL */
            "501-555-1515",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User16",         /* USERID_COL */
            "password16",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userSixteen",          /* CN_COL */
            "lastSixteen",          /* SN_COL */
            "mimsTest5UR16@jts.com",/* EMAIL_COL */
            "501-555-1616",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User17",         /* USERID_COL */
            "password17",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userSeventeen",        /* CN_COL */
            "lastSeventeen",        /* SN_COL */
            "mimsTest5UR17@jts.com",/* EMAIL_COL */
            "501-555-1717",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User18",         /* USERID_COL */
            "password18",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userEighteen",         /* CN_COL */
            "lastEighteen",         /* SN_COL */
            "mimsTest5UR18@jts.com",/* EMAIL_COL */
            "501-555-1818",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User19",         /* USERID_COL */
            "password19",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userNineteen",         /* CN_COL */
            "lastNineteen",         /* SN_COL */
            "mimsTest5UR19@jts.com",/* EMAIL_COL */
            "501-555-1919",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User20",         /* USERID_COL */
            "password20",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userTwenty",           /* CN_COL */
            "lastTwenty",           /* SN_COL */
            "mimsTest5UR20@jts.com",/* EMAIL_COL */
            "501-555-2020",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User21",         /* USERID_COL */
            "password21",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userTwentyone",        /* CN_COL */
            "lastTwentyone",        /* SN_COL */
            "mimsTest5UR21@jts.com",/* EMAIL_COL */
            "501-555-2121",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User22",         /* USERID_COL */
            "password22",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userTwentytwo",        /* CN_COL */
            "lastTwentytwo",        /* SN_COL */
            "mimsTest5UR22@jts.com",/* EMAIL_COL */
            "501-555-2222",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User23",         /* USERID_COL */
            "password23",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userTwentythree",      /* CN_COL */
            "lastTwentythree",      /* SN_COL */
            "mimsTest5UR23@jts.com",/* EMAIL_COL */
            "501-555-2323",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User24",         /* USERID_COL */
            "password24",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userTwentyfour",       /* CN_COL */
            "lastTwentyfour",       /* SN_COL */
            "mimsTest5UR24@jts.com",/* EMAIL_COL */
            "501-555-2424",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User25",         /* USERID_COL */
            "password25",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userTwentyfive",       /* CN_COL */
            "lastTwentyfive",       /* SN_COL */
            "mimsTest5UR25@jts.com",/* EMAIL_COL */
            "501-555-2525",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User26",         /* USERID_COL */
            "password26",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userTwentysix",        /* CN_COL */
            "lastTwentysix",        /* SN_COL */
            "mimsTest5UR26@jts.com",/* EMAIL_COL */
            "501-555-2626",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        }
    };

    // Test Case TU5:
    @MyAnnotation(name = "USERS_TU5_UPD", value = "USR TU5_UPD")
    final public static String[][] USERS_TU5_UPD = {
        {
            "oamTU5User1",          /* USERID_COL */
            "passw0rd1",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userOne",              /* CN_COL */
            "lastOne",              /* SN_COL */
            "mimsTest5UR1@jts.com", /* EMAIL_COL */
            "501-555-1111",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV1",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User2",          /* USERID_COL */
            "passw0rd2",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userTwo",              /* CN_COL */
            "lastTwo",              /* SN_COL */
            "mimsTest5UR2@jts.com", /* EMAIL_COL */
            "501-555-2222",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV2",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User3",          /* USERID_COL */
            "passw0rd3",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userThree",            /* CN_COL */
            "lastThree",            /* SN_COL */
            "mimsTest5UR3@jts.com", /* EMAIL_COL */
            "501-555-3333",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV3",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User4",          /* USERID_COL */
            "passw0rd4",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userFour",             /* CN_COL */
            "lastFour",             /* SN_COL */
            "mimsTest5UR4@jts.com", /* EMAIL_COL */
            "501-555-4444",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV4",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User5",          /* USERID_COL */
            "password5",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userFive",             /* CN_COL */
            "lastFive",             /* SN_COL */
            "mimsTest5UR5@jts.com", /* EMAIL_COL */
            "501-555-5555",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV5",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User6",          /* USERID_COL */
            "password6",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userSix",              /* CN_COL */
            "lastSix",              /* SN_COL */
            "mimsTest5UR6@jts.com", /* EMAIL_COL */
            "501-555-6666",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV6",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User7",          /* USERID_COL */
            "password7",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userSeven",            /* CN_COL */
            "lastSeven",            /* SN_COL */
            "mimsTest5UR7@jts.com", /* EMAIL_COL */
            "501-555-7777",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV7",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User8",          /* USERID_COL */
            "password8",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userEight",            /* CN_COL */
            "lastEight",            /* SN_COL */
            "mimsTest5UR8@jts.com", /* EMAIL_COL */
            "501-555-8888",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV8",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User9",          /* USERID_COL */
            "password9",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userNine",             /* CN_COL */
            "lastNine",             /* SN_COL */
            "mimsTest5UR9@jts.com", /* EMAIL_COL */
            "501-555-9999",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV9",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU5User10",         /* USERID_COL */
            "password10",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU5",        /* DESC_COL */
            "userTen",              /* CN_COL */
            "lastTen",              /* SN_COL */
            "mimsTest5UR10@jts.com",/* EMAIL_COL */
            "501-555-0000",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
    };

    // Test Case TU6, these are system users setup in Fortress properties to disallow deletion:
    @MyAnnotation(name = "USERS_TU6_SYS", value = "USR TU6_SYS")
    final public static String[][] USERS_TU6 = {
        {
            "oamTU6User1",          /* USERID_COL */
            "password1",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU6",        /* DESC_COL */
            "userOne",              /* CN_COL */
            "lastOne",              /* SN_COL */
            "mimsTest6UR1@jts.com", /* EMAIL_COL */
            "501-555-1111",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV1",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
            "TRUE",                 /* SYSTEM USER */
        },
        {
            "oamTU6User2",          /* USERID_COL */
            "password2",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU6",        /* DESC_COL */
            "userTwo",              /* CN_COL */
            "lastTwo",              /* SN_COL */
            "mimsTest6UR2@jts.com", /* EMAIL_COL */
            "501-555-2222",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV1",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
            "TRUE",                 /* SYSTEM USER */
        },
        {
            "oamTU6User3",          /* USERID_COL */
            "password3",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU6",        /* DESC_COL */
            "userThree",            /* CN_COL */
            "lastThree",            /* SN_COL */
            "mimsTest6UR3@jts.com", /* EMAIL_COL */
            "501-555-3333",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV1",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
            "TRUE",                 /* SYSTEM USER */
        },
        {
            "oamTU6User4",          /* USERID_COL */
            "password4",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU6",        /* DESC_COL */
            "userFour",             /* CN_COL */
            "lastFour",             /* SN_COL */
            "mimsTest6UR4@jts.com", /* EMAIL_COL */
            "501-555-4444",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV1",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
            "TRUE",                 /* SYSTEM USER */
        },
        {
            "oamTU6User5",          /* USERID_COL */
            "password5",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU6",        /* DESC_COL */
            "userFive",             /* CN_COL */
            "lastFive",             /* SN_COL */
            "mimsTest6UR5@jts.com", /* EMAIL_COL */
            "501-555-5555",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV1",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
            "TRUE",                 /* SYSTEM USER */
        }
    };


    // Test Case TU7:
    @MyAnnotation(name = "USERS_TU7_HIER", value = "USR TU7_HIER")
    final public static String[][] USERS_TU7_HIER = {
        {
            "oamTU7User1",          /* USERID_COL */
            "passw0rd1",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU7",        /* DESC_COL */
            "userOne",              /* CN_COL */
            "lastOne",              /* SN_COL */
            "mimsTest7UR1@jts.com", /* EMAIL_COL */
            "501-555-1111",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV1",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU7User2",          /* USERID_COL */
            "passw0rd2",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU7",        /* DESC_COL */
            "userTwo",              /* CN_COL */
            "lastTwo",              /* SN_COL */
            "mimsTest7UR2@jts.com", /* EMAIL_COL */
            "501-555-2222",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV2",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU7User3",          /* USERID_COL */
            "passw0rd3",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU7",        /* DESC_COL */
            "userThree",            /* CN_COL */
            "lastThree",            /* SN_COL */
            "mimsTest7UR3@jts.com", /* EMAIL_COL */
            "501-555-3333",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV3",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU7User4",          /* USERID_COL */
            "passw0rd4",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU7",        /* DESC_COL */
            "userFour",             /* CN_COL */
            "lastFour",             /* SN_COL */
            "mimsTest7UR4@jts.com", /* EMAIL_COL */
            "501-555-4444",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV4",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU7User5",          /* USERID_COL */
            "password5",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU7",        /* DESC_COL */
            "userFive",             /* CN_COL */
            "lastFive",             /* SN_COL */
            "mimsTest7UR5@jts.com", /* EMAIL_COL */
            "501-555-5555",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV5",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU7User6",          /* USERID_COL */
            "password6",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU7",        /* DESC_COL */
            "userSix",              /* CN_COL */
            "lastSix",              /* SN_COL */
            "mimsTest7UR6@jts.com", /* EMAIL_COL */
            "501-555-6666",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV6",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU7User7",          /* USERID_COL */
            "password7",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU7",        /* DESC_COL */
            "userSeven",            /* CN_COL */
            "lastSeven",            /* SN_COL */
            "mimsTest7UR7@jts.com", /* EMAIL_COL */
            "501-555-7777",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV7",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU7User8",          /* USERID_COL */
            "password8",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU7",        /* DESC_COL */
            "userEight",            /* CN_COL */
            "lastEight",            /* SN_COL */
            "mimsTest7UR8@jts.com", /* EMAIL_COL */
            "501-555-8888",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV8",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU7User9",          /* USERID_COL */
            "password9",            /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU7",        /* DESC_COL */
            "userNine",             /* CN_COL */
            "lastNine",             /* SN_COL */
            "mimsTest7UR9@jts.com", /* EMAIL_COL */
            "501-555-9999",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV9",                 /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU7User10",         /* USERID_COL */
            "password10",           /* PASSWORD_COL */
            "Test1",                /* PW POLICY ATTR */
            "Test Case TU7",        /* DESC_COL */
            "userTen",              /* CN_COL */
            "lastTen",              /* SN_COL */
            "mimsTest7UR10@jts.com",/* EMAIL_COL */
            "501-555-0000",         /* PHONE_COL */
            "0000",                 /* BTIME_COL */
            "0000",                 /* ETIME_COL */
            "20100101",             /* BDATE_COL */
            "21000101",             /* EDATE_COL */
            "none",                 /* BLOCKDATE_COL */
            "none",                 /* ELOCKDATE_COL */
            "1234567",              /* DAYMASK_COL */
            "DEV10",                /* ORG_COL */
            "30",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
    };

    // Test Case TU8:
    @MyAnnotation(name = "USERS_TU8_SSD", value = "USR TU8_SSD")
    final static String[][] USERS_TU8_SSD = {
        {
            "oamTU8User1",        /* USERID_COL */
            "passw0rd1",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU8",      /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU8@jts.com",  /* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU8User2",        /* USERID_COL */
            "passw0rd2",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU8",      /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU2TU8@jts.com",  /* EMAIL_COL */
            "501-111-2222",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU8User3",        /* USERID_COL */
            "passw0rd3",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU8",      /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU3TU8@jts.com",  /* EMAIL_COL */
            "501-111-3333",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU8User4",        /* USERID_COL */
            "passw0rd4",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU8",      /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU4TU8@jts.com",  /* EMAIL_COL */
            "501-111-3333",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        }
    };

    // Test Case TU8:
    @MyAnnotation(name = "USERS_TU9_SSD_HIER", value = "USR TU9_SSD_HIER")
    final static String[][] USERS_TU9_SSD_HIER = {
        {
            "oamTU9User1",        /* USERID_COL */
            "passw0rd1",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU9",      /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU9@jts.com",  /* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU9User2",        /* USERID_COL */
            "passw0rd2",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU9",      /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU2TU9@jts.com",  /* EMAIL_COL */
            "501-111-2222",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU9User3",        /* USERID_COL */
            "passw0rd3",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU9",      /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU3TU8@jts.com",  /* EMAIL_COL */
            "501-111-3333",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        }
    };
    // Test Case TU10:
    @MyAnnotation(name = "USERS_TU10_SSD_HIER", value = "USR TU10_SSD_HIER")
    final static String[][] USERS_TU10_SSD_HIER = {
        {
            "oamTU10User1",       /* USERID_COL */
            "passw0rd1",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU10",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU10@jts.com", /* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU10User2",       /* USERID_COL */
            "passw0rd2",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU10",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU2TU10@jts.com", /* EMAIL_COL */
            "501-111-2222",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU10User3",       /* USERID_COL */
            "passw0rd3",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU10",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU3TU10@jts.com", /* EMAIL_COL */
            "501-111-3333",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        }
    };
    // Test Case TU11:
    @MyAnnotation(name = "USERS_TU11_SSD_HIER", value = "USR TU11_SSD_HIER")
    final static String[][] USERS_TU11_SSD_HIER = {
        {
            "oamTU11User1",       /* USERID_COL */
            "passw0rd1",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU11",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU11@jts.com", /* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU11User2",       /* USERID_COL */
            "passw0rd2",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU11",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU2TU11@jts.com", /* EMAIL_COL */
            "501-111-2222",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU11User3",       /* USERID_COL */
            "passw0rd3",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU11",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU3TU11@jts.com", /* EMAIL_COL */
            "501-111-3333",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        }
    };


    // Test Case TU12:
    @MyAnnotation(name = "USERS_TU12_DSD", value = "USR TU12_DSD")
    final static String[][] USERS_TU12_DSD = {
        {
            "oamTU12User1",       /* USERID_COL */
            "passw0rd1",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU12",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU12@jts.com", /* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU12User2",       /* USERID_COL */
            "passw0rd2",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU12",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU2TU12@jts.com", /* EMAIL_COL */
            "501-111-2222",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU12User3",       /* USERID_COL */
            "passw0rd3",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU12",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU3TU12@jts.com", /* EMAIL_COL */
            "501-111-3333",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU12User4",       /* USERID_COL */
            "passw0rd4",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU12",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU4TU12@jts.com", /* EMAIL_COL */
            "501-111-3333",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        }
    };

    // Test Case TU13:
    @MyAnnotation(name = "USERS_TU13_DSD_HIER", value = "USR TU13_DSD_HIER")
    final static String[][] USERS_TU13_DSD_HIER = {
        {
            "oamTU13User1",       /* USERID_COL */
            "passw0rd1",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU13",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU13@jts.com", /* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU13User2",       /* USERID_COL */
            "passw0rd2",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU13",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU2TU13@jts.com", /* EMAIL_COL */
            "501-111-2222",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU13User3",       /* USERID_COL */
            "passw0rd3",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU13",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU3TU13@jts.com", /* EMAIL_COL */
            "501-111-3333",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        }
    };
    // Test Case TU14:
    @MyAnnotation(name = "USERS_TU14_DSD_HIER", value = "USR TU14_DSD_HIER")
    final static String[][] USERS_TU14_DSD_HIER = {
        {
            "oamTU14User1",       /* USERID_COL */
            "passw0rd1",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU14",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU14@jts.com", /* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU14User2",       /* USERID_COL */
            "passw0rd2",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU14",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU2TU14@jts.com", /* EMAIL_COL */
            "501-111-2222",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU14User3",       /* USERID_COL */
            "passw0rd3",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU14",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU3TU14@jts.com", /* EMAIL_COL */
            "501-111-3333",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        }
    };
    // Test Case TU15:
    @MyAnnotation(name = "USERS_TU15_DSD_HIER", value = "USR TU15_DSD_HIER")
    final static String[][] USERS_TU15_DSD_HIER = {
        {
            "oamTU15User1",       /* USERID_COL */
            "passw0rd1",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU15",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU15@jts.com", /* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU15User2",       /* USERID_COL */
            "passw0rd2",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU15",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU2TU15@jts.com", /* EMAIL_COL */
            "501-111-2222",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU15User3",       /* USERID_COL */
            "passw0rd3",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU15",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU3TU15@jts.com", /* EMAIL_COL */
            "501-111-3333",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        }
    };

    // Test Case TU16:
    @MyAnnotation(name = "USERS_TU16_ARBAC", value = "USR TU16_ARBAC")
    public final static String[][] USERS_TU16_ARBAC = {
        {
            "oamTU16User1",       /* USERID_COL */
            "passw0rd1",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU16@jts.com", /* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16User2",       /* USERID_COL */
            "passw0rd2",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU2TU16@jts.com", /* EMAIL_COL */
            "501-111-2222",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16User3",       /* USERID_COL */
            "passw0rd3",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU3TU16@jts.com", /* EMAIL_COL */
            "501-111-3333",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16User4",       /* USERID_COL */
            "passw0rd4",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU4TU16@jts.com", /* EMAIL_COL */
            "501-111-4444",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16User5",       /* USERID_COL */
            "passw0rd5",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU5TU16@jts.com", /* EMAIL_COL */
            "501-111-5555",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16User6",       /* USERID_COL */
            "passw0rd6",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU6TU16@jts.com", /* EMAIL_COL */
            "501-111-6666",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16User7",       /* USERID_COL */
            "passw0rd7",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU7TU16@jts.com", /* EMAIL_COL */
            "501-111-7777",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16User8",       /* USERID_COL */
            "passw0rd8",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU8TU16@jts.com", /* EMAIL_COL */
            "501-111-8888",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16User9",       /* USERID_COL */
            "passw0rd9",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU9TU16@jts.com", /* EMAIL_COL */
            "501-111-9999",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16User10",      /* USERID_COL */
            "passw0rd10",         /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU10TU16@jts.com",/* EMAIL_COL */
            "501-111-0000",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        }
    };

    // Test Case TU16B:
    @MyAnnotation(name = "USERS_TU16B_ARBAC", value = "USR TU16B_ARBAC")
    public final static String[][] USERS_TU16B_ARBAC = {
        {
            "oamTU16BUser1",      /* USERID_COL */
            "passw0rd1",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16B",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU16B@jts.com",/* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "oamT1UOrg1",         /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16BUser2",      /* USERID_COL */
            "passw0rd2",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16B",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU2TU16B@jts.com",/* EMAIL_COL */
            "501-111-2222",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "oamT1UOrg2",         /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16BUser3",      /* USERID_COL */
            "passw0rd3",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16B",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU3TU16B@jts.com",/* EMAIL_COL */
            "501-111-3333",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "oamT1UOrg3",         /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16BUser4",      /* USERID_COL */
            "passw0rd4",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16B",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU4TU16B@jts.com",/* EMAIL_COL */
            "501-111-4444",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "oamT1UOrg4",         /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16BUser5",      /* USERID_COL */
            "passw0rd5",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16B",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU5TU16B@jts.com",/* EMAIL_COL */
            "501-111-5555",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "oamT1UOrg5",         /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16BUser6",      /* USERID_COL */
            "passw0rd6",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16B",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU6TU16B@jts.com",/* EMAIL_COL */
            "501-111-6666",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "oamT1UOrg6",         /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16BUser7",      /* USERID_COL */
            "passw0rd7",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16B",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU7TU16B@jts.com",/* EMAIL_COL */
            "501-111-7777",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "oamT1UOrg7",         /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16BUser8",      /* USERID_COL */
            "passw0rd8",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16B",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU8TU16B@jts.com",/* EMAIL_COL */
            "501-111-8888",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "oamT1UOrg8",         /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16BUser9",      /* USERID_COL */
            "passw0rd9",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16B",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU9TU16B@jts.com",/* EMAIL_COL */
            "501-111-9999",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "oamT1UOrg9",         /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU16BUser10",     /* USERID_COL */
            "passw0rd10",         /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU16B",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU10TU16B@jts.com",/* EMAIL_COL */
            "501-111-0000",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "oamT1UOrg10",        /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        }
    };

    // Test Case TU17A:
    @MyAnnotation(name = "USERS_TU17A_ARBAC", value = "USR TU17A_ARBAC")
    public final static String[][] USERS_TU17A_ARBAC = {
        {
            "oamTU17AUser1",      /* USERID_COL */
            "passw0rd1",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU17A",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU17A@jts.com",/* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU17AUser2",      /* USERID_COL */
            "passw0rd2",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU17A",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU17A@jts.com",/* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU17AUser3",      /* USERID_COL */
            "passw0rd3",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU17A",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU17A@jts.com",/* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU17AUser4",      /* USERID_COL */
            "passw0rd4",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU17A",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU17A@jts.com",/* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU17AUser5",      /* USERID_COL */
            "passw0rd5",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU17A",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU17A@jts.com",/* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        }
    };

    // Test Case TU17U:
    @MyAnnotation(name = "USERS_TU17U_ARBAC", value = "USR TU17U_ARBAC")
    public final static String[][] USERS_TU17U_ARBAC = {
        {
            "oamTU17UUser1",      /* USERID_COL */
            "passw0rd1",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU17U",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU17U@jts.com",/* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            OrgUnitTestData.getName(OrgUnitTestData.ORGS_USR_TO5[0]),
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU17UUser2",      /* USERID_COL */
            "passw0rd2",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU17U",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU17U@jts.com",/* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            OrgUnitTestData.getName(OrgUnitTestData.ORGS_USR_TO5[1]),
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU17UUser3",      /* USERID_COL */
            "passw0rd3",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU17U",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU17U@jts.com",/* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            OrgUnitTestData.getName(OrgUnitTestData.ORGS_USR_TO5[2]),
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU17UUser4",      /* USERID_COL */
            "passw0rd4",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU17U",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU17U@jts.com",/* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            OrgUnitTestData.getName(OrgUnitTestData.ORGS_USR_TO5[3]),
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU17UUser5",      /* USERID_COL */
            "passw0rd5",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU17U",    /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "mimsU1TU17U@jts.com",/* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            OrgUnitTestData.getName(OrgUnitTestData.ORGS_USR_TO5[4]),
            "15",                   /* TIMEOUT_COL */
            "",                     /* ASSGND_ROLES_COL */
            "",                     /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        }
    };

    // Test Case TU18U:
    @MyAnnotation(name = "USERS_TU18U_TR6_DESC", value = "USR TU18U TR6 DESC")
    public final static String[][] USERS_TU18U_TR6_DESC = {
        {
            "oamTU18User1",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU18U TR6_DESC", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR1TU18U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT6A1",                  /* ASSGND_ROLES_COL */
            "",                         /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU18User2",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU18U TR6_DESC", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR2TU18U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT6B1A1",                /* ASSGND_ROLES_COL */
            "oamT6A1,",                 /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU18User3",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU18U TR6_DESC", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR3TU18U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT6B2A1",                /* ASSGND_ROLES_COL */
            "oamT6A1,",                 /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU18User4",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU18U TR6_DESC", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR4TU18U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT6C1B1A1",              /* ASSGND_ROLES_COL */
            "oamT6A1,oamT6B1A1",        /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU18User5",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU18U TR6_DESC", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR5TU18U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT6C2B1A1",              /* ASSGND_ROLES_COL */
            "oamT6A1,oamT6B1A1,oamT6B2A1",/* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU18User6",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU18U TR6_DESC", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR6TU18U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT6C3B2A1",              /* ASSGND_ROLES_COL */
            "oamT6A1,oamT6B2A1",        /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU18User7",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU18U TR6_DESC", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR7TU18U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT6C4B2A1",              /* ASSGND_ROLES_COL */
            "oamT6A1,oamT6B2A1",        /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU18User8",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU18U TR6_DESC", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR8TU18U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT6D1C1B1A1",            /* ASSGND_ROLES_COL */
            "oamT6A1,oamT6B1A1,oamT6C1B1A1", /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU18User9",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU18U TR6_DESC", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR9TU18U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT6D2C1B1A1",            /* ASSGND_ROLES_COL */
            "oamT6A1,oamT6B1A1,oamT6B2A1,oamT6C1B1A1,oamT6C2B1A1",/* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU18User10",            /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU18U TR6_DESC", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR10TU18U@jtstools.com",  /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT6D3C2B1A1",            /* ASSGND_ROLES_COL */
            "oamT6A1,oamT6B1A1,oamT6B2A1,oamT6C2B1A1",  /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU18User11",            /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU18U TR6_DESC", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR11TU18U@jtstools.com",  /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT6D4C2B1A1",            /* ASSGND_ROLES_COL */
            "oamT6A1,oamT6B1A1,oamT6B2A1,oamT6C2B1A1", /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU18User12",            /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU18U TR6_DESC", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR12TU18U@jtstools.com",  /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT6D5C3B2A1",            /* ASSGND_ROLES_COL */
            "oamT6A1,oamT6B2A1,oamT6C3B2A1", /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU18User13",            /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU18U TR6_DESC", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR13TU18U@jtstools.com",  /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT6D6C3B2A1",            /* ASSGND_ROLES_COL */
            "oamT6A1,oamT6B2A1,oamT6C3B2A1,oamT6C4B2A1",/* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU18User14",            /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU18U TR6_DESC", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR14TU18U@jtstools.com",  /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT6D7C4B2A1",            /* ASSGND_ROLES_COL */
            "oamT6A1,oamT6B2A1,oamT6C4B2A1",/* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU18User15",            /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU18U TR6_DESC", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "mimsU15TU18U@jts.com",     /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT6D8C4B2A1",            /* ASSGND_ROLES_COL */
            "oamT6A1,oamT6B2A1,oamT6C4B2A1", /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        }
    };

    // Test Case TU18U:
    @MyAnnotation(name = "USERS_TU19U_TR7_ASC", value = "USR TU19U TR7 ASC")
    public final static String[][] USERS_TU19U_TR7_ASC = {
        {
            "oamTU19User1",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU19U TR7_ASC",  /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR1TU19U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT7A1",                  /* ASSGND_ROLES_COL */
            "",                         /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU19User2",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU19U TR7_ASC",  /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR2TU19U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT7B1A1",                /* ASSGND_ROLES_COL */
            "oamT7A1,",                 /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU19User3",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU19U TR7_ASC",  /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR3TU19U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT7B2A1",                /* ASSGND_ROLES_COL */
            "oamT7A1,",                 /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU19User4",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU19U TR7_ASC",  /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR4TU19U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT7C1B1A1",              /* ASSGND_ROLES_COL */
            "oamT7A1,oamT7B1A1",        /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU19User5",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU19U TR7_ASC",  /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR5TU19U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT7C2B1A1",              /* ASSGND_ROLES_COL */
            "oamT7A1,oamT7B1A1,oamT7B2A1",/* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU19User6",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU19U TR7_ASC",  /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR6TU19U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT7C3B2A1",              /* ASSGND_ROLES_COL */
            "oamT7A1,oamT7B2A1",        /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU19User7",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU19U TR7_ASC",  /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR7TU19U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT7C4B2A1",              /* ASSGND_ROLES_COL */
            "oamT7A1,oamT7B2A1",        /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU19User8",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU19U TR7_ASC",  /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR8TU19U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT7D1C1B1A1",            /* ASSGND_ROLES_COL */
            "oamT7A1,oamT7B1A1,oamT7C1B1A1",/* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU19User9",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU19U TR7_ASC",  /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR9TU19U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT7D2C1B1A1",            /* ASSGND_ROLES_COL */
            "oamT7A1,oamT7B1A1,oamT7B2A1,oamT7C1B1A1,oamT7C2B1A1",/* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU19User10",            /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU19U TR7_ASC",  /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR10TU19U@jtstools.com",  /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT7D3C2B1A1",            /* ASSGND_ROLES_COL */
            "oamT7A1,oamT7B1A1,oamT7B2A1,oamT7C2B1A1",/* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU19User11",            /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU19U TR7_ASC",  /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR11TU19U@jtstools.com",  /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT7D4C2B1A1",            /* ASSGND_ROLES_COL */
            "oamT7A1,oamT7B1A1,oamT7B2A1,oamT7C2B1A1",/* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU19User12",            /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU19U TR7_ASC",  /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR12TU19U@jtstools.com",  /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT7D5C3B2A1",            /* ASSGND_ROLES_COL */
            "oamT7A1,oamT7B2A1,oamT7C3B2A1",/* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU19User13",            /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU19U TR7_ASC",  /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR13TU19U@jtstools.com",  /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT7D6C3B2A1",            /* ASSGND_ROLES_COL */
            "oamT7A1,oamT7B2A1,oamT7C3B2A1,oamT7C4B2A1",/* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU19User14",            /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU19U TR7_ASC",  /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR14TU19U@jtstools.com",  /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT7D7C4B2A1",            /* ASSGND_ROLES_COL */
            "oamT7A1,oamT7B2A1,oamT7C4B2A1",/* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU19User15",            /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU19U TR7_ASC",  /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "mimsU15TU19U@jts.com",     /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "oamT7D8C4B2A1",            /* ASSGND_ROLES_COL */
            "oamT7A1,oamT7B2A1,oamT7C4B2A1",/* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        }
    };

    // Test Case TU20U:
    @MyAnnotation(name = "USERS_TU20U_TR5B", value = "USR TU20U TR5B HIER")
    public final static String[][] USERS_TU20U_TR5B = {
        {
            "oamTU20User1",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU20U TR5_HIER", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR1TU20U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "",                         /* ASSGND_ROLES_COL */
            "" ,                        /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU20User2",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU20U TR5_HIER", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR2TU20U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "",                         /* ASSGND_ROLES_COL */
            "",                         /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU20User3",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU20U TR5_HIER", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR3TU20U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "",                         /* ASSGND_ROLES_COL */
            "",                         /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU20User4",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU20U TR5_HIER", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR4TU20U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "",                         /* ASSGND_ROLES_COL */
            "",                         /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU20User5",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU20U TR5_HIER", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR5TU20U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "",                         /* ASSGND_ROLES_COL */
            "",                         /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU20User6",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU20U TR5_HIER", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR6TU20U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "",                         /* ASSGND_ROLES_COL */
            "",                         /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU20User7",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU20U TR5_HIER", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR7TU20U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "",                         /* ASSGND_ROLES_COL */
            "",                         /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU20User8",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU20U TR5_HIER", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR8TU20U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "",                         /* ASSGND_ROLES_COL */
            "",                         /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU20User9",             /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU20U TR5_HIER", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR9TU20U@jtstools.com",   /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "",                         /* ASSGND_ROLES_COL */
            "",                         /* AUTHZ_ROLES_COL */
            "Maumelle,AR,72113,9 Vantage Point,2 floor,MBR",  /* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
        {
            "oamTU20User10",            /* USERID_COL */
            "passw0rd1",                /* PASSWORD_COL */
            "Test1",                    /* PW POLICY ATTR */
            "Test Case TU20U TR5_HIER", /* DESC_COL */
            "fnameoneupd",              /* CN_COL */
            "lnameoneupd",              /* SN_COL */
            "USR10TU20U@jtstools.com",  /* EMAIL_COL */
            "501-111-1111",             /* PHONE_COL */
            "0000",                     /* BTIME_COL */
            "0000",                     /* ETIME_COL */
            "20091001",                 /* BDATE_COL */
            "21000101",                 /* EDATE_COL */
            "20300101",                 /* BLOCKDATE_COL */
            "20300115",                 /* ELOCKDATE_COL */
            "1234567",                  /* DAYMASK_COL */
            "DEV1",                     /* ORG_COL */
            "0",                        /* TIMEOUT_COL */
            "",                         /* ASSGND_ROLES_COL */
            "",                         /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
    };

   // Test Case TU12:
    @MyAnnotation(name = "USERS_TU21_DSD_BRUNO", value = "USR TU21_DSD_BRUNO")
    final static String[][] USERS_TU21_DSD_BRUNO = {
        {
            "oamTU21User1",       /* USERID_COL */
            "passw0rd1",          /* PASSWORD_COL */
            "Test1",              /* PW POLICY ATTR */
            "Test Case TU21",     /* DESC_COL */
            "fnameoneupd",        /* CN_COL */
            "lnameoneupd",        /* SN_COL */
            "jtsU1TU21@jts.com", /* EMAIL_COL */
            "501-111-1111",       /* PHONE_COL */
            "0000",               /* BTIME_COL */
            "0000",               /* ETIME_COL */
            "20091001",           /* BDATE_COL */
            "21000101",           /* EDATE_COL */
            "20300101",           /* BLOCKDATE_COL */
            "20300115",           /* ELOCKDATE_COL */
            "1234567",            /* DAYMASK_COL */
            "DEV1",               /* ORG_COL */
            "15",                 /* TIMEOUT_COL */
            "oamT17DSD1,oamT17DSD3",/* ASSGND_ROLES_COL */
            "",                   /* AUTHZ_ROLES_COL */
            "Twentynine Palms,CA,92252,Hiway 62",/* ADDRESS_COL */
            "888-888-8888,777-777-7777",/* PHONES_COL */
            "555-555-5555,444-444-4444",/* MOBILES_COL */
        }
    };

     /**
     * The Fortress test data for junit uses 2-dimensional arrays.
     */
    private final static int USERID_COL = 0;
    private final static int PASSWORD_COL = 1;
    private final static int PW_POLICY_COL = 2;
    private final static int DESC_COL = 3;
    private final static int CN_COL = 4;
    private final static int SN_COL = 5;
    private final static int EMAIL_COL = 6;
    private final static int PHONE_COL = 7;
    private final static int BTIME_COL = 8;
    private final static int ETIME_COL = 9;
    private final static int BDATE_COL = 10;
    private final static int EDATE_COL = 11;
    private final static int BLOCKDATE_COL = 12;
    private final static int ELOCKDATE_COL = 13;
    private final static int DAYMASK_COL = 14;
    private final static int ORG_COL = 15;
    private final static int TIMEOUT_COL = 16;
    private final static int ASSGND_ROLES_COL = 17;
    private final static int AUTHZ_ROLES_COL = 18;
    private final static int ADDRESS_COL = 19;
    private final static int PHONES_COL = 20;
    private final static int MOBILES_COL = 21;
    private final static int SYSTEM_COL = 22;


    /**
     * @param user
     * @param usr
     */
    public static void assertEquals(User user, String[] usr)
    {
        assertEquals(UserTestData.class.getName() + ".assertEquals failed compare user userId", getUserId(usr).toUpperCase(), user.getUserId().toUpperCase());
        assertEquals(UserTestData.class.getName() + ".assertEquals failed compare user desc", getDescription(usr), user.getDescription());
        //assertEquals(UserTestData.class.getName() + ".assertEquals failed compare user pw policy", getPwPolicy(usr), user.getPwPolicy());
        assertEquals(UserTestData.class.getName() + ".assertEquals failed compare user name", (getFName(usr) + " " + getLName(usr)), user.getName());
        assertEquals(UserTestData.class.getName() + ".assertEquals failed compare user cn", (getFName(usr) + " " + getLName(usr)), user.getCn());
        assertEquals(UserTestData.class.getName() + ".assertEquals failed compare user sn", getLName(usr), user.getSn());
        assertEquals(UserTestData.class.getName() + ".assertEquals failed compare user ou", getOu(usr), user.getOu());
        assertTrue(UserTestData.class.getName() + ".assertEquals failed compare user address", getAddress(usr).equals(user.getAddress()));
        //assertAddress(usr, user.getAddress());
        assertEquals(UserTestData.class.getName() + ".assertEquals failed compare user phones", getPhones(usr), user.getPhones());
        assertEquals(UserTestData.class.getName() + ".assertEquals failed compare user mobiles", getMobiles(usr), user.getMobiles());
        Constraint validConstraint = getUserConstraint(usr);
        TestUtils.assertTemporal(UserTestData.class.getName() + ".assertEquals", validConstraint, user);
    }

    public static void assertAddress(String[] usr, Address address)
    {
        Address expectedAddress = getAddress(usr);
        assertTrue(UserTestData.class.getName() + ".assertEquals failed compare user address", expectedAddress.equals(address));
    }

    /**
     * @param usr
     * @return
     */
    public static String getUserId(String[] usr)
    {
        return usr[USERID_COL];
    }

    /**
     * @param usr
     * @return
     */
    public static char[] getPassword(String[] usr)
    {
        return usr[PASSWORD_COL].toCharArray();
    }

    /**
     * @param usr
     * @return
     */
    public static String getPwPolicy(String[] usr)
    {
        return usr[PW_POLICY_COL];
    }

    /**
     * @param usr
     * @return
     */
    public static String getDescription(String[] usr)
    {
        return usr[DESC_COL];
    }

    /**
     * @param usr
     * @return
     */
    public static String getOu(String[] usr)
    {
        return usr[ORG_COL];
    }

    /**
     * @param usr
     * @return
     */
    public static String getFName(String[] usr)
    {
        return usr[CN_COL];
    }

    /**
     * @param usr
     * @return
     */
    public static String getLName(String[] usr)
    {
        return usr[SN_COL];
    }

    /**
     * @param usr
     * @return
     */
    public static String getBeginTime(String[] usr)
    {
        return usr[BTIME_COL];
    }

    /**
     * @param usr
     * @return
     */
    public static String getEndTime(String[] usr)
    {
        return usr[ETIME_COL];
    }

    /**
     * @param usr
     * @return
     */
    public static String getBeginDate(String[] usr)
    {
        return usr[BDATE_COL];
    }

    /**
     * @param usr
     * @return
     */
    public static String getEndDate(String[] usr)
    {
        return usr[EDATE_COL];
    }

    /**
     * @param usr
     * @return
     */
    public static String getBeginLockDate(String[] usr)
    {
        return usr[BLOCKDATE_COL];
    }

    /**
     * @param usr
     * @return
     */
    public static String getEndLockDate(String[] usr)
    {
        return usr[ELOCKDATE_COL];
    }

    /**
     * @param usr
     * @return
     */
    public static String getDayMask(String[] usr)
    {
        return usr[DAYMASK_COL];
    }

    /**
     * @param usr
     * @return
     */
    public static int getTimeOut(String[] usr)
    {
        return Integer.parseInt(usr[TIMEOUT_COL]);
    }

    /**
     * @param usr
     * @return
     */
    public static Boolean isSystem(String[] usr)
    {
        Boolean isSystem;
        try
        {
            String szBoolean = usr[SYSTEM_COL];
            if(VUtil.isNotNullOrEmpty(szBoolean))
            {
                isSystem = new Boolean(szBoolean);
            }
            else
            {
                isSystem = false;
            }
        }
        catch(java.lang.ArrayIndexOutOfBoundsException ae)
        {
                isSystem = false;
        }
        return isSystem;
    }

    /**
     * @param usr
     * @return
     */
    public static User getUser(String[] usr)
    {
        User user = (User) getUserConstraint(usr);
        user.setUserId(getUserId(usr));
        user.setPassword(getPassword(usr));
        user.setPwPolicy(getPwPolicy(usr));
        user.setDescription(getDescription(usr));
        user.setName(getFName(usr) + " " + getLName(usr));
        user.setCn(user.getName());
        user.setSn(getLName(usr));
        user.setOu(getOu(usr));
        user.setAddress(getAddress(usr));
        user.setPhones(getPhones(usr));
        user.setMobiles(getMobiles(usr));
        user.setSystem(isSystem(usr));
        return user;
    }

    /**
     * @param usr
     * @return
     */
    private static com.jts.fortress.util.time.Constraint getUserConstraint(String[] usr)
    {
        User user = new User();
        user.setBeginDate(getBeginDate(usr));
        user.setEndDate(getEndDate(usr));
        user.setBeginLockDate(getBeginLockDate(usr));
        user.setEndLockDate(getEndLockDate(usr));
        user.setBeginTime(getBeginTime(usr));
        user.setEndTime(getEndTime(usr));
        user.setDayMask(getDayMask(usr));
        user.setTimeout(getTimeOut(usr));
        return user;
    }


    /**
     * @param szInput
     * @return
     */
    public static Set<String> getAssignedRoles(String[] szInput)
    {
        return getSets(szInput, ASSGND_ROLES_COL);
    }

    /**
     * @param szInput
     * @return
     */
    public static Set<String> getAuthorizedRoles(String[] szInput)
    {
        return getSets(szInput, AUTHZ_ROLES_COL);
    }

    /**
     * @param szInput
     * @return
     */
    public static List<String> getPhones(String[] szInput)
    {
        return getList(szInput, PHONES_COL);
    }

    /**
     * @param szInput
     * @return
     */
    public static List<String> getMobiles(String[] szInput)
    {
        return getList(szInput, MOBILES_COL);
    }

    /**
     * @param szInput
     * @return
     */
    public static Address getAddress(String[] szInput)
    {
        return getAddress(szInput, ADDRESS_COL);
    }

    /**
     * @param szInput
     * @param col
     * @return
     */
    private static Address getAddress(String[] szInput, int col)
    {
        Address address = null;
        try
        {
            if (VUtil.isNotNullOrEmpty(szInput[col]))
            {
                address = new Address();
                StringTokenizer charSetTkn = new StringTokenizer(szInput[col], ",");
                if (charSetTkn.countTokens() > 0)
                {
                    int count = 0;
                    while (charSetTkn.hasMoreTokens())
                    {
                        String value = charSetTkn.nextToken();
                        switch(count++)
                        {
                            case 0:
                                address.setCity(value);
                                break;
                            case 1:
                                address.setState(value);
                                break;
                            case 2:
                                address.setPostalCode(value);
                                break;
                            default:
                                address.setAddress(value);
                                break;
                        }
                    }
                }
            }
        }
        catch (java.lang.ArrayIndexOutOfBoundsException ae)
        {
            // ignore
        }
        return address;
    }

    /**
     * @param szInput
     * @param col
     * @return
     */
    private static Set<String> getSets(String[] szInput, int col)
    {
        Set<String> vSets = new TreeSet<String>(new AlphabeticalOrder());
        try
        {
            if (VUtil.isNotNullOrEmpty(szInput[col]))
            {
                StringTokenizer charSetTkn = new StringTokenizer(szInput[col], ",");
                if (charSetTkn.countTokens() > 0)
                {
                    while (charSetTkn.hasMoreTokens())
                    {
                        String value = charSetTkn.nextToken();
                        vSets.add(value);
                    }
                }
            }
        }
        catch (java.lang.ArrayIndexOutOfBoundsException ae)
        {
            // ignore
        }
        return vSets;
    }


    /**
     * @param szInput
     * @param col
     * @return
     */
    private static List<String> getList(String[] szInput, int col)
    {
        List<String> vList = new ArrayList<String>();
        try
        {
            if (VUtil.isNotNullOrEmpty(szInput[col]))
            {
                StringTokenizer charSetTkn = new StringTokenizer(szInput[col], ",");
                if (charSetTkn.countTokens() > 0)
                {
                    while (charSetTkn.hasMoreTokens())
                    {
                        String value = charSetTkn.nextToken();
                        vList.add(value);
                    }
                }
            }
        }
        catch (java.lang.ArrayIndexOutOfBoundsException ae)
        {
            // ignore
        }
        return vList;
    }

    /**
     * Not currently used but will be in future for performance testing:
     */
    final static int USER_MULTIPLIER = 10;
}