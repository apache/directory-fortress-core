/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac.dao.apache;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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

import us.jts.fortress.CreateException;
import us.jts.fortress.FinderException;
import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.GlobalIds;
import us.jts.fortress.ObjectFactory;
import us.jts.fortress.RemoveException;
import us.jts.fortress.UpdateException;
import us.jts.fortress.ldap.apacheds.ApacheDsDataProvider;
import us.jts.fortress.rbac.PwPolicy;
import us.jts.fortress.util.attr.VUtil;


/**
 * This DAO class maintains the OpenLDAP Password Policy entity which is a composite of the following structural and aux object classes:
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
 * <p/>
 * This class is thread safe.
 *
 * @author Shawn McKinney
 */
public final class PolicyDAO extends ApacheDsDataProvider implements us.jts.fortress.rbac.dao.PolicyDAO
{
    /*
      *  *************************************************************************
      *  **  OPENLDAP PW POLICY ATTRIBUTES AND CONSTANTS
      *  ************************************************************************
      */
    private static final String CLS_NM = PolicyDAO.class.getName();
    private static final String OLPW_POLICY_EXTENSION = "2.5.4.35";
    private static final String OLPW_POLICY_CLASS = "pwdPolicy";
    /**
     * This object class combines OpenLDAP PW Policy schema with the Fortress audit context.
     */
    private static final String OAM_PWPOLICY_OBJ_CLASS[] =
        {
            GlobalIds.TOP, "device", OLPW_POLICY_CLASS, GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };

    private static final String OLPW_ATTRIBUTE = "pwdAttribute";
    private static final String OLPW_MIN_AGE = "pwdMinAge";
    private static final String OLPW_MAX_AGE = "pwdMaxAge";
    private static final String OLPW_IN_HISTORY = "pwdInHistory";
    private static final String OLPW_CHECK_QUALITY = "pwdCheckQuality";
    private static final String OLPW_MIN_LENGTH = "pwdMinLength";
    private static final String OLPW_EXPIRE_WARNING = "pwdExpireWarning";
    private static final String OLPW_GRACE_LOGIN_LIMIT = "pwdGraceAuthNLimit";
    private static final String OLPW_LOCKOUT = "pwdLockout";
    private static final String OLPW_LOCKOUT_DURATION = "pwdLockoutDuration";
    private static final String OLPW_MAX_FAILURE = "pwdMaxFailure";
    private static final String OLPW_FAILURE_COUNT_INTERVAL = "pwdFailureCountInterval";
    private static final String OLPW_MUST_CHANGE = "pwdMustChange";
    private static final String OLPW_ALLOW_USER_CHANGE = "pwdAllowUserChange";
    private static final String OLPW_SAFE_MODIFY = "pwdSafeModify";
    private static final String[] PASSWORD_POLICY_ATRS =
        {
            OLPW_MIN_AGE, OLPW_MAX_AGE, OLPW_IN_HISTORY, OLPW_CHECK_QUALITY,
            OLPW_MIN_LENGTH, OLPW_EXPIRE_WARNING, OLPW_GRACE_LOGIN_LIMIT, OLPW_LOCKOUT,
            OLPW_LOCKOUT_DURATION, OLPW_MAX_FAILURE, OLPW_FAILURE_COUNT_INTERVAL,
            OLPW_MUST_CHANGE, OLPW_ALLOW_USER_CHANGE, OLPW_SAFE_MODIFY,
    };

    private static final String[] PASSWORD_POLICY_NAME_ATR =
        {
            GlobalIds.CN
    };


    /**
     * Package private default constructor.
     */
    public PolicyDAO()
    {
    }


