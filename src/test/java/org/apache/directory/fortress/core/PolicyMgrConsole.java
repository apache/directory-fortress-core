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
package org.apache.directory.fortress.core;


import org.apache.directory.fortress.core.model.PwPolicy;
import org.apache.directory.fortress.core.impl.TestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class PolicyMgrConsole
{
    private PwPolicyMgr pm = null;
    final private static String PWDATTRIBUTE = "This attribute contains the name of the attribute to which the password policy is applied. For example, the password policy may be  applied  to the userPassword attribute. \n Note:  in this implementation, the only value accepted for pwdAttribute is  userPassword .";
    final private static String PWDMINAGE = "This attribute contains the number of seconds that must elapse  between modifications allowed  to  the    password.  If  this  attribute    is not present, zero seconds is assumed (i.e. the  password  may  be  modified whenever and however often is desired).";
    final private static String PWDMAXAGE = "This attribute contains the number of seconds after which a modified password will expire.  If this attribute is not present, or if its value is zero (0), then passwords will not expire.";
    final private static String PWDINHISTORY = "This  attribute is used to specify the maximum number of used passwords that will be stored in the pwdHistory attribute.  If  the  pwdInHistory attribute  is  not present, or if its value is zero (0), used passwords will not be stored in pwdHistory and thus any previously-used password may be reused.";
    final private static String PWDCHECKQUALITY = "This attribute    indicates if and how password syntax will be checked while a password is being modified or added. If this attribute is not present, or its value is zero (0), no syntax checking will be done. If its value is one (1), the server will check the syntax, and if the server is unable to check the syntax, whether due to a client-side hashed password or some other reason, it will be accepted. If its value is two (2), the server will check the syntax, and if the server is unable to check the syntax it will return an error refusing the pass-word.";
    final private static String PWDMINLENGTH = "When syntax checking is enabled (see also the    pwdCheckSyntax attribute), this attribute contains the minimum number of characters that  will be accepted in a password. If this attribute is not present,minimum password length is not enforced. If the server is unable to check  the  length of the password, whether due to a client-side hashed password or some other reason, the server will, depending on the value of  pwdCheckSyntax,  either accept the password without checking it (if pwdCheckSyntax is zero (0) or one (1)) or refuse it (if pwdCheckSyntax is two (2)).";
    final private static String PWDEXPIREWARNING = "This attribute contains the maximum number of seconds before a password is due to expire that expiration warning messages will be returned to a user  who is authenticating to the directory.  If this attribute is not present, or if the value is zero (0), no warnings will be sent.";
    final private static String PWDGRACELOGINLIMIT = "This attribute contains the number of times that  an  expired  password may be used to authenticate a user to the directory. If this attribute is not present or if its value is zero (0), users with expired passwords will not be allowed to authenticate to the directory.";
    final private static String PWDLOCKOUT = "This attribute specifies the action that should be taken by the directory when a user has made a number of failed attempts to authenticate to the directory. If pwdLockout is set (its value is TRUE), the user will not be allowed to attempt to authenticate to the directory after there have been a specified number of consecutive failed bind attempts. The maximum number of consecutive failed bind attempts allowed is specified by the pwdMaxFailure attribute. If pwdLockout is not present, or if its value is FALSE, the password may be used to authenticate no matter how many consecutive failed bind attempts have been made.";
    final private static String PWDLOCKOUTDURATION = "This attribute contains the number of seconds during which the password cannot be used to authenticate the user to the directory due to too many  consecutive  failed bind attempts.  (See also pwdLockout and pwd-MaxFailure.)  If pwdLockoutDuration is not present, or if its value is zero  (0),  the password cannot be used to authenticate the user to the directory again until it is reset by an administrator.";
    final private static String PWDMAXFAILURE = "This attribute contains the number of consecutive failed bind attempts after which the password may not be used to authenticate a user to the directory.  If pwdMaxFailure is not present, or its value is zero (0), then a user will be allowed to continue to attempt to authenticate to the directory, no matter how many consecutive failed bind attempts have occurred with that user's DN. (See also pwdLockout and pwdLockoutDuration.)";
    final private static String PWDFAILURECOUNTINTERVAL = "This attribute contains the number of seconds after which old consecutive failed bind attempts are purged from the failure counter, even though no  successful  authentication  has  occurred.  If  pwdFailure-CountInterval is not present, or its value is zero (0), the failure counter will only be reset by a successful authentication.";
    final private static String PWDMUSTCHANGE = "This attribute specifies whether users must change their passwords when they first bind to the directory after a password is set or reset by the administrator, or not.  If pwdMustChange has a value of TRUE, users must change their passwords when they first bind to the directory after a password is set or reset by the administrator.  If pwd-MustChange is not present, or its value is FALSE, users are not required to change their password upon binding after the administrator sets or resets the password.";
    final private static String PWDALLOWUSERCHANGE = "This attribute specifies whether users are allowed to change their own passwords or not.  If pwdAllowUserChange is set to TRUE, or if the attribute is  not  present, users will be allowed to change their own passwords.  If its value is FALSE, users will not be allowed to change their own passwords.";
    final private static String PWDSAFEMODIFY = "This attribute denotes whether the user's existing password must be sent along with their new password when changing a password. If pwd-SafeModify is set to TRUE, the existing password must be sent along with the new password.  If the attribute is not present, or its value is  FALSE,  the existing password need not be sent along with the new password.";

    private static final String CLS_NM = PolicyMgrConsole.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );


    /**
     * put your documentation comment here
     */
    public PolicyMgrConsole()
    {
        try
        {
            pm = PwPolicyMgrFactory.createInstance( TestUtils.getContext() );
        }
        catch ( org.apache.directory.fortress.core.SecurityException e )
        {
            LOG.error( " constructor caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e );
        }
    }


    void add()
    {
        PwPolicy policy = new PwPolicy();
        try
        {
            /*
             *  public class PwPolicy
             *  {
                    private String name;
                    private String attribute;
                    private Integer minAge;
                    private Integer maxAge;
                    private Short inHistory;
                    private Short checkQuality;
                    private Short minLength;
                    private Integer expireWarning;
                    private Short graceLoginLimit;
                    private Boolean lockout;
                    private Integer lockoutDuration;
                    private Short maxFailure;
                    private Short failureCountInterval;
                    private Boolean mustChange;
                    private Boolean allowUserChange;
                    private Boolean safeModify;
             *  }
             */
            ReaderUtil.clearScreen();
            System.out.println( "ADD PASSWORD POLICY" );
            System.out.println( "Enter name:" );
            policy.setName( ReaderUtil.readLn() );

            System.out.println( PWDCHECKQUALITY );
            System.out.println( "Enter checkQuality:" );
            policy.setCheckQuality( new Short( ReaderUtil.readLn() ) );

            System.out.println( PWDMAXAGE );
            System.out.println( "Enter maxAge:" );
            policy.setMaxAge( new Long( ReaderUtil.readLn() ) );

            System.out.println( PWDMINAGE );
            System.out.println( "Enter minAge:" );
            policy.setMinAge( new Integer( ReaderUtil.readLn() ) );

            System.out.println( PWDMINLENGTH );
            System.out.println( "Enter minLength:" );
            policy.setMinLength( new Short( ReaderUtil.readLn() ) );

            System.out.println( PWDFAILURECOUNTINTERVAL );
            System.out.println( "Enter failureCountInterval:" );
            policy.setFailureCountInterval( new Short( ReaderUtil.readLn() ) );

            System.out.println( PWDMAXFAILURE );
            System.out.println( "Enter maxFailure:" );
            policy.setMaxFailure( new Short( ReaderUtil.readLn() ) );

            System.out.println( PWDSAFEMODIFY );
            System.out.println( "Enter safeModify:" );
            policy.setSafeModify( Boolean.valueOf( ReaderUtil.readLn() ) );

            System.out.println( PWDMUSTCHANGE );
            System.out.println( "Enter mustChange:" );
            policy.setMustChange( Boolean.valueOf( ReaderUtil.readLn() ) );

            System.out.println( PWDINHISTORY );
            System.out.println( "Enter inHistory:" );
            policy.setInHistory( new Short( ReaderUtil.readLn() ) );

            System.out.println( PWDGRACELOGINLIMIT );
            System.out.println( "Enter graceLoginLimit:" );
            policy.setGraceLoginLimit( new Short( ReaderUtil.readLn() ) );

            System.out.println( PWDLOCKOUT );
            System.out.println( "Enter lockout:" );
            policy.setLockout( Boolean.valueOf( ReaderUtil.readLn() ) );

            System.out.println( PWDLOCKOUTDURATION );
            System.out.println( "Enter lockoutDuration:" );
            policy.setLockoutDuration( new Integer( ReaderUtil.readLn() ) );

            System.out.println( PWDALLOWUSERCHANGE );
            System.out.println( "Enter allowUserChange:" );
            policy.setAllowUserChange( Boolean.valueOf( ReaderUtil.readLn() ) );

            System.out.println( PWDEXPIREWARNING );
            System.out.println( "Enter expireWarning:" );
            policy.setExpireWarning( new Long( ReaderUtil.readLn() ) );

            pm.add( policy );
            System.out.print( "policy name [" + policy.getName() + "]" );
            System.out.println( " has been added" );
            System.out.println( "ENTER to continue" );
        }
        catch ( SecurityException e )
        {
            LOG.error( "add caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e );
        }
        ReaderUtil.readChar();
    }
}
