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

import org.openldap.fortress.ldap.suffix.Suffix;

/**
 * The class is used by {@link FortressAntTask} to create new {@link org.openldap.fortress.ldap.suffix.Suffix} used to drive {@link org.openldap.fortress.ldap.suffix.SuffixP#add(org.openldap.fortress.ldap.suffix.Suffix)}.
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
 * @author Shawn McKinney
 */
public class Addsuffix

{
    final private List<Suffix> suffixes = new ArrayList<>();

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
     * @return collection containing {@link org.openldap.fortress.ldap.suffix.Suffix}s targeted for insertion.
     */
    public List<org.openldap.fortress.ldap.suffix.Suffix> getSuffixes()
    {
        return this.suffixes;
    }
}

