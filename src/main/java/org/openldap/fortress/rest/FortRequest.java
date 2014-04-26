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

