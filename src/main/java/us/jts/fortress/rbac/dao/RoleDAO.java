package us.jts.fortress.rbac.dao;


import java.util.List;

import us.jts.fortress.CreateException;
import us.jts.fortress.FinderException;
import us.jts.fortress.RemoveException;
import us.jts.fortress.UpdateException;
import us.jts.fortress.rbac.Graphable;
import us.jts.fortress.rbac.Role;


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