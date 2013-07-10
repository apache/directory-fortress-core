/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.jts.fortress.CreateException;
import us.jts.fortress.FinderException;
import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.GlobalIds;
import us.jts.fortress.ObjectFactory;
import us.jts.fortress.PasswordException;
import us.jts.fortress.RemoveException;
import us.jts.fortress.SecurityException;
import us.jts.fortress.UpdateException;
import us.jts.fortress.cfg.Config;
import us.jts.fortress.ldap.DataProvider;
import us.jts.fortress.ldap.openldap.OLPWControlImpl;
import us.jts.fortress.util.attr.AttrHelper;
import us.jts.fortress.util.attr.VUtil;
import us.jts.fortress.util.time.CUtil;

import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttribute;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModification;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModificationSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPSearchResults;


/**
 * Data access class for LDAP User entity.
 * <p/>
 * <p/>
 * The Fortress User LDAP schema follows:
 * <p/>
 * <h4>1. InetOrgPerson Structural Object Class </h4>
 * <code># The inetOrgPerson represents people who are associated with an</code><br />
 * <code># organization in some way.  It is a structural class and is derived</code><br />
 * <code># from the organizationalPerson which is defined in X.521 [X521].</code><br />
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 2.16.840.1.113730.3.2.2</code>
 * <li> <code>NAME 'inetOrgPerson'</code>
 * <li> <code>DESC 'RFC2798: Internet Organizational Person'</code>
 * <li> <code>SUP organizationalPerson</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MAY ( audio $ businessCategory $ carLicense $ departmentNumber $</code>
 * <li> <code>displayName $ employeeNumber $ employeeType $ givenName $</code>
 * <li> <code>homePhone $ homePostalAddress $ initials $ jpegPhoto $</code>
 * <li> <code>labeledURI $ mail $ manager $ mobile $ o $ pager $ photo $</code>
 * <li> <code>roomNumber $ secretary $ uid $ userCertificate $</code>
 * <li> <code>x500uniqueIdentifier $ preferredLanguage $</code>
 * <li> <code>userSMIMECertificate $ userPKCS12 ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>2. ftProperties AUXILIARY Object Class is used to store client specific name/value pairs on target entity</h4>
 * <code># This aux object class can be used to store custom attributes.</code><br />
 * <code># The properties collections consist of name/value pairs and are not constrainted by Fortress.</code><br />
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
 * <h4>3. ftUserAttrs is used to store user RBAC and Admin role assignment and other security attributes on User entity</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 1.3.6.1.4.1.38088.3.1</code>
 * <li> <code>NAME 'ftUserAttrs'</code>
 * <li> <code>DESC 'Fortress User Attribute AUX Object Class'</code>
 * <li> <code>AUXILIARY</code>
 * <li> <code>MUST ( ftId )</code>
 * <li> <code>MAY ( ftRC $ ftRA $ ftARC $ ftARA $ ftCstr</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>4. ftMods AUXILIARY Object Class is used to store Fortress audit variables on target entity.</h4>
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
 * @created August 30, 2009
 */
final class UserDAO extends DataProvider
{
    private static final String CLS_NM = UserDAO.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static PwPolicyControl pwControl;

    /**
     * Initialize the OpenLDAP Pw Policy validator.
     */
    static
    {
        if ( GlobalIds.IS_OPENLDAP )
        {
            pwControl = new OLPWControlImpl();
        }
    }

    /*
      *  *************************************************************************
      *  **  OpenAccessMgr USERS STATICS
      *  ************************************************************************
      */
    private static final String USERS_AUX_OBJECT_CLASS_NAME = "ftUserAttrs";
    private static final String ORGANIZATIONAL_PERSON_OBJECT_CLASS_NAME = "organizationalPerson";
    private static final String USER_OBJECT_CLASS = "user.objectclass";
    private static final String USERS_EXTENSIBLE_OBJECT = "extensibleObject";

    // The Fortress User entity attributes are stored within standard LDAP object classes along with custom auxiliary object classes:
    private static final String USER_OBJ_CLASS[] =
        {
            GlobalIds.TOP,
            Config.getProperty( USER_OBJECT_CLASS ),
            USERS_AUX_OBJECT_CLASS_NAME,
            GlobalIds.PROPS_AUX_OBJECT_CLASS_NAME,
            GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME,
            USERS_EXTENSIBLE_OBJECT
    };

    private static final String objectClassImpl = Config.getProperty( USER_OBJECT_CLASS );
    private static final String SN = "sn";
    private static final String PW = "userpassword";
    private static final String SYSTEM_USER = "ftSystem";

    /**
     * Constant contains the locale attribute name used within organizationalPerson ldap object classes.
     */
    private static final String L = "l";

    /**
     * Constant contains the postal address attribute name used within organizationalPerson ldap object classes.
     */
    private static final String POSTAL_ADDRESS = "postalAddress";

    /**
     * Constant contains the state attribute name used within organizationalPerson ldap object classes.
     */
    private static final String STATE = "st";

    /**
     * Constant contains the postal code attribute name used within organizationalPerson ldap object classes.
     */
    private static final String POSTAL_CODE = "postalCode";

    /**
     * Constant contains the post office box attribute name used within organizationalPerson ldap object classes.
     */
    private static final String POST_OFFICE_BOX = "postOfficeBox";

    /**
     * Constant contains the country attribute name used within organizationalPerson ldap object classes.
     */
    private static final String COUNTRY = "c";

    /**
     * Constant contains the  attribute name used within inetorgperson ldap object classes.
     */
    private static final String PHYSICAL_DELIVERY_OFFICE_NAME = "physicalDeliveryOfficeName";

    /**
     * Constant contains the  attribute name used within inetorgperson ldap object classes.
     */
    private static final String DEPARTMENT_NUMBER = "departmentNumber";

    /**
     * Constant contains the  attribute name used within inetorgperson ldap object classes.
     */
    private static final String ROOM_NUMBER = "roomNumber";

    /**
     * Constant contains the mobile attribute values used within iNetOrgPerson ldap object classes.
     */
    private static final String MOBILE = "mobile";

    /**
     * Constant contains the telephone attribute values used within organizationalPerson ldap object classes.
     */
    private static final String TELEPHONE_NUMBER = "telephoneNumber";

    /**
     * Constant contains the  attribute name for jpeg images to be stored within inetorgperson ldap object classes.
     */
    private static final String JPEGPHOTO = "jpegPhoto";

    /**
     * Constant contains the email attribute values used within iNetOrgPerson ldap object classes.
     */
    private static final String MAIL = "mail";
    private static final String DISPLAY_NAME = "displayName";
    private static final String TITLE = "title";
    private static final String EMPLOYEE_TYPE = "employeeType";

    private static final String OPENLDAP_POLICY_SUBENTRY = "pwdPolicySubentry";
    private static final String OPENLDAP_PW_RESET = "pwdReset";
    private static final String OPENLDAP_PW_LOCKED_TIME = "pwdAccountLockedTime";
    private static final String OPENLDAP_ACCOUNT_LOCKED_TIME = "pwdAccountLockedTime";
    private static final String LOCK_VALUE = "000001010000Z";
    private static final String[] USERID =
        { GlobalIds.UID };
    private static final String[] ROLES =
        { GlobalIds.USER_ROLE_ASSIGN };

    private static final String[] USERID_ATRS =
        {
            GlobalIds.UID
    };

    // This smaller result set of attributes are needed for user validation and authentication operations.
    private static final String[] AUTHN_ATRS =
        {
            GlobalIds.FT_IID,
            GlobalIds.UID, PW,
            GlobalIds.DESC,
            GlobalIds.OU, GlobalIds.CN,
            SN,
            GlobalIds.CONSTRAINT,
            GlobalIds.IS_OPENLDAP ? OPENLDAP_PW_RESET : null,
            GlobalIds.IS_OPENLDAP ? OPENLDAP_PW_LOCKED_TIME : null,
            GlobalIds.PROPS
    };

    // This default set of attributes contains all and is used for search operations.
    private static final String[] DEFAULT_ATRS =
        {
            GlobalIds.FT_IID,
            GlobalIds.UID, PW,
            GlobalIds.DESC,
            GlobalIds.OU,
            GlobalIds.CN,
            SN,
            GlobalIds.USER_ROLE_DATA,
            GlobalIds.CONSTRAINT,
            GlobalIds.USER_ROLE_ASSIGN,
            GlobalIds.IS_OPENLDAP ? OPENLDAP_PW_RESET : null,
            GlobalIds.IS_OPENLDAP ? OPENLDAP_PW_LOCKED_TIME : null,
            GlobalIds.IS_OPENLDAP ? OPENLDAP_POLICY_SUBENTRY : null,
            GlobalIds.PROPS,
            GlobalIds.USER_ADMINROLE_ASSIGN,
            GlobalIds.USER_ADMINROLE_DATA,
            POSTAL_ADDRESS,
            L,
            POSTAL_CODE,
            POST_OFFICE_BOX,
            STATE,
            PHYSICAL_DELIVERY_OFFICE_NAME,
            DEPARTMENT_NUMBER,
            ROOM_NUMBER,
            TELEPHONE_NUMBER,
            MOBILE,
            MAIL,
            EMPLOYEE_TYPE,
            TITLE,
            SYSTEM_USER,
            JPEGPHOTO
    };

