package us.jts.fortress.rbac.dao;


import java.util.List;
import java.util.Set;

import us.jts.fortress.CreateException;
import us.jts.fortress.FinderException;
import us.jts.fortress.RemoveException;
import us.jts.fortress.SecurityException;
import us.jts.fortress.UpdateException;
import us.jts.fortress.rbac.OrgUnit;
import us.jts.fortress.rbac.Role;
import us.jts.fortress.rbac.Session;
import us.jts.fortress.rbac.User;
import us.jts.fortress.rbac.UserRole;


public interface UserDAO
{
    List<User> findUsers( OrgUnit ou, boolean limitSize ) throws FinderException;


    List<User> findUsers( User user ) throws FinderException;


    List<String> findUsers( User user, int limit ) throws FinderException;


    String assign( UserRole uRole ) throws UpdateException, FinderException;


    boolean changePassword( User entity, char[] newPassword ) throws SecurityException;


    Session checkPassword( User user ) throws FinderException;


    List<User> getAuthorizedUsers( Role role ) throws FinderException;


    User update( User entity ) throws UpdateException;


    void lock( User user ) throws UpdateException;


    void unlock( User user ) throws UpdateException;


    User create( User entity ) throws CreateException;


    String deassign( UserRole uRole ) throws UpdateException, FinderException;


    void resetUserPassword( User user ) throws UpdateException;


    User updateProps( User entity, boolean replace ) throws UpdateException;


    String remove( User user ) throws RemoveException;


    String deletePwPolicy( User user ) throws UpdateException;


    List<User> getAssignedUsers( Role role ) throws FinderException;


    List<String> getRoles( User user ) throws FinderException;


    User getUser( User user, boolean isRoles ) throws FinderException;


    Set<String> getAssignedUsers( Set<String> roles, String contextId ) throws FinderException;


    List<String> getAuthorizedUsers( Role role, int limit ) throws FinderException;
}