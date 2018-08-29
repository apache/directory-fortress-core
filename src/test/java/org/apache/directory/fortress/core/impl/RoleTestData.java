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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.RoleConstraint;
import org.apache.directory.fortress.core.model.SDSet;
import org.apache.directory.fortress.core.model.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.directory.fortress.core.model.Constraint;

import javax.crypto.spec.RC2ParameterSpec;


/**
 * Description of the Class
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class RoleTestData extends TestCase
{
    private static final Logger LOG = LoggerFactory.getLogger( RoleTestData.class.getName() );

    /**
      * Test Case TR1:
     */
    public static final String[][] ROLES_TR1 =
        {
            {
                "oamROLE1", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
        },
            {
                "oamROLE2", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
    },
            {
                "oamROLE3", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamROLE4", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamROLE5", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamROLE6", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamROLE7", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamROLE8", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamROLE9", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamROLE10", /* NAME_COL */
                "Test Case TR1", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
    };

    /**
      * Test Case TR2: 
     */
    public static final String[][] ROLES_TR2 =
        {
            {
                "oamT2ROLE1", /* NAME_COL */
                "Test Case R2", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "none", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "none", /* BLOCKDATE_COL */
                "none", /* ELOCKDATE_COL */
                "all" /* DAYMASK_COL */
        },
            {
                "oamT2ROLE2", /* NAME_COL */
                "Test Case R2", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "none", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "none", /* BLOCKDATE_COL */
                "none", /* ELOCKDATE_COL */
                "all" /* DAYMASK_COL */
    },
            {
                "oamT2ROLE3", /* NAME_COL */
                "Test Case R2", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "none", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "none", /* BLOCKDATE_COL */
                "none", /* ELOCKDATE_COL */
                "all" /* DAYMASK_COL */
},
            {
                "oamT2ROLE4", /* NAME_COL */
                "Test Case R2", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "none", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "none", /* BLOCKDATE_COL */
                "none", /* ELOCKDATE_COL */
                "all" /* DAYMASK_COL */
},
            {
                "oamT2ROLE5", /* NAME_COL */
                "Test Case R2", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "none", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "none", /* BLOCKDATE_COL */
                "none", /* ELOCKDATE_COL */
                "all" /* DAYMASK_COL */
},
            {
                "oamT2ROLE6", /* NAME_COL */
                "Test Case R2", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "none", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "none", /* BLOCKDATE_COL */
                "none", /* ELOCKDATE_COL */
                "all" /* DAYMASK_COL */
},
            {
                "oamT2ROLE7", /* NAME_COL */
                "Test Case R2", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "none", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "none", /* BLOCKDATE_COL */
                "none", /* ELOCKDATE_COL */
                "all" /* DAYMASK_COL */
},
            {
                "oamT2ROLE8", /* NAME_COL */
                "Test Case R2", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "none", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "none", /* BLOCKDATE_COL */
                "none", /* ELOCKDATE_COL */
                "all" /* DAYMASK_COL */
},
            {
                "oamT2ROLE9", /* NAME_COL */
                "Test Case R2", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "none", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "none", /* BLOCKDATE_COL */
                "none", /* ELOCKDATE_COL */
                "all" /* DAYMASK_COL */
},
            {
                "oamT2ROLE10", /* NAME_COL */
                "Test Case R2", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "none", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "none", /* BLOCKDATE_COL */
                "none", /* ELOCKDATE_COL */
                "all" /* DAYMASK_COL */
},
    };

    public static final String[][] ROLES_TR3 =
        {
            {
                "oamT3ROLE1", /* NAME_COL */
                "Test Case TR3", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
        },
            {
                "oamT3ROLE2", /* NAME_COL */
                "Test Case TR3", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
    },
            {
                "oamT3ROLE3", /* NAME_COL */
                "Test Case TR3", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT3ROLE4", /* NAME_COL */
                "Test Case TR3", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT3ROLE5", /* NAME_COL */
                "Test Case TR3", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT3ROLE6", /* NAME_COL */
                "Test Case TR3", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT3ROLE7", /* NAME_COL */
                "Test Case TR3", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT3ROLE8", /* NAME_COL */
                "Test Case TR3", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT3ROLE9", /* NAME_COL */
                "Test Case TR3", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT3ROLE10", /* NAME_COL */
                "Test Case TR3", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
    };

    public static final String[][] ROLES_TR4 =
        {
            {
                "oamT4ROLE1", /* NAME_COL */
                "Test Case TR4", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
        },
            {
                "oamT4ROLE2", /* NAME_COL */
                "Test Case TR4", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
    },
            {
                "oamT4ROLE3", /* NAME_COL */
                "Test Case TR4", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT4ROLE4", /* NAME_COL */
                "Test Case TR4", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT4ROLE5", /* NAME_COL */
                "Test Case TR4", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT4ROLE6", /* NAME_COL */
                "Test Case TR4", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT4ROLE7", /* NAME_COL */
                "Test Case TR4", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT4ROLE8", /* NAME_COL */
                "Test Case TR4", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT4ROLE9", /* NAME_COL */
                "Test Case TR4", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT4ROLE10", /* NAME_COL */
                "Test Case TR4", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
    };

    public static final String[][] ROLES_TR4_UPD =
        {
            {
                "oamT4ROLE1", /* NAME_COL */
                "Test Case TR4-U", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20080101", /* BDATE_COL */
                "20110101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "123456" /* DAYMASK_COL */
        },
            {
                "oamT4ROLE2", /* NAME_COL */
                "Test Case TR4-U", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20080101", /* BDATE_COL */
                "20110101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "123456" /* DAYMASK_COL */
    },
            {
                "oamT4ROLE3", /* NAME_COL */
                "Test Case TR4-U", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20080101", /* BDATE_COL */
                "20110101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "123456" /* DAYMASK_COL */
},
            {
                "oamT4ROLE4", /* NAME_COL */
                "Test Case TR4-U", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20080101", /* BDATE_COL */
                "20110101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "123456" /* DAYMASK_COL */
},
            {
                "oamT4ROLE5", /* NAME_COL */
                "Test Case TR4-U", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20080101", /* BDATE_COL */
                "20110101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "123456" /* DAYMASK_COL */
},
            {
                "oamT4ROLE6", /* NAME_COL */
                "Test Case TR4-U", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20080101", /* BDATE_COL */
                "20110101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "123456" /* DAYMASK_COL */
},
            {
                "oamT4ROLE7", /* NAME_COL */
                "Test Case TR4-U", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20080101", /* BDATE_COL */
                "20110101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "123456" /* DAYMASK_COL */
},
            {
                "oamT4ROLE8", /* NAME_COL */
                "Test Case TR4-U", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20080101", /* BDATE_COL */
                "20110101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "123456" /* DAYMASK_COL */
},
            {
                "oamT4ROLE9", /* NAME_COL */
                "Test Case TR4-U", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20080101", /* BDATE_COL */
                "29110101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "123456" /* DAYMASK_COL */
},
            {
                "oamT4ROLE10", /* NAME_COL */
                "Test Case TR4-U", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20080101", /* BDATE_COL */
                "20110101", /* EDATE_COL */
                "20300101", /* BLOCKDATE_COL */
                "20300115", /* ELOCKDATE_COL */
                "123456" /* DAYMASK_COL */
},
    };

    public static final String[][] ROLES_TR5_HIER =
        {
            {
                "oamT5ROLE1", /* NAME_COL */
                "Test Case TR5", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5ROLE2" /* PARENTS_COL */
        },
            {
                "oamT5ROLE2", /* NAME_COL */
                "Test Case TR5", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5ROLE3" /* PARENTS_COL */

    },
            {
                "oamT5ROLE3", /* NAME_COL */
                "Test Case TR5", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5ROLE4" /* PARENTS_COL */
},
            {
                "oamT5ROLE4", /* NAME_COL */
                "Test Case TR5", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5ROLE5" /* PARENTS_COL */
},
            {
                "oamT5ROLE5", /* NAME_COL */
                "Test Case TR5", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5ROLE6" /* PARENTS_COL */
},
            {
                "oamT5ROLE6", /* NAME_COL */
                "Test Case TR5", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5ROLE7" /* PARENTS_COL */
},
            {
                "oamT5ROLE7", /* NAME_COL */
                "Test Case TR5", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5ROLE8" /* PARENTS_COL */
},
            {
                "oamT5ROLE8", /* NAME_COL */
                "Test Case TR5", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5ROLE9" /* PARENTS_COL */
},
            {
                "oamT5ROLE9", /* NAME_COL */
                "Test Case TR5", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5ROLE10" /* PARENTS_COL */
},
            {
                "oamT5ROLE10", /* NAME_COL */
                "Test Case TR5", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "" /* PARENTS_COL */
},
    };

    public static final String[][] ROLES_TR5B =
        {
            {
                "oamT5BROLE1", /* NAME_COL */
                "Test Case TR5B", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5BROLE2" /* PARENTS_COL */
        },
            {
                "oamT5BROLE2", /* NAME_COL */
                "Test Case TR5B", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5BROLE3" /* PARENTS_COL */

    },
            {
                "oamT5BROLE3", /* NAME_COL */
                "Test Case TR5B", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5BROLE4" /* PARENTS_COL */
},
            {
                "oamT5BROLE4", /* NAME_COL */
                "Test Case TR5B", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5BROLE5" /* PARENTS_COL */
},
            {
                "oamT5BROLE5", /* NAME_COL */
                "Test Case TR5B", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5BROLE6" /* PARENTS_COL */
},
            {
                "oamT5BROLE6", /* NAME_COL */
                "Test Case TR5B", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5BROLE7" /* PARENTS_COL */
},
            {
                "oamT5BROLE7", /* NAME_COL */
                "Test Case TR5B", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5BROLE8" /* PARENTS_COL */
},
            {
                "oamT5BROLE8", /* NAME_COL */
                "Test Case TR5B", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5BROLE9" /* PARENTS_COL */
},
            {
                "oamT5BROLE9", /* NAME_COL */
                "Test Case TR5B", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT5BROLE10" /* PARENTS_COL */
},
            {
                "oamT5BROLE10", /* NAME_COL */
                "Test Case TR5B", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "" /* PARENTS_COL */
},
    };

    public static final String[][] ROLES_TR6_DESC =
        {
            {
                "oamT6A1", /* NAME_COL */
                "Test Case TR6", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT6B1A1,oamT6B2A1", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "D" /* INHERITANCE_FLAG_COL */
        },
            {
                "oamT6B1A1", /* NAME_COL */
                "Test Case TR6", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT6C1B1A1,oamT6C2B1A1", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "D" /* INHERITANCE_FLAG_COL */
    },
            {
                "oamT6B2A1", /* NAME_COL */
                "Test Case TR6", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT6C3B2A1,oamT6C4B2A1", /* RELATIONSHIP_COL */
                "oamT6C2B1A1", /* INHERITANCE_COL */
                "D" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT6C1B1A1", /* NAME_COL */
                "Test Case TR6", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT6D1C1B1A1,oamT6D2C1B1A1", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "D" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT6C2B1A1", /* NAME_COL */
                "Test Case TR6", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT6D3C2B1A1,oamT6D4C2B1A1", /* RELATIONSHIP_COL */
                "oamT6D2C1B1A1", /* INHERITANCE_COL */
                "D" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT6C3B2A1", /* NAME_COL */
                "Test Case TR6", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT6D5C3B2A1,oamT6D6C3B2A1", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "D" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT6C4B2A1", /* NAME_COL */
                "Test Case TR6", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT6D7C4B2A1,oamT6D8C4B2A1", /* RELATIONSHIP_COL */
                "oamT6D6C3B2A1", /* INHERITANCE_COL */
                "D" /* INHERITANCE_FLAG_COL */
},
    };

    public static final Map<String, String> TR6_AUTHORIZED_USERS = new HashMap<>();
    static
    {
        TR6_AUTHORIZED_USERS
            .put(
                "oamT6A1",
                "jtsTU18User1,jtsTU18User2,jtsTU18User3,jtsTU18User4,jtsTU18User5,jtsTU18User6,jtsTU18User7,jtsTU18User8,jtsTU18User9,jtsTU18User10,jtsTU18User11,jtsTU18User12,jtsTU18User13,jtsTU18User14,jtsTU18User15" );
        TR6_AUTHORIZED_USERS.put( "oamT6B1A1",
            "jtsTU18User2,jtsTU18User4,jtsTU18User5,jtsTU18User8,jtsTU18User9,jtsTU18User10,jtsTU18User11" );
        TR6_AUTHORIZED_USERS
            .put(
                "oamT6B2A1",
                "jtsTU18User3,jtsTU18User5,jtsTU18User6,jtsTU18User7,jtsTU18User9,jtsTU18User10,jtsTU18User11,jtsTU18User12,jtsTU18User13,jtsTU18User14,jtsTU18User15" );
        TR6_AUTHORIZED_USERS.put( "oamT6C1B1A1", "jtsTU18User4,jtsTU18User8,jtsTU18User9" );
        TR6_AUTHORIZED_USERS.put( "oamT6C2B1A1", "jtsTU18User5,jtsTU18User9,jtsTU18User10,jtsTU18User11" );
        TR6_AUTHORIZED_USERS.put( "oamT6C3B2A1", "jtsTU18User6,jtsTU18User12,jtsTU18User13" );
        TR6_AUTHORIZED_USERS.put( "oamT6C4B2A1", "jtsTU18User7,jtsTU18User13,jtsTU18User14,jtsTU18User15" );
        TR6_AUTHORIZED_USERS.put( "oamT6D1C1B1A1", "jtsTU18User8" );
        TR6_AUTHORIZED_USERS.put( "oamT6D2C1B1A1", "jtsTU18User9" );
        TR6_AUTHORIZED_USERS.put( "oamT6D3C2B1A1", "jtsTU18User10" );
        TR6_AUTHORIZED_USERS.put( "oamT6D4C2B1A1", "jtsTU18User11" );
        TR6_AUTHORIZED_USERS.put( "oamT6D5C3B2A1", "jtsTU18User12" );
        TR6_AUTHORIZED_USERS.put( "oamT6D6C3B2A1", "jtsTU18User13" );
        TR6_AUTHORIZED_USERS.put( "oamT6D7C4B2A1", "jtsTU18User14" );
        TR6_AUTHORIZED_USERS.put( "oamT6D8C4B2A1", "jtsTU18User15" );
    }

    public static final String[][] ROLES_TR7_ASC =
        {
            {
                "oamT7D1C1B1A1", /* NAME_COL */
                "Test Case TR7", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT7C1B1A1", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "C" /* INHERITANCE_FLAG_COL */
        },
            {
                "oamT7D2C1B1A1", /* NAME_COL */
                "Test Case TR7", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT7C2B1A1", /* RELATIONSHIP_COL */
                "oamT7C1B1A1", /* INHERITANCE_COL */
                "C" /* INHERITANCE_FLAG_COL */
    },
            {
                "oamT7D3C2B1A1", /* NAME_COL */
                "Test Case TR7", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "oamT7C2B1A1", /* INHERITANCE_COL */
                "C" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT7D4C2B1A1", /* NAME_COL */
                "Test Case TR7", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "oamT7C2B1A1", /* INHERITANCE_COL */
                "C" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT7D5C3B2A1", /* NAME_COL */
                "Test Case TR7", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT7C3B2A1", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "C" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT7D6C3B2A1", /* NAME_COL */
                "Test Case TR7", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT7C4B2A1", /* RELATIONSHIP_COL */
                "oamT7C3B2A1", /* INHERITANCE_COL */
                "C" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT7D7C4B2A1", /* NAME_COL */
                "Test Case TR7", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "oamT7C4B2A1", /* INHERITANCE_COL */
                "C" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT7D8C4B2A1", /* NAME_COL */
                "Test Case TR7", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "oamT7C4B2A1", /* INHERITANCE_COL */
                "C" /* INHERITANCE_FLAG_COL */
},

            {
                "oamT7C1B1A1", /* NAME_COL */
                "Test Case TR7", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT7B1A1", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "A" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT7C2B1A1", /* NAME_COL */
                "Test Case TR7", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT7B2A1", /* RELATIONSHIP_COL */
                "oamT7B1A1", /* INHERITANCE_COL */
                "A" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT7C3B2A1", /* NAME_COL */
                "Test Case TR7", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "oamT7B2A1", /* INHERITANCE_COL */
                "A" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT7C4B2A1", /* NAME_COL */
                "Test Case TR7", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "oamT7B2A1", /* INHERITANCE_COL */
                "A" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT7B1A1", /* NAME_COL */
                "Test Case TR7", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT7A1", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "A" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT7B2A1", /* NAME_COL */
                "Test Case TR7", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "", /* RELATIONSHIP_COL */
                "oamT7A1", /* INHERITANCE_COL */
                "A" /* INHERITANCE_FLAG_COL */
},

    };

    public static final Map<String, String> TR7_AUTHORIZED_USERS = new HashMap<>();
    static
    {
        TR6_AUTHORIZED_USERS
            .put(
                "oamT7A1",
                "jtsTU19User1,jtsTU19User2,jtsTU19User3,jtsTU19User4,jtsTU19User5,jtsTU19User6,jtsTU19User7,jtsTU19User8,jtsTU19User9,jtsTU19User10,jtsTU19User11,jtsTU19User12,jtsTU19User13,jtsTU19User14,jtsTU19User15" );
        TR6_AUTHORIZED_USERS.put( "oamT7B1A1",
            "jtsTU19User2,jtsTU19User4,jtsTU19User5,jtsTU19User8,jtsTU19User9,jtsTU19User10,jtsTU19User11" );
        TR6_AUTHORIZED_USERS
            .put(
                "oamT7B2A1",
                "jtsTU19User3,jtsTU19User5,jtsTU19User6,jtsTU19User7,jtsTU19User9,jtsTU19User10,jtsTU19User11,jtsTU19User12,jtsTU19User13,jtsTU19User14,jtsTU19User15" );
        TR6_AUTHORIZED_USERS.put( "oamT7C1B1A1", "jtsTU19User4,jtsTU19User8,jtsTU19User9" );
        TR6_AUTHORIZED_USERS.put( "oamT7C2B1A1", "jtsTU19User5,jtsTU19User9,jtsTU19User10,jtsTU19User11" );
        TR6_AUTHORIZED_USERS.put( "oamT7C3B2A1", "jtsTU19User6,jtsTU19User12,jtsTU19User13" );
        TR6_AUTHORIZED_USERS.put( "oamT7C4B2A1", "jtsTU19User7,jtsTU19User13,jtsTU19User14,jtsTU19User15" );
        TR6_AUTHORIZED_USERS.put( "oamT7D1C1B1A1", "jtsTU19User8" );
        TR6_AUTHORIZED_USERS.put( "oamT7D2C1B1A1", "jtsTU19User9" );
        TR6_AUTHORIZED_USERS.put( "oamT7D3C2B1A1", "jtsTU19User10" );
        TR6_AUTHORIZED_USERS.put( "oamT7D4C2B1A1", "jtsTU19User11" );
        TR6_AUTHORIZED_USERS.put( "oamT7D5C3B2A1", "jtsTU19User12" );
        TR6_AUTHORIZED_USERS.put( "oamT7D6C3B2A1", "jtsTU19User13" );
        TR6_AUTHORIZED_USERS.put( "oamT7D7C4B2A1", "jtsTU19User14" );
        TR6_AUTHORIZED_USERS.put( "oamT7D8C4B2A1", "jtsTU19User15" );
    }

    public static final String[][] ROLES_TR8_SSD =
        {
            {
                "oamSSDR1", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
        },
            {
                "oamSSDR2", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
    },
            {
                "oamSSDR3", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamSSDR4", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamSSDR5", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamSSDR6", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamSSDR7", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamSSDR8", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamSSDR9", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamSSDR10", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamSSDR11", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamSSDR12", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamSSDR13", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamSSDR14", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamSSDR15", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamSSDR16", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamSSDR17", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamSSDR18", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamSSDR19", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamSSDR20", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
}
    };

    public static final String[][] ROLES_TR9_SSD =
        {
            {
                "oamT9SSDR1", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
        },
            {
                "oamT9SSDR2", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
    },
            {
                "oamT9SSDR3", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT9SSDR4", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT9SSDR5", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT9SSDR6", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT9SSDR7", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT9SSDR8", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT9SSDR9", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT9SSDR10", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
}
    };

    public static final String[][] ROLES_TR10_SSD =
        {
            {
                "oamT10SSDR1", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
        },
            {
                "oamT10SSDR2", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
    },
            {
                "oamT10SSDR3", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT10SSDR4", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT10SSDR5", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT10SSDR6", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT10SSDR7", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT10SSDR8", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT10SSDR9", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT10SSDR10", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
}
    };

    public static final String[][] ROLES_TR11_DESC_SSD =
        {
            {
                "oamT11SSD1", /* NAME_COL */
                "Test Case TR11", /* DESC_COL */
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
                "T" /* INHERITANCE_FLAG_COL */
        },
            {
                "oamT11SSD2", /* NAME_COL */
                "Test Case TR11", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT11SSD3", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
    },
            {
                "oamT11SSD4", /* NAME_COL */
                "Test Case TR11", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT11SSD5,oamT11SSD6", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT11SSD7", /* NAME_COL */
                "Test Case TR11", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT11SSD8,oamT11SSD9,oamT11SSD10", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
}
    };

    public static final String[][] ROLES_TR12_DESC_SSD =
        {
            {
                "oamT12SSD1", /* NAME_COL */
                "Test Case TR12", /* DESC_COL */
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
                "T" /* INHERITANCE_FLAG_COL */
        },
            {
                "oamT12SSD2", /* NAME_COL */
                "Test Case TR12", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT12SSD3", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
    },
            {
                "oamT12SSD4", /* NAME_COL */
                "Test Case TR12", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT12SSD5,oamT12SSD6", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT12SSD7", /* NAME_COL */
                "Test Case TR12", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT12SSD8,oamT12SSD9,oamT12SSD10", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
}
    };

    public static final String[][] ROLES_TR13_DESC_SSD =
        {
            {
                "oamT13SSD1", /* NAME_COL */
                "Test Case TR13", /* DESC_COL */
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
                "T" /* INHERITANCE_FLAG_COL */
        },
            {
                "oamT13SSD2", /* NAME_COL */
                "Test Case TR13", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT13SSD3", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
    },
            {
                "oamT13SSD4", /* NAME_COL */
                "Test Case TR13", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT13SSD5,oamT13SSD6", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT13SSD7", /* NAME_COL */
                "Test Case TR13", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT13SSD8,oamT13SSD9,oamT13SSD10", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
}
    };

    public static final String[][] ROLES_TR8_DSD =
        {
            {
                "oamDSDR1", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
        },
            {
                "oamDSDR2", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
    },
            {
                "oamDSDR3", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamDSDR4", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamDSDR5", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamDSDR6", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamDSDR7", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamDSDR8", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamDSDR9", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamDSDR10", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamDSDR11", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamDSDR12", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamDSDR13", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamDSDR14", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamDSDR15", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamDSDR16", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamDSDR17", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamDSDR18", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamDSDR19", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamDSDR20", /* NAME_COL */
                "Test Case TR8", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
}
    };

    public static final String[][] ROLES_TR9_DSD =
        {
            {
                "oamT9DSDR1", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
        },
            {
                "oamT9DSDR2", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
    },
            {
                "oamT9DSDR3", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT9DSDR4", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT9DSDR5", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT9DSDR6", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT9DSDR7", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT9DSDR8", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT9DSDR9", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT9DSDR10", /* NAME_COL */
                "Test Case TR9", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
}
    };

    public static final String[][] ROLES_TR10_DSD =
        {
            {
                "oamT10DSDR1", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
        },
            {
                "oamT10DSDR2", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
    },
            {
                "oamT10DSDR3", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT10DSDR4", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT10DSDR5", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT10DSDR6", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT10DSDR7", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT10DSDR8", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT10DSDR9", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT10DSDR10", /* NAME_COL */
                "Test Case TR10", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
}
    };

    public static final String[][] ROLES_TR11_DESC_DSD =
        {
            {
                "oamT11DSD1", /* NAME_COL */
                "Test Case TR11", /* DESC_COL */
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
                "T" /* INHERITANCE_FLAG_COL */
        },
            {
                "oamT11DSD2", /* NAME_COL */
                "Test Case TR11", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT11DSD3", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
    },
            {
                "oamT11DSD4", /* NAME_COL */
                "Test Case TR11", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT11DSD5,oamT11DSD6", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT11DSD7", /* NAME_COL */
                "Test Case TR11", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT11DSD8,oamT11DSD9,oamT11DSD10", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
}
    };

    public static final String[][] ROLES_TR12_DESC_DSD =
        {
            {
                "oamT12DSD1", /* NAME_COL */
                "Test Case TR12", /* DESC_COL */
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
                "T" /* INHERITANCE_FLAG_COL */
        },
            {
                "oamT12DSD2", /* NAME_COL */
                "Test Case TR12", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT12DSD3", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
    },
            {
                "oamT12DSD4", /* NAME_COL */
                "Test Case TR12", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT12DSD5,oamT12DSD6", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT12DSD7", /* NAME_COL */
                "Test Case TR12", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT12DSD8,oamT12DSD9,oamT12DSD10", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
}
    };

    public static final String[][] ROLES_TR13_DESC_DSD =
        {
            {
                "oamT13DSD1", /* NAME_COL */
                "Test Case TR13", /* DESC_COL */
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
                "T" /* INHERITANCE_FLAG_COL */
        },
            {
                "oamT13DSD2", /* NAME_COL */
                "Test Case TR13", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT13DSD3", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
    },
            {
                "oamT13DSD4", /* NAME_COL */
                "Test Case TR13", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT13DSD5,oamT13DSD6", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
},
            {
                "oamT13DSD7", /* NAME_COL */
                "Test Case TR13", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT13DSD8,oamT13DSD9,oamT13DSD10", /* RELATIONSHIP_COL */
                "", /* INHERITANCE_COL */
                "T" /* INHERITANCE_FLAG_COL */
}
    };

    public static final String[][] ROLES_TR14_ARBAC =
        {
            {
                "oamT14ROLE1", /* NAME_COL */
                "Test Case TR14", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT14ROLE2" /* PARENTS_COL */
        },
            {
                "oamT14ROLE2", /* NAME_COL */
                "Test Case TR14", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT14ROLE3" /* PARENTS_COL */

    },
            {
                "oamT14ROLE3", /* NAME_COL */
                "Test Case TR14", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT14ROLE4" /* PARENTS_COL */
},
            {
                "oamT14ROLE4", /* NAME_COL */
                "Test Case TR14", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT14ROLE5" /* PARENTS_COL */
},
            {
                "oamT14ROLE5", /* NAME_COL */
                "Test Case TR14", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT14ROLE6" /* PARENTS_COL */
},
            {
                "oamT14ROLE6", /* NAME_COL */
                "Test Case TR14", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT14ROLE7" /* PARENTS_COL */
},
            {
                "oamT14ROLE7", /* NAME_COL */
                "Test Case TR14", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT14ROLE8" /* PARENTS_COL */
},
            {
                "oamT14ROLE8", /* NAME_COL */
                "Test Case TR14", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT14ROLE9" /* PARENTS_COL */
},
            {
                "oamT14ROLE9", /* NAME_COL */
                "Test Case TR14", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "oamT14ROLE10" /* PARENTS_COL */
},
            {
                "oamT14ROLE10", /* NAME_COL */
                "Test Case TR14", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "" /* PARENTS_COL */
},
    };

    public static final String[][] ROLES_TR15_ARBAC =
        {
            {
                "T15ROLE1", /* NAME_COL */
                "Test Case TR15", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "" /* PARENTS_COL */
        },
            {
                "T15ROLE2", /* NAME_COL */
                "Test Case TR15", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "T15ROLE1" /* PARENTS_COL */
    },
            {
                "T15ROLE3", /* NAME_COL */
                "Test Case TR15", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "T15ROLE1" /* PARENTS_COL */
},
            {
                "T15ROLE4", /* NAME_COL */
                "Test Case TR15", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "T15ROLE2" /* PARENTS_COL */
},
            {
                "T15ROLE5", /* NAME_COL */
                "Test Case TR15", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "T15ROLE3" /* PARENTS_COL */
},
            {
                "T15ROLE6", /* NAME_COL */
                "Test Case TR15", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567", /* DAYMASK_COL */
                "T15ROLE5" /* PARENTS_COL */
}
    };

    public static final String[][] ROLES_TR16_SD =
        {
            {
                "oamT16SDR1", /* NAME_COL */
                "Test Case TR16", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
        },
            {
                "oamT16SDR2", /* NAME_COL */
                "Test Case TR16", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
    },
            {
                "oamT16SDR3", /* NAME_COL */
                "Test Case TR16", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT16SDR4", /* NAME_COL */
                "Test Case TR16", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT16SDR5", /* NAME_COL */
                "Test Case TR16", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT16SDR6", /* NAME_COL */
                "Test Case TR16", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT16SDR7", /* NAME_COL */
                "Test Case TR16", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT16SDR8", /* NAME_COL */
                "Test Case TR16", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT16SDR9", /* NAME_COL */
                "Test Case TR16", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT16SDR10", /* NAME_COL */
                "Test Case TR16", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
}
    };

    public static final String[][] ROLES_TR17_DSD_BRUNO =
        {
            {
                "oamT17DSD1", /* NAME_COL */
                "Test Case TR17", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
        },
            {
                "oamT17DSD2", /* NAME_COL */
                "Test Case TR17", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
    },
            {
                "oamT17DSD3", /* NAME_COL */
                "Test Case TR17", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
},
            {
                "oamT17DSD4", /* NAME_COL */
                "Test Case TR17", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
}
    };

    /**
    * The Fortress test data for junit uses 2-dimensional arrays.
    */
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

    final static int NUM_ASSIGNED_ROLES = 5;


    /**
     *
     * @param role
     * @param rle
     */
    public static void assertEquals( Role role, String[] rle )
    {
        Constraint validConstraint = RoleTestData.getRoleConstraint( rle );
        assertEquals( RoleTestData.class.getName() + ".assertEquals failed compare role name", getName( rle ),
            role.getName() );
        assertEquals( RoleTestData.class.getName() + ".assertEquals failed compare role desc", getDescription( rle ),
            role.getDescription() );
        TestUtils.assertTemporal( RoleTestData.class.getName() + ".assertEquals", validConstraint, role );
        LOG.debug( RoleTestData.class.getName() + ".assertEquals [" + role.getName() + "] successful" );
    }


    /**
     *
     * @param userId
     * @param uRole
     * @param urle
     */
    public static void assertEquals( String userId, UserRole uRole, String[] urle )
    {
        Constraint validConstraint = RoleTestData.getRoleConstraint( urle );
        assertEquals( RoleTestData.class.getName() + ".assertEquals failed compare userrole userId", userId,
            uRole.getUserId() );
        assertEquals( RoleTestData.class.getName() + ".assertEquals failed compare userrole name", getName( urle ),
            uRole.getName() );
        TestUtils.assertTemporal( RoleTestData.class.getName() + ".assertEquals", validConstraint, uRole );
        LOG.debug( RoleTestData.class.getName() + ".assertEquals userId [" + userId + "] role name [" + uRole.getName()
            + "] successful" );
    }


    /**
     *
     * @param rle
     * @return
     */
    public static String getName( String[] rle )
    {
        return rle[NAME_COL];
    }


    /**
     *
     * @param rle
     * @return
     */
    public static String getDescription( String[] rle )
    {
        return rle[DESC_COL];
    }


    /**
     *
     * @param rle
     * @return
     */
    public static String getBeginTime( String[] rle )
    {
        return rle[BTIME_COL];
    }


    /**
     *
     * @param rle
     * @return
     */
    public static String getEndTime( String[] rle )
    {
        return rle[ETIME_COL];
    }


    /**
     *
     * @param rle
     * @return
     */
    public static String getBeginDate( String[] rle )
    {
        return rle[BDATE_COL];
    }


    /**
     *
     * @param rle
     * @return
     */
    public static String getEndDate( String[] rle )
    {
        return rle[EDATE_COL];
    }


    /**
     *
     * @param rle
     * @return
     */
    public static String getBeginLockDate( String[] rle )
    {
        return rle[BLOCKDATE_COL];
    }


    /**
     *
     * @param rle
     * @return
     */
    public static String getEndLockDate( String[] rle )
    {
        return rle[ELOCKDATE_COL];
    }


    /**
     *
     * @param rle
     * @return
     */
    public static String getDayMask( String[] rle )
    {
        return rle[DAYMASK_COL];
    }


    /**
     *
     * @param rle
     * @return
     */
    public static Integer getTimeOut( String[] rle )
    {
        return Integer.parseInt( rle[TIMEOUT_COL] );
    }


    /**
     * 
     * @param rle
     * @return
     */
    public static Set<String> getRelationships( String[] rle )
    {
        Set<String> parents = new HashSet<>();
        if ( StringUtils.isNotEmpty( rle[RELATIONSHIP_COL] ) )
        {
            StringTokenizer charSetTkn = new StringTokenizer( rle[RELATIONSHIP_COL], TestUtils.DELIMITER_TEST_DATA );
            if ( charSetTkn.countTokens() > 0 )
            {
                while ( charSetTkn.hasMoreTokens() )
                {
                    String pRole = charSetTkn.nextToken();
                    parents.add( pRole );
                }
            }
        }
        return parents;
    }


    public static List<String> getRelationshipList( String[] rle )
    {
        List<String> parents = new ArrayList<>();
        if ( StringUtils.isNotEmpty( rle[RELATIONSHIP_COL] ) )
        {
            StringTokenizer charSetTkn = new StringTokenizer( rle[RELATIONSHIP_COL], TestUtils.DELIMITER_TEST_DATA );
            if ( charSetTkn.countTokens() > 0 )
            {
                while ( charSetTkn.hasMoreTokens() )
                {
                    String pRole = charSetTkn.nextToken();
                    parents.add( pRole );
                }
            }
        }
        return parents;
    }


    /**
     *
     * @param rle
     * @return
     */
    public static Set<String> getInheritances( String[] rle )
    {
        Set<String> rels = new HashSet<>();
        if ( StringUtils.isNotEmpty( rle[INHERITANCE_COL] ) )
        {
            StringTokenizer charSetTkn = new StringTokenizer( rle[INHERITANCE_COL], TestUtils.DELIMITER_TEST_DATA );
            if ( charSetTkn.countTokens() > 0 )
            {
                while ( charSetTkn.hasMoreTokens() )
                {
                    String pRole = charSetTkn.nextToken();
                    rels.add( pRole );
                }
            }
        }
        return rels;
    }


    /**
     * 
     * @param rle
     * @return
     */
    public static boolean isCreate( String[] rle )
    {
        boolean isAsc = false;
        if ( StringUtils.isNotEmpty( rle[INHERITANCE_FLAG_COL] ) && rle[INHERITANCE_FLAG_COL].equalsIgnoreCase( "C" ) )
        {
            isAsc = true;
        }

        return isAsc;
    }


    /**
     *
     * @param rle
     * @return
     */
    public static boolean isTree( String[] rle )
    {
        boolean result = false;
        if ( StringUtils.isNotEmpty( rle[INHERITANCE_FLAG_COL] ) && rle[INHERITANCE_FLAG_COL].equalsIgnoreCase( "T" ) )
        {
            result = true;
        }

        return result;
    }


    /**
     *
     * @param rle
     * @return
     */
    public static Role getRole( String[] rle )
    {
        Role role = ( Role ) getRoleConstraint( rle );
        role.setName( getName( rle ) );
        role.setDescription( getDescription( rle ) );
        return role;
    }


    /**
     *
     * @param userId
     * @param urle
     * @return
     */
    public static UserRole getUserRole( String userId, String[] urle )
    {
        UserRole uRole = ( UserRole ) getUserRoleConstraint( urle );
        uRole.setName( getName( urle ) );
        uRole.setUserId( userId );
        return uRole;
    }


    /**
     * @param rle
     * @return
     */
    public static Constraint getRoleConstraint( String[] rle )
    {
        Role role = new Role();
        role.setBeginDate( getBeginDate( rle ) );
        role.setEndDate( getEndDate( rle ) );
        role.setBeginLockDate( getBeginLockDate( rle ) );
        role.setEndLockDate( getEndLockDate( rle ) );
        role.setBeginTime( getBeginTime( rle ) );
        role.setEndTime( getEndTime( rle ) );
        role.setDayMask( getDayMask( rle ) );
        role.setTimeout( getTimeOut( rle ) );
        return role;
    }


    /**
     *
     * @param rle
     * @return
     */
    private static Constraint getUserRoleConstraint( String[] rle )
    {
        UserRole uRole = new UserRole();
        uRole.setBeginDate( getBeginDate( rle ) );
        uRole.setEndDate( getEndDate( rle ) );
        uRole.setBeginLockDate( getBeginLockDate( rle ) );
        uRole.setEndLockDate( getEndLockDate( rle ) );
        uRole.setBeginTime( getBeginTime( rle ) );
        uRole.setEndTime( getEndTime( rle ) );
        uRole.setDayMask( getDayMask( rle ) );
        uRole.setTimeout( getTimeOut( rle ) );
        return uRole;
    }

    /*
    Begin SSD Test Data Declarations:
     */

    // SSD Set columns:
    //private final static int NAME_COL = 0;
    //private final static int DESC_COL = 1;
    private final static int CARDINALITY = 2;
    private final static int MEMBERS = 3;


    /**
     *
     * @param rle
     * @return
     */
    public static int getCardinality( String[] rle )
    {
        return Integer.parseInt( rle[CARDINALITY] );
    }


    public static Set<String> getMembers( String[] rle )
    {
        Set<String> mems = new HashSet<>();
        if ( StringUtils.isNotEmpty( rle[MEMBERS] ) )
        {
            StringTokenizer charSetTkn = new StringTokenizer( rle[MEMBERS], TestUtils.DELIMITER_TEST_DATA );
            if ( charSetTkn.countTokens() > 0 )
            {
                while ( charSetTkn.hasMoreTokens() )
                {
                    String pRole = charSetTkn.nextToken();
                    mems.add( pRole );
                }
            }
        }
        return mems;
    }


    /**
     *
     * @param rle
     * @return
     */
    /*
    public static Map<String, String> getMembers(String[] rle)
    {
        Map<String, String> mems = new HashMap<>();
        if(org.apache.directory.fortress.core.util.attr.StringUtils.isNotEmpty(rle[MEMBERS]))
        {
            StringTokenizer charSetTkn = new StringTokenizer(rle[MEMBERS], TestUtils.DELIMITER_TEST_DATA);
            if (charSetTkn.countTokens() > 0)
            {
                while (charSetTkn.hasMoreTokens())
                {
                    String pRole = charSetTkn.nextToken();
                    mems.put(pRole, "");
                }
            }
        }
        return mems;
    }
    */

    /**
     *
     * @param ssdArry
     * @return
     */
    public static SDSet getSDSet( String[] ssdArry )
    {
        SDSet ssd = new SDSet();
        ssd.setName( getName( ssdArry ) );
        ssd.setDescription( getDescription( ssdArry ) );
        ssd.setCardinality( getCardinality( ssdArry ) );
        ssd.setMembers( getMembers( ssdArry ) );
        return ssd;
    }

    public static final String[][] SSD_T1 =
        {
            {
                "oamSsdTest1", /* NAME_COL */
                "Test Case T1", /* DESC_COL */
                "2", /* CARDINALITY */
                "oamSSDR1,oamSSDR2,oamSSDR3", /* RELATIONSHIP_COL */
        },
            {
                "oamSsdTest2", /* NAME_COL */
                "Test Case T1", /* DESC_COL */
                "2", /* CARDINALITY */
                "oamSSDR4,oamSSDR5,oamSSDR6", /* RELATIONSHIP_COL */
    },
            {
                "oamSsdTest3", /* NAME_COL */
                "Test Case T1", /* DESC_COL */
                "3", /* CARDINALITY */
                "oamSSDR7,oamSSDR8,oamSSDR9,oamSSDR10", /* RELATIONSHIP_COL */
},
            {
                "oamSsdTest4", /* NAME_COL */
                "Test Case T1", /* DESC_COL */
                "9", /* CARDINALITY */
                "oamSSDR11,oamSSDR12,oamSSDR13,oamSSDR14,oamSSDR15,oamSSDR16,oamSSDR17,oamSSDR18,oamSSDR19,oamSSDR20",
}
    };

    public static final String[][] SSD_T2 =
        {
            {
                "oamT2SsdTest", /* NAME_COL */
                "Test Case T2", /* DESC_COL */
                "2", /* CARDINALITY */
                "", /* RELATIONSHIP_COL */
        }
    };

    public static final String[][] SSD_T3 =
        {
            {
                "oamT3SsdTest", /* NAME_COL */
                "Test Case T3", /* DESC_COL */
                "2", /* CARDINALITY */
                "", /* RELATIONSHIP_COL */
        }
    };

    public static final String[][] SSD_T4 =
        {
            {
                "oamT4Ssd1", /* NAME_COL */
                "Test Case T4 A", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT11SSD1,oamT11SSD2,oamT11SSD4,oamT11SSD7", /* RELATIONSHIP_COL */
        }
    };

    public static final String[][] SSD_T4_B =
        {
            {
                "oamT4Ssd1", /* NAME_COL */
                "Test Case T4B", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT11SSD1,oamT11SSD3,oamT11SSD5,oamT11SSD8", /* RELATIONSHIP_COL */
        },
            {
                "oamT4Ssd2", /* NAME_COL */
                "Test Case T4", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT11SSD3,oamT11SSD6,oamT11SSD9", /* RELATIONSHIP_COL */
    },
            {
                "oamT4Ssd3", /* NAME_COL */
                "Test Case T4", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT11SSD6,oamT11SSD10", /* RELATIONSHIP_COL */
}
    };

    public static final String[][] SSD_T5 =
        {
            {
                "oamT5Ssd1", /* NAME_COL */
                "Test Case T5 A", /* DESC_COL */
                "3", /* CARDINALITY */
                "oamT12SSD3,oamT12SSD5,oamT12SSD8", /* RELATIONSHIP_COL */
        }
    };

    public static final String[][] SSD_T5_B =
        {
            {
                "oamT5Ssd1", /* NAME_COL */
                "Test Case T5B", /* DESC_COL */
                "3", /* CARDINALITY */
                "oamT12SSD3,oamT12SSD6,oamT12SSD9", /* RELATIONSHIP_COL */
        },
            {
                "oamT5Ssd2", /* NAME_COL */
                "Test Case T5B", /* DESC_COL */
                "3", /* CARDINALITY */
                "oamT12SSD3,oamT12SSD5,oamT12SSD8", /* RELATIONSHIP_COL */
    },
            {
                "oamT5Ssd3", /* NAME_COL */
                "Test Case T5B", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT12SSD2,oamT12SSD4,oamT12SSD7", /* RELATIONSHIP_COL */
}
    };

    public static final String[][] SSD_T6 =
        {
            {
                "oamT6Ssd1", /* NAME_COL */
                "Test Case T6 A", /* DESC_COL */
                "2", /* CARDINALITY */
                "oamT13SSD1,oamT13SSD6,oamT13SSD9", /* RELATIONSHIP_COL */
        }
    };

    public static final String[][] SSD_T6_B =
        {
            {
                "oamT6Ssd1", /* NAME_COL */
                "Test Case T6B", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT13SSD5,oamT13SSD5,oamT13SSD8", /* RELATIONSHIP_COL */
        },
            {
                "oamT6Ssd2", /* NAME_COL */
                "Test Case T6B", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT13SSD1,oamT13SSD3,oamT13SSD8", /* RELATIONSHIP_COL */
    },
            {
                "oamT6Ssd3", /* NAME_COL */
                "Test Case T6B", /* DESC_COL */
                "2", /* CARDINALITY */
                "oamT13SSD10,oamT13SSD1", /* RELATIONSHIP_COL */
//                "oamT13SSD10,oamT13SSD5,oamT13SSD1", /* RELATIONSHIP_COL */
}
    };

    public static final String[][] SSD_T7 =
        {
            {
                "oamT7Ssd1", /* NAME_COL */
                "Test Case T7", /* DESC_COL */
                "0", /* CARDINALITY */
                "", /* RELATIONSHIP_COL */
        },
            {
                "oamT7Ssd2", /* NAME_COL */
                "Test Case T7", /* DESC_COL */
                "0", /* CARDINALITY */
                "", /* RELATIONSHIP_COL */
    },
            {
                "oamT7Ssd3", /* NAME_COL */
                "Test Case T7", /* DESC_COL */
                "0", /* CARDINALITY */
                "", /* RELATIONSHIP_COL */
},
            {
                "oamT7Ssd4", /* NAME_COL */
                "Test Case T7", /* DESC_COL */
                "0", /* CARDINALITY */
                "", /* RELATIONSHIP_COL */
},
            {
                "oamT7Ssd5", /* NAME_COL */
                "Test Case T7", /* DESC_COL */
                "0", /* CARDINALITY */
                "", /* RELATIONSHIP_COL */
}
    };

    public static final String[][] DSD_T1 =
        {
            {
                "oamDsdTest1", /* NAME_COL */
                "Test Case T1", /* DESC_COL */
                "2", /* CARDINALITY */
                "oamDSDR1,oamDSDR2,oamDSDR3", /* RELATIONSHIP_COL */
        },
            {
                "oamDsdTest2", /* NAME_COL */
                "Test Case T1", /* DESC_COL */
                "2", /* CARDINALITY */
                "oamDSDR4,oamDSDR5,oamDSDR6", /* RELATIONSHIP_COL */
    },
            {
                "oamDsdTest3", /* NAME_COL */
                "Test Case T1", /* DESC_COL */
                "3", /* CARDINALITY */
                "oamDSDR7,oamDSDR8,oamDSDR9,oamDSDR10", /* RELATIONSHIP_COL */
},
            {
                "oamDsdTest4", /* NAME_COL */
                "Test Case T1", /* DESC_COL */
                "9", /* CARDINALITY */
                "oamDSDR11,oamDSDR12,oamDSDR13,oamDSDR14,oamDSDR15,oamDSDR16,oamDSDR17,oamDSDR18,oamDSDR19,oamDSDR20",
}
    };

    public static final String[][] DSD_T2 =
        {
            {
                "oamT2DsdTest", /* NAME_COL */
                "Test Case T2", /* DESC_COL */
                "2", /* CARDINALITY */
                "", /* RELATIONSHIP_COL */
        }
    };

    public static final String[][] DSD_T3 =
        {
            {
                "oamT3DsdTest", /* NAME_COL */
                "Test Case T3", /* DESC_COL */
                "2", /* CARDINALITY */
                "", /* RELATIONSHIP_COL */
        }
    };

    public static final String[][] DSD_T4 =
        {
            {
                "oamT4Dsd1", /* NAME_COL */
                "Test Case T4 A", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT11DSD1,oamT11DSD2,oamT11DSD4,oamT11DSD7", /* RELATIONSHIP_COL */
        }
    };

    public static final String[][] DSD_T4_B =
        {
            {
                "oamT4Dsd1", /* NAME_COL */
                "Test Case T4B", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT11DSD1,oamT11DSD3,oamT11DSD5,oamT11DSD8", /* RELATIONSHIP_COL */
        },
            {
                "oamT4Dsd2", /* NAME_COL */
                "Test Case T4", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT11DSD3,oamT11DSD6,oamT11DSD9", /* RELATIONSHIP_COL */
    },
            {
                "oamT4Dsd3", /* NAME_COL */
                "Test Case T4", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT11DSD6,oamT11DSD10", /* RELATIONSHIP_COL */
}
    };

    public static final String[][] DSD_T5 =
        {
            {
                "oamT5Dsd1", /* NAME_COL */
                "Test Case T5 A", /* DESC_COL */
                "3", /* CARDINALITY */
                "oamT12DSD3,oamT12DSD5,oamT12DSD8", /* RELATIONSHIP_COL */
        }
    };

    public static final String[][] DSD_T5_B =
        {
            {
                "oamT5Dsd1", /* NAME_COL */
                "Test Case T5B", /* DESC_COL */
                "3", /* CARDINALITY */
                "oamT12DSD3,oamT12DSD6,oamT12DSD9", /* RELATIONSHIP_COL */
        },
            {
                "oamT5Dsd2", /* NAME_COL */
                "Test Case T5B", /* DESC_COL */
                "3", /* CARDINALITY */
                "oamT12DSD3,oamT12DSD5,oamT12DSD8", /* RELATIONSHIP_COL */
    },
            {
                "oamT5Dsd3", /* NAME_COL */
                "Test Case T5B", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT12DSD2,oamT12DSD4,oamT12DSD7", /* RELATIONSHIP_COL */
}
    };

    public static final String[][] DSD_T6 =
        {
            {
                "oamT6Dsd1", /* NAME_COL */
                "Test Case T6 A", /* DESC_COL */
                "2", /* CARDINALITY */
                "oamT13DSD1,oamT13DSD6,oamT13DSD9", /* RELATIONSHIP_COL */
        }
    };

    public static final String[][] DSD_T6_B =
        {
            {
                "oamT6Dsd1", /* NAME_COL */
                "Test Case T6B", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT13DSD5,oamT13DSD5,oamT13DSD8", /* RELATIONSHIP_COL */
        },
            {
                "oamT6Dsd2", /* NAME_COL */
                "Test Case T6B", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT13DSD1,oamT13DSD3,oamT13DSD8", /* RELATIONSHIP_COL */
    },
            {
                "oamT6Dsd3", /* NAME_COL */
                "Test Case T6B", /* DESC_COL */
                "2", /* WORKS FOR ADD ACTIVE ROLE DSD TESTS */
                "oamT13DSD10,oamT13DSD5,oamT13DSD1", /* RELATIONSHIP_COL */
}
    };

    public static final String[][] DSD_T6_C =
        {
            {
                "oamT6Dsd1", /* NAME_COL */
                "Test Case T6B", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT13DSD5,oamT13DSD5,oamT13DSD8", /* RELATIONSHIP_COL */
        },
            {
                "oamT6Dsd2", /* NAME_COL */
                "Test Case T6B", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT13DSD1,oamT13DSD3,oamT13DSD8", /* RELATIONSHIP_COL */
    },
            {
                "oamT6Dsd3", /* NAME_COL */
                "Test Case T6B", /* DESC_COL */
                "3", /* WORKS FOR CREATE SESSION DSD TESTS */
                "oamT13DSD10,oamT13DSD5,oamT13DSD1", /* RELATIONSHIP_COL */
}
    };

    public static final String[][] DSD_T6_D =
        {
            {
                "oamT6Dsd1", /* NAME_COL */
                "Test Case T6D", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT13DSD5,oamT13DSD5,oamT13DSD8", /* RELATIONSHIP_COL */
            },
            {
                "oamT6Dsd2", /* NAME_COL */
                "Test Case T6D", /* DESC_COL */
                "4", /* CARDINALITY */
                "oamT13DSD1,oamT13DSD3,oamT13DSD8", /* RELATIONSHIP_COL */
            },
            {
                "oamT6Dsd3", /* NAME_COL */
                "Test Case T6D", /* DESC_COL */
                "2", /* WORKS FOR ADD ACTIVE ROLE DSD TESTS */
                "oamT13DSD10,oamT13DSD1", /* RELATIONSHIP_COL */
            }
        };

    public static final String[][] DSD_T7 =
        {
            {
                "oamT7Dsd1", /* NAME_COL */
                "Test Case T7", /* DESC_COL */
                "0", /* CARDINALITY */
                "", /* RELATIONSHIP_COL */
        },
            {
                "oamT7Dsd2", /* NAME_COL */
                "Test Case T7", /* DESC_COL */
                "0", /* CARDINALITY */
                "", /* RELATIONSHIP_COL */
    },
            {
                "oamT7Dsd3", /* NAME_COL */
                "Test Case T7", /* DESC_COL */
                "0", /* CARDINALITY */
                "", /* RELATIONSHIP_COL */
},
            {
                "oamT7Dsd4", /* NAME_COL */
                "Test Case T7", /* DESC_COL */
                "0", /* CARDINALITY */
                "", /* RELATIONSHIP_COL */
},
            {
                "oamT7Dsd5", /* NAME_COL */
                "Test Case T7", /* DESC_COL */
                "0", /* CARDINALITY */
                "", /* RELATIONSHIP_COL */
}
    };

    public static final String[][] DSD_T8_BRUNO =
        {
            {
                "oamT8Dsd1", /* NAME_COL */
                "Test Case T8", /* DESC_COL */
                "2", /* CARDINALITY */
                "oamT17DSD1,oamT17DSD2", /* RELATIONSHIP_COL */
        },
            {
                "oamT8Dsd2", /* NAME_COL */
                "Test Case T8", /* DESC_COL */
                "2", /* CARDINALITY */
                "oamT17DSD3,oamT17DSD4", /* RELATIONSHIP_COL */
    },
            {
                "oamT8Dsd3", /* NAME_COL */
                "Test Case T8", /* DESC_COL */
                "2", /* CARDINALITY */
                "oamT17DSD1,oamT17DSD3", /* RELATIONSHIP_COL */
}
    };


    public static final String[][] ROLES_ABAC_WASHERS =
        {
            {
                "Washers", /* NAME_COL */
                "May wash currency", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
            },
        };

    public static final String[][] ROLES_ABAC_TELLERS =
        {
            {
                "Tellers", /* NAME_COL */
                "May transact on customer accounts", /* DESC_COL */
                "30", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
            },
        };

    public static final String[][] ROLES_ABAC_USERS =
        {
            {
                "Bank_Users", /* NAME_COL */
                "Basic rights to log into the web app", /* DESC_COL */
                "0", /* TIMEOUT_COL */
                "0000", /* BTIME_COL */
                "0000", /* ETIME_COL */
                "20090101", /* BDATE_COL */
                "21000101", /* EDATE_COL */
                "20500101", /* BLOCKDATE_COL */
                "20500115", /* ELOCKDATE_COL */
                "1234567" /* DAYMASK_COL */
            },
        };

    public static final String[][] DSD_TR18_ABAC =
        {
            {
                "BankSafe", /* NAME_COL */
                "User may only activate one of these roles", /* DESC_COL */
                "2", /* CARDINALITY */
                "Tellers,Washers", /* RELATIONSHIP_COL */
            }
        };

    /**
     * The Fortress test data for junit uses 2-dimensional arrays.
     */
    private final static int C_UID_COL = 0;
    private final static int C_ROLE_COL = 1;
    private final static int C_KEY_COL = 2;
    private final static int C_VALUE_COL = 3;
    private final static int C_TYPE_COL = 4;

    public static String getConstraintUserid( String[] rle )
    {
        return rle[C_UID_COL];
    }
    public static String getConstraintRole( String[] rle )
    {
        return rle[C_ROLE_COL];
    }
    public static String getConstraintKey( String[] rle )
    {
        return rle[C_KEY_COL];
    }
    public static String getConstraintValue( String[] rle )
    {
        return rle[C_VALUE_COL];
    }
    public static String getConstraintType( String[] rle )
    {
        return rle[C_TYPE_COL];
    }

    public static UserRole getUserRoleConstraintAbac( String[] constraint )
    {
        UserRole uRole = new UserRole();
        uRole.setUserId( getConstraintUserid( constraint ) );
        uRole.setName( getConstraintRole( constraint ) );
        RoleConstraint rConstraint = new RoleConstraint();
        rConstraint.setTypeName( getConstraintType( constraint ) );
        rConstraint.setKey( getConstraintKey( constraint ) );
        rConstraint.setValue( getConstraintValue( constraint ) );
        List<RoleConstraint> constraints = new ArrayList();
        constraints.add( rConstraint );
        uRole.setRoleConstraints( constraints );
        return uRole;
    }


    public static final String[][] ROLE_CONSTRAINTS_TR18_ABAC =
        {
            {
                "curly", /* USERID */
                "tellers", /* ROLE NAME */
                "locale", /* KEY */
                "east", /* VALUE */
                "USER" /* TYPE */
            },
            {
                "curly", /* USERID */
                "washers", /* ROLE NAME */
                "locale", /* KEY */
                "north", /* VALUE */
                "USER" /* TYPE */
            },
            {
                "curly", /* USERID */
                "washers", /* ROLE NAME */
                "locale", /* KEY */
                "south", /* VALUE */
                "USER" /* TYPE */
            },
            {
                "moe", /* USERID */
                "tellers", /* ROLE NAME */
                "locale", /* KEY */
                "north", /* VALUE */
                "USER" /* TYPE */
            },
            {
                "moe", /* USERID */
                "washers", /* ROLE NAME */
                "locale", /* KEY */
                "east", /* VALUE */
                "USER" /* TYPE */
            },
            {
                "moe", /* USERID */
                "washers", /* ROLE NAME */
                "locale", /* KEY */
                "south", /* VALUE */
                "USER" /* TYPE */
            },
            {
                "larry", /* USERID */
                "tellers", /* ROLE NAME */
                "locale", /* KEY */
                "south", /* VALUE */
                "USER" /* TYPE */
            },
            {
                "larry", /* USERID */
                "washers", /* ROLE NAME */
                "locale", /* KEY */
                "north", /* VALUE */
                "USER" /* TYPE */
            },
            {
                "larry", /* USERID */
                "washers", /* ROLE NAME */
                "locale", /* KEY */
                "east", /* VALUE */
                "USER" /* TYPE */
            },
        };

    public static final String[][] ROLE_CONSTRAINTS_TR18_ROLES =
        {
            {
                "", /* USERID */
                "Tellers", /* ROLE NAME */
                "locale", /* KEY */
                "", /* VALUE */
                "USER" /* TYPE */
            },
            {
                "", /* USERID */
                "Washers", /* ROLE NAME */
                "locale", /* KEY */
                "", /* VALUE */
                "USER" /* TYPE */
            }
        };

}