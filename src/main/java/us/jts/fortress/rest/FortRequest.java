/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */
package us.jts.fortress.rest;

import us.jts.fortress.rbac.FortEntity;
import us.jts.fortress.rbac.Session;

import javax.xml.bind.annotation.*;

/**
 * This class is used to pass request data to En Masse server.
 * </p>
 * This class is not thread safe.
 *
 * @author Shawn McKinney
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

