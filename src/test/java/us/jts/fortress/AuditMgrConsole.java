/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

/*
 *  This class is used for testing purposes.
 */
package us.jts.fortress;

import us.jts.fortress.rbac.Bind;
import us.jts.fortress.rbac.AuthZ;
import us.jts.fortress.rbac.Mod;
import us.jts.fortress.rbac.TestUtils;
import us.jts.fortress.rbac.UserAudit;
import us.jts.fortress.rbac.Permission;
import us.jts.fortress.util.attr.AttrHelper;
import us.jts.fortress.util.attr.VUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        catch (SecurityException e)
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
                System.out.println("    UserId        " + AttrHelper.getAuthZId(aBind.getReqDN()));
                Date aDate = null;
                try
                {
                    aDate = AttrHelper.decodeGeneralizedTime(aBind.getReqEnd());
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
            if(VUtil.isNotNullOrEmpty(val))
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
            if(VUtil.isNotNullOrEmpty(val))
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
            if(VUtil.isNotNullOrEmpty(val))
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
                    aDate = AttrHelper.decodeGeneralizedTime(authZ.getReqEnd());
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

                System.out.println("    userId          " + AttrHelper.getAuthZId(authZ.getReqAuthzID()));
                Permission pOp = AttrHelper.getAuthZPerm(authZ);
                System.out.println("    Resource Name   " + pOp.getObjectName());
                System.out.println("    Operation       " + pOp.getOpName());
                // TODO: fix the NPE that happens here:
                //System.out.println("    Success?        " + authZ.getReqEntries().equals("1"));
                int rCtr = 0;
                if (pOp.getRoles() != null)
                {
                    for (String role : pOp.getRoles())
                    {
                        System.out.println("    Role[" + rCtr++ + "]         " + role);
                    }
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
                    aDate = AttrHelper.decodeGeneralizedTime(authZ.getReqEnd());
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
                System.out.println("    userId          " + AttrHelper.getAuthZId(authZ.getReqDN()));
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
            if(VUtil.isNotNullOrEmpty(val))
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
            if(VUtil.isNotNullOrEmpty(val))
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
            if(VUtil.isNotNullOrEmpty(val))
            {
                uAudit.setObjName(val);
                System.out.println("size=" + val.length() + " val=" + val);
            }
            System.out.println("Enter admin operation name to search Audit Mods with or NULL for skip:");
            val = ReaderUtil.readLn();
            if(VUtil.isNotNullOrEmpty(val))
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
            if(VUtil.isNotNullOrEmpty(val))
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

}