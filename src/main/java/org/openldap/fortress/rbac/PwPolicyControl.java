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

package org.openldap.fortress.rbac;


import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPControl;


/**
 * Interface is used to allow pluggable password policy interrogation.
 *
 * @author Shawn McKinney
 */
public interface PwPolicyControl
{
    /**
     * Check the password policy controls returned from server and sets the PwMessage with what it finds.
     *
     * @param controls ldap controls object.
     * @param isAuthenticated set to 'true' if password checks pass.
     * @param pwMsg describes the outcome of the policy checks.
     */
    public void checkPasswordPolicy( LDAPControl[] controls, boolean isAuthenticated, PwMessage pwMsg );
}
