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
import java.util.List;
import java.util.Set;

/**
 * This class is used to return response data from Fortress Rest server.
 * <p>
 * This class is not thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement(name = "FortResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fortResponse", propOrder =
{
    "errorCode",
    "errorMessage",
    "entity",
    "entities",
    "values",
    "valueSet",
    "isAuthorized",
    "session"
})
public class FortResponse
{
    private int errorCode;
    private Boolean isAuthorized;
    private String errorMessage;
    private FortEntity entity;
    private List<FortEntity> entities;
    private List<String> values;
    private Set<String> valueSet;
    private Session session;

    public FortEntity getEntity()
    {
        return entity;
    }

    public void setEntity(FortEntity entity)
    {
        this.entity = entity;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public Boolean getAuthorized()
    {
        return isAuthorized;
    }

    public void setAuthorized(Boolean authorized)
    {
        isAuthorized = authorized;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public <T extends FortEntity> List<T> getEntities()
    {
        return (List<T>)entities;
    }

    public <T extends FortEntity> void setEntities(List<T> entities)
    {
        this.entities = (List<FortEntity>)entities;
    }

    public List<String> getValues()
    {
        return values;
    }

    public void setValues(List<String> values)
    {
        this.values = values;
    }

    public Set<String> getValueSet()
    {
        return valueSet;
    }

    public void setValueSet(Set<String> valueSet)
    {
        this.valueSet = valueSet;
    }

    public Session getSession()
    {
        return session;
    }

    public void setSession(Session session)
    {
        this.session = session;
    }
}

