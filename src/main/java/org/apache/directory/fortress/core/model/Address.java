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


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This entity is stored on {@link User} and is used to store postal address information in LDAP.
 * <p>
 * Contains data retrieved from the following LDAP attributes:
 * <p>
 * <ul>
 *   <li>  ------------------------------------------</li>
 *   <li> <code>postalAddress</code></li>
 *   <li> <code>st</code></li>
 *   <li> <code>postalCode</code></li>
 *   <li> <code>postOfficeBox</code></li>
 *   <li> <code>c</code></li>
 *   <li>  ------------------------------------------</li>
 * </ul>
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRootElement(name = "fortAddress")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "address", propOrder =
    {
        "addresses",
        "city",
        "state",
        "country",
        "postalCode",
        "postOfficeBox",
        "building",
        "departmentNumber",
        "roomNumber"
})
public class Address implements Serializable
{
    private static final long serialVersionUID = 1L;

    //@XmlElement(nillable = true)
    private List<String> addresses;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String postOfficeBox;
    private String building;
    private String departmentNumber;
    private String roomNumber;


    /**
     * This attribute is bound for {@code postalAddress} attribute on {@code organizationalPerson} object class.
     *
     * @param address contains a String value containing address line that is bound for multi-occurring {@code postalAddress} attribute.
     */
    public void setAddress( String address )
    {
        if ( addresses == null )
        {
            addresses = new ArrayList<>();
        }

        addresses.add( address );
    }


    /**
     * Return an ArrayList of type String that contains zero or more values retrieved from {@code postalAddress} attribute from {@code organizationalPerson} object class.
     *
     * @return a non-null ArrayList of type String that contains zero or more address lines associated with the user.
     */
    public List<String> getAddresses()
    {
        if ( addresses == null )
        {
            addresses = new ArrayList<>();
        }

        return addresses;
    }


    /**
     * Set an ArrayList of type String that contains one or more values bound for {@code postalAddress} attribute on {@code organizationalPerson} object class.
     *
     * @param addresses contains ArrayList of type String with one or more address lines associated with the user.
     */
    public void setAddresses( List<String> addresses )
    {
        this.addresses = addresses;
    }


    /**
     * Return a String that contains a value retrieved from {@code l} (location) attribute from {@code organizationalPerson} object class.
     *
     * @return a String that contains city associated with the user.
     */
    public String getCity()
    {
        return city;
    }


    /**
     * Accept a String that contains a value {@code l} (location) bound for {@code organizationalPerson} object class.
     *
     * @param city associated with the user.
     */
    public void setCity( String city )
    {
        this.city = city;
    }


    /**
     * Return a String that contains a value retrieved from {@code st} (state) attribute from {@code organizationalPerson} object class.
     *
     * @return a String that contains state associated with the user.
     */
    public String getState()
    {
        return state;
    }


    /**
     * Accept a String that contains a value {@code st} (state) bound for {@code organizationalPerson} object class.
     *
     * @param state associated with the user.
     */
    public void setState( String state )
    {
        this.state = state;
    }


    /**
     * TODO: Add support for this attribute:
     * Return a String that contains a value retrieved from {@code c} (country) attribute from {@code c} object class.
     *
     * @return a String that contains country associated with the user.
     */
    public String getCountry()
    {
        return country;
    }


    /**
     * TODO: Add support for this attribute:
     * Accept a String that contains a value {@code c} (country) bound for {@code c} object class.
     *
     * @param country associated with the user.
     */
    public void setCountry( String country )
    {
        this.country = country;
    }


    /**
     * Return a String that contains a value retrieved from {@code postalCode} attribute from {@code organizationalPerson} object class.
     *
     * @return a String that contains postalCode associated with the user.
     */
    public String getPostalCode()
    {
        return postalCode;
    }


    /**
     * Accept a String that contains a value {@code postalCode} bound for {@code organizationalPerson} object class.
     *
     * @param postalCode associated with the user.
     */
    public void setPostalCode( String postalCode )
    {
        this.postalCode = postalCode;
    }


    /**
     * Return a String that contains a value retrieved from {@code postOfficeBox} attribute from {@code organizationalPerson} object class.
     *
     * @return a String that contains postOfficeBox associated with the user.
     */
    public String getPostOfficeBox()
    {
        return postOfficeBox;
    }


    /**
     * Accept a String that contains a value {@code postOfficeBox} bound for {@code organizationalPerson} object class.
     *
     * @param postOfficeBox associated with the user.
     */
    public void setPostOfficeBox( String postOfficeBox )
    {
        this.postOfficeBox = postOfficeBox;
    }


    /**
     * Return a String that contains a value retrieved from {@code building} attribute from {@code organizationalPerson} object class.
     *
     * @return a String that contains building associated with the user.
     */
    public String getBuilding()
    {
        return building;
    }


