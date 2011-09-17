/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to create new {@link com.jts.fortress.ldap.suffix.Suffix} used to drive {@link com.jts.fortress.ldap.suffix.SuffixP#delete(com.jts.fortress.ldap.suffix.Suffix)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Delsuffix', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <delsuffix>
 *   ...
 * </delsuffix>
 * }
 * </pre>
 * <font size="3" color="red">
 * This class is destructive as it will remove all nodes below the suffix using recursive delete function.<BR>
 * Extreme care should be taken during execution to ensure target dn is correct and permanent removal of data is intended.  There is no
 * 'undo' for this operation.
 * </font>
 * <p/>
 *
 * @author smckinn
 * @created January 21, 2010
 */
public class Delsuffix
{
    final private List<com.jts.fortress.ldap.suffix.Suffix> suffixes = new ArrayList<com.jts.fortress.ldap.suffix.Suffix>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Delsuffix()
    {
    }

    /**
     * <p>This method name, 'addSuffix', is used for derived xml tag 'suffix' in the load script.</p>
     * <pre>
     * {@code
     * <target name="all">
     *     <OamAdmin>
     *         <delsuffix>
     *           ...
     *         </delsuffix>
     *     </OamAdmin>
     * </target>
     * }
     * </pre>
     * <p/>
     * <font size="2" color="red">
     * This method is destructive and will remove all nodes below.<BR>
     * Extreme care should be taken during execution to ensure target dn is correct and permanent removal of data is intended.  There is no
     * 'undo' for this operation.
     * </font>
     *
     * @param suffix contains reference to data element targeted for removal..
     */
    public void addSuffix(com.jts.fortress.ldap.suffix.Suffix suffix)
    {
        this.suffixes.add(suffix);
    }

    /**
     * Used by {@link FortressAntTask#deleteSuffixes()} to retrieve list of Suffixes as defined in input xml file.
     *
     * @return collection containing {@link com.jts.fortress.ldap.suffix.Suffix}s targeted for removal.
     */
    public List<com.jts.fortress.ldap.suffix.Suffix> getSuffixes()
    {
        return this.suffixes;
    }
}

