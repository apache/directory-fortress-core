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


import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import junit.framework.TestCase;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.model.Address;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.Constraint;


/**
 * Description of the Class
 * M
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class UserTestData extends TestCase
{

    public static final String[][] USERS_TU0 =
        {
            {
                "jtsTestAdminUser", /* USERID_COL */
                "password1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU1", /* DESC_COL */
                "fnameone", /* CN_COL */
                "lnameone", /* SN_COL */
                "jtsTestAdminUser@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV0", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777", /* PHONES_COL */
                "555-555-5555,444-444-4444", /* MOBILES_COL */
                "Admin", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p1.jpeg", /* JPEGPHOTO_COL  */
        },
    };
    
    /**
     * Test Case TU1:
     */
    public static final String[][] USERS_TU1 =
        {
            {
                "jtsUser1", /* USERID_COL */
                "password1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU1", /* DESC_COL */
                "fnameone", /* CN_COL */
                "lnameone", /* SN_COL */
                "jtsUser1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "10", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p2.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsUser2", /* USERID_COL */
                "password2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU1", /* DESC_COL */
                "fnametwo", /* CN_COL */
                "lnametwo", /* SN_COL */
                "jtsUser1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "10", /* TIMEOUT_privateCOL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p3.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsUser3", /* USERID_COL */
                "password3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU1", /* DESC_COL */
                "fnamethree", /* CN_COL */
                "lnametree", /* SN_COL */
                "jtsUser1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "10", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p4.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsUser4", /* USERID_COL */
                "password4", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU1", /* DESC_COL */
                "fnamefour", /* CN_COL */
                "lnamefour", /* SN_COL */
                "jtsUser1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "10", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p5.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsUser5", /* USERID_COL */
                "password5", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU1", /* DESC_COL */
                "fnamefive", /* CN_COL */
                "lnamefive", /* SN_COL */
                "jtsUser1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "10", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p6.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsUser6", /* USERID_COL */
                "password6", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU1", /* DESC_COL */
                "fnamesix", /* CN_COL */
                "lnamesix", /* SN_COL */
                "jtsUser1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "10", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p7.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsUser7", /* USERID_COL */
                "password7", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU1", /* DESC_COL */
                "fnameseven", /* CN_COL */
                "lnameseven", /* SN_COL */
                "jtsUser1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "10", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p8.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsUser8", /* USERID_COL */
                "password8", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU1", /* DESC_COL */
                "fnameeight", /* CN_COL */
                "lnameeight", /* SN_COL */
                "jtsUser1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "10", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p9.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsUser9", /* USERID_COL */
                "password9", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU1", /* DESC_COL */
                "fnamenine", /* CN_COL */
                "lnamenine", /* SN_COL */
                "jtsUser1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "10", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p10.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsUser10", /* USERID_COL */
                "password10", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU1", /* DESC_COL */
                "fnameten", /* CN_COL */
                "lnameten", /* SN_COL */
                "jtsUser1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "10", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p11.jpeg", /* JPEGPHOTO_COL  */
}
    };

    /**
     * Test Case TU1 updated:
     */
    public static final String[][] USERS_TU1_UPD =
        {
            {
                "jtsUser1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "jtsUser1UPD@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
            {
                "jtsUser2", /* USERID_COL */
                "passw0rd2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "jtsUser2UPD@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
    },
            {
                "jtsUser3", /* USERID_COL */
                "passw0rd3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "jtsUser3UPD@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
},
            {
                "jtsUser4", /* USERID_COL */
                "passw0rd4", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "jtsUser4UPD@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
},
            {
                "jtsUser5", /* USERID_COL */
                "passw0rd5", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "jtsUser5UPD@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
},
            {
                "jtsUser6", /* USERID_COL */
                "passw0rd6", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "jtsUser6UPD@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
},
            {
                "jtsUser7", /* USERID_COL */
                "passw0rd7", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "jtsUser7UPD@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
},
            {
                "jtsUser8", /* USERID_COL */
                "passw0rd8", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "jtsUser8UPD@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
},
            {
                "jtsUser9", /* USERID_COL */
                "passw0rd9", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "jtsUser9UPD@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
},
            {
                "jtsUser10", /* USERID_COL */
                "passw0rd10", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "jtsUser10UPD@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
}
    };

    /**
     * Test Case TU2:
     */
    public static final String[][] USERS_TU2 =
        {
            {
                "jtsTU2User1", /* USERID_COL */
                "password1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userOne", /* CN_COL */
                "lastOne", /* SN_COL */
                "fortTest2UR1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p12.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU2User2", /* USERID_COL */
                "password2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userTwo", /* CN_COL */
                "lastTwo", /* SN_COL */
                "fortTest2UR2@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p13.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU2User3", /* USERID_COL */
                "password3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userThree", /* CN_COL */
                "lastThree", /* SN_COL */
                "fortTest2UR3@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV3", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p14.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU2User4", /* USERID_COL */
                "password4", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userFour", /* CN_COL */
                "lastFour", /* SN_COL */
                "fortTest2UR4@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV4", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p15.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU2User5", /* USERID_COL */
                "password5", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userFive", /* CN_COL */
                "lastFive", /* SN_COL */
                "fortTest2UR5@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV5", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p16.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU2User6", /* USERID_COL */
                "password6", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userSix", /* CN_COL */
                "lastSix", /* SN_COL */
                "fortTest2UR6@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV6", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p17.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU2User7", /* USERID_COL */
                "password7", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userSeven", /* CN_COL */
                "lastSeven", /* SN_COL */
                "fortTest2UR7@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV7", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p18.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU2User8", /* USERID_COL */
                "password8", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userEight", /* CN_COL */
                "lastEight", /* SN_COL */
                "fortTest2UR8@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV8", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p19.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU2User9", /* USERID_COL */
                "password9", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userNine", /* CN_COL */
                "lastNine", /* SN_COL */
                "fortTest2UR9@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV9", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p20.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU2User10", /* USERID_COL */
                "password10", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userTen", /* CN_COL */
                "lastTen", /* SN_COL */
                "fortTest2UR10@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p21.jpeg", /* JPEGPHOTO_COL  */
}
    };

    /**
     * Test Case TU2:
     */
    public static final String[][] USERS_TU2_RST =
        {
            {
                "jtsTU2User1", /* USERID_COL */
                "newpasswd", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userOne", /* CN_COL */
                "lastOne", /* SN_COL */
                "fortTest2UR1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p22.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU2User2", /* USERID_COL */
                "newpasswd", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userTwo", /* CN_COL */
                "lastTwo", /* SN_COL */
                "fortTest2UR2@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p23.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU2User3", /* USERID_COL */
                "newpasswd", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userThree", /* CN_COL */
                "lastThree", /* SN_COL */
                "fortTest2UR3@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV3", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p24.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU2User4", /* USERID_COL */
                "newpasswd", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userFour", /* CN_COL */
                "lastFour", /* SN_COL */
                "fortTest2UR4@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV4", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p25.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU2User5", /* USERID_COL */
                "newpasswd", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userFive", /* CN_COL */
                "lastFive", /* SN_COL */
                "fortTest2UR5@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV5", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p26.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU2User6", /* USERID_COL */
                "newpasswd", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userSix", /* CN_COL */
                "lastSix", /* SN_COL */
                "fortTest2UR6@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV6", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p27.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU2User7", /* USERID_COL */
                "newpasswd", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userSeven", /* CN_COL */
                "lastSeven", /* SN_COL */
                "fortTest2UR7@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV7", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p28.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU2User8", /* USERID_COL */
                "newpasswd", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userEight", /* CN_COL */
                "lastEight", /* SN_COL */
                "fortTest2UR8@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV8", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p1.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU2User9", /* USERID_COL */
                "newpasswd", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userNine", /* CN_COL */
                "lastNine", /* SN_COL */
                "fortTest2UR9@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV9", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p2.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU2User10", /* USERID_COL */
                "newpasswd", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userTen", /* CN_COL */
                "lastTen", /* SN_COL */
                "fortTest2UR10@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p3.jpeg", /* JPEGPHOTO_COL  */
},
    };

    public static final String[][] USERS_TU2_CHG =
        {
            {
                "jtsTU2User1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userOne", /* CN_COL */
                "lastOne", /* SN_COL */
                "fortTest2UR1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
        },
            {
                "jtsTU2User2", /* USERID_COL */
                "passw0rd2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userTwo", /* CN_COL */
                "lastTwo", /* SN_COL */
                "fortTest2UR2@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
    },
            {
                "jtsTU2User3", /* USERID_COL */
                "passw0rd3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userThree", /* CN_COL */
                "lastThree", /* SN_COL */
                "fortTest2UR3@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV3", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
},
            {
                "jtsTU2User4", /* USERID_COL */
                "passw0rd4", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userFour", /* CN_COL */
                "lastFour", /* SN_COL */
                "fortTest2UR4@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV4", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
},
            {
                "jtsTU2User5", /* USERID_COL */
                "passw0rd5", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userFive", /* CN_COL */
                "lastFive", /* SN_COL */
                "fortTest2UR5@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV5", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
},
            {
                "jtsTU2User6", /* USERID_COL */
                "passw0rd6", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userSix", /* CN_COL */
                "lastSix", /* SN_COL */
                "fortTest2UR6@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV6", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
},
            {
                "jtsTU2User7", /* USERID_COL */
                "passw0rd7", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userSeven", /* CN_COL */
                "lastSeven", /* SN_COL */
                "fortTest2UR7@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV7", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
},
            {
                "jtsTU2User8", /* USERID_COL */
                "passw0rd8", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userEight", /* CN_COL */
                "lastEight", /* SN_COL */
                "fortTest2UR8@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV8", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
},
            {
                "jtsTU2User9", /* USERID_COL */
                "passw0rd9", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userNine", /* CN_COL */
                "lastNine", /* SN_COL */
                "fortTest2UR9@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV9", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
},
            {
                "jtsTU2User10", /* USERID_COL */
                "passw0rd1-", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU2", /* DESC_COL */
                "userTen", /* CN_COL */
                "lastTen", /* SN_COL */
                "fortTest2UR10@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
},
    };

    // Test Case TU3:
    public static final String[][] USERS_TU3 =
        {
            {
                "jtsTU3User1", /* USERID_COL */
                "password1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU3", /* DESC_COL */
                "userOne", /* CN_COL */
                "lastOne", /* SN_COL */
                "fortTest3UR1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p4.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU3User2", /* USERID_COL */
                "password2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU3", /* DESC_COL */
                "userTwo", /* CN_COL */
                "lastTwo", /* SN_COL */
                "fortTest3UR2@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p5.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU3User3", /* USERID_COL */
                "password3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU3", /* DESC_COL */
                "userThree", /* CN_COL */
                "lastThree", /* SN_COL */
                "fortTest3UR3@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV3", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p6.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU3User4", /* USERID_COL */
                "password4", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU3", /* DESC_COL */
                "userFour", /* CN_COL */
                "lastFour", /* SN_COL */
                "fortTest3UR4@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV4", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p7.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU3User5", /* USERID_COL */
                "password5", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU3", /* DESC_COL */
                "userFive", /* CN_COL */
                "lastFive", /* SN_COL */
                "fortTest3UR5@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV5", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p8.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU3User6", /* USERID_COL */
                "password6", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU3", /* DESC_COL */
                "userSix", /* CN_COL */
                "lastSix", /* SN_COL */
                "fortTest3UR6@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV6", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p9.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU3User7", /* USERID_COL */
                "password7", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU3", /* DESC_COL */
                "userSeven", /* CN_COL */
                "lastSeven", /* SN_COL */
                "fortTest3UR7@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV7", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p10.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU3User8", /* USERID_COL */
                "password8", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU3", /* DESC_COL */
                "userEight", /* CN_COL */
                "lastEight", /* SN_COL */
                "fortTest3UR8@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV8", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p11.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU3User9", /* USERID_COL */
                "password9", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU3", /* DESC_COL */
                "userNine", /* CN_COL */
                "lastNine", /* SN_COL */
                "fortTest3UR9@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV9", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p12.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU3User10", /* USERID_COL */
                "password10", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU3", /* DESC_COL */
                "userTen", /* CN_COL */
                "lastTen", /* SN_COL */
                "fortTest3UR10@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p13.jpeg", /* JPEGPHOTO_COL  */
},
    };

    // Test Case TU4:
    public static final String[][] USERS_TU4 =
        {
            {
                "jtsTU4User1", /* USERID_COL */
                "password1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU4", /* DESC_COL */
                "userOne", /* CN_COL */
                "lastOne", /* SN_COL */
                "fortTest4UR1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p14.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU4User2", /* USERID_COL */
                "password2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU4", /* DESC_COL */
                "userTwo", /* CN_COL */
                "lastTwo", /* SN_COL */
                "fortTest4UR2@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p15.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU4User3", /* USERID_COL */
                "password3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU4", /* DESC_COL */
                "userThree", /* CN_COL */
                "lastThree", /* SN_COL */
                "fortTest4UR3@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV3", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p16.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU4User4", /* USERID_COL */
                "password4", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU4", /* DESC_COL */
                "userFour", /* CN_COL */
                "lastFour", /* SN_COL */
                "fortTest4UR4@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV4", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p17.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU4User5", /* USERID_COL */
                "password5", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU4", /* DESC_COL */
                "userFive", /* CN_COL */
                "lastFive", /* SN_COL */
                "fortTest4UR5@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV5", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p18.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU4User6", /* USERID_COL */
                "password6", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU4", /* DESC_COL */
                "userSix", /* CN_COL */
                "lastSix", /* SN_COL */
                "fortTest4UR6@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV6", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p19.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU4User7", /* USERID_COL */
                "password7", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU4", /* DESC_COL */
                "userSeven", /* CN_COL */
                "lastSeven", /* SN_COL */
                "fortTest4UR7@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV7", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p20.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU4User8", /* USERID_COL */
                "password8", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU4", /* DESC_COL */
                "userEight", /* CN_COL */
                "lastEight", /* SN_COL */
                "fortTest4UR8@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV8", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p21.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU4User9", /* USERID_COL */
                "password9", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU4", /* DESC_COL */
                "userNine", /* CN_COL */
                "lastNine", /* SN_COL */
                "fortTest4UR9@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV9", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p22.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU4User10", /* USERID_COL */
                "password10", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU4", /* DESC_COL */
                "userTen", /* CN_COL */
                "lastTen", /* SN_COL */
                "fortTest4UR10@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p23.jpeg", /* JPEGPHOTO_COL  */
},
    };

    // Test Case TU5:
    @MyAnnotation(name = "USERS_TU5", value = "USR TU5")
    final public static String[][] USERS_TU5 =
        {
            {
                "jtsTU5User1", /* USERID_COL */
                "password1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userOne", /* CN_COL */
                "lastOne", /* SN_COL */
                "fortTest5UR1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p24.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU5User2", /* USERID_COL */
                "password2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userTwo", /* CN_COL */
                "lastTwo", /* SN_COL */
                "fortTest5UR2@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p25.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU5User3", /* USERID_COL */
                "password3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userThree", /* CN_COL */
                "lastThree", /* SN_COL */
                "fortTest5UR3@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV3", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p26.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User4", /* USERID_COL */
                "password4", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userFour", /* CN_COL */
                "lastFour", /* SN_COL */
                "fortTest5UR4@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV4", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p27.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User5", /* USERID_COL */
                "password5", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userFive", /* CN_COL */
                "lastFive", /* SN_COL */
                "fortTest5UR5@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV5", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p28.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User6", /* USERID_COL */
                "password6", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userSix", /* CN_COL */
                "lastSix", /* SN_COL */
                "fortTest5UR6@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV6", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p1.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User7", /* USERID_COL */
                "password7minlength", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userSeven", /* CN_COL */
                "lastSeven", /* SN_COL */
                "fortTest5UR7@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV7", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p2.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User8", /* USERID_COL */
                "password8", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userEight", /* CN_COL */
                "lastEight", /* SN_COL */
                "fortTest5UR8@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV8", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p3.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User9", /* USERID_COL */
                "password9", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userNine", /* CN_COL */
                "lastNine", /* SN_COL */
                "fortTest5UR9@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV9", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p4.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User10", /* USERID_COL */
                "password10", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userTen", /* CN_COL */
                "lastTen", /* SN_COL */
                "fortTest5UR10@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p5.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User11", /* USERID_COL */
                "password11", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userEleven", /* CN_COL */
                "lastEleven", /* SN_COL */
                "fortTest5UR11@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p6.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User12", /* USERID_COL */
                "password12", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userTwelve", /* CN_COL */
                "lastTwelve", /* SN_COL */
                "fortTest5UR12@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p7.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User13", /* USERID_COL */
                "password13", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userThirteen", /* CN_COL */
                "lastThirteen", /* SN_COL */
                "fortTest5UR13@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p8.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User14", /* USERID_COL */
                "password14", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userFourteen", /* CN_COL */
                "lastFourteen", /* SN_COL */
                "fortTest5UR14@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p9.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User15", /* USERID_COL */
                "password15", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userFifteen", /* CN_COL */
                "lastFifteen", /* SN_COL */
                "fortTest5UR15@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p10.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User16", /* USERID_COL */
                "password16", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userSixteen", /* CN_COL */
                "lastSixteen", /* SN_COL */
                "fortTest5UR16@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p11.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User17", /* USERID_COL */
                "password17", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userSeventeen", /* CN_COL */
                "lastSeventeen", /* SN_COL */
                "fortTest5UR17@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p12.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User18", /* USERID_COL */
                "password18", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userEighteen", /* CN_COL */
                "lastEighteen", /* SN_COL */
                "fortTest5UR18@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p13.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User19", /* USERID_COL */
                "password19", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userNineteen", /* CN_COL */
                "lastNineteen", /* SN_COL */
                "fortTest5UR19@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p14.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User20", /* USERID_COL */
                "password20", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userTwenty", /* CN_COL */
                "lastTwenty", /* SN_COL */
                "fortTest5UR20@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p15.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User21", /* USERID_COL */
                "password21", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userTwentyone", /* CN_COL */
                "lastTwentyone", /* SN_COL */
                "fortTest5UR21@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p16.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User22", /* USERID_COL */
                "password22", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userTwentytwo", /* CN_COL */
                "lastTwentytwo", /* SN_COL */
                "fortTest5UR22@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p17.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User23", /* USERID_COL */
                "password23", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userTwentythree", /* CN_COL */
                "lastTwentythree", /* SN_COL */
                "fortTest5UR23@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p18.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User24", /* USERID_COL */
                "password24", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userTwentyfour", /* CN_COL */
                "lastTwentyfour", /* SN_COL */
                "fortTest5UR24@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p19.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User25", /* USERID_COL */
                "password25", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userTwentyfive", /* CN_COL */
                "lastTwentyfive", /* SN_COL */
                "fortTest5UR25@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p20.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User26", /* USERID_COL */
                "password26", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userTwentysix", /* CN_COL */
                "lastTwentysix", /* SN_COL */
                "fortTest5UR26@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p21.jpeg", /* JPEGPHOTO_COL  */
}
    };

    // Test Case TU5:
    @MyAnnotation(name = "USERS_TU5_UPD", value = "USR TU5_UPD")
    final public static String[][] USERS_TU5_UPD =
        {
            {
                "jtsTU5User1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userOne", /* CN_COL */
                "lastOne", /* SN_COL */
                "fortTest5UR1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p22.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU5User2", /* USERID_COL */
                "passw0rd2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userTwo", /* CN_COL */
                "lastTwo", /* SN_COL */
                "fortTest5UR2@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p23.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU5User3", /* USERID_COL */
                "passw0rd3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userThree", /* CN_COL */
                "lastThree", /* SN_COL */
                "fortTest5UR3@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV3", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p24.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User4", /* USERID_COL */
                "passw0rd4", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userFour", /* CN_COL */
                "lastFour", /* SN_COL */
                "fortTest5UR4@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV4", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p25.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User5", /* USERID_COL */
                "password5", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userFive", /* CN_COL */
                "lastFive", /* SN_COL */
                "fortTest5UR5@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV5", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p26.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User6", /* USERID_COL */
                "password6", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userSix", /* CN_COL */
                "lastSix", /* SN_COL */
                "fortTest5UR6@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV6", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p27.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User7", /* USERID_COL */
                "password7", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userSeven", /* CN_COL */
                "lastSeven", /* SN_COL */
                "fortTest5UR7@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV7", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p28.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User8", /* USERID_COL */
                "password8", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userEight", /* CN_COL */
                "lastEight", /* SN_COL */
                "fortTest5UR8@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV8", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p1.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User9", /* USERID_COL */
                "password9", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userNine", /* CN_COL */
                "lastNine", /* SN_COL */
                "fortTest5UR9@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV9", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p2.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU5User10", /* USERID_COL */
                "password10", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU5", /* DESC_COL */
                "userTen", /* CN_COL */
                "lastTen", /* SN_COL */
                "fortTest5UR10@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p3.jpeg", /* JPEGPHOTO_COL  */
},
    };

    // Test Case TU6, these are system users setup in Fortress properties to disallow deletion:
    @MyAnnotation(name = "USERS_TU6_SYS", value = "USR TU6_SYS")
    final public static String[][] USERS_TU6 =
        {
            {
                "jtsTU6User1", /* USERID_COL */
                "password1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU6", /* DESC_COL */
                "userOne", /* CN_COL */
                "lastOne", /* SN_COL */
                "fortTest6UR1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "", /* TITLE_COL */
                "", /* EMPLOYEE_TYPE_COL */
                "TRUE", /* SYSTEM USER */
                "p4.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU6User2", /* USERID_COL */
                "password2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU6", /* DESC_COL */
                "userTwo", /* CN_COL */
                "lastTwo", /* SN_COL */
                "fortTest6UR2@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "", /* TITLE_COL */
                "", /* EMPLOYEE_TYPE_COL */
                "TRUE", /* SYSTEM USER */
                "p5.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU6User3", /* USERID_COL */
                "password3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU6", /* DESC_COL */
                "userThree", /* CN_COL */
                "lastThree", /* SN_COL */
                "fortTest6UR3@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "", /* TITLE_COL */
                "", /* EMPLOYEE_TYPE_COL */
                "TRUE", /* SYSTEM USER */
                "p6.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU6User4", /* USERID_COL */
                "password4", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU6", /* DESC_COL */
                "userFour", /* CN_COL */
                "lastFour", /* SN_COL */
                "fortTest6UR4@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "", /* TITLE_COL */
                "", /* EMPLOYEE_TYPE_COL */
                "TRUE", /* SYSTEM USER */
                "p7.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU6User5", /* USERID_COL */
                "password5", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU6", /* DESC_COL */
                "userFive", /* CN_COL */
                "lastFive", /* SN_COL */
                "fortTest6UR5@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "", /* TITLE_COL */
                "", /* EMPLOYEE_TYPE_COL */
                "TRUE", /* SYSTEM USER */
                "p8.jpeg", /* JPEGPHOTO_COL  */
}
    };

    // Test Case TU7:
    @MyAnnotation(name = "USERS_TU7_HIER", value = "USR TU7_HIER")
    final public static String[][] USERS_TU7_HIER =
        {
            {
                "jtsTU7User1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU7", /* DESC_COL */
                "userOne", /* CN_COL */
                "lastOne", /* SN_COL */
                "fortTest7UR1@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p9.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU7User2", /* USERID_COL */
                "passw0rd2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU7", /* DESC_COL */
                "userTwo", /* CN_COL */
                "lastTwo", /* SN_COL */
                "fortTest7UR2@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV2", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p10.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU7User3", /* USERID_COL */
                "passw0rd3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU7", /* DESC_COL */
                "userThree", /* CN_COL */
                "lastThree", /* SN_COL */
                "fortTest7UR3@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV3", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p11.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU7User4", /* USERID_COL */
                "passw0rd4", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU7", /* DESC_COL */
                "userFour", /* CN_COL */
                "lastFour", /* SN_COL */
                "fortTest7UR4@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV4", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p12.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU7User5", /* USERID_COL */
                "password5", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU7", /* DESC_COL */
                "userFive", /* CN_COL */
                "lastFive", /* SN_COL */
                "fortTest7UR5@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV5", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p13.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU7User6", /* USERID_COL */
                "password6", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU7", /* DESC_COL */
                "userSix", /* CN_COL */
                "lastSix", /* SN_COL */
                "fortTest7UR6@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV6", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p14.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU7User7", /* USERID_COL */
                "password7", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU7", /* DESC_COL */
                "userSeven", /* CN_COL */
                "lastSeven", /* SN_COL */
                "fortTest7UR7@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV7", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p15.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU7User8", /* USERID_COL */
                "password8", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU7", /* DESC_COL */
                "userEight", /* CN_COL */
                "lastEight", /* SN_COL */
                "fortTest7UR8@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV8", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p16.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU7User9", /* USERID_COL */
                "password9", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU7", /* DESC_COL */
                "userNine", /* CN_COL */
                "lastNine", /* SN_COL */
                "fortTest7UR9@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV9", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p17.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU7User10", /* USERID_COL */
                "password10", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU7", /* DESC_COL */
                "userTen", /* CN_COL */
                "lastTen", /* SN_COL */
                "fortTest7UR10@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20100101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "", /* BLOCKDATE_COL */
                "", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV10", /* ORG_COL */
                "30", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p18.jpeg", /* JPEGPHOTO_COL  */
},
    };

    // Test Case TU8:
    @MyAnnotation(name = "USERS_TU8_SSD", value = "USR TU8_SSD")
    public static final String[][] USERS_TU8_SSD =
        {
            {
                "jtsTU8User1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU8", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU8@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p19.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU8User2", /* USERID_COL */
                "passw0rd2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU8", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU2TU8@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p20.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU8User3", /* USERID_COL */
                "passw0rd3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU8", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU3TU8@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p21.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU8User4", /* USERID_COL */
                "passw0rd4", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU8", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU4TU8@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p22.jpeg", /* JPEGPHOTO_COL  */
}
    };

    // Test Case TU8:
    @MyAnnotation(name = "USERS_TU9_SSD_HIER", value = "USR TU9_SSD_HIER")
    public static final String[][] USERS_TU9_SSD_HIER =
        {
            {
                "jtsTU9User1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU9", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU9@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p23.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU9User2", /* USERID_COL */
                "passw0rd2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU9", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU2TU9@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p24.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU9User3", /* USERID_COL */
                "passw0rd3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU9", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU3TU8@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p25.jpeg", /* JPEGPHOTO_COL  */
}
    };

    // Test Case TU10:
    @MyAnnotation(name = "USERS_TU10_SSD_HIER", value = "USR TU10_SSD_HIER")
    public static final String[][] USERS_TU10_SSD_HIER =
        {
            {
                "jtsTU10User1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU10", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU10@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p26.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU10User2", /* USERID_COL */
                "passw0rd2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU10", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU2TU10@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p27.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU10User3", /* USERID_COL */
                "passw0rd3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU10", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU3TU10@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p28.jpeg", /* JPEGPHOTO_COL  */
}
    };

    // Test Case TU11:
    @MyAnnotation(name = "USERS_TU11_SSD_HIER", value = "USR TU11_SSD_HIER")
    public static final String[][] USERS_TU11_SSD_HIER =
        {
            {
                "jtsTU11User1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU11", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU11@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p1.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU11User2", /* USERID_COL */
                "passw0rd2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU11", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU2TU11@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p2.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU11User3", /* USERID_COL */
                "passw0rd3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU11", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU3TU11@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p3.jpeg", /* JPEGPHOTO_COL  */
}
    };

    // Test Case TU12:
    @MyAnnotation(name = "USERS_TU12_DSD", value = "USR TU12_DSD")
    public static final String[][] USERS_TU12_DSD =
        {
            {
                "jtsTU12User1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU12", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU12@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p4.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU12User2", /* USERID_COL */
                "passw0rd2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU12", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU2TU12@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p5.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU12User3", /* USERID_COL */
                "passw0rd3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU12", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU3TU12@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p6.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU12User4", /* USERID_COL */
                "passw0rd4", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU12", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU4TU12@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p7.jpeg", /* JPEGPHOTO_COL  */
}
    };

    // Test Case TU13:
    @MyAnnotation(name = "USERS_TU13_DSD_HIER", value = "USR TU13_DSD_HIER")
    public static final String[][] USERS_TU13_DSD_HIER =
        {
            {
                "jtsTU13User1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU13", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU13@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p8.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU13User2", /* USERID_COL */
                "passw0rd2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU13", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU2TU13@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p9.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU13User3", /* USERID_COL */
                "passw0rd3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU13", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU3TU13@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p10.jpeg", /* JPEGPHOTO_COL  */
}
    };

    // Test Case TU14:
    @MyAnnotation(name = "USERS_TU14_DSD_HIER", value = "USR TU14_DSD_HIER")
    public static final String[][] USERS_TU14_DSD_HIER =
        {
            {
                "jtsTU14User1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU14", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU14@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p11.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU14User2", /* USERID_COL */
                "passw0rd2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU14", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU2TU14@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p12.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU14User3", /* USERID_COL */
                "passw0rd3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU14", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU3TU14@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p13.jpeg", /* JPEGPHOTO_COL  */
}
    };
    // Test Case TU15:
    @MyAnnotation(name = "USERS_TU15_DSD_HIER", value = "USR TU15_DSD_HIER")
    public static final String[][] USERS_TU15_DSD_HIER =
        {
            {
                "jtsTU15User1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU15", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU15@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p14.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU15User2", /* USERID_COL */
                "passw0rd2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU15", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU2TU15@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p15.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU15User3", /* USERID_COL */
                "passw0rd3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU15", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU3TU15@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p16.jpeg", /* JPEGPHOTO_COL  */
}
    };

    // Test Case TU16:
    @MyAnnotation(name = "USERS_TU16_ARBAC", value = "USR TU16_ARBAC")
    public static final String[][] USERS_TU16_ARBAC =
        {
            {
                "jtsTU16User1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU16@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p17.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU16User2", /* USERID_COL */
                "passw0rd2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU2TU16@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p18.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU16User3", /* USERID_COL */
                "passw0rd3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU3TU16@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p19.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU16User4", /* USERID_COL */
                "passw0rd4", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU4TU16@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p20.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU16User5", /* USERID_COL */
                "passw0rd5", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU5TU16@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p21.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU16User6", /* USERID_COL */
                "passw0rd6", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU6TU16@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p22.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU16User7", /* USERID_COL */
                "passw0rd7", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU7TU16@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p23.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU16User8", /* USERID_COL */
                "passw0rd8", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU8TU16@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p24.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU16User9", /* USERID_COL */
                "passw0rd9", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU9TU16@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p25.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU16User10", /* USERID_COL */
                "passw0rd10", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU10TU16@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p26.jpeg", /* JPEGPHOTO_COL  */
}
    };

    // Test Case TU16B:
    @MyAnnotation(name = "USERS_TU16B_ARBAC", value = "USR TU16B_ARBAC")
    public static final String[][] USERS_TU16B_ARBAC =
        {
            {
                "jtsTU16BUser1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16B", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU16B@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT1UOrg1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p27.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU16BUser2", /* USERID_COL */
                "passw0rd2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16B", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU2TU16B@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT1UOrg2", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p28.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU16BUser3", /* USERID_COL */
                "passw0rd3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16B", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU3TU16B@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT1UOrg3", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p1.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU16BUser4", /* USERID_COL */
                "passw0rd4", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16B", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU4TU16B@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT1UOrg4", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p2.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU16BUser5", /* USERID_COL */
                "passw0rd5", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16B", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU5TU16B@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT1UOrg5", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p3.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU16BUser6", /* USERID_COL */
                "passw0rd6", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16B", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU6TU16B@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT1UOrg6", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p4.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU16BUser7", /* USERID_COL */
                "passw0rd7", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16B", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU7TU16B@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT1UOrg7", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p5.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU16BUser8", /* USERID_COL */
                "passw0rd8", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16B", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU8TU16B@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT1UOrg8", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p6.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU16BUser9", /* USERID_COL */
                "passw0rd9", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16B", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU9TU16B@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT1UOrg9", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p7.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU16BUser10", /* USERID_COL */
                "passw0rd10", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU16B", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU10TU16B@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT1UOrg10", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p8.jpeg", /* JPEGPHOTO_COL  */
}
    };

    // Test Case TU17A:
    @MyAnnotation(name = "USERS_TU17A_ARBAC", value = "USR TU17A_ARBAC")
    public static final String[][] USERS_TU17A_ARBAC =
        {
            {
                "jtsTU17AUser1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU17A", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU17A@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p9.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU17AUser2", /* USERID_COL */
                "passw0rd2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU17A", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU17A@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p10.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU17AUser3", /* USERID_COL */
                "passw0rd3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU17A", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU17A@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p11.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU17AUser4", /* USERID_COL */
                "passw0rd4", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU17A", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU17A@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p12.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU17AUser5", /* USERID_COL */
                "passw0rd5", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU17A", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU17A@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p13.jpeg", /* JPEGPHOTO_COL  */
}
    };

    // Test Case TU17U:
    @MyAnnotation(name = "USERS_TU17U_ARBAC", value = "USR TU17U_ARBAC")
    public static final String[][] USERS_TU17U_ARBAC =
        {
            {
                "jtsTU17UUser1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU17U", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU17U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[0] ),
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p14.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU17UUser2", /* USERID_COL */
                "passw0rd2", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU17U", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU17U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[1] ),
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p15.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU17UUser3", /* USERID_COL */
                "passw0rd3", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU17U", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU17U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[2] ),
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p16.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU17UUser4", /* USERID_COL */
                "passw0rd4", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU17U", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU17U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[3] ),
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p17.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU17UUser5", /* USERID_COL */
                "passw0rd5", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU17U", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU1TU17U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_USR_TO5[4] ),
                "15", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p18.jpeg", /* JPEGPHOTO_COL  */
}
    };

    // Test Case TU18U:
    @MyAnnotation(name = "USERS_TU18U_TR6_DESC", value = "USR TU18U TR6 DESC")
    public static final String[][] USERS_TU18U_TR6_DESC =
        {
            {
                "jtsTU18User1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU18U TR6_DESC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR1TU18U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT6A1", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p19.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU18User2", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU18U TR6_DESC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR2TU18U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT6B1A1", /* ASSGND_ROLES_COL */
                "oamT6A1,", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p20.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU18User3", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU18U TR6_DESC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR3TU18U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT6B2A1", /* ASSGND_ROLES_COL */
                "oamT6A1,", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p21.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU18User4", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU18U TR6_DESC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR4TU18U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT6C1B1A1", /* ASSGND_ROLES_COL */
                "oamT6A1,oamT6B1A1", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p22.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU18User5", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU18U TR6_DESC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR5TU18U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT6C2B1A1", /* ASSGND_ROLES_COL */
                "oamT6A1,oamT6B1A1,oamT6B2A1",/* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p23.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU18User6", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU18U TR6_DESC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR6TU18U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT6C3B2A1", /* ASSGND_ROLES_COL */
                "oamT6A1,oamT6B2A1", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p24.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU18User7", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU18U TR6_DESC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR7TU18U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT6C4B2A1", /* ASSGND_ROLES_COL */
                "oamT6A1,oamT6B2A1", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p25.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU18User8", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU18U TR6_DESC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR8TU18U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT6D1C1B1A1", /* ASSGND_ROLES_COL */
                "oamT6A1,oamT6B1A1,oamT6C1B1A1", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p26.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU18User9", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU18U TR6_DESC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR9TU18U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT6D2C1B1A1", /* ASSGND_ROLES_COL */
                "oamT6A1,oamT6B1A1,oamT6B2A1,oamT6C1B1A1,oamT6C2B1A1",/* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p27.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU18User10", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU18U TR6_DESC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR10TU18U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT6D3C2B1A1", /* ASSGND_ROLES_COL */
                "oamT6A1,oamT6B1A1,oamT6B2A1,oamT6C2B1A1", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p28.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU18User11", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU18U TR6_DESC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR11TU18U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT6D4C2B1A1", /* ASSGND_ROLES_COL */
                "oamT6A1,oamT6B1A1,oamT6B2A1,oamT6C2B1A1", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p1.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU18User12", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU18U TR6_DESC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR12TU18U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT6D5C3B2A1", /* ASSGND_ROLES_COL */
                "oamT6A1,oamT6B2A1,oamT6C3B2A1", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p2.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU18User13", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU18U TR6_DESC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR13TU18U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT6D6C3B2A1", /* ASSGND_ROLES_COL */
                "oamT6A1,oamT6B2A1,oamT6C3B2A1,oamT6C4B2A1",/* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p3.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU18User14", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU18U TR6_DESC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR14TU18U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT6D7C4B2A1", /* ASSGND_ROLES_COL */
                "oamT6A1,oamT6B2A1,oamT6C4B2A1",/* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p4.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU18User15", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU18U TR6_DESC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU15TU18U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT6D8C4B2A1", /* ASSGND_ROLES_COL */
                "oamT6A1,oamT6B2A1,oamT6C4B2A1", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p5.jpeg", /* JPEGPHOTO_COL  */
}
    };

    // Test Case TU18U:
    @MyAnnotation(name = "USERS_TU19U_TR7_ASC", value = "USR TU19U TR7 ASC")
    public static final String[][] USERS_TU19U_TR7_ASC =
        {
            {
                "jtsTU19User1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU19U TR7_ASC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR1TU19U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT7A1", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p6.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU19User2", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU19U TR7_ASC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR2TU19U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT7B1A1", /* ASSGND_ROLES_COL */
                "oamT7A1,", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p7.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU19User3", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU19U TR7_ASC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR3TU19U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT7B2A1", /* ASSGND_ROLES_COL */
                "oamT7A1,", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p8.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU19User4", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU19U TR7_ASC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR4TU19U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT7C1B1A1", /* ASSGND_ROLES_COL */
                "oamT7A1,oamT7B1A1", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p9.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU19User5", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU19U TR7_ASC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR5TU19U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT7C2B1A1", /* ASSGND_ROLES_COL */
                "oamT7A1,oamT7B1A1,oamT7B2A1",/* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p10.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU19User6", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU19U TR7_ASC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR6TU19U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT7C3B2A1", /* ASSGND_ROLES_COL */
                "oamT7A1,oamT7B2A1", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p11.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU19User7", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU19U TR7_ASC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR7TU19U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT7C4B2A1", /* ASSGND_ROLES_COL */
                "oamT7A1,oamT7B2A1", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p12.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU19User8", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU19U TR7_ASC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR8TU19U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT7D1C1B1A1", /* ASSGND_ROLES_COL */
                "oamT7A1,oamT7B1A1,oamT7C1B1A1",/* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p13.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU19User9", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU19U TR7_ASC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR9TU19U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT7D2C1B1A1", /* ASSGND_ROLES_COL */
                "oamT7A1,oamT7B1A1,oamT7B2A1,oamT7C1B1A1,oamT7C2B1A1",/* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p14.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU19User10", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU19U TR7_ASC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR10TU19U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT7D3C2B1A1", /* ASSGND_ROLES_COL */
                "oamT7A1,oamT7B1A1,oamT7B2A1,oamT7C2B1A1",/* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p15.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU19User11", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU19U TR7_ASC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR11TU19U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT7D4C2B1A1", /* ASSGND_ROLES_COL */
                "oamT7A1,oamT7B1A1,oamT7B2A1,oamT7C2B1A1",/* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p16.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU19User12", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU19U TR7_ASC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR12TU19U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT7D5C3B2A1", /* ASSGND_ROLES_COL */
                "oamT7A1,oamT7B2A1,oamT7C3B2A1",/* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p17.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU19User13", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU19U TR7_ASC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR13TU19U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT7D6C3B2A1", /* ASSGND_ROLES_COL */
                "oamT7A1,oamT7B2A1,oamT7C3B2A1,oamT7C4B2A1",/* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p18.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU19User14", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU19U TR7_ASC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR14TU19U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT7D7C4B2A1", /* ASSGND_ROLES_COL */
                "oamT7A1,oamT7B2A1,oamT7C4B2A1",/* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p19.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU19User15", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU19U TR7_ASC", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "fortU15TU19U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "oamT7D8C4B2A1", /* ASSGND_ROLES_COL */
                "oamT7A1,oamT7B2A1,oamT7C4B2A1",/* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p20.jpeg", /* JPEGPHOTO_COL  */
}
    };

    // Test Case TU20U:
    @MyAnnotation(name = "USERS_TU20U_TR5B", value = "USR TU20U TR5B HIER")
    public static final String[][] USERS_TU20U_TR5B =
        {
            {
                "jtsTU20User1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU20U TR5_HIER", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR1TU20U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p21.jpeg", /* JPEGPHOTO_COL  */
        },
            {
                "jtsTU20User2", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU20U TR5_HIER", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR2TU20U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p22.jpeg", /* JPEGPHOTO_COL  */
    },
            {
                "jtsTU20User3", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU20U TR5_HIER", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR3TU20U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p23.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU20User4", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU20U TR5_HIER", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR4TU20U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p24.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU20User5", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU20U TR5_HIER", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR5TU20U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p25.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU20User6", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU20U TR5_HIER", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR6TU20U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p26.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU20User7", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU20U TR5_HIER", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR7TU20U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p27.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU20User8", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU20U TR5_HIER", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR8TU20U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p28.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU20User9", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU20U TR5_HIER", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR9TU20U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p1.jpeg", /* JPEGPHOTO_COL  */
},
            {
                "jtsTU20User10", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU20U TR5_HIER", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "USR10TU20U@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p2.jpeg", /* JPEGPHOTO_COL  */
        }
    };

    // Test Case TU21:
    @MyAnnotation(name = "USERS_TU21_DSD_BRUNO", value = "USR TU21_DSD_BRUNO")
    public static final String[][] USERS_TU21_DSD_BRUNO =
        {
            {
                "jtsTU21User1", /* USERID_COL */
                "passw0rd1", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Test Case TU21", /* DESC_COL */
                "fnameoneupd", /* CN_COL */
                "lnameoneupd", /* SN_COL */
                "jtsU1TU21@jts.us", /* EMAILS_COL */
                "p1:v1,p2:v2,p3:v3", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "15", /* TIMEOUT_COL */
                "oamT17DSD1,oamT17DSD3",/* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p3.jpeg", /* JPEGPHOTO_COL  */
        }
    };


    // Test Case TU21:
    @MyAnnotation(name = "USERS_TU22_ABAC", value = "USR TU22 ABAC")
    public static final String[][] USERS_TU22_ABAC =
        {
            {
                "curly", /* USERID_COL */
                "password", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Head Teller of the East, Coin Washer in North and South", /* DESC_COL */
                "Curly Howrowitz", /* CN_COL */
                "Horowitz", /* SN_COL */
                "curly.horowitz@stooge.com", /* EMAILS_COL */
                "", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p1.jpeg", /* JPEGPHOTO_COL  */
            },
            {
                "moe", /* USERID_COL */
                "password", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Head Teller of the North, Coin Washer in East and South", /* DESC_COL */
                "Moe Howard", /* CN_COL */
                "Howard", /* SN_COL */
                "moe.howard@stooge.com", /* EMAILS_COL */
                "", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p1.jpeg", /* JPEGPHOTO_COL  */
            },
            {
                "larry", /* USERID_COL */
                "password", /* PASSWORD_COL */
                "Test1", /* PW POLICY ATTR */
                "Head Teller of the South, Coin Washer in North and East", /* DESC_COL */
                "Larry Fine", /* CN_COL */
                "Fine", /* SN_COL */
                "larry.fine@stooge.com", /* EMAILS_COL */
                "", /* PROPS_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20091001", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "DEV1", /* ORG_COL */
                "0", /* TIMEOUT_COL */
                "", /* ASSGND_ROLES_COL */
                "", /* AUTHZ_ROLES_COL */
                "Lawrence,KS,66045,Strong Hall,Computer Science,222",/* ADDRESS_COL */
                "888-888-8888,777-777-7777",/* PHONES_COL */
                "555-555-5555,444-444-4444",/* MOBILES_COL */
                "Tester", /* TITLE_COL */
                "Permanent", /* EMPLOYEE_TYPE_COL */
                "FALSE", /* SYSTEM USER */
                "p3.jpeg", /* JPEGPHOTO_COL  */
            },
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
    private final static int EMAILS_COL = 6;
    private final static int PROPS_COL = 7;
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
    private final static int TITLE_COL = 22;
    private final static int EMPLOYEE_TYPE_COL = 23;
    private final static int SYSTEM_COL = 24;
    private final static int JPEGPHOTO_COL = 25;


    /**
     * @param user
     * @param usr
     */
    public static void assertEquals( User user, String[] usr )
    {
        assertEquals( UserTestData.class.getName() + ".assertEquals failed compare user userId", getUserId( usr )
            .toUpperCase(), user.getUserId().toUpperCase() );
        assertEquals( UserTestData.class.getName() + ".assertEquals failed compare user desc", getDescription( usr ),
            user.getDescription() );
        //assertEquals(UserTestData.class.getName() + ".assertEquals failed compare user pw policy", getPwPolicy(usr), user.getPwPolicy());
/*
        assertEquals( UserTestData.class.getName() + ".assertEquals failed compare user name",
            ( getFName( usr ) + " " + getLName( usr ) ), user.getName() );
*/
        assertEquals( UserTestData.class.getName() + ".assertEquals failed compare user cn",
            ( getFName( usr ) + " " + getLName( usr ) ), user.getCn() );
        assertEquals( UserTestData.class.getName() + ".assertEquals failed compare user sn", getLName( usr ),
            user.getSn() );
        assertEquals( UserTestData.class.getName() + ".assertEquals failed compare user ou", getOu( usr ), user.getOu() );
        assertTrue( UserTestData.class.getName() + ".assertEquals failed compare user address", getAddress( usr )
            .equals( user.getAddress() ) );
        //assertAddress(usr, user.getAddress());
        assertEquals( UserTestData.class.getName() + ".assertEquals failed compare user phones", getPhones( usr ),
            user.getPhones() );
        assertEquals( UserTestData.class.getName() + ".assertEquals failed compare user mobiles", getMobiles( usr ),
            user.getMobiles() );
        assertProps( usr, user.getProperties() );
        assertEmail( usr, user.getEmails() );
        Constraint validConstraint = getUserConstraint( usr );
        TestUtils.assertTemporal( UserTestData.class.getName() + ".assertEquals", validConstraint, user );
    }


    public static void assertEquals( User user, User user2 )
    {
        assertEquals( UserTestData.class.getName() + ".assertEquals failed compare user userId", user2.getUserId()
            .toUpperCase(), user.getUserId().toUpperCase() );
        assertEquals( UserTestData.class.getName() + ".assertEquals failed compare user desc", user2.getDescription( ),
            user.getDescription() );
        assertEquals(UserTestData.class.getName() + ".assertEquals failed compare user pw policy", user2.getPwPolicy( ), user.getPwPolicy());

        assertEquals( UserTestData.class.getName() + ".assertEquals failed compare user name",
            user2.getName( ), user.getName() );

        assertEquals( UserTestData.class.getName() + ".assertEquals failed compare user cn",
            user2.getCn( ), user.getCn() );
        assertEquals( UserTestData.class.getName() + ".assertEquals failed compare user sn", user2.getSn( ),
            user.getSn() );
        assertEquals( UserTestData.class.getName() + ".assertEquals failed compare user ou", user2.getOu( ), user.getOu() );
        //assertTrue( UserTestData.class.getName() + ".assertEquals failed compare user address", user2.getAddress( )
        //    .equals( user.getAddress() ) );
        //assertAddress(usr, user.getAddress());
        assertEquals( UserTestData.class.getName() + ".assertEquals failed compare user phones", user2.getPhones( ),
            user.getPhones() );
        assertEquals( UserTestData.class.getName() + ".assertEquals failed compare user mobiles", user2.getMobiles( ),
            user.getMobiles() );
        //assertProps( user2.getProperties(), user.getProperties() );
        //assertEmail( usr, user.getEmails() );
        TestUtils.assertTemporal( UserTestData.class.getName() + ".assertEquals", user2, user );
    }


    /**
     * Determine if a given User object contains its assigned email  values.
     *
     * @param usr expected user properties
     * @param emails actual user emails
     */
    public static void assertEmail( String[] usr, List<String> emails )
    {
        List<String> expected = getEmails( usr );

        if ( CollectionUtils.isNotEmpty( expected ) )
        {
            assertNotNull( UserTestData.class.getName() + ".assertEmail null for user: " + getUserId( usr ), emails );

            for ( String email : expected )
            {
                assertTrue( UserTestData.class.getName() + ".assertEmail failed compare email for user: "
                    + getUserId( usr ) + ", expected value: " + expected, emails.contains( email ) );
            }
        }
    }


    public static void assertAddress( String[] usr, Address address )
    {
        Address expectedAddress = getAddress( usr );
        assertTrue( UserTestData.class.getName() + ".assertEquals failed compare user address",
            expectedAddress.equals( address ) );
    }


    /**
     * Determine if a given User object contains its assigned properties values.
     *
     * @param usr expected user properties
     * @param properties actual user properties
     */
    public static void assertProps( String[] usr, Properties properties )
    {
        Properties usrProps = getProps( usr );

        if ( usrProps != null )
        {
            assertNotNull( UserTestData.class.getName() + ".assertProps null for user: " + getUserId( usr ), properties );

            for ( Enumeration<?> e = properties.propertyNames(); e.hasMoreElements(); )
            {
                String key = ( String ) e.nextElement();
                String val = usrProps.getProperty( key );
                String uval = usrProps.getProperty( key );
                assertEquals( UserTestData.class.getName() + ".assertProps failed compare props for user: "
                    + getUserId( usr ) + ", key: " + key + ", expected value: " + val + ", actual value: " + uval, val,
                    uval );
            }
        }
    }


    /**
     * @param usr
     * @return
     */
    public static String getUserId( String[] usr )
    {
        return usr[USERID_COL];
    }


    /**
     * @param usr
     * @return
     */
    public static String getPassword( String[] usr )
    {
        return usr[PASSWORD_COL];
    }


    /**
     * @param usr
     * @return
     */
    public static String getPwPolicy( String[] usr )
    {
        return usr[PW_POLICY_COL];
    }


    /**
     * @param usr
     * @return
     */
    public static String getDescription( String[] usr )
    {
        return usr[DESC_COL];
    }


    /**
     * @param usr
     * @return
     */
    public static String getOu( String[] usr )
    {
        return usr[ORG_COL];
    }


    /**
     * @param usr
     * @return
     */
    public static String getFName( String[] usr )
    {
        return usr[CN_COL];
    }


    /**
     * @param usr
     * @return
     */
    public static String getLName( String[] usr )
    {
        return usr[SN_COL];
    }


    /**
     * @param usr
     * @return
     */
    public static String getBeginTime( String[] usr )
    {
        return usr[BTIME_COL];
    }


    /**
     * @param usr
     * @return
     */
    public static String getEndTime( String[] usr )
    {
        return usr[ETIME_COL];
    }


    /**
     * @param usr
     * @return
     */
    public static String getBeginDate( String[] usr )
    {
        String value = null;
        if(StringUtils.isNotEmpty( usr[BDATE_COL] ))
            value = usr[BDATE_COL];
        return value;
    }


    /**
     * @param usr
     * @return
     */
    public static String getEndDate( String[] usr )
    {
        String value = null;
        if(StringUtils.isNotEmpty( usr[EDATE_COL] ))
            value = usr[EDATE_COL];
        return value;
    }


    /**
     * @param usr
     * @return
     */
    public static String getBeginLockDate( String[] usr )
    {
        String value = null;
        if(StringUtils.isNotEmpty( usr[BLOCKDATE_COL] ))
            value = usr[BLOCKDATE_COL];
        return value;
    }


    /**
     * @param usr
     * @return
     */
    public static String getEndLockDate( String[] usr )
    {
        String value = null;
        if(StringUtils.isNotEmpty( usr[ELOCKDATE_COL] ))
            value = usr[ELOCKDATE_COL];
        return value;
    }


    /**
     * @param usr
     * @return
     */
    public static String getDayMask( String[] usr )
    {
        return usr[DAYMASK_COL];
    }


    /**
     * @param usr
     * @return
     */
    public static int getTimeOut( String[] usr )
    {
        return Integer.parseInt( usr[TIMEOUT_COL] );
    }


    /**
     *
     * @param usr
     * @return
     */
    public static String getTitle( String[] usr )
    {
        String value = null;

        try
        {
            value = usr[TITLE_COL];
        }
        catch ( ArrayIndexOutOfBoundsException ae )
        {
            // attribute is optional, do nothing here
        }

        return value;
    }


    /**
     *
     * @param usr
     * @return
     */
    public static String getEmployeeType( String[] usr )
    {
        String value = null;

        try
        {
            value = usr[EMPLOYEE_TYPE_COL];
        }
        catch ( ArrayIndexOutOfBoundsException ae )
        {
            // attribute is optional, do nothing here
        }

        return value;
    }


    public static byte[] getJpegPhoto( String[] usr )
    {
        byte[] value = null;

        try
        {
            String fileName = usr[JPEGPHOTO_COL];
            value = TestUtils.readJpegFile( fileName );
        }
        catch ( ArrayIndexOutOfBoundsException ae )
        {
            // attribute is optional, do nothing here
        }

        return value;
    }


    /**
     * @param usr
     * @return
     */
    public static Boolean isSystem( String[] usr )
    {
        if ( usr.length >= SYSTEM_COL )
        {
            return Boolean.valueOf( usr[SYSTEM_COL] );
        }
        else
        {
            return Boolean.FALSE;
        }
    }


    /**
     * @param usr
     * @return
     */
    public static User getUser( String[] usr )
    {
        User user = ( User ) getUserConstraint( usr );
        user.setUserId( getUserId( usr ) );
        user.setPassword( getPassword( usr ) );
        user.setPwPolicy( getPwPolicy( usr ) );
        user.setDescription( getDescription( usr ) );
        user.setName( getFName( usr ) + " " + getLName( usr ) );
        user.setCn( user.getName() );
        user.setSn( getLName( usr ) );
        user.setOu( getOu( usr ) );
        user.setAddress( getAddress( usr ) );
        user.setPhones( getPhones( usr ) );
        user.setMobiles( getMobiles( usr ) );
        user.setTitle( getTitle( usr ) );
        user.setEmployeeType( getEmployeeType( usr ) );
        user.setSystem( isSystem( usr ) );
        user.addProperties( getProps( usr ) );
        user.setEmails( getEmails( usr ) );
        user.setJpegPhoto( getJpegPhoto( usr ) );

        return user;
    }


    /**
     * @param usr
     * @return
     */
    private static Constraint getUserConstraint( String[] usr )
    {
        User user = new User();
        user.setBeginDate( getBeginDate( usr ) );
        user.setEndDate( getEndDate( usr ) );
        user.setBeginLockDate( getBeginLockDate( usr ) );
        user.setEndLockDate( getEndLockDate( usr ) );
        user.setBeginTime( getBeginTime( usr ) );
        user.setEndTime( getEndTime( usr ) );
        user.setDayMask( getDayMask( usr ) );
        user.setTimeout( getTimeOut( usr ) );

        return user;
    }


    /**
     *
     * @param szInput
     * @return
     */
    public static Properties getProps( String[] szInput )
    {
        Properties properties = null;
        List<String> props = new ArrayList<String>();

        getElements( props, szInput, PROPS_COL );

        if ( CollectionUtils.isNotEmpty( props ) )
        {
            properties = new Properties();

            for ( String szRaw : props )
            {
                int indx = szRaw.indexOf( GlobalIds.PROP_SEP );
                if ( indx >= 1 )
                {
                    properties.setProperty( szRaw.substring( 0, indx ), szRaw.substring( indx + 1 ) );
                }
            }
        }

        return properties;
    }


    /**
     * @param szInput
     * @return
     */
    public static Set<String> getAssignedRoles( String[] szInput )
    {
        Set<String> elements = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
        getElements( elements, szInput, ASSGND_ROLES_COL );

        return elements;
    }


    /**
     * @param szInput
     * @return
     */
    public static Collection<String> getAuthorizedRoles( String[] szInput )
    {
        Set<String> elements = new TreeSet<String>( String.CASE_INSENSITIVE_ORDER );
        getElements( elements, szInput, AUTHZ_ROLES_COL );

        return elements;
    }


    /**
     * @param szInput
     * @return
     */
    public static List<String> getPhones( String[] szInput )
    {
        List<String> phones = new ArrayList<String>();

        getElements( phones, szInput, PHONES_COL );

        return phones;
    }


    /**
     * @param szInput
     * @return
     */
    public static List<String> getMobiles( String[] szInput )
    {
        List<String> mobiles = new ArrayList<String>();

        getElements( mobiles, szInput, MOBILES_COL );

        return mobiles;
    }


    /**
     * @param szInput
     * @return
     */
    public static Address getAddress( String[] szInput )
    {
        return getAddress( szInput, ADDRESS_COL );
    }


    /**
     * @param szInput
     * @return
     */
    public static List<String> getEmails( String[] szInput )
    {
        List<String> emails = new ArrayList<String>();

        getElements( emails, szInput, EMAILS_COL );

        return emails;
    }


    private static Address getAddress( String[] szInput, int col )
    {
        Address address = null;

        try
        {
            if ( StringUtils.isNotEmpty( szInput[col] ) )
            {
                address = new Address();
                StringTokenizer charSetTkn = new StringTokenizer( szInput[col], TestUtils.DELIMITER_TEST_DATA );

                if ( charSetTkn.countTokens() > 0 )
                {
                    int count = 0;

                    while ( charSetTkn.hasMoreTokens() )
                    {
                        String value = charSetTkn.nextToken();

                        /* ADDRESS_COL */
                        switch ( count++ )
                        {
                        // "Lawrence,KS,66045,Strong Hall,Computer Science,222",
                        //
                        // Twentynine Palms */
                            case 0:
                                address.setCity( value );
                                break;
                            // CA */
                            case 1:
                                address.setState( value );
                                break;
                            // 92252 */
                            case 2:
                                address.setPostalCode( value );
                                break;
                            // 2345 */
                            case 3:
                                address.setBuilding( value );
                                break;
                            // 123 */
                            case 4:
                                address.setDepartmentNumber( value );
                                break;
                            // 2525 */
                            case 5:
                                address.setRoomNumber( value );
                                break;
                            // Hiway 62
                            /* ADDRESS_COL */
                            default:
                                address.setAddress( value );
                                break;
                        }
                    }
                }
            }
        }
        catch ( java.lang.ArrayIndexOutOfBoundsException ae )
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
    private static void getElements( Collection<String> elements, String[] szInput, int col )
    {
        try
        {
            String input = szInput[col];

            if ( StringUtils.isNotEmpty( input ) )
            {
                StringTokenizer charSetTkn = new StringTokenizer( input, TestUtils.DELIMITER_TEST_DATA );

                if ( charSetTkn.countTokens() > 0 )
                {
                    while ( charSetTkn.hasMoreTokens() )
                    {
                        String value = charSetTkn.nextToken();
                        elements.add( value );
                    }
                }
            }
        }
        catch ( ArrayIndexOutOfBoundsException ae )
        {
            // ignore
        }
    }

    /**
     * Not currently used but will be in future for performance testing:
     */
    final static int USER_MULTIPLIER = 10;
}