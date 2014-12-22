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
package org.apache.directory.fortress.core.rest;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * This class wraps JAXBContext and is used for simple caching mechanism during Fortress XML processing.
 * The intent is to leave future extension point in case schema validation is needed which prevents handling in cache itself.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@SuppressWarnings( "rawtypes" )
public class JAXBCachedEntry
{
    private final Class cachedClass;
    private final JAXBContext context;

    /**
     * Public constructor requires the entity class to be passed.
     *
     * @param type contains reference to object of type class.
     * @throws JAXBException thrown in the event new instance cannot be created.
     */
    public JAXBCachedEntry( Class type ) throws JAXBException
    {
        context = JAXBContext.newInstance( type );
        cachedClass = type;
    }

    /**
     * Return the class that is associated with this cached JAXBContext.
     *
     * @return class associated with JAXContext
     */
    public Class getCachedClass()
    {
        return cachedClass;
    }

    /**
     * Return the JAXBContext object associated with this wrapper class.
     *
     * @return handle to JAXBContext object.
     */
    public JAXBContext getContext()
    {
        return context;
    }
}