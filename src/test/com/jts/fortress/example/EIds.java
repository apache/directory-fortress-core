/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.example;


public class EIds
{
    public final static String EXAMPLE_ADMIN_IMPLEMENTATION = "exampleAdminImplementation";
    public final static String EXAMPLE_ADMIN_DEFAULT_CLASS = "ExampleAdminMgrImpl";

    // place any global variables related to example entity here
    public final static String EXAMPLE_ROOT = "exampleRoot";

    public final static int EXAMPLE_LEN = 40;

    public final static String EXAMPLE_NM = "oamExampleName";
    public final static String EXAMPLE_OBJECT_CLASS_NM = "oamExamples";

    public final static String EXAMPLE_OBJ_CLASS[] = {
            com.jts.fortress.constants.GlobalIds.TOP, EXAMPLE_OBJECT_CLASS_NM, com.jts.fortress.constants.GlobalIds.PROPS_AUX_OBJECT_CLASS_NAME
//            GlobalIds.TOP, EXAMPLE_OBJECT_CLASS_NM, GlobalIds.PROPS_AUX_OBJECT_CLASS_NAME, GlobalIds.TEMPORAL_AUX_OBJECT_CLASS_NAME
    };

    public static final int BATCH_SIZE = 100;
}

