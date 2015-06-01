/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.fortress.core.ant;

import org.apache.directory.fortress.core.model.OrgUnit;

import java.io.Serializable;


/**
 * Entity is used by custom Apache Ant task for special handling of collections.  This is necessary because the
 * Ant parser cannot deal with complex data attribute types.  The class extends a base entity.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class OrgUnitAnt extends OrgUnit implements Serializable
{
    /** Default serialVersionUID */
    private static final long serialVersionUID = 1L;

    private String typeName;


    /**
     * Return the type of OU in string format.
     *
     * @return String that represents static or dynamic relations.
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Method accepts a String variable that maps to its parent's set type.
     *
     * @param typeName String value represents perm or user ou data sets.
     */
    public void setTypeName( String typeName )
    {
        this.typeName = typeName;
        if ( typeName != null && typeName.equalsIgnoreCase( "PERM" ) )
        {
            setType( OrgUnit.Type.PERM );
        }
        else
        {
            setType( OrgUnit.Type.USER );
        }
    }
}
