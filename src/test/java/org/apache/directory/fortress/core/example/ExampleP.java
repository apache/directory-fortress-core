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

import org.apache.directory.fortress.core.util.ConstraintValidator;
import org.slf4j.LoggerFactory;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.util.VUtil;

import java.util.List;


public class ExampleP
{
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger( ExampleP.class.getName() );
    final ExampleDAO el = new ExampleDAO();
    private static final ConstraintValidator constraintValidator = VUtil.getConstraintValidator();

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
                LOG.warn(warning);
            }
            else
            {
                VUtil.description(ee.getDescription());
            }
        }
        else
        {
            //name name
            VUtil.safeText(ee.getName(), GlobalIds.ROLE_LEN);
            if (ee.getDescription() != null && ee.getDescription().length() > 0)
            {
                VUtil.description(ee.getDescription());
            }
        }
        if (ee.getTimeout() >= 0)
        {
            constraintValidator.timeout(ee.getTimeout());
        }
        if (ee.getBeginTime() != null && ee.getBeginTime().length() > 0)
        {
            constraintValidator.beginTime(ee.getBeginTime());
        }
        if (ee.getEndTime() != null && ee.getEndTime().length() > 0)
        {
            constraintValidator.endTime(ee.getEndTime());
        }
        if (ee.getBeginDate() != null && ee.getBeginDate().length() > 0)
        {
            constraintValidator.beginDate(ee.getBeginDate());
        }
        if (ee.getEndDate() != null && ee.getEndDate().length() > 0)
        {
            constraintValidator.endDate(ee.getEndDate());
        }
        if (ee.getDayMask() != null && ee.getDayMask().length() > 0)
        {
            constraintValidator.dayMask(ee.getDayMask());
        }
        if (ee.getBeginLockDate() != null && ee.getBeginLockDate().length() > 0)
        {
            constraintValidator.beginDate(ee.getBeginLockDate());
        }
        if (ee.getEndLockDate() != null && ee.getEndLockDate().length() > 0)
        {
            constraintValidator.endDate(ee.getEndLockDate());
        }
    }
}

