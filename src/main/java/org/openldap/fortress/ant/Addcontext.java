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

import org.openldap.fortress.rbac.Context;

import java.util.ArrayList;
import java.util.List;


/**
 * The class is used by {@link FortressAntTask} to create new {@link Context} used to define multi-tenant property.
 * It is not intended to be callable by programs outside of the Ant load utility.  The class name itself maps to the xml tag used by load utility.
 * <p>This class name, 'Addcontext', is used for the xml tag in the load script.</p>
 * <pre>
 * {@code
 * <target name="all">
 *     <FortressAdmin>
 *         <addcontext>
 *           ...
 *         </addcontext>
 *     </FortressAdmin>
 * </target>
 * }
 * </pre>
 *
 * @author Shawn McKinney
 */
public class Addcontext

{
    final private List<Context> contexts = new ArrayList<>();

    /**
     * All Ant data entities must have a default constructor.
     */
    public Addcontext()
    {
    }

    /**
     * <p>This method name, 'addContext', is used for derived xml tag 'context' in the load script.</p>
     * <pre>
     * {@code
     * <addcontext>
     *     <context name="123"/>
     * </addsuffix>
     * }
     * </pre>
     *
     * @param context contains reference to data element targeted for insertion..
     */
    public void addContext(Context context)
    {
        this.contexts.add(context);
    }

    /**
     * Used by {@link Context} to retrieve list of contexts as defined in input xml file.
     *
     * @return List of context names.
     */
    public List<Context> getContexts()
    {
        return this.contexts;
    }
}

