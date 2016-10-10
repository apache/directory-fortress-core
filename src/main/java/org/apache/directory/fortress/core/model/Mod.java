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


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;


/**
 * This entity class contains OpenLDAP slapd access log records that correspond to modifications made to the directory.
 * <p>
 * The auditModify Structural object class is used to store Fortress update and delete events that can later be queried via ldap API.<br>
 * The deletions can be recorded in this manner and associated with Fortress context because deletions will perform a modification first
 * if audit is enabled.
 * <p>
 * <code>The Modify operation contains a description  of  modifications  in  the</code><br>
 * <code>reqMod  attribute,  which  was  already  described  above  in  the  Add</code><br>
 * <code>operation. It may optionally  contain  the  previous  contents  of  any</code><br>
 * <code>modified  attributes  in the reqOld attribute, using the same format as</code><br>
 * <code>described above for the Delete operation.  The reqOld attribute is only</code><br>
 * <code>populated  if  the  entry  being modified matches the configured logold</code><br>
 * <code>filter.</code><br>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass (  1.3.6.1.4.1.4203.666.11.5.2.9</code>
 * <li> <code>NAME 'auditModify'</code>
 * <li> <code>DESC 'Modify operation'</code>
 * <li> <code>SUP auditWriteObject STRUCTURAL</code>
 * <li> <code>MAY reqOld MUST reqMod )</code>
 * <li> ------------------------------------------
 * </ul>
 * <p>
 * Note this class uses descriptions pulled from man pages on slapd access log.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement(name = "fortMod")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mod", propOrder =
    {
        "reqSession",
        "objectClass",
        "reqAuthzID",
        "reqDN",
        "reqResult",
        "reqStart",
        "reqEnd",
        "reqMod",
        "reqType",
        "sequenceId"
})
public class Mod extends FortEntity implements Serializable
{
    /** Default serialVersionUID */
    private static final long serialVersionUID = 1L;
    private String reqSession;
    private String objectClass;
    private String reqAuthzID;
    private String reqDN;
    private String reqResult;
    private String reqStart;
    private String reqEnd;
    private String reqType;
    private List<String> reqMod;
    private long sequenceId;


    /**
     * The reqMod attribute carries all of the attributes of the original entry being added.
     * (Or in the case of a Modify operation, all of the modifications being performed.)
     * The values are formatted as attribute:&lt;+|-|=|#&gt; [ value] Where '+' indicates an Add of a value,
     * '-' for Delete, '=' for Replace, and '#' for Increment. In an Add operation, all of  the
     * reqMod  values will have the '+' designator.
     *
     * @return collection of Strings that map to 'reqMod' attribute on 'auditModify' object class.
     */
    public List<String> getReqMod()
    {
        return reqMod;
    }


    /**
     * The reqMod attribute carries all of the attributes of the original entry being added.
     * (Or in the case of a Modify operation, all of the modifications being performed.)
     * The values are formatted as attribute:&lt;+|-|=|#&gt; [ value] Where '+' indicates an Add of a value,
     * '-' for Delete, '=' for Replace, and '#' for Increment. In an Add operation, all of  the
     * reqMod  values will have the '+' designator.
     *
     * @param reqMod contains collection of Strings that map to 'reqMod' attribute on 'auditModify' object class.
     */
    public void setReqMod( List<String> reqMod )
    {
        this.reqMod = reqMod;
    }


    /**
     * reqEnd provide the end time of the operation. It uses generalizedTime syntax.
     *
     * @return value that maps to 'reqEnd' attribute on 'auditModify' object class.
     */
    public String getReqEnd()
    {
        return reqEnd;
    }


    /**
     * reqEnd provide the end time of the operation. It uses generalizedTime syntax.
     *
     * @param reqEnd value that maps to same name on 'auditModify' object class.
     */
    public void setReqEnd( String reqEnd )
    {
        this.reqEnd = reqEnd;
    }


    /**
     * The reqSession attribute is an implementation-specific identifier  that
     * is  common to all the operations associated with the same LDAP session.
     * Currently this is slapd's internal connection ID, stored in decimal.
     *
     * @return value that maps to 'reqSession' attribute on 'auditModify' object class.
     */
    public String getReqSession()
    {
        return reqSession;
    }


    /**
     * The reqSession attribute is an implementation-specific identifier  that
     * is  common to all the operations associated with the same LDAP session.
     * Currently this is slapd's internal connection ID, stored in decimal.
     *
     * @param reqSession maps to same name on 'auditModify' object class.
     */
    public void setReqSession( String reqSession )
    {
        this.reqSession = reqSession;
    }


    /**
     * Get the object class name of the audit record.  For this entity, this value will always be 'auditModify'.
     *
     * @return value that maps to 'objectClass' attribute on 'auditModify' obejct class.
     */
    public String getObjectClass()
    {
        return objectClass;
    }


    /**
     * Set the object class name of the audit record.  For this entity, this value will always be 'auditModify'.
     *
     * @param objectClass value that maps to same name on 'auditModify' obejct class.
     */
    public void setObjectClass( String objectClass )
    {
        this.objectClass = objectClass;
    }


    /**
     * The  reqAuthzID  attribute  is  the  distinguishedName of the user that
     * performed the operation.  This will usually be the  same  name  as  was
     * established  at  the  start of a session by a Bind request (if any) but
     * may be altered in various circumstances.
     * For Fortress bind operations this will map to {@link User#userId}
     *
     * @return value that maps to 'reqAuthzID' on 'auditModify' object class.
     */
    public String getReqAuthzID()
    {
        return reqAuthzID;
    }


    /**
     * The  reqAuthzID  attribute  is  the  distinguishedName of the user that
     * performed the operation.  This will usually be the  same  name  as  was
     * established  at  the  start of a session by a Bind request (if any) but
     * may be altered in various circumstances.
     * For Fortress bind operations this will map to {@link User#userId}
     *
     */
    public void setReqAuthzID( String reqAuthzID )
    {
        this.reqAuthzID = reqAuthzID;
    }


    /**
     * The reqDN attribute is the  distinguishedName  of  the  target  of  the
     * operation.  E.g.,for a Bind request, this is the Bind DN. For an Add
     * request, this is the DN of the entry being added. For a Search request,
     * this is the base DN of the search.
     *
     * @return value that map to 'reqDN' attribute on 'auditModify' object class.
     */
    public String getReqDN()
    {
        return reqDN;
    }


    /**
     * The reqDN attribute is the  distinguishedName  of  the  target  of  the
     * operation. E.g., for a Bind request, this is the Bind DN. For an Add
     * request, this is the DN of the entry being added. For a Search request,
     * this is the base DN of the search.
     *
     * @param reqDN maps to 'reqDN' attribute on 'auditModify' object class.
     */
    public void setReqDN( String reqDN )
    {
        this.reqDN = reqDN;
    }


    /**
     * The reqResult attribute is the numeric LDAP result code of the
     * operation, indicating either success or a particular LDAP  error  code.
     * An  error code may be accompanied by a text error message which will be
     * recorded in the reqMessage attribute.
     *
     * @return value that maps to 'reqResult' attribute on 'auditModify' object class.
     */
    public String getReqResult()
    {
        return reqResult;
    }


    /**
     * The reqResult attribute is the numeric LDAP result code of the
     * operation, indicating either success or a particular LDAP  error  code.
     * An  error code may be accompanied by a text error message which will be
     * recorded in the reqMessage attribute.
     *
     * @param reqResult maps to same name on 'auditModify' object class.
     */
    public void setReqResult( String reqResult )
    {
        this.reqResult = reqResult;
    }


    /**
     * reqStart provide the start of the operation, They use generalizedTime syntax.
     * The reqStart attribute is also used as the RDN for each log entry.
     *
     * @return value that maps to 'reqStart' attribute on 'auditModify' object class.
     */
    public String getReqStart()
    {
        return reqStart;
    }


    /**
     * reqStart provide the start of the operation, They use generalizedTime syntax.
     * The reqStart attribute is also used as the RDN for each log entry.
     *
     * @param reqStart maps to same name on 'auditModify' object class.
     */
    public void setReqStart( String reqStart )
    {
        this.reqStart = reqStart;
    }


    /**
     * The reqType attribute is a simple string containing the type of
     * operation being logged, e.g.  add, delete, search,  etc.  For  extended
     * operations, the  type also includes the OID of the extended operation,
     * e.g. extended(1.1.1.1)
     *
     * @return value that maps to 'reqType' attribute on 'auditModify' object class.
     */
    public String getReqType()
    {
        return reqType;
    }


    /**
     * The reqType attribute is a simple string containing the type of
     * operation being logged, e.g. add, delete, search, etc. For extended
     * operations,  the  type also includes the OID of the extended operation,
     * e.g.extended(1.1.1.1)
     *
     * @param reqType maps to same name on 'auditModify' object class.
     */
    public void setReqType( String reqType )
    {
        this.reqType = reqType;
    }


    /**
     * Sequence id is used internal to Fortress.
     * @return long value contains sequence id.
     */
    public long getSequenceId()
    {
        return sequenceId;
    }


    /**
     * Sequence id is used internal to Fortress
     * @param sequenceId contains sequence to use.
     */
    public void setSequenceId( long sequenceId )
    {
        this.sequenceId = sequenceId;
    }
}