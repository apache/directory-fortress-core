/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
 */

package org.openldap.fortress.rbac;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as a container for {@code java.util.Properties} for passing to En Masse server.
 * </p>
 * This class is thread safe.
 *
 * @author Shawn McKinney
 *         <p/>
 *         <p>The following schema fragment specifies the expected content contained within this class.
 *         <p/>
 *         <pre>
 *                 &lt;complexType>
 *                   &lt;complexContent>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                       &lt;sequence>
 *                         &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
 *                           &lt;complexType>
 *                             &lt;complexContent>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                 &lt;sequence>
 *                                   &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *                                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *                                 &lt;/sequence>
 *                               &lt;/restriction>
 *                             &lt;/complexContent>
 *                           &lt;/complexType>
 *                         &lt;/element>
 *                       &lt;/sequence>
 *                     &lt;/restriction>
 *                   &lt;/complexContent>
 *                 &lt;/complexType>
 *                 </pre>
 */
@XmlRootElement(name = "fortProps")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "props", propOrder = {
    "entry"
})
public class Props extends FortEntity implements Serializable
{
    private List<Props.Entry> entry;

    /**
     * Gets the value of the entry property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entry property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntry().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Props.Entry }
     *
     *
     */
    public List<Props.Entry> getEntry()
    {
        if (entry == null)
        {
            entry = new ArrayList<>();
        }
        return this.entry;
    }

    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
     *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "key",
        "value"
    })
    public static class Entry implements Serializable
    {

        protected String key;
        protected String value;

        /**
         * Gets the value of the key property.
         *
         * @return
         *     possible object is
         *     {@link Object }
         *
         */
        public String getKey()
        {
            return key;
        }

        /**
         * Sets the value of the key property.
         *
         * @param value
         *     allowed object is
         *     {@link Object }
         *
         */
        public void setKey(String value)
        {
            this.key = value;
        }

        /**
         * Gets the value of the value property.
         *
         * @return
         *     possible object is
         *     {@link Object }
         *
         */
        public String getValue()
        {
            return value;
        }

        /**
         * Sets the value of the value property.
         *
         * @param value
         *     allowed object is
         *     {@link Object }
         *
         */
        public void setValue(String value)
        {
            this.value = value;
        }

        /**
         *
         * @param obj
         * @return boolean value
         */
        public boolean equals(Object obj)
        {
            if (obj != null && obj instanceof Props.Entry)
            {
                Props.Entry inObj = (Props.Entry) obj;
                return key.equals(inObj.getKey());
            }
            return false;
        }
    }
}