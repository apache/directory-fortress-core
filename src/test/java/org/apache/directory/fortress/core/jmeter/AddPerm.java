/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.fortress.core.jmeter;

import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import static org.junit.Assert.*;

/**
 * Add permission entry tests.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class AddPerm extends LoadBase
{
    /**
     * This test case performs add object entry.
     *
     * @param samplerContext Description of the Parameter
     * @return Description of the Return Value
     */
    public SampleResult runTest( JavaSamplerContext samplerContext )
    {
        boolean result = false;
        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();
        assertNotNull( adminMgr );
        int count = getKey( Op.ADD );
        Permission perm = getPermission( count );
        if ( count == 1 )
        {
            // Add the permission object entry (which requires a perm OU setup):
            assertNotNull( "ou operand not setup", ou );
            try
            {
                PermObj obj = adminMgr.addPermObj( new PermObj( perm.getObjName(), ou ));
                assertNotNull( perm.getObjName(), obj );
            }
            catch ( org.apache.directory.fortress.core.SecurityException se )
            {
                warn( se.getMessage() );
            }
        }
        try
        {
            Permission outPerm = adminMgr.addPermission( perm );
            assertNotNull( perm.getOpName(), outPerm );
            LOG.debug( "threadid: {}, add perm obj: {}, op: {}", getThreadId(), perm.getObjName(), perm.getOpName() );
            result = true;
            sleep();
        }
        catch ( org.apache.directory.fortress.core.SecurityException se )
        {
            warn( se.getMessage() );
        }
        wrapup( sampleResult, perm.getObjName() + "." + perm.getOpName(), result );
        return sampleResult;
    }
}
