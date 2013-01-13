/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package com.jts.fortress.rbac;

import com.jts.fortress.GlobalIds;
import com.jts.fortress.util.LogUtil;
import com.jts.fortress.util.attr.VUtil;
import junit.framework.TestCase;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.log4j.Logger;

/**
 * Description of the Class
 *
 * @author Shawn McKinney
 */
public class TestUtils extends TestCase
{
    final protected static Logger log = Logger.getLogger(TestUtils.class.getName());

    private static String contextId = GlobalIds.HOME;
    public static String getContext()
    {
        // This property can be overriden with system property:
        String tenant = System.getProperty(GlobalIds.TENANT);
        if(VUtil.isNotNullOrEmpty(tenant) && !tenant.equals("${tenant}"))
        {
            contextId = tenant;
        }
        return contextId;
    }

    /**
     * 
     * @param len
     */
    public static void sleep(String len)
    {
        try
        {
            Integer iSleep = (Integer.parseInt(len) * 1000);
            //log.info(TestUtils.class.getName() + ".sleep for len=" + iSleep);
            LogUtil.logIt(TestUtils.class.getName() + ".sleep for len=" + iSleep);
            Thread.currentThread().sleep(iSleep);
        }
        catch (InterruptedException ie)
        {
            log.warn(TestUtils.class.getName() + ".sleep caught InterruptedException=" + ie.getMessage(), ie);
        }
    }

    /**
     *
     * @param len
     */
    public static void sleep(int len)
    {
        try
        {
            int iSleep = len * 1000;
            com.jts.fortress.util.LogUtil.logIt(TestUtils.class.getName() + ".sleep for len=" + iSleep);
            Thread.currentThread().sleep(iSleep);
        }
        catch (InterruptedException ie)
        {
            log.warn(TestUtils.class.getName() + ".sleep caught InterruptedException=" + ie.getMessage(), ie);
        }
    }

    /**
     *
     * @param len
     */
    public static void sleep(long len)
    {
        try
        {
            long iSleep = len * 1000;
            com.jts.fortress.util.LogUtil.logIt(TestUtils.class.getName() + ".sleep for len=" + iSleep);
            Thread.currentThread().sleep(iSleep);
        }
        catch (InterruptedException ie)
        {
            log.warn(TestUtils.class.getName() + ".sleep caught InterruptedException=" + ie.getMessage(), ie);
        }
    }

    /**
     *
     * @param inClass
     * @param fieldLabel
     * @return
     */
    public static String getDataLabel(Class inClass, String fieldLabel)
    {
        String labelValue = null;
        try
        {
            Field field = inClass.getField(fieldLabel);
            Annotation annotation = field.getAnnotation(MyAnnotation.class);
            if (annotation instanceof MyAnnotation)
            {
                MyAnnotation myAnnotation = (MyAnnotation) annotation;
                labelValue = myAnnotation.value();
            }
        }
        catch (NoSuchFieldException e)
        {
            System.out.println("annotation excep=" + e);
        }

        return labelValue;
    }


    /**
     *
     * @param inClass
     * @param fieldLabel
     * @return
     */
    public static String getTestDataLabels(Class inClass, String fieldLabel)
    {
        String fieldName = null;
        try
        {
            //Field field = inClass.getField(fieldLabel);
            Field field = inClass.getField("POLICIES_TP1");

            Annotation annotation = field.getAnnotation(MyAnnotation.class);
            //Annotation[] annotations = field.getDeclaredAnnotations();
            if (annotation instanceof MyAnnotation)
            {
                MyAnnotation myAnnotation = (MyAnnotation) annotation;

                //System.out.println("name: " + "dd");
                System.out.println("*************** name: " + myAnnotation.name());
                System.out.println("*************** value: " + myAnnotation.value());
                fieldName = myAnnotation.name();
            }
        }
        catch (NoSuchFieldException e)
        {
            System.out.println("annotation excep=" + e);
        }

        return fieldName;
    }


    /**
     * 
     * @param srchVal
     * @return
     */
    public static String getSrchValue(String srchVal)
    {
        srchVal = srchVal.substring(0,srchVal.length()-2);
        return srchVal;
    }

    /**
     * @param msg
     * @param c1
     * @param c2
     */
    public static void assertTemporal(String msg, com.jts.fortress.util.time.Constraint c1, com.jts.fortress.util.time.Constraint c2)
    {
        assertEquals(msg, c1.getBeginDate(), c2.getBeginDate());
        assertEquals(msg, c1.getEndDate(), c2.getEndDate());
        assertEquals(msg, c1.getBeginLockDate(), c2.getBeginLockDate());
        assertEquals(msg, c1.getEndLockDate(), c2.getEndLockDate());
        assertEquals(msg, c1.getBeginTime(), c2.getBeginTime());
        assertEquals(msg, c1.getEndTime(), c2.getEndTime());
        assertEquals(msg, c1.getDayMask(), c2.getDayMask());
        assertEquals(msg, c1.getTimeout(), c2.getTimeout());
    }


    /**
     *
     * @param szInput
     * @return
     */
    public static Set<String> getSets(String szInput)
    {
        Set<String> vSets = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        try
        {
            if (VUtil.isNotNullOrEmpty(szInput))
            {
                StringTokenizer charSetTkn = new StringTokenizer(szInput, ",");
                if (charSetTkn.countTokens() > 0)
                {
                    while (charSetTkn.hasMoreTokens())
                    {
                        String value = charSetTkn.nextToken();
                        vSets.add(value);
                    }
                }
            }
        }
        catch (java.lang.ArrayIndexOutOfBoundsException ae)
        {
            // ignore
        }
        return vSets;
    }

}


