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
package org.openldap.fortress.rest;

import org.openldap.fortress.rbac.FortEntity;
import org.openldap.fortress.rbac.Session;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Set;

/**
 * This class is used to return response data from En Masse server.
 * </p>
 * This class is not thread safe.
 *
 * @author Shawn McKinney
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
    @XmlElement(nillable = true)
    private Boolean isAuthorized;
    private String errorMessage;
    @XmlElement(nillable = true)
    private FortEntity entity;
    @XmlElement(nillable = true)
    private List<FortEntity> entities;
    @XmlElement(nillable = true)
    private List<String> values;
    @XmlElement(nillable = true)
    private Set<String> valueSet;
    @XmlElement(nillable = true)
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

