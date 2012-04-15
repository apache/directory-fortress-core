/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.audit;

import com.jts.fortress.FortEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

/**
 * This entity is used to pass search criteria into the {@link com.jts.fortress.AuditMgr} APIs, down through the
 * {@link AuditP} process layer and finally into the {@link AuditDAO} data access layer.  Once the data has been
 * retrieved from the directory it will be passed back to the caller using one of audit output entities.
 * <p/>
 * All audit data is returned to user using one of the following:
 * <ul>
 * <li> Authorization events: {@link AuthZ}
 * <li> Authentication events: {@link Bind}
 * <li> Modification events: {@link Mod}
 * </ul>
 * <p/>
 * <p/>
 *
 * @author Shawn McKinney
 * @created April 1, 2010
 */
@XmlRootElement(name = "fortUserAudit")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userAudit", propOrder = {
    "name",
    "description",
    "failedOnly",
    "objName",
    "opName",
    "userId",
    "internalUserId",
    "beginDate",
    "endDate",
    "dn"
})
public class UserAudit extends FortEntity implements java.io.Serializable
{
    private String name;
    private String description;
    private boolean failedOnly;
    private String objName;
    private String opName;
    private String userId;
    private String internalUserId;
    @XmlElement(nillable = true)
    private Date beginDate;
    @XmlElement(nillable = true)
    private Date endDate;
    private String dn;


    /**
     * Get the optional objName attribute which limits set by {@link com.jts.fortress.rbac.Permission#objectName}.
     * For modification search, this attr maps to {@link AuditDAO#REQMOD}.  For authorization search, it will map to {@link AuditDAO#REQDN}.
     * The object name is derived from another class name which represents targets for Fortress authorizations. For example {@link com.jts.fortress.rbac.AdminMgrImpl} or 'CustomerCheckOutPage'.
     *
     * @return the name of the object which maps to 'reqDn' for 'auditSearch' target, or 'reqMod' for 'auditMod' search.
     */
    public String getObjName()
    {
        return objName;
    }

    /**
     * Set the optional objName attribute which limits set by {@link com.jts.fortress.rbac.Permission#objectName}.
     * For modification search, this attr maps to {@link AuditDAO#REQMOD}.  For authorization search, it will map to {@link AuditDAO#REQDN}.
     * The object name is derived from another class name which represents targets for Fortress authorizations. For example {@link com.jts.fortress.rbac.AdminMgrImpl} or 'CustomerCheckOutPage'.
     *
     * @param objName maps to 'reqDn' for 'auditSearch' target, or 'reqMod' for 'auditMod' search.
     */
    public void setObjName(String objName)
    {
        this.objName = objName;
    }

    /**
     * The failedOnly flag will limit result set to include only authN or authZ events that have failed.
     * <p/>
     * <ul>
     * <li>{@link AuditMgrImpl#searchInvalidUsers(UserAudit)} maps to ({@link AuditDAO#REQENTRIES} == 0)
     * <li>{@link AuditMgrImpl#searchAuthZs(UserAudit)} maps to ({@link AuditDAO#REQENTRIES} == 0)
     * <li>{@link AuditMgrImpl#searchBinds(UserAudit)} maps to ({@link AuditDAO#REQRESULT} >= 1)
     * </ul>
     *
     * @return boolean if true will limit search to failed events.
     */
    public boolean isFailedOnly()
    {
        return failedOnly;
    }

    /**
     * The failedOnly flag will limit result set to include only authN or authZ events that have failed.
     * <p/>
     * <ul>
     * <li>{@link AuditMgrImpl#searchInvalidUsers(UserAudit)} maps to ({@link AuditDAO#REQENTRIES} == 0)
     * <li>{@link AuditMgrImpl#searchAuthZs(UserAudit)} maps to ({@link AuditDAO#REQENTRIES} == 0)
     * <li>{@link AuditMgrImpl#searchBinds(UserAudit)} maps to ({@link AuditDAO#REQRESULT} >= 1)
     * </ul>
     *
     * @param failedOnly if boolean true search will limit to failed only.
     */
    public void setFailedOnly(boolean failedOnly)
    {
        this.failedOnly = failedOnly;
    }

