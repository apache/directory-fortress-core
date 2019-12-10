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


/**
 * This entity class contains OpenLDAP slapd access log records that correspond to bind attempts made to the directory.
 * <p>
 * The auditBind Structural object class is used to store authentication events that can later be queried via ldap API.<br>
 * <code># The Bind class includes the reqVersion attribute which contains the LDAP</code>
 * <code># protocol version specified in the Bind as well as the reqMethod attribute</code>
 * <code># which contains the Bind Method used in the Bind. This will be the string</code>
 * <code># SIMPLE for LDAP Simple Binds or SASL(mech) for SASL Binds. Note that unless</code>
 * <code># configured as a global overlay, only Simple Binds using DNs that reside in</code>
 * <code># the current database will be logged:</code>
 * <pre>
 * ------------------------------------------
 * objectclass (  1.3.6.1.4.1.4203.666.11.5.2.6 NAME 'auditBind'</code>
 * DESC 'Bind operation'</code>
 * SUP auditObject STRUCTURAL</code>
 * MUST ( reqVersion $ reqMethod ) )</code>
 * ------------------------------------------
 * </pre>
 * <p>
 * Note this class used descriptions pulled from man pages on slapd access log.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement(name = "fortBind")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bind", propOrder =
    {
        "createTimestamp",
        "creatorsName",
        "entryCSN",
        "entryDN",
        "entryUUID",
        "hasSubordinates",
        "modifiersName",
        "modifyTimestamp",
        "objectClass",
        "reqAuthzID",
        "reqControls",
        "reqDN",
        "reqEnd",
        "reqMethod",
        "reqResult",
        "reqSession",
        "reqStart",
        "reqType",
        "reqVersion",
        "structuralObjectClass",
        "sequenceId"
})
public class Bind extends FortEntity implements Serializable
{
    /** Default serialVersionUID */
    private static final long serialVersionUID = 1L;
    private String createTimestamp;
    private String creatorsName;
    private String entryCSN;
    private String entryDN;
    private String entryUUID;
    private String hasSubordinates;
    private String modifiersName;
    private String modifyTimestamp;
    private String objectClass;
    private String reqAuthzID;
    private String reqControls;
    private String reqDN;
    private String reqEnd;
    private String reqMethod;
    private String reqResult;
    private String reqSession;
    private String reqStart;
    private String reqType;
    private String reqVersion;
    private String structuralObjectClass;
    private long sequenceId;


    /**
     * Get the attribute that maps to 'reqStart' which provides the start time of the operation which is also the rDn for the node.
     * These time attributes use generalizedTime syntax. The reqStart attribute is also used as the RDN for each log entry.
     *
     * @return attribute that maps to 'reqStart' in 'auditBind' object class.
     */
    public String getCreateTimestamp()
    {
        return createTimestamp;
    }


    /**
     * Set the attribute that maps to 'reqStart' which provides the start time of the operation which is also the rDn for the node.
     * These time attributes use generalizedTime syntax. The reqStart attribute is also used as the RDN for each log entry.
     *
     * @param createTimestamp attribute that maps to 'reqStart' in 'auditBind' object class.
     */
    public void setCreateTimestamp( String createTimestamp )
    {
        this.createTimestamp = createTimestamp;
    }


    /**
     * Return the user dn containing the identity of log user who added the audit record.  This will be the system user that
     * is configured for performing slapd access log operations on behalf of Fortress.
     *
     * @return value that maps to 'creatorsName' attribute on 'auditBind' object class.
     */
    public String getCreatorsName()
    {
        return creatorsName;
    }


    /**
     * Set the user dn containing the identity of log user who added the audit record.  This will be the system user that
     * is configured for performing slapd access log operations on behalf of Fortress.
     *
     * @param creatorsName maps to 'creatorsName' attribute on 'auditBind' object class.
     */
    public void setCreatorsName( String creatorsName )
    {
        this.creatorsName = creatorsName;
    }


    /**
     * Return the Change Sequence Number (CSN) containing sequence number that is used for OpenLDAP synch replication functionality.
     *
     * @return attribute that maps to 'entryCSN' on 'auditBind' object class.
     */
    public String getEntryCSN()
    {
        return entryCSN;
    }


    /**
     * Set the Change Sequence Number (CSN) containing sequence number that is used for OpenLDAP synch replication functionality.
     *
     * @param entryCSN maps to 'entryCSN' attribute on 'auditBind' object class.
     */
    public void setEntryCSN( String entryCSN )
    {
        this.entryCSN = entryCSN;
    }


    /**
     * Get the entry dn for bind object stored in directory.  This attribute uses the 'reqStart' along with suffix for log.
     *
     * @return attribute that maps to 'entryDN' on 'auditBind' object class.
     */
    public String getEntryDN()
    {
        return entryDN;
    }


    /**
     * Set the entry dn for bind object stored in directory.  This attribute uses the 'reqStart' along with suffix for log.
     *
     * @param entryDN attribute that maps to 'entryDN' on 'auditBind' object class.
     */
    public void setEntryDN( String entryDN )
    {
        this.entryDN = entryDN;
    }


    /**
     * Get the attribute that contains the Universally Unique ID (UUID) of the corresponding 'auditBind' record.
     *
     * @return value that maps to 'entryUUID' attribute on 'auditBind' object class.
     */
    public String getEntryUUID()
    {
        return entryUUID;
    }


    /**
     * Set the attribute that contains the Universally Unique ID (UUID) of the corresponding 'auditBind' record.
     *
     * @param entryUUID that maps to 'entryUUID' attribute on 'auditBind' object class.
     */
    public void setEntryUUID( String entryUUID )
    {
        this.entryUUID = entryUUID;
    }


    /**
     * Get the attribute that corresponds to the boolean value hasSubordinates.
     *
     * @return value that maps to 'hasSubordinates' attribute on 'auditBind' object class.
     */
    public String getHasSubordinates()
    {
        return hasSubordinates;
    }


    /**
     * Set the attribute that corresponds to the boolean value hasSubordinates.
     *
     * @param hasSubordinates maps to same name on 'auditBind' object class.
     */
    public void setHasSubordinates( String hasSubordinates )
    {
        this.hasSubordinates = hasSubordinates;
    }


    /**
     * Return the user dn containing the identity of log user who last modified the audit record.  This will be the system user that
     * is configured for performing slapd access log operations on behalf of Fortress.
     *
     * @return value that maps to 'modifiersName' attribute on 'auditBind' object class.
     */
    public String getModifiersName()
    {
        return modifiersName;
    }


    /**
     * Set the user dn containing the identity of log user who modified the audit record.  This will be the system user that
     * is configured for performing slapd access log operations on behalf of Fortress.
     *
     * @param modifiersName maps to 'modifiersName' attribute on 'auditBind' object class.
     */
    public void setModifiersName( String modifiersName )
    {
        this.modifiersName = modifiersName;
    }


    /**
     * Get the attribute that maps to 'modifyTimestamp' which provides the last time audit record was changed.
     * The time attributes use generalizedTime syntax.
     *
     * @return attribute that maps to 'modifyTimestamp' in 'auditBind' object class.
     */
    public String getModifyTimestamp()
    {
        return modifyTimestamp;
    }


    /**
     * Set the attribute that maps to 'modifyTimestamp' which provides the last time audit record was changed.
     * The time attributes use generalizedTime syntax.
     *
     * @param modifyTimestamp attribute that maps to same name in 'auditBind' object class.
     */
    public void setModifyTimestamp( String modifyTimestamp )
    {
        this.modifyTimestamp = modifyTimestamp;
    }


    /**
     * Get the object class name of the audit record.  For this entity, this value will always be 'auditBind'.
     *
     * @return value that maps to 'objectClass' attribute on 'auditBind' obejct class.
     */
    public String getObjectClass()
    {
        return objectClass;
    }


    /**
     * Set the object class name of the audit record.  For this entity, this value will always be 'auditBind'.
     *
     * @param objectClass value that maps to same name on 'auditBind' obejct class.
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
     * For Fortress bind operations this will map to User#userId
     *
     * @return value that maps to 'reqAuthzID' on 'auditBind' object class.
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
     * For Fortress bind operations this will map to User#userId
     *
     */
    public void setReqAuthzID( String reqAuthzID )
    {
        this.reqAuthzID = reqAuthzID;
    }


    /**
     * The reqControls and reqRespControls attributes carry any controls  sent
     * by  the  client  on  the  request  and  returned  by  the server in the
     * response, respectively. The attribute  values  are  just  uninterpreted
     * octet strings.
     *
     * @return value that maps to 'reqControls' attribute on 'auditBind' object class.
     */
    public String getReqControls()
    {
        return reqControls;
    }


    /**
     * The reqControls and reqRespControls attributes carry any controls  sent
     * by  the  client  on  the  request  and  returned  by  the server in the
     * response, respectively. The attribute  values  are  just  uninterpreted
     * octet strings.
     *
     * @param reqControls maps to same name attribute on 'auditBind' object class.
     */
    public void setReqControls( String reqControls )
    {
        this.reqControls = reqControls;
    }


    /**
     * The reqDN attribute is the  distinguishedName  of  the  target  of  the
     * operation.  E.g.,  for  a Bind request, this is the Bind DN. For an Add
     * request, this is the DN of the entry being added. For a Search request,
     * this is the base DN of the search.
     *
     * @return value that map to 'reqDN' attribute on 'auditBind' object class.
     */
    public String getReqDN()
    {
        return reqDN;
    }


    /**
     * The reqDN attribute is the  distinguishedName  of  the  target  of  the
     * operation.  E.g.,  for  a Bind request, this is the Bind DN. For an Add
     * request, this is the DN of the entry being added. For a Search request,
     * this is the base DN of the search.
     *
     * @param reqDN maps to 'reqDN' attribute on 'auditBind' object class.
     */
    public void setReqDN( String reqDN )
    {
        this.reqDN = reqDN;
    }


    /**
     * reqEnd provide the end time of the operation. It uses generalizedTime syntax.
     *
     * @return value that maps to 'reqEnd' attribute on 'auditBind' object class.
     */
    public String getReqEnd()
    {
        return reqEnd;
    }


    /**
     * reqEnd provide the end time of the operation. It uses generalizedTime syntax.
     *
     * @param reqEnd value that maps to same name on 'auditBind' object class.
     */
    public void setReqEnd( String reqEnd )
    {
        this.reqEnd = reqEnd;
    }


    /**
     * The reqMethod attribute contains the Bind Method used in the Bind. This will be
     * the string SIMPLE for LDAP Simple Binds or SASL(<mech>) for SASL Binds.
     * Note  that  unless  configured  as  a global overlay, only Simple Binds
     * using DNs that reside in the current database will be logged.
     *
     * @return String that maps to 'reqMethod' attribute on 'auditBind' object class.
     */
    public String getReqMethod()
    {
        return reqMethod;
    }


    /**
     * The reqMethod attribute contains the Bind Method used in the Bind. This will be
     * the string SIMPLE for LDAP Simple Binds or SASL(<mech>) for SASL Binds.
     * Note  that  unless  configured  as  a global overlay, only Simple Binds
     * using DNs that reside in the current database will be logged.
     *
     * @param reqMethod maps to same name on 'auditBind' object class.
     */
    public void setReqMethod( String reqMethod )
    {
        this.reqMethod = reqMethod;
    }


    /**
     * The  reqResult  attribute  is  the  numeric  LDAP  result  code  of the
     * operation, indicating either success or a particular LDAP  error  code.
     * An  error code may be accompanied by a text error message which will be
     * recorded in the reqMessage attribute.
     *
     * @return value that maps to 'reqResult' attribute on 'auditBind' object class.
     */
    public String getReqResult()
    {
        return reqResult;
    }


    /**
     * The  reqResult  attribute  is  the  numeric  LDAP  result  code  of the
     * operation, indicating either success or a particular LDAP  error  code.
     * An  error code may be accompanied by a text error message which will be
     * recorded in the reqMessage attribute.
     *
     * @param reqResult maps to same name on 'auditBind' object class.
     */
    public void setReqResult( String reqResult )
    {
        this.reqResult = reqResult;
    }


    /**
     * The reqSession attribute is an implementation-specific identifier  that
     * is  common to all the operations associated with the same LDAP session.
     * Currently this is slapd's internal connection ID, stored in decimal.
     *
     * @return value that maps to 'reqSession' attribute on 'auditBind' object class.
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
     * @param reqSession maps to same name on 'auditBind' object class.
     */
    public void setReqSession( String reqSession )
    {
        this.reqSession = reqSession;
    }


    /**
     * reqStart provide the start of the operation,  They  use generalizedTime syntax.
     * The reqStart attribute is also used as the RDN for each log entry.
     *
     * @return value that maps to 'reqStart' attribute on 'auditBind' object class.
     */
    public String getReqStart()
    {
        return reqStart;
    }


    /**
     * reqStart provide the start of the operation,  They  use generalizedTime syntax.
     * The reqStart attribute is also used as the RDN for each log entry.
     *
     * @param reqStart maps to same name on 'auditBind' object class.
     */
    public void setReqStart( String reqStart )
    {
        this.reqStart = reqStart;
    }


    /**
     * The  reqType  attribute  is  a  simple  string  containing  the type of
     * operation being logged, e.g.  add, delete, search,  etc.  For  extended
     * operations,  the  type also includes the OID of the extended operation,
     * e.g.  extended(1.1.1.1)
     *
     * @return value that maps to 'reqType' attribute on 'auditBind' object class.
     */
    public String getReqType()
    {
        return reqType;
    }


    /**
     * The  reqType  attribute  is  a  simple  string  containing  the type of
     * operation being logged, e.g.  add, delete, search,  etc.  For  extended
     * operations,  the  type also includes the OID of the extended operation,
     * e.g.  extended(1.1.1.1)
     *
     * @param reqType maps to same name on 'auditBind' object class.
     */
    public void setReqType( String reqType )
    {
        this.reqType = reqType;
    }


    /**
     * The reqVersion attribute which contains the
     * LDAP protocol version specified in the Bind
     *
     * @return value that maps to the 'reqVersion' attribute on 'auditBind' object class.
     */
    public String getReqVersion()
    {
        return reqVersion;
    }


    /**
     * The reqVersion attribute which contains the
     * LDAP protocol version specified in the Bind
     *
     * @param reqVersion maps to same name on 'auditBind' object class.
     */
    public void setReqVersion( String reqVersion )
    {
        this.reqVersion = reqVersion;
    }


    /**
     * Returns the name of the structural object class that is used to log the event.  For this entity
     * this value will always be 'auditBind'.
     *
     * @return value that maps to 'structuralObjectClass' attribute that contains the name 'auditBind'.
     */
    public String getStructuralObjectClass()
    {
        return structuralObjectClass;
    }


    /**
     * Returns the name of the structural object class that is used to log the event.  For this entity
     * this value will always be 'auditBind'.
     *
     * @param structuralObjectClass maps to same name on 'auditBind' object class.
     */
    public void setStructuralObjectClass( String structuralObjectClass )
    {
        this.structuralObjectClass = structuralObjectClass;
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
