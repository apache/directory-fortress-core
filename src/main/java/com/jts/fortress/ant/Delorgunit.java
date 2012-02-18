/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import java.util.ArrayList;
import java.util.List;

import com.jts.fortress.arbac.OrgUnitAnt;


/**
 * The class is used by {@link FortressAntTask} to delete {@link com.jts.fortress.arbac.OrgUnit}s used to drive {@link com.jts.fortress.DelegatedAdminMgr#delete(com.jts.fortress.arbac.OrgUnit)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Delorgunit', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <delorgunit>
 *           ...
 *         </delorgunit>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author smckinn
 * @created November 19, 2010
 */
public class Delorgunit
{
    final private List<OrgUnitAnt> ous = new ArrayList<OrgUnitAnt>();


    /**
     * All Ant data entities must have a default constructor.
     */
    public Delorgunit()
    {
    }


    /**
     * <p>This method name, 'addOrgUnit', is used for derived xml tag 'orgunit' in the load script.</p>
     * <pre>
     * {@code
     * <delorgunit>
     *     <orgunit name="demousrs1" typeName="USER"/>
     *     <orgunit name="demousrs2" typename="USER"/>
     *     <orgunit name="demoapps1" typeName="PERM"/>
     *     <orgunit name="demoapps2" typename="PERM"/>
     * </delorgunit>
     * }
     * </pre>
     *
     * @param ou contains reference to data element targeted for deletion..
     */
    public void addOrgUnit(OrgUnitAnt ou)
    {
        this.ous.add(ou);
    }


    /**
     * Used by {@link FortressAntTask#addOrgunits()} to retrieve list of OrgUnits as defined in input xml file.
     *
     * @return collection containing {@link com.jts.fortress.arbac.OrgUnit}s targeted for removal.
     */
    public List<OrgUnitAnt> getOrgUnits()
    {
        return this.ous;
    }
}