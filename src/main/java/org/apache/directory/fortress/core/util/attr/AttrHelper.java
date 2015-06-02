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
package org.apache.directory.fortress.core.util.attr;

import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.name.Rdn;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.model.Administrator;
import org.apache.directory.fortress.core.model.AuthZ;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.util.ObjUtil;

import java.util.List;
import java.util.StringTokenizer;
import java.util.Properties;

/**
 * Class contains static utilities for various conversions of ldap data to application entity and back.  These methods are called by the
 * Fortress DAO's, i.e. {@link org.apache.directory.fortress.core.impl.UserDAO}.
 * These are low-level data utilities and no validations are performed.  These apis should not be called by outside programs.
 * <p/>
 * This class is thread safe.
 * <p/>

 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class AttrHelper
{

    /**
     * Private constructor
     *
     */
    private AttrHelper()
    {
    }

    /**
     * Perform copy on ARBAC attributes.  This is used during assignment of {@link org.apache.directory.fortress.core.model.AdminRole} to {@link org.apache.directory.fortress.core.model.User}.
     * This method does not perform input validations.
     *
     * @param srcR contains source attributes to copy.
     * @param trgR contains the target reference.
     */
    public static void copyAdminAttrs(Administrator srcR, Administrator trgR)
    {
        trgR.setBeginInclusive(srcR.isBeginInclusive());
        trgR.setEndInclusive(srcR.isEndInclusive());
        trgR.setBeginRange(srcR.getBeginRange());
        trgR.setEndRange(srcR.getEndRange());
        // copy the user and perm pools:
        trgR.setOsP(srcR.getOsP());
        trgR.setOsU(srcR.getOsU());
    }

    /**
     * Convert from a {@link java.util.List} of properties stored as name:value pairs to
     * a {@link java.util.Properties}.
     *
     * @param propList contains a list of name-value pairs separated by a ':'.
     * @return reference to a Properties collection.
     */
    public static Properties getProperties(List<String> propList)
    {
        return getProperties(propList, GlobalIds.PROP_SEP );
    }

    /**
     * Convert from a {@link java.util.List} of properties stored as name:value pairs to
     * a {@link java.util.Properties}.
     *
     * @param propList contains a list of name-value pairs separated by a ':'.
     * @param separator contains char to be used to separate key and value.
     * @return reference to a Properties collection.
     */
    public static Properties getProperties( List<String> propList, char separator )
    {
        Properties props = null;
        if (propList != null && propList.size() > 0)
        {
            props = new Properties();
            propList.size();
            for (String raw : propList)
            {
                int indx = raw.indexOf(separator);
                if (indx >= 1)
                {
                    props.setProperty(raw.substring(0, indx), raw.substring(indx + 1));
                }
            }
        }
        return props;
    }

    /**
     * Convert from a comma delimited list of name-value pairs separated by a ':'.  Return the pros as {@link java.util.Properties}.
     *
     * @param inputString contains comma delimited list of properties.
     * @return java collection class containing props.
     */
    public static Properties getProperties( String inputString )
    {
        return getProperties( inputString, GlobalIds.PROP_SEP );
    }

    /**
     * Convert from a comma delimited list of name-value pairs separated by a ':'.  Return the pros as {@link java.util.Properties}.
     *
     * @param inputString contains comma delimited list of properties.
     * @param separator contains char to be used to separate key and value.
     * @return java collection class containing props.
     */
    public static Properties getProperties( String inputString, char separator )
    {
        return getProperties( inputString, separator, GlobalIds.DELIMITER );
    }

    /**
     * Convert from a comma delimited list of name-value pairs separated by a ':'.  Return the pros as {@link java.util.Properties}.
     *
     * @param inputString contains comma delimited list of properties.
     * @param separator contains char to be used to separate key and value.
     * @param delimiter contains a single char specifying delimiter between properties.
     * @return java collection class containing props.
     */
    public static Properties getProperties( String inputString, char separator, String delimiter )
    {
        Properties props = new Properties();
        if (inputString != null && inputString.length() > 0)
        {
            StringTokenizer maxTkn = new StringTokenizer(inputString, delimiter);
            if (maxTkn.countTokens() > 0)
            {
                while (maxTkn.hasMoreTokens())
                {
                    String val = maxTkn.nextToken();
                    int indx = val.indexOf(separator);
                    if (indx >= 1)
                    {
                        String name = val.substring(0, indx).trim();
                        String value = val.substring(indx + 1).trim();
                        props.setProperty(name, value);
                    }
                }
            }
        }
        return props;
    }

    /**
     * Parse a raw slapd access log format data string for userId.
     *
     * @param inputString raw slapd access log data.
     * @return string containing userId.
     */
    public static String getAuthZId(String inputString)
    {
        //reqAuthzID               <uid=fttu3user4,ou=people,dc=jts,dc=com>
        String userId = null;
        if (inputString != null && inputString.length() > 0)
        {
            StringTokenizer maxTkn = new StringTokenizer(inputString, ",");
            if (maxTkn.countTokens() > 0)
            {
                String val = maxTkn.nextToken();
                int indx = val.indexOf('=');
                if (indx >= 1)
                {
                    userId = val.substring(indx + 1);
                }
            }
        }
        return userId;
    }


    /**
     * Break the authZ eqDn attribute into 1. permission object name, 2. op name and 3. object id (optional).
     *
     * @param authZ contains the raw dn format from openldap slapo access log data
     * @return Permisison containing objName, opName and optionally the objId populated from the raw data.
     */
    public static Permission getAuthZPerm(AuthZ authZ) throws LdapInvalidDnException
    {
        // This will be returned to the caller:
        Permission pOp = new Permission();
        // Break dn into rdns for leaf and parent.  Use the 'type' field in rdn.
        // The objId value is optional.  If present it will be part of the parent's relative distinguished name..
        // Here the sample reqDN=ftOpNm=TOP2_2+ftObjId=002,ftObjNm=TOB2_1,ou=Permissions,ou=RBAC,dc=example,dc=com
        // Will be mapped to objName=TOB2_1, opName=TOP2_2, objId=002, in the returned permission object.
        Dn dn = new Dn( authZ.getReqDN() );
        if(dn != null && dn.getRdns() != null && ObjUtil.isNotNullOrEmpty( dn.getRdns() ) )
        {
            for( Rdn rdn : dn.getRdns() )
            {
                // The rdn type attribute will be mapped to objName, opName and objId fields.
                switch ( rdn.getType() )
                {
                    case GlobalIds.POP_NAME:
                        pOp.setOpName( rdn.getType() );
                        break;
                    case GlobalIds.POBJ_NAME:
                        pOp.setObjName( rdn.getType() );
                        break;
                    case GlobalIds.POBJ_ID:
                        pOp.setObjId( rdn.getType() );
                        break;
                }
            }
        }
        return pOp;
    }
}
