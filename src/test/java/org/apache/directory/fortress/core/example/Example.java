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
package org.apache.directory.fortress.core.example;


import org.apache.directory.fortress.core.model.Constraint;

import java.rmi.server.UID;
import java.util.List;


public class Example
    implements Constraint, java.io.Serializable
{
    private String id; // this maps to oamId
    private String name; // this is oamRoleName
    private String description; // this is description
    private String dn; // this attribute is automatically saved to each ldap record.
    private String beginTime; // this attribute is oamBeginTime
    private String endTime; // this attribute is oamEndTime
    private String beginDate; // this attribute is oamBeginDate
    private String endDate; // this attribute is oamEndDate
    private String beginLockDate;// this attribute is oamBeginLockDate
    private String endLockDate; // this attribute is oamEndLockDate
    private String dayMask; // this attribute is oamDayMask
    private int timeout; // this attribute is oamTimeOut


    public String getRawData()
    {
        return rawData;
    }


    public void setRawData( String rawData )
    {
        this.rawData = rawData;
    }

    private String rawData;


    public String getInheritedDN()
    {
        return inheritedDN;
    }


    public void setInheritedDN( String inheritedDN )
    {
        this.inheritedDN = inheritedDN;
    }

    private String inheritedDN;


    public String getTestname()
    {
        return testname;
    }


    public void setTestname( String testname )
    {
        this.testname = testname;
    }

    private String testname; // this is oamRoleName


    /**
     * Gets the name attribute of the Role object
     *
     * @return The name value
     */
    public String getName()
    {
        return this.name;
    }


    /**
     * Sets the name attribute of the Role object
     *
     * @param name The new name value
     */
    public void setName( String name )
    {
        this.name = name;
    }


    /**
     * Gets the description attribute of the Role object
     *
     * @return The description value
     */
    public String getDescription()
    {
        return this.description;
    }


    /**
     * Sets the description attribute of the Role object
     *
     * @param description The new description value
     */
    public void setDescription( String description )
    {
        this.description = description;
    }


    /**
     * Gets the id attribute of the Role object
     *
     * @return The id value
     */
    public String getId()
    {
        return id;
    }


    /**
     * Sets the id attribute of the Role object
     */
    public void setId()
    {
        // generate a unique id that will be used as the rDn for this entry:
        UID iid = new UID();
        // assign the unique id to the internal id of the entity:
        this.id = iid.toString();
    }


    public void setId( String id )
    {
        this.id = id;
    }


    public boolean isTemporalSet()
    {
        return ( beginTime != null && endTime != null && beginDate != null && endDate != null && beginLockDate != null
            && endLockDate != null && dayMask != null );
    }


    public String getBeginTime()
    {
        return this.beginTime;
    }


    public void setBeginTime( String beginTime )
    {
        this.beginTime = beginTime;
    }


    public String getEndTime()
    {
        return this.endTime;
    }


    public void setEndTime( String endTime )
    {
        this.endTime = endTime;
    }


    public String getBeginDate()
    {
        return this.beginDate;
    }


    public void setBeginDate( String beginDate )
    {
        this.beginDate = beginDate;
    }


    public String getEndDate()
    {
        return this.endDate;
    }


    public void setEndDate( String endDate )
    {
        this.endDate = endDate;
    }


    public String getBeginLockDate()
    {
        return this.beginLockDate;
    }


    public void setBeginLockDate( String beginLockDate )
    {
        this.beginLockDate = beginLockDate;
    }


    public String getEndLockDate()
    {
        return this.endLockDate;
    }


    public void setEndLockDate( String endLockDate )
    {
        this.endLockDate = endLockDate;
    }


    public String getDayMask()
    {
        return this.dayMask;
    }


    public void setDayMask( String dayMask )
    {
        this.dayMask = dayMask;
    }


    public Integer getTimeout()
    {
        return this.timeout;
    }


    public void setTimeout( Integer timeout )
    {
        this.timeout = timeout;
    }

    @Override
    public List getConstraints()
    {
        throw new java.lang.UnsupportedOperationException();
    }
}
