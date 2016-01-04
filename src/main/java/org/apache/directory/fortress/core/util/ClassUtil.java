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
package org.apache.directory.fortress.core.util;


import org.apache.directory.fortress.core.CfgException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.api.util.Strings;

import java.io.InputStream;


/**
 * General purpose factory uses java reflection to instantiate new Manager object.
 * <p>
 * This class is called by the Manager factories:
 * <ol>
 * <li>{@link org.apache.directory.fortress.core.AccessMgrFactory}</li>
 * <li>{@link org.apache.directory.fortress.core.AdminMgrFactory}</li>
 * <li>{@link org.apache.directory.fortress.core.AuditMgrFactory}</li>
 * <li>{@link org.apache.directory.fortress.core.DelAccessMgrFactory}</li>
 * <li>{@link org.apache.directory.fortress.core.DelAdminMgrFactory}</li>
 * <li>{@link org.apache.directory.fortress.core.DelReviewMgrFactory}</li>
 * <li>{@link org.apache.directory.fortress.core.PwPolicyMgrFactory}</li>
 * <li>{@link org.apache.directory.fortress.core.ReviewMgrFactory}</li>
 * <li>{@link org.apache.directory.fortress.core.ConfigMgrFactory}</li>
 * </ol>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class ClassUtil
{
    /**
     * Private constructor
     *
     */
    private ClassUtil()
    {
    }

    /**
     * Given a valid class name call the default constructor through reflexion and return the reference to the caller.
     * @param className contains fully qualified java class name to be instantiated.  Must have a public default constructor to be successful.
     * @return reference to instantiated ManagerImpl object.
     * @throws org.apache.directory.fortress.core.CfgException in the event of failure to instantiate.
     *
     */
    public static Object createInstance( String className ) throws CfgException
    {
        Object target;

        try
        {
            if ( Strings.isEmpty( className ) )
            {
                String error = "createInstance() null or empty classname";
                throw new CfgException( GlobalErrIds.FT_MGR_CLASS_NAME_NULL, error );
            }

            target = Class.forName( className ).newInstance();
        }
        catch ( ClassNotFoundException e )
        {
            String error = "createInstance() className [" + className + "] caught java.lang.ClassNotFoundException="
                + e;
            throw new CfgException( GlobalErrIds.FT_MGR_CLASS_NOT_FOUND, error, e );
        }
        catch ( InstantiationException e )
        {
            String error = "createInstance()  [" + className + "] caught java.lang.InstantiationException=" + e;
            throw new CfgException( GlobalErrIds.FT_MGR_INST_EXCEPTION, error, e );
        }
        catch ( IllegalAccessException e )
        {
            String error = "createInstance()  [" + className + "] caught java.lang.IllegalAccessException=" + e;
            throw new CfgException( GlobalErrIds.FT_MGR_ILLEGAL_ACCESS, error, e );
        }

        return target;
    }


    /**
     * Find a file on the classloader and return as InputStream.
     * @param name contains the name of the file resource.
     * @return handle to the InputStream
     * @throws CfgException in the event resource is not found on classloader.
     */
    public static InputStream resourceAsStream( String name ) throws CfgException
    {
        InputStream is = ClassUtil.class.getClassLoader().getResourceAsStream( name );

        if ( is == null )
        {
            throw new CfgException( GlobalErrIds.FT_RESOURCE_NOT_FOUND, name );
        }

        return is;
    }
}
