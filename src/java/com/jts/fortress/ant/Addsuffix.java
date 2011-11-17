/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.ant;

import java.util.ArrayList;
import java.util.List;

import com.jts.fortress.ldap.suffix.Suffix;

/**
 * The class is used by {@link FortressAntTask} to create new {@link com.jts.fortress.ldap.suffix.Suffix} used to drive {@link com.jts.fortress.ldap.suffix.SuffixP#add(com.jts.fortress.ldap.suffix.Suffix)}.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Addsuffix', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <addsuffix>
 *           ...
 *         </addsuffix>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author smckinn
 * @created January 21, 2010
 */
public class Addsuffix

{
    final private List<Suffix> suffixes = new ArrayList<Suffix>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Addsuffix()
    {
    }

    /**
     * <p>This method name, 'addSuffix', is used for derived xml tag 'suffix' in the load script.</p>
     * <pre>
     * {@code
     * <addsuffix>
     *     <suffix name="jts" dc="com" description="Joshua Tree Software"/>
     * </addsuffix>
     * }
     * </pre>
     *
     * @param suffix contains reference to data element targeted for insertion..
     */
    public void addSuffix(Suffix suffix)
    {
        this.suffixes.add(suffix);
    }

    /**
     * Used by {@link FortressAntTask#addSuffixes()} to retrieve list of Suffixes as defined in input xml file.
     *
     * @return collection containing {@link com.jts.fortress.ldap.suffix.Suffix}s targeted for insertion.
     */
    public List<com.jts.fortress.ldap.suffix.Suffix> getSuffixes()
    {
        return this.suffixes;
    }
}

