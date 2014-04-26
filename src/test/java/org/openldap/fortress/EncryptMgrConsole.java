/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
 */

/*
 *  This class is used for testing purposes.
 */
package org.openldap.fortress;

import org.openldap.fortress.util.crypto.EncryptUtil;
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


