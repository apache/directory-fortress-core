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
 * Interface class for ExampleAdminMgrImpl.
 *
 * @author Shawn McKinney
 * @created December 26, 2010
 */
public interface ExampleAdminMgr
{
    public Example addExample(Example example)
        throws org.openldap.fortress.SecurityException;

    public void delExample(Example example)
        throws SecurityException;
}

