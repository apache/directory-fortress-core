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

import org.openldap.fortress.SecurityException;

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
     * @throws org.openldap.fortress.SecurityException
     */
    public Example addExample(Example example)
        throws SecurityException
    {
        org.openldap.fortress.util.attr.VUtil.assertNotNull(example, EErrIds.EXAMPLE_ID_NULL, ".addExample");
        return examP.add(example);
    }

    /**
     * @param example
     * @throws org.openldap.fortress.SecurityException
     */
    public void delExample(Example example)
        throws org.openldap.fortress.SecurityException
    {
        org.openldap.fortress.util.attr.VUtil.assertNotNull(example, EErrIds.EXAMPLE_ID_NULL, ".addExample");
        examP.delete(example.getName());
    }
}

