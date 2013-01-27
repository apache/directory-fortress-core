/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.ant;

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to create new {@link us.jts.fortress.ldap.suffix.Suffix} used to drive {@link us.jts.fortress.ldap.suffix.SuffixP#delete(us.jts.fortress.ldap.suffix.Suffix)}.
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
 * @author Shawn McKinney
 */
public class Delsuffix
{
    final private List<us.jts.fortress.ldap.suffix.Suffix> suffixes = new ArrayList<us.jts.fortress.ldap.suffix.Suffix>();

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
     *     <FortressAdmin>
     *         <delsuffix>
     *           ...
     *         </delsuffix>
     *     </FortressAdmin>
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
    public void addSuffix(us.jts.fortress.ldap.suffix.Suffix suffix)
    {
        this.suffixes.add(suffix);
    }

    /**
     * Used by {@link FortressAntTask#deleteSuffixes()} to retrieve list of Suffixes as defined in input xml file.
     *
     * @return collection containing {@link us.jts.fortress.ldap.suffix.Suffix}s targeted for removal.
     */
    public List<us.jts.fortress.ldap.suffix.Suffix> getSuffixes()
    {
        return this.suffixes;
    }
}

