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
package org.apache.directory.fortress.core.model;

import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.ValidationException;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.util.ConstraintValidator;
import org.apache.directory.fortress.core.util.VUtil;

/**
 *  Utilities to copy constraints attributes between entities.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class ConstraintUtil
{

    private static final ConstraintValidator constraintValidator = VUtil.getConstraintValidator();

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
     * Validate the non-null attributes on the constraint.
     *
     * @param c1 contains the temporal values associated with an entity.
     * @throws org.apache.directory.fortress.core.ValidationException on first invalid attribute found.
     */
    public static void validate( Constraint c1 )
        throws ValidationException
    {
        if ( c1.getTimeout() != null )
        {
            constraintValidator.timeout( c1.getTimeout() );
        }
        if ( StringUtils.isNotEmpty( c1.getBeginTime() ) )
        {
            constraintValidator.beginTime( c1.getBeginTime() );
        }
        if ( StringUtils.isNotEmpty( c1.getEndTime() ) )
        {
            constraintValidator.endTime( c1.getEndTime() );
        }
        if ( StringUtils.isNotEmpty( c1.getBeginDate() ) )
        {
            constraintValidator.beginDate( c1.getBeginDate() );
        }
        if ( StringUtils.isNotEmpty( c1.getEndDate() ) )
        {
            constraintValidator.endDate( c1.getEndDate() );
        }
        if ( StringUtils.isNotEmpty( c1.getDayMask() ) )
        {
            constraintValidator.dayMask( c1.getDayMask() );
        }
        if ( StringUtils.isNotEmpty( c1.getBeginLockDate() ) )
        {
            constraintValidator.beginDate( c1.getBeginLockDate() );
        }
        if ( StringUtils.isNotEmpty( c1.getEndLockDate() ) )
        {
            constraintValidator.endDate( c1.getEndLockDate() );
        }
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
            StringTokenizer tkn = new StringTokenizer( inputString, Config.getInstance().getDelimiter(), true );
            if ( tkn.countTokens() > 0 )
            {
                int count = tkn.countTokens();
                int index = 0;
                boolean previousTokenWasDelimiter = false;
                for ( int i = 0; i < count; i++ )
                {
                    String szValue = tkn.nextToken();
                    if ( szValue.equals( Config.getInstance().getDelimiter() ) && !previousTokenWasDelimiter )
                    {
                        previousTokenWasDelimiter = true;
                    }
                    else if ( szValue.equals( Config.getInstance().getDelimiter() ) )
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
        String delimiter = Config.getInstance().getDelimiter();
        if ( constraint != null )
        {
            StringBuilder sb = new StringBuilder();
            sb.append( constraint.getName() );
            sb.append( delimiter );

            if ( constraint.getTimeout() != null )
            {
                sb.append( constraint.getTimeout() );
            }

            sb.append( delimiter );

            if ( constraint.getBeginTime() != null )
            {
                sb.append( constraint.getBeginTime() );
            }

            sb.append( delimiter );

            if ( constraint.getEndTime() != null )
            {
                sb.append( constraint.getEndTime() );
            }

            sb.append( delimiter );

            if ( constraint.getBeginDate() != null )
            {
                sb.append( constraint.getBeginDate() );
            }

            sb.append( delimiter );

            if ( constraint.getEndDate() != null )
            {
                sb.append( constraint.getEndDate() );
            }

            sb.append( delimiter );

            if ( constraint.getBeginLockDate() != null )
            {
                sb.append( constraint.getBeginLockDate() );
            }

            sb.append( delimiter );

            if ( constraint.getEndLockDate() != null )
            {
                sb.append( constraint.getEndLockDate() );
            }

            sb.append( delimiter );

            if ( constraint.getDayMask() != null )
            {
                sb.append( constraint.getDayMask() );
            }

            szConstraint = sb.toString();
        }
        return szConstraint;
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
        if ( trgC.getTimeout() != null )
        {
            srcC.setTimeout( trgC.getTimeout() );
        }
        else if ( srcC.getTimeout() != null )
        {
            trgC.setTimeout( srcC.getTimeout() );
        }
        if ( StringUtils.isNotEmpty( trgC.getBeginTime() ) )
        {
            constraintValidator.beginTime( trgC.getBeginTime() );
        }
        else if ( StringUtils.isNotEmpty( srcC.getBeginTime() ) )
        {
            trgC.setBeginTime( srcC.getBeginTime() );
        }
        if ( StringUtils.isNotEmpty( trgC.getEndTime() ) )
        {
            constraintValidator.endTime( trgC.getEndTime() );
        }
        else if ( StringUtils.isNotEmpty( srcC.getEndTime() ) )
        {
            trgC.setEndTime( srcC.getEndTime() );
        }
        if ( StringUtils.isNotEmpty( trgC.getBeginDate() ) )
        {
            constraintValidator.beginDate( trgC.getBeginDate() );
        }
        else if ( StringUtils.isNotEmpty( srcC.getBeginDate() ) )
        {
            trgC.setBeginDate( srcC.getBeginDate() );
        }
        if ( StringUtils.isNotEmpty( trgC.getEndDate() ) )
        {
            constraintValidator.endDate( trgC.getEndDate() );
        }
        else if ( StringUtils.isNotEmpty( srcC.getEndDate() ) )
        {
            trgC.setEndDate( srcC.getEndDate() );
        }
        if ( StringUtils.isNotEmpty( trgC.getDayMask() ) )
        {
            constraintValidator.dayMask( trgC.getDayMask() );
        }
        else if ( StringUtils.isNotEmpty( srcC.getDayMask() ) )
        {
            trgC.setDayMask( srcC.getDayMask() );
        }
        if ( StringUtils.isNotEmpty( trgC.getBeginLockDate() ) )
        {
            constraintValidator.beginDate( trgC.getBeginLockDate() );
        }
        else if ( StringUtils.isNotEmpty( srcC.getBeginLockDate() ) )
        {
            trgC.setBeginLockDate( srcC.getBeginLockDate() );
        }
        if ( StringUtils.isNotEmpty( trgC.getEndLockDate() ) )
        {
            constraintValidator.endDate( trgC.getEndLockDate() );
        }
        else if ( StringUtils.isNotEmpty( srcC.getEndLockDate() ) )
        {
            trgC.setEndLockDate( srcC.getEndLockDate() );
        }
    }
}
