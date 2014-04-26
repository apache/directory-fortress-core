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

package org.openldap.fortress.ant;

import org.openldap.fortress.rbac.OrgUnitAnt;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to create new {@link org.openldap.fortress.rbac.OrgUnit}s used to drive {@link org.openldap.fortress.DelAdminMgr#add(org.openldap.fortress.rbac.OrgUnit)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Addorgunit', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <addorgunit>
 *           ...
 *         </addorgunit>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author Shawn McKinney
 */
public class Addorgunit
{
    final private List<OrgUnitAnt> ous = new ArrayList<>();


    /**
     * All Ant data entities must have a default constructor.
     */
    public Addorgunit()
    {
    }


    /**
     * <p>This method name, 'addOrgUnit', is used for derived xml tag 'orgunit' in the load script.</p>
     * <pre>
     * {@code
     * <addorgunit>
     *     <orgunit name="demousrs1" typeName="USER" description="Test User Org 1 for User on Tomcat Calendar App"/>
     *     <orgunit name="demousrs2" typename="USER" description="Test User Org 2 for User on Tomcat  Calendar App"/>
     *     <orgunit name="demoapps1" typeName="PERM" description="Test Perm Org 1 for Permission on Tomcat Calendar App"/>
     *     <orgunit name="demoapps2" typename="PERM" description="Test Perm Org 2 for Permission on Tomcat Calendar App"/>
     * </addorgunit>
     * }
     * </pre>
     *
     * @param ou contains reference to data element targeted for insertion..
     */
    public void addOrgUnit(OrgUnitAnt ou)
    {
        this.ous.add(ou);
    }


    /**
     * Used by {@link FortressAntTask#addOrgunits()} to retrieve list of OrgUnits as defined in input xml file.
     *
     * @return collection containing {@link org.openldap.fortress.rbac.OrgUnit}s targeted for insertion.
     */
    public List<OrgUnitAnt> getOrgUnits()
    {
        return this.ous;
    }
}

