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
package org.apache.directory.fortress.core.rbac.dao;


import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.cfg.Config;


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
                return new org.apache.directory.fortress.core.rbac.dao.unboundid.AdminRoleDAO();

            case APACHE_LDAP_API:
                return new org.apache.directory.fortress.core.rbac.dao.apache.AdminRoleDAO();

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
                return new org.apache.directory.fortress.core.rbac.dao.unboundid.AuditDAO();

            case APACHE_LDAP_API:
                return new org.apache.directory.fortress.core.rbac.dao.apache.AuditDAO();

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
                return new org.apache.directory.fortress.core.rbac.dao.unboundid.OrgUnitDAO();

            case APACHE_LDAP_API:
                return new org.apache.directory.fortress.core.rbac.dao.apache.OrgUnitDAO();

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
                return new org.apache.directory.fortress.core.rbac.dao.unboundid.PermDAO();

            case APACHE_LDAP_API:
                return new org.apache.directory.fortress.core.rbac.dao.apache.PermDAO();

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
                return new org.apache.directory.fortress.core.rbac.dao.unboundid.PolicyDAO();

            case APACHE_LDAP_API:
                return new org.apache.directory.fortress.core.rbac.dao.apache.PolicyDAO();

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
                return new org.apache.directory.fortress.core.rbac.dao.unboundid.RoleDAO();

            case APACHE_LDAP_API:
                return new org.apache.directory.fortress.core.rbac.dao.apache.RoleDAO();

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
                return new org.apache.directory.fortress.core.rbac.dao.unboundid.SdDAO();

            case APACHE_LDAP_API:
                return new org.apache.directory.fortress.core.rbac.dao.apache.SdDAO();

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
                return new org.apache.directory.fortress.core.rbac.dao.unboundid.UserDAO();

            case APACHE_LDAP_API:
                return new org.apache.directory.fortress.core.rbac.dao.apache.UserDAO();

            default:
                return null;
        }
    }
}