    private static final String[] ROLE_ATR =
        {
            GlobalIds.USER_ROLE_DATA
    };

    private static final String[] AROLE_ATR =
        {
            GlobalIds.USER_ADMINROLE_DATA
    };


    /**
     * Don't let classes outside of this package construct this.
     */
    UserDAO()
    {
    }


    /**
     * @param entity
     * @return
     * @throws CreateException
     *
     */
    final User create( User entity )
        throws CreateException
    {
        LDAPConnection ld = null;

        try
        {
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add( createAttributes( GlobalIds.OBJECT_CLASS, USER_OBJ_CLASS ) );

            entity.setInternalId();
            attrs.add( createAttribute( GlobalIds.FT_IID, entity.getInternalId() ) );
            attrs.add( createAttribute( GlobalIds.UID, entity.getUserId() ) );

            // CN is required on inetOrgPerson object class, if caller did not set, use the userId:
            if ( !VUtil.isNotNullOrEmpty( entity.getCn() ) )
            {
                entity.setCn( entity.getUserId() );
            }

            attrs.add( createAttribute( GlobalIds.CN, entity.getCn() ) );

            // SN is required on inetOrgPerson object class, if caller did not set, use the userId:
            if ( !VUtil.isNotNullOrEmpty( entity.getSn() ) )
            {
                entity.setSn( entity.getUserId() );
            }

            attrs.add( createAttribute( SN, entity.getSn() ) );

            // guard against npe
            attrs.add( createAttribute( PW,
                VUtil.isNotNullOrEmpty( entity.getPassword() ) ? new String( entity.getPassword() ) : new String(
                    new char[]
                        {} ) ) );
            attrs.add( createAttribute( DISPLAY_NAME, entity.getCn() ) );

            if ( VUtil.isNotNullOrEmpty( entity.getTitle() ) )
            {
                attrs.add( createAttribute( TITLE, entity.getTitle() ) );
            }

            if ( VUtil.isNotNullOrEmpty( entity.getEmployeeType() ) )
            {
                attrs.add( createAttribute( EMPLOYEE_TYPE, entity.getEmployeeType() ) );
            }

            // These are multi-valued attributes, use the util function to load:
            // These items are optional.  The utility function will return quietly if no items are loaded into collection:
            loadAttrs( entity.getPhones(), attrs, TELEPHONE_NUMBER );
            loadAttrs( entity.getMobiles(), attrs, MOBILE );
            loadAttrs( entity.getEmails(), attrs, MAIL );

            // The following attributes are optional:
            if ( VUtil.isNotNullOrEmpty( entity.isSystem() ) )
            {
                attrs.add( createAttribute( SYSTEM_USER, entity.isSystem().toString().toUpperCase() ) );
            }
            if ( GlobalIds.IS_OPENLDAP && VUtil.isNotNullOrEmpty( entity.getPwPolicy() ) )
            {
                String dn = GlobalIds.POLICY_NODE_TYPE + "=" + entity.getPwPolicy() + ","
                    + getRootDn( entity.getContextId(), GlobalIds.PPOLICY_ROOT );
                attrs.add( createAttribute( OPENLDAP_POLICY_SUBENTRY, dn ) );
            }

            if ( VUtil.isNotNullOrEmpty( entity.getOu() ) )
            {
                attrs.add( createAttribute( GlobalIds.OU, entity.getOu() ) );
            }

            if ( VUtil.isNotNullOrEmpty( entity.getDescription() ) )
            {
                attrs.add( createAttribute( GlobalIds.DESC, entity.getDescription() ) );
            }

            // props are optional as well:
            // Add "initial" property here.
            entity.addProperty( "init", "" );
            loadProperties( entity.getProperties(), attrs, GlobalIds.PROPS );
            // map the userid to the name field in constraint:
            entity.setName( entity.getUserId() );
            attrs.add( createAttribute( GlobalIds.CONSTRAINT, CUtil.setConstraint( entity ) ) );
            loadAddress( entity.getAddress(), attrs );
            if ( VUtil.isNotNullOrEmpty( entity.getJpegPhoto() ) )
            {
                attrs.add( new LDAPAttribute( JPEGPHOTO, entity.getJpegPhoto() ) );
            }

            String dn = getDn( entity.getUserId(), entity.getContextId() );

            LDAPEntry myEntry = new LDAPEntry( dn, attrs );
            ld = getAdminConnection();
            add( ld, myEntry, entity );
            entity.setDn( dn );
        }
        catch ( LDAPException e )
        {
            String error = "create userId [" + entity.getUserId() + "] caught LDAPException="
                + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new CreateException( GlobalErrIds.USER_ADD_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return entity;
    }


    /**
     * @param entity
     * @return
     * @throws UpdateException
     */
    final User update( User entity )
        throws UpdateException
    {
        LDAPConnection ld = null;
        String userDn = getDn( entity.getUserId(), entity.getContextId() );

        try
        {
            LDAPModificationSet mods = new LDAPModificationSet();

            if ( VUtil.isNotNullOrEmpty( entity.getCn() ) )
            {
                LDAPAttribute cn = new LDAPAttribute( GlobalIds.CN, entity.getCn() );
                mods.add( LDAPModification.REPLACE, cn );
            }

            if ( VUtil.isNotNullOrEmpty( entity.getSn() ) )
            {
                LDAPAttribute sn = new LDAPAttribute( SN, entity.getSn() );
                mods.add( LDAPModification.REPLACE, sn );
            }

            if ( VUtil.isNotNullOrEmpty( entity.getOu() ) )
            {
                LDAPAttribute ou = new LDAPAttribute( GlobalIds.OU, entity.getOu() );
                mods.add( LDAPModification.REPLACE, ou );
            }

            if ( VUtil.isNotNullOrEmpty( entity.getPassword() ) )
            {
                LDAPAttribute pw = new LDAPAttribute( PW, new String( entity.getPassword() ) );
                mods.add( LDAPModification.REPLACE, pw );
            }

            if ( VUtil.isNotNullOrEmpty( entity.getDescription() ) )
            {
                LDAPAttribute desc = new LDAPAttribute( GlobalIds.DESC,
                    entity.getDescription() );
                mods.add( LDAPModification.REPLACE, desc );
            }

            if ( VUtil.isNotNullOrEmpty( entity.getEmployeeType() ) )
            {
                LDAPAttribute employeeType = new LDAPAttribute( EMPLOYEE_TYPE, entity.getEmployeeType() );
                mods.add( LDAPModification.REPLACE, employeeType );
            }

            if ( VUtil.isNotNullOrEmpty( entity.getTitle() ) )
            {
                LDAPAttribute title = new LDAPAttribute( TITLE, entity.getTitle() );
                mods.add( LDAPModification.REPLACE, title );
            }

            if ( GlobalIds.IS_OPENLDAP && VUtil.isNotNullOrEmpty( entity.getPwPolicy() ) )
            {
                String szDn = GlobalIds.POLICY_NODE_TYPE + "=" + entity.getPwPolicy() + ","
                    + getRootDn( entity.getContextId(), GlobalIds.PPOLICY_ROOT );
                LDAPAttribute dn = new LDAPAttribute( OPENLDAP_POLICY_SUBENTRY, szDn );
                mods.add( LDAPModification.REPLACE, dn );
            }

            if ( VUtil.isNotNullOrEmpty( entity.isSystem() ) )
            {
                LDAPAttribute system = new LDAPAttribute( SYSTEM_USER, entity.isSystem().toString().toUpperCase() );
                mods.add( LDAPModification.REPLACE, system );
            }

            if ( entity.isTemporalSet() )
            {
                // map the userid to the name field in constraint:
                entity.setName( entity.getUserId() );
                String szRawData = CUtil.setConstraint( entity );

                if ( VUtil.isNotNullOrEmpty( szRawData ) )
                {
                    LDAPAttribute constraint = new LDAPAttribute( GlobalIds.CONSTRAINT, szRawData );
                    mods.add( LDAPModification.REPLACE, constraint );
                }
            }

            if ( VUtil.isNotNullOrEmpty( entity.getProperties() ) )
            {
                loadProperties( entity.getProperties(), mods, GlobalIds.PROPS, true );
            }

            loadAddress( entity.getAddress(), mods );
            // These are multi-valued attributes, use the util function to load:
            loadAttrs( entity.getPhones(), mods, TELEPHONE_NUMBER );
            loadAttrs( entity.getMobiles(), mods, MOBILE );
            loadAttrs( entity.getEmails(), mods, MAIL );
            if ( VUtil.isNotNullOrEmpty( entity.getJpegPhoto() ) )
            {
                mods.add( LDAPModification.REPLACE, new LDAPAttribute( JPEGPHOTO, entity.getJpegPhoto() ) );
            }

            if ( mods.size() > 0 )
            {
                ld = getAdminConnection();
                modify( ld, userDn, mods, entity );
                entity.setDn( userDn );
            }

            entity.setDn( userDn );
        }
        catch ( LDAPException e )
        {
            String error = "update userId [" + entity.getUserId() + "] caught LDAPException="
                + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.USER_UPDATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return entity;
    }


    /**
     * @param entity
     * @param replace
     * @return
     * @throws UpdateException
     */
    final User updateProps( User entity, boolean replace )
        throws UpdateException
    {
        LDAPConnection ld = null;
        String userDn = getDn( entity.getUserId(), entity.getContextId() );

        try
        {
            LDAPModificationSet mods = new LDAPModificationSet();

            if ( VUtil.isNotNullOrEmpty( entity.getProperties() ) )
            {
                loadProperties( entity.getProperties(), mods, GlobalIds.PROPS, replace );
            }

            if ( mods.size() > 0 )
            {
                ld = getAdminConnection();
                modify( ld, userDn, mods, entity );
                entity.setDn( userDn );
            }

            entity.setDn( userDn );
        }
        catch ( LDAPException e )
        {
            String error = "updateProps userId [" + entity.getUserId() + "] isReplace [" + replace
                + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.USER_UPDATE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return entity;
    }


    /**
     * @param user
     * @throws RemoveException
     */
    final String remove( User user )
        throws RemoveException
    {
        LDAPConnection ld = null;
        String userDn = getDn( user.getUserId(), user.getContextId() );

        try
        {
            ld = getAdminConnection();
            delete( ld, userDn, user );
        }
        catch ( LDAPException e )
        {
            String error = "remove userId [" + user.getUserId() + "] caught LDAPException="
                + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new RemoveException( GlobalErrIds.USER_DELETE_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return userDn;
    }


    /**
     * @param user
     * @throws us.jts.fortress.UpdateException
     *
     */
    final void lock( User user )
        throws UpdateException
    {
        LDAPConnection ld = null;
        String userDn = getDn( user.getUserId(), user.getContextId() );

        try
        {
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute pwdAccoutLock = new LDAPAttribute( OPENLDAP_PW_LOCKED_TIME, LOCK_VALUE );
            mods.add( LDAPModification.REPLACE, pwdAccoutLock );
            ld = getAdminConnection();
            modify( ld, userDn, mods, user );
        }
        catch ( LDAPException e )
        {
            String error = "lock user [" + user.getUserId() + "] caught LDAPException="
                + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.USER_PW_LOCK_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param user
     * @throws UpdateException
     *
     */
    final void unlock( User user )
        throws UpdateException
    {
        LDAPConnection ld = null;
        String userDn = getDn( user.getUserId(), user.getContextId() );

        try
        {
            //ld = getAdminConnection();
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute pwdlockedTime = new LDAPAttribute( OPENLDAP_PW_LOCKED_TIME );
            mods.add( LDAPModification.DELETE, pwdlockedTime );
            ld = getAdminConnection();
            modify( ld, userDn, mods, user );
        }
        catch ( LDAPException e )
        {
            if ( e.getLDAPResultCode() == LDAPException.NO_SUCH_ATTRIBUTE )
            {
                LOG.info( "unlock user [" + user.getUserId() + "] no such attribute:"
                    + OPENLDAP_ACCOUNT_LOCKED_TIME );
            }
            else
            {
                String error = "unlock user [" + user.getUserId() + "] caught LDAPException= "
                    + e.getLDAPResultCode() + " msg=" + e.getMessage();
                throw new UpdateException( GlobalErrIds.USER_PW_UNLOCK_FAILED, error, e );
            }
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param user
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    final User getUser( User user, boolean isRoles )
        throws FinderException
    {
        User entity = null;
        LDAPConnection ld = null;
        String userDn = getDn( user.getUserId(), user.getContextId() );

        String[] uATTRS;
        // Retrieve role attributes?

        if ( isRoles )
        {
            // Retrieve the User's assigned RBAC and Admin Role attributes from directory.
            uATTRS = DEFAULT_ATRS;

        }
        else
        {
            // Do not retrieve the User's assigned RBAC and Admin Role attributes from directory.
            uATTRS = AUTHN_ATRS;
        }

        LDAPEntry findEntry = null;

        try
        {
            ld = getAdminConnection();
            findEntry = read( ld, userDn, uATTRS );
        }
        catch ( LDAPException e )
        {
            if ( e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT )
            {
                String warning = "getUser COULD NOT FIND ENTRY for user [" + user.getUserId() + "]";
                throw new FinderException( GlobalErrIds.USER_NOT_FOUND, warning );
            }

            String error = "getUser [" + userDn + "]= caught LDAPException=" + e.getLDAPResultCode()
                + " msg=" + e.getMessage();
            throw new FinderException( GlobalErrIds.USER_READ_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        entity = unloadLdapEntry( findEntry, 0, user.getContextId() );

        if ( entity == null )
        {
            String warning = "getUser userId [" + user.getUserId() + "] not found, Fortress rc="
                + GlobalErrIds.USER_NOT_FOUND;
            throw new FinderException( GlobalErrIds.USER_NOT_FOUND, warning );
        }

        return entity;
    }


    /**
     * @param user
     * @return
     * @throws us.jts.fortress.FinderException
     */
    final List<UserAdminRole> getUserAdminRoles( User user )
        throws FinderException
    {
        List<UserAdminRole> roles = null;
        LDAPConnection ld = null;
        String userDn = getDn( user.getUserId(), user.getContextId() );

        try
        {
            ld = getAdminConnection();
            LDAPEntry findEntry = read( ld, userDn, AROLE_ATR );
            roles = unloadUserAdminRoles( findEntry, user.getUserId(), user.getContextId() );
        }
        catch ( LDAPException e )
        {
            if ( e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT )
            {
                String warning = "getUserAdminRoles COULD NOT FIND ENTRY for user [" + user.getUserId() + "]";
                throw new FinderException( GlobalErrIds.USER_NOT_FOUND, warning );
            }

            String error = "getUserAdminRoles [" + userDn + "]= caught LDAPException="
                + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException( GlobalErrIds.USER_READ_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return roles;
    }


    /**
     * @param user
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    final List<String> getRoles( User user )
        throws FinderException
    {
        List<String> roles = null;
        LDAPConnection ld = null;
        String userDn = getDn( user.getUserId(), user.getContextId() );

        try
        {
            ld = getAdminConnection();
            LDAPEntry findEntry = read( ld, userDn, ROLES );

            if ( findEntry == null )
            {
                String warning = "getRoles userId [" + user.getUserId() + "] not found, Fortress rc="
                    + GlobalErrIds.USER_NOT_FOUND;
                throw new FinderException( GlobalErrIds.USER_NOT_FOUND, warning );
            }

            roles = getAttributes( findEntry, GlobalIds.USER_ROLE_ASSIGN );
        }
        catch ( LDAPException e )
        {
            if ( e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT )
            {
                String warning = "getRoles COULD NOT FIND ENTRY for user [" + user.getUserId() + "]";
                throw new FinderException( GlobalErrIds.USER_NOT_FOUND, warning );
            }

            String error = "getRoles [" + userDn + "]= caught LDAPException=" + e.getLDAPResultCode()
                + " msg=" + e.getMessage();
            throw new FinderException( GlobalErrIds.URLE_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return roles;
    }


    /**
     * @param user
     * @return
     * @throws us.jts.fortress.FinderException
     *
     * @throws us.jts.fortress.SecurityException
     */
    final Session checkPassword( User user ) throws FinderException
    {
        Session session = null;
        LDAPConnection ld = null;
        String userDn = getDn( user.getUserId(), user.getContextId() );

        try
        {
            session = new ObjectFactory().createSession();
            session.setUserId( user.getUserId() );
            ld = getUserConnection();
            boolean result = bind( ld, userDn, user.getPassword() );

            if ( result )
            {
                // check openldap password policies here
                checkPwPolicies( ld, session );

                if ( session.getErrorId() == 0 )
                {
                    session.setAuthenticated( true );
                }
            }
        }
        catch ( LDAPException e )
        {
            if ( e.getLDAPResultCode() == LDAPException.INVALID_CREDENTIALS )
            {
                // Check controls to see if password is locked, expired or out of grace:
                checkPwPolicies( ld, session );
                // if check pw control did not find problem the user entered invalid pw:
                if ( session.getErrorId() == 0 )
                {
                    String info = "checkPassword INVALID PASSWORD for userId [" + user.getUserId() + "]";
                    session.setMsg( info );
                    session.setErrorId( GlobalErrIds.USER_PW_INVLD );
                    session.setAuthenticated( false );
                }
            }
            else
            {
                String error = "checkPassword userId [" + user.getUserId() + "] caught LDAPException="
                    + e.getLDAPResultCode() + " msg=" + e.getMessage();
                throw new FinderException( GlobalErrIds.USER_READ_FAILED, error, e );
            }
        }
        finally
        {
            closeUserConnection( ld );
        }

        return session;
    }


    /**
     * @param user
     * @return
     * @throws FinderException
     */
    final List<User> findUsers( User user ) throws FinderException
    {
        List<User> userList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String userRoot = getRootDn( user.getContextId(), GlobalIds.USER_ROOT );

        try
        {
            String filter;

            if ( VUtil.isNotNullOrEmpty( user.getUserId() ) )
            {
                // place a wild card after the input userId:
                String searchVal = encodeSafeText( user.getUserId(), GlobalIds.USERID_LEN );
                filter = GlobalIds.FILTER_PREFIX + objectClassImpl + ")("
                    + GlobalIds.UID + "=" + searchVal + "*))";
            }
            else if ( VUtil.isNotNullOrEmpty( user.getInternalId() ) )
            {
                // internalUserId search
                String searchVal = encodeSafeText( user.getInternalId(), GlobalIds.USERID_LEN );
                // this is not a wildcard search. Must be exact match.
                filter = GlobalIds.FILTER_PREFIX + objectClassImpl + ")("
                    + GlobalIds.FT_IID + "=" + searchVal + "))";
            }
            else
            {
                // Beware - returns ALL users!!:"
                filter = "(objectclass=" + objectClassImpl + ")";
            }

            ld = getAdminConnection();
            searchResults = search( ld, userRoot,
                LDAPConnection.SCOPE_ONE, filter, DEFAULT_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;

            while ( searchResults.hasMoreElements() )
            {
                userList.add( unloadLdapEntry( searchResults.next(), sequence++, user.getContextId() ) );
            }
        }
        catch ( LDAPException e )
        {
            String warning = "findUsers userRoot [" + userRoot + "] caught LDAPException="
                + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException( GlobalErrIds.USER_SEARCH_FAILED, warning, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return userList;
    }


    /**
     * @param user
     * @param limit
     * @return
     * @throws FinderException
     *
     */
    final List<String> findUsers( User user, int limit ) throws FinderException
    {
        List<String> userList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String userRoot = getRootDn( user.getContextId(), GlobalIds.USER_ROOT );

        try
        {
            String searchVal = encodeSafeText( user.getUserId(), GlobalIds.USERID_LEN );
            String filter = GlobalIds.FILTER_PREFIX + objectClassImpl + ")("
                + GlobalIds.UID + "=" + searchVal + "*))";
            ld = getAdminConnection();
            searchResults = search( ld, userRoot,
                LDAPConnection.SCOPE_ONE, filter, USERID, false, GlobalIds.BATCH_SIZE, limit );

            while ( searchResults.hasMoreElements() )
            {
                LDAPEntry entry = searchResults.next();
                userList.add( getAttribute( entry, GlobalIds.UID ) );
            }
        }
        catch ( LDAPException e )
        {
            String warning = "findUsers caught LDAPException=" + e.getLDAPResultCode() + " msg="
                + e.getMessage();
            throw new FinderException( GlobalErrIds.USER_SEARCH_FAILED, warning, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return userList;
    }


    /**
     * @param role
     * @return
     * @throws FinderException
     *
     */
    final List<User> getAuthorizedUsers( Role role )
        throws FinderException
    {
        List<User> userList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String userRoot = getRootDn( role.getContextId(), GlobalIds.USER_ROOT );

        try
        {
            String roleVal = encodeSafeText( role.getName(), GlobalIds.USERID_LEN );
            String filter = GlobalIds.FILTER_PREFIX + USERS_AUX_OBJECT_CLASS_NAME + ")(";
            Set<String> roles = RoleUtil.getDescendants( role.getName(), role.getContextId() );

            if ( VUtil.isNotNullOrEmpty( roles ) )
            {
                filter += "|(" + GlobalIds.USER_ROLE_ASSIGN + "=" + roleVal + ")";

                for ( String uRole : roles )
                {
                    filter += "(" + GlobalIds.USER_ROLE_ASSIGN + "=" + uRole + ")";
                }

                filter += ")";
            }
            else
            {
                filter += GlobalIds.USER_ROLE_ASSIGN + "=" + roleVal + ")";
            }

            filter += ")";
            ld = getAdminConnection();
            searchResults = search( ld, userRoot,
                LDAPConnection.SCOPE_ONE, filter, DEFAULT_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;

            while ( searchResults.hasMoreElements() )
            {
                userList.add( unloadLdapEntry( searchResults.next(), sequence++, role.getContextId() ) );
            }
        }
        catch ( LDAPException e )
        {
            String warning = "getAuthorizedUsers role name [" + role.getName() + "] caught LDAPException="
                + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException( GlobalErrIds.URLE_SEARCH_FAILED, warning, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return userList;
    }


    /**
     * @param role
     * @return
     * @throws FinderException
     */
    final List<User> getAssignedUsers( Role role )
        throws FinderException
    {
        List<User> userList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String userRoot = getRootDn( role.getContextId(), GlobalIds.USER_ROOT );

        try
        {
            String roleVal = encodeSafeText( role.getName(), GlobalIds.USERID_LEN );
            String filter = GlobalIds.FILTER_PREFIX + USERS_AUX_OBJECT_CLASS_NAME + ")("
                + GlobalIds.USER_ROLE_ASSIGN + "=" + roleVal + "))";
            ld = getAdminConnection();
            searchResults = search( ld, userRoot,
                LDAPConnection.SCOPE_ONE, filter, DEFAULT_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;

            while ( searchResults.hasMoreElements() )
            {
                userList.add( unloadLdapEntry( searchResults.next(), sequence++, role.getContextId() ) );
            }
        }
        catch ( LDAPException e )
        {
            String warning = "getAssignedUsers role name [" + role.getName() + "] caught LDAPException="
                + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException( GlobalErrIds.URLE_SEARCH_FAILED, warning, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return userList;
    }


    /**
     *
     * @param roles
     * @return
     * @throws FinderException
     */
    final Set<String> getAssignedUsers( Set<String> roles, String contextId )
        throws FinderException
    {
        Set<String> userSet = new HashSet<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String userRoot = getRootDn( contextId, GlobalIds.USER_ROOT );

        try
        {
            String filter = GlobalIds.FILTER_PREFIX + USERS_AUX_OBJECT_CLASS_NAME + ")(|";
            if ( VUtil.isNotNullOrEmpty( roles ) )
            {
                for ( String roleVal : roles )
                {
                    String filteredVal = encodeSafeText( roleVal, GlobalIds.USERID_LEN );
                    filter += "(" + GlobalIds.USER_ROLE_ASSIGN + "=" + filteredVal + ")";
                }
            }
            else
            {
                return null;
            }

            filter += "))";
            ld = getAdminConnection();
            searchResults = search( ld, userRoot,
                LDAPConnection.SCOPE_ONE, filter, USERID_ATRS, false, GlobalIds.BATCH_SIZE );

            while ( searchResults.hasMoreElements() )
            {
                userSet.add( getAttribute( searchResults.next(), GlobalIds.UID ) );
            }
        }
        catch ( LDAPException e )
        {
            String warning = "getAssignedUsers caught LDAPException=" + e.getLDAPResultCode() + " msg="
                + e.getMessage();
            throw new FinderException( GlobalErrIds.URLE_SEARCH_FAILED, warning, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return userSet;
    }


    /**
     * @param role
     * @return
     * @throws FinderException
     */
    final List<User> getAssignedUsers( AdminRole role )
        throws FinderException
    {
        List<User> userList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String userRoot = getRootDn( role.getContextId(), GlobalIds.USER_ROOT );

        try
        {
            String roleVal = encodeSafeText( role.getName(), GlobalIds.USERID_LEN );
            String filter = GlobalIds.FILTER_PREFIX + USERS_AUX_OBJECT_CLASS_NAME + ")("
                + GlobalIds.USER_ADMINROLE_ASSIGN + "=" + roleVal + "))";
            ld = getAdminConnection();
            searchResults = search( ld, userRoot,
                LDAPConnection.SCOPE_ONE, filter, DEFAULT_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;

            while ( searchResults.hasMoreElements() )
            {
                userList.add( unloadLdapEntry( searchResults.next(), sequence++, role.getContextId() ) );
            }
        }
        catch ( LDAPException e )
        {
            String warning = "getAssignedUsers admin role name [" + role.getName()
                + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException( GlobalErrIds.ARLE_USER_SEARCH_FAILED, warning, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return userList;
    }


    /**
     * @param role
     * @param limit
     * @return
     * @throws FinderException
     *
     */
    final List<String> getAuthorizedUsers( Role role, int limit )
        throws FinderException
    {
        List<String> userList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String userRoot = getRootDn( role.getContextId(), GlobalIds.USER_ROOT );

        try
        {
            String roleVal = encodeSafeText( role.getName(), GlobalIds.USERID_LEN );
            String filter = GlobalIds.FILTER_PREFIX + USERS_AUX_OBJECT_CLASS_NAME + ")("
                + GlobalIds.USER_ROLE_ASSIGN + "=" + roleVal + "))";
            ld = getAdminConnection();
            searchResults = search( ld, userRoot,
                LDAPConnection.SCOPE_ONE, filter, USERID, false, GlobalIds.BATCH_SIZE, limit );

            while ( searchResults.hasMoreElements() )
            {
                LDAPEntry entry = searchResults.next();
                userList.add( getAttribute( entry, GlobalIds.UID ) );
            }
        }
        catch ( LDAPException e )
        {
            String warning = "getAuthorizedUsers role name [" + role.getName() + "] caught LDAPException="
                + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException( GlobalErrIds.URLE_SEARCH_FAILED, warning, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return userList;
    }


    /**
     * @param searchVal
     * @return
     * @throws FinderException
     */
    final List<String> findUsersList( String searchVal, String contextId )
        throws FinderException
    {
        List<String> userList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String userRoot = getRootDn( contextId, GlobalIds.USER_ROOT );

        try
        {
            searchVal = encodeSafeText( searchVal, GlobalIds.USERID_LEN );
            //ld = getAdminConnection();
            String filter = GlobalIds.FILTER_PREFIX + objectClassImpl + ")("
                + GlobalIds.UID + "=" + searchVal + "*))";
            ld = getAdminConnection();
            searchResults = search( ld, userRoot,
                LDAPConnection.SCOPE_ONE, filter, DEFAULT_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;

            while ( searchResults.hasMoreElements() )
            {
                userList.add( ( unloadLdapEntry( searchResults.next(), sequence++, contextId ) ).getUserId() );
            }
        }
        catch ( LDAPException e )
        {
            String warning = "findUsersList caught LDAPException=" + e.getLDAPResultCode() + " msg="
                + e.getMessage();
            throw new FinderException( GlobalErrIds.USER_SEARCH_FAILED, warning, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return userList;
    }


    /**
     * @param ou
     * @return
     * @throws FinderException
     */
    final List<User> findUsers( OrgUnit ou, boolean limitSize )
        throws FinderException
    {
        List<User> userList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String userRoot = getRootDn( ou.getContextId(), GlobalIds.USER_ROOT );

        try
        {
            String szOu = encodeSafeText( ou.getName(), GlobalIds.OU_LEN );
            String filter = GlobalIds.FILTER_PREFIX + objectClassImpl + ")("
                + GlobalIds.OU + "=" + szOu + "))";
            int maxLimit;

            if ( limitSize )
            {
                maxLimit = 10;
            }
            else
            {
                maxLimit = 0;
            }

            ld = getAdminConnection();
            searchResults = search( ld, userRoot,
                LDAPConnection.SCOPE_ONE, filter, DEFAULT_ATRS, false, GlobalIds.BATCH_SIZE, maxLimit );
            long sequence = 0;

            while ( searchResults.hasMoreElements() )
            {
                userList.add( unloadLdapEntry( searchResults.next(), sequence++, ou.getContextId() ) );
            }
        }
        catch ( LDAPException e )
        {
            String warning = "findUsers caught LDAPException=" + e.getLDAPResultCode() + " msg="
                + e.getMessage();
            throw new FinderException( GlobalErrIds.USER_SEARCH_FAILED, warning, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return userList;
    }


    /**
     * @param entity
     * @param newPassword
     * @return
     * @throws UpdateException
     *
     * @throws SecurityException
     */
    final boolean changePassword( User entity, char[] newPassword )
        throws SecurityException
    {
        boolean rc = true;
        LDAPConnection ld = null;
        LDAPModificationSet mods;
        String userDn = getDn( entity.getUserId(), entity.getContextId() );

        try
        {
            ld = getUserConnection();
            bind( ld, userDn, entity.getPassword() );
            mods = new LDAPModificationSet();
            LDAPAttribute pw = new LDAPAttribute( PW, new String( newPassword ) );
            mods.add( LDAPModification.REPLACE, pw );
            modify( ld, userDn, mods );

            // The 2nd modify is to update audit attributes on the User entry:
            if ( GlobalIds.IS_AUDIT && entity.getAdminSession() != null )
            {
                // Because the user modified their own password, set their userId here:
                //(entity.getAdminSession()).setInternalUserId(entity.getUserId());
                mods = new LDAPModificationSet();
                modify( ld, userDn, mods, entity );
            }
        }
        catch ( LDAPException e )
        {
            String warning = User.class.getName() + ".changePassword user [" + entity.getUserId() + "] ";

            if ( e.getLDAPResultCode() == LDAPException.CONSTRAINT_VIOLATION )
            {
                warning += " constraint violation, ldap rc=" + e.getLDAPResultCode() + " ldap msg=" + e.getMessage()
                    + " Fortress rc=" + GlobalErrIds.PSWD_CONST_VIOLATION;
                throw new PasswordException( GlobalErrIds.PSWD_CONST_VIOLATION, warning );
            }
            else if ( e.getLDAPResultCode() == LDAPException.INSUFFICIENT_ACCESS_RIGHTS )
            {
                warning += " user not authorized to change password, ldap rc=" + e.getLDAPResultCode() + " ldap msg="
                    + e.getMessage() + " Fortress rc=" + GlobalErrIds.USER_PW_MOD_NOT_ALLOWED;
                throw new UpdateException( GlobalErrIds.USER_PW_MOD_NOT_ALLOWED, warning );
            }

            warning += " caught LDAPException rc=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.USER_PW_CHANGE_FAILED, warning, e );
        }
        finally
        {
            closeUserConnection( ld );
        }

        return rc;
    }


    /**
     * @param user
     * @throws UpdateException
     *
     */
    final void resetUserPassword( User user )
        throws UpdateException
    {
        LDAPConnection ld = null;
        String userDn = getDn( user.getUserId(), user.getContextId() );

        try
        {
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute pw = new LDAPAttribute( PW, new String( user.getPassword() ) );
            mods.add( LDAPModification.REPLACE, pw );
            LDAPAttribute pwdReset = new LDAPAttribute( OPENLDAP_PW_RESET, "TRUE" );
            mods.add( LDAPModification.REPLACE, pwdReset );
            ld = getAdminConnection();
            modify( ld, userDn, mods, user );
        }
        catch ( LDAPException e )
        {
            String warning = "resetUserPassword userId [" + user.getUserId() + "] caught LDAPException="
                + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.USER_PW_RESET_FAILED, warning, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param uRole
     * @return
     * @throws UpdateException
     *
     * @throws FinderException
     *
     */
    final String assign( UserRole uRole )
        throws UpdateException, FinderException
    {
        LDAPConnection ld = null;
        String userDn = getDn( uRole.getUserId(), uRole.getContextId() );

        try
        {
            //ld = getAdminConnection();
            LDAPModificationSet mods = new LDAPModificationSet();
            String szUserRole = uRole.getRawData();
            LDAPAttribute attr = new LDAPAttribute( GlobalIds.USER_ROLE_DATA, szUserRole );
            mods.add( LDAPModification.ADD, attr );
            attr = new LDAPAttribute( GlobalIds.USER_ROLE_ASSIGN, uRole.getName() );
            mods.add( LDAPModification.ADD, attr );
            ld = getAdminConnection();
            modify( ld, userDn, mods, uRole );
        }
        catch ( LDAPException e )
        {
            String warning = "assign userId [" + uRole.getUserId() + "] name [" + uRole.getName() + "] ";

            if ( e.getLDAPResultCode() == LDAPException.ATTRIBUTE_OR_VALUE_EXISTS )
            {
                warning += "assignment already exists.";
                throw new FinderException( GlobalErrIds.URLE_ASSIGN_EXIST, warning );
            }
            else
            {
                warning += "caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
                throw new UpdateException( GlobalErrIds.URLE_ASSIGN_FAILED, warning, e );
            }
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return userDn;
    }


    /**
     * @param uRole
     * @return
     * @throws UpdateException
     *
     * @throws FinderException
     *
     */
    final String deassign( UserRole uRole )
        throws UpdateException, FinderException
    {
        LDAPConnection ld = null;
        String userDn = getDn( uRole.getUserId(), uRole.getContextId() );

        try
        {
            // read the user's RBAC role assignments to locate target record.  Need the raw data before attempting removal:
            List<UserRole> roles = getUserRoles( uRole.getUserId(), uRole.getContextId() );
            int indx = -1;

            // Does the user have any roles assigned?
            if ( roles != null )
            {
                // function call will set indx to -1 if name not found:
                indx = roles.indexOf( uRole );

                // Is the targeted name assigned to user?
                if ( indx > -1 )
                {
                    // Retrieve the targeted name:
                    UserRole fRole = roles.get( indx );
                    // delete the name assignment attribute using the raw name data:
                    LDAPModificationSet mods = new LDAPModificationSet();
                    LDAPAttribute rAttr = new LDAPAttribute( GlobalIds.USER_ROLE_DATA, fRole.getRawData() );
                    mods.add( LDAPModification.DELETE, rAttr );
                    rAttr = new LDAPAttribute( GlobalIds.USER_ROLE_ASSIGN, fRole.getName() );
                    mods.add( LDAPModification.DELETE, rAttr );
                    ld = getAdminConnection();
                    modify( ld, userDn, mods, uRole );
                }
            }
            // target name not found:
            if ( indx == -1 )
            {
                // The user does not have the target name assigned,
                String warning = "deassign userId [" + uRole.getUserId() + "] name [" + uRole.getName()
                    + "] assignment does not exist.";
                throw new FinderException( GlobalErrIds.URLE_ASSIGN_NOT_EXIST, warning );
            }
        }
        catch ( LDAPException e )
        {
            String warning = "deassign userId [" + uRole.getUserId() + "] name [" + uRole.getName()
                + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.URLE_DEASSIGN_FAILED, warning, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return userDn;
    }


    /**
     * @param uRole
     * @return
     * @throws UpdateException
     *
     * @throws FinderException
     *
     */
    final String assign( UserAdminRole uRole )
        throws UpdateException, FinderException
    {
        LDAPConnection ld = null;
        String userDn = getDn( uRole.getUserId(), uRole.getContextId() );

        try
        {
            LDAPModificationSet mods = new LDAPModificationSet();
            String szUserRole = uRole.getRawData();
            LDAPAttribute attr = new LDAPAttribute( GlobalIds.USER_ADMINROLE_DATA, szUserRole );
            mods.add( LDAPModification.ADD, attr );
            attr = new LDAPAttribute( GlobalIds.USER_ADMINROLE_ASSIGN, uRole.getName() );
            mods.add( LDAPModification.ADD, attr );
            ld = getAdminConnection();
            modify( ld, userDn, mods, uRole );
        }
        catch ( LDAPException e )
        {
            if ( e.getLDAPResultCode() == LDAPException.ATTRIBUTE_OR_VALUE_EXISTS )
            {
                String warning = "assign userId [" + uRole.getUserId() + "] name [" + uRole.getName()
                    + "] assignment already exists.";
                throw new FinderException( GlobalErrIds.ARLE_ASSIGN_EXIST, warning );
            }
            else
            {
                String warning = "assign userId [" + uRole.getUserId() + "] name [" + uRole.getName()
                    + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
                throw new UpdateException( GlobalErrIds.ARLE_ASSIGN_FAILED, warning, e );
            }
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return userDn;
    }


    /**
     * @param uRole
     * @return
     * @throws UpdateException
     *
     * @throws FinderException
     *
     */
    final String deassign( UserAdminRole uRole )
        throws UpdateException, FinderException
    {
        LDAPConnection ld = null;
        String userDn = getDn( uRole.getUserId(), uRole.getContextId() );

        try
        {
            // read the user's ARBAC roles to locate record.  Need the raw data before attempting removal:
            User user = new User( uRole.getUserId() );
            user.setContextId( uRole.getContextId() );
            List<UserAdminRole> roles = getUserAdminRoles( user );
            //User user = getUser(uRole.getUserId(), true);
            //List<UserAdminRole> roles = user.getAdminRoles();
            int indx = -1;

            // Does the user have any roles assigned?
            if ( roles != null )
            {
                // function call will set indx to -1 if name not found:
                indx = roles.indexOf( uRole );

                // Is the targeted name assigned to user?
                if ( indx > -1 )
                {
                    // Retrieve the targeted name:
                    UserRole fRole = roles.get( indx );
                    // delete the name assignment attribute using the raw name data:
                    LDAPModificationSet mods = new LDAPModificationSet();
                    LDAPAttribute rAttr = new LDAPAttribute( GlobalIds.USER_ADMINROLE_DATA, fRole.getRawData() );
                    mods.add( LDAPModification.DELETE, rAttr );
                    rAttr = new LDAPAttribute( GlobalIds.USER_ADMINROLE_ASSIGN, fRole.getName() );
                    mods.add( LDAPModification.DELETE, rAttr );
                    ld = getAdminConnection();
                    modify( ld, userDn, mods, uRole );
                }
            }

            // target name not found:
            if ( indx == -1 )
            {
                // The user does not have the target name assigned,
                String warning = "deassign userId [" + uRole.getUserId() + "] name [" + uRole.getName()
                    + "] assignment does not exist.";
                throw new FinderException( GlobalErrIds.ARLE_DEASSIGN_NOT_EXIST, warning );
            }
        }
        catch ( LDAPException e )
        {
            String warning = "deassign userId [" + uRole.getUserId() + "] name [" + uRole.getName()
                + "] caught LDAPException=" + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.ARLE_DEASSIGN_FAILED, warning, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return userDn;
    }


    /**
     * @param user
     * @return
     * @throws UpdateException
     *
     */
    final String deletePwPolicy( User user )
        throws UpdateException
    {
        LDAPConnection ld = null;
        String userDn = getDn( user.getUserId(), user.getContextId() );

        try
        {
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute policy = new LDAPAttribute( OPENLDAP_POLICY_SUBENTRY );
            mods.add( LDAPModification.DELETE, policy );
            ld = getAdminConnection();
            modify( ld, userDn, mods, user );
        }
        catch ( LDAPException e )
        {
            String warning = "deletePwPolicy userId [" + user.getUserId() + "] caught LDAPException="
                + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new UpdateException( GlobalErrIds.USER_PW_PLCY_DEL_FAILED, warning, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }

        return userDn;
    }


    /**
     * @param le
     * @return
     * @throws LDAPException
     */
    private User unloadLdapEntry( LDAPEntry le, long sequence, String contextId )
    {
        User entity = new ObjectFactory().createUser();
        entity.setSequenceId( sequence );
        entity.setInternalId( getAttribute( le, GlobalIds.FT_IID ) );
        entity.setDescription( getAttribute( le, GlobalIds.DESC ) );
        entity.setUserId( getAttribute( le, GlobalIds.UID ) );
        entity.setCn( getAttribute( le, GlobalIds.CN ) );
        entity.setName( entity.getCn() );
        entity.setSn( getAttribute( le, SN ) );
        entity.setOu( getAttribute( le, GlobalIds.OU ) );
        entity.setDn( le.getDN() );
        entity.setTitle( getAttribute( le, TITLE ) );
        entity.setEmployeeType( getAttribute( le, EMPLOYEE_TYPE ) );
        unloadTemporal( le, entity );
        entity.setRoles( unloadUserRoles( le, entity.getUserId(), contextId ) );
        entity.setAdminRoles( unloadUserAdminRoles( le, entity.getUserId(), contextId ) );
        entity.setAddress( unloadAddress( le ) );
        entity.setPhones( getAttributes( le, TELEPHONE_NUMBER ) );
        entity.setMobiles( getAttributes( le, MOBILE ) );
        entity.setEmails( getAttributes( le, MAIL ) );
        String szBoolean = getAttribute( le, SYSTEM_USER );
        if ( szBoolean != null )
        {
            entity.setSystem( Boolean.valueOf( szBoolean ) );
        }

        entity.addProperties( AttrHelper.getProperties( getAttributes( le, GlobalIds.PROPS ) ) );

        if ( GlobalIds.IS_OPENLDAP )
        {
            szBoolean = getAttribute( le, OPENLDAP_PW_RESET );
            if ( szBoolean != null && szBoolean.equalsIgnoreCase( "true" ) )
            {
                entity.setReset( true );
            }
            String szPolicy = getAttribute( le, OPENLDAP_POLICY_SUBENTRY );
            if ( VUtil.isNotNullOrEmpty( szPolicy ) )
            {
                entity.setPwPolicy( getRdn( szPolicy ) );
            }
            szBoolean = getAttribute( le, OPENLDAP_PW_LOCKED_TIME );
            if ( szBoolean != null && szBoolean.equals( LOCK_VALUE ) )
            {
                entity.setLocked( true );
            }
        }
        entity.setJpegPhoto( getPhoto( le, JPEGPHOTO ) );
        return entity;
    }


    /**
     * @param userId
     * @return
     * @throws FinderException
     */
    private List<UserRole> getUserRoles( String userId, String contextId )
        throws FinderException
    {
        List<UserRole> roles = null;
        LDAPConnection ld = null;
        String userDn = getDn( userId, contextId );
        try
        {
            ld = getAdminConnection();
            LDAPEntry findEntry = read( ld, userDn, ROLE_ATR );
            roles = unloadUserRoles( findEntry, userId, contextId );
        }
        catch ( LDAPException e )
        {
            if ( e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT )
            {
                String warning = "getUserRoles COULD NOT FIND ENTRY for user [" + userId + "]";
                throw new FinderException( GlobalErrIds.USER_NOT_FOUND, warning );
            }

            String error = "getUserRoles [" + userDn + "]= caught LDAPException=" + e.getLDAPResultCode()
                + " msg=" + e.getMessage();
            throw new FinderException( GlobalErrIds.USER_READ_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return roles;
    }


    /**
     * @param ld
     * @param pwMsg
     */
    private void checkPwPolicies( LDAPConnection ld, PwMessage pwMsg )
    {
        int rc = 0;
        boolean success = false;
        String msgHdr = "checkPwPolicies for userId [" + pwMsg.getUserId() + "] ";

        if ( ld != null )
        {
            if ( !GlobalIds.IS_OPENLDAP )
            {
                pwMsg.setWarningId( GlobalPwMsgIds.NO_CONTROLS_FOUND );
                pwMsg.setErrorId( GlobalPwMsgIds.GOOD );
                String msg = msgHdr + "PW POLICY NOT ENABLED";
                pwMsg.setMsg( msg );
                LOG.debug( msg );
                return;
            }
            else if ( pwControl != null )
            {
                pwControl.checkPasswordPolicy( ld, success, pwMsg );
            }

            // OpenLDAP has notified of password violation:
            if ( pwMsg.getErrorId() > 0 )
            {
                String errMsg;

                switch ( pwMsg.getErrorId() )
                {

                    case GlobalPwMsgIds.CHANGE_AFTER_RESET:
                        // Don't throw exception if authenticating in J2EE Realm - The Web application must give user a chance to modify their password.
                        if ( !GlobalIds.IS_REALM )
                        {
                            errMsg = msgHdr + "PASSWORD HAS BEEN RESET BY LDAP_ADMIN_POOL_UID";
                            rc = GlobalErrIds.USER_PW_RESET;
                        }
                        else
                        {
                            errMsg = msgHdr
                                + "PASSWORD HAS BEEN RESET BY LDAP_ADMIN_POOL_UID BUT ALLOWING TO CONTINUE DUE TO REALM";
                            success = true;
                            pwMsg.setWarningId( GlobalErrIds.USER_PW_RESET );
                        }

                        break;

                    case GlobalPwMsgIds.ACCOUNT_LOCKED:
                        errMsg = msgHdr + "ACCOUNT HAS BEEN LOCKED";
                        rc = GlobalErrIds.USER_PW_LOCKED;
                        break;

                    case GlobalPwMsgIds.PASSWORD_HAS_EXPIRED:
                        errMsg = msgHdr + "PASSWORD HAS EXPIRED";
                        rc = GlobalErrIds.USER_PW_EXPIRED;
                        break;

                    case GlobalPwMsgIds.NO_MODIFICATIONS:
                        errMsg = msgHdr + "PASSWORD MOD NOT ALLOWED";
                        rc = GlobalErrIds.USER_PW_MOD_NOT_ALLOWED;
                        break;

                    case GlobalPwMsgIds.MUST_SUPPLY_OLD:
                        errMsg = msgHdr + "MUST SUPPLY OLD PASSWORD";
                        rc = GlobalErrIds.USER_PW_MUST_SUPPLY_OLD;
                        break;

                    case GlobalPwMsgIds.INSUFFICIENT_QUALITY:
                        errMsg = msgHdr + "PASSWORD QUALITY VIOLATION";
                        rc = GlobalErrIds.USER_PW_NSF_QUALITY;
                        break;

                    case GlobalPwMsgIds.PASSWORD_TOO_SHORT:
                        errMsg = msgHdr + "PASSWORD TOO SHORT";
                        rc = GlobalErrIds.USER_PW_TOO_SHORT;
                        break;

                    case GlobalPwMsgIds.PASSWORD_TOO_YOUNG:
                        errMsg = msgHdr + "PASSWORD TOO YOUNG";
                        rc = GlobalErrIds.USER_PW_TOO_YOUNG;
                        break;

                    case GlobalPwMsgIds.HISTORY_VIOLATION:
                        errMsg = msgHdr + "PASSWORD IN HISTORY VIOLATION";
                        rc = GlobalErrIds.USER_PW_IN_HISTORY;
                        break;

                    default:
                        errMsg = msgHdr + "PASSWORD CHECK FAILED";
                        rc = GlobalErrIds.USER_PW_CHK_FAILED;
                        break;
                }

                pwMsg.setMsg( errMsg );
                pwMsg.setErrorId( rc );
                pwMsg.setAuthenticated( success );
                LOG.debug( errMsg );
            }
            else
            {
                // Checked out good:
                String msg = msgHdr + "PASSWORD CHECK SUCCESS";
                pwMsg.setMsg( msg );
                pwMsg.setErrorId( 0 );
                pwMsg.setAuthenticated( true );
                LOG.debug( msg );
            }
        }
        else
        {
            // Even though we didn't find valid pw control, the actual userid/pw check passed:
            pwMsg.setAuthenticated( success );
            pwMsg.setWarningId( GlobalPwMsgIds.NO_CONTROLS_FOUND );
            pwMsg.setErrorId( GlobalPwMsgIds.GOOD );
            String msg = msgHdr + "NO PASSWORD CONTROLS FOUND";
            pwMsg.setMsg( msg );
            LOG.warn( "checkPwPolicies " + msg );
        }
    }


    /**
     * Given a collection of ARBAC roles, {@link UserAdminRole}, convert to raw data format and load into ldap attribute set in preparation for ldap add.
     *
     * @param list  contains List of type {@link UserAdminRole} targeted for adding to ldap.
     * @param attrs collection of ldap attributes containing ARBAC role assignments in raw ldap format.
     */
    private void loadUserAdminRoles( List<UserAdminRole> list, LDAPAttributeSet attrs )
    {
        if ( list != null )
        {
            LDAPAttribute attr = null;
            LDAPAttribute attrNm = null;

            for ( UserAdminRole userRole : list )
            {
                String szUserRole = userRole.getRawData();

                if ( attr == null )
                {
                    attr = new LDAPAttribute( GlobalIds.USER_ADMINROLE_DATA, szUserRole );
                    attrNm = new LDAPAttribute( GlobalIds.USER_ADMINROLE_ASSIGN, userRole.getName() );
                }
                else
                {
                    attr.addValue( szUserRole );
                    attrNm.addValue( userRole.getName() );
                }
            }

            if ( attr != null )
            {
                attrs.add( attr );
                attrs.add( attrNm );
            }
        }
    }


    /**
     * Given a collection of RBAC roles, {@link UserRole}, convert to raw data format and load into ldap modification set in preparation for ldap modify.
     *
     * @param list contains List of type {@link UserRole} targeted for updating into ldap.
     * @param mods contains ldap modification set containing RBAC role assignments in raw ldap format to be updated.
     */
    private void loadUserRoles( List<UserRole> list, LDAPModificationSet mods )
    {
        LDAPAttribute attr = null;
        LDAPAttribute attrNm = null;

        if ( list != null )
        {
            for ( UserRole userRole : list )
            {
                String szUserRole = userRole.getRawData();

                if ( attr == null )
                {
                    attr = new LDAPAttribute( GlobalIds.USER_ROLE_DATA, szUserRole );
                    attrNm = new LDAPAttribute( GlobalIds.USER_ROLE_ASSIGN, userRole.getName() );
                }
                else
                {
                    attr.addValue( szUserRole );
                    attrNm.addValue( userRole.getName() );
                }
            }

            if ( attr != null )
            {
                mods.add( LDAPModification.REPLACE, attr );
                mods.add( LDAPModification.REPLACE, attrNm );
            }
        }
    }


    /**
     * Given a collection of ARBAC roles, {@link UserAdminRole}, convert to raw data format and load into ldap modification set in preparation for ldap modify.
     *
     * @param list contains List of type {@link UserAdminRole} targeted for updating to ldap.
     * @param mods contains ldap modification set containing ARBAC role assignments in raw ldap format to be updated.
     */
    private void loadUserAdminRoles( List<UserAdminRole> list, LDAPModificationSet mods )
    {
        LDAPAttribute attr = null;
        LDAPAttribute attrNm = null;

        if ( list != null )
        {
            for ( UserAdminRole userRole : list )
            {
                String szUserRole = userRole.getRawData();

                if ( attr == null )
                {
                    attr = new LDAPAttribute( GlobalIds.USER_ADMINROLE_DATA, szUserRole );
                    attrNm = new LDAPAttribute( GlobalIds.USER_ADMINROLE_ASSIGN, userRole.getName() );
                }
                else
                {
                    attr.addValue( szUserRole );
                    attrNm.addValue( userRole.getName() );
                }
            }

            if ( attr != null )
            {
                mods.add( LDAPModification.REPLACE, attr );
                mods.add( LDAPModification.REPLACE, attrNm );
            }
        }
    }


    /**
     * Given a collection of RBAC roles, {@link UserRole}, convert to raw data format and load into ldap attribute set in preparation for ldap add.
     *
     * @param list  contains List of type {@link UserRole} targeted for adding to ldap.
     * @param attrs collection of ldap attributes containing RBAC role assignments in raw ldap format.
     */
    private void loadUserRoles( List<UserRole> list, LDAPAttributeSet attrs )
    {
        if ( list != null )
        {
            LDAPAttribute attr = null;
            LDAPAttribute attrNm = null;

            for ( UserRole userRole : list )
            {
                String szUserRole = userRole.getRawData();

                if ( attr == null )
                {
                    attr = new LDAPAttribute( GlobalIds.USER_ROLE_DATA, szUserRole );
                    attrNm = new LDAPAttribute( GlobalIds.USER_ROLE_ASSIGN, userRole.getName() );
                }
                else
                {
                    attr.addValue( szUserRole );
                    attrNm.addValue( userRole.getName() );
                }
            }

            if ( attr != null )
            {
                attrs.add( attr );
                attrs.add( attrNm );
            }
        }
    }


    /**
     * Given a User address, {@link Address}, load into ldap attribute set in preparation for ldap add.
     *
     * @param address  contains User address {@link Address} targeted for adding to ldap.
     * @param attrs collection of ldap attributes containing RBAC role assignments in raw ldap format.
     */
    private void loadAddress( Address address, LDAPAttributeSet attrs )
    {
        if ( address != null )
        {
            LDAPAttribute attr;

            if ( VUtil.isNotNullOrEmpty( address.getAddresses() ) )
            {
                for ( String val : address.getAddresses() )
                {
                    attr = new LDAPAttribute( POSTAL_ADDRESS, val );
                    attrs.add( attr );
                }
            }

            if ( VUtil.isNotNullOrEmpty( address.getCity() ) )
            {
                attr = new LDAPAttribute( L, address.getCity() );
                attrs.add( attr );
            }

            //if(VUtil.isNotNullOrEmpty(address.getCountry()))
            //{
            //    attr = new LDAPAttribute(GlobalIds.COUNTRY, address.getAddress1());
            //    attrs.add(attr);
            //}
            if ( VUtil.isNotNullOrEmpty( address.getPostalCode() ) )
            {
                attr = new LDAPAttribute( POSTAL_CODE, address.getPostalCode() );
                attrs.add( attr );
            }

            if ( VUtil.isNotNullOrEmpty( address.getPostOfficeBox() ) )
            {
                attr = new LDAPAttribute( POST_OFFICE_BOX, address.getPostOfficeBox() );
                attrs.add( attr );
            }

            if ( VUtil.isNotNullOrEmpty( address.getState() ) )
            {
                attr = new LDAPAttribute( STATE, address.getState() );
                attrs.add( attr );
            }

            if ( VUtil.isNotNullOrEmpty( address.getBuilding() ) )
            {
                attr = new LDAPAttribute( PHYSICAL_DELIVERY_OFFICE_NAME, address.getBuilding() );
                attrs.add( attr );
            }

            if ( VUtil.isNotNullOrEmpty( address.getDepartmentNumber() ) )
            {
                attr = new LDAPAttribute( DEPARTMENT_NUMBER, address.getDepartmentNumber() );
                attrs.add( attr );
            }

            if ( VUtil.isNotNullOrEmpty( address.getRoomNumber() ) )
            {
                attr = new LDAPAttribute( ROOM_NUMBER, address.getRoomNumber() );
                attrs.add( attr );
            }
        }
    }


    /**
     * Given an address, {@link Address}, load into ldap modification set in preparation for ldap modify.
     *
     * @param address contains entity of type {@link Address} targeted for updating into ldap.
     * @param mods contains ldap modification set contains attributes to be updated in ldap.
     */
    private void loadAddress( Address address, LDAPModificationSet mods )
    {
        LDAPAttribute attr;

        if ( address != null )
        {
            if ( VUtil.isNotNullOrEmpty( address.getAddresses() ) )
            {
                attr = new LDAPAttribute( POSTAL_ADDRESS );
                mods.add( LDAPModification.REPLACE, attr );

                for ( String val : address.getAddresses() )
                {
                    attr = new LDAPAttribute( POSTAL_ADDRESS, val );
                    mods.add( LDAPModification.ADD, attr );
                }
            }

            if ( VUtil.isNotNullOrEmpty( address.getCity() ) )
            {
                attr = new LDAPAttribute( L, address.getCity() );
                mods.add( LDAPModification.REPLACE, attr );
            }

            if ( VUtil.isNotNullOrEmpty( address.getPostalCode() ) )
            {
                attr = new LDAPAttribute( POSTAL_CODE, address.getPostalCode() );
                mods.add( LDAPModification.REPLACE, attr );
            }

            if ( VUtil.isNotNullOrEmpty( address.getPostOfficeBox() ) )
            {
                attr = new LDAPAttribute( POST_OFFICE_BOX, address.getPostOfficeBox() );
                mods.add( LDAPModification.REPLACE, attr );
            }

            if ( VUtil.isNotNullOrEmpty( address.getState() ) )
            {
                attr = new LDAPAttribute( STATE, address.getState() );
                mods.add( LDAPModification.REPLACE, attr );
            }

            if ( VUtil.isNotNullOrEmpty( address.getBuilding() ) )
            {
                attr = new LDAPAttribute( PHYSICAL_DELIVERY_OFFICE_NAME, address.getBuilding() );
                mods.add( LDAPModification.REPLACE, attr );
            }

            if ( VUtil.isNotNullOrEmpty( address.getDepartmentNumber() ) )
            {
                attr = new LDAPAttribute( DEPARTMENT_NUMBER, address.getDepartmentNumber() );
                mods.add( LDAPModification.REPLACE, attr );
            }

            if ( VUtil.isNotNullOrEmpty( address.getRoomNumber() ) )
            {
                attr = new LDAPAttribute( ROOM_NUMBER, address.getRoomNumber() );
                mods.add( LDAPModification.REPLACE, attr );
            }
        }
    }


    /**
     * Given an ldap entry containing organzationalPerson address information, convert to {@link Address}
     *
     * @param le     contains ldap entry to retrieve admin roles from.
     * @return entity of type {@link Address}.
     * @throws com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException in the event of ldap client error.
     */
    private Address unloadAddress( LDAPEntry le )
    {
        Address addr = new ObjectFactory().createAddress();
        List<String> pAddrs = getAttributes( le, POSTAL_ADDRESS );
        if ( pAddrs != null )
        {
            for ( String pAddr : pAddrs )
            {
                addr.setAddress( pAddr );
            }
        }
        addr.setCity( getAttribute( le, L ) );
        addr.setState( getAttribute( le, STATE ) );
        addr.setPostalCode( getAttribute( le, POSTAL_CODE ) );
        addr.setPostOfficeBox( getAttribute( le, POST_OFFICE_BOX ) );
        addr.setBuilding( getAttribute( le, PHYSICAL_DELIVERY_OFFICE_NAME ) );
        addr.setDepartmentNumber( getAttribute( le, DEPARTMENT_NUMBER ) );
        addr.setRoomNumber( getAttribute( le, ROOM_NUMBER ) );
        // todo: add support for country attribute
        //addr.setCountry(getAttribute(le, GlobalIds.COUNTRY));

        return addr;
    }


    /**
     * Given an ldap entry containing ARBAC roles assigned to user, retrieve the raw data and convert to a collection of {@link UserAdminRole}
     * including {@link us.jts.fortress.util.time.Constraint}.
     *
     * @param le     contains ldap entry to retrieve admin roles from.
     * @param userId attribute maps to {@link UserAdminRole#userId}.
     * @param contextId
     * @return List of type {@link UserAdminRole} containing admin roles assigned to a particular user.
     * @throws com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException in the event of ldap client error.
     */
    private List<UserAdminRole> unloadUserAdminRoles( LDAPEntry le, String userId, String contextId )
    {
        List<UserAdminRole> uRoles = null;
        List<String> roles = getAttributes( le, GlobalIds.USER_ADMINROLE_DATA );

        if ( roles != null )
        {
            long sequence = 0;
            uRoles = new ArrayList<>();

            for ( String raw : roles )
            {
                UserAdminRole ure = new ObjectFactory().createUserAdminRole();
                ure.load( raw, contextId );
                ure.setSequenceId( sequence++ );
                ure.setUserId( userId );
                uRoles.add( ure );
            }
        }

        return uRoles;
    }


    /**
     *
     * @param userId
     * @param contextId
     * @return
     */
    private String getDn( String userId, String contextId )
    {
        return GlobalIds.UID + "=" + userId + "," + getRootDn( contextId, GlobalIds.USER_ROOT );
    }


    /**
    * Given an ldap entry containing RBAC roles assigned to user, retrieve the raw data and convert to a collection of {@link UserRole}
    * including {@link us.jts.fortress.util.time.Constraint}.
    *
    * @param le     contains ldap entry to retrieve roles from.
    * @param userId attribute maps to {@link UserRole#userId}.
    * @param contextId
    * @return List of type {@link UserRole} containing RBAC roles assigned to a particular user.
    * @throws com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException in the event of ldap client error.
    */
    private List<UserRole> unloadUserRoles( LDAPEntry le, String userId, String contextId )
    {
        List<UserRole> uRoles = null;
        List<String> roles = getAttributes( le, GlobalIds.USER_ROLE_DATA );

        if ( roles != null )
        {
            long sequence = 0;
            uRoles = new ArrayList<>();

            for ( String raw : roles )
            {
                UserRole ure = new ObjectFactory().createUserRole();
                ure.load( raw, contextId );
                ure.setUserId( userId );
                ure.setSequenceId( sequence++ );
                uRoles.add( ure );
            }
        }

        return uRoles;
    }
}