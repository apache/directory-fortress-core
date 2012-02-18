/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import java.util.ArrayList;
import java.util.List;

import com.jts.fortress.rbac.PermObj;

/**
 * The class is used by {@link FortressAntTask} to load {@link PermObj}s used to drive {@link com.jts.fortress.AdminMgr#addPermObj(com.jts.fortress.rbac.PermObj)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'AddpermObj', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <addpermobj>
 *           ...
 *         </addpermobj>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author smckinn
 * @created November 23, 2010
 */
public class AddpermObj
{

    final private List<PermObj> permObjs = new ArrayList<PermObj>();


    /**
     * All Ant data entities must have a default constructor.
     */
    public AddpermObj()
    {
    }


    /**
     * <p>This method name, 'addPermObj', is used for derived xml tag 'permobj' in the load script.</p>
     * <pre>
     * {@code
     * <addpermobj>
     *     <permobj objectName="/jsp/cal/cal1.jsp" description="Fortress Web Demo App Object 1" ou="demoapps1" type="Ant"/>
     *     <permobj objectName="/jsp/cal/cal2.jsp" description="Fortress Web Demo App Object 2" ou="demoapps1" type="Ant"/>
     * </addpermobj>
     * }
     * </pre>
     *
     * @param permObj contains reference to data element targeted for insertion..
     */
    public void addPermObj(PermObj permObj)
    {
        this.permObjs.add(permObj);
    }


    /**
     * Used by {@link FortressAntTask#addPermObjs()} to retrieve list of PermObjs as defined in input xml file.
     *
     * @return collection containing {@link com.jts.fortress.rbac.PermObj}s targeted for insertion.
     */
    public List<PermObj> getPermObjs()
    {
        return this.permObjs;
    }
}