    /**
     * @param entity
     * @return
     * @throws us.jts.fortress.CreateException
     *
     */
    public final PwPolicy create( PwPolicy entity )
        throws CreateException
    {
        LdapConnection ld = null;
        String dn = getDn( entity );

        try
        {
            Entry attrs = new DefaultEntry( dn );
            attrs.add( GlobalIds.OBJECT_CLASS, OAM_PWPOLICY_OBJ_CLASS );
            attrs.add( GlobalIds.CN, entity.getName() );
            attrs.add( OLPW_ATTRIBUTE, OLPW_POLICY_EXTENSION );

            if ( entity.getMinAge() != null )
            {
                attrs.add( OLPW_MIN_AGE, entity.getMinAge().toString() );
            }

            if ( entity.getMaxAge() != null )
            {
                attrs.add( OLPW_MAX_AGE, entity.getMaxAge().toString() );
            }

            if ( entity.getInHistory() != null )
            {
                attrs.add( OLPW_IN_HISTORY, entity.getInHistory().toString() );
            }

            if ( entity.getCheckQuality() != null )
            {
                attrs.add( OLPW_CHECK_QUALITY, entity.getCheckQuality().toString() );
            }

            if ( entity.getMinLength() != null )
            {
                attrs.add( OLPW_MIN_LENGTH, entity.getMinLength().toString() );
            }

            if ( entity.getExpireWarning() != null )
            {
                attrs.add( OLPW_EXPIRE_WARNING, entity.getExpireWarning().toString() );
            }

            if ( entity.getGraceLoginLimit() != null )
            {
                attrs.add( OLPW_GRACE_LOGIN_LIMIT, entity.getGraceLoginLimit().toString() );
            }

            if ( entity.getLockout() != null )
            {
                /**
                 * For some reason OpenLDAP requires the pwdLockout boolean value to be upper case:
                 */
                attrs.add( OLPW_LOCKOUT, entity.getLockout().toString().toUpperCase() );
            }

            if ( entity.getLockoutDuration() != null )
            {
                attrs.add( OLPW_LOCKOUT_DURATION, entity.getLockoutDuration().toString() );
            }

            if ( entity.getMaxFailure() != null )
            {
                attrs.add( OLPW_MAX_FAILURE, entity.getMaxFailure().toString() );
            }

            if ( entity.getFailureCountInterval() != null )
            {
                attrs.add( OLPW_FAILURE_COUNT_INTERVAL, entity.getFailureCountInterval().toString() );
            }

            if ( entity.getMustChange() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                attrs.add( OLPW_MUST_CHANGE, entity.getMustChange().toString().toUpperCase() );
            }

            if ( entity.getAllowUserChange() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                attrs.add( OLPW_ALLOW_USER_CHANGE, entity.getAllowUserChange().toString()
                    .toUpperCase() );
            }

            if ( entity.getSafeModify() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                attrs.add( OLPW_SAFE_MODIFY, entity.getSafeModify().toString().toUpperCase() );
            }

            Entry myEntry = new DefaultEntry( dn, attrs );
            ld = getAdminConnection();
            add( ld, myEntry, entity );
        }
        catch ( LdapException e )
        {
            String error = "create name [" + entity.getName() + "] caught LdapException=" + e.getMessage();
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
     * @throws us.jts.fortress.UpdateException
     *
     */
    public final void update( PwPolicy entity ) throws UpdateException
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
                    OLPW_MIN_AGE, entity.getMinAge().toString() ) );
            }

            if ( entity.getMaxAge() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    OLPW_MAX_AGE, entity.getMaxAge().toString() ) );
            }

            if ( entity.getInHistory() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    OLPW_IN_HISTORY, entity.getInHistory().toString() ) );
            }

            if ( entity.getCheckQuality() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    OLPW_CHECK_QUALITY, entity.getCheckQuality().toString() ) );
            }

            if ( entity.getMinLength() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    OLPW_MIN_LENGTH, entity.getMinLength().toString() ) );
            }

            if ( entity.getExpireWarning() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    OLPW_EXPIRE_WARNING, entity.getExpireWarning().toString() ) );
            }

            if ( entity.getGraceLoginLimit() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    OLPW_GRACE_LOGIN_LIMIT, entity.getGraceLoginLimit().toString() ) );
            }

            if ( entity.getLockout() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    OLPW_LOCKOUT, entity.getLockout().toString().toUpperCase() ) );
            }

            if ( entity.getLockoutDuration() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    OLPW_LOCKOUT_DURATION, entity.getLockoutDuration().toString() ) );
            }

            if ( entity.getMaxFailure() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    OLPW_MAX_FAILURE, entity.getMaxFailure().toString() ) );
            }

            if ( entity.getFailureCountInterval() != null )
            {
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    OLPW_FAILURE_COUNT_INTERVAL, entity.getFailureCountInterval().toString() ) );
            }

            if ( entity.getMustChange() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    OLPW_MUST_CHANGE, entity.getMustChange().toString().toUpperCase() ) );
            }

            if ( entity.getAllowUserChange() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    OLPW_ALLOW_USER_CHANGE, entity.getAllowUserChange().toString().toUpperCase() ) );
            }

            if ( entity.getSafeModify() != null )
            {
                /**
                 * OpenLDAP requires the boolean values to be upper case:
                 */
                mods.add( new DefaultModification(
                    ModificationOperation.REPLACE_ATTRIBUTE,
                    OLPW_SAFE_MODIFY, entity.getSafeModify().toString().toUpperCase() ) );
            }

            if ( mods != null && mods.size() > 0 )
            {
                ld = getAdminConnection();
                modify( ld, dn, mods, entity );
            }
        }
        catch ( LdapException e )
        {
            String error = "update name [" + entity.getName() + "] caught LdapException=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.PSWD_UPDATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param entity
     * @throws us.jts.fortress.RemoveException
     */
    public final void remove( PwPolicy entity ) throws RemoveException
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
            String error = "remove name [" + entity.getName() + "] caught LdapException=" + e.getMessage();
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
     * @throws us.jts.fortress.FinderException
     *
     */
    public final PwPolicy getPolicy( PwPolicy policy ) throws FinderException
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
                + e.getMessage();
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
        entity.setName( getRdn( le.getDn().getName() ) );
        //entity.setAttribute(getAttribute(le, OLPW_ATTRIBUTE));
        String val = getAttribute( le, OLPW_MIN_AGE );

        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setMinAge( new Integer( val ) );
        }

        val = getAttribute( le, OLPW_MAX_AGE );

        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setMaxAge( new Long( val ) );
        }

        val = getAttribute( le, OLPW_IN_HISTORY );

        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setInHistory( new Short( val ) );
        }

        val = getAttribute( le, OLPW_CHECK_QUALITY );

        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setCheckQuality( new Short( val ) );
        }

        val = getAttribute( le, OLPW_MIN_LENGTH );

        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setMinLength( new Short( val ) );
        }

        val = getAttribute( le, OLPW_EXPIRE_WARNING );

        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setExpireWarning( new Long( val ) );
        }

        val = getAttribute( le, OLPW_GRACE_LOGIN_LIMIT );

        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setGraceLoginLimit( new Short( val ) );
        }

        val = getAttribute( le, OLPW_LOCKOUT );

        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setLockout( Boolean.valueOf( val ) );
        }

        val = getAttribute( le, OLPW_LOCKOUT_DURATION );

        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setLockoutDuration( new Integer( val ) );
        }

        val = getAttribute( le, OLPW_MAX_FAILURE );

        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setMaxFailure( new Short( val ) );
        }

        val = getAttribute( le, OLPW_FAILURE_COUNT_INTERVAL );

        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setFailureCountInterval( new Short( val ) );
        }

        val = getAttribute( le, OLPW_MUST_CHANGE );

        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            //noinspection BooleanConstructorCall
            entity.setMustChange( Boolean.valueOf( val ) );
        }

        val = getAttribute( le, OLPW_ALLOW_USER_CHANGE );

        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setAllowUserChange( Boolean.valueOf( val ) );
        }

        val = getAttribute( le, OLPW_SAFE_MODIFY );

        if ( VUtil.isNotNullOrEmpty( val ) )
        {
            entity.setSafeModify( Boolean.valueOf( val ) );
        }

        return entity;
    }


    /**
     * @param policy
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    public final List<PwPolicy> findPolicy( PwPolicy policy ) throws FinderException
    {
        List<PwPolicy> policyArrayList = new ArrayList<>();
        LdapConnection ld = null;
        String policyRoot = getPolicyRoot( policy.getContextId() );
        String searchVal = null;

        try
        {
            searchVal = encodeSafeText( policy.getName(), GlobalIds.PWPOLICY_NAME_LEN );
            String filter = GlobalIds.FILTER_PREFIX + OLPW_POLICY_CLASS + ")("
                + GlobalIds.POLICY_NODE_TYPE + "=" + searchVal + "*))";
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, policyRoot,
                SearchScope.ONELEVEL, filter, PASSWORD_POLICY_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;

            while ( searchResults.next() )
            {
                policyArrayList.add( unloadLdapEntry( searchResults.getEntry(), sequence++ ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "findPolicy name [" + searchVal + "] caught LdapException=" + e.getMessage();
            throw new FinderException( GlobalErrIds.PSWD_SEARCH_FAILED, error, e );
        }
        catch ( CursorException e )
        {
            String error = "findPolicy name [" + searchVal + "] caught LdapException=" + e.getMessage();
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
    public final Set<String> getPolicies( String contextId )
        throws FinderException
    {
        Set<String> policySet = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
        LdapConnection ld = null;
        String policyRoot = getPolicyRoot( contextId );

        try
        {
            String filter = "(objectclass=" + OLPW_POLICY_CLASS + ")";
            ld = getAdminConnection();
            SearchCursor searchResults = search( ld, policyRoot,
                SearchScope.ONELEVEL, filter, PASSWORD_POLICY_NAME_ATR, false, GlobalIds.BATCH_SIZE );

            while ( searchResults.next() )
            {
                policySet.add( getAttribute( searchResults.getEntry(), GlobalIds.CN ) );
            }
        }
        catch ( LdapException e )
        {
            String error = "getPolicies caught LdapException=" + e.getMessage();
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
        return GlobalIds.POLICY_NODE_TYPE + "=" + policy.getName() + "," + getPolicyRoot( policy.getContextId() );
    }


    private String getPolicyRoot( String contextId )
    {
        return getRootDn( contextId, GlobalIds.PPOLICY_ROOT );
    }
}
