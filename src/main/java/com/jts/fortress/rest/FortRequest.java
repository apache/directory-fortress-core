/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */
package com.jts.fortress.rest;

import com.jts.fortress.rbac.FortEntity;
import com.jts.fortress.rbac.Session;

import javax.xml.bind.annotation.*;

/**
 * This class is used to pass request data to En Masse server.
 * </p>
 * This class is thread safe.
 *
 * @author Shawn McKinney
 * @created Feb 10, 2012
 */
@XmlRootElement(name = "FortRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fortRequest", propOrder =
{
    "entity",
    "value",
    "limit",
    "contextId",
    "session"
})
public class FortRequest
{
    @XmlElement(nillable = true)
    private FortEntity entity;
    @XmlElement(nillable = true)
    private Session session;
    private String value;
    @XmlElement(nillable = true)
    private Integer limit;
    private String contextId;

    public FortEntity getEntity()
    {
        return entity;
    }

    public void setEntity(FortEntity entity)
    {
        this.entity = entity;
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
}