    /**
     * Get the optional opName attribute which limits {@link AuditMgrImpl#searchAdminMods(UserAudit)} by {@link AuditDAO#REQMOD}.
     * The operation name is derived from a method name of a class which represents targets for Fortress authorizations. For example 'read', 'search' or 'add'.
     *
     * @return value that maps to 'reqMod' on 'auditMod' object class.
     */
    public String getOpName()
    {
        return opName;
    }

    /**
     * Set the optional opName attribute which limits {@link AuditMgrImpl#searchAdminMods(UserAudit)} by {@link AuditDAO#REQMOD}.
     * The operation name is derived from a method name of a class which represents targets for Fortress authorizations. For example 'read', 'search' or 'add'.
     *
     * @param opName attribute maps to 'reqMod' on 'auditMod' object class.
     */
    public void setOpName(String opName)
    {
        this.opName = opName;
    }

    /**
     * Get the optional userId attribute which limits set by {@link com.jts.fortress.rbac.User#userId}.
     * For authentication searchs, this attr maps to {@link AuditDAO#REQDN}.  For authorization search, it will map to {@link AuditDAO#REQUAUTHZID}.
     * The userId for this search represents the end user.
     *
     * @return the userId which maps to 'reqDn' for authentications or 'reqAuthzID' for authorization events.
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * Set the optional userId attribute which limits set by {@link com.jts.fortress.rbac.User#userId}.
     * For authentication searchs, this attr maps to {@link AuditDAO#REQDN}.  For authorization search, it will map to {@link AuditDAO#REQUAUTHZID}.
     * The userId for this search represents the end user.
     *
     * @param userId maps to 'reqDn' for authentications or 'reqAuthzID' for authorization events.
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * Get the optional internalUserId attribute which limits set by {@link com.jts.fortress.rbac.User#internalId}.
     * For {@link AuditMgrImpl#searchUserSessions(UserAudit)} this attr maps to {@link AuditDAO#REQMOD}.
     * The internalUserId for this search represents the end user but is stored as its internal id.
     *
     * @return the internalUserId which maps to 'reqMod' for 'auditModify' object class searches.
     */
    public String getInternalUserId()
    {
        return internalUserId;
    }

    /**
     * Set the optional internalUserId attribute which limits set by {@link com.jts.fortress.rbac.User#internalId}.
     * For {@link AuditMgrImpl#searchUserSessions(UserAudit)} this attr maps to {@link AuditDAO#REQMOD}.
     * The internalUserId for this search represents the end user but is stored as its internal id.
     *
     * @param internalUserId maps to 'reqMod' for 'auditModify' object class searches.
     */
    public void setInternalUserId(String internalUserId)
    {
        this.internalUserId = internalUserId;
    }

    /**
     * Get the Date for search to begin.  The earlier the date, the more records will be returned.
     * This attribute is mapped to 'reqStart' on slapd audit records which provides the start
     * time of the operation which is also the rDn for the node.
     *
     * @return attribute that maps to 'reqStart' in audit object classes.
     */
    public Date getBeginDate()
    {
        return beginDate;
    }

    /**
     * Set the Date for search to begin.  The earlier the date, the more records will be returned.
     * This attribute is mapped to 'reqStart' on slapd audit records which provides the start
     * time of the operation which is also the rDn for the node.
     *
     * @param beginDate attribute that maps to 'reqStart' in audit object classes.
     */
    public void setBeginDate(Date beginDate)
    {
        this.beginDate = beginDate;
    }

    /**
     *
     * @return
     */
    public Date getEndDate()
    {
        return endDate;
    }

    /**
     *
     * @param endDate
     */
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    /**
     * Get the optional dn attribute can be used to constraint {@link AuditMgrImpl#searchUserSessions(UserAudit)}.
     * The dn for this search may represent any target entry in DIT that has been recently modified or deleted.
     *
     * @return the dn which maps to 'reqDn' for 'auditModify' object class searches.
     */
    public String getDn()
    {
        return dn;
    }

    /**
     * Set the optional dn attribute can be used to constraint {@link AuditMgrImpl#searchUserSessions(UserAudit)}.
     * The dn for this search may represent any target entry in DIT that has been recently modified or deleted.
     *
     * @param dn maps to 'reqDn' for 'auditModify' object class searches.
     */
    public void setDn(String dn)
    {
        this.dn = dn;
    }
}