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
package org.apache.directory.fortress.core.util.time;


import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.CfgException;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.model.ObjectFactory;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.ValidationException;
import org.apache.directory.fortress.core.util.ClassUtil;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.model.UserRole;
import org.apache.directory.fortress.core.model.Warning;
import org.apache.directory.fortress.core.util.VUtil;
import org.apache.directory.fortress.core.util.ObjUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;


/**
 * This class contains utilities for temporal constraint processing that are used by Fortress internal.  All of the methods are static and the class
 * is thread safe.
 * The Validators are configured via properties set in Fortress cfg:
 * <p/>
 * <h4> Validators supported include</h4>
 * <ol>
 * <li>{@link org.apache.directory.fortress.core.GlobalIds#VALIDATOR_PROPS}0={@link Date}</li>
 * <li>{@link org.apache.directory.fortress.core.GlobalIds#VALIDATOR_PROPS}1={@link LockDate}</li>
 * <li>{@link org.apache.directory.fortress.core.GlobalIds#VALIDATOR_PROPS}2={@link Timeout}</li>
 * <li>{@link org.apache.directory.fortress.core.GlobalIds#VALIDATOR_PROPS}3={@link ClockTime}</li>
 * <li>{@link org.apache.directory.fortress.core.GlobalIds#VALIDATOR_PROPS}4={@link Day}</li>
 * <li>{@link org.apache.directory.fortress.core.GlobalIds#DSD_VALIDATOR_PROP}={@link org.apache.directory.fortress.core.impl.DSDChecker}</li>
 * </ol>
 * </p>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class CUtil
{
    private static final String CLS_NM = CUtil.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static List<Validator> validators;
    private static final String DSDVALIDATOR = Config.getProperty( GlobalIds.DSD_VALIDATOR_PROP );

    /**
     * Private constructor
     *
     */
    private CUtil()
    {
    }

    /**
     * Used by DAO utilities to convert from a string with comma delimited values to fortress internal format {@link Constraint}.
     *
     * @param inputString contains raw data format which is comma delimited containing temporal data.
     * @param constraint  used by internal processing to perform validations.
     */
    public static void setConstraint( String inputString, Constraint constraint )
    {
        if ( StringUtils.isNotEmpty( inputString ) )
        {
            StringTokenizer tkn = new StringTokenizer( inputString, GlobalIds.DELIMITER, true );
            if ( tkn.countTokens() > 0 )
            {
                int count = tkn.countTokens();
                int index = 0;
                boolean previousTokenWasDelimiter = false;
                for ( int i = 0; i < count; i++ )
                {
                    String szValue = tkn.nextToken();
                    if ( szValue.equals( GlobalIds.DELIMITER ) && !previousTokenWasDelimiter )
                    {
                        previousTokenWasDelimiter = true;
                    }
                    else if ( szValue.equals( GlobalIds.DELIMITER ) )
                    {
                        previousTokenWasDelimiter = true;
                        index++;
                    }
                    else
                    {
                        previousTokenWasDelimiter = false;
                        switch ( index++ )
                        {
                            case 0:
                                // only set the name attr if it isn't already set:
                                if ( ( constraint.getName() == null ) || ( constraint.getName().length() == 0 ) )
                                {
                                    constraint.setName( szValue );
                                }

                                break;
                            case 1:
                                constraint.setTimeout( Integer.parseInt( szValue ) );
                                break;
                            case 2:
                                constraint.setBeginTime( szValue );
                                break;
                            case 3:
                                constraint.setEndTime( szValue );
                                break;
                            case 4:
                                constraint.setBeginDate( szValue );
                                break;
                            case 5:
                                constraint.setEndDate( szValue );
                                break;
                            case 6:
                                constraint.setBeginLockDate( szValue );
                                break;
                            case 7:
                                constraint.setEndLockDate( szValue );
                                break;
                            case 8:
                                constraint.setDayMask( szValue );
                                break;
                        }
                    }
                }
            }
        }
    }


    /**
     * Convert from fortress {@link Constraint} to comma delimited ldap format.
     *
     * @param constraint contains the temporal data.
     * @return string containing raw data bound for ldap.
     */
    public static String setConstraint( Constraint constraint )
    {
        String szConstraint = null;
        if ( constraint != null )
        {
            StringBuilder sb = new StringBuilder();
            sb.append( constraint.getName() );
            sb.append( GlobalIds.DELIMITER );

            if ( constraint.getTimeout() != null )
            {
                sb.append( constraint.getTimeout() );
            }

            sb.append( GlobalIds.DELIMITER );

            if ( constraint.getBeginTime() != null )
            {
                sb.append( constraint.getBeginTime() );
            }

            sb.append( GlobalIds.DELIMITER );

            if ( constraint.getEndTime() != null )
            {
                sb.append( constraint.getEndTime() );
            }

            sb.append( GlobalIds.DELIMITER );

            if ( constraint.getBeginDate() != null )
            {
                sb.append( constraint.getBeginDate() );
            }

            sb.append( GlobalIds.DELIMITER );

            if ( constraint.getEndDate() != null )
            {
                sb.append( constraint.getEndDate() );
            }

            sb.append( GlobalIds.DELIMITER );

            if ( constraint.getBeginLockDate() != null )
            {
                sb.append( constraint.getBeginLockDate() );
            }

            sb.append( GlobalIds.DELIMITER );

            if ( constraint.getEndLockDate() != null )
            {
                sb.append( constraint.getEndLockDate() );
            }

            sb.append( GlobalIds.DELIMITER );

            if ( constraint.getDayMask() != null )
            {
                sb.append( constraint.getDayMask() );
            }

            szConstraint = sb.toString();
        }
        return szConstraint;
    }


    /**
     * Validate the non-null attributes on the constraint.
     *
     * @param c1 contains the temporal values associated with an entity.
     * @throws org.apache.directory.fortress.core.ValidationException on first invalid attribute found.
     */
    public static void validate( Constraint c1 )
        throws ValidationException
    {
        if ( ObjUtil.isNotNullOrEmpty( c1.getTimeout() ) )
        {
            VUtil.timeout( c1.getTimeout() );
        }
        if ( StringUtils.isNotEmpty( c1.getBeginTime() ) )
        {
            VUtil.beginTime( c1.getBeginTime() );
        }
        if ( StringUtils.isNotEmpty( c1.getEndTime() ) )
        {
            VUtil.endTime( c1.getEndTime() );
        }
        if ( StringUtils.isNotEmpty( c1.getBeginDate() ) )
        {
            VUtil.beginDate( c1.getBeginDate() );
        }
        if ( StringUtils.isNotEmpty( c1.getEndDate() ) )
        {
            VUtil.endDate( c1.getEndDate() );
        }
        if ( StringUtils.isNotEmpty( c1.getDayMask() ) )
        {
            VUtil.dayMask( c1.getDayMask() );
        }
        if ( StringUtils.isNotEmpty( c1.getBeginLockDate() ) )
        {
            VUtil.beginDate( c1.getBeginLockDate() );
        }
        if ( StringUtils.isNotEmpty( c1.getEndLockDate() ) )
        {
            VUtil.endDate( c1.getEndLockDate() );
        }
    }


    /**
     * Utility is used during processing of constraint values.  The rule used here is if the target constraint will
     * accept the source constraint attribute only when not set initially.  If target constraint's attribute is set,
     * validation on the constraint will be performed.
     *
     * @param srcC Contains instantiated constraint with one or more attributes to be copied.
     * @param trgC instantiated object may contain zero or more attributes set.  Copy will not be performed on set attrs.
     * @throws org.apache.directory.fortress.core.ValidationException on first invalid attribute found.
     */
    public static void validateOrCopy( Constraint srcC, Constraint trgC )
        throws ValidationException
    {
        //VUtil.timeout(trgC.getTimeout());
        if ( ObjUtil.isNotNullOrEmpty( trgC.getTimeout() ) )
        {
            srcC.setTimeout( trgC.getTimeout() );
        }
        else if ( ObjUtil.isNotNullOrEmpty( srcC.getTimeout() ) )
        {
            trgC.setTimeout( srcC.getTimeout() );
        }
        if ( StringUtils.isNotEmpty( trgC.getBeginTime() ) )
        {
            VUtil.beginTime( trgC.getBeginTime() );
        }
        else if ( StringUtils.isNotEmpty( srcC.getBeginTime() ) )
        {
            trgC.setBeginTime( srcC.getBeginTime() );
        }
        if ( StringUtils.isNotEmpty( trgC.getEndTime() ) )
        {
            VUtil.endTime( trgC.getEndTime() );
        }
        else if ( StringUtils.isNotEmpty( srcC.getEndTime() ) )
        {
            trgC.setEndTime( srcC.getEndTime() );
        }
        if ( StringUtils.isNotEmpty( trgC.getBeginDate() ) )
        {
            VUtil.beginDate( trgC.getBeginDate() );
        }
        else if ( StringUtils.isNotEmpty( srcC.getBeginDate() ) )
        {
            trgC.setBeginDate( srcC.getBeginDate() );
        }
        if ( StringUtils.isNotEmpty( trgC.getEndDate() ) )
        {
            VUtil.endDate( trgC.getEndDate() );
        }
        else if ( StringUtils.isNotEmpty( srcC.getEndDate() ) )
        {
            trgC.setEndDate( srcC.getEndDate() );
        }
        if ( StringUtils.isNotEmpty( trgC.getDayMask() ) )
        {
            VUtil.dayMask( trgC.getDayMask() );
        }
        else if ( StringUtils.isNotEmpty( srcC.getDayMask() ) )
        {
            trgC.setDayMask( srcC.getDayMask() );
        }
        if ( StringUtils.isNotEmpty( trgC.getBeginLockDate() ) )
        {
            VUtil.beginDate( trgC.getBeginLockDate() );
        }
        else if ( StringUtils.isNotEmpty( srcC.getBeginLockDate() ) )
        {
            trgC.setBeginLockDate( srcC.getBeginLockDate() );
        }
        if ( StringUtils.isNotEmpty( trgC.getEndLockDate() ) )
        {
            VUtil.endDate( trgC.getEndLockDate() );
        }
        else if ( StringUtils.isNotEmpty( srcC.getEndLockDate() ) )
        {
            trgC.setEndLockDate( srcC.getEndLockDate() );
        }
    }

    /**
     * enum specifies what type of constraint is being targeted - User or Rold.
     */
    public static enum ConstraintType
    {
        /**
         * Specifies {@link org.apache.directory.fortress.core.model.User}
         */
        USER,
        /**
         * Specifies {@link org.apache.directory.fortress.core.model.Role}
         */
        ROLE
    }

    /**
     * static initializer retrieves Validators names from config and constructs for later processing.
     */
    static
    {
        try
        {
            validators = getValidators();
        }
        catch ( SecurityException ex )
        {
            LOG.error( "static initialzier caught SecurityException=" + ex.getMessage(), ex );
        }
    }


    /**
     * Copy source constraint to target. Both must be created before calling this utility.
     *
     * @param srcC contains constraint source.
     * @param trgC contains target constraint.
     */
    public static void copy( Constraint srcC, Constraint trgC )
    {
        // Both variables must be instantiated before being passed in to this method.
        trgC.setTimeout( srcC.getTimeout() );

        if ( StringUtils.isNotEmpty( srcC.getName() ) )
        {
            trgC.setName( srcC.getName() );
        }
        if ( StringUtils.isNotEmpty( srcC.getBeginTime() ) )
        {
            trgC.setBeginTime( srcC.getBeginTime() );
        }
        if ( StringUtils.isNotEmpty( srcC.getEndTime() ) )
        {
            trgC.setEndTime( srcC.getEndTime() );
        }
        if ( StringUtils.isNotEmpty( srcC.getDayMask() ) )
        {
            trgC.setDayMask( srcC.getDayMask() );
        }
        if ( StringUtils.isNotEmpty( srcC.getBeginDate() ) )
        {
            trgC.setBeginDate( srcC.getBeginDate() );
        }
        if ( StringUtils.isNotEmpty( srcC.getEndDate() ) )
        {
            trgC.setEndDate( srcC.getEndDate() );
        }
        if ( StringUtils.isNotEmpty( srcC.getBeginLockDate() ) )
        {
            trgC.setBeginLockDate( srcC.getBeginLockDate() );
        }
        if ( StringUtils.isNotEmpty( srcC.getEndLockDate() ) )
        {
            trgC.setEndLockDate( srcC.getEndLockDate() );
        }
    }


    /**
     * This utility iterates over all of the Validators initialized for runtime and calls them passing the {@link Constraint} contained within the
     * targeted entity.  If a particular {@link org.apache.directory.fortress.core.model.UserRole} violates constraint it will not be activated.  If {@link org.apache.directory.fortress.core.model.User} validation fails a ValidationException will be thrown thus preventing User logon.
     *
     * @param session contains {@link org.apache.directory.fortress.core.model.User} and {@link org.apache.directory.fortress.core.model.UserRole} constraints {@link Constraint} to be checked.
     * @param type    specifies User {@link ConstraintType#USER} or rOLE {@link ConstraintType#ROLE}.
     * @param checkDsd will check DSD constraints if true
     * @throws org.apache.directory.fortress.core.SecurityException in the event validation fails for User or system error occurs.
     */
    public static void validateConstraints( Session session, ConstraintType type, boolean checkDsd )
        throws SecurityException
    {
        String location = "validateConstraints";
        int rc;
        if ( validators == null )
        {
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "{} userId [{}]  no constraints enabled", location, session.getUserId() );
            }
            return;
        }
        // no need to continue if the role list is empty and we're trying to check role constraints:
        else if ( type == ConstraintType.ROLE && !ObjUtil.isNotNullOrEmpty( session.getRoles() )
            && !ObjUtil.isNotNullOrEmpty( session.getAdminRoles() ) )
        {
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "{} userId [{}]  has no roles assigned", location, session.getUserId() );
            }
            return;
        }
        for ( Validator val : validators )
        {
            Time currTime = TUtil.getCurrentTime();
            // first check the constraint on the user:
            if ( type == ConstraintType.USER )
            {
                rc = val.validate( session, session.getUser(), currTime );
                if ( rc > 0 )
                {
                    String info = location + " user [" + session.getUserId() + "] was deactivated reason code [" + rc
                        + "]";
                    throw new ValidationException( rc, info );
                }
            }
            // Check the constraints for each activated role:
            else
            {
                if ( ObjUtil.isNotNullOrEmpty( session.getRoles() ) )
                {
                    // now check the constraint on every role activation candidate contained within session object:
                    ListIterator<UserRole> roleItems = session.getRoles().listIterator();

                    while ( roleItems.hasNext() )
                    {
                        Constraint constraint = roleItems.next();
                        rc = val.validate( session, constraint, currTime );

                        if ( rc > 0 )
                        {
                            String msg = location + " role [" + constraint.getName() + "] for user ["
                                + session.getUserId() + "] was deactivated reason code [" + rc + "]";
                            LOG.info( msg );
                            roleItems.remove();
                            session.setWarning( new ObjectFactory().createWarning( rc, msg, Warning.Type.ROLE,
                                constraint.getName() ) );
                        }
                    }
                }
                if ( ObjUtil.isNotNullOrEmpty( session.getAdminRoles() ) )
                {
                    // now check the constraint on every arbac role activation candidate contained within session object:
                    ListIterator roleItems = session.getAdminRoles().listIterator();
                    while ( roleItems.hasNext() )
                    {
                        Constraint constraint = ( Constraint ) roleItems.next();
                        rc = val.validate( session, constraint, currTime );
                        if ( rc > 0 )
                        {
                            String msg = location + " admin role [" + constraint.getName() + "] for user ["
                                + session.getUserId() + "] was deactivated reason code [" + rc + "]";
                            LOG.info( msg );
                            roleItems.remove();
                            session.setWarning( new ObjectFactory().createWarning( rc, msg, Warning.Type.ROLE,
                                constraint.getName() ) );
                        }
                    }
                }
            }
        }

        // now perform DSD validation on session's impl roles:
        if ( checkDsd && DSDVALIDATOR != null && DSDVALIDATOR.length() > 0 && type == ConstraintType.ROLE
            && ObjUtil.isNotNullOrEmpty( session.getRoles() ) )
        {
            Validator dsdVal = ( Validator ) ClassUtil.createInstance( DSDVALIDATOR );
            dsdVal.validate( session, session.getUser(), null );
        }
        // reset the user's last access timestamp:
        session.setLastAccess();
    }


    /**
     * Utility is used internally by this class to retrieve a list of all Validator class names, instantiate and return.
     *
     * @return list of type {@link Validator} containing all active validation routines for entity constraint processing.
     * @throws org.apache.directory.fortress.core.CfgException in the event validator cannot be instantiated.
     */
    private static List<Validator> getValidators()
        throws CfgException
    {
        List<Validator> validators = new ArrayList<>();
        for ( int i = 0;; i++ )
        {
            String prop = GlobalIds.VALIDATOR_PROPS + i;
            String className = Config.getProperty( prop );
            if ( className == null )
            {
                break;
            }

            validators.add( ( Validator ) ClassUtil.createInstance( className ) );
        }
        return validators;
    }
}