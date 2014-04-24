/*
 * Copyright (c) 2009-2014, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.ldap.openldap;


import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.jts.fortress.GlobalIds;
import us.jts.fortress.ObjectFactory;
import us.jts.fortress.rbac.GlobalPwMsgIds;
import us.jts.fortress.rbac.PwMessage;
import us.jts.fortress.rbac.PwPolicyControl;

import com.unboundid.ldap.sdk.migrate.ldapjdk.LDAPControl;
import us.jts.fortress.rbac.Warning;


/**
 * This class reads the OpenLDAP password policy control and translates into data entity for Fortress.  In order for these checks
 * to be successful the OpenLDAP server must have enabled the pw policy overlay.  Read the OpenLDAP man pages for how this overlay works.
 * <p/>

 *
 * @author Shawn McKinney
 */
public class OLPWControlImpl implements PwPolicyControl
{
    private static final String CLS_NM = OLPWControlImpl.class.getName();
    private final static Logger LOG = LoggerFactory.getLogger( CLS_NM );


    /**
     * Reads the OpenLDAP password policy control and sets the PwMessage with what it finds.
     *
     * <p/>This function will use the password policy control that is contained within the ldap connection object.
     * Ber encoding:
     * <ul>
     * <li>  ------------------------------------------
     * <li>  PasswordPolicyResponseValue ::= SEQUENCE {
     * <li>  warning [0] CHOICE {
     * <li>  timeBeforeExpiration [0] INTEGER (0 .. maxInt),
     * <li>  graceLoginsRemaining [1] INTEGER (0 .. maxInt) } OPTIONAL
     * <li>  error [1] ENUMERATED {
     * <li>  passwordExpired        (0),
     * <li>  accountLocked          (1),
     * <li>  changeAfterReset       (2),
     * <li>  passwordModNotAllowed  (3),
     * <li>  mustSupplyOldPassword  (4),
     * <li>  invalidPasswordSyntax  (5),
     * <li>  passwordTooShort       (6),
     * <li>  passwordTooYoung       (7),
     * <li>  passwordInHistory      (8) } OPTIONAL }
     * <li>  ---
     * <li>  Old Encoding Scheme:
     * <li>  PPOLICY_WARNING    0xa0
     * <li>  PPOLICY_ERROR      0xa1
     * <li>  PPOLICY_EXPIRE     0xa0
     * <li>  PPOLICY_GRACE      0xa1
     * <li>  New Encoding Scheme:
     * <li>  PPOLICY_WARNING 0xa0
     * <li>  PPOLICY_ERROR 0x81
     * <li>  PPOLICY_EXPIRE 0x80
     * <li>  PPOLICY_GRACE  0x81
     * </ul>
     *
     * @param controls ldap controls object.
     * @param isAuthenticated set to 'true' if password checks pass.
     * @param pwMsg describes the outcome of the policy checks.
     */
    @Override
    public void checkPasswordPolicy( LDAPControl[] controls, boolean isAuthenticated, PwMessage pwMsg )
    {
        String methodName = "checkPasswordPolicy";
        pwMsg.setErrorId( GlobalPwMsgIds.GOOD );
        //pwMsg.setWarningId( GlobalPwMsgIds.PP_NOWARNING );
        pwMsg.setAuthenticated( isAuthenticated );

        if ( controls == null )
        {
            pwMsg.setWarning( new ObjectFactory().createWarning( GlobalPwMsgIds.NO_CONTROLS_FOUND, "PW CONTROLS NOT FOUND", Warning.Type.PASSWORD ) );
        }
        else if ( controls.length >= 1 )
        {
            for ( int i = 0; i < controls.length; i++ )
            {
                if ( LOG.isDebugEnabled() )
                {
                    LOG.debug( "{} controls[{}]={}", methodName, i, controls[i] );
                }

                LDAPControl con = controls[i];
                String id = con.getID();

                if ( id.compareTo( GlobalIds.OPENLDAP_PW_RESPONSE_CONTROL ) == 0 )
                {
                    byte[] rB = con.getValue();

                    if ( LOG.isDebugEnabled() )
                    {
                        LOG.debug( "{} control value length={}", methodName, rB.length );

                        String bytes = "";

                        for ( byte aRB : rB )
                        {
                            bytes = bytes + printRawData( aRB );
                        }

                        LOG.debug( "{} printRawData numbytes: {}", methodName, bytes );
                    }

                    if ( rB == null || rB[1] == 0 )
                    {
                        LOG.debug( methodName + " no password policy for user" );
                        pwMsg.setWarning( new ObjectFactory().createWarning( GlobalPwMsgIds.NOT_PW_POLICY_ENABLED, "NO PW POLICY ENABLED FOR USER", Warning.Type.PASSWORD ) );
                    }

                    if ( LOG.isDebugEnabled() )
                    {
                        LOG.debug( "{} byte[]={}", methodName, Arrays.toString( rB ) );
                        LOG.debug( "control.toString()={}", con.toString() );
                    }

                    int indx = 0;
                    int lBerObjType = getInt( rB[indx++] );

                    if ( LOG.isDebugEnabled() )
                    {
                        LOG.debug( "{} BER encoded object type={}", methodName, lBerObjType );
                    }

                    int msgLen = getInt( rB[indx++] );

                    while ( indx < msgLen )
                    {
                        switch ( rB[indx++] )
                        {
                            case ( byte ) 0xa0:
                                // BER Encoded byte array:
                                //client: 00110000 00000101 10100000
                                //  			     		^
                                //		PPOLICY_WARNING  0xa0
                                int policyWarnLen = getInt( rB[indx++] );

                                switch ( rB[indx++] )
                                {
                                    case ( byte ) 0xa0:
                                    case ( byte ) 0x80:
                                        //pwMsg.setWarningId( GlobalPwMsgIds.PASSWORD_EXPIRATION_WARNING );
                                        pwMsg.setWarning( new ObjectFactory().createWarning( GlobalPwMsgIds.PASSWORD_EXPIRATION_WARNING, "PASSWORD HAS EXPIRED", Warning.Type.PASSWORD ) );
                                        // BER Encoded byte array:
                                        // client: 00110000 00000110 10100000 00000100 10100000 00000010 00000010 00100100
                                        //							 ^                  ^                   ^
                                        //       PPOLICY_WARNING  0xa0 PPOLICY_EXPIRE 0xa0       EXP int==(decimal 548) 1000100100
                                        int expLength = getInt( rB[indx++] );
                                        int expire = getInt( rB[indx++] );

                                        for ( int k = 1; k < expLength; k++ )
                                        {
                                            expire = expire << 8;
                                            int next = getInt( rB[indx++] );
                                            expire = expire | next;
                                        }

                                        pwMsg.setExpirationSeconds( expire );

                                        if ( LOG.isDebugEnabled() )
                                        {
                                            LOG.debug( "{} User:{}, password expires in: {} seconds.", methodName, pwMsg.getUserId(), expire );
                                        }

                                        break;

                                    case ( byte ) 0xa1:
                                    case ( byte ) 0x81:
                                        pwMsg.setWarning( new ObjectFactory().createWarning( GlobalPwMsgIds.PASSWORD_GRACE_WARNING, "PASSWORD IN GRACE", Warning.Type.PASSWORD ) );
                                        // BER Encoded byte array:
                                        //client: 00110000 00000101 10100000 00000011 10100001 00000001 01100100
                                        //  			     		^                 ^                 ^
                                        //			PPOLICY_WARNING  0xa0   PPOLICY_GRACE 0xa1       grace integer value
                                        int graceLen = getInt( rB[indx++] );
                                        int grace = getInt( rB[indx++] );

                                        for ( int k = 1; k < graceLen; k++ )
                                        {
                                            grace = grace << 8;
                                            int next = getInt( rB[indx++] );
                                            grace = grace | next;
                                        }

                                        pwMsg.setGraceLogins( grace );

                                        if ( LOG.isDebugEnabled() )
                                        {
                                            LOG.debug( "{} UserId:{}, # logins left={}", methodName, pwMsg.getUserId() + grace );
                                        }

                                        break;

                                    default:
                                        pwMsg.setWarning( new ObjectFactory().createWarning( GlobalPwMsgIds.INVALID_PASSWORD_MESSAGE, "INVALID PASSWORD", Warning.Type.PASSWORD ) );

                                        if ( LOG.isDebugEnabled() )
                                        {
                                            LOG.debug( "{} UserId:{}, Invalid PPOlicy Type", methodName, pwMsg.getUserId() );
                                        }

                                        break;
                                }

                                break;

                            case ( byte ) 0xa1:
                            case ( byte ) 0x81:
                                // BER Encoded byte array:
                                //client: 00110000 00001011 10100000 00000110 10100000 00000100 00000001 11100001 00110011 01111101 10100001 00000001 00000010
                                //							 ^                  ^                 ^                                   ^                     ^
                                //		   PPOLICY_WARNING  0xa0 PPOLICY_EXPIRE 0xa0      expire int==(decimal 100)     PPOLICY_ERR 0xa1             ERR #==2
                                int errLen = getInt( rB[indx++] );
                                int err = getInt( rB[indx++] );

                                if ( LOG.isDebugEnabled() )
                                {
                                    LOG.debug( "{} UserId:{}, PPOLICY_ERROR={}", methodName, pwMsg.getUserId(), err);
                                }

                                switch ( err )
                                {
                                    case 0:
                                        pwMsg.setErrorId( GlobalPwMsgIds.PASSWORD_HAS_EXPIRED );
                                        break;

                                    case 1:
                                        pwMsg.setErrorId( GlobalPwMsgIds.ACCOUNT_LOCKED );
                                        break;

                                    case 2:
                                        pwMsg.setErrorId( GlobalPwMsgIds.CHANGE_AFTER_RESET );
                                        break;

                                    case 3:
                                        pwMsg.setErrorId( GlobalPwMsgIds.NO_MODIFICATIONS );
                                        break;

                                    case 4:
                                        pwMsg.setErrorId( GlobalPwMsgIds.MUST_SUPPLY_OLD );
                                        break;

                                    case 5:
                                        pwMsg.setErrorId( GlobalPwMsgIds.INSUFFICIENT_QUALITY );
                                        break;

                                    case 6:
                                        pwMsg.setErrorId( GlobalPwMsgIds.PASSWORD_TOO_SHORT );
                                        break;

                                    case 7:
                                        pwMsg.setErrorId( GlobalPwMsgIds.PASSWORD_TOO_YOUNG );
                                        break;

                                    case 8:
                                        pwMsg.setErrorId( GlobalPwMsgIds.HISTORY_VIOLATION );
                                        break;

                                    case 65535:
                                        pwMsg.setErrorId( GlobalPwMsgIds.GOOD );
                                        break;

                                    default:
                                        pwMsg.setErrorId( GlobalPwMsgIds.INVALID_PASSWORD_MESSAGE );
                                        break;
                                }

                                break;

                            default:
                                pwMsg.setWarning( new ObjectFactory().createWarning( GlobalPwMsgIds.INVALID_PASSWORD_MESSAGE, "INVALID POLICY MESSAGE TYPE", Warning.Type.PASSWORD ) );

                                if ( LOG.isDebugEnabled() )
                                {
                                    LOG.debug( "{} userId:{}, Invalid PPOlicy Message Type", methodName, pwMsg.getUserId());
                                }

                                break;
                        }
                    }
                }
                else
                {
                    pwMsg.setWarning( new ObjectFactory().createWarning( GlobalPwMsgIds.INVALID_PASSWORD_MESSAGE, "CANNOT PROCESS OPENLDAP POLICY CONTROL", Warning.Type.PASSWORD ) );

                    if ( LOG.isDebugEnabled() )
                    {
                        LOG.debug( "{} UserId:{},  Can't process LDAP control.", methodName, pwMsg.getUserId() );
                    }
                }
            }
        }
    }


