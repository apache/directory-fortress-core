/*
* Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
*/

package us.jts.fortress.rbac;


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
 * This entity is stored on {@link Session} and is used to pass warnings that occur during role activation and password policy validation.
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