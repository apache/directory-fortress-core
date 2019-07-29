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
package org.apache.directory.fortress.core.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.directory.api.ldap.model.constants.SchemaConstants;
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
import org.apache.directory.fortress.core.CreateException;
import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.RemoveException;
import org.apache.directory.fortress.core.UpdateException;
import org.apache.directory.fortress.core.ldap.LdapDataProvider;
import org.apache.directory.fortress.core.model.ObjectFactory;
import org.apache.directory.fortress.core.model.PwPolicy;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.ldap.client.api.LdapConnection;


/**
 * This DAO class maintains the LDAP Password Policy entity which is a composite of the following structural and aux object classes:
 * <h4>1. organizationalRole Structural Object Class is used to store basic attributes like cn and description</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code> objectclass ( 2.5.6.14 NAME 'device'</code>
 * <li> <code>DESC 'RFC2256: a device'</code>
 * <li> <code>SUP top STRUCTURAL</code>
 * <li> <code>MUST cn</code>
 * <li> <code>MAY ( serialNumber $ seeAlso $ owner $ ou $ o $ l $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>2. pwdPolicy AUXILIARY Object Class is used to store OpenLDAP Password Policies</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.42.2.27.8.2.1</code>
 * <li> <code>NAME 'pwdPolicy'</code>
 * <li> <code>SUP top</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MUST ( pwdAttribute )</code>
 * <li> <code>MAY ( pwdMinAge $ pwdMaxAge $ pwdInHistory $ pwdCheckQuality $</code>
 * <li> <code>pwdMinLength $ pwdExpireWarning $ pwdGraceAuthNLimit $ pwdLockout $</code>
 * <li> <code>pwdLockoutDuration $ pwdMaxFailure $ pwdFailureCountInterval $</code>
 * <li> <code>pwdMustChange $ pwdAllowUserChange $ pwdSafeModify ) )</code>
 * <li> <code></code>
 * <li> <code></code>
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
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
final class PolicyDAO extends LdapDataProvider
{
    /*
      *  *************************************************************************
      *  **  OPENLDAP PW POLICY ATTRIBUTES AND CONSTANTS
      *  ************************************************************************
      */
    private static final String OLPW_POLICY_EXTENSION = "2.5.4.35";
    private static final String PW_POLICY_EXTENSION = ( Config.getInstance().isOpenldap() ) ? OLPW_POLICY_EXTENSION : "userPassword";
    private static final String ADS_BASE_CLASS = "ads-base";
    private static final String PW_POLICY_CLASS = ( Config.getInstance().isOpenldap() ) ? "pwdPolicy" : "ads-passwordPolicy";

    /**
     * This object class combines PW Policy schema with the Fortress audit context.
     */
    private static String PWPOLICY_OBJ_CLASS[] = (Config.getInstance().isOpenldap())
        ? new String[]
        {
            SchemaConstants.TOP_OC,
            SchemaConstants.DEVICE_OC,
            PW_POLICY_CLASS,
            GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
        }
        : new String[]
        {
            SchemaConstants.TOP_OC,
            ADS_BASE_CLASS,
            PW_POLICY_CLASS,
            GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
        };

    private static final String PW_PWD_ID = (Config.getInstance().isOpenldap()) ? "cn" : "ads-pwdid";
    private static final String PW_MIN_AGE = (Config.getInstance().isOpenldap()) ? "pwdMinAge" : "ads-pwdMinAge";
    private static final String PW_MAX_AGE = (Config.getInstance().isOpenldap()) ? "pwdMaxAge" : "ads-pwdMaxAge";
    private static final String PW_IN_HISTORY = (Config.getInstance().isOpenldap()) ? "pwdInHistory" : "ads-pwdInHistory";
    private static final String PW_CHECK_QUALITY = (Config.getInstance().isOpenldap()) ? "pwdCheckQuality" : "ads-pwdCheckQuality";
    private static final String PW_MIN_LENGTH = (Config.getInstance().isOpenldap()) ? "pwdMinLength" : "ads-pwdMinLength";
    private static final String PW_EXPIRE_WARNING = (Config.getInstance().isOpenldap()) ? "pwdExpireWarning" : "ads-pwdExpireWarning";
    private static final String PW_GRACE_LOGIN_LIMIT = (Config.getInstance().isOpenldap()) ? "pwdGraceAuthNLimit" : "ads-pwdGraceAuthNLimit";
    private static final String PW_LOCKOUT = (Config.getInstance().isOpenldap()) ? "pwdLockout" : "ads-pwdLockout";
    private static final String PW_LOCKOUT_DURATION = (Config.getInstance().isOpenldap()) ? "pwdLockoutDuration" : "ads-pwdLockoutDuration";
    private static final String PW_MAX_FAILURE = (Config.getInstance().isOpenldap()) ? "pwdMaxFailure" : "ads-pwdMaxFailure";
    private static final String PW_FAILURE_COUNT_INTERVAL = (Config.getInstance().isOpenldap()) ? "pwdFailureCountInterval" : "ads-pwdFailureCountInterval";
    private static final String PW_MUST_CHANGE = (Config.getInstance().isOpenldap()) ? "pwdMustChange" : "ads-pwdMustChange";
    private static final String PW_ALLOW_USER_CHANGE = (Config.getInstance().isOpenldap()) ? "pwdAllowUserChange" : "ads-pwdAllowUserChange";
    private static final String PW_SAFE_MODIFY = (Config.getInstance().isOpenldap()) ? "pwdSafeModify" : "ads-pwdSafeModify";
    private static final String PW_ATTRIBUTE = (Config.getInstance().isOpenldap()) ? "pwdAttribute" : "ads-pwdAttribute";

    private static final String[] PASSWORD_POLICY_ATRS =
        {
            PW_PWD_ID, PW_MIN_AGE, PW_MAX_AGE, PW_IN_HISTORY, PW_CHECK_QUALITY,
            PW_MIN_LENGTH, PW_EXPIRE_WARNING, PW_GRACE_LOGIN_LIMIT, PW_LOCKOUT,
            PW_LOCKOUT_DURATION, PW_MAX_FAILURE, PW_FAILURE_COUNT_INTERVAL,
            PW_MUST_CHANGE, PW_ALLOW_USER_CHANGE, PW_SAFE_MODIFY
        };

    private static final String[] PASSWORD_POLICY_NAME_ATR = { PW_PWD_ID };

    /**
     * @param entity
     * @return
     * @throws org.apache.directory.fortress.core.CreateException
     *
     */
    PwPolicy create( PwPolicy entity )
        throws CreateException
    {
        LdapConnection ld = null;
        String dn = getDn( entity );

        try
        {
            Entry entry = new DefaultEntry( dn );

            entry.add( SchemaConstants.OBJECT_CLASS_AT, PWPOLICY_OBJ_CLASS );
            entry.add( PW_PWD_ID, entity.getName() );
            entry.add( PW_ATTRIBUTE, PW_POLICY_EXTENSION );

            if ( entity.getMinAge() != null )
            {
                entry.add( PW_MIN_AGE, entity.getMinAge().toString() );
            }

            if ( entity.getMaxAge() != null )
            {
                entry.add( PW_MAX_AGE, entity.getMaxAge().toString() );
            }

            if ( entity.getInHistory() != null )
            {
                entry.add( PW_IN_HISTORY, entity.getInHistory().toString() );
            }

            if ( entity.getCheckQuality() != null )
            {
                entry.add( PW_CHECK_QUALITY, entity.getCheckQuality().toString() );
            }

            if ( entity.getMinLength() != null )
            {
                entry.add( PW_MIN_LENGTH, entity.getMinLength().toString() );
            }

            if ( entity.getExpireWarning() != null )
            {
                entry.add( PW_EXPIRE_WARNING, entity.getExpireWarning().toString() );
            }

            if ( entity.getGraceLoginLimit() != null )
            {
                entry.add( PW_GRACE_LOGIN_LIMIT, entity.getGraceLoginLimit().toString() );
            }

            if ( entity.getLockout() != null )
            {
                /**
                 * OpenLDAP requires the pwdLockout boolean value to be upper case:
                 */
                entry.add( PW_LOCKOUT, entity.getLockout().toString().toUpperCase() );
            }

            if ( entity.getLockoutDuration() != null )
            {
                entry.add( PW_LOCKOUT_DURATION, entity.getLockoutDuration().toString() );
            }

            if ( entity.getMaxFailure() != null )
            {
                entry.add( PW_MAX_FAILURE, entity.getMaxFailure().toString() );
            }

            if ( entity.getFailureCountInterval() != null )
            {
                entry.add( PW_FAILURE_COUNT_INTERVAL, entity.getFailureCountInterval().toString() );
            }

            if ( entity.getMustChange() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                entry.add( PW_MUST_CHANGE, entity.getMustChange().toString().toUpperCase() );
            }

            if ( entity.getAllowUserChange() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                entry.add( PW_ALLOW_USER_CHANGE, entity.getAllowUserChange().toString()
                        .toUpperCase() );
            }

            if ( entity.getSafeModify() != null )
            {
                entry.add( PW_SAFE_MODIFY, entity.getSafeModify().toString().toUpperCase() );
            }

            ld = getAdminConnection();
            add( ld, entry, entity );
        }
        catch ( LdapException e )
        {
            String error = "create name [" + entity.getName() + "] caught LdapException=" + e;
            throw new CreateException( GlobalErrIds.PSWD_CREATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return entity;
    }


    /**
     * @param entity
     * @throws org.apache.directory.fortress.core.UpdateException
     *
     */
    void update( PwPolicy entity ) throws UpdateException
    {
        LdapConnection ld = null;
        String dn = getDn( entity );

        try
        {
            List<Modification> mods = new ArrayList<Modification>();

            if ( entity.getMinAge() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    PW_MIN_AGE, entity.getMinAge().toString() ) );
            }

            if ( entity.getMaxAge() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    PW_MAX_AGE, entity.getMaxAge().toString() ) );
            }

            if ( entity.getInHistory() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    PW_IN_HISTORY, entity.getInHistory().toString() ) );
            }

            if ( entity.getCheckQuality() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    PW_CHECK_QUALITY, entity.getCheckQuality().toString() ) );
            }

            if ( entity.getMinLength() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    PW_MIN_LENGTH, entity.getMinLength().toString() ) );
            }

            if ( entity.getExpireWarning() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    PW_EXPIRE_WARNING, entity.getExpireWarning().toString() ) );
            }

            if ( entity.getGraceLoginLimit() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    PW_GRACE_LOGIN_LIMIT, entity.getGraceLoginLimit().toString() ) );
            }

            if ( entity.getLockout() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    PW_LOCKOUT, entity.getLockout().toString().toUpperCase() ) );
            }

            if ( entity.getLockoutDuration() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    PW_LOCKOUT_DURATION, entity.getLockoutDuration().toString() ) );
            }

            if ( entity.getMaxFailure() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    PW_MAX_FAILURE, entity.getMaxFailure().toString() ) );
            }

            if ( entity.getFailureCountInterval() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    PW_FAILURE_COUNT_INTERVAL, entity.getFailureCountInterval().toString() ) );
            }

            if ( entity.getMustChange() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    PW_MUST_CHANGE, entity.getMustChange().toString().toUpperCase() ) );
            }

            if ( entity.getAllowUserChange() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    PW_ALLOW_USER_CHANGE, entity.getAllowUserChange().toString().toUpperCase() ) );
            }

            if ( entity.getSafeModify() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    PW_SAFE_MODIFY, entity.getSafeModify().toString().toUpperCase() ) );
            }

            if ( mods != null && mods.size() > 0 )
            {
                ld = getAdminConnection();
                modify( ld, dn, mods, entity );
            }
        }
        catch ( LdapException e )
        {
            String error = "update name [" + entity.getName() + "] caught LdapException=" + e;
            throw new UpdateException( GlobalErrIds.PSWD_UPDATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param entity
     * @throws org.apache.directory.fortress.core.RemoveException
     */
    void remove( PwPolicy entity ) throws RemoveException
    {
        LdapConnection ld = null;
        String dn = getDn( entity );

        try
        {
            ld = getAdminConnection();
            delete( ld, dn, entity );
        }
        catch ( LdapException e )
        {
            String error = "remove name [" + entity.getName() + "] caught LdapException=" + e;
            throw new RemoveException( GlobalErrIds.PSWD_DELETE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param policy
     * @return
     * @throws org.apache.directory.fortress.core.FinderException
     *
     */
    PwPolicy getPolicy( PwPolicy policy ) throws FinderException
    {
        PwPolicy entity = null;
        LdapConnection ld = null;
        String dn = getDn( policy );

        try
        {
            ld = getAdminConnection();
            Entry findEntry = read( ld, dn, PASSWORD_POLICY_ATRS );
            entity = unloadLdapEntry( findEntry, 0 );
        }
        catch ( LdapNoSuchObjectException e )
        {
            String warning = "getPolicy Obj COULD NOT FIND ENTRY for dn [" + dn + "]";
            throw new FinderException( GlobalErrIds.PSWD_NOT_FOUND, warning );
        }
        catch ( LdapException e )
        {
            String error = "getPolicy name [" + policy.getName() + "] caught LdapException="
                + e;
            throw new FinderException( GlobalErrIds.PSWD_READ_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return entity;
    }


    /**
     *
     * @param le
     * @param sequence
     * @return
     * @throws LdapInvalidAttributeValueException 
     * @throws LdapException
     */
    private PwPolicy unloadLdapEntry( Entry le, long sequence ) throws LdapInvalidAttributeValueException
    {
        PwPolicy entity = new ObjectFactory().createPswdPolicy();
        entity.setSequenceId( sequence );

        entity.setName( getAttribute( le, PW_PWD_ID ) );
        String val;

        val = getAttribute( le, PW_MIN_AGE );
        if ( StringUtils.isNotEmpty( val ) )
        {
            entity.setMinAge( Integer.valueOf( val ) );
        }

        val = getAttribute( le, PW_MAX_AGE );
        if ( StringUtils.isNotEmpty( val ) )
        {
            entity.setMaxAge( Long.valueOf( val ) );
        }

        val = getAttribute( le, PW_IN_HISTORY );
        if ( StringUtils.isNotEmpty( val ) )
        {
            entity.setInHistory( Short.valueOf( val ) );
        }

        val = getAttribute( le, PW_CHECK_QUALITY );
        if ( StringUtils.isNotEmpty( val ) )
        {
            entity.setCheckQuality( Short.valueOf( val ) );
        }

        val = getAttribute( le, PW_MIN_LENGTH );
        if ( StringUtils.isNotEmpty( val ) )
        {
            entity.setMinLength( Short.valueOf( val ) );
        }

        val = getAttribute( le, PW_EXPIRE_WARNING );
        if ( StringUtils.isNotEmpty( val ) )
        {
            entity.setExpireWarning( Long.valueOf( val ) );
        }

        val = getAttribute( le, PW_GRACE_LOGIN_LIMIT );
        if ( StringUtils.isNotEmpty( val ) )
        {
            entity.setGraceLoginLimit( Short.valueOf( val ) );
        }

        val = getAttribute( le, PW_LOCKOUT );
        if ( StringUtils.isNotEmpty( val ) )
        {
            entity.setLockout( Boolean.valueOf( val ) );
        }

        val = getAttribute( le, PW_LOCKOUT_DURATION );
        if ( StringUtils.isNotEmpty( val ) )
        {
            entity.setLockoutDuration( Integer.valueOf( val ) );
        }

        val = getAttribute( le, PW_MAX_FAILURE );
        if ( StringUtils.isNotEmpty( val ) )
        {
            entity.setMaxFailure( Short.valueOf( val ) );
        }

        val = getAttribute( le, PW_FAILURE_COUNT_INTERVAL );
        if ( StringUtils.isNotEmpty( val ) )
        {
           entity.setFailureCountInterval( Short.valueOf( val ) );
        }

        val = getAttribute( le, PW_MUST_CHANGE );
        if ( StringUtils.isNotEmpty( val ) )
        {
            //noinspection BooleanConstructorCall
            entity.setMustChange( Boolean.valueOf( val ) );
        }

        val = getAttribute( le, PW_ALLOW_USER_CHANGE );
        if ( StringUtils.isNotEmpty( val ) )
        {
            entity.setAllowUserChange( Boolean.valueOf( val ) );
        }

        val = getAttribute( le, PW_SAFE_MODIFY );

        if ( StringUtils.isNotEmpty( val ) )
        {
            entity.setSafeModify( Boolean.valueOf( val ) );
        }

        return entity;
    }


    /**
     * @param policy
     * @return
     * @throws org.apache.directory.fortress.core.FinderException
     *
     */
    List<PwPolicy> findPolicy( PwPolicy policy ) throws FinderException
    {
        List<PwPolicy> policyArrayList = new ArrayList<>();
        LdapConnection ld = null;
        String policyRoot = getPolicyRoot( policy.getContextId() );
        String searchVal = null;

        try
        {
            searchVal = encodeSafeText( policy.getName(), GlobalIds.PWPOLICY_NAME_LEN );
            String szFilter = GlobalIds.FILTER_PREFIX + PW_POLICY_CLASS + ")(" + PW_PWD_ID + "=" + searchVal + "*))";
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, policyRoot,
                SearchScope.ONELEVEL, szFilter, PASSWORD_POLICY_ATRS, false, Config.getInstance().getInt(GlobalIds.CONFIG_LDAP_MAX_BATCH_SIZE, GlobalIds.BATCH_SIZE ) );
            long sequence = 0;

            while ( searchResults.next() )
            {
                policyArrayList.add( unloadLdapEntry( searchResults.getEntry(), sequence++ ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "findPolicy name [" + searchVal + "] caught LdapException=" + e;
            throw new FinderException( GlobalErrIds.PSWD_SEARCH_FAILED, error, e );
        }
        catch ( CursorException e )
        {
            String error = "findPolicy name [" + searchVal + "] caught CursorException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PSWD_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return policyArrayList;
    }


    /**
     * @return
     * @throws FinderException
     */
    Set<String> getPolicies( String contextId )
        throws FinderException
    {
        Set<String> policySet = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
        LdapConnection ld = null;
        String policyRoot = getPolicyRoot( contextId );

        try
        {
            String szFilter = "(objectclass=" + PW_POLICY_CLASS + ")";
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, policyRoot,
                SearchScope.ONELEVEL, szFilter, PASSWORD_POLICY_NAME_ATR, false, Config.getInstance().getInt(GlobalIds.CONFIG_LDAP_MAX_BATCH_SIZE, GlobalIds.BATCH_SIZE ) );

            while ( searchResults.next() )
            {
                Entry entry = searchResults.getEntry();
                policySet.add( getAttribute( entry, PW_PWD_ID ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "getPolicies caught LdapException=" + e;
            throw new FinderException( GlobalErrIds.PSWD_SEARCH_FAILED, error, e );
        }
        catch ( CursorException e )
        {
            String error = "getPolicies caught LdapException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PSWD_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return policySet;
    }

    private String getDn( PwPolicy policy )
    {
        return PW_PWD_ID + "=" + policy.getName() + "," + getPolicyRoot( policy.getContextId() );
    }

    // Allow callers from package {@link UserDAO} access to this method to resolve the pwdAttribute dn.
    static String getPolicyDn( User user )
    {
        return PW_PWD_ID + "=" + user.getPwPolicy() + "," + getPolicyRoot( user.getContextId() );
    }

    private static String getPolicyRoot( String contextId )
    {
        String szDn;

        // ApacheDS requires pw policy objects to be stored under ou=config node;
        if ( Config.getInstance().isApacheds() )
            szDn = getRootDn( contextId, GlobalIds.ADS_PPOLICY_ROOT );
        else
            szDn = getRootDn( contextId, GlobalIds.PPOLICY_ROOT );
        return szDn;
    }
}