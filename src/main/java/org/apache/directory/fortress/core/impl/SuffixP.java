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


import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.ValidationException;
import org.apache.directory.fortress.core.model.Suffix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.util.VUtil;


/**
 * Process module for the suffix or root node of Fortress directory structure. The suffix represents the topmost node in a 
 * directory information tree.  For example dc=companyName,dc=com.  The suffix data is passed using 
 * {@link org.apache.directory.fortress.core.model.Suffix} class.  This class does perform simple data validations.
 * The {@link org.apache.directory.fortress.core.ant.FortressAntTask#addSuffixes()} method calls the {@link #add} from this 
 * class during initial base loads. Removal {@link org.apache.directory.fortress.core.ant.FortressAntTask#deleteSuffixes()} 
 * is performed during regression tests and should never be executed targeting production directory systems.<br>
 * This class will accept {@link org.apache.directory.fortress.core.model.Suffix}, and forward on to it's corresponding DAO 
 * class {@link SuffixDAO} for add/delete of suffix.
 * <p>
 * Class will throw {@link org.apache.directory.fortress.core.SecurityException} to caller in the event of security policy, 
 * data constraint violation or system error internal to DAO object. This class will forward DAO exceptions 
 * ({@link org.apache.directory.fortress.core.FinderException}, {@link org.apache.directory.fortress.core.CreateException},
 * {@link org.apache.directory.fortress.core.UpdateException},{@link org.apache.directory.fortress.core.RemoveException}),
 * or {@link org.apache.directory.fortress.core.ValidationException} as 
 * {@link org.apache.directory.fortress.core.SecurityException}s with appropriate error id from {@link GlobalErrIds}.
 * <p style="font-size:2em; color:red;">
 * The {@link #delete} method in this class is destructive as it will remove all nodes below the suffix using recursive 
 * delete function.<br>
 * Extreme care should be taken during execution to ensure target dn is correct and permanent removal of data is intended.
 * There is no 'undo' for this operation.
 * <p>
 * Simple error mapping is performed in {@link #validate} class.
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class SuffixP
{
    private static final String CLS_NM = SuffixP.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );


    /**
     * Add a new suffix to the Directory Information Tree (DIT).  After added the
     * node will be listed in domain component format, i.e. dc=companyName, dc=com, or dc=orgName, dc=org.
     *
     * @param suffix contains the dc name and top level dc for target node.
     * @throws org.apache.directory.fortress.core.SecurityException in event of validation or system error.
     */
    public final void add( Suffix suffix )
        throws SecurityException
    {
        validate( suffix );
        SuffixDAO sDao = new SuffixDAO();
        sDao.create( suffix );
    }


    /**
     * Remove the suffix along with descendant nodes.  This is a destructive method which will remove all DIT nodes under
     * the specified.
     * <p>
     * <p style="font-size:2em; color:red;">
     * This method is destructive and will remove all nodes below.<BR>
     * Extreme care should be taken during execution to ensure target dn is correct and permanent removal of data is intended.  There is no
     * 'undo' for this operation.
     *
     * @param suffix contains the dc name and top level dc for target node.
     * @throws SecurityException in event of validation or system error.
     */
    public final void delete( Suffix suffix )
        throws SecurityException
    {
        validate( suffix );
        SuffixDAO sDao = new SuffixDAO();
        sDao.remove( suffix );
    }


    /**
     * Method will perform simple validations to ensure the integrity of the {@link Suffix} entity targeted for insertion
     * or deletion in directory.
     *
     * @param entity contains the enum type to validate
     * @throws org.apache.directory.fortress.core.SecurityException thrown in the event the attribute is null.
     */
    private void validate( Suffix entity )
        throws SecurityException
    {
        if ( entity.getName().length() > GlobalIds.OU_LEN )
        {
            String name = entity.getName();
            String error = "validate name [" + name + "] invalid length [" + entity.getName().length() + "]";
            LOG.warn( error );
            throw new ValidationException( GlobalErrIds.SUFX_NAME_INVLD, error );
        }
        if ( StringUtils.isEmpty( entity.getName() ) )
        {
            String error = "validate name validation failed, null or empty value";
            LOG.warn( error );
            throw new ValidationException( GlobalErrIds.SUFX_NAME_NULL, error );
        }
        if ( entity.getDc().length() > GlobalIds.OU_LEN )
        {
            String name = entity.getName();
            String error = "validate dc [" + name + "] invalid length [" + entity.getName().length() + "]";
            LOG.warn( error );
            throw new ValidationException( GlobalErrIds.SUFX_DCTOP_INVLD, error );
        }
        if ( StringUtils.isEmpty( entity.getDc() ) )
        {
            String error = "validate dc validation failed, null or empty value";
            LOG.warn( error );
            throw new ValidationException( GlobalErrIds.SUFX_DCTOP_NULL, error );
        }
        VUtil.safeText( entity.getDescription(), GlobalIds.DESC_LEN );
        if ( StringUtils.isNotEmpty( entity.getDescription() ) )
        {
            VUtil.description( entity.getDescription() );
        }
    }
}
