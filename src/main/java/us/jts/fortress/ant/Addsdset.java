/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.ant;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is used by {@link FortressAntTask} to create new {@link SDSetAnt}s used to drive {@link us.jts.fortress.AdminMgr#createSsdSet(us.jts.fortress.rbac.SDSet)} or {@link us.jts.fortress.AdminMgr#createDsdSet(us.jts.fortress.rbac.SDSet)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Addsdset', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <addsdset>
 *           ...
 *         </addsdset>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author Shawn McKinney
 */
public class Addsdset
{
    final private List<SDSetAnt> sds = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Addsdset()
    {
    }

    /**
     * <p>This method name, 'addSdset', is used for derived xml tag 'sdset' in the load script.</p>
     * <pre>
     * {@code
     * <addsdset>
     *     <sdset name="DemoSSD1"
     *         description="Demo static separation of duties"
     *         cardinality="2"
     *         settype="STATIC"
     *         setmembers="role1,role2"/>
     *     <sdset name="DemoDSD1"
     *         description="Demo dynamic separation of duties"
     *         cardinality="2"
     *         settype="DYNAMIC"
     *         setmembers="role1,role3"/>
     * </addsdset>
     * }
     * </pre>
     *
     * @param sd contains reference to data element targeted for insertion..
     */
    public void addSdset(SDSetAnt sd)
    {
        this.sds.add(sd);
    }

    /**
     * Used by {@link FortressAntTask#addSdsets()} to retrieve list of SDSetAnt as defined in input xml file.
     *
     * @return collection containing {@link SDSetAnt}s targeted for insertion.
     */
    public List<SDSetAnt> getSdset()
    {
        return this.sds;
    }
}

