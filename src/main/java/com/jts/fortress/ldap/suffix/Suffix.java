/*
* Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
*/

package com.jts.fortress.ldap.suffix;


/**
 * This class contains the Suffix node for OpenLDAP Directory Information Tree.
 * <br />The domain component object class is 'dcObject' <br />
 * <p/>
 * dcObject Auxiliary Object Class is used to store basic attributes like domain component names and description.
 * <ul>
 * <li>  ------------------------------------------
 * <li> <code># RFC 2247</code>
 * <li> <code>objectclass ( 1.3.6.1.4.1.1466.344 NAME 'dcObject'</code>
 * <li> <code>SUP top AUXILIARY MUST dc )</code>
 * <li>  ------------------------------------------
 * </ul>
 * <p/>
 * see <a href="http://http://en.wikipedia.org/wiki/LDAP/">Wikipedia LDAP</a>
 * <font size="2" color="blue">
 * <blockquote>
 * <h3>
 * Naming structure
 * </h3>
 * Since an LDAP server can return referrals to other servers for requests the server itself will not/can not serve, a naming structure for LDAP entries is needed so one can find a server holding a given DN. Since such a structure already exists in the Domain name system (DNS), servers' top level names often mimic DNS names, as they do in X.500.
 * If an organization has domain name example.org, its top level LDAP entry will typically have the DN dc=example,dc=org (where dc means domain component). If the LDAP server is also named ldap.example.org, the organization's top level LDAP URL becomes ldap://ldap.example.org/dc=example,dc=org.
 * Below the top level, the entry names will typically reflect the organization's internal structure or needs rather than DNS names.
 * </blockquote>
 * </font>
 * <p/>
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 *
 * @author Shawn McKinney
 */
public class Suffix
{
    private String dc;
    private String name;
    private String description;


    /**
     * Generate instance of suffix to be loaded as domain component ldap object.
     *
     * @param dc          top level domain component maps to 'dc' (i.e. 'com') attribute in 'dcObject' object class.
     * @param name        second level domain component name maps to attribute in 'dcObject' object class.
     * @param description maps to 'o' attribute in 'dcObject' object class.
     */
    public Suffix(String dc, String name, String description)
    {
        this.dc = dc;
        this.name = name;
        this.description = description;
    }

    /**
     * Default constructor used by {@link com.jts.fortress.ant.FortressAntTask}
     */
    public Suffix()
    {
    }

    /**
     * Get top level domain component specifier, i.e. dc=com.  This attribute is required.
     *
     * @return dc maps to 'dc' in 'dcObject' object class.
     */
    public String getDc()
    {
        return dc;
    }

    /**
     * Set top level domain component specifier, i.e. dc=com.  This attribute is required.
     *
     * @param dc maps to 'dc' in 'dcObject' object class.
     */
    public void setDc(String dc)
    {
        this.dc = dc;
    }

    /**
     * Get the second level qualifier on the domain component.  This attribute is required.
     *
     * @return name maps to 'dcObject' object class.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the second level qualifier on the domain component.  This attribute is required.
     *
     * @param name maps to 'dcObject' object class.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get the description for the domain component.  This value is not required or constrained
     * but is validated on reasonability.
     *
     * @return field maps to 'o' attribute on 'dcObject'.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Set the description for the domain component.  This value is not required or constrained
     * but is validated on reasonability.
     *
     * @param description maps to 'o' attribute on 'dcObject'.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
}

