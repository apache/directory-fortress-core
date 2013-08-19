/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.rbac.dao;


import java.util.List;
import java.util.Set;

import us.jts.fortress.CreateException;
import us.jts.fortress.FinderException;
import us.jts.fortress.RemoveException;
import us.jts.fortress.UpdateException;
import us.jts.fortress.rbac.Role;
import us.jts.fortress.rbac.SDSet;


/**
 * This class performs persistence on the RBAC Static Separation of Duties and Dynamic Separation of Duties data sets.
 * <p/>
 * The Fortress SDSet entity is a composite of the following other Fortress structural and aux object classes:
 * <h4>1. organizationalRole Structural Object Class is used to store basic attributes like cn and description</h4>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>objectclass ( 2.5.6.8 NAME 'organizationalRole'</code>
 * <li> <code>DESC 'RFC2256: an organizational role'</code>
 * <li> <code>SUP top STRUCTURAL</code>
 * <li> <code>MUST cn</code>
 * <li> <code>MAY ( x121Address $ registeredAddress $ destinationIndicator $</code>
 * <li> <code>preferredDeliveryMethod $ telexNumber $ teletexTerminalIdentifier $</code>
 * <li> <code>telephoneNumber $ internationaliSDNNumber $ facsimileTelephoneNumber $</code>
 * <li> <code>seeAlso $ roleOccupant $ preferredDeliveryMethod $ street $</code>
 * <li> <code>postOfficeBox $ postalCode $ postalAddress $</code>
 * <li> <code>physicalDeliveryOfficeName $ ou $ st $ l $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <h4>2. The RBAC Separation of Duties</h4>
 * <ul>
 * <li>  ---Static Separation of Duties Set-------
 * <li> <code>objectclass   ( 1.3.6.1.4.1.38088.2.4</code>
 * <li> <code>NAME 'ftSSDSet'</code>
 * <li> <code>DESC 'Fortress Role Static Separation of Duty Set Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftSetName $ ftSetCardinality )</code>
 * <li> <code>MAY ( ftRoles $ description ) )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * OR
 * <h4>Dynamic Separation of Duties Set</h4>
 * <ul>
 * <li>
 * <li> <code>objectclass   ( 1.3.6.1.4.1.38088.2.5</code>
 * <li> <code>NAME 'ftDSDSet'</code>
 * <li> <code>DESC 'Fortress Role Dynamic Separation of Duty Set Object Class'</code>
 * <li> <code>SUP organizationalrole</code>
 * <li> <code>STRUCTURAL</code>
 * <li> <code>MUST ( ftId $ ftSetName $ ftSetCardinality )</code>
 * <li> <code>MAY ( ftRoles $ description ) )</code>
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
 * <p/>
 *
 * @author Shawn McKinney
 */
public interface SdDAO
{
    /**
     * @param entity
     * @return
     * @throws us.jts.fortress.CreateException
     */
    SDSet create( SDSet entity ) throws CreateException;


    /**
     * @param entity
     * @return
     * @throws us.jts.fortress.UpdateException
     */
    SDSet update( SDSet entity ) throws UpdateException;


    /**
     * @param entity
     * @throws us.jts.fortress.RemoveException
     */
    SDSet remove( SDSet entity ) throws RemoveException;


    /**
     * @param sdSet
     * @return
     * @throws FinderException
     */
    SDSet getSD( SDSet sdSet ) throws FinderException;


    /**
     * Given an SSD name and type, find matching object in the directory.
     * @param sdset requires name and type.
     * @return List of matching SDSets.
     * @throws us.jts.fortress.FinderException
     */
    List<SDSet> search( SDSet sdset ) throws FinderException;


    /**
     * @param role
     * @return
     * @throws us.jts.fortress.FinderException
     */
    List<SDSet> search( Role role, SDSet.SDType type ) throws FinderException;


    /**
     * @param roles
     * @param sdSet
     * @return
     * @throws us.jts.fortress.FinderException
     */
    Set<SDSet> search( Set<String> roles, SDSet sdSet ) throws FinderException;
}