    /**
     * @param bte
     * @return int
     */
    private static int getInt( byte bte )
    {
        return bte & 0xff;
    }


    /**
     * Description of the Method
     *
     * @param ch Description of the Parameter
     * @return Description of the Return Value
     */
    private static String printRawData( byte ch )
    {
        int B0 = 0x01;
        int B1 = 0x02;
        int B2 = 0x04;
        int B3 = 0x08;
        int B4 = 0x10;
        int B5 = 0x20;
        int B6 = 0x40;
        int B7 = 0x80;

        String byteString;
        if ( ( ch & B7 ) != 0 )
        {
            byteString = "1";
        }
        else
        {
            byteString = "0";
        }
        if ( ( ch & B6 ) != 0 )
        {
            byteString += "1";
        }
        else
        {
            byteString += "0";
        }
        if ( ( ch & B5 ) != 0 )
        {
            byteString += "1";
        }
        else
        {
            byteString += "0";
        }
        if ( ( ch & B4 ) != 0 )
        {
            byteString += "1";
        }
        else
        {
            byteString += "0";
        }
        if ( ( ch & B3 ) != 0 )
        {
            byteString += "1";
        }
        else
        {
            byteString += "0";
        }
        if ( ( ch & B2 ) != 0 )
        {
            byteString += "1";
        }
        else
        {
            byteString += "0";
        }
        if ( ( ch & B1 ) != 0 )
        {
            byteString += "1";
        }
        else
        {
            byteString += "0";
        }
        if ( ( ch & B0 ) != 0 )
        {
            byteString += "1";
        }
        else
        {
            byteString += "0";
        }
        byteString += " ";
        return byteString;
    }
}