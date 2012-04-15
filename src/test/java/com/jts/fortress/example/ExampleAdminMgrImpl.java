/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.example;

import com.jts.fortress.SecurityException;
import org.apache.log4j.Logger;

/**
 * Object provides example of Fortress API.
 *
 * @author Shawn McKinney
 * @created December 26, 2010
 */
public class ExampleAdminMgrImpl implements ExampleAdminMgr
{
    private static final String CLS_NM = ExampleAdminMgrImpl.class.getName();
    private static final ExampleP examP = new ExampleP();
    protected static final Logger log = Logger.getLogger(CLS_NM);
    
    /**
     * @param example
     * @return
     * @throws com.jts.fortress.SecurityException
     */
    public Example addExample(Example example)
        throws SecurityException
    {
        com.jts.fortress.util.attr.VUtil.assertNotNull(example, EErrIds.EXAMPLE_ID_NULL, CLS_NM + ".addExample");
        return examP.add(example);
    }

    /**
     * @param example
     * @throws com.jts.fortress.SecurityException
     */
    public void delExample(Example example)
        throws com.jts.fortress.SecurityException
    {
        com.jts.fortress.util.attr.VUtil.assertNotNull(example, EErrIds.EXAMPLE_ID_NULL, CLS_NM + ".addExample");
        examP.delete(example.getName());
    }
}

