/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.example;

import us.jts.fortress.SecurityException;

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

    /**
     * @param example
     * @return
     * @throws us.jts.fortress.SecurityException
     */
    public Example addExample(Example example)
        throws SecurityException
    {
        us.jts.fortress.util.attr.VUtil.assertNotNull(example, EErrIds.EXAMPLE_ID_NULL, CLS_NM + ".addExample");
        return examP.add(example);
    }

    /**
     * @param example
     * @throws us.jts.fortress.SecurityException
     */
    public void delExample(Example example)
        throws us.jts.fortress.SecurityException
    {
        us.jts.fortress.util.attr.VUtil.assertNotNull(example, EErrIds.EXAMPLE_ID_NULL, CLS_NM + ".addExample");
        examP.delete(example.getName());
    }
}

