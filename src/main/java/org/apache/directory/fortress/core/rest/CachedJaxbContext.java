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

import java.util.Hashtable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * This class contains a very simple caching mechanism for storing JAXBContext objects associated with Fortress XML
 * processing.
 * The intent is to reduce the performance penalty for calling JAXBContext.newInstance( class );
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@SuppressWarnings( "rawtypes" )
public class CachedJaxbContext
{

    private static volatile Hashtable<Class, JAXBCachedEntry> jaxbInstanceCache = new Hashtable();

    /**
     * Once constructed this object can be stored as static member of class that performs JAX XML processing.
     *
     * @param type contains the class name that is being marshalled/unmarshalled.     *
     * @return handle to JAXBContext to be used to marshall or unmarshall XML data.
     * @throws JAXBException in the event the JAXBContext cannot be obtained.
     */
    public synchronized JAXBContext getJaxbContext( Class type ) throws JAXBException
    {

        JAXBCachedEntry cache = jaxbInstanceCache.get( type );
        if ( cache == null )
        {
            cache = new JAXBCachedEntry( type );
            jaxbInstanceCache.put( type, cache );
            return cache.getContext();
        }
        return cache.getContext();
    }

    /**
     * Return a handle to JAXB unmarshaller for a particular data type.  JAXBContext itself is thread safe.
     *
     * @param type contains the class name associated with a particular data type.
     * @return handel to JAXB unmarshaller.
     * @throws JAXBException in the event the unmarshaller cannot be retrieved.
     */
    public Unmarshaller createUnMarshaller( Class type ) throws JAXBException
    {
        JAXBContext context = getJaxbContext( type );
        return context.createUnmarshaller();
    }


    /**
     * Return a handle to JAXB unmarshaller for a particular data type.  JAXBContext itself is thread safe.
     *
     * @param type contains the class name associated with a particular data type.
     * @return handel to JAXB marshaller.
     * @throws JAXBException in the event the marshaller cannot be retrieved.
     */
    public Marshaller createMarshaller( Class type ) throws JAXBException
    {
        JAXBContext context = getJaxbContext( type );
        return context.createMarshaller();
    }
}
