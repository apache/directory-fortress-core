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

package org.openldap.fortress.example;


import org.openldap.fortress.GlobalIds;

public class EIds
{
    public final static String EXAMPLE_ADMIN_IMPLEMENTATION = "exampleAdminImplementation";
    public final static String EXAMPLE_ADMIN_DEFAULT_CLASS = "ExampleAdminMgrImpl";

    // place any global variables related to example entity here
    public final static String EXAMPLE_ROOT = "example.root";

    public final static int EXAMPLE_LEN = 40;

    public final static String EXAMPLE_NM = "oamExampleName";
    public final static String EXAMPLE_OBJECT_CLASS_NM = "oamExamples";

    public final static String EXAMPLE_OBJ_CLASS[] = {
            GlobalIds.TOP, EXAMPLE_OBJECT_CLASS_NM, GlobalIds.PROPS_AUX_OBJECT_CLASS_NAME
//            GlobalIds.TOP, EXAMPLE_OBJECT_CLASS_NM, GlobalIds.PROPS_AUX_OBJECT_CLASS_NAME, GlobalIds.TEMPORAL_AUX_OBJECT_CLASS_NAME
    };

    public static final int BATCH_SIZE = 100;
}

