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

import org.apache.directory.fortress.core.model.ComparisonOperator;
import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.PermissionAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;


/**
 * Description of the Class
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class PermTestData extends TestCase
{
    private static final Logger LOG = LoggerFactory.getLogger( PermTestData.class.getName() );

    // Fortress Test Objects:
    /**
     * Test Case TOB1:
     */
    public static final String[][] OBJS_TOB1 =
        {
            {
                "TOB1_1", /* NAME_COL */
                "Test Case TOB1", /* DESC_COL */
                "APP1", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
        },
            {
                "TOB1_2", /* NAME_COL */
                "Test Case TOB1", /* DESC_COL */
                "APP2", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
    },
            {
                "TOB1_3", /* NAME_COL */
                "Test Case TOB1", /* DESC_COL */
                "APP3", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB1_4", /* NAME_COL */
                "Test Case TOB1", /* DESC_COL */
                "APP4", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
}
    };

    /**
     * Test Case TOB1:
     */
    public static final String[][] OBJS_TOB2 =
        {
            {
                "TOB2_1", /* NAME_COL */
                "Test Case TOB2", /* DESC_COL */
                "APP1", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
        },
            {
                "TOB2_2", /* NAME_COL */
                "Test Case TOB2", /* DESC_COL */
                "APP2", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
    },
            {
                "TOB2_3", /* NAME_COL */
                "Test Case TOB3", /* DESC_COL */
                "APP3", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB2_4", /* NAME_COL */
                "Test Case TOB2", /* DESC_COL */
                "APP4", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
}
    };

    /**
     * Test Case TOB1:
     */
    public static final String[][] OBJS_TOB3 =
        {
            {
                "TOB3_1", /* NAME_COL */
                "Test Case TOB3", /* DESC_COL */
                "APP1", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
        },
            {
                "TOB3_2", /* NAME_COL */
                "Test Case TOB3", /* DESC_COL */
                "APP2", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
    },
            {
                "TOB3_3", /* NAME_COL */
                "Test Case TOB3", /* DESC_COL */
                "APP3", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB3_4", /* NAME_COL */
                "Test Case TOB3", /* DESC_COL */
                "APP4", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
}
    };

    /**
     * Test Case TOB4:
     */
    public static final String[][] OBJS_TOB4 =
        {
            {
                "TOB4_1", /* NAME_COL */
                "Test Case TOB4", /* DESC_COL */
                "APP1", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
        },
            {
                "TOB4_2", /* NAME_COL */
                "Test Case TOB4", /* DESC_COL */
                "APP2", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
    },
            {
                "TOB4_3", /* NAME_COL */
                "Test Case TOB4", /* DESC_COL */
                "APP3", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB4_4", /* NAME_COL */
                "Test Case TOB4", /* DESC_COL */
                "APP4", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB4_5", /* NAME_COL */
                "Test Case TOB4", /* DESC_COL */
                "APP4", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB4_6", /* NAME_COL */
                "Test Case TOB4", /* DESC_COL */
                "APP4", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB4_7", /* NAME_COL */
                "Test Case TOB4", /* DESC_COL */
                "APP4", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB4_8", /* NAME_COL */
                "Test Case TOB4", /* DESC_COL */
                "APP4", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB4_9", /* NAME_COL */
                "Test Case TOB4", /* DESC_COL */
                "APP4", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB4_10", /* NAME_COL */
                "Test Case TOB4", /* DESC_COL */
                "APP4", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
}
    };

    /**
     * Test Case TOB4_UPD:
     */
    public static final String[][] OBJS_TOB4_UPD =
        {
            {
                "TOB4_1", /* NAME_COL */
                "Updated TOB4", /* DESC_COL */
                "APP1", /* ORG_COL */
                "TST1", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
        },
            {
                "TOB4_2", /* NAME_COL */
                "Updated TOB4", /* DESC_COL */
                "APP2", /* ORG_COL */
                "TST2", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
    },
            {
                "TOB4_3", /* NAME_COL */
                "Updated TOB4", /* DESC_COL */
                "APP3", /* ORG_COL */
                "TST3", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB4_4", /* NAME_COL */
                "Updated TOB4", /* DESC_COL */
                "APP4", /* ORG_COL */
                "TST4", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB4_5", /* NAME_COL */
                "Updated TOB4", /* DESC_COL */
                "APP4", /* ORG_COL */
                "TST5", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB4_6", /* NAME_COL */
                "Updated TOB4", /* DESC_COL */
                "APP4", /* ORG_COL */
                "TST6", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB4_7", /* NAME_COL */
                "Updated TOB4", /* DESC_COL */
                "APP4", /* ORG_COL */
                "TST7", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB4_8", /* NAME_COL */
                "Updated TOB4", /* DESC_COL */
                "APP4", /* ORG_COL */
                "TST8", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB4_9", /* NAME_COL */
                "Updated TOB4", /* DESC_COL */
                "APP4", /* ORG_COL */
                "TST9", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB4_10", /* NAME_COL */
                "Updated TOB4", /* DESC_COL */
                "APP4", /* ORG_COL */
                "TST10", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
}
    };

    public static final String[][] OBJS_TOB5 =
        {
            {
                "TOB5_1", /* NAME_COL */
                "Test Case TOB5", /* DESC_COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[0] ),
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
        },
            {
                "TOB5_2", /* NAME_COL */
                "Test Case TOB5", /* DESC_COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[1] ),
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
    },
            {
                "TOB5_3", /* NAME_COL */
                "Test Case TOB5", /* DESC_COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[2] ),
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB5_4", /* NAME_COL */
                "Test Case TOB5", /* DESC_COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[3] ),
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOB5_5", /* NAME_COL */
                "Test Case TOB5", /* DESC_COL */
                OrgUnitTestData.getName( OrgUnitTestData.ORGS_PRM_TO5[4] ),
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
}
    };

    public static final String[][] OBJS_TOB6 =
        {
            {
                "TOB6_1", /* NAME_COL */
                "Hierarchcial Tests TR5", /* DESC_COL */
                "APP1", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
        }
    };

    /**
     * Test Case TOP1:
     */
    public static final String[][] OPS_TOP5 =
        {
            {
                "TOP5_1", /* NAME_COL */
                "Test Case TOP5", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
        },
            {
                "TOP5_2", /* NAME_COL */
                "Test Case TOP5", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
    },
            {
                "TOP5_3", /* NAME_COL */
                "Test Case TOP5", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP5_4", /* NAME_COL */
                "Test Case TOP5", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP5_5", /* NAME_COL */
                "Test Case TOP5", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP5_6", /* NAME_COL */
                "Test Case TOP5", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP5_7", /* NAME_COL */
                "Test Case TOP5", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP5_8", /* NAME_COL */
                "Test Case TOP5", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP5_9", /* NAME_COL */
                "Test Case TOP5", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP5_10", /* NAME_COL */
                "Test Case TOP5", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
}
    };

    /**
     * Test Case TOP1:
     */
    public static final String[][] OPS_TOP1 =
        {
            {
                "TOP1_1", /* NAME_COL */
                "Test Case TOP1", /* DESC_COL */
                "001", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
        },
            {
                "TOP1_2", /* NAME_COL */
                "Test Case TOP1", /* DESC_COL */
                "002", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
    },
            {
                "TOP1_3", /* NAME_COL */
                "Test Case TOP1", /* DESC_COL */
                "003", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP1_4", /* NAME_COL */
                "Test Case TOP1", /* DESC_COL */
                "004", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP1_5", /* NAME_COL */
                "Test Case TOP1", /* DESC_COL */
                "005", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP1_6", /* NAME_COL */
                "Test Case TOP1", /* DESC_COL */
                "006", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP1_7", /* NAME_COL */
                "Test Case TOP1", /* DESC_COL */
                "007", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP1_8", /* NAME_COL */
                "Test Case TOP1", /* DESC_COL */
                "008", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP1_9", /* NAME_COL */
                "Test Case TOP1", /* DESC_COL */
                "009", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP1_10", /* NAME_COL */
                "Test Case TOP1", /* DESC_COL */
                "010", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
}
    };

    /**
     * Test Case TOP1:
     */
    public static final String[][] OPS_TOP1_UPD =
        {
            {
                "TOP1_1", /* NAME_COL */
                "TOP1 Updated", /* DESC_COL */
                "001", /* OBJ_ID_COL */
                "TST1", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
        },
            {
                "TOP1_2", /* NAME_COL */
                "TOP1 Updated", /* DESC_COL */
                "002", /* OBJ_ID_COL */
                "TST2", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
    },
            {
                "TOP1_3", /* NAME_COL */
                "TOP1 Updated", /* DESC_COL */
                "003", /* OBJ_ID_COL */
                "TST3", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP1_4", /* NAME_COL */
                "TOP1 Updated", /* DESC_COL */
                "004", /* OBJ_ID_COL */
                "TST4", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP1_5", /* NAME_COL */
                "TOP1 Updated", /* DESC_COL */
                "005", /* OBJ_ID_COL */
                "TST5", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP1_6", /* NAME_COL */
                "TOP1 Updated", /* DESC_COL */
                "006", /* OBJ_ID_COL */
                "TST6", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP1_7", /* NAME_COL */
                "TOP1 Updated", /* DESC_COL */
                "007", /* OBJ_ID_COL */
                "TST7", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP1_8", /* NAME_COL */
                "TOP1 Updated", /* DESC_COL */
                "008", /* OBJ_ID_COL */
                "TST8", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP1_9", /* NAME_COL */
                "TOP1 Updated", /* DESC_COL */
                "009", /* OBJ_ID_COL */
                "TST9", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP1_10", /* NAME_COL */
                "TOP1 Updated", /* DESC_COL */
                "010", /* OBJ_ID_COL */
                "TST10", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
}
    };

    public static final String[][] OPS_TOP2 =
        {
            {
                "TOP2_1", /* NAME_COL */
                "Test Case TOP2", /* DESC_COL */
                "001", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
        },
            {
                "TOP2_2", /* NAME_COL */
                "Test Case TOP2", /* DESC_COL */
                "002", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
    },
            {
                "TOP2_3", /* NAME_COL */
                "Test Case TOP2", /* DESC_COL */
                "003", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP2_4", /* NAME_COL */
                "Test Case TOP2", /* DESC_COL */
                "004", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP2_5", /* NAME_COL */
                "Test Case TOP2", /* DESC_COL */
                "005", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP2_6", /* NAME_COL */
                "Test Case TOP2", /* DESC_COL */
                "006", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP2_7", /* NAME_COL */
                "Test Case TOP2", /* DESC_COL */
                "007", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP2_8", /* NAME_COL */
                "Test Case TOP2", /* DESC_COL */
                "008", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP2_9", /* NAME_COL */
                "Test Case TOP2", /* DESC_COL */
                "009", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP2_10", /* NAME_COL */
                "Test Case TOP2", /* DESC_COL */
                "010", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
}
    };

    public static final String[][] OPS_TOP3 =
        {
            {
                "TOP3_1", /* NAME_COL */
                "Test Case TOP3", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
        },
            {
                "TOP3_2", /* NAME_COL */
                "Test Case TOP3", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
    },
            {
                "TOP3_3", /* NAME_COL */
                "Test Case TOP3", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP3_4", /* NAME_COL */
                "Test Case TOP3", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP3_5", /* NAME_COL */
                "Test Case TOP3", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP3_6", /* NAME_COL */
                "Test Case TOP3", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP3_7", /* NAME_COL */
                "Test Case TOP3", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP3_8", /* NAME_COL */
                "Test Case TOP3", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP3_9", /* NAME_COL */
                "Test Case TOP3", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP3_10", /* NAME_COL */
                "Test Case TOP3", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
}
    };

    public static final String[][] OPS_TOP4 =
        {
            {
                "TOP4_1", /* NAME_COL */
                "Test Case TOP4", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
        },
            {
                "TOP4_2", /* NAME_COL */
                "Test Case TOP4", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
    },
            {
                "TOP4_3", /* NAME_COL */
                "Test Case TOP4", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP4_4", /* NAME_COL */
                "Test Case TOP4", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP4_5", /* NAME_COL */
                "Test Case TOP4", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP4_6", /* NAME_COL */
                "Test Case TOP4", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP4_7", /* NAME_COL */
                "Test Case TOP4", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP4_8", /* NAME_COL */
                "Test Case TOP4", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP4_9", /* NAME_COL */
                "Test Case TOP4", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
},
            {
                "TOP4_10", /* NAME_COL */
                "Test Case TOP4", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
}
    };

    public static final String[][] ARBAC_OBJS_1 =
        {
            {
                "DelAdminMgr",/* NAME_COL */
                "ARBAC02 policies", /* DESC_COL */
                "APP1", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "T" /* IS_ADMIN_COL */
        }
    };

    /**
    * Test Case TOP1:
    */
    public static final String[][] ARBAC_OPS_1 =
        {
            {
                "addRole", /* NAME_COL */
                "Add Admin Role", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
        },
            {
                "deleteRole", /* NAME_COL */
                "Delete Admin Role",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
    },
            {
                "updateRole", /* NAME_COL */
                "Update Admin Role",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "assignUser", /* NAME_COL */
                "Assign Admin Role",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deassignUser", /* NAME_COL */
                "Deassign Admin Role",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "enableRoleConstraint", /* NAME_COL */
                "Assign Admin Role",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "disableRoleConstraint", /* NAME_COL */
                "Deassign Admin Role",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addRoleConstraint", /* NAME_COL */
                "Assign Admin Role",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "removeRoleConstraint", /* NAME_COL */
                "Deassign Admin Role",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addOrgUnit", /* NAME_COL */
                "Add Org Unit", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPSARBAC_OBJS_1_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "updateOrgUnit", /* NAME_COL */
                "Update Org Unit", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deleteOrgUnit", /* NAME_COL */
                "Delete Org Unit", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addDescendant", /* NAME_COL */
                "Add Org Unit Desc",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addInheritance", /* NAME_COL */
                "Add Org Unit Inheritance",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deleteInheritance",/* NAME_COL */
                "Delete Org Unit Inheritance",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addAdminObject", /* NAME_COL */
                "Add Admin Object", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "updateAdminObject",/* NAME_COL */
                "Update Admin Object",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deleteAdminObject",/* NAME_COL */
                "Delete Admin Object",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addPermObj", /* NAME_COL */
                "Add Admin Permission",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "updatePermObj", /* NAME_COL */
                "Update Admin Permission",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deletePermObj", /* NAME_COL */
                "Delete Admin Perm",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "grantPermission", /* NAME_COL */
                "Grant Admin Perm", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "revokePermission", /* NAME_COL */
                "Revoke Admin Perm",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "canAssign", /* NAME_COL */
                "Can Assign Role", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "canDeassign", /* NAME_COL */
                "Can Deassign Role",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "canGrant", /* NAME_COL */
                "Can Grant Perm", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "canRevoke", /* NAME_COL */
                "Can Revoke Perm", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
}
    };

    public static final String[][] ARBAC_OBJ2 =
        {
            {
                "AROBJ2_1", /* NAME_COL */
                "Test Case AROBJ_2",/* DESC_COL */
                "APP1", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "T" /* IS_ADMIN_COL */
        },
            {
                "AROBJ2_2", /* NAME_COL */
                "Test Case AROBJ_2",/* DESC_COL */
                "APP1", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "T" /* IS_ADMIN_COL */
    },
            {
                "AROBJ2_3", /* NAME_COL */
                "Test Case AROBJ_2",/* DESC_COL */
                "APP1", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "AROBJ2_4", /* NAME_COL */
                "Test Case AROBJ_2",/* DESC_COL */
                "APP1", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "AROBJ2_5", /* NAME_COL */
                "Test Case AROBJ_2",/* DESC_COL */
                "APP1", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "T" /* IS_ADMIN_COL */
},
    };

    public static final String[][] ARBAC_OPS_2 =
        {
            {
                "AROP2_1", /* NAME_COL */
                "Test Case AROP2", /* DESC_COL */
                "001", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
        },
            {
                "AROP2_2", /* NAME_COL */
                "Test Case AROP2", /* DESC_COL */
                "001", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
    },
            {
                "AROP2_3", /* NAME_COL */
                "Test Case AROP2", /* DESC_COL */
                "001", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "AROP2_4", /* NAME_COL */
                "Test Case AROP2", /* DESC_COL */
                "001", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "AROP2_5", /* NAME_COL */
                "Test Case AROP2", /* DESC_COL */
                "001", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "AROP2_6", /* NAME_COL */
                "Test Case AROP2", /* DESC_COL */
                "001", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "AROP2_7", /* NAME_COL */
                "Test Case AROP2", /* DESC_COL */
                "001", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "AROP2_8", /* NAME_COL */
                "Test Case AROP2", /* DESC_COL */
                "001", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "AROP2_9", /* NAME_COL */
                "Test Case AROP2", /* DESC_COL */
                "001", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "AROP2_10", /* NAME_COL */
                "Test Case AROP2", /* DESC_COL */
                "001", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
}
    };

    public static final String[][] PSWDMGR_OBJ =
        {
            {
                "org.apache.directory.fortress.core.impl.PwPolicyMgrImpl",
                "ARBAC02 policies", /* DESC_COL */
                "APP0", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "T" /* IS_ADMIN_COL */
        }
    };

    /**
    * Test Case TOP1:
    */
    public static final String[][] PSWDMGR_OPS =
        {
            {
                "add", /* NAME_COL */
                "PasswordMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
        },
            {
                "update", /* NAME_COL */
                "PasswordMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
    },
            {
                "delete", /* NAME_COL */
                "PasswordMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "updateUserPolicy", /* NAME_COL */
                "PasswordMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deletePasswordPolicy", /* NAME_COL */
                "PasswordMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "search", /* NAME_COL */
                "PasswordMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "read", /* NAME_COL */
                "PasswordMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
    };

    public static final String[][] ADMINMGR_OBJ =
        {
            {
                "org.apache.directory.fortress.core.impl.AdminMgrImpl",
                "ARBAC02 policies", /* DESC_COL */
                "APP0", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "T" /* IS_ADMIN_COL */
        }
    };

    /**
    * Test Case TOP1:
    */
    public static final String[][] ADMINMGR_OPS =
        {
            {
                "addUser", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
        },
            {
                "disableUser", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
    },
            {
                "deleteUser", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "updateUser", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "changePassword", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "lockUserAccount", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "unlockUserAccount", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "resetPassword", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addRole", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deleteRole", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "updateRole", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "assignUser", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deassignUser", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addPermission", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addPermObj", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deletePermission", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deletePermObj", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "updatePermission", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "updatePermObj", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "grantPermission", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "revokePermission", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "grantPermissionUser", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "revokePermissionUser", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addDescendant", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addAscendant", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addInheritance", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deleteInheritance", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "createSsdSet", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "updateSsdSet", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addSsdRoleMember", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deleteSsdRoleMember", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deleteSsdSet", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "setSsdSetCardinality", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "createDsdSet", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "updateDsdSet", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addDsdRoleMember", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deleteDsdRoleMember", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deleteDsdSet", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "setDsdSetCardinality", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addPermissionAttributeSet", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addPermissionAttributeToSet", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deletePermissionAttributeSet", /* NAME_COL */
                "AdminMgr Operation", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
/*
                    "addPermissionAttributeToSet"
                    "removePermissionAttributeFromSet"
                    "updatePermissionAttributeInSet",
                    "addPermissionAttributeSet"
                    "deletePermissionAttributeSet"

*/
            {
                    "addRoleConstraint", /* NAME_COL */
                    "AdminMgr Operation", /* DESC_COL */
                    "", /* OBJ_ID_COL */
                    "ADMIN", /* TYPE_COL */
                    "", /* PROPS_COL */
                    "", /* ROLES_COL */
                    "", /* USERS_COL */
                    "", /* GROUPS_COL */
                    "T" /* IS_ADMIN_COL */
            },
            {
                    "removeRoleConstraint", /* NAME_COL */
                    "AdminMgr Operation", /* DESC_COL */
                    "", /* OBJ_ID_COL */
                    "ADMIN", /* TYPE_COL */
                    "", /* PROPS_COL */
                    "", /* ROLES_COL */
                    "", /* USERS_COL */
                    "", /* GROUPS_COL */
                    "T" /* IS_ADMIN_COL */
            },
            {
                    "enableRoleConstraint", /* NAME_COL */
                    "AdminMgr Operation", /* DESC_COL */
                    "", /* OBJ_ID_COL */
                    "ADMIN", /* TYPE_COL */
                    "", /* PROPS_COL */
                    "", /* ROLES_COL */
                    "", /* USERS_COL */
                    "", /* GROUPS_COL */
                    "T" /* IS_ADMIN_COL */
            },
            {
                    "disableRoleConstraint", /* NAME_COL */
                    "AdminMgr Operation", /* DESC_COL */
                    "", /* OBJ_ID_COL */
                    "ADMIN", /* TYPE_COL */
                    "", /* PROPS_COL */
                    "", /* ROLES_COL */
                    "", /* USERS_COL */
                    "", /* GROUPS_COL */
                    "T" /* IS_ADMIN_COL */
            },
    };


    public static final String[][] DELEGATEDMGR_OBJ =
        {
            {
                "org.apache.directory.fortress.core.impl.DelAdminMgrImpl",
                "Delegated Admin Mgr Object", /* DESC_COL */
                "APP0", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "T" /* IS_ADMIN_COL */
        }
    };

    /**
    * Test Case TOP1:
    */
    public static final String[][] DELEGATEDMGR_OPS =
        {
            {
                "addRole", /* NAME_COL */
                "Delegated AdminMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
        },
            {
                "deleteRole", /* NAME_COL */
                "Delegated AdminMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
    },
            {
                "updateRole", /* NAME_COL */
                "Delegated AdminMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "assignUser", /* NAME_COL */
                "Delegated AdminMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deassignUser", /* NAME_COL */
                "Delegated AdminMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addOU", /* NAME_COL */
                "Delegated AdminMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "updateOU", /* NAME_COL */
                "Delegated AdminMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deleteOU", /* NAME_COL */
                "Delegated AdminMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addDescendantOU", /* NAME_COL */
                "Delegated AdminMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addAscendantOU", /* NAME_COL */
                "Delegated AdminMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addInheritanceOU", /* NAME_COL */
                "Delegated AdminMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deleteInheritanceOU", /* NAME_COL */
                "Delegated AdminMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addDescendantRole", /* NAME_COL */
                "Delegated AdminMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addAscendantRole", /* NAME_COL */
                "Delegated AdminMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "addInheritanceRole", /* NAME_COL */
                "Delegated AdminMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "deleteInheritanceRole", /* NAME_COL */
                "Delegated AdminMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
    };

    public static final String[][] DELEGATEDREVIEWMGR_OBJ =
        {
            {
                "org.apache.directory.fortress.core.impl.DelReviewMgrImpl",
                "Delegated Review Mgr Object", /* DESC_COL */
                "APP0", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "T" /* IS_ADMIN_COL */
        }
    };

    /**
    * Test Case TOP1:
    */
    public static final String[][] DELEGATEDREVIEWMGR_OPS =
        {
            {
                "readRole", /* NAME_COL */
                "Delegated ReviewMgr Op",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
        },
            {
                "findRoles", /* NAME_COL */
                "Delegated ReviewMgr Op",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
    },
            {
                "assignedRoles", /* NAME_COL */
                "Delegated ReviewMgr Op",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "assignedUsers", /* NAME_COL */
                "Delegated ReviewMgr Op",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "readOU", /* NAME_COL */
                "Delegated ReviewMgr Op",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "searchOU", /* NAME_COL */
                "Delegated ReviewMgr Op",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
                {
                        "rolePermissions", /* NAME_COL */
                        "Delegated ReviewMgr Op", /* DESC_COL */
                        "", /* OBJ_ID_COL */
                        "ADMIN", /* TYPE_COL */
                        "", /* PROPS_COL */
                        "", /* ROLES_COL */
                        "", /* USERS_COL */
                        "", /* GROUPS_COL */
                        "T" /* IS_ADMIN_COL */
                },

    };

    public static final String[][] REVIEWMGR_OBJ =
        {
            {
                "org.apache.directory.fortress.core.impl.ReviewMgrImpl",
                "Review Mgr Object", /* DESC_COL */
                "APP0", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "T" /* IS_ADMIN_COL */
        }
    };

    /**
    * Test Case TOP1:
    */
    public static final String[][] REVIEWMGR_OPS =
        {
            {
                "readPermission", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
        },
            {
                "readPermObj", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
    },
            {
                "findPermissions", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "findPermObjs", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "findPermsByObj", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "findAnyPermissions", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "readRole", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "findRoles", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "readUser", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "findUsers", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "assignedUsers", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "assignedRoles", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "authorizedUsers", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "authorizedRoles", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "rolePermissions", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "userPermissions", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "permissionRoles", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "authorizedPermissionRoles",/* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "permissionUsers", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "authorizedPermissionUsers", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "ssdRoleSets", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "ssdRoleSet", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "ssdRoleSetRoles", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "ssdRoleSetCardinality", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "dsdRoleSets", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "ssdSets", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "dsdRoleSet", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "dsdRoleSetRoles", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "dsdRoleSetCardinality", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "dsdSets", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
            },
            {
                "readPermAttributeSet", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
            },
            {
                "findRoleConstraints", /* NAME_COL */
                "ReviewMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
            },
    };


    public static final String[][] AUDITMGR_OBJ =
        {
            {
                "org.apache.directory.fortress.core.impl.AuditMgrImpl",
                "Audit Mgr Object", /* DESC_COL */
                "APP0", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "T" /* IS_ADMIN_COL */
        }
    };

    /**
     *
     */
    public static final String[][] AUDITMGR_OPS =
        {
            {
                "searchBinds", /* NAME_COL */
                "AuditMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
        },
            {
                "searchAuthZs", /* NAME_COL */
                "AuditMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
    },
            {
                "getUserAuthZs", /* NAME_COL */
                "AuditMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "searchUserSessions", /* NAME_COL */
                "AuditMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "searchAdminMods", /* NAME_COL */
                "AuditMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
            {
                "searchInvalidUsers", /* NAME_COL */
                "AuditMgr Op", /* DESC_COL */
                "", /* OBJ_ID_COL */
                "ADMIN", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "T" /* IS_ADMIN_COL */
},
    };


    public static final String[][] GROUPMGR_OBJ =
            {
                    {
                            "org.apache.directory.fortress.core.impl.GroupMgrImpl",
                            "ARBAC02 policies", /* DESC_COL */
                            "APP0", /* ORG_COL */
                            "TST", /* TYPE_COL */
                            "", /* PROPS_COL */
                            "T" /* IS_ADMIN_COL */
                    }
            };

    /**
     * Test Case TOP1:
     */
    public static final String[][] GROUPMGR_OPS =
            {
                    {
                            "add", /* NAME_COL */
                            "GroupMgr Operation", /* DESC_COL */
                            "", /* OBJ_ID_COL */
                            "ADMIN", /* TYPE_COL */
                            "", /* PROPS_COL */
                            "", /* ROLES_COL */
                            "", /* USERS_COL */
                            "", /* GROUPS_COL */
                            "T" /* IS_ADMIN_COL */
                    },
                    {
                            "update", /* NAME_COL */
                            "GroupMgr Operation", /* DESC_COL */
                            "", /* OBJ_ID_COL */
                            "ADMIN", /* TYPE_COL */
                            "", /* PROPS_COL */
                            "", /* ROLES_COL */
                            "", /* USERS_COL */
                            "", /* GROUPS_COL */
                            "T" /* IS_ADMIN_COL */
                    },
                    {
                            "delete", /* NAME_COL */
                            "GroupMgr Operation", /* DESC_COL */
                            "", /* OBJ_ID_COL */
                            "ADMIN", /* TYPE_COL */
                            "", /* PROPS_COL */
                            "", /* ROLES_COL */
                            "", /* USERS_COL */
                            "", /* GROUPS_COL */
                            "T" /* IS_ADMIN_COL */
                    },
                    {
                            "read", /* NAME_COL */
                            "GroupMgr Operation", /* DESC_COL */
                            "", /* OBJ_ID_COL */
                            "ADMIN", /* TYPE_COL */
                            "", /* PROPS_COL */
                            "", /* ROLES_COL */
                            "", /* USERS_COL */
                            "", /* GROUPS_COL */
                            "T" /* IS_ADMIN_COL */
                    },
                    {
                            "find", /* NAME_COL */
                            "GroupMgr Operation", /* DESC_COL */
                            "", /* OBJ_ID_COL */
                            "ADMIN", /* TYPE_COL */
                            "", /* PROPS_COL */
                            "", /* ROLES_COL */
                            "", /* USERS_COL */
                            "", /* GROUPS_COL */
                            "T" /* IS_ADMIN_COL */
                    },
                    {
                            "findWithUsers", /* NAME_COL */
                            "GroupMgr Operation", /* DESC_COL */
                            "", /* OBJ_ID_COL */
                            "ADMIN", /* TYPE_COL */
                            "", /* PROPS_COL */
                            "", /* ROLES_COL */
                            "", /* USERS_COL */
                            "", /* GROUPS_COL */
                            "T" /* IS_ADMIN_COL */
                    },
                    {
                            "roleGroups", /* NAME_COL */
                            "GroupMgr Operation", /* DESC_COL */
                            "", /* OBJ_ID_COL */
                            "ADMIN", /* TYPE_COL */
                            "", /* PROPS_COL */
                            "", /* ROLES_COL */
                            "", /* USERS_COL */
                            "", /* GROUPS_COL */
                            "T" /* IS_ADMIN_COL */
                    },
                    {
                            "groupRoles", /* NAME_COL */
                            "GroupMgr Operation", /* DESC_COL */
                            "", /* OBJ_ID_COL */
                            "ADMIN", /* TYPE_COL */
                            "", /* PROPS_COL */
                            "", /* ROLES_COL */
                            "", /* USERS_COL */
                            "", /* GROUPS_COL */
                            "T" /* IS_ADMIN_COL */
                    },
                    {
                            "assign", /* NAME_COL */
                            "GroupMgr Operation", /* DESC_COL */
                            "", /* OBJ_ID_COL */
                            "ADMIN", /* TYPE_COL */
                            "", /* PROPS_COL */
                            "", /* ROLES_COL */
                            "", /* USERS_COL */
                            "", /* GROUPS_COL */
                            "T" /* IS_ADMIN_COL */
                    },
                    {
                            "deassign", /* NAME_COL */
                            "GroupMgr Operation", /* DESC_COL */
                            "", /* OBJ_ID_COL */
                            "ADMIN", /* TYPE_COL */
                            "", /* PROPS_COL */
                            "", /* ROLES_COL */
                            "", /* USERS_COL */
                            "", /* GROUPS_COL */
                            "T" /* IS_ADMIN_COL */
                    },
                    {
                            "addProperty", /* NAME_COL */
                            "GroupMgr Operation", /* DESC_COL */
                            "", /* OBJ_ID_COL */
                            "ADMIN", /* TYPE_COL */
                            "", /* PROPS_COL */
                            "", /* ROLES_COL */
                            "", /* USERS_COL */
                            "", /* GROUPS_COL */
                            "T" /* IS_ADMIN_COL */
                    },
                    {
                            "deleteProperty", /* NAME_COL */
                            "GroupMgr Operation", /* DESC_COL */
                            "", /* OBJ_ID_COL */
                            "ADMIN", /* TYPE_COL */
                            "", /* PROPS_COL */
                            "", /* ROLES_COL */
                            "", /* USERS_COL */
                            "", /* GROUPS_COL */
                            "T" /* IS_ADMIN_COL */
                    },
            };

    public static final String[][] ABAC_TELLER_OBJS =
        {
            {
                "TellersPage",
                "Used by Tellers", /* DESC_COL */
                "APP0", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
            },
        };

    public static final String[][] ABAC_WASHER_OBJS =
        {
            {
                "WashersPage",
                "Used by Washers", /* DESC_COL */
                "APP0", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
            },
        };

    public static final String[][] ABAC_ACCOUNT_OBJS =
        {
            {
                "Account",
                "Things we can do with Customer Accounts", /* DESC_COL */
                "APP0", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
            },
        };

    public static final String[][] ABAC_CURRENCY_OBJS =
        {
            {
                "Currency",
                "Things we can do with currency", /* DESC_COL */
                "APP0", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
            },
        };

    public static final String[][] ABAC_BRANCH_OBJS =
        {
            {
                "Branch",
                "Functions corresponds with a particular branch", /* DESC_COL */
                "APP0", /* ORG_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "F" /* IS_ADMIN_COL */
            },
        };

    /**
     * Test Case ABAC1:
     */
    public static final String[][] ABAC_TELLER_OPS =
        {
            {
                "link", /* NAME_COL */
                "Tellers will view this link",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
            },
        };
    public static final String[][] ABAC_WASHER_OPS =
        {
            {
                "link", /* NAME_COL */
                "Washers will view this link",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
            },
        };
    public static final String[][] ABAC_ACCOUNT_OPS =
        {
            {
                "deposit", /* NAME_COL */
                "account.deposit function",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
            },
            {
                "withdrawal", /* NAME_COL */
                "dccount.withdrawal function",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
            },
            {
                "inquiry", /* NAME_COL */
                "account.inquiry function",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
            },
        };
    public static final String[][] ABAC_CURRENCY_OPS =
        {
            {
                "soak", /* NAME_COL */
                "Currency.soak function",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
            },
            {
                "rinse", /* NAME_COL */
                "Currency.rinse function",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
            },
            {
                "dry", /* NAME_COL */
                "Currency.dry function",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
            },
        };
    public static final String[][] ABAC_BRANCH_OPS =
        {
            {
                "login", /* NAME_COL */
                "ability to login to branch web app",/* DESC_COL */
                "", /* OBJ_ID_COL */
                "TST", /* TYPE_COL */
                "", /* PROPS_COL */
                "", /* ROLES_COL */
                "", /* USERS_COL */
                "", /* GROUPS_COL */
                "F" /* IS_ADMIN_COL */
            },
        };


    /**
     * The Fortress test data for junit uses 2-dimensional arrays.
     * These are the columns used for Fortress Object Test Data:
     */
    private final static int NAME_COL = 0;
    private final static int DESC_COL = 1;
    private final static int ORG_COL = 2;
    private final static int TYPE_COL = 3;
    private final static int PROPS_COL = 4;
    private final static int IS_ADMIN_COL = 5;

    // Fortress Test Operations:
    /**
     * These are the columns used for Fortress Operation Test Data.
     * Columns commented out indicate previous declaration.
     */
    //final static int NAME_COL = 0;
    //final static int DESC_COL = 1;
    private final static int OBJ_ID_COL = 2;
    //final static int TYPE_COL = 3;
    //final static int PROPS_COL = 4;
    private final static int ROLES_COL = 5;
    private final static int USERS_COL = 6;
    private final static int GROUPS_COL = 7;
    private final static int OP_ADMIN_COL = 8;


    //Fortress Permission Attribute Set Test Data
    /**
     * Test Case TPASET1:
     */
    public static final String TPA_SET_1_NAME = "TPASET1";
    public static final String TPA_SET_2_NAME = "TPASET2";
    public static final String TPA_SET_TYPE = "paSetType";
    public static final String TPA_SET_NOT_EXIST_NAME = "NOTEXISTTPASETNAME";
    
    public static final String[][] PA_TPSASET1 =
    {
        {
            "TPASET1AttributeName1", /* NAME */
            "true", /* REQUIRED */
            "string", /* DATA_TYPE */
            "EQUALS", /* COMPARISON_OPERATOR */
            "literal:value" /* VALID_VALUE */
        }
    };

    public static final String[][] PA_TPSASET2 =
    {
        {
            "TPASET2AttributeName1", /* NAME */
            "true", /* REQUIRED */
            "string", /* DATA_TYPE */
            "EQUALS", /* COMPARISON_OPERATOR */
            "literal:value" /* VALID_VALUE */
        },
        {
            "TPASET2AttributeName2", /* NAME */
            "false", /* REQUIRED */
            "int", /* DATA_TYPE */
            "EQUALS", /* COMPARISON_OPERATOR */
            "literal:value" /* VALID_VALUE */
        }
    };
    
    public static final String[][] PA_TPSASET2_ADDITIONAL =
    {
        {
            "TPASET2AttributeName3", /* NAME */
            "true", /* REQUIRED */
            "string", /* DATA_TYPE */
            "EQUALS", /* COMPARISON_OPERATOR */
            "literal:value" /* VALID_VALUE */
        },
        {
            "TPASET2AttributeName4", /* NAME */
            "false", /* REQUIRED */
            "int", /* DATA_TYPE */
            "EQUALS", /* COMPARISON_OPERATOR */
            "literal:value" /* VALID_VALUE */
        }
    };
    
    public static PermissionAttribute getPA( String[] pa )
    {
        PermissionAttribute permAttr = new PermissionAttribute();
        permAttr.setAttributeName(pa[0]);
        permAttr.setDataType(pa[2]);
        permAttr.setDefaultOperator(ComparisonOperator.valueOf(pa[3]).name());
        permAttr.getValidValues().add(pa[4]);
        return permAttr;
    }
    
    /**
     * @param op
     * @return
     */
    public static String getObjId( String[] op )
    {
        return op[OBJ_ID_COL];
    }


    /**
     * @param objName
     * @param op
     * @return
     */
    public static Permission getOp( String objName, String[] op )
    {
        Permission pOp = new Permission();
        pOp.setObjName( objName );
        pOp.setObjId( getObjId( op ) );
        pOp.setOpName( getName( op ) );
        pOp.setDescription( getDescription( op ) );
        //pOp.setAbstractName(pOp.getObjName() + "." + pOp.getOpName());
        pOp.setType( getType( op ) );
        pOp.setAdmin( isOpAdmin( op ) );
        return pOp;
    }


    /**
     * @param objName
     * @param pOp
     * @param op
     */
    public static void assertEquals( String objName, Permission pOp, String[] op )
    {
        assertEquals( PermTestData.class.getName() + ".assertEquals failed compare perm objName", objName,
            pOp.getObjName() );
        assertEquals( PermTestData.class.getName() + ".assertEquals failed compare perm operation name", getName( op ),
            pOp.getOpName() );
        assertEquals( PermTestData.class.getName() + ".assertEquals failed compare perm type", getType( op ),
            pOp.getType() );
        assertEquals( PermTestData.class.getName() + ".assertEquals failed compare perm abstract name",
            ( objName + "." + getName( op ) ), ( pOp.getObjName() + "." + pOp.getOpName() ) );
        assertEquals( PermTestData.class.getName() + ".assertEquals failed compare perm isAdmin", isOpAdmin( op ),
            pOp.isAdmin() );
        LOG.debug( PermTestData.class.getName() + ".assertEquals perm objName [" + objName + "] operation name ["
            + pOp.getOpName() + "] successful" );
    }


    /**
     * @param obj
     * @return
     */
    public static PermObj getObj( String[] obj )
    {
        PermObj pObj = new PermObj();
        pObj.setObjName( getName( obj ) );
        pObj.setDescription( getDescription( obj ) );
        pObj.setType( getType( obj ) );
        pObj.setOu( getOu( obj ) );
        pObj.setAdmin( isAdmin( obj ) );
        return pObj;
    }


    /**
     * @param pObj
     * @param obj
     */
    public static void assertEquals( PermObj pObj, String[] obj )
    {
        assertEquals( PermTestData.class.getName() + ".assertEquals failed compare perm objName", getName( obj ),
            pObj.getObjName() );
        assertEquals( PermTestData.class.getName() + ".assertEquals failed compare perm description",
            getDescription( obj ), pObj.getDescription() );
        assertEquals( PermTestData.class.getName() + ".assertEquals failed compare perm type", getType( obj ),
            pObj.getType() );
        assertEquals( PermTestData.class.getName() + ".assertEquals failed compare perm ou", getOu( obj ), pObj.getOu() );
        assertEquals( PermTestData.class.getName() + ".assertEquals failed compare perm isAdmin", isAdmin( obj ),
            pObj.isAdmin() );
        LOG.debug( PermTestData.class.getName() + ".assertEquals perm objName [" + pObj.getObjName()
            + "] successful" );
    }


    /**
     * @param obj
     * @return
     */
    public static String getName( String[] obj )
    {
        return obj[NAME_COL];
    }


    /**
     * @param obj
     * @return
     */
    public static String getDescription( String[] obj )
    {
        return obj[DESC_COL];
    }


    /**
     * @param obj
     * @return
     */
    public static String getType( String[] obj )
    {
        return obj[TYPE_COL];
    }


    /**
     * @param obj
     * @return
     */
    public static String getOu( String[] obj )
    {
        return obj[ORG_COL];
    }


    public static boolean isAdmin( String[] obj )
    {
        return obj[IS_ADMIN_COL].equalsIgnoreCase( "T" );
    }


    public static boolean isOpAdmin( String[] obj )
    {
        return obj[OP_ADMIN_COL].equalsIgnoreCase( "T" );
    }
    
    public static Set<PermissionAttribute> loadPermissionAttributes(String[][] objArray){
        Set<PermissionAttribute> permAttrs = new HashSet<PermissionAttribute>();
        
        for ( String[] obj : objArray )
        {
            PermissionAttribute pa = PermTestData.getPA(obj);
            permAttrs.add(pa);
        }
        
        return permAttrs;
    }
    
}