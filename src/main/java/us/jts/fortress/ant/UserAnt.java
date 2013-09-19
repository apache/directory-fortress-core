/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.ant;


import us.jts.fortress.rbac.User;
import us.jts.fortress.rbac.UserRole;
import us.jts.fortress.util.attr.AttrHelper;

import java.util.StringTokenizer;

/**
 * Entity is used by custom Apache Ant task for special handling of collections.  This is necessary because the
 * Ant parser cannot deal with complex data attribute types.  The class extends a base entity.
 *
 * @author Shawn McKinney
 */
public class UserAnt extends User
{
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

    public String getAddresses()
    {
        return addresses;
    }

    public void setAddresses(String addresses)
    {
        this.addresses = addresses;
         // allow the setter to process comma delimited strings:
         StringTokenizer tkn = new StringTokenizer(addresses, ",");
         if (tkn.countTokens() > 0)
         {
             while (tkn.hasMoreTokens())
             {
                 String aTkn = tkn.nextToken();
                 getAddress().setAddress(aTkn);
             }
         }
    }

    public String getUserProps()
    {
        return userProps;
    }

    public void setUserProps( String userProps )
    {
        this.userProps = userProps;
        addProperties(AttrHelper.getProperties(userProps));
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        getAddress().setPostalCode(postalCode);
    }

    public String getPostOfficeBox()
    {
        return postOfficeBox;
    }

    public void setPostOfficeBox(String postOfficeBox)
    {
        getAddress().setPostOfficeBox(postOfficeBox);
    }

    public String getBuilding()
    {
        return building;
    }

    public void setBuilding(String building)
    {
        getAddress().setBuilding(building);
    }

    public String getDepartmentNumber()
    {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber)
    {
        getAddress().setDepartmentNumber(departmentNumber);
    }

    public String getRoomNumber()
    {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber)
    {
        getAddress().setRoomNumber(roomNumber);
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        getAddress().setCity(city);
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        getAddress().setState(state);
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        getAddress().setCountry(country);
    }

    private String country;


    /**
     * Generally not good practice to handle passwords as Strings in Java but this method allows Ant's digester to consume field in String format from the xml input file.
     * It subsequently converts to char[] as needed by the parent entity - {@link User}.
     *
     * @param password String format will be converted to char[].
     */
    public void setPassword(String password)
    {
        super.setPassword(password.toCharArray());
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
        // allow the setter to process comma delimited strings:
        StringTokenizer tkn = new StringTokenizer(phone, ",");
        if (tkn.countTokens() > 0)
        {
            while (tkn.hasMoreTokens())
            {
                String pTkn = tkn.nextToken();
                getPhones().add(pTkn);
            }
        }
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
        // allow the setter to process comma delimited strings:
        StringTokenizer tkn = new StringTokenizer(email, ",");
        if (tkn.countTokens() > 0)
        {
            while (tkn.hasMoreTokens())
            {
                String eTkn = tkn.nextToken();
                getEmails().add(eTkn);
            }
        }
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
        // allow the setter to process comma delimited strings:
        StringTokenizer tkn = new StringTokenizer(mobile, ",");
        if (tkn.countTokens() > 0)
        {
            while (tkn.hasMoreTokens())
            {
                String pTkn = tkn.nextToken();
                getMobiles().add(pTkn);
            }
        }
    }

}