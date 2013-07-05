/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */
package us.jts.fortress.rbac.dao;


import us.jts.fortress.GlobalIds;
import us.jts.fortress.cfg.Config;
import us.jts.fortress.rbac.dao.apache.ApacheAdminRoleDAO;
import us.jts.fortress.rbac.dao.apache.ApacheOrgUnitDAO;
import us.jts.fortress.rbac.dao.apache.ApacheRoleDAO;
import us.jts.fortress.rbac.dao.apache.ApacheUserDAO;
import us.jts.fortress.rbac.dao.unboundid.UnboundIdAdminRoleDAO;
import us.jts.fortress.rbac.dao.unboundid.UnboundIdOrgUnitDAO;
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


    /**
     * Create an instance of a AdminRoleDAO which depends of the used Backend
     * 
     * @return The created instance
     */
    public static AdminRoleDAO createAdminRoleDAO()
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
                return new UnboundIdAdminRoleDAO();

            case APACHE_LDAP_API:
                return new ApacheAdminRoleDAO();

            default:
                return null;
        }
    }


    /**
     * Create an instance of a OrgUnitDAO which depends of the used Backend
     * 
     * @return The created instance
     */
    public static OrgUnitDAO createOrgUnitDAO()
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
                return new UnboundIdOrgUnitDAO();

            case APACHE_LDAP_API:
                return new ApacheOrgUnitDAO();

            default:
                return null;
        }
    }
}
