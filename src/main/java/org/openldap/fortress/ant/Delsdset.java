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

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to create new {@link SDSetAnt}s used to drive {@link org.openldap.fortress.AdminMgr#deleteSsdSet(org.openldap.fortress.rbac.SDSet)} or {@link org.openldap.fortress.AdminMgr#deleteDsdSet(org.openldap.fortress.rbac.SDSet)}.
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
    final private List<SDSetAnt> sds = new ArrayList<>();

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

