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

import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Role;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import static org.junit.Assert.*;

/**
 * Revoke perm entry tests.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class RevokePerm extends LoadBase
{
    /**
     * This test case performs revoke permission.
     *
     * @param samplerContext Description of the Parameter
     * @return Description of the Return Value
     */
    public SampleResult runTest( JavaSamplerContext samplerContext )
    {
        boolean result = false;
        SampleResult sampleResult = new SampleResult();
        Permission perm = null;
        try
        {
            sampleResult.sampleStart();
            assertNotNull( adminMgr );
            assertNotNull( concat("role operand not setup", role ) );
            perm = getPermission( getKey( Op.ADD ) );
            adminMgr.revokePermission( perm, new Role( role ) );
            LOG.debug( "threadid: {}, perm obj: {}, op: {}, role: {}", getThreadId(), perm.getObjName(), perm.getOpName(), role );
            result = true;
            sleep();
        }
        catch ( org.apache.directory.fortress.core.SecurityException se )
        {
            warn( se.getMessage() );
        }
        wrapup( sampleResult, perm.getObjName() + "." + perm.getOpName() + " role: " + role, result );
        return sampleResult;
    }
}
