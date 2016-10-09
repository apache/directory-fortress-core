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

import org.apache.directory.fortress.core.util.EncryptUtil;
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
        String myEncryptedText = EncryptUtil.getInstance().encrypt(text);
        System.out.println("Encrypted value=" + myEncryptedText);
        ReaderUtil.readChar();
    }


    void decrypt()
    {
        ReaderUtil.clearScreen();
        System.out.println("Enter text to decrypt:");
        String text = ReaderUtil.readLn();
        String plainText = EncryptUtil.getInstance().decrypt(text);
        System.out.println("Unencrypted value=" + plainText);
        ReaderUtil.readChar();
    }
}


