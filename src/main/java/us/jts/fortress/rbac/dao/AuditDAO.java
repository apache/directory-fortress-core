/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */
package us.jts.fortress.rbac.dao;


import java.util.List;

import us.jts.fortress.FinderException;
import us.jts.fortress.rbac.AuthZ;
import us.jts.fortress.rbac.Bind;
import us.jts.fortress.rbac.Mod;
import us.jts.fortress.rbac.UserAudit;


/**
 * This class performs data access for OpenLDAP synch repl log data
 * <p/>
 * <h3>1. Binds</h3>
 * <p/>
 * The auditBind Structural object class is used to store authentication events that can later be queried via ldap API.<br />
 * <code># The Bind class includes the reqVersion attribute which contains the LDAP</code>
 * <code># protocol version specified in the Bind as well as the reqMethod attribute</code>
 * <code># which contains the Bind Method used in the Bind. This will be the string</code>
 * <code># SIMPLE for LDAP Simple Binds or SASL(mech) for SASL Binds. Note that unless</code>
 * <code># configured as a global overlay, only Simple Binds using DNs that reside in</code>
 * <code># the current database will be logged:</code>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass (  1.3.6.1.4.1.4203.666.11.5.2.6 NAME 'auditBind'</code>
 * <li> <code>DESC 'Bind operation'</code>
 * <li> <code>SUP auditObject STRUCTURAL</code>
 * <li> <code>MUST ( reqVersion $ reqMethod ) )</code>
 * <li> ------------------------------------------
 * </ul>
 * <h3>2. Authorizations</h3>
 * <code>For  the  Search class the reqScope attribute contains the scope of the</code><br />
 * <code>original search request, using the values specified for  the  LDAP  URL</code><br />
 * <code>format. I.e.  base, one, sub, or subord.  The reqDerefAliases attribute</code><br />
 * <code>is one of never, finding, searching, or always,  denoting  how  aliases</code><br />
 * <code>will  be  processed during the search.  The reqAttrsOnly attribute is a</code><br />
 * <code>Boolean value showing TRUE if only attribute names were  requested,  or</code><br />
 * <code>FALSE  if  attributes  and  their values were requested.  The reqFilter</code><br />
 * <code>attribute carries the filter used in the search request.   The  reqAttr</code><br />
 * <code>attribute  lists  the  requested attributes if specific attributes were</code><br />
 * <code>requested.  The reqEntries attribute is the integer count of  how  many</code><br />
 * <code>entries  were  returned  by  this search request.  The reqSizeLimit and</code><br />
 * <code>reqTimeLimit attributes indicate what  limits  were  requested  on  the</code><br />
 * <code>search operation.</code><br />
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass  (  1.3.6.1.4.1.4203.666.11.5.2.11</code>
 * <li> <code>NAME 'auditSearch'</code>
 * <li> <code>DESC 'Search operation'</code>
 * <li> <code>SUP auditReadObject STRUCTURAL</code>
 * <li> <code>MUST ( reqScope $ reqDerefAliases $ reqAttrsOnly )</code>
 * <li> <code>MAY ( reqFilter $ reqAttr $ reqEntries $ reqSizeLimit $</code>
 * <li> <code>reqTimeLimit ) )</code>
 * <li> ------------------------------------------
 * </ul>
 * <p/>
 * <p/>
 * <h3>3. Modifications</h3>
 * The auditModify Structural object class is used to store Fortress update and delete events that can later be queried via ldap API.<br />
 * The deletions can be recorded in this manner and associated with Fortress context because deletions will perform a modification first
 * if audit is enabled.
 * <p/>
 * <code>The Modify operation contains a description  of  modifications  in  the</code><br />
 * <code>reqMod  attribute,  which  was  already  described  above  in  the  Add</code><br />
 * <code>operation. It may optionally  contain  the  previous  contents  of  any</code><br />
 * <code>modified  attributes  in the reqOld attribute, using the same format as</code><br />
 * <code>described above for the Delete operation.  The reqOld attribute is only</code><br />
 * <code>populated  if  the  entry  being modified matches the configured logold</code><br />
 * <code>filter.</code><br />
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass (  1.3.6.1.4.1.4203.666.11.5.2.9</code>
 * <li> <code>NAME 'auditModify'</code>
 * <li> <code>DESC 'Modify operation'</code>
 * <li> <code>SUP auditWriteObject STRUCTURAL</code>
 * <li> <code>MAY reqOld MUST reqMod )</code>
 * <li> ------------------------------------------
 * </ul>
 * <p/>
 * Note this class used descriptions pulled from man pages on slapd access log.
 * <p/>
 * This class is thread safe.
 *
 * @author Shawn McKinney
 */
public interface AuditDAO
{
    /**
     * This method returns failed authentications where the userid is not present in the directory.  This
     * is possible because Fortress performs read on user before the bind.
     * User:
     * dn: reqStart=20101014235402.000000Z, cn=log
     * reqStart: 20101014235402.000000Z
     * reqEnd: 20101014235402.000001Z
     * reqAuthzID: cn=Manager,dc=jts,dc=com
     * reqDerefAliases: never
     * reqSession: 84
     * reqAttrsOnly: FALSE
     * reqSizeLimit: -1
     * objectClass: auditSearch
     * reqResult: 32
     * reqAttr: ftId
     * reqAttr: uid
     * reqAttr: userpassword
     * reqAttr: description
     * reqAttr: ou
     * reqAttr: cn
     * reqAttr: sn
     * reqAttr: ftRoleCstr
     * reqAttr: ftCstr
     * reqAttr: ftRoleAsgn
     * reqAttr: pwdReset
     * reqAttr: pwdAccountLockedTime
     * reqAttr: ftProps
     * reqEntries: 0
     * reqFilter: (|(objectClass=*)(?objectClass=ldapSubentry))
     * reqType: search
     * reqDN: uid=foo,ou=People,dc=jts,dc=com        /cal/cal2.jsp
     * reqTimeLimit: -1
     * reqScope: base
     *
     * @param audit
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    List<AuthZ> searchInvalidAuthNs( UserAudit audit ) throws FinderException;


    /**
     * @param audit
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    List<AuthZ> searchAuthZs( UserAudit audit ) throws FinderException;


    /**
     * @param audit
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    List<AuthZ> getAllAuthZs( UserAudit audit ) throws FinderException;


    /**
     * @param audit
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    List<Bind> searchBinds( UserAudit audit ) throws FinderException;


    /**
     * @param audit
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    List<Mod> searchUserMods( UserAudit audit ) throws FinderException;


    /**
     * @param audit
     * @return
     * @throws FinderException
     */
    List<Mod> searchAdminMods( UserAudit audit ) throws FinderException;
}
