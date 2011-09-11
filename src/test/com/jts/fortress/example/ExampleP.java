/*
 * Copyright (c) 2009-2011. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.example;

import com.jts.fortress.SecurityException;
import com.jts.fortress.util.attr.VUtil;

import java.util.List;

import org.apache.log4j.Logger;


public class ExampleP
{
    final protected static Logger log = Logger.getLogger(ExampleP.class.getName());
    final ExampleDAO el = new ExampleDAO();


    public Example read(String name)
        throws SecurityException
    {
        return el.findByKey(name);
    }

    public final List<Example> search(String searchVal)
        throws SecurityException
    {
        // Call the finder.
        return el.findExamples(searchVal);
    }


    public Example add(Example ee)
        throws SecurityException
    {
        validate(ee, false);
        return el.create(ee);
    }


    public Example update(Example ee)
        throws SecurityException
    {
        validate(ee, false);
        return el.update(ee);
    }


    public void delete(String name)
        throws SecurityException
    {
        el.remove(name);
    }


    void validate(Example ee, boolean isUpdate)
        throws SecurityException
    {
        int length;


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
        if (!isUpdate)
        {
            //name name
            VUtil.safeText(ee.getName(), EIds.EXAMPLE_LEN);
            if (ee.getDescription() == null || ee.getDescription().length() == 0)
            {
                String warning = "RoleP.validate null or empty description";
                log.warn(warning);
            }
            else
            {
                VUtil.description(ee.getDescription());
            }
        }
        else
        {
            //name name
            VUtil.safeText(ee.getName(), com.jts.fortress.constants.GlobalIds.ROLE_LEN);
            if (ee.getDescription() != null && ee.getDescription().length() > 0)
            {
                VUtil.description(ee.getDescription());
            }
        }
        if (ee.getTimeout() >= 0)
        {
            VUtil.timeout(ee.getTimeout());
        }
        if (ee.getBeginTime() != null && ee.getBeginTime().length() > 0)
        {
            VUtil.beginTime(ee.getBeginTime());
        }
        if (ee.getEndTime() != null && ee.getEndTime().length() > 0)
        {
            VUtil.endTime(ee.getEndTime());
        }
        if (ee.getBeginDate() != null && ee.getBeginDate().length() > 0)
        {
            VUtil.beginDate(ee.getBeginDate());
        }
        if (ee.getEndDate() != null && ee.getEndDate().length() > 0)
        {
            VUtil.endDate(ee.getEndDate());
        }
        if (ee.getDayMask() != null && ee.getDayMask().length() > 0)
        {
            VUtil.dayMask(ee.getDayMask());
        }
        if (ee.getBeginLockDate() != null && ee.getBeginLockDate().length() > 0)
        {
            VUtil.beginDate(ee.getBeginLockDate());
        }
        if (ee.getEndLockDate() != null && ee.getEndLockDate().length() > 0)
        {
            VUtil.endDate(ee.getEndLockDate());
        }
    }
}

