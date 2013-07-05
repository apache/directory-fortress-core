/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */
package us.jts.fortress.rbac.dao;


import us.jts.fortress.GlobalIds;
import us.jts.fortress.cfg.Config;
import us.jts.fortress.rbac.dao.apache.ApacheRoleDAO;
import us.jts.fortress.rbac.dao.apache.ApacheUserDAO;
import us.jts.fortress.rbac.dao.unboundid.UnboundIdRoleDAO;
import us.jts.fortress.rbac.dao.unboundid.UnboundIdUserDAO;


/**
 * A factory that creates DAO for either the UnboundID or the Apache Ldap API lib
 * @author elecharny
 */
public class DaoFactory
{
    /**
     * Create an instance of a UserDAO which depends of the used Backend
     * 
     * @return The created instance
     */
    public static UserDAO createUserDAO()
    {
        String daoConnector = Config.getProperty( GlobalIds.DAO_CONNECTOR );

        DAOType daoType = DAOType.UNBOUNDID_API;

        if ( ( daoConnector != null ) && ( daoConnector.equalsIgnoreCase( "APACHE_LDAP_API" ) ) )
        {
            daoType = DAOType.APACHE_LDAP_API;
        }

        switch ( daoType )
        {
            case UNBOUNDID_API:
                return new UnboundIdUserDAO();

            case APACHE_LDAP_API:
                return new ApacheUserDAO();

            default:
                return null;
        }
    }


    /**
     * Create an instance of a RoleDAO which depends of the used Backend
     * 
     * @return The created instance
     */
    public static RoleDAO createRoleDAO()
    {
        String daoConnector = Config.getProperty( GlobalIds.DAO_CONNECTOR );

        DAOType daoType = DAOType.UNBOUNDID_API;

        if ( ( daoConnector != null ) && ( daoConnector.equalsIgnoreCase( "APACHE_LDAP_API" ) ) )
        {
            daoType = DAOType.APACHE_LDAP_API;
        }

        switch ( daoType )
        {
            case UNBOUNDID_API:
                return new UnboundIdRoleDAO();

            case APACHE_LDAP_API:
                return new ApacheRoleDAO();

            default:
                return null;
        }
    }
}
