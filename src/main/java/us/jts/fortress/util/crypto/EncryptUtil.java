/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.util.crypto;

import us.jts.fortress.cfg.Config;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Contains a simple wrapper for Jasypt open source encryption APIs, see <a href="http://www.jasypt.org/">Jasypt</a>.
 *
 * @author Shawn McKinney
 */
public class EncryptUtil
{
    private static final BasicTextEncryptor textEncryptor;
    private static String CRYPTO_PROP = "crypto.prop";
    static
    {
        textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(Config.getProperty(CRYPTO_PROP, "adlfarerovcja;39 d"));
    }

    /**
     * This wraps {@link #encrypt(String)} method.  Will return an encrypted value to standard out using System.out.println().
     * It can be used for ad-hoc encryption of vars {@code fortress.properties} bound.
     * @param args contains a single String to {@link #encrypt(String)}.
     */
    public static void main(String[] args)
    {
        if(args[0] != null && args[0].length() > 0)
        {
            String encryptedValue = textEncryptor.encrypt(args[0]);
            System.out.println("Encrypted value=" + encryptedValue);
        }
    }


    /**
     * Returns 'true' if the property {@code crypto.prop} has a value set in fortress cfg.  If this value
     * is 'false', Fortress will assume LDAP password in cfg file are clear text.
     *
     * @return boolean
     */
    public static boolean isEnabled()
    {
        boolean result = false;
        if(Config.getProperty(CRYPTO_PROP)!= null && !Config.getProperty(CRYPTO_PROP).equals("${crypto.prop}"))
        {
            result = true;
        }
        return result;
    }


    /**
     * Encrypt a value using Jasypt utility.
     *
     * @param clearText contains the text to be encrypted.
     * @return String containing encrypted text.
     */
    public static String encrypt(String clearText)
    {
        return textEncryptor.encrypt(clearText);
    }

    /**
     * Decrypt a value using Jasypt utility.
     *
     * @param encryptedText contains the text to be decrypted.
     * @return String containing decrypted text.
     */
    public static String decrypt(String encryptedText)
    {
        return textEncryptor.decrypt(encryptedText);
    }
}

