/*
 * Copyright © 2009-2013, JoshuaTree.  All Rights Reserved.
 */

/*
 *  This class is used for testing purposes.
 */
package us.jts.fortress;

import us.jts.fortress.util.crypto.EncryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EncryptMgrConsole
{
    private static final String CLS_NM = EncryptMgrConsole.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );


    void encrypt()
    {
        ReaderUtil.clearScreen();
        System.out.println("Enter text to encrypt:");
        String text = ReaderUtil.readLn();
        String myEncryptedText = EncryptUtil.encrypt(text);
        System.out.println("Encrypted value=" + myEncryptedText);
        ReaderUtil.readChar();
    }


    void decrypt()
    {
        ReaderUtil.clearScreen();
        System.out.println("Enter text to decrypt:");
        String text = ReaderUtil.readLn();
        String plainText = EncryptUtil.decrypt(text);
        System.out.println("Unencrypted value=" + plainText);
        ReaderUtil.readChar();
    }
}


