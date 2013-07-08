/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac.dao.unboundid;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.jts.fortress.CreateException;
import us.jts.fortress.FinderException;
import us.jts.fortress.GlobalErrIds;
import us.jts.fortress.GlobalIds;
import us.jts.fortress.ObjectFactory;
import us.jts.fortress.RemoveException;
import us.jts.fortress.UpdateException;
import us.jts.fortress.ldap.DataProvider;
import us.jts.fortress.rbac.Graphable;
import us.jts.fortress.rbac.OrgUnit;
import us.jts.fortress.rbac.PsoUtil;
import us.jts.fortress.rbac.UsoUtil;

import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttribute;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPAttributeSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPConnection;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPEntry;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPException;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModification;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPModificationSet;
import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPSearchResults;


/**
 * This class provides dataaccess to the OrgUnit datasets in LDAP.
 * <p/>
 * The OrgUnitDAO maintains the following structural and aux object classes:
 * <h4>1. organizationalUnit Structural Object Class is used to store basic attributes like ou and description</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 2.5.6.5 NAME 'organizationalUnit'</code>
 * <li> <code>DESC 'RFC2256: an organizational unit'</code>
 * <li> <code>SUP top STRUCTURAL</code>
 * <li> <code>MUST ou</code>
 * <li> <code>MAY ( userPassword $ searchGuide $ seeAlso $ businessCategory $</code>
 * <li> <code>x121Address $ registeredAddress $ destinationIndicator $</code>
 * <li> <code>preferredDeliveryMethod $ telexNumber $ teletexTerminalIdentifier $</code>
 * <li> <code>telephoneNumber $ internationaliSDNNumber $</code>
 * <li> <code>facsimileTelephoneNumber $ street $ postOfficeBox $ postalCode $</code>
 * <li> <code>postalAddress $ physicalDeliveryOfficeName $ st $ l $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>2. ftOrgUnit Structural objectclass is used to store the OrgUnit internal id</h4>
 * <ul>                                                              us.jts.fortress.arbac.
 * <li>  ------------------------------------------
 * <li> <code> objectclass	( 1.3.6.1.4.1.38088.2.6</code>
 * <li> <code>NAME 'ftOrgUnit'</code>
 * <li> <code>DESC 'Fortress OrgUnit Class'</code>
 * <li> <code>SUP organizationalunit</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId ) )</code>
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
 * @created September 18, 2010
 */
public final class OrgUnitDAO extends DataProvider implements us.jts.fortress.rbac.dao.OrgUnitDAO
{
    private static final String CLS_NM = OrgUnitDAO.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static final String ORGUNIT_OBJECT_CLASS_NM = "ftOrgUnit";

    private static final String ORGUNIT_OBJ_CLASS[] =
        {
            GlobalIds.TOP, ORGUNIT_OBJECT_CLASS_NM, GlobalIds.FT_MODIFIER_AUX_OBJECT_CLASS_NAME
    };
    private static final String[] ORGUNIT_ATRS =
        {
            GlobalIds.FT_IID, GlobalIds.OU, GlobalIds.DESC, GlobalIds.PARENT_NODES
    };

    private static final String[] ORGUNIT_ATR =
        {
            GlobalIds.OU
    };


    /**
     * Package private default constructor.
     */
    public OrgUnitDAO()
    {
    }


    /**
     * @param entity
     * @return
     * @throws us.jts.fortress.CreateException
     *
     */
    public final OrgUnit create( OrgUnit entity )
        throws CreateException
    {
        LDAPConnection ld = null;
        String dn = getDn( entity );
        try
        {
            LDAPAttributeSet attrs = new LDAPAttributeSet();
            attrs.add( createAttributes( GlobalIds.OBJECT_CLASS, ORGUNIT_OBJ_CLASS ) );
            entity.setId();
            attrs.add( createAttribute( GlobalIds.FT_IID, entity.getId() ) );
            if ( entity.getDescription() != null && entity.getDescription().length() > 0 )
                attrs.add( createAttribute( GlobalIds.DESC, entity.getDescription() ) );
            // organizational name requires OU attribute:
            attrs.add( createAttribute( GlobalIds.OU, entity.getName() ) );

            // These multi-valued attributes are optional.  The utility function will return quietly if no items are loaded into collection:
            loadAttrs( entity.getParents(), attrs, GlobalIds.PARENT_NODES );

            LDAPEntry myEntry = new LDAPEntry( dn, attrs );
            ld = getAdminConnection();
            add( ld, myEntry, entity );
        }
        catch ( LDAPException e )
        {
            String error = "create orgUnit name [" + entity.getName() + "] type [" + entity.getType()
                + "] root [" + dn + "] caught LDAPException=" + e;
            int errCode;
            if ( entity.getType() == OrgUnit.Type.PERM )
            {
                errCode = GlobalErrIds.ORG_ADD_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_ADD_FAILED_USER;

            }
            throw new CreateException( errCode, error, e );
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
     * @throws us.jts.fortress.UpdateException
     *
     */
    public final OrgUnit update( OrgUnit entity )
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn( entity );
        try
        {
            LDAPModificationSet mods = new LDAPModificationSet();
            if ( entity.getDescription() != null && entity.getDescription().length() > 0 )
            {
                LDAPAttribute desc = new LDAPAttribute( GlobalIds.DESC, entity.getDescription() );
                mods.add( LDAPModification.REPLACE, desc );
            }
            loadAttrs( entity.getParents(), mods, GlobalIds.PARENT_NODES );
            if ( mods.size() > 0 )
            {
                ld = getAdminConnection();
                modify( ld, dn, mods, entity );
            }
        }
        catch ( LDAPException e )
        {
            String error = "update orgUnit name [" + entity.getName() + "] type [" + entity.getType()
                + "] root [" + dn + "] caught LDAPException=" + e;
            int errCode;
            if ( entity.getType() == OrgUnit.Type.PERM )
            {
                errCode = GlobalErrIds.ORG_UPDATE_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_UPDATE_FAILED_USER;
            }

            throw new UpdateException( errCode, error, e );
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
    public final void deleteParent( OrgUnit entity )
        throws UpdateException
    {
        LDAPConnection ld = null;
        String dn = getDn( entity );
        try
        {
            LDAPModificationSet mods = new LDAPModificationSet();
            LDAPAttribute occupant = new LDAPAttribute( GlobalIds.PARENT_NODES );
            mods.add( LDAPModification.DELETE, occupant );
            ld = getAdminConnection();
            modify( ld, dn, mods, entity );
        }
        catch ( LDAPException e )
        {
            String error = "deleteParent orgUnit name [" + entity.getName() + "] type [" + entity.getType()
                + "] root [" + dn + "] caught LDAPException=" + e;
            int errCode;
            if ( entity.getType() == OrgUnit.Type.PERM )
            {
                errCode = GlobalErrIds.ORG_REMOVE_PARENT_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_REMOVE_PARENT_FAILED_USER;
            }

            throw new UpdateException( errCode, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
    }


    /**
     * @param entity
     * @return
     * @throws us.jts.fortress.RemoveException
     *
     */
    public final OrgUnit remove( OrgUnit entity )
        throws RemoveException
    {
        LDAPConnection ld = null;
        String dn = getDn( entity );
        try
        {
            ld = getAdminConnection();
            delete( ld, dn, entity );
        }
        catch ( LDAPException e )
        {
            String error = "remove orgUnit name [" + entity.getName() + "] type [" + entity.getType()
                + "] root [" + dn + "] caught LDAPException=" + e;
            int errCode;
            if ( entity.getType() == OrgUnit.Type.PERM )
            {
                errCode = GlobalErrIds.ORG_DELETE_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_DELETE_FAILED_USER;
            }

            throw new RemoveException( errCode, error, e );
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
     * @throws FinderException
     *
     */
    public final OrgUnit findByKey( OrgUnit entity )
        throws FinderException
    {
        OrgUnit oe = null;
        LDAPConnection ld = null;
        String dn = getDn( entity );
        try
        {
            ld = getAdminConnection();
            LDAPEntry findEntry = read( ld, dn, ORGUNIT_ATRS );
            if ( findEntry == null )
            {
                String warning = "findByKey orgUnit name [" + entity.getName() + "] type ["
                    + entity.getType() + "] COULD NOT FIND ENTRY for dn [" + dn + "]";
                int errCode;
                if ( entity.getType() == OrgUnit.Type.PERM )
                {
                    errCode = GlobalErrIds.ORG_NOT_FOUND_PERM;
                }
                else
                {
                    errCode = GlobalErrIds.ORG_NOT_FOUND_USER;
                }
                throw new FinderException( errCode, warning );
            }
            oe = getEntityFromLdapEntry( findEntry, 0, entity.getContextId() );
        }
        catch ( LDAPException e )
        {
            if ( e.getLDAPResultCode() == LDAPException.NO_SUCH_OBJECT )
            {
                String warning = "findByKey orgUnit name [" + entity.getName() + "] type ["
                    + entity.getType() + "] COULD NOT FIND ENTRY for dn [" + dn + "]";
                int errCode;
                if ( entity.getType() == OrgUnit.Type.PERM )
                {
                    errCode = GlobalErrIds.ORG_NOT_FOUND_PERM;
                }
                else
                {
                    errCode = GlobalErrIds.ORG_NOT_FOUND_USER;
                }
                throw new FinderException( errCode, warning );
            }
            else
            {
                String error = "findByKey orgUnitName [" + entity.getName() + "] type [" + entity.getType()
                    + "] dn [" + dn + "] caught LDAPException=" + e;
                int errCode;
                if ( entity.getType() == OrgUnit.Type.PERM )
                {
                    errCode = GlobalErrIds.ORG_READ_FAILED_PERM;
                }
                else
                {
                    errCode = GlobalErrIds.ORG_READ_FAILED_USER;
                }
                throw new FinderException( errCode, error, e );
            }
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return oe;
    }


    /**
     * @param orgUnit
     * @return
     * @throws us.jts.fortress.FinderException
     *
     */
    public final List<OrgUnit> findOrgs( OrgUnit orgUnit )
        throws FinderException
    {
        List<OrgUnit> orgUnitList = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String orgUnitRoot = getOrgRoot( orgUnit );
        try
        {
            String searchVal = encodeSafeText( orgUnit.getName(), GlobalIds.ROLE_LEN );
            String filter = GlobalIds.FILTER_PREFIX + ORGUNIT_OBJECT_CLASS_NM + ")("
                + GlobalIds.OU + "=" + searchVal + "*))";
            ld = getAdminConnection();
            searchResults = search( ld, orgUnitRoot,
                LDAPConnection.SCOPE_ONE, filter, ORGUNIT_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;
            while ( searchResults.hasMoreElements() )
            {
                orgUnitList.add( getEntityFromLdapEntry( searchResults.next(), sequence++, orgUnit.getContextId() ) );
            }
        }
        catch ( LDAPException e )
        {
            String error = "findOrgs search val [" + orgUnit.getName() + "] type [" + orgUnit.getType()
                + "] root [" + orgUnitRoot + "] caught LDAPException=" + e;
            int errCode;
            if ( orgUnit.getType() == OrgUnit.Type.PERM )
            {
                errCode = GlobalErrIds.ORG_SEARCH_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_SEARCH_FAILED_USER;
            }

            throw new FinderException( errCode, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return orgUnitList;
    }


    /**
     *
     * @param orgUnit
     * @return
     * @throws FinderException
     */
    public final Set<String> getOrgs( OrgUnit orgUnit )
        throws FinderException
    {
        Set<String> ouSet = new TreeSet<>( String.CASE_INSENSITIVE_ORDER );
        LDAPConnection ld = null;
        String orgUnitRoot = getOrgRoot( orgUnit );
        try
        {
            String filter = "(objectclass=" + ORGUNIT_OBJECT_CLASS_NM + ")";
            ld = getAdminConnection();
            LDAPSearchResults searchResults = search( ld, orgUnitRoot,
                LDAPConnection.SCOPE_ONE, filter, ORGUNIT_ATR, false, GlobalIds.BATCH_SIZE );
            while ( searchResults.hasMoreElements() )
            {
                ouSet.add( getAttribute( searchResults.next(), GlobalIds.OU ) );
            }
        }
        catch ( LDAPException e )
        {
            String error = "getOrgs type [" + orgUnit.getType() + "] root [" + orgUnitRoot
                + "] caught LDAPException=" + e;
            int errCode;
            if ( orgUnit.getType() == OrgUnit.Type.PERM )
            {
                errCode = GlobalErrIds.ORG_GET_FAILED_PERM;
            }
            else
            {
                errCode = GlobalErrIds.ORG_GET_FAILED_USER;
            }
            throw new FinderException( errCode, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return ouSet;
    }


    /**
      *
      * @param orgUnit
      * @return
      * @throws FinderException
      */
    public final List<Graphable> getAllDescendants( OrgUnit orgUnit )
        throws FinderException
    {
        String orgUnitRoot = getOrgRoot( orgUnit );
        String[] DESC_ATRS =
            { GlobalIds.OU, GlobalIds.PARENT_NODES };
        List<Graphable> descendants = new ArrayList<>();
        LDAPConnection ld = null;
        LDAPSearchResults searchResults;
        String filter = null;
        try
        {
            filter = GlobalIds.FILTER_PREFIX + ORGUNIT_OBJECT_CLASS_NM + ")("
                + GlobalIds.PARENT_NODES + "=*))";
            ld = getAdminConnection();
            searchResults = search( ld, orgUnitRoot,
                LDAPConnection.SCOPE_ONE, filter, DESC_ATRS, false, GlobalIds.BATCH_SIZE );
            long sequence = 0;
            while ( searchResults.hasMoreElements() )
            {
                descendants.add( unloadDescendants( searchResults.next(), sequence++, orgUnit.getContextId() ) );
            }
        }
        catch ( LDAPException e )
        {
            String error = "getAllDescendants filter [" + filter + "] caught LDAPException="
                + e.getLDAPResultCode() + " msg=" + e.getMessage();
            throw new FinderException( GlobalErrIds.ARLE_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection( ld );
        }
        return descendants;
    }


    /**
     * @param orgUnit
     * @return
     */
    private String getDn( OrgUnit orgUnit )
    {
        String dn = null;
        switch ( orgUnit.type )
        {
            case USER:
                dn = GlobalIds.OU + "=" + orgUnit.getName() + ","
                    + getRootDn( orgUnit.getContextId(), GlobalIds.OSU_ROOT );
                break;
            case PERM:
                dn = GlobalIds.OU + "=" + orgUnit.getName() + ","
                    + getRootDn( orgUnit.getContextId(), GlobalIds.PSU_ROOT );
                break;
            default:
                String warning = "getDn invalid type";
                LOG.warn( warning );
                break;
        }
        return dn;
    }


    /**
     *
     * @param orgUnit
     * @return
     */
    private String getOrgRoot( OrgUnit orgUnit )
    {
        String dn = null;
        switch ( orgUnit.type )
        {
            case USER:
                dn = getRootDn( orgUnit.getContextId(), GlobalIds.OSU_ROOT );
                break;
            case PERM:
                dn = getRootDn( orgUnit.getContextId(), GlobalIds.PSU_ROOT );
                break;
            default:
                String warning = "getOrgRootDn invalid type";
                LOG.warn( warning );
                break;
        }
        return dn;
    }


    /**
    *
    * @param le
    * @param sequence
    * @param contextId
    * @return
    * @throws LDAPException
    */
    private Graphable unloadDescendants( LDAPEntry le, long sequence, String contextId )
    {
        OrgUnit entity = new ObjectFactory().createOrgUnit();
        entity.setSequenceId( sequence );
        entity.setName( getAttribute( le, GlobalIds.OU ) );
        entity.setParents( getAttributeSet( le, GlobalIds.PARENT_NODES ) );
        return entity;
    }


    /**
     *
     * @param le
     * @param sequence
     * @param contextId
     * @return
     * @throws LDAPException
     */
    private OrgUnit getEntityFromLdapEntry( LDAPEntry le, long sequence, String contextId )
    {
        OrgUnit entity = new ObjectFactory().createOrgUnit();
        entity.setSequenceId( sequence );
        entity.setId( getAttribute( le, GlobalIds.FT_IID ) );
        entity.setName( getAttribute( le, GlobalIds.OU ) );
        entity.setDescription( getAttribute( le, GlobalIds.DESC ) );
        String dn = le.getDN();
        if ( dn.contains( getRootDn( contextId, GlobalIds.PSU_ROOT ) ) )
        {
            entity.setType( OrgUnit.Type.PERM );
            //entity.setParents(PsoUtil.getParents(entity.getName().toUpperCase(), contextId));
            entity.setChildren( PsoUtil.getChildren( entity.getName().toUpperCase(), contextId ) );
        }
        else if ( dn.contains( getRootDn( contextId, GlobalIds.OSU_ROOT ) ) )
        {
            entity.setType( OrgUnit.Type.USER );
            //entity.setParents(UsoUtil.getParents(entity.getName().toUpperCase(), contextId));
            entity.setChildren( UsoUtil.getChildren( entity.getName().toUpperCase(), contextId ) );
        }
        entity.setParents( getAttributeSet( le, GlobalIds.PARENT_NODES ) );
        return entity;
    }
}