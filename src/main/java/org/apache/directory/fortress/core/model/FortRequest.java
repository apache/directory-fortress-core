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
package org.apache.directory.fortress.core.model;

import javax.xml.bind.annotation.*;

/**
 * This class is used to pass request data to Fortress Rest server.
 * <p>
 * This class is not thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement(name = "FortRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fortRequest", propOrder =
{
    "entity",
    "entity2",
    "value",
    "limit",
    "contextId",
    "session",
    "isReplace"
})
public class FortRequest
{
    @XmlElement(nillable = true)
    private FortEntity entity;
    @XmlElement(nillable = true)
    private FortEntity entity2;
    @XmlElement(nillable = true)
    private Session session;
    private String value;
    @XmlElement(nillable = true)
    private Integer limit;
    private String contextId;
    @XmlElement(nillable = true)
    private Boolean isReplace;

    public FortEntity getEntity()
    {
        return entity;
    }

    public void setEntity(FortEntity entity)
    {
        this.entity = entity;
    }

    public FortEntity getEntity2()
    {
        return entity2;
    }

    public void setEntity2(FortEntity entity2)
    {
        this.entity2 = entity2;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public Session getSession()
    {
        return session;
    }

    public void setSession(Session session)
    {
        this.session = session;
    }

    public Integer getLimit()
    {
        return limit;
    }

    public void setLimit(Integer limit)
    {
        this.limit = limit;
    }

    public String getContextId()
    {
        return contextId;
    }

    public void setContextId(String contextId)
    {
        this.contextId = contextId;
    }

    public Boolean getIsReplace()
    {
        return isReplace;
    }

    public void setIsReplace(Boolean isReplace)
    {
        this.isReplace = isReplace;
    }
}

