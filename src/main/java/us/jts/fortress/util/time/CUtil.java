/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.util.time;

import us.jts.fortress.CfgException;
import us.jts.fortress.GlobalIds;
import us.jts.fortress.cfg.Config;
import us.jts.fortress.SecurityException;
import us.jts.fortress.ValidationException;
import us.jts.fortress.rbac.ClassUtil;
import us.jts.fortress.rbac.Session;

import us.jts.fortress.util.attr.VUtil;
import org.apache.log4j.Logger;

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
 * <li>{@link us.jts.fortress.GlobalIds#VALIDATOR_PROPS}0={@link Date}</li>
 * <li>{@link us.jts.fortress.GlobalIds#VALIDATOR_PROPS}1={@link LockDate}</li>
 * <li>{@link us.jts.fortress.GlobalIds#VALIDATOR_PROPS}2={@link Timeout}</li>
 * <li>{@link us.jts.fortress.GlobalIds#VALIDATOR_PROPS}3={@link ClockTime}</li>
 * <li>{@link us.jts.fortress.GlobalIds#VALIDATOR_PROPS}4={@link Day}</li>
 * <li>{@link us.jts.fortress.GlobalIds#DSD_VALIDATOR_PROP}={@link us.jts.fortress.rbac.DSDChecker}</li>
 * </ol>
 * </p>
 *
 * @author Shawn McKinney
 */
public class CUtil
{
    private static final String CLS_NM = CUtil.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);
    private static List<Validator> validators;
    private static final String DSDVALIDATOR = Config.getProperty(GlobalIds.DSD_VALIDATOR_PROP);

    /**
     * Used by DAO utilities to convert from a string with comma delimited values to fortress internal format {@link Constraint}.
     *
     * @param inputString contains raw data format which is comma delimited containing temporal data.
     * @param constraint  used by internal processing to perform validations.
     */
    public static void setConstraint(String inputString, Constraint constraint)
    {
        if(VUtil.isNotNullOrEmpty(inputString))
        {
            StringTokenizer tkn = new StringTokenizer(inputString, GlobalIds.COMMA);
            if (tkn.countTokens() > 0)
            {
                int count = tkn.countTokens();
                for (int i = 0; i < count; i++)
                {
                    switch (i)
                    {
                        case 0:
                            String name = tkn.nextToken();
                            // only set the name attr if it isn't already set:
                            if (constraint.getName() == null || constraint.getName().length() == 0)
                                constraint.setName(name);
                            break;
                        case 1:
                            constraint.setTimeout(Integer.parseInt(tkn.nextToken()));
                            break;
                        case 2:
                            constraint.setBeginTime(tkn.nextToken());
                            break;
                        case 3:
                            constraint.setEndTime(tkn.nextToken());
                            break;
                        case 4:
                            constraint.setBeginDate(tkn.nextToken());
                            break;
                        case 5:
                            constraint.setEndDate(tkn.nextToken());
                            break;
                        case 6:
                            constraint.setBeginLockDate(tkn.nextToken());
                            break;
                        case 7:
                            constraint.setEndLockDate(tkn.nextToken());
                            break;
                        case 8:
                            constraint.setDayMask(tkn.nextToken());
                            break;
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
    public static String setConstraint(Constraint constraint)
    {
        String szConstraint = null;
        if (constraint != null)
        {
            StringBuilder sb = new StringBuilder();
            sb.append(constraint.getName());
            sb.append(GlobalIds.COMMA);
            if(constraint.getTimeout() != null)
                sb.append(constraint.getTimeout());
            sb.append(",");
            if (constraint.getBeginTime() != null)
                sb.append(constraint.getBeginTime());
            sb.append(",");
            if (constraint.getEndTime() != null)
                sb.append(constraint.getEndTime());
            sb.append(",");
            if (constraint.getBeginDate() != null)
                sb.append(constraint.getBeginDate());
            sb.append(",");
            if (constraint.getEndDate() != null)
                sb.append(constraint.getEndDate());
            sb.append(",");
            if (constraint.getBeginLockDate() != null)
                sb.append(constraint.getBeginLockDate());
            sb.append(",");
            if (constraint.getEndLockDate() != null)
                sb.append(constraint.getEndLockDate());
            sb.append(",");
            if (constraint.getDayMask() != null)
                sb.append(constraint.getDayMask());
            szConstraint = sb.toString();
        }
        return szConstraint;
    }

    /**
     * Validate the non-null attributes on the constraint.
     *
     * @param c1 contains the temporal values associated with an entity.
     * @throws us.jts.fortress.ValidationException on first invalid attribute found.
     */
    public static void validate(Constraint c1)
        throws us.jts.fortress.ValidationException
    {
        if (VUtil.isNotNullOrEmpty(c1.getTimeout()))
        {
            VUtil.timeout(c1.getTimeout());
        }
        if (VUtil.isNotNullOrEmpty(c1.getBeginTime()))
        {
            VUtil.beginTime(c1.getBeginTime());
        }
        if (VUtil.isNotNullOrEmpty(c1.getEndTime()))
        {
            VUtil.endTime(c1.getEndTime());
        }
        if (VUtil.isNotNullOrEmpty(c1.getBeginDate()))
        {
            VUtil.beginDate(c1.getBeginDate());
        }
        if (VUtil.isNotNullOrEmpty(c1.getEndDate()))
        {
            VUtil.endDate(c1.getEndDate());
        }
        if (VUtil.isNotNullOrEmpty(c1.getDayMask()))
        {
            VUtil.dayMask(c1.getDayMask());
        }
        if (VUtil.isNotNullOrEmpty(c1.getBeginLockDate()))
        {
            VUtil.beginDate(c1.getBeginLockDate());
        }
        if (VUtil.isNotNullOrEmpty(c1.getEndLockDate()))
        {
            VUtil.endDate(c1.getEndLockDate());
        }
    }

    /**
     * Utility is used during processing of constraint values.  The rule used here is if the target constraint will
     * accept the source constraint attribute only when not set initially.  If target constraint's attribute is set,
     * validation on the constraint will be performed.
     *
     * @param srcC Contains instantiated constraint with one or more attributes to be copied.
     * @param trgC instantiated object may contain zero or more attributes set.  Copy will not be performed on set attrs.
     * @throws us.jts.fortress.ValidationException on first invalid attribute found.
     */
    public static void validateOrCopy(Constraint srcC, Constraint trgC)
        throws ValidationException
    {
        //VUtil.timeout(trgC.getTimeout());
        if (VUtil.isNotNullOrEmpty(trgC.getTimeout()))
        {
            srcC.setTimeout(trgC.getTimeout());
        }
        else if (VUtil.isNotNullOrEmpty(srcC.getTimeout()))
        {
            trgC.setTimeout(srcC.getTimeout());
        }
        if (VUtil.isNotNullOrEmpty(trgC.getBeginTime()))
        {
            VUtil.beginTime(trgC.getBeginTime());
        }
        else if (VUtil.isNotNullOrEmpty(srcC.getBeginTime()))
        {
            trgC.setBeginTime(srcC.getBeginTime());
        }
        if (VUtil.isNotNullOrEmpty(trgC.getEndTime()))
        {
            VUtil.endTime(trgC.getEndTime());
        }
        else if (VUtil.isNotNullOrEmpty(srcC.getEndTime()))
        {
            trgC.setEndTime(srcC.getEndTime());
        }
        if (VUtil.isNotNullOrEmpty(trgC.getBeginDate()))
        {
            VUtil.beginDate(trgC.getBeginDate());
        }
        else if (VUtil.isNotNullOrEmpty(srcC.getBeginDate()))
        {
            trgC.setBeginDate(srcC.getBeginDate());
        }
        if (VUtil.isNotNullOrEmpty(trgC.getEndDate()))
        {
            VUtil.endDate(trgC.getEndDate());
        }
        else if (VUtil.isNotNullOrEmpty(srcC.getEndDate()))
        {
            trgC.setEndDate(srcC.getEndDate());
        }
        if (VUtil.isNotNullOrEmpty(trgC.getDayMask()))
        {
            VUtil.dayMask(trgC.getDayMask());
        }
        else if (VUtil.isNotNullOrEmpty(srcC.getDayMask()))
        {
            trgC.setDayMask(srcC.getDayMask());
        }
        if (VUtil.isNotNullOrEmpty(trgC.getBeginLockDate()))
        {
            VUtil.beginDate(trgC.getBeginLockDate());
        }
        else if (VUtil.isNotNullOrEmpty(srcC.getBeginLockDate()))
        {
            trgC.setBeginLockDate(srcC.getBeginLockDate());
        }
        if (VUtil.isNotNullOrEmpty(trgC.getEndLockDate()))
        {
            VUtil.endDate(trgC.getEndLockDate());
        }
        else if (VUtil.isNotNullOrEmpty(srcC.getEndLockDate()))
        {
            trgC.setEndLockDate(srcC.getEndLockDate());
        }
    }

    /**
     * enum specifies what type of constraint is being targeted - User or Rold.
     */
    public static enum ConstraintType
    {
        /**
         * Specifies {@link us.jts.fortress.rbac.User}
         */
        USER,
        /**
         * Specifies {@link us.jts.fortress.rbac.Role}
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
        catch (us.jts.fortress.SecurityException ex)
        {
            log.fatal(CLS_NM + ".static initialzier caught SecurityException=" + ex.getMessage(), ex);
        }
    }

    /**
     * Copy source constraint to target. Both must be created before calling this utility.
     *
     * @param srcC contains constraint source.
     * @param trgC contains target constraint.
     */
    public static void copy(Constraint srcC, Constraint trgC)
    {
        // Both variables must be instantiated before being passed in to this method.
        trgC.setTimeout(srcC.getTimeout());

        if (VUtil.isNotNullOrEmpty(srcC.getName()))
        {
            trgC.setName(srcC.getName());
        }
        if (VUtil.isNotNullOrEmpty(srcC.getBeginTime()))
        {
            trgC.setBeginTime(srcC.getBeginTime());
        }
        if (VUtil.isNotNullOrEmpty(srcC.getEndTime()))
        {
            trgC.setEndTime(srcC.getEndTime());
        }
        if (VUtil.isNotNullOrEmpty(srcC.getDayMask()))
        {
            trgC.setDayMask(srcC.getDayMask());
        }
        if (VUtil.isNotNullOrEmpty(srcC.getBeginDate()))
        {
            trgC.setBeginDate(srcC.getBeginDate());
        }
        if (VUtil.isNotNullOrEmpty(srcC.getEndDate()))
        {
            trgC.setEndDate(srcC.getEndDate());
        }
        if (VUtil.isNotNullOrEmpty(srcC.getBeginLockDate()))
        {
            trgC.setBeginLockDate(srcC.getBeginLockDate());
        }
        if (VUtil.isNotNullOrEmpty(srcC.getEndLockDate()))
        {
            trgC.setEndLockDate(srcC.getEndLockDate());
        }
    }


    /**
     * This utility iterates over all of the Validators initialized for runtime and calls them passing the {@link Constraint} contained within the
     * targeted entity.  If a particular {@link us.jts.fortress.rbac.UserRole} violates constraint it will not be activated.  If {@link us.jts.fortress.rbac.User} validation fails a ValidationException will be thrown thus preventing User logon.
     *
     * @param session contains {@link us.jts.fortress.rbac.User} and {@link us.jts.fortress.rbac.UserRole} constraints {@link Constraint} to be checked.
     * @param type    specifies User {@link ConstraintType#USER} or rOLE {@link ConstraintType#ROLE}.
     * @param checkDsd will check DSD constraints if true
     * @throws us.jts.fortress.SecurityException in the event validation fails for User or system error occurs.
     */
    public static void validateConstraints(Session session, ConstraintType type, boolean checkDsd)
        throws SecurityException
    {
        String location = CLS_NM + ".validateConstraints";
        int rc;
        if (validators == null)
        {
            if(log.isDebugEnabled())
            {
                log.debug(location + " userId [" + session.getUserId() + "]  no constraints enabled");
            }
            return;
        }
        // no need to continue if the role list is empty and we're trying to check role constraints:
        else if (type == ConstraintType.ROLE && !VUtil.isNotNullOrEmpty(session.getRoles()) && !VUtil.isNotNullOrEmpty(session.getAdminRoles()))
        {
            if(log.isDebugEnabled())
            {
                log.debug(location + " userId [" + session.getUserId() + "] has no roles assigned");
            }
            return;
        }
        for (Validator val : validators)
        {
            Time currTime = TUtil.getCurrentTime();
            // first check the constraint on the user:
            if (type == ConstraintType.USER)
            {
                rc = val.validate(session, session.getUser(), currTime);
                if (rc > 0)
                {
                    String info = location + " user [" + session.getUserId() + "] was deactivated reason code [" + rc + "]";
                    throw new ValidationException(rc, info);
                }
            }
            // Check the constraints for each role assignment:
            else
            {
                if (VUtil.isNotNullOrEmpty(session.getRoles()))
                {
                    // now check the constraint on every rbac role activation candidate contained within session object:
                    ListIterator roleItems = session.getRoles().listIterator();
                    while (roleItems.hasNext())
                    {
                        Constraint constraint = (Constraint) roleItems.next();
                        rc = val.validate(session, constraint, currTime);
                        if (rc > 0)
                        {
                            log.info(location + " role [" + constraint.getName() + "] for user ["
                                + session.getUserId() + "] was deactivated reason code [" + rc + "]");
                            roleItems.remove();
                        }
                    }
                }
                if (VUtil.isNotNullOrEmpty(session.getAdminRoles()))
                {
                    // now check the constraint on every arbac role activation candidate contained within session object:
                    ListIterator roleItems = session.getAdminRoles().listIterator();
                    while (roleItems.hasNext())
                    {
                        Constraint constraint = (Constraint) roleItems.next();
                        rc = val.validate(session, constraint, currTime);
                        if (rc > 0)
                        {
                            log.info(location + " admin role [" + constraint.getName() + "] for user ["
                                + session.getUserId() + "] was deactivated reason code [" + rc + "]");
                            roleItems.remove();
                        }
                    }
                }
            }
        }

        // now perform DSD validation on session's rbac roles:
        if (checkDsd && DSDVALIDATOR != null && DSDVALIDATOR.length() > 0 && type == ConstraintType.ROLE && us.jts.fortress.util.attr.VUtil.isNotNullOrEmpty(session.getRoles()))
        {
            Validator dsdVal = (Validator) ClassUtil.createInstance(DSDVALIDATOR);
            dsdVal.validate(session, session.getUser(), null);
        }
        // reset the user's last access timestamp:
        session.setLastAccess();
    }

    /**
     * Utility is used internally by this class to retrieve a list of all Validator class names, instantiate and return.
     *
     * @return list of type {@link Validator} containing all active validation routines for entity constraint processing.
     * @throws us.jts.fortress.CfgException in the event validator cannot be instantiated.
     */
    private static List<Validator> getValidators()
        throws CfgException
    {
        List<Validator> validators = new ArrayList<Validator>();
        for (int i = 0; ; i++)
        {
            String prop = GlobalIds.VALIDATOR_PROPS + i;
            String className = Config.getProperty(prop);
            if (className == null)
            {
                break;
            }

            validators.add((Validator) ClassUtil.createInstance(className));
        }
        return validators;
    }
}