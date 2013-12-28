/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */
package us.jts.fortress.rbac.dao;


import java.util.List;

import us.jts.fortress.CreateException;
import us.jts.fortress.FinderException;
import us.jts.fortress.RemoveException;
import us.jts.fortress.UpdateException;
import us.jts.fortress.rbac.Graphable;
import us.jts.fortress.rbac.Role;


/**
 * This class perform data access for Fortress Role entity.
 * <p/>
 * The Fortress Role entity is a composite of the following other Fortress structural and aux object classes:
 * <h4>1. ftRls Structural objectclass is used to store the Role information like name and temporal constraint attributes</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass   ( 1.3.6.1.4.1.38088.2.1</code>
 * <li> <code>NAME 'ftRls'</code>
 * <li> <code>DESC 'Fortress Role Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftRoleName )</code>
 * <li> <code>MAY ( description $ ftCstr ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>2. ftProperties AUXILIARY Object Class is used to store client specific name/value pairs on target entity</h4>
 * <code># This aux object class can be used to store custom attributes.</code><br />
 * <code># The properties collections consist of name/value pairs and are not constrainted by Fortress.</code><br />
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.2</code>
 * <li> <code>NAME 'ftProperties'</code>
 * <li> <code>DESC 'Fortress Properties AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MAY ( ftProps ) ) </code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>3. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity</h4>
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
 * <p/>
 * This class is thread safe.
 *
 * @author Emmanuel Lecharny
 */
public interface RoleDAO
{
    /**
     * @param entity
     * @return
     * @throws CreateException
     */
    Role create( Role entity ) throws CreateException;


    /**
     * @param entity
     * @return
     * @throws us.jts.fortress.UpdateException
     *
     */
    Role update( Role entity ) throws UpdateException;


    /**
     *
     * @param entity
     * @throws UpdateException
     */
    void deleteParent( Role entity ) throws UpdateException;


    /**
     * @param entity
     * @param userDn
     * @return
     * @throws us.jts.fortress.UpdateException
     *
     */
    Role assign( Role entity, String userDn ) throws UpdateException;


    /**
     * @param entity
     * @param userDn
     * @return
     * @throws us.jts.fortress.UpdateException
     *
     */
    Role deassign( Role entity, String userDn ) throws UpdateException;


    /**
     * @param role
     * @throws RemoveException
     */
    void remove( Role role ) throws RemoveException;


    /**
     * @param role
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    Role getRole( Role role ) throws FinderException;


    /**
     * @param role
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    List<Role> findRoles( Role role ) throws FinderException;


    /**
     * @param role
     * @param limit
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    List<String> findRoles( Role role, int limit ) throws FinderException;


    /**
     *
     * @param userDn
     * @param contextId
     * @return
     * @throws FinderException
     */
    List<String> findAssignedRoles( String userDn, String contextId ) throws FinderException;


    /**
     *
     * @param contextId
     * @return
     * @throws FinderException
     */
    List<Graphable> getAllDescendants( String contextId ) throws FinderException;
}