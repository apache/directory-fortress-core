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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This entity is used by en masse to communicate {@link org.openldap.fortress.rbac.Role}, {@link org.openldap.fortress.rbac.Permission} and {@link org.openldap.fortress.rbac.Session} information to the server for access control decisions.
 * <p/>
 * @author Shawn McKinney
 */
@XmlRootElement(name = "fortRolePerm")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rolePerm", propOrder = {
    "role",
    "perm"
})
public class RolePerm extends FortEntity
    implements java.io.Serializable
{
    private Role role;
    private Permission perm;

    public Role getRole()
    {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }

    public Permission getPerm()
    {
        return perm;
    }

    public void setPerm(Permission perm)
    {
        this.perm = perm;
    }
}