/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import com.jts.fortress.rbac.OrgUnitAnt;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to create new {@link com.jts.fortress.rbac.OrgUnit}s used to drive {@link com.jts.fortress.DelAdminMgr#add(com.jts.fortress.rbac.OrgUnit)}.
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
 * @created November 19, 2010
 */
public class Addorgunit
{
    final private List<OrgUnitAnt> ous = new ArrayList<OrgUnitAnt>();


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
     * @return collection containing {@link com.jts.fortress.rbac.OrgUnit}s targeted for insertion.
     */
    public List<OrgUnitAnt> getOrgUnits()
    {
        return this.ous;
    }
}

