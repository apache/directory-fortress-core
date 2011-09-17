/*
 * Copyright © Joshua Tree Software, LLC, 2009-2011 All Rights Reserved.
 */

/*
 *  This class is used for testing purposes.
 */
package com.jts.fortress;

import com.jts.fortress.util.crypto.EncryptUtil;
import org.apache.log4j.Logger;

public class EncryptMgrConsole
{
    private static final String OCLS_NM = EncryptMgrConsole.class.getName();
    private static final Logger log = Logger.getLogger(OCLS_NM);


    protected void encrypt()
    {
        ReaderUtil.clearScreen();
        System.out.println("Enter text to encrypt:");
        String text = ReaderUtil.readLn();
        String myEncryptedText = EncryptUtil.encrypt(text);
        System.out.println("Encrypted value=" + myEncryptedText);
        ReaderUtil.readChar();
    }


    protected void decrypt()
    {
        ReaderUtil.clearScreen();
        System.out.println("Enter text to decrypt:");
        String text = ReaderUtil.readLn();
        String plainText = EncryptUtil.decrypt(text);
        System.out.println("Unencrypted value=" + plainText);
        ReaderUtil.readChar();
    }
}


