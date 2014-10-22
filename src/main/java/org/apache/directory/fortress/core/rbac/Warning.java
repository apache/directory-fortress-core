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
package org.apache.directory.fortress.core.rbac;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * This class contains messages that map to warning that occur during role activation and password policy validation.
 * <p/>
 *
 * @author Shawn McKinney
 */
/**
 * This entity is stored on {@link org.apache.directory.fortress.core.rbac.Session} and is used to pass warnings that occur during role activation and password policy validation.
 * <p/>
 * Contains data from event that occurs during session initialization:
 * <p/>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>id</code>
 * <li> <code>msg</code>
 * <li> <code>type</code>
 * <li> <code>name</code>
 * <li>  ------------------------------------------
 * </ul>
 *
 * @author Shawn McKinney
 */
@XmlRootElement(name = "fortWarning")
@XmlAccessorType( XmlAccessType.FIELD)
@XmlType(name = "warning", propOrder =
    {
        "id",
        "msg",
        "type",
        "name"
    })
public class Warning implements Serializable
{
    public Warning()
    {
    }

    /**
     *
     * @param id
     * @param msg
     * @param type
     */
    public Warning(int id, String msg, Type type)
    {
        this.id = id;
        this.msg = msg;
        this.type = type;
    }

    /**
     *
     * @param id
     * @param msg
     * @param type
     * @param name
     */
    public Warning(int id, String msg, Type type, String name)
    {
        this.id = id;
        this.msg = msg;
        this.name = name;
        this.type = type;
    }

    /**
     * Type determines if warning is of type Role or Password Policy.
     */
    @XmlType(name = "warnType")
    @XmlEnum
    public enum Type
    {
        /**
         * Problem during role activation.
         */
        ROLE,

        /**
         * Problem during password policy validation.
         */
        PASSWORD
    }

    private int id;
    private String msg;
    private String name;
    private Type type;

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg( String msg )
    {
        this.msg = msg;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Type getType()
    {
        return type;
    }

    public void setType( Type type )
    {
        this.type = type;
    }
}