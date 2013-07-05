/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */
package us.jts.fortress.rbac.dao;


import java.util.List;

import us.jts.fortress.CreateException;
import us.jts.fortress.FinderException;
import us.jts.fortress.RemoveException;
import us.jts.fortress.UpdateException;
import us.jts.fortress.rbac.OrgUnit;
import us.jts.fortress.rbac.PermObj;
import us.jts.fortress.rbac.Permission;
import us.jts.fortress.rbac.Role;
import us.jts.fortress.rbac.Session;
import us.jts.fortress.rbac.User;


/**
 * Permission data access class for LDAP.
 * <p/>
 * This DAO class maintains the PermObj and Permission entities.
 * <h3>The Fortress PermObj Entity Class is a composite of 3 LDAP Schema object classes</h2>
 * <h4>PermObj Base - ftObject STRUCTURAL Object Class is used to store object name, id and type variables on target entity.</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass   ( 1.3.6.1.4.1.38088.2.2</code>
 * <li> <code>NAME 'ftObject'</code>
 * <li> <code>DESC 'Fortress Permission Object Class'</code>
 * <li> <code>SUP organizationalunit</code>                                              GlobalIds
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST (</code>
 * <li> <code>ftId $ ftObjNm ) </code>
 * <li> <code>MAY ( ftType ) )  </code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>PermObj - ftProperties AUXILIARY Object Class is used to store client specific name/value pairs on target entity.</h4>
 * <code>This aux object class can be used to store custom attributes.</code><br />
 * <code>The properties collections consist of name/value pairs and are not constrainted by Fortress.</code><br />
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.2</code>
 * <li> <code>NAME 'ftProperties'</code>
 * <li> <code>DESC 'Fortress Properties AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY ( ftProps ) ) </code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>PermObj - ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity.</h4>
 * <ul>
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.4</code>
 * <li> <code>NAME 'ftMods'</code>
 * <li> <code>DESC 'Fortress Modifiers AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY (</code>
 * <li> <code>ftModifier $</code>
 * <li> <code>ftModCode $</code>
 * <li> <code>ftModId ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h3>The Fortress Permission Entity Class is composite of 3 LDAP Schema object classes</h3>
 * The Permission entity extends a single OpenLDAP standard structural object class, 'organizationalRole' with
 * one extension structural class, ftOperation,  and two auxiliary object classes, ftProperties, ftMods.
 * The following 4 LDAP object classes will be mapped into this entity:
 * <h4>Permission Base - 'ftOperation' STRUCTURAL Object Class is assigned roles and/or users which grants permissions which can be later checked</h4>
 * using either 'checkAccess' or 'sessionPermissions APIs both methods that reside in the 'AccessMgrImpl' class.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass   ( 1.3.6.1.4.1.38088.2.3</code>
 * <li> <code>NAME 'ftOperation'</code>
 * <li> <code>DESC 'Fortress Permission Operation Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftPermName $</code>
 * <li> <code>ftObjNm $ ftOpNm )</code>
 * <li> <code>MAY ( ftRoles $ ftUsers $</code>
 * <li> <code> ftObjId $ ftType) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>Permission Aux - ftProperties AUXILIARY Object Class is used to store optional client or otherwise custom name/value pairs on target entity.</h4>
 * <code>This aux object class can be used to store custom attributes.</code><br />
 * <code>The properties collections consist of name/value pairs and are not constrainted by Fortress.</code><br />
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.2</code>
 * <li> <code>NAME 'ftProperties'</code>
 * <li> <code>DESC 'Fortress Properties AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY ( ftProps ) ) </code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>Permission Aux - ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity.</h4>
 * <ul>
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.4</code>
 * <li> <code>NAME 'ftMods'</code>
 * <li> <code>DESC 'Fortress Modifiers AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY (</code>
 * <li> <code>ftModifier $</code>
 * <li> <code>ftModCode $</code>
 * <li> <code>ftModId ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * This class is thread safe.
 * <p/>
 *
 * @author Emmanuel Lecharny
 */
