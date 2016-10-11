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
package org.apache.directory.fortress.core.ant;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.util.PropUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.directory.fortress.core.model.User;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.StringTokenizer;


/**
 * Entity is used by custom Apache Ant task for special handling of collections.  This is necessary because the
 * Ant parser cannot deal with complex data attribute types.  The class extends a base entity.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class UserAnt extends User
{
    /** Default serialVersionUID */
    private static final long serialVersionUID = 1L;
    private String userProps;
    private String email;
    private String phone;
    private String mobile;
    private String city;
    private String state;
    private String addresses;
    private String postalCode;
    private String postOfficeBox;
    private String building;
    private String departmentNumber;
    private String roomNumber;
    private String photo;
    private static final String CLS_NM = UserAnt.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );


    /**
     * Return addresses
     *
     * @return String containing addresses
     */
    public String getAddresses()
    {
        return addresses;
    }


    /**
     * Set addresses
     *
     * @param addresses String containing comma separated address names
     */
    public void setAddresses( String addresses )
    {
        this.addresses = addresses;
        // allow the setter to process comma delimited strings:
        StringTokenizer tkn = new StringTokenizer( addresses, "," );
        if ( tkn.countTokens() > 0 )
        {
            while ( tkn.hasMoreTokens() )
            {
                String aTkn = tkn.nextToken();
                getAddress().setAddress( aTkn );
            }
        }
    }


    /**
     * Return user properties
     *
     * @return String containing user properties
     */
    public String getUserProps()
    {
        return userProps;
    }


    /**
     * Set user properties.
     *
     * @param userProps string containing user properties.
     */
    public void setUserProps( String userProps )
    {
        this.userProps = userProps;
        addProperties( PropUtil.getProperties( userProps ) );
    }


    /**
     * Return postal code.
     *
     * @return String containing postal code.
     */
    public String getPostalCode()
    {
        return postalCode;
    }


    /**
     * Set a postal code on address.
     *
     * @param postalCode String containing postal code.
     */
    public void setPostalCode( String postalCode )
    {
        getAddress().setPostalCode( postalCode );
    }


    /**
     * Return post ofice box associated with a user.
     *
     * @return String containing postal code.
     */
    public String getPostOfficeBox()
    {
        return postOfficeBox;
    }


    /**
     * Set a post office box
     *
     * @param postOfficeBox Sting containing post office box.
     */
    public void setPostOfficeBox( String postOfficeBox )
    {
        getAddress().setPostOfficeBox( postOfficeBox );
    }


    /**
     * Return the building designator.
     *
     * @return String containing building designator.
     */
    public String getBuilding()
    {
        return building;
    }


    /**
     * Set building designator.
     *
     * @param building Accept String containing building designator.
     */
    public void setBuilding( String building )
    {
        getAddress().setBuilding( building );
    }


    /**
     * Return department number.
     *
     * @return String containing department number.
     */
    public String getDepartmentNumber()
    {
        return departmentNumber;
    }


    /**
     * Set department number.
     *
     * @param departmentNumber Accept String containing department number.
     */
    public void setDepartmentNumber( String departmentNumber )
    {
        getAddress().setDepartmentNumber( departmentNumber );
    }


    /**
     * Return room number.
     *
     * @return String containing room number.
     */
    public String getRoomNumber()
    {
        return roomNumber;
    }


    /**
     * Set the room number.
     *
     * @param roomNumber Accept a String containing the room number.
     */
    public void setRoomNumber( String roomNumber )
    {
        getAddress().setRoomNumber( roomNumber );
    }


    /**
     * Return the city name.
     *
     * @return String containing the city name.
     */
    public String getCity()
    {
        return city;
    }


    /**
     * Set the city name.
     *
     * @param city Accept a String containing the city name for the user.
     */
    public void setCity( String city )
    {
        getAddress().setCity( city );
    }


    /**
     * Return the state name.
     *
     * @return String containing the name of the State.
     */
    public String getState()
    {
        return state;
    }


    /**
     * Set the state name of the user.
     *
     * @param state Accept a String containing the state name.
     */
    public void setState( String state )
    {
        getAddress().setState( state );
    }


    /**
     * Return the country name.
     *
     * @return String with country name.
     */
    public String getCountry()
    {
        return country;
    }


    /**
     * Set the country name
     *
     * @param country String containing user's country.
     */
    public void setCountry( String country )
    {
        getAddress().setCountry( country );
    }

    private String country;

    /**
     * Return phone for a user.
     *
     * @return String containing phone number.
     */
    public String getPhone()
    {
        return phone;
    }


    /**
     * Set a phone on the user.  The user object can store many phone number.
     *
     * @param phone contains String bound to {@code telephoneNumber} attribute on {@code organizationalPerson} object
     *              class.
     */
    public void setPhone( String phone )
    {
        this.phone = phone;
        // allow the setter to process comma delimited strings:
        StringTokenizer tkn = new StringTokenizer( phone, "," );
        if ( tkn.countTokens() > 0 )
        {
            while ( tkn.hasMoreTokens() )
            {
                String pTkn = tkn.nextToken();
                getPhones().add( pTkn );
            }
        }
    }


    /**
     * Return email for user.
     *
     * @return String containing email.
     */
    public String getEmail()
    {
        return email;
    }


    /**
     * A User may have many phones set on their record.
     *
     * @param email contains a String to be stored as email address on user.
     */
    public void setEmail( String email )
    {
        this.email = email;
        // allow the setter to process comma delimited strings:
        StringTokenizer tkn = new StringTokenizer( email, "," );
        if ( tkn.countTokens() > 0 )
        {
            while ( tkn.hasMoreTokens() )
            {
                String eTkn = tkn.nextToken();
                getEmails().add( eTkn );
            }
        }
    }


    /**
     * Return a mobile number for the user.
     *
     * @return String containing mobile number.
     */
    public String getMobile()
    {
        return mobile;
    }


    /**
     * Set a mobile number on the user.  The user may have many mobiles.
     *
     * @param mobile contains a String containing mobile phone numbers associated with the user.
     */
    public void setMobile( String mobile )
    {
        this.mobile = mobile;
        // allow the setter to process comma delimited strings:
        StringTokenizer tkn = new StringTokenizer( mobile, "," );
        if ( tkn.countTokens() > 0 )
        {
            while ( tkn.hasMoreTokens() )
            {
                String pTkn = tkn.nextToken();
                getMobiles().add( pTkn );
            }
        }
    }


    /**
     * Return the user's jpg photo
     *
     * @return String
     */
    public String getPhoto()
    {
        return photo;
    }


    /**
     * Set a photo on user's record
     *
     * @param photo The photo to store
     */
    public void setPhoto( String photo )
    {
        this.photo = photo;
        if ( StringUtils.isNotEmpty( photo ) )
        {
            byte[] jpeg = getJpegPhoto( photo );
            if ( ArrayUtils.isNotEmpty( jpeg ) )
            {
                setJpegPhoto( jpeg );
            }
        }
    }


    private static byte[] getJpegPhoto( String fileName )
    {
        byte[] value = null;
        try
        {
            value = readJpegFile( fileName );
        }
        catch ( ArrayIndexOutOfBoundsException ae )
        {
            // attribute is optional, do nothing here
        }

        return value;
    }


    /**
     * Read the image from specified file location and return as a byte array.
     *
     * @param fileName fully qualified file name
     * @return byte array containing image
     */
    public static byte[] readJpegFile( String fileName )
    {
        URL fUrl = UserAnt.class.getClassLoader().getResource( fileName );
        byte[] image = null;
        try
        {
            if ( fUrl != null )
            {
                image = FileUtils.readFileToByteArray( new File( fUrl.toURI() ) );
            }
        }
        catch ( URISyntaxException se )
        {
            String warn = "readJpegFile caught URISyntaxException=" + se;
            LOG.warn( warn );
        }
        catch ( IOException ioe )
        {
            String warn = "readJpegFile caught IOException=" + ioe;
            LOG.warn( warn );
        }
        return image;
    }
}