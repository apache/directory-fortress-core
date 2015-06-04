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
package org.apache.directory.fortress.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.name.Rdn;
import org.apache.directory.fortress.core.model.Bind;
import org.apache.directory.fortress.core.model.AuthZ;
import org.apache.directory.fortress.core.model.Mod;
import org.apache.directory.fortress.core.impl.TestUtils;
import org.apache.directory.fortress.core.model.UserAudit;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.util.AuditUtil;
import org.apache.directory.fortress.core.util.time.TUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

class AuditMgrConsole
{
    private AuditMgr am = null;
    private static final String CLS_NM = AuditMgrConsole.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    public AuditMgrConsole()
    {
        try
        {
            am = AuditMgrFactory.createInstance(TestUtils.getContext());
        }
        catch ( org.apache.directory.fortress.core.SecurityException e)
        {
            LOG.error(" constructor caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
    }

    /**
     *
     */
    void findBinds()
    {
        ReaderUtil.clearScreen();
        try
        {
            System.out.println("Enter userId value to search Audit Binds with or null to retrieve all:");
            String val = ReaderUtil.readLn();
            UserAudit uAudit = new UserAudit();
            uAudit.setUserId(val);
            List<Bind> list = am.searchBinds(uAudit);
            printAuthNs(list);
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("findBinds caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     *
     */
    void getBindReport()
    {
        ReaderUtil.clearScreen();
        try
        {
            System.out.println("Enter userId value to search Audit Binds with or null to retrieve all:");
            String val = ReaderUtil.readLn();
            UserAudit uAudit = new UserAudit();
            uAudit.setUserId(val);
            System.out.println("Check for failed only? (Enter 'Y' for yes or 'N' for no");
            val = ReaderUtil.readLn();
            if (val.equalsIgnoreCase("Y"))
                uAudit.setFailedOnly(true);

            System.out.println("Check within the last n hours?  Enter number of hours or null for unlimited");
            val = ReaderUtil.readLn();
            if (val != null && val.length() > 0)
            {
                int hours = Integer.parseInt(val);
                Date date = new Date();
                long millis = date.getTime();
                millis = millis - (1000 * 60 * 60 * hours);
                Date date2 = new Date(millis);
                uAudit.setBeginDate(date2);
            }

            List<Bind> list = am.searchBinds(uAudit);
            printAuthNReport(list);
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("getBindReport caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     *
     * @param list
     */
    void printAuthNReport(List<Bind> list)
    {
        if (list != null && list.size() > 0)
        {
            int ctr = 0;
            for (Bind aBind : list)
            {
                /*
            public class Bind
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
                */
                System.out.println("AUTHENTICATION AUDIT RECORD " + ctr++);
                System.out.println("***************************************");
                System.out.println("    UserId        " + AuditUtil.getAuthZId( aBind.getReqDN() ));
                Date aDate = null;
                try
                {
                    aDate = TUtil.decodeGeneralizedTime( aBind.getReqEnd() );
                }
                catch (ParseException pe)
                {
                    System.out.println("    Bind Time     " + "ParseException=" + pe.getMessage());
                }
                if(aDate != null)
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    String formattedDate = formatter.format(aDate);
                    System.out.println("    Bind Time     " + formattedDate);
                }
                System.out.println("    AuthN Type    " + aBind.getReqMethod());
                System.out.println("    Success?      " + aBind.getReqResult().equals("0"));
                System.out.println("    Session       " + aBind.getReqSession());
                System.out.println("    Type          " + aBind.getReqType());
                System.out.println("    Version       " + aBind.getReqVersion());
                System.out.println();
                System.out.println();
            }
        }
        else
        {
            System.out.println("no authN's found");
        }
    }


    /**
     * 
     * @param list
     */
    void printAuthNs(List<Bind> list)
    {
        if (list != null && list.size() > 0)
        {
            int ctr = 0;
            for (Bind aBind : list)
            {
                /*
            public class Bind
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
                */

                System.out.println("AUDIT BIND OBJECT [" + ctr++ + "]:");
                System.out.println("    createTimestamp          [" + aBind.getCreateTimestamp() + "]");
                System.out.println("    creatorsName             [" + aBind.getCreatorsName() + "]");
                System.out.println("    entryCSN                 [" + aBind.getEntryCSN() + "]");
                System.out.println("    entryDN                  [" + aBind.getEntryDN() + "]");
                System.out.println("    entryUUID                [" + aBind.getEntryUUID() + "]");
                System.out.println("    hasSubordinates          [" + aBind.getHasSubordinates() + "]");
                System.out.println("    modifiersName            [" + aBind.getModifiersName() + "]");
                System.out.println("    modifyTimestamp          [" + aBind.getModifyTimestamp() + "]");
                System.out.println("    objectClass              [" + aBind.getObjectClass() + "]");
                System.out.println("    reqAuthzID               [" + aBind.getReqAuthzID() + "]");
                System.out.println("    reqControls              [" + aBind.getReqControls() + "]");
                System.out.println("    reqDN                    [" + aBind.getReqDN() + "]");
                System.out.println("    reqEnd                   [" + aBind.getReqEnd() + "]");
                System.out.println("    reqMethod                [" + aBind.getReqMethod() + "]");
                System.out.println("    reqResult                [" + aBind.getReqResult() + "]");
                System.out.println("    reqSession               [" + aBind.getReqSession() + "]");
                System.out.println("    reqStart                 [" + aBind.getReqStart() + "]");
                System.out.println("    reqType                  [" + aBind.getReqType() + "]");
                System.out.println("    reqVersion               [" + aBind.getReqVersion() + "]");
                System.out.println("    structuralObjectClass    [" + aBind.getStructuralObjectClass() + "]");
            }
        }
        else
        {
            System.out.println("no authN's found");
        }
    }


    /**
     *
     */
    void findAuthZs()
    {
        ReaderUtil.clearScreen();
        try
        {
            System.out.println("Enter object name to search Audit AuthZs with:");
            String val = ReaderUtil.readLn();
            UserAudit uAudit = new UserAudit();
            uAudit.setObjName(val);
            System.out.println("Enter operation name to search Audit AuthZs with:");
            val = ReaderUtil.readLn();
            uAudit.setOpName(val);
            System.out.println("Enter userId to search Audit AuthZs with:");
            val = ReaderUtil.readLn();
            if(StringUtils.isNotEmpty( val ))
            {
                uAudit.setUserId(val);
                System.out.println("size=" + val.length() + " val=" + val);

            }
            else
            {
                System.out.println("val is empty or null");

            }
            //uAudit.setUserId(val);
            System.out.println("Check for failed only? (Enter 'Y' for yes or 'N' for no");
            val = ReaderUtil.readLn();
            if (val.equalsIgnoreCase("Y"))
                uAudit.setFailedOnly(true);

            List<AuthZ> list = am.searchAuthZs(uAudit);
            printAuthZs(list);
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("findAuthZs caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    /**
     *
     */
    void getAuthZs()
    {
        ReaderUtil.clearScreen();
        try
        {
            UserAudit uAudit = new UserAudit();
            System.out.println("Enter userId to search Audit AuthZs with:");
            String val = ReaderUtil.readLn();
            if(StringUtils.isNotEmpty( val ))
            {
                uAudit.setUserId(val);
                System.out.println("size=" + val.length() + " val=" + val);

            }
            else
            {
                System.out.println("val is empty or null");

            }
            System.out.println("Check for failed only? (Enter 'Y' for yes or 'N' for no");
            val = ReaderUtil.readLn();
            if (val.equalsIgnoreCase("Y"))
                uAudit.setFailedOnly(true);

            List<AuthZ> list = am.getUserAuthZs(uAudit);
            printAuthZs(list);
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("getUserAuthZs caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    /**
     *
     */
    void getAuthReport()
    {
        ReaderUtil.clearScreen();
        try
        {
            UserAudit uAudit = new UserAudit();
            System.out.println("Enter userId to search Audit AuthZs with:");
            String val = ReaderUtil.readLn();
            if(StringUtils.isNotEmpty( val ))
            {
                uAudit.setUserId(val);
                System.out.println("size=" + val.length() + " val=" + val);

            }
            else
            {
                System.out.println("val is empty or null");

            }
                        
            //uAudit.setUserId(val);
            System.out.println("Check for failed only? (Enter 'Y' for yes or 'N' for no");
            val = ReaderUtil.readLn();
            if (val.equalsIgnoreCase("Y"))
                uAudit.setFailedOnly(true);

            System.out.println("Check within the last n hours?  Enter number of hours or null for unlimited");
            val = ReaderUtil.readLn();
            if (val != null && val.length() > 0)
            {
                int hours = Integer.parseInt(val);
                Date date = new Date();
                long millis = date.getTime();
                millis = millis - (1000 * 60 * 60 * hours);
                Date date2 = new Date(millis);
                uAudit.setBeginDate(date2);
            }

            List<AuthZ> list = am.getUserAuthZs(uAudit);
            printAuthZReport(list);
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("getAuthReport caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     * @param list
     */
    void printAuthZReport(List<AuthZ> list)
    {
        ReaderUtil.clearScreen();
        if (list != null && list.size() > 0)
        {
            int ctr = 0;
            for (AuthZ authZ : list)
            {
                /*
            public class AuthZ
            {
                private String createTimestamp;
                private String creatorsName;
                private String entryCSN;
                private String entryDN;
                private String entryUUID;
                private String hasSubordinates;
                private String modifiersName;
                private String modifyTimestamp;
                private String objectClass;
                private String reqAttr;
                private String reqAttrsOnly;
                private String reqAuthzID;
                private String reqControls;
                private String reqDN;
                private String reqDerefAliases;
                private String reqEnd;
                private String reqEntries;
                private String reqFilter;
                private String reqResult;
                private String reqScope;
                private String reqSession;
                private String reqSizeLimit;
                private String reqStart;
                private String reqTimeLimit;
                private String reqType;
                private String structuralObjectClass;
                private String subschemaSubentry;
                */
                //System.out.println("**********************************");
                System.out.println("AUTHORIZATION AUDIT RECORD " + ctr++);
                System.out.println("***************************************");
                Date aDate = null;
                try
                {
                    aDate = TUtil.decodeGeneralizedTime(authZ.getReqEnd());
                }
                catch (ParseException pe)
                {
                    System.out.println("    Access Time    " + "ParseException=" + pe.getMessage());
                }
                if(aDate != null)
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    String formattedDate = formatter.format(aDate);
                    System.out.println("    Access Time     " + formattedDate);
                }

                System.out.println("    userId          " + AuditUtil.getAuthZId( authZ.getReqAuthzID() ));
                try
                {
                    Permission pOp = getAuthZPerm(authZ);
                    System.out.println("    Resource Name   " + pOp.getObjName());
                    System.out.println("    Operation       " + pOp.getOpName());
                    int rCtr = 0;
                    if ( pOp.getRoles() != null )
                    {
                        // TODO: fix the NPE that happens here:
                        System.out.println("    Success?        " + authZ.getReqEntries().equals(GlobalIds.AUTHZ_COMPARE_FAILURE_FLAG));
                        for (String role : pOp.getRoles())
                        {
                            System.out.println("    Role[" + rCtr++ + "]         " + role);
                        }
                    }
                }
                catch(LdapInvalidDnException e)
                {
                    System.out.println("LdapInvalidDnException=" + e);
                }
                //System.out.println("    reqStart        [" + authZ.getReqStart() + "]");
                //System.out.println("    reqEnd          [" + authZ.getReqEnd() + "]");
                System.out.println();
                System.out.println();
                //System.out.println("**********************************");
            }
        }
        else
        {
            System.out.println("AuthZ list empty");
        }
    }

    /**
     * Break the authZ eqDn attribute into 1. permission object name, 2. op name and 3. object id (optional).
     *
     * @param authZ contains the raw dn format from openldap slapo access log data
     * @return Permisison containing objName, opName and optionally the objId populated from the raw data.
     */
    public static Permission getAuthZPerm(AuthZ authZ) throws LdapInvalidDnException
    {
        // This will be returned to the caller:
        Permission pOp = new Permission();
        // Break dn into rdns for leaf and parent.  Use the 'type' field in rdn.
        // The objId value is optional.  If present it will be part of the parent's relative distinguished name..
        // Here the sample reqDN=ftOpNm=TOP2_2+ftObjId=002,ftObjNm=TOB2_1,ou=Permissions,ou=RBAC,dc=example,dc=com
        // Will be mapped to objName=TOB2_1, opName=TOP2_2, objId=002, in the returned permission object.
        Dn dn = new Dn( authZ.getReqDN() );
        if( dn.getRdns() != null && CollectionUtils.isNotEmpty( dn.getRdns() ) )
        {
            for( Rdn rdn : dn.getRdns() )
            {
                // The rdn type attribute will be mapped to objName, opName and objId fields.
                switch ( rdn.getType() )
                {
                    case GlobalIds.POP_NAME:
                        pOp.setOpName( rdn.getType() );
                        break;
                    case GlobalIds.POBJ_NAME:
                        pOp.setObjName( rdn.getType() );
                        break;
                    case GlobalIds.POBJ_ID:
                        pOp.setObjId( rdn.getType() );
                        break;
                }
            }
        }
        return pOp;
    }

    void printfailedAuthNReport(List<AuthZ> list)
    {
        ReaderUtil.clearScreen();
        if (list != null && list.size() > 0)
        {
            int ctr = 0;
            for (AuthZ authZ : list)
            {
                /*
            public class AuthZ
            {
                private String createTimestamp;
                private String creatorsName;
                private String entryCSN;
                private String entryDN;
                private String entryUUID;
                private String hasSubordinates;
                private String modifiersName;
                private String modifyTimestamp;
                private String objectClass;
                private String reqAttr;
                private String reqAttrsOnly;
                private String reqAuthzID;
                private String reqControls;
                private String reqDN;
                private String reqDerefAliases;
                private String reqEnd;
                private String reqEntries;
                private String reqFilter;
                private String reqResult;
                private String reqScope;
                private String reqSession;
                private String reqSizeLimit;
                private String reqStart;
                private String reqTimeLimit;
                private String reqType;
                private String structuralObjectClass;
                private String subschemaSubentry;
                */
                //System.out.println("**********************************");
                System.out.println("FAILED AUTHENTICATIONS AUDIT RECORD " + ctr++);
                System.out.println("***************************************");
                Date aDate = null;
                try
                {
                    aDate = TUtil.decodeGeneralizedTime(authZ.getReqEnd());
                }
                catch (ParseException pe)
                {
                    System.out.println("    Access Time    " + "ParseException=" + pe.getMessage());
                }
                if(aDate != null)
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    String formattedDate = formatter.format(aDate);
                    System.out.println("    Access Time     " + formattedDate);
                }
                System.out.println("    userId          " + AuditUtil.getAuthZId( authZ.getReqDN() ));
                System.out.println("    Success?        " + authZ.getReqEntries().equals("1"));
                System.out.println("    reqDN           " + authZ.getReqDN());
                System.out.println();
                System.out.println();
            }
        }
        else
        {
            System.out.println("AuthZ list empty");
        }
    }

    /**
     * @param list
     */
    void printAuthZs(List<AuthZ> list)
    {
        ReaderUtil.clearScreen();
        if (list != null && list.size() > 0)
        {
            int ctr = 0;
            for (AuthZ authZ : list)
            {
                /*
            public class AuthZ
            {
                private String createTimestamp;
                private String creatorsName;
                private String entryCSN;
                private String entryDN;
                private String entryUUID;
                private String hasSubordinates;
                private String modifiersName;
                private String modifyTimestamp;
                private String objectClass;
                private String reqAttr;
                private String reqAttrsOnly;
                private String reqAuthzID;
                private String reqControls;
                private String reqDN;
                private String reqDerefAliases;
                private String reqEnd;
                private String reqEntries;
                private String reqFilter;
                private String reqResult;
                private String reqScope;
                private String reqSession;
                private String reqSizeLimit;
                private String reqStart;
                private String reqTimeLimit;
                private String reqType;
                private String structuralObjectClass;
                private String subschemaSubentry;
                */

                System.out.println("AUDIT AUTHZ OBJECT [" + ctr++ + "]:");
                System.out.println("    reqAuthzID               [" + authZ.getReqAuthzID() + "]");
                System.out.println("    reqDN                    [" + authZ.getReqDN() + "]");
                System.out.println("    reqFilter                [" + authZ.getReqFilter() + "]");
                System.out.println("    reqEntries               [" + authZ.getReqEntries() + "]");
                System.out.println("    reqStart                 [" + authZ.getReqStart() + "]");
                System.out.println("    reqEnd                   [" + authZ.getReqEnd() + "]");

                System.out.println("    createTimestamp          [" + authZ.getCreateTimestamp() + "]");
                System.out.println("    creatorsName             [" + authZ.getCreatorsName() + "]");
                System.out.println("    entryCSN                 [" + authZ.getEntryCSN() + "]");
                System.out.println("    entryDN                  [" + authZ.getEntryDN() + "]");
                System.out.println("    entryUUID                [" + authZ.getEntryUUID() + "]");
                System.out.println("    hasSubordinates          [" + authZ.getHasSubordinates() + "]");
                System.out.println("    modifiersName            [" + authZ.getModifiersName() + "]");
                System.out.println("    modifyTimestamp          [" + authZ.getModifyTimestamp() + "]");
                System.out.println("    objectClass              [" + authZ.getObjectClass() + "]");
                System.out.println("    reqControls              [" + authZ.getReqControls() + "]");
                System.out.println("    reqResult                [" + authZ.getReqResult() + "]");
                System.out.println("    reqSession               [" + authZ.getReqSession() + "]");
                System.out.println("    reqType                  [" + authZ.getReqType() + "]");
                System.out.println("    structuralObjectClass    [" + authZ.getStructuralObjectClass() + "]");
                System.out.println("    reqAttr                  [" + authZ.getReqAttr() + "]");
                System.out.println("    reqAttrsOnly             [" + authZ.getReqAttrsOnly() + "]");
                System.out.println("    reqDerefAliases          [" + authZ.getReqDerefAliases() + "]");
                System.out.println("    reqScope                 [" + authZ.getReqScope() + "]");
                System.out.println("    reqSizeLimit             [" + authZ.getReqSizeLimit() + "]");
                System.out.println("    reqTimeLimit             [" + authZ.getReqTimeLimit() + "]");
            }
        }
        else
        {
            System.out.println("AuthZ list empty");
        }
    }


    /**
     * 
     */
    void getModReport()
    {
        ReaderUtil.clearScreen();
        try
        {
            UserAudit uAudit = new UserAudit();
            System.out.println("Enter userId to search Audit Mods with:");
            String val = ReaderUtil.readLn();
            if(StringUtils.isNotEmpty( val ))
            {
                uAudit.setUserId(val);
                System.out.println("size=" + val.length() + " val=" + val);

            }
            else
            {
                System.out.println("val is empty or null");

            }
            System.out.println("Check within the last n hours?  Enter number of hours or null for unlimited");
            val = ReaderUtil.readLn();
            if (val != null && val.length() > 0)
            {
                int hours = Integer.parseInt(val);
                Date date = new Date();
                long millis = date.getTime();
                millis = millis - (1000 * 60 * 60 * hours);
                Date date2 = new Date(millis);
                uAudit.setBeginDate(date2);
            }

            List<Mod> list = am.searchUserSessions(uAudit);
            printMods(list);
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("getModReport caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     * 
     * @param list
     */
    void printMods(List<Mod> list)
    {
        ReaderUtil.clearScreen();
        if (list != null && list.size() > 0)
        {
            int ctr = 0;
            for (Mod mod : list)
            {
                /*
                public class Mod
                {
                    private String reqSession;
                    private String objectClass;
                    private String reqAuthzID;
                    private String reqDN;
                    private String reqResult;
                    private String reqStart;
                    private String reqEnd;
                    private String reqType;
                    private List<String> reqMod;
                */
                System.out.println("AUDIT MOD OBJECT [" + ctr++ + "]:");
                System.out.println("    reqAuthzID               [" + mod.getReqAuthzID() + "]");
                System.out.println("    reqDN                    [" + mod.getReqDN() + "]");
                System.out.println("    reqStart                 [" + mod.getReqStart() + "]");
                System.out.println("    reqEnd                   [" + mod.getReqEnd() + "]");
                System.out.println("    objectClass              [" + mod.getObjectClass() + "]");
                System.out.println("    reqResult                [" + mod.getReqResult() + "]");
                System.out.println("    reqSession               [" + mod.getReqSession() + "]");
                System.out.println("    reqType                  [" + mod.getReqType() + "]");
                if(mod.getReqMod() != null)
                {
                    int mCtr = 0;
                    for (String mVal : mod.getReqMod())
                    {
                        System.out.println("    reqMod[" + mCtr++ + "]                [" + mVal + "]");
                    }
                }
            }
        }
        else
        {
            System.out.println("Mods list empty");
        }
    }


    void getAdminModReport()
    {
        ReaderUtil.clearScreen();
        try
        {
            UserAudit uAudit = new UserAudit();
            System.out.println("Enter userId to search Audit Mods with or NULL for skip:");
            String val = ReaderUtil.readLn();
            if(StringUtils.isNotEmpty( val ))
            {
                uAudit.setUserId(val);
                System.out.println("size=" + val.length() + " val=" + val);

            }

            System.out.println("Check within the last n hours?  Enter number of hours or null for unlimited");
            val = ReaderUtil.readLn();
            if (val != null && val.length() > 0)
            {
                int hours = Integer.parseInt(val);
                Date date = new Date();
                long millis = date.getTime();
                millis = millis - (1000 * 60 * 60 * hours);
                Date date2 = new Date(millis);
                uAudit.setBeginDate(date2);
            }

            /*
            System.out.println("Enter begin time and date - format YYYYMMDDHHMM or null for unlimited");
            val = ReaderUtil.readLn();
            if (val != null && val.length() > 0)
            {
                //int hours = Integer.parseInt(val);
                int year = new Integer(val.substring(0, 3));
                int month = new Integer(val.substring(0, 3));
                int day = new Integer(val.substring(0, 3));


                Date date = new Date();
                java.sql.Date date2 = new java.sql.Date(2011, 11, 25);
                date2.getTime();
                long millis = date.getTime();
                millis = millis - (1000 * 60 * 60 * hours);
                Date date2 = new Date(millis);
                uAudit.setBeginDate(date2);
            } */

            System.out.println("Enter admin object name to search Audit Mods with or NULL for skip:");
            val = ReaderUtil.readLn();
            if(StringUtils.isNotEmpty( val ))
            {
                uAudit.setObjName(val);
                System.out.println("size=" + val.length() + " val=" + val);
            }
            System.out.println("Enter admin operation name to search Audit Mods with or NULL for skip:");
            val = ReaderUtil.readLn();
            if(StringUtils.isNotEmpty( val ))
            {
                uAudit.setOpName(val);
                System.out.println("size=" + val.length() + " val=" + val);
            }
            List<Mod> list = am.searchAdminMods(uAudit);
            printMods(list);
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("getAdminModReport caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    void getAuthNInvalidReport()
    {
        ReaderUtil.clearScreen();
        try
        {
            UserAudit uAudit = new UserAudit();
            System.out.println("Enter userId to search Audit AuthZs with:");
            String val = ReaderUtil.readLn();
            if( StringUtils.isNotEmpty( val ))
            {
                uAudit.setUserId(val);
                System.out.println("size=" + val.length() + " val=" + val);

            }
            else
            {
                System.out.println("val is empty or null");

            }

            //uAudit.setUserId(val);
            //System.out.println("Check for failed only? (Enter 'Y' for yes or 'N' for no");
            //val = ReaderUtil.readLn();
            //if (val.equalsIgnoreCase("Y"))

            uAudit.setFailedOnly(true);
            System.out.println("Check within the last n hours?  Enter number of hours or null for unlimited");
            val = ReaderUtil.readLn();
            if (val != null && val.length() > 0)
            {
                int hours = Integer.parseInt(val);
                Date date = new Date();
                long millis = date.getTime();
                millis = millis - (1000 * 60 * 60 * hours);
                Date date2 = new Date(millis);
                uAudit.setBeginDate(date2);
            }

            List<AuthZ> list = am.searchInvalidUsers(uAudit);
            printfailedAuthNReport(list);
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("getAuthNInvalidReport caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     * Parse slapd access raw data to pull the permission name out.
     *
     * @param authZ raw data contained in Fortress audit entity.
     * @return Permission contains {@link org.apache.directory.fortress.core.model.Permission#objName} and {@link org.apache.directory.fortress.core.model.Permission#opName}
     */
    private Permission getAuthZPerm2(AuthZ authZ)
    {
        int indx = 0;
        //final int objectClass = 1;
        final int oPNm = 2;
        final int oBjNm = 3;
        final int user = 4;
        final int roles = 6;

        // reqFilter
        // <(&(objectClass=ftOperation)
        // (ftOpNm=top1_10)(ftObjNm=tob2_4)
        // (|(ftUsers=fttu3user4)
        // (ftRoles=ftt3role1)
        // (ftRoles=ftt3role2)
        // (ftRoles=ftt3role3)
        // (ftRoles=ftt3role4)
        // (ftRoles=ftt3role5)
        // (ftRoles=ftt3role6)
        // (ftRoles=ftt3role7)
        // (ftRoles=ftt3role8)
        // (ftRoles=ftt3role9)
        // (ftRoles=ftt3role10)))>

        Permission pOp = new Permission();
        if (authZ.getReqFilter() != null && authZ.getReqFilter().length() > 0)
        {
            StringTokenizer maxTkn = new StringTokenizer(authZ.getReqFilter(), "=");
            //System.out.println("maxTken size=" + maxTkn.countTokens());
            int numTokens = maxTkn.countTokens();
            for (int i = 0; i < numTokens; i++)
            {
                String val = maxTkn.nextToken();
                //System.out.println("token[" + i + "]=" + val);
                switch (i)
                {
                    //case objectClass:
                    //    indx = val.indexOf('=');
                    //    if (indx >= 1)
                    //    {
                    //        String value = val.substring(indx + 1, val.length() - 1);
                    //    }
                    //    break;

                    case oPNm:
                        indx = val.indexOf('=');
                        if (indx >= 1)
                        {
                            pOp.setOpName(val.substring(indx + 1, val.length() - 1));
                        }
                        break;

                    case oBjNm:
                        indx = val.indexOf('=');
                        if (indx >= 1)
                        {
                            pOp.setObjName( val.substring( indx + 1, val.length() - 1 ) );
                        }
                        break;

                    case user:
                        indx = val.indexOf('=');
                        if (indx >= 1)
                        {
                            pOp.setUser(val.substring(indx + 1, val.length() - 1));
                        }
                        break;

                    default:
                        int indx2 = 0;
                        if (i >= roles)
                        {
                            indx = val.indexOf('=');
                            indx2 = val.indexOf(')');
                        }
                        if (indx >= 1)
                        {
                            pOp.setRole(val.substring(indx + 1, indx2));
                        }
                        break;
                }
            }
        }
        return pOp;
    }
}