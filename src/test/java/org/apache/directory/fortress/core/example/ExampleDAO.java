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
package org.apache.directory.fortress.core.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.apache.directory.fortress.core.model.ConstraintUtil;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.slf4j.LoggerFactory;

public class ExampleDAO extends LdapDataProvider

{
    private static final String CLS_NM = ExampleDAO.class.getName();
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger( CLS_NM );
    protected final static String[] EXAMPLE_ATRS = {
        GlobalIds.FT_IID, EIds.EXAMPLE_NM, SchemaConstants.DESCRIPTION_AT, GlobalIds.CONSTRAINT
    };

    /**
     * @param entity
     * @return
     * @throws org.apache.directory.fortress.core.CreateException
     *
     */
    public Example create(Example entity)
        throws CreateException
    {
        LdapConnection ld = null;
        String dn = SchemaConstants.CN_AT + "=" + entity.getName() + "," + Config.getInstance().getProperty(EIds.EXAMPLE_ROOT);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("create dn [" + dn + "]");
        }
        try
        {

            /*
        public class Example
                implements Constraint, java.io.Serializable
        {
            private String id;          // this maps to oamId
            private String name;          // this is oamRoleName
            private String description; // this is description
            private String dn;          // this attribute is automatically saved to each ldap record.
            private String beginTime;     // this attribute is oamBeginTime
            private String endTime;        // this attribute is oamEndTime
            private String beginDate;    // this attribute is oamBeginDate
            private String endDate;        // this attribute is oamEndDate
            private String beginLockDate;// this attribute is oamBeginLockDate
            private String endLockDate;    // this attribute is oamEndLockDate
            private String dayMask;        // this attribute is oamDayMask
            private int timeout;        // this attribute is oamTimeOut
            */

            ld = getAdminConnection();
            Entry entry = new DefaultEntry( dn );
            entry.add( createAttributes( SchemaConstants.OBJECT_CLASS_AT, EIds.EXAMPLE_OBJ_CLASS ) );

            entity.setId();

            entry.add( GlobalIds.FT_IID, entity.getId() );

            entry.add( EIds.EXAMPLE_NM, entity.getName() );

            if (entity.getDescription() != null && entity.getDescription().length() > 0)
                entry.add( SchemaConstants.DESCRIPTION_AT, entity.getDescription() );

            // organizational name requires CN attribute:
            entry.add( SchemaConstants.CN_AT, entity.getName() );

            //AttrHelper.loadTemporalAttrs(entity, attrs);
            entity.setName("EXAMPLE");
            entry.add( GlobalIds.CONSTRAINT, ConstraintUtil.setConstraint( entity ) );
            add(ld, entry);
        }
        catch (LdapException e)
        {
            String error = "create [" + entity.getName() + "] caught LDAPException=" + e;
            LOG.error(error);
            throw new CreateException(EErrIds.EXAMPLE_ADD_FAILED, error);
        }
        finally
        {
            closeAdminConnection(ld);
        }
        return entity;
    }


    /**
     * @param entity
     * @return
     * @throws org.apache.directory.fortress.core.UpdateException
     *
     */
    public Example update(Example entity)
        throws UpdateException
    {
        LdapConnection ld = null;
        String dn = SchemaConstants.CN_AT + "=" + entity.getName() + "," + Config.getInstance().getProperty( EIds.EXAMPLE_ROOT );
        if (LOG.isDebugEnabled())
        {
            LOG.debug("update dn [" + dn + "]");
        }
        try
        {
            ld = getAdminConnection();
            List<Modification> mods = new ArrayList<Modification>();
            if (entity.getDescription() != null && entity.getDescription().length() > 0)
            {
                mods.add( new DefaultModification( ModificationOperation.REPLACE_ATTRIBUTE, SchemaConstants.DESCRIPTION_AT, entity.getDescription() ) );

            }

            String szRawData = ConstraintUtil.setConstraint( entity );
            if (szRawData != null && szRawData.length() > 0)
            {
                mods.add( new DefaultModification( ModificationOperation.REPLACE_ATTRIBUTE, GlobalIds.CONSTRAINT, szRawData ) );
            }
            if (mods.size() > 0)
            {
                modify(ld, dn, mods);
            }
        }
        catch (LdapException e)
        {
            String error = "update [" + entity.getName() + "] caught LDAPException=" + e;
            LOG.error(error);
            throw new UpdateException(EErrIds.EXAMPLE_UPDATE_FAILED, error);
        }
        finally
        {
            closeAdminConnection(ld);
        }
        return entity;
    }


    /**
     * @param name
     * @throws org.apache.directory.fortress.core.RemoveException
     *
     */
    public void remove(String name)
        throws RemoveException
    {
        LdapConnection ld = null;
        String dn = SchemaConstants.CN_AT + "=" + name + "," + Config.getInstance().getProperty(EIds.EXAMPLE_ROOT);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("remove dn [" + dn + "]");
        }
        try
        {
            ld = getAdminConnection();
            delete(ld, dn);
        }
        catch (LdapException e)
        {
            String error = "remove [" + name + "] caught LDAPException=" + e;
            LOG.error(error);
            throw new RemoveException(EErrIds.EXAMPLE_DELETE_FAILED, error);
        }
        finally
        {
            closeAdminConnection(ld);
        }
    }


    /**
     * @param name
     * @return
     * @throws org.apache.directory.fortress.core.FinderException
     *
     */
    public Example findByKey(String name)
        throws FinderException
    {
        Example entity = null;
        LdapConnection ld = null;
        String dn = SchemaConstants.CN_AT + "=" + name + "," + Config.getInstance().getProperty(EIds.EXAMPLE_ROOT);
        if (LOG.isDebugEnabled())
        {
            LOG.debug("findByKey dn [" + dn + "]");
        }
        try
        {
            ld = getAdminConnection();
            Entry findEntry = read( ld, dn, EXAMPLE_ATRS );

            entity = getEntityFromLdapEntry(findEntry);
            if (entity == null)
            {
                String error = "findByKey could not find entry for example name [" + name + "]";
                LOG.error(error);
                throw new FinderException(EErrIds.EXAMPLE_NOT_FOUND, error);
            }
        }
        catch ( LdapNoSuchObjectException e )
        {
            String error = "findByKey COULD NOT FIND ENTRY for example name [" + name + "]";
            throw new FinderException( GlobalErrIds.SSD_NOT_FOUND, error );
        }
        catch (LdapException e)
        {
            String error = "findByKey name [" + name + "] caught LDAPException=" + e;
            LOG.warn(error);
            throw new FinderException(EErrIds.EXAMPLE_READ_FAILED, error);
        }
        finally
        {
            closeAdminConnection(ld);
        }
        return entity;
    }


    /**
     * @param searchVal
     * @return
     * @throws org.apache.directory.fortress.core.FinderException
     *
     */
    public List<Example> findExamples(String searchVal)
        throws FinderException
    {
        List<Example> exampleList = new ArrayList<>();
        LdapConnection ld = null;
        String exampleRoot = Config.getInstance().getProperty( EIds.EXAMPLE_ROOT );

        if (LOG.isDebugEnabled())
        {
            LOG.debug("findExamples: " + EIds.EXAMPLE_ROOT + " [" + exampleRoot + "]");
        }
        try
        {
            searchVal = encodeSafeText(searchVal, GlobalIds.ROLE_LEN);
            ld = getAdminConnection();
            String filter = GlobalIds.FILTER_PREFIX + Arrays.toString(EIds.EXAMPLE_OBJ_CLASS) + ")("
                + EIds.EXAMPLE_NM + "=" + searchVal + "*))";
            SearchCursor searchResults = search( ld, exampleRoot,
                SearchScope.SUBTREE, filter, EXAMPLE_ATRS, false, Config.getInstance().getInt(GlobalIds.CONFIG_LDAP_MAX_BATCH_SIZE, Config.getInstance().getInt(GlobalIds.CONFIG_LDAP_MAX_BATCH_SIZE, GlobalIds.BATCH_SIZE ) ));
            while ( searchResults.next() )
            {
                exampleList.add(getEntityFromLdapEntry(searchResults.getEntry()));
            }
        }
        catch (LdapException e)
        {
            String error = "findExamples caught LDAPException=" + e;
            LOG.warn(error);
            throw new FinderException(EErrIds.EXAMPLE_SEARCH_FAILED, error);
        }
        catch ( CursorException e )
        {
            String error = "findExamples caught CursorException=" + e;
            throw new FinderException( EErrIds.EXAMPLE_SEARCH_FAILED, error, e );
        }
        finally
        {
            closeAdminConnection(ld);
        }
        return exampleList;
    }


    /**
     * @param le
     * @return
     */
    private Example getEntityFromLdapEntry(Entry le) throws LdapInvalidAttributeValueException
    {
        Example entity = new Example();
        entity.setId( getAttribute( le, GlobalIds.FT_IID ) );
        entity.setName(getAttribute(le, EIds.EXAMPLE_NM));
        entity.setDescription(getAttribute(le, SchemaConstants.DESCRIPTION_AT));
        unloadTemporal(le, entity);
        return entity;
    }
}

