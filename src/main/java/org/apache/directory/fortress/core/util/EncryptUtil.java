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
package org.apache.directory.fortress.core.util;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Contains a simple wrapper for Jasypt open source encryption APIs, see <a href="http://www.jasypt.org/">Jasypt</a>.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class EncryptUtil
{
    private BasicTextEncryptor textEncryptor;
    private static final String CRYPTO_PROP = "crypto.prop";
    
    private static volatile EncryptUtil sINSTANCE = null;
    
    public static EncryptUtil getInstance()
    {
        if(sINSTANCE == null)
        {
            synchronized (EncryptUtil.class)
            {
                if(sINSTANCE == null)
                {
                    sINSTANCE = new EncryptUtil();
                }
            }
        }
        return sINSTANCE;
    }
    
    private void init()
    {
        textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(Config.getInstance().getProperty(CRYPTO_PROP, "adlfarerovcja;39 d"));
    }

    /**
     * Private constructor
     *
     */
    private EncryptUtil()
    {
        init();
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
            String encryptedValue = EncryptUtil.getInstance().encrypt(args[0]);
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
        if(Config.getInstance().getProperty(CRYPTO_PROP)!= null && !Config.getInstance().getProperty(CRYPTO_PROP).equals("${crypto.prop}"))
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
    public String encrypt(String clearText)
    {
        return textEncryptor.encrypt(clearText);
    }

    /**
     * Decrypt a value using Jasypt utility.
     *
     * @param encryptedText contains the text to be decrypted.
     * @return String containing decrypted text.
     */
    public String decrypt(String encryptedText)
    {
        return textEncryptor.decrypt(encryptedText);
    }
}

