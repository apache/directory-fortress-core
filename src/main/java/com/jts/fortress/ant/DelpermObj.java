/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package com.jts.fortress.ant;

import java.util.ArrayList;
import java.util.List;

import com.jts.fortress.rbac.PermObj;


/**
 * The class is used by {@link FortressAntTask} to load {@link PermObj}s used to drive {@link com.jts.fortress.AdminMgr#deletePermObj(com.jts.fortress.rbac.PermObj)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'DelpermObj', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <delpermobj>
 *           ...
 *         </delpermobj>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author Shawn McKinney
 */
public class DelpermObj
{
    final private List<PermObj> permObjs = new ArrayList<PermObj>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public DelpermObj()
    {
    }

    /**
     * <p>This method name, 'delPermObj', is used for derived xml tag 'permobj' in the load script.</p>
     * <pre>
     * {@code
     * <delpermobj>
     *     <permobj objectName="/jsp/cal/cal1.jsp"/>
     *     <permobj objectName="/jsp/cal/cal2.jsp"/>
     * </delpermobj>
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
     * @return collection containing {@link com.jts.fortress.rbac.PermObj}s targeted for deletion.
     */
    public List<PermObj> getObjs()
    {
        return this.permObjs;
    }
}

