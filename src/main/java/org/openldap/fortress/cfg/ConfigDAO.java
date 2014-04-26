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

package org.openldap.fortress.cfg;


import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openldap.fortress.FinderException;
import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.RemoveException;
import org.openldap.fortress.UpdateException;
import org.openldap.fortress.ldap.UnboundIdDataProvider;
import org.openldap.fortress.util.attr.AttrHelper;
import org.openldap.fortress.util.attr.VUtil;

import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModificationSet;


/**
 * This class provides data access for the standard ldap object device that has been extended to support name/value pairs.
 * Fortress uses this data structure to store its remote cfg parameters.
 * <p/>
 * The Fortress Config node is a combination of:
 * <p/>
 * 'device' Structural Object Class is used to store basic attributes like cn which will be used for config node name.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 2.5.6.14 NAME 'device'</code>
 * <li> <code>DESC 'RFC2256: a device''</code>
 * <li> <code>SUP top STRUCTURAL</code>
 * <li> <code>MUST cn</code>
 * <li> <code>MAY ( serialNumber $ seeAlso $ owner $ ou $ o $ l $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * 'ftProperties' AUXILIARY Object Class is used to store name/value pairs on target node.<br />
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
 * <p/>
 * <p/>
 * This class is thread safe.
 *
 * @author Shawn McKinney
 */
final class ConfigDAO extends UnboundIdDataProvider

{
    private static final String CLS_NM = ConfigDAO.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static final String CONFIG_ROOT_PARAM = "config.root";
    private static final String CONFIG_ROOT_DN = Config.getProperty( CONFIG_ROOT_PARAM );
    private static final String DEVICE_OBJECT_CLASS_NM = "device";

    private static final String CONFIG_OBJ_CLASS[] =
        {
            DEVICE_OBJECT_CLASS_NM, GlobalIds.PROPS_AUX_OBJECT_CLASS_NAME
    };

    private static final String[] CONFIG_ATRS =
        {
            GlobalIds.CN, GlobalIds.PROPS
    };


    /**
     * Package private default constructor.
     */
    ConfigDAO()
    {
    }


    /**
     * @param name
     * @param props
     * @return
     * @throws org.openldap.fortress.CreateException
     */
    final Properties create( String name, Properties props )
        throws org.openldap.fortress.CreateException
    {
        LDAPConnection ld = null;
        String dn = getDn( name );
        LOG.info( "create dn [" + dn + "]" );
        try
        {
            ld = getAdminConnection();
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add( createAttributes( GlobalIds.OBJECT_CLASS, CONFIG_OBJ_CLASS ) );
            attrs.add( createAttribute( GlobalIds.CN, name ) );
            loadProperties( props, attrs, GlobalIds.PROPS );
            LDAPEntry myEntry = new LDAPEntry( dn, attrs );
            add( ld, myEntry );
        }
        catch ( LDAPException e )
        {
            String error;
            if ( e.getLDAPResultCode() == LDAPException.ENTRY_ALREADY_EXISTS )
            {
                String warning = "create config dn [" + dn + "] caught LDAPException="
                    + e.getLDAPResultCode() + " msg=" + e.getMessage();
                throw new org.openldap.fortress.CreateException( GlobalErrIds.FT_CONFIG_ALREADY_EXISTS, warning );
            }
            else
            {
                error = "create config dn [" + dn + "] caught LDAPException=" + e.getLDAPResultCode()
                    + " msg=" + e.getMessage();
            }
            LOG.error( error, e );
            throw new org.openldap.fortress.CreateException( GlobalErrIds.FT_CONFIG_CREATE_FAILED, error );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return props;
    }


    /**
     * @param name
     * @param props
     * @return
     * @throws org.openldap.fortress.UpdateException
     */
    final Properties update( String name, Properties props )
        throws org.openldap.fortress.UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn( name );
        LOG.info( "update dn [" + dn + "]" );
        try
        {
            ld = getAdminConnection();
            LDAPModificationSet mods = new LDAPModificationSet();
            if ( org.openldap.fortress.util.attr.VUtil.isNotNullOrEmpty( props ) )
            {
                loadProperties( props, mods, GlobalIds.PROPS, true );
            }
            if ( mods.size() > 0 )
            {
                modify( ld, dn, mods );
            }
        }
        catch ( LDAPException e )
        {
            String error = "update dn [" + dn + "] caught LDAPException=" + e.getLDAPResultCode() + " msg="
                + e.getMessage();
            throw new org.openldap.fortress.UpdateException( GlobalErrIds.FT_CONFIG_UPDATE_FAILED, error );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return props;
    }


    /**
     * @param name
     * @throws org.openldap.fortress.RemoveException
     */
    final void remove( String name )
        throws RemoveException
    {
        LDAPConnection ld = null;
        String dn = getDn( name );
        LOG.info( "remove dn [" + dn + "]" );
        try
        {
            ld = getAdminConnection();
            delete( ld, dn );
        }
        catch ( LDAPException e )
        {
            String error = "remove dn [" + dn + "] LDAPException=" + e.getLDAPResultCode() + " msg="
                + e.getMessage();
            throw new org.openldap.fortress.RemoveException( GlobalErrIds.FT_CONFIG_DELETE_FAILED, error );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param name
     * @param props
     * @return
     * @throws org.openldap.fortress.UpdateException
     */
    final Properties remove( String name, Properties props )
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn( name );
        LOG.info( "remove props dn [" + dn + "]" );
        try
        {
            ld = getAdminConnection();
            LDAPModificationSet mods = new LDAPModificationSet();
            if ( VUtil.isNotNullOrEmpty( props ) )
            {
                removeProperties( props, mods, GlobalIds.PROPS );
            }
            if ( mods.size() > 0 )
            {
                modify( ld, dn, mods );
            }
        }
        catch ( LDAPException e )
        {
            String error = "remove props dn [" + dn + "] caught LDAPException=" + e.getLDAPResultCode()
                + " msg=" + e.getMessage();
            throw new org.openldap.fortress.UpdateException( GlobalErrIds.FT_CONFIG_DELETE_PROPS_FAILED, error );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return props;
    }


    /**
     * @param name
     * @return
     * @throws org.openldap.fortress.FinderException
     */
    final Properties getConfig( String name )
        throws FinderException
    {
        Properties props = null;
        LDAPConnection ld = null;
        String dn = getDn( name );
        LOG.info( "getConfig dn [" + dn + "]" );
        try
        {
            ld = getAdminConnection();
            LDAPEntry findEntry = read( ld, dn, CONFIG_ATRS );
            props = AttrHelper.getProperties( getAttributes( findEntry, GlobalIds.PROPS ) );
        }
        catch ( LDAPException e )
        {
            if ( e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT )
            {
                String warning = "getConfig COULD NOT FIND ENTRY for dn [" + dn + "]";
                throw new org.openldap.fortress.FinderException( GlobalErrIds.FT_CONFIG_NOT_FOUND, warning );
            }
            String error = "getConfig dn [" + dn + "] LEXCD=" + e.getLDAPResultCode() + " LEXMSG=" + e;
            throw new FinderException( GlobalErrIds.FT_CONFIG_READ_FAILED, error );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return props;
    }


    /**
     *
     * @param name
     * @return
     */
    private String getDn( String name )
    {
        return GlobalIds.CN + "=" + name + "," + CONFIG_ROOT_DN;
    }
}
