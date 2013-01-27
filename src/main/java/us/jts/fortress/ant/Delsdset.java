/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.ant;

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to create new {@link SDSetAnt}s used to drive {@link us.jts.fortress.AdminMgr#deleteSsdSet(us.jts.fortress.rbac.SDSet)} or {@link us.jts.fortress.AdminMgr#deleteDsdSet(us.jts.fortress.rbac.SDSet)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Delsdset', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <delsdset>
 *           ...
 *         </delsdset>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author Shawn McKinney
 */
public class Delsdset
{
    final private List<SDSetAnt> sds = new ArrayList<SDSetAnt>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Delsdset()
    {
    }

    /**
     * <p>This method name, 'addSdset', is used for derived xml tag 'sdset' in the load script.</p>
     * <pre>
     * {@code
     * <delsdset>
     *     <sdset name="DemoSSD1" settype="STATIC" />
     *     <sdset name="DemoDSD1" settype="DYNAMIC" />
     * </delsdset>
     * }
     * </pre>
     *
     * @param sd contains reference to data element targeted for removal.
     */
    public void addSdset(SDSetAnt sd)
    {
        this.sds.add(sd);
    }

    /**
     * Used by {@link FortressAntTask#deleteSdsets()} to retrieve list of SDSetAnt as defined in input xml file.
     *
     * @return collection containing {@link SDSetAnt}s targeted for removal.
     */
    public List<SDSetAnt> getSdset()
    {
        return this.sds;
    }
}

