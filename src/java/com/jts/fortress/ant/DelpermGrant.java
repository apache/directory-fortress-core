/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to revoke {@link PermGrant}s used to drive {@link com.jts.fortress.AdminMgr#revokePermission(com.jts.fortress.rbac.Permission, com.jts.fortress.rbac.Role)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'DelpermGrant', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <OamAdmin>
 *         <delpermgrant>
 *           ...
 *         </delpermgrant>
 *     </OamAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author smckinn
 * @created July 22, 2010
 */
public class DelpermGrant
{
    private List<PermGrant> permGrants = new ArrayList<PermGrant>();


    /**
     * All Ant data entities must have a default constructor.
     */
    public DelpermGrant()
    {
    }


    /**
     * <p>This method name, 'addPermGrant', is used for derived xml tag 'permgrant' in the load script.</p>
     * <pre>
     * {@code
     * <delpermgrant>
     *     <permgrant objName="/jsp/cal/cal1.jsp" opName="main" roleNm="role1"/>
     *     <permgrant objName="/jsp/cal/cal2.jsp" opName="8am" roleNm="role1"/>
     *     <permgrant objName="/jsp/cal/cal2.jsp" opName="10am" roleNm="role1"/>
     *     <permgrant objName="/jsp/cal/cal2.jsp" opName="12pm" roleNm="role1"/>
     *     <permgrant objName="/jsp/cal/cal2.jsp" opName="2pm" roleNm="role1"/>
     *     <permgrant objName="/jsp/cal/cal2.jsp" opName="4pm" roleNm="role1"/>
     *     <permgrant objName="/jsp/cal/cal2.jsp" opName="6pm" roleNm="role1"/>
     * </delpermgrant>
     * }
     * </pre>
     *
     * @param permGrant contains reference to data element targeted for revocation.
     */
    public void addPermGrant(PermGrant permGrant)
    {
        this.permGrants.add(permGrant);
    }


    /**
     * Used by {@link FortressAntTask#addPermGrants()} to retrieve list of PermGrant as defined in input xml file.
     *
     * @return collection containing {@link PermGrant}s targeted for revocation.
     */
    public List<PermGrant> getPermGrants()
    {
        return this.permGrants;
    }
}

