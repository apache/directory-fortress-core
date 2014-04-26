/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
 */
package org.openldap.fortress.rbac.dao;


import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.cfg.Config;


/**
 * A factory that creates DAO for either the UnboundID or the Apache Ldap API lib
 * @author elecharny
 */
public class DaoFactory
{

    /**
     * Create an instance of a AdminRoleDAO which depends of the used Backend
     * 
     * @return The created instance
     */
    public static AdminRoleDAO createAdminRoleDAO()
    {
        String daoConnector = Config.getProperty( GlobalIds.DAO_CONNECTOR );

        DAOType daoType = DAOType.UNBOUNDID_API;

        if ( ( daoConnector != null ) && ( daoConnector.equalsIgnoreCase( GlobalIds.APACHE_LDAP_API ) ) )
        {
            daoType = DAOType.APACHE_LDAP_API;
        }

        switch ( daoType )
        {
            case UNBOUNDID_API:
                return new org.openldap.fortress.rbac.dao.unboundid.AdminRoleDAO();

            case APACHE_LDAP_API:
                return new org.openldap.fortress.rbac.dao.apache.AdminRoleDAO();

            default:
                return null;
        }
    }


    /**
     * Create an instance of a AuditDAO which depends of the used Backend
     * 
     * @return The created instance
     */
    public static AuditDAO createAuditDAO()
    {
        String daoConnector = Config.getProperty( GlobalIds.DAO_CONNECTOR );

        DAOType daoType = DAOType.UNBOUNDID_API;

        if ( ( daoConnector != null ) && ( daoConnector.equalsIgnoreCase( GlobalIds.APACHE_LDAP_API ) ) )
        {
            daoType = DAOType.APACHE_LDAP_API;
        }

        switch ( daoType )
        {
            case UNBOUNDID_API:
                return new org.openldap.fortress.rbac.dao.unboundid.AuditDAO();

            case APACHE_LDAP_API:
                return new org.openldap.fortress.rbac.dao.apache.AuditDAO();

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

        if ( ( daoConnector != null ) && ( daoConnector.equalsIgnoreCase( GlobalIds.APACHE_LDAP_API ) ) )
        {
            daoType = DAOType.APACHE_LDAP_API;
        }

        switch ( daoType )
        {
            case UNBOUNDID_API:
                return new org.openldap.fortress.rbac.dao.unboundid.OrgUnitDAO();

            case APACHE_LDAP_API:
                return new org.openldap.fortress.rbac.dao.apache.OrgUnitDAO();

            default:
                return null;
        }
    }


    /**
     * Create an instance of a PermDAO which depends of the used Backend
     * 
     * @return The created instance
     */
    public static PermDAO createPermDAO()
    {
        String daoConnector = Config.getProperty( GlobalIds.DAO_CONNECTOR );

        DAOType daoType = DAOType.UNBOUNDID_API;

        if ( ( daoConnector != null ) && ( daoConnector.equalsIgnoreCase( GlobalIds.APACHE_LDAP_API ) ) )
        {
            daoType = DAOType.APACHE_LDAP_API;
        }

        switch ( daoType )
        {
            case UNBOUNDID_API:
                return new org.openldap.fortress.rbac.dao.unboundid.PermDAO();

            case APACHE_LDAP_API:
                return new org.openldap.fortress.rbac.dao.apache.PermDAO();

            default:
                return null;
        }
    }


    /**
     * Create an instance of a PolicyDAO which depends of the used Backend
     * 
     * @return The created instance
     */
    public static PolicyDAO createPolicyDAO()
    {
        String daoConnector = Config.getProperty( GlobalIds.DAO_CONNECTOR );

        DAOType daoType = DAOType.UNBOUNDID_API;

        if ( ( daoConnector != null ) && ( daoConnector.equalsIgnoreCase( GlobalIds.APACHE_LDAP_API ) ) )
        {
            daoType = DAOType.APACHE_LDAP_API;
        }

        switch ( daoType )
        {
            case UNBOUNDID_API:
                return new org.openldap.fortress.rbac.dao.unboundid.PolicyDAO();

            case APACHE_LDAP_API:
                return new org.openldap.fortress.rbac.dao.apache.PolicyDAO();

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

        if ( ( daoConnector != null ) && ( daoConnector.equalsIgnoreCase( GlobalIds.APACHE_LDAP_API ) ) )
        {
            daoType = DAOType.APACHE_LDAP_API;
        }

        switch ( daoType )
        {
            case UNBOUNDID_API:
                return new org.openldap.fortress.rbac.dao.unboundid.RoleDAO();

            case APACHE_LDAP_API:
                return new org.openldap.fortress.rbac.dao.apache.RoleDAO();

            default:
                return null;
        }
    }


    /**
     * Create an instance of a SdDAO which depends of the used Backend
     * 
     * @return The created instance
     */
    public static SdDAO createSdDAO()
    {
        String daoConnector = Config.getProperty( GlobalIds.DAO_CONNECTOR );

        DAOType daoType = DAOType.UNBOUNDID_API;

        if ( ( daoConnector != null ) && ( daoConnector.equalsIgnoreCase( GlobalIds.APACHE_LDAP_API ) ) )
        {
            daoType = DAOType.APACHE_LDAP_API;
        }

        switch ( daoType )
        {
            case UNBOUNDID_API:
                return new org.openldap.fortress.rbac.dao.unboundid.SdDAO();

            case APACHE_LDAP_API:
                return new org.openldap.fortress.rbac.dao.apache.SdDAO();

            default:
                return null;
        }
    }


    /**
     * Create an instance of a UserDAO which depends of the used Backend
     * 
     * @return The created instance
     */
    public static UserDAO createUserDAO()
    {
        String daoConnector = Config.getProperty( GlobalIds.DAO_CONNECTOR );

        DAOType daoType = DAOType.UNBOUNDID_API;

        if ( ( daoConnector != null ) && ( daoConnector.equalsIgnoreCase( GlobalIds.APACHE_LDAP_API ) ) )
        {
            daoType = DAOType.APACHE_LDAP_API;
        }

        switch ( daoType )
        {
            case UNBOUNDID_API:
                return new org.openldap.fortress.rbac.dao.unboundid.UserDAO();

            case APACHE_LDAP_API:
                return new org.openldap.fortress.rbac.dao.apache.UserDAO();

            default:
                return null;
        }
    }
}
