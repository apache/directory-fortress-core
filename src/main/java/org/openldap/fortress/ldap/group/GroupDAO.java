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
package org.openldap.fortress.ldap.group;


import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.SearchCursor;
import org.apache.directory.api.ldap.model.entry.DefaultEntry;
import org.apache.directory.api.ldap.model.entry.DefaultModification;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.entry.Modification;
import org.apache.directory.api.ldap.model.entry.ModificationOperation;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.exception.LdapInvalidAttributeValueException;
import org.apache.directory.api.ldap.model.exception.LdapNoSuchObjectException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.openldap.fortress.FinderException;
import org.openldap.fortress.ObjectFactory;
import org.openldap.fortress.UpdateException;
import org.openldap.fortress.cfg.Config;
import org.openldap.fortress.ldap.ApacheDsDataProvider;
import org.openldap.fortress.rbac.User;
import org.openldap.fortress.util.attr.AttrHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openldap.fortress.CreateException;
import org.openldap.fortress.GlobalErrIds;
import org.openldap.fortress.GlobalIds;
import org.openldap.fortress.RemoveException;
import org.openldap.fortress.util.attr.VUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the Group node for LDAP Directory Information Tree.
 * This class is thread safe.
 *
 * @author Shawn McKinney
 */
final class GroupDAO extends ApacheDsDataProvider
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
        LdapConnection ld = null;
        String nodeDn = getDn( group.getName(), group.getContextId() );
        try
        {
            LOG.debug( "create group dn {[]}", nodeDn );
            Entry myEntry = new DefaultEntry( nodeDn );
            myEntry.add( GlobalIds.OBJECT_CLASS, GROUP_OBJ_CLASS );
            myEntry.add( GlobalIds.CN , group.getName() );
            myEntry.add( GROUP_PROTOCOL_ATTR_IMPL, group.getProtocol() );
            loadAttrs( group.getMembers(), myEntry, MEMBER );
            loadProperties( group.getProperties(), myEntry, GROUP_PROPERTY_ATTR_IMPL, '=' );
            if ( VUtil.isNotNullOrEmpty( group.getDescription() ) )
            {
                myEntry.add( GlobalIds.DESC, group.getDescription() );
            }
            ld = getAdminConnection();
            add( ld, myEntry );
        }
        catch ( LdapException e )
        {
            String error = "create group node dn [" + nodeDn + "] caught LDAPException=" + e.getMessage();
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
    final Group update( Group group ) throws org.openldap.fortress.FinderException, org.openldap.fortress.UpdateException
    {
        LdapConnection ld = null;
        String nodeDn = getDn( group.getName(), group.getContextId() );
        try
        {
            LOG.debug( "update group dn {[]}", nodeDn );
            List<Modification> mods = new ArrayList<Modification>();
            if ( VUtil.isNotNullOrEmpty( group.getDescription() ) )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE, GlobalIds.DESC, group.getDescription() ) );
            }
            if ( VUtil.isNotNullOrEmpty( group.getProtocol() ) )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE, GROUP_PROTOCOL_ATTR_IMPL, group.getProtocol() ) );
            }
            loadAttrs( group.getMembers(), mods, MEMBER );
            loadProperties( group.getProperties(), mods, GROUP_PROPERTY_ATTR_IMPL, true, '=' );
            if ( mods.size() > 0 )
            {
                ld = getAdminConnection();
                modify( ld, nodeDn, mods, group );
            }
        }
        catch ( LdapException e )
        {
            String error = "update group node dn [" + nodeDn + "] caught LDAPException=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.GROUP_UPDATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return get( group );
    }

    final Group add( Group group, String key, String value ) throws org.openldap.fortress.FinderException, org.openldap.fortress.CreateException
    {
        LdapConnection ld = null;
        String nodeDn = getDn( group.getName(), group.getContextId() );
        try
        {
            LOG.debug( "add group property dn {[]}, key {[]}, value {[]}", nodeDn, key, value );
            List<Modification> mods = new ArrayList<Modification>();
            mods.add( new DefaultModification(
                ModificationOperation.ADD_ATTRIBUTE, GROUP_PROPERTY_ATTR_IMPL, key + "=" + value ) );
            ld = getAdminConnection();
            modify( ld, nodeDn, mods, group );
        }
        catch ( LdapException e )
        {
            String error = "update group property node dn [" + nodeDn + "] caught LDAPException=" + e.getMessage();
            throw new CreateException( GlobalErrIds.GROUP_ADD_PROPERTY_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return get( group );
    }

    final Group delete( Group group, String key, String value ) throws org.openldap.fortress.FinderException, org.openldap.fortress.RemoveException
    {
        LdapConnection ld = null;
        String nodeDn = getDn( group.getName(), group.getContextId() );
        try
        {
            LOG.debug( "delete group property dn {[]}, key {[]}, value {[]}", nodeDn, key, value );
            List<Modification> mods = new ArrayList<Modification>();
            mods.add( new DefaultModification(
                ModificationOperation.REMOVE_ATTRIBUTE, GROUP_PROPERTY_ATTR_IMPL, key + "=" + value ) );
            ld = getAdminConnection();
            modify( ld, nodeDn, mods, group );
        }
        catch ( LdapException e )
        {
            String error = "delete group property node dn [" + nodeDn + "] caught LDAPException=" + e.getMessage();
            throw new RemoveException( GlobalErrIds.GROUP_DELETE_PROPERTY_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return get( group );
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
        LdapConnection ld = null;
        String nodeDn = getDn( group.getName(), group.getContextId() );
        LOG.debug( "remove group dn {[]}", nodeDn );
        try
        {
            ld = getAdminConnection();
            deleteRecursive( ld, nodeDn );
        }
        catch ( CursorException e )
        {
            String error = "remove group node dn [" + nodeDn + "] caught CursorException="
                + e.getMessage();
            throw new org.openldap.fortress.RemoveException( GlobalErrIds.GROUP_DELETE_FAILED, error, e );
        }
        catch ( LdapException e )
        {
            String error = "remove group node dn [" + nodeDn + "] caught LDAPException=" + e.getMessage();
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
    final Group assign( Group entity, String userDn ) throws org.openldap.fortress.FinderException, UpdateException
    {
        LdapConnection ld = null;
        String dn = getDn( entity.getName(), entity.getContextId() );
        LOG.debug( "assign group property dn {[]}, member dn {[]}", dn, userDn );
        try
        {
            List<Modification> mods = new ArrayList<Modification>();
            mods.add( new DefaultModification(
                ModificationOperation.ADD_ATTRIBUTE, MEMBER, userDn ) );
            ld = getAdminConnection();
            modify( ld, dn, mods, entity );
        }
        catch ( LdapException e )
        {
            String error = "assign group name [" + entity.getName() + "] user dn [" + userDn + "] caught " +
                "LDAPException=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.GROUP_USER_ASSIGN_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return get( entity );
    }

    /**
     * @param entity
     * @param userDn
     * @return
     * @throws org.openldap.fortress.UpdateException
     *
     */
    final Group deassign( Group entity, String userDn ) throws org.openldap.fortress.FinderException, UpdateException
    {
        LdapConnection ld = null;
        String dn = getDn( entity.getName(), entity.getContextId() );
        LOG.debug( "deassign group property dn {[]}, member dn {[]}", dn, userDn );
        try
        {
            List<Modification> mods = new ArrayList<Modification>();
            mods.add( new DefaultModification(
                ModificationOperation.REMOVE_ATTRIBUTE, MEMBER, userDn ) );

            ld = getAdminConnection();
            modify( ld, dn, mods, entity );
        }
        catch ( LdapException e )
        {
            String error = "deassign group name [" + entity.getName() + "] user dn [" + userDn + "] caught " +
                "LDAPException=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.GROUP_USER_DEASSIGN_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return get( entity );
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
        LdapConnection ld = null;
        String dn = getDn( group.getName(), group.getContextId() );
        try
        {
            ld = getAdminConnection();
            Entry findEntry = read( ld, dn, GROUP_ATRS );
            entity = unloadLdapEntry( findEntry, 0 );
            if ( entity == null )
            {
                String warning = "read no entry found dn [" + dn + "]";
                throw new FinderException( GlobalErrIds.GROUP_NOT_FOUND, warning );
            }
        }
        catch ( LdapNoSuchObjectException e )
        {
            String warning = "read Obj COULD NOT FIND ENTRY for dn [" + dn + "]";
            throw new FinderException( GlobalErrIds.GROUP_NOT_FOUND, warning );
        }
        catch ( LdapException e )
        {
            String error = "read dn [" + dn + "] LdapException=" + e.getMessage();
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
        LdapConnection ld = null;
        SearchCursor searchResults;
        String groupRoot = getRootDn( group.getContextId(), GlobalIds.GROUP_ROOT );
        String filter = null;
        try
        {
            String searchVal = encodeSafeText( group.getName(), GlobalIds.ROLE_LEN );
            filter = GlobalIds.FILTER_PREFIX + GROUP_OBJECT_CLASS_IMPL + ")(" + GlobalIds.CN + "=" + searchVal + "*))";
            ld = getAdminConnection();
            searchResults = search( ld, groupRoot, SearchScope.ONELEVEL, filter, GROUP_ATRS, false,
                GlobalIds.BATCH_SIZE );
            long sequence = 0;
            while ( searchResults.next() )
            {
                groupList.add( unloadLdapEntry( searchResults.getEntry(), sequence++ ) );
            }
        }
        catch ( CursorException e )
        {
            String error = "find filter [" + filter + "] caught CursorException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.GROUP_SEARCH_FAILED, error, e );
        }
        catch ( LdapException e )
        {
            String error = "find filter [" + filter + "] caught LDAPException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.GROUP_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return groupList;
    }

    /**
     * @param user
     * @return
     * @throws org.openldap.fortress.FinderException
     *
     */
    final List<Group> find( User user ) throws FinderException
    {
        List<Group> groupList = new ArrayList<>();
        LdapConnection ld = null;
        SearchCursor searchResults;
        String groupRoot = getRootDn( user.getContextId(), GlobalIds.GROUP_ROOT );
        String filter = null;
        try
        {
            String searchVal = encodeSafeText( user.getUserId(), GlobalIds.USERID_LEN );
            filter = GlobalIds.FILTER_PREFIX + GROUP_OBJECT_CLASS_IMPL + ")(" + MEMBER + "=" + user.getDn() + "))";
            ld = getAdminConnection();
            searchResults = search( ld, groupRoot, SearchScope.ONELEVEL, filter, GROUP_ATRS, false,
                GlobalIds.BATCH_SIZE );
            long sequence = 0;
            while ( searchResults.next() )
            {
                groupList.add( unloadLdapEntry( searchResults.getEntry(), sequence++ ) );
            }
        }
        catch ( CursorException e )
        {
            String error = "find filter [" + filter + "] caught CursorException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.GROUP_SEARCH_FAILED, error, e );
        }
        catch ( LdapException e )
        {
            String error = "find filter [" + filter + "] caught LDAPException=" + e.getMessage();
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
     * @throws LdapException
     */
    private Group unloadLdapEntry( Entry le, long sequence )
        throws LdapInvalidAttributeValueException
    {
        Group entity = new ObjectFactory().createGroup();
        entity.setName( getAttribute( le, GlobalIds.CN ) );
        entity.setDescription( getAttribute( le, GlobalIds.DESC ) );
        entity.setProtocol( getAttribute( le, GROUP_PROTOCOL_ATTR_IMPL ) );
        entity.setMembers( getAttributes( le, MEMBER ) );
        entity.setMemberDn( true );
        entity.setProperties( AttrHelper.getProperties( getAttributes( le, GROUP_PROPERTY_ATTR_IMPL ), '=' ) );
        entity.setSequenceId( sequence );
        return entity;
    }

    private String getDn( String name, String contextId )
    {
        return GlobalIds.CN + "=" + name + "," + getRootDn( contextId, GlobalIds.GROUP_ROOT );
    }
}