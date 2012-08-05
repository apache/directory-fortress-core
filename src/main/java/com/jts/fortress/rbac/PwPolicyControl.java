/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;

/**
 * Interface is used to allow pluggable password policy interrogation.
 *

 * @author Shawn McKinney
 * @created October 13, 2009
 */
public interface PwPolicyControl
{
    /**
     * Check the password policy controls returned from server and sets the PwMessage with what it finds.
     *
     * @param ld ldap connection object.
     * @param isAuthenticated set to 'true' if password checks pass.
     * @param pwMsg describes the outcome of the policy checks.
     */
    public void checkPasswordPolicy(LDAPConnection ld, boolean isAuthenticated, PwMessage pwMsg);
}