public interface PermDAO
{
    /**
     * @param entity
     * @return
     * @throws us.jts.fortress.CreateException
     *
     */
    PermObj createObject( PermObj entity ) throws CreateException;


    /**
     * @param entity
     * @return
     * @throws us.jts.fortress.UpdateException
     *
     */
    PermObj updateObj( PermObj entity ) throws UpdateException;


    /**
     * @param entity
     * @throws us.jts.fortress.RemoveException
     *
     */
    void deleteObj( PermObj entity ) throws RemoveException;


    /**
     * @param entity
     * @return
     * @throws us.jts.fortress.CreateException
     *
     */
    Permission createOperation( Permission entity ) throws CreateException;


    /**
     * @param entity
     * @return
     * @throws us.jts.fortress.UpdateException
     *
     */
    Permission updateOperation( Permission entity ) throws UpdateException;


    /**
     * @param entity
     * @throws us.jts.fortress.RemoveException
     *
     */
    void deleteOperation( Permission entity ) throws RemoveException;


    /**
     * @param pOp
     * @param role
     * @throws us.jts.fortress.UpdateException
     *
     * @throws us.jts.fortress.FinderException
     *
     */
    void grant( Permission pOp, Role role ) throws UpdateException;


    /**
     * @param pOp
     * @param role
     * @throws us.jts.fortress.UpdateException
     *
     * @throws us.jts.fortress.FinderException
     *
     */
    void revoke( Permission pOp, Role role ) throws UpdateException, FinderException;


    /**
     * @param pOp
     * @param user
     * @throws us.jts.fortress.UpdateException
     *
     * @throws us.jts.fortress.FinderException
     *
     */
    void grant( Permission pOp, User user ) throws UpdateException;


    /**
     * @param pOp
     * @param user
     * @throws us.jts.fortress.UpdateException
     *
     * @throws us.jts.fortress.FinderException
     *
     */
    void revoke( Permission pOp, User user ) throws UpdateException, FinderException;


    /**
     * @param permission
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    Permission getPerm( Permission permission ) throws FinderException;


    /**
     * @param permObj
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    PermObj getPerm( PermObj permObj ) throws FinderException;


    /**
     * This method performs fortress authorization using data passed in (session) and stored on ldap server (permission).  It has been recently changed to use ldap compare operations in order to trigger slapd access log updates in directory.
     * It performs ldap operations:  read and (optionally) compare.  The first is to pull back the permission to see if user has access or not.  The second is to trigger audit
     * record storage on ldap server but can be disabled.
     *
     * @param session contains {@link Session#getUserId()}, for rbac check {@link us.jts.fortress.rbac.Session#getRoles()}, for arbac check: {@link us.jts.fortress.rbac.Session#getAdminRoles()}.
     * @param inPerm  must contain required attributes {@link Permission#objectName} and {@link Permission#opName}.  {@link Permission#objectId} is optional.
     * @return boolean containing result of check.
     * @throws us.jts.fortress.FinderException
     *          In the event system error occurs looking up data on ldap server.
     */
    boolean checkPermission( Session session, Permission inPerm ) throws FinderException;


    /**
     * @param permission
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    List<Permission> findPermissions( Permission permission ) throws FinderException;


    /**
     * @param permObj
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    List<PermObj> findPermissions( PermObj permObj ) throws FinderException;


    /**
     * @param ou
     * @return
     * @throws FinderException
     */
    List<PermObj> findPermissions( OrgUnit ou, boolean limitSize ) throws FinderException;


    /**
     * @param role
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    List<Permission> findPermissions( Role role ) throws FinderException;


    /**
     * @param user
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    List<Permission> findPermissions( User user ) throws FinderException;


    /**
     * @param user
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    List<Permission> findUserPermissions( User user ) throws FinderException;


    /**
     * @param session
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    List<Permission> findPermissions( Session session ) throws FinderException;
}
