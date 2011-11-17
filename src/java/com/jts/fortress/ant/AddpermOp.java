/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to load {@link PermAnt}s used to drive {@link com.jts.fortress.AdminMgr#addPermission(com.jts.fortress.rbac.Permission)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'AddpermOp', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <addpermop>
 *           ...
 *         </addpermop>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author smckinn
 * @created November 23, 2010
 */
public class AddpermOp
{
    final private List<PermAnt> permissions = new ArrayList<PermAnt>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public AddpermOp()
    {
    }

    /**
     * <p>This method name, 'addPermOp', is used for derived xml tag 'permop' in the load script.</p>
     * <pre>
     * {@code
     * <addpermop>
     *     <permop opName="main" objectName="/jsp/cal/cal1.jsp" type="Ant"/>
     *     <permop opName="8am" objectName="/jsp/cal/cal2.jsp" type="Ant"/>
     *     <permop opName="9am" objectName="/jsp/cal/cal2.jsp" type="Ant"/>
     *     <permop opName="10am" objectName="/jsp/cal/cal2.jsp" type="Ant"/>
     *     <permop opName="11am" objectName="/jsp/cal/cal2.jsp" type="Ant"/>
     *     <permop opName="12pm" objectName="/jsp/cal/cal2.jsp" type="Ant"/>
     *     <permop opName="1pm" objectName="/jsp/cal/cal2.jsp" type="Ant"/>
     *     <permop opName="2pm" objectName="/jsp/cal/cal2.jsp" type="Ant"/>
     *     <permop opName="3pm" objectName="/jsp/cal/cal2.jsp" type="Ant"/>
     *     <permop opName="4pm" objectName="/jsp/cal/cal2.jsp" type="Ant"/>
     *     <permop opName="5pm" objectName="/jsp/cal/cal2.jsp" type="Ant"/>
     *     <permop opName="6pm" objectName="/jsp/cal/cal2.jsp" type="Ant"/>
     * </addpermop>
     * }
     * </pre>
     *
     * @param permission contains reference to data element targeted for insertion..
     */
    public void addPermOp(PermAnt permission)
    {
        this.permissions.add(permission);
    }

    /**
     * Used by {@link FortressAntTask#addPermOps()} to retrieve list of Permissions as defined in input xml file.
     *
     * @return collection containing {@link PermAnt}s targeted for insertion.
     */
    public List<PermAnt> getPermOps()
    {
        return this.permissions;
    }
}

