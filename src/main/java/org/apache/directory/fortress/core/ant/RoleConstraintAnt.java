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


import org.apache.directory.fortress.core.model.RoleConstraint;



/**
 * Entity is used by custom Apache Ant task for special handling of collections.
 * Used when the common entity found in the model doesn't contain necessary attributes to complete the operation via ant admin task.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class RoleConstraintAnt extends RoleConstraint
{
    /** Default serialVersionUID */
    private static final long serialVersionUID = 1L;
    private String role;

    /**
     *
     * @return
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     *
     * @param userId
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    private String userId;


    /**
     * Return the role as a String.
     *
     * @return String contains a comma delimited set of role names assigned to permission.
     */
    public String getRole()
    {
        return role;
    }


    /**
     * Accept a comma delimited String containing a list of Roles to be granted to a permission.  This function
     * will parse the String and call the setter on its parent.
     *
     * @param role name targeted for role constraint.
     */
    public void setRole( String role )
    {
        this.role = role;
    }
}
