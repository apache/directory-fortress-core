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
package org.apache.directory.fortress.core.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is used as a container for {@code java.util.Properties} for passing to Fortress Rest server.
 * <p>
 * This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 *         <p>
 *         <p>The following schema fragment specifies the expected content contained within this class.
 *         <p>
 *         <pre>
 *             &lt;complexType&gt;
 *               &lt;complexContent&gt;
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                   &lt;sequence&gt;
 *                     &lt;element name="entry" maxOccurs="unbounded" minOccurs="0"&gt;
 *                       &lt;complexType&gt;
 *                         &lt;complexContent&gt;
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                             &lt;sequence&gt;
 *                               &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/&gt;
 *                               &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/&gt;
 *                             &lt;/sequence&gt;
 *                           &lt;/restriction&gt;
 *                         &lt;/complexContent&gt;
 *                       &lt;/complexType&gt;
 *                     &lt;/element&gt;
 *                   &lt;/sequence&gt;
 *                 &lt;/restriction&gt;
 *               &lt;/complexContent&gt;
 *             &lt;/complexType&gt;
 *             </pre>
 */
@XmlRootElement(name = "fortProps")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "props", propOrder =
    {
        "entry"
})
public class Props extends FortEntity implements Serializable
{
    /** Default serialVersionUID */
    private static final long serialVersionUID = 1L;
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
        if ( entry == null )
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
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/&gt;
     *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder =
        {
            "key",
            "value"
    })
    public static class Entry implements Serializable
    {
        /** Default serialVersionUID */
        private static final long serialVersionUID = 1L;

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
        public void setKey( String value )
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
        public void setValue( String value )
        {
            this.value = value;
        }


        /**
         *
         * @param obj
         * @return boolean value
         */
        public boolean equals( Object obj )
        {
            if ( obj instanceof Props.Entry )
            {
                Props.Entry inObj = ( Props.Entry ) obj;
                return key.equals( inObj.getKey() );
            }
            return false;
        }

        @Override
        public int hashCode()
        {
            int result = key != null ? key.hashCode() : 0;
            result = 31 * result + ( value != null ? value.hashCode() : 0 );
            return result;
        }
    }
}