/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */
package us.jts.fortress.rbac;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This entity is stored on {@link User} and is used to store postal address information in LDAP.
 * <p/>
 * Contains data retrieved from the following LDAP attributes:
 * <p/>
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code>postalAddress</code>
 * <li> <code>st</code>
 * <li> <code>postalCode</code>
 * <li> <code>postOfficeBox</code>
 * <li> <code>c</code>
 * <li>  ------------------------------------------
 * </ul>
 *
 * @author Shawn McKinney
 */
@XmlRootElement(name = "fortAddress")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "address", propOrder = {
    "addresses",
    "city",
    "state",
    "country",
    "postalCode",
    "postOfficeBox"
})
public class Address implements Serializable
{
    @XmlElement(nillable = true)
    private List<String> addresses;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String postOfficeBox;

    /**
     * This attribute is bound for {@code postalAddress} attribute on {@code organizationalPerson} object class.
     *
     * @param address contains a String value containing address line that is bound for multi-occurring {@code postalAddress} attribute.
     */
    public void setAddress(String address)
    {
        if (addresses == null)
        {
            addresses = new ArrayList<>();
        }
        addresses.add(address);
    }

    /**
     * Return an ArrayList of type String that contains zero or more values retrieved from {@code postalAddress} attribute from {@code organizationalPerson} object class.
     *
     * @return a non-null ArrayList of type String that contains zero or more address lines associated with the user.
     */
    public List<String> getAddresses()
    {
        if (addresses == null)

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
    public void setAddresses(List<String> addresses)
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
    public void setCity(String city)
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
    public void setState(String state)
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
    public void setCountry(String country)
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
    public void setPostalCode(String postalCode)
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
    public void setPostOfficeBox(String postOfficeBox)
    {
        this.postOfficeBox = postOfficeBox;
    }

    /**
     * Override the standard equals on object to use the attributes of this class.
     *
     * @param o
     * @return boolean value
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (addresses != null ? !addresses.equals(address.addresses) : address.addresses != null) return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (country != null ? !country.equals(address.country) : address.country != null) return false;
        if (postOfficeBox != null ? !postOfficeBox.equals(address.postOfficeBox) : address.postOfficeBox != null)
            return false;
        if (postalCode != null ? !postalCode.equals(address.postalCode) : address.postalCode != null) return false;
        if (state != null ? !state.equals(address.state) : address.state != null) return false;

        return true;
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
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (postOfficeBox != null ? postOfficeBox.hashCode() : 0);
        return result;
    }
}