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

package org.openldap.fortress.ldap.group;


import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttribute;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModification;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModificationSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPSearchResults;
import org.openldap.fortress.FinderException;
import org.openldap.fortress.ObjectFactory;
import org.openldap.fortress.UpdateException;
import org.openldap.fortress.cfg.Config;
import org.openldap.fortress.util.attr.AttrHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openldap.fortress.CreateException;
import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.RemoveException;
import org.openldap.fortress.ldap.UnboundIdDataProvider;

import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import org.openldap.fortress.util.attr.VUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the Group node for LDAP Directory Information Tree.
 * This class is thread safe.
 *
 * @author Shawn McKinney
 */
final class GroupDAO extends UnboundIdDataProvider
{
    private static final String CLS_NM = GroupDAO.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static final String GROUP_OBJECT_CLASS = "group.objectclass";
    private static final String GROUP_OBJECT_CLASS_IMPL = Config.getProperty( GROUP_OBJECT_CLASS );
    private static final String GROUP_PROTOCOL_ATTR = "group.protocol";
    private static final String GROUP_PROTOCOL_ATTR_IMPL = Config.getProperty( GROUP_PROTOCOL_ATTR );
    private static final String GROUP_PROPERTY_ATTR = "group.properties";
    private static final String GROUP_PROPERTY_ATTR_IMPL = Config.getProperty( GROUP_PROPERTY_ATTR );
    private static final String GROUP_OBJ_CLASS[] = {GlobalIds.TOP, GROUP_OBJECT_CLASS_IMPL};
    private static final String MEMBER = "member";
    private static final String[] GROUP_ATRS = {GlobalIds.CN, GlobalIds.DESC, GROUP_PROTOCOL_ATTR_IMPL, GROUP_PROPERTY_ATTR_IMPL, MEMBER};

    /**
     * Package private default constructor.
     */
    GroupDAO()
    {
    }

    /**
     * @param group
     * @throws org.openldap.fortress.CreateException
     *
     */
    final Group create( Group group ) throws org.openldap.fortress.CreateException
    {
        LDAPConnection ld = null;
        String nodeDn = getDn( group.getName(), group.getContextId() );
        try
        {
            LOG.debug( "create group dn {[]}", nodeDn );
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add( createAttributes( GlobalIds.OBJECT_CLASS, GROUP_OBJ_CLASS ) );
            attrs.add( createAttribute( GlobalIds.CN, group.getName() ) );
            attrs.add( createAttribute( GROUP_PROTOCOL_ATTR_IMPL, group.getProtocol() ) );
            loadAttrs( group.getMembers(), attrs, MEMBER );
            loadProperties( group.getProperties(), attrs, GROUP_PROPERTY_ATTR_IMPL, '=' );
            if ( VUtil.isNotNullOrEmpty( group.getDescription() ) )
            {
                attrs.add( createAttribute( GlobalIds.DESC, group.getDescription() ) );
            }

            LDAPEntry myEntry = new LDAPEntry( nodeDn, attrs );
            ld = getAdminConnection();
            add( ld, myEntry );
        }
        catch ( LDAPException e )
        {
            String error = "create group node dn [" + nodeDn + "] caught LDAPException=" + e.getLDAPResultCode() + " " +
                "msg=" + e.getMessage();
            throw new CreateException( GlobalErrIds.GROUP_ADD_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return group;
    }

    /**
     * @param group
     * @return
     * @throws org.openldap.fortress.CreateException
     *
     */
    final Group update( Group group ) throws org.openldap.fortress.UpdateException
    {
        LDAPConnection ld = null;
        String nodeDn = getDn( group.getName(), group.getContextId() );
        try
        {
            LOG.debug( "update group dn {[]}", nodeDn );
            LDAPModificationSet mods = new LDAPModificationSet();
            if ( VUtil.isNotNullOrEmpty( group.getDescription() ) )
            {
                LDAPAttribute desc = new LDAPAttribute( GlobalIds.DESC, group.getDescription() );
                mods.add( LDAPModification.REPLACE, desc );
            }
            if ( VUtil.isNotNullOrEmpty( group.getProtocol() ) )
            {
                LDAPAttribute protocol = new LDAPAttribute( GROUP_PROTOCOL_ATTR_IMPL, group.getProtocol() );
                mods.add( LDAPModification.REPLACE, protocol );
            }
            loadAttrs( group.getMembers(), mods, MEMBER, false );
            if ( VUtil.isNotNullOrEmpty( group.getProperties() ) )
            {
                loadProperties( group.getProperties(), mods, GROUP_PROPERTY_ATTR_IMPL, '=', false );
            }
            if ( mods.size() > 0 )
            {
                ld = getAdminConnection();
                modify( ld, nodeDn, mods, group );
            }
        }
        catch ( LDAPException e )
        {
            String error = "update group node dn [" + nodeDn + "] caught LDAPException=" + e.getLDAPResultCode() + " " +
                "msg=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.GROUP_UPDATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return group;
    }

    final Group add( Group group, String key, String value ) throws org.openldap.fortress.CreateException
    {
        LDAPConnection ld = null;
        String nodeDn = getDn( group.getName(), group.getContextId() );
        try
        {
            LOG.debug( "add group property dn {[]}, key {[]}, value {[]}", nodeDn, key, value );
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute prop = new LDAPAttribute( GROUP_PROPERTY_ATTR_IMPL, key + "=" + value );
            mods.add( LDAPModification.ADD, prop );
            ld = getAdminConnection();
            modify( ld, nodeDn, mods, group );
        }
        catch ( LDAPException e )
        {
            String error = "update group property node dn [" + nodeDn + "] caught LDAPException=" + e.getLDAPResultCode() + " " +
                "msg=" + e.getMessage();
            throw new CreateException( GlobalErrIds.GROUP_ADD_PROPERTY_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return group;
    }

    final Group delete( Group group, String key, String value ) throws org.openldap.fortress.RemoveException
    {
        LDAPConnection ld = null;
        String nodeDn = getDn( group.getName(), group.getContextId() );
        try
        {
            LOG.debug( "delete group property dn {[]}, key {[]}, value {[]}", nodeDn, key, value );
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute prop = new LDAPAttribute( GROUP_PROPERTY_ATTR_IMPL, key + "=" + value );
            mods.add( LDAPModification.DELETE, prop );
            ld = getAdminConnection();
            modify( ld, nodeDn, mods, group );
        }
        catch ( LDAPException e )
        {
            String error = "delete group property node dn [" + nodeDn + "] caught LDAPException=" + e.getLDAPResultCode() + " " +
                "msg=" + e.getMessage();
            throw new RemoveException( GlobalErrIds.GROUP_DELETE_PROPERTY_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return group;
    }

    /**
     * This method will remove group node from diretory.
     *
     * @param group
     * @throws org.openldap.fortress.RemoveException
     *
     */
    final Group remove( Group group ) throws org.openldap.fortress.RemoveException
    {
        LDAPConnection ld = null;
        String nodeDn = getDn( group.getName(), group.getContextId() );
        LOG.debug( "remove group dn {[]}", nodeDn );
        try
        {
            ld = getAdminConnection();
            deleteRecursive( ld, nodeDn );
        }
        catch ( LDAPException e )
        {
            String error = "remove group node dn [" + nodeDn + "] caught LDAPException=" + e.getLDAPResultCode() + " " +
                "msg=" + e.getMessage();
            throw new RemoveException( GlobalErrIds.GROUP_DELETE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return group;
    }

    /**
     * @param entity
     * @param userDn
     * @return
     * @throws org.openldap.fortress.UpdateException
     *
     */
    final Group assign( Group entity, String userDn ) throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn( entity.getName(), entity.getContextId() );
        LOG.debug( "assign group property dn {[]}, member dn {[]}", dn, userDn );
        try
        {
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute member = new LDAPAttribute( MEMBER, userDn );
            mods.add( LDAPModification.ADD, member );
            ld = getAdminConnection();
            modify( ld, dn, mods, entity );
        }
        catch ( LDAPException e )
        {
            String error = "assign group name [" + entity.getName() + "] user dn [" + userDn + "] caught " +
                "LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.GROUP_USER_ASSIGN_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return entity;
    }

    /**
     * @param entity
     * @param userDn
     * @return
     * @throws org.openldap.fortress.UpdateException
     *
     */
    final Group deassign( Group entity, String userDn ) throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn( entity.getName(), entity.getContextId() );
        LOG.debug( "deassign group property dn {[]}, member dn {[]}", dn, userDn );
        try
        {
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute member = new LDAPAttribute( MEMBER, userDn );
            mods.add( LDAPModification.DELETE, member );
            ld = getAdminConnection();
            modify( ld, dn, mods, entity );
        }
        catch ( LDAPException e )
        {
            String error = "deassign group name [" + entity.getName() + "] user dn [" + userDn + "] caught " +
                "LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.GROUP_USER_DEASSIGN_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return entity;
    }

    /**
     * @param group
     * @return
     * @throws org.openldap.fortress.FinderException
     *
     */
    final Group get( Group group ) throws FinderException
    {
        Group entity = null;
        LDAPConnection ld = null;
        String dn = getDn( group.getName(), group.getContextId() );
        try
        {
            ld = getAdminConnection();
            LDAPEntry findEntry = read( ld, dn, GROUP_ATRS );
            entity = unloadLdapEntry( findEntry, 0 );
            if ( entity == null )
            {
                String warning = "read no entry found dn [" + dn + "]";
                throw new FinderException( GlobalErrIds.GROUP_NOT_FOUND, warning );
            }
        }
        catch ( LDAPException e )
        {
            if ( e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT )
            {
                String warning = "read Obj COULD NOT FIND ENTRY for dn [" + dn + "]";
                throw new FinderException( GlobalErrIds.GROUP_NOT_FOUND, warning );
            }
            String error = "read dn [" + dn + "] LEXCD=" + e.getLDAPResultCode() + " LEXMSG=" + e;
            throw new FinderException( GlobalErrIds.GROUP_READ_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return entity;
    }

    /**
     * @param group
     * @return
     * @throws org.openldap.fortress.FinderException
     *
     */
    final List<Group> find( Group group ) throws FinderException
    {
        List<Group> groupList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String groupRoot = getRootDn( group.getContextId(), GlobalIds.GROUP_ROOT );
        String filter = null;
        try
        {
            String searchVal = encodeSafeText( group.getName(), GlobalIds.ROLE_LEN );
            filter = GlobalIds.FILTER_PREFIX + GROUP_OBJECT_CLASS_IMPL + ")(" + GlobalIds.CN + "=" + searchVal + "*))";
            ld = getAdminConnection();
            searchResults = search( ld, groupRoot, LDAPConnection.SCOPE_ONE, filter, GROUP_ATRS, false,
                GlobalIds.BATCH_SIZE );
            long sequence = 0;
            while ( searchResults.hasMoreElements() )
            {
                groupList.add( unloadLdapEntry( searchResults.next(), sequence++ ) );
            }
        }
        catch ( LDAPException e )
        {
            String error = "find filter [" + filter + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e
                .getMessage();
            throw new FinderException( GlobalErrIds.GROUP_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return groupList;
    }

    /**
     * @param le
     * @param sequence
     * @return
     * @throws LDAPException
     */
    private Group unloadLdapEntry( LDAPEntry le, long sequence )
    {
        Group entity = new ObjectFactory().createGroup();
        entity.setName( getAttribute( le, GlobalIds.CN ) );
        entity.setDescription( getAttribute( le, GlobalIds.DESC ) );
        entity.setProtocol( getAttribute( le, GROUP_PROTOCOL_ATTR_IMPL ) );
        entity.setMembers( getAttributes( le, MEMBER ) );
        entity.addProperties( AttrHelper.getProperties( getAttributes( le, GROUP_PROPERTY_ATTR_IMPL ), '=' ) );
        entity.setSequenceId( sequence );
        return entity;
    }

    private String getDn( String name, String contextId )
    {
        return GlobalIds.CN + "=" + name + "," + getRootDn( contextId, GlobalIds.GROUP_ROOT );
    }
}