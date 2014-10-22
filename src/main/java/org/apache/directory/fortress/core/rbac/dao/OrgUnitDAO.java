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


import java.util.List;
import java.util.Set;

import org.apache.directory.fortress.core.CreateException;
import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.RemoveException;
import org.apache.directory.fortress.core.UpdateException;
import org.apache.directory.fortress.core.rbac.Graphable;
import org.apache.directory.fortress.core.rbac.OrgUnit;


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
 * <ul>                                                              org.apache.directory.fortress.arbac.
 * <li>  ------------------------------------------
 * <li> <code> objectclass  ( 1.3.6.1.4.1.38088.2.6</code>
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
 * @author Emmanuel Lecharny
 */
public interface OrgUnitDAO
{
    /**
     * @param entity
     * @return
     * @throws org.apache.directory.fortress.core.CreateException
     *
     */
    OrgUnit create( OrgUnit entity ) throws CreateException;


    /**
     * @param entity
     * @return
     * @throws org.apache.directory.fortress.core.UpdateException
     *
     */
    OrgUnit update( OrgUnit entity ) throws UpdateException;


    /**
     * @param entity
     * @throws org.apache.directory.fortress.core.UpdateException
     *
     */
    void deleteParent( OrgUnit entity ) throws UpdateException;


    /**
     * @param entity
     * @return
     * @throws org.apache.directory.fortress.core.RemoveException
     *
     */
    OrgUnit remove( OrgUnit entity ) throws RemoveException;


    /**
     * @param entity
     * @return
     * @throws FinderException
     *
     */
    OrgUnit findByKey( OrgUnit entity ) throws FinderException;


    /**
     * @param orgUnit
     * @return
     * @throws org.apache.directory.fortress.core.FinderException
     *
     */
    List<OrgUnit> findOrgs( OrgUnit orgUnit ) throws FinderException;


    /**
     *
     * @param orgUnit
     * @return
     * @throws FinderException
     */
    Set<String> getOrgs( OrgUnit orgUnit ) throws FinderException;


    /**
      *
      * @param orgUnit
      * @return
      * @throws FinderException
      */
    List<Graphable> getAllDescendants( OrgUnit orgUnit ) throws FinderException;
}