    /**
     * Accept a String that contains a value {@code building} bound for {@code organizationalPerson} object class.
     *
     * @param building associated with the user.
     */
    public void setBuilding( String building )
    {
        this.building = building;
    }


    /**
     * Return a String that contains a value retrieved from {@code departmentNumber} attribute from {@code organizationalPerson} object class.
     *
     * @return a String that contains departmentNumber associated with the user.
     */
    public String getDepartmentNumber()
    {
        return departmentNumber;
    }


    /**
     * Accept a String that contains a value {@code departmentNumber} bound for {@code inetOrgperson} object class.
     *
     * @param departmentNumber associated with the user.
     */
    public void setDepartmentNumber( String departmentNumber )
    {
        this.departmentNumber = departmentNumber;
    }


    /**
     * Return a String that contains a value retrieved from {@code roomNumber} attribute from {@code organizationalPerson} object class.
     *
     * @return a String that contains roomNumber associated with the user.
     */
    public String getRoomNumber()
    {
        return roomNumber;
    }


    /**
     * Accept a String that contains a value {@code roomNumber} bound for {@code inetOrgperson} object class.
     *
     * @param roomNumber associated with the user.
     */
    public void setRoomNumber( String roomNumber )
    {
        this.roomNumber = roomNumber;
    }


    /**
     * Override the standard equals on object to use the attributes of this class.
     *
     * @param o
     * @return boolean value
     */
    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( ( o == null ) || ( getClass() != o.getClass() ) )
        {
            return false;
        }

        Address address = ( Address ) o;

        if ( addresses != null ? !addresses.equals( address.addresses ) : address.addresses != null )
        {
            return false;
        }

        if ( building != null ? !building.equals( address.building ) : address.building != null )
        {
            return false;
        }

        if ( city != null ? !city.equals( address.city ) : address.city != null )
        {
            return false;
        }

        if ( country != null ? !country.equals( address.country ) : address.country != null )
        {
            return false;
        }

        if ( departmentNumber != null ? !departmentNumber.equals( address.departmentNumber )
            : address.departmentNumber != null )
        {
            return false;
        }

        if ( postOfficeBox != null ? !postOfficeBox.equals( address.postOfficeBox ) : address.postOfficeBox != null )
        {
            return false;
        }

        if ( postalCode != null ? !postalCode.equals( address.postalCode ) : address.postalCode != null )
        {
            return false;
        }

        if ( roomNumber != null ? !roomNumber.equals( address.roomNumber ) : address.roomNumber != null )
        {
            return false;
        }

        return !( state != null ? !state.equals( address.state ) : address.state != null );

    }


    /**
     * Override the standard hashCode on object to use attributes of class.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        int result = addresses != null ? addresses.hashCode() : 0;
        result = 31 * result + ( city != null ? city.hashCode() : 0 );
        result = 31 * result + ( state != null ? state.hashCode() : 0 );
        result = 31 * result + ( country != null ? country.hashCode() : 0 );
        result = 31 * result + ( postalCode != null ? postalCode.hashCode() : 0 );
        result = 31 * result + ( postOfficeBox != null ? postOfficeBox.hashCode() : 0 );
        result = 31 * result + ( building != null ? building.hashCode() : 0 );
        result = 31 * result + ( departmentNumber != null ? departmentNumber.hashCode() : 0 );
        result = 31 * result + ( roomNumber != null ? roomNumber.hashCode() : 0 );

        return result;
    }


    /**
     * @see Object#toString()
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append( "Address object: \n" );

        if ( roomNumber != null )
        {
            sb.append( "    roomNumber :" ).append( roomNumber ).append( '\n' );
        }

        if ( departmentNumber != null )
        {
            sb.append( "    departmentNumber :" ).append( departmentNumber ).append( '\n' );
        }

        if ( building != null )
        {
            sb.append( "    building :" ).append( building ).append( '\n' );
        }

        if ( addresses != null )
        {
            sb.append( "    addresses : " );

            boolean isFirst = true;

            for ( String addr : addresses )
            {
                if ( isFirst )
                {
                    isFirst = false;
                }
                else
                {
                    sb.append( ", " );
                }

                sb.append( addr );
            }

            sb.append( '\n' );
        }

        if ( city != null )
        {
            sb.append( "    city :" ).append( city ).append( '\n' );
        }

        if ( postalCode != null )
        {
            sb.append( "    postalCode :" ).append( postalCode ).append( '\n' );
        }

        if ( postOfficeBox != null )
        {
            sb.append( "    postOfficeBox :" ).append( postOfficeBox ).append( '\n' );
        }

        if ( state != null )
        {
            sb.append( "    state :" ).append( state ).append( '\n' );
        }

        if ( country != null )
        {
            sb.append( "    country :" ).append( country ).append( '\n' );
        }

        return sb.toString();
    }
}