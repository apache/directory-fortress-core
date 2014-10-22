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
package org.apache.directory.fortress.core.ldap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.directory.fortress.core.CfgRuntimeException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement the X509TrustManager interface which will be used during JSSE truststore manager initialization for LDAP
 * client-to-server communications over TLS/SSL.
 * It is used during certificate validation operations within JSSE.
 * <p/>
 * Note: This class allows self-signed certificates to pass the validation checks.
 *
 * @author Shawn McKinney
 */
public final class LdapClientTrustStoreManager implements X509TrustManager, Serializable
{
    // Logging
    private static final String CLS_NM = LdapClientTrustStoreManager.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    // Config variables
    private final boolean isExamineValidityDates;
    private final char[] trustStorePw;
    private final String trustStoreFile;
    private final String trustStoreFormat;

    /**
     * Constructor used by connection configuration utility to load trust store manager.
     *
     * @param trustStoreFile    contains fully qualified name of trust store file.
     * @param trustStorePw      contains the password for trust store
     * @param trustStoreFormat  contains the format for trust store
     * @param isExamineValidity boolean var determines if certificate will be examined for valid dates on load.
     */
    public LdapClientTrustStoreManager( final String trustStoreFile, final char[] trustStorePw,
        final String trustStoreFormat, final boolean isExamineValidity )
    {
        if ( trustStoreFile == null )
        {
            // Cannot continue, throw an unchecked exception:
            throw new CfgRuntimeException( GlobalErrIds.FT_CONFIG_JSSE_TRUSTSTORE_NULL,
                "FortressTrustStoreManager constructor : input file name is null" );
        }
        // contains the fully-qualified file name of a valid JSSE TrustStore on local file system:
        this.trustStoreFile = trustStoreFile;
        // the password to the JSSE TrustStore:
        this.trustStorePw = trustStorePw;
        // If true, verify the current date is within the validity period for every certificate in the TrustStore:
        this.isExamineValidityDates = isExamineValidity;
        if ( trustStoreFormat == null )
        {
            this.trustStoreFormat = KeyStore.getDefaultType();
        }
        else
        {
            this.trustStoreFormat = trustStoreFormat;
        }
    }

    /**
     * Determine if client certificate is to be trusted.
     *
     * @param x509Chain
     * @param authNType
     * @throws CertificateException
     */
    public synchronized void checkClientTrusted( final X509Certificate[] x509Chain,
        final String authNType ) throws CertificateException
    {
        // For each certificate in the chain, check validity:
        for ( final X509TrustManager trustMgr : getTrustManagers( x509Chain ) )
        {
            trustMgr.checkClientTrusted( x509Chain, authNType );
        }
    }

    /**
     * Determine if server certificate is to be trusted.
     *
     * @param x509Chain
     * @param authNType
     * @throws CertificateException
     */
    public synchronized void checkServerTrusted( final X509Certificate[] x509Chain, final String authNType ) throws
        CertificateException
    {
        for ( final X509TrustManager trustManager : getTrustManagers( x509Chain ) )
        {
            trustManager.checkServerTrusted( x509Chain, authNType );
        }
    }

    /**
     * Return the list of accepted issuers for this trust manager.
     *
     * @return array of accepted issuers
     */
    public synchronized X509Certificate[] getAcceptedIssuers()
    {
        return new X509Certificate[0];
    }

    /**
     * Return array of trust managers to caller.  Will verify that current date is within certs validity period.
     *
     * @param x509Chain contains input X.509 certificate chain.
     * @return array of X.509 trust managers.
     * @throws CertificateException if trustStoreFile instance variable is null.
     */
    private synchronized X509TrustManager[] getTrustManagers( final X509Certificate[] x509Chain ) throws
        CertificateException
    {
        // If true, verify the current date is within each certificates validity period.
        if ( isExamineValidityDates )
        {
            final Date currentDate = new Date();
            for ( final X509Certificate x509Cert : x509Chain )
            {
                x509Cert.checkValidity( currentDate );
            }
        }
        // The trustStoreFile should contain the fully-qualified name of a Java TrustStore on local file system.
        final File trustStoreFile = new File( this.trustStoreFile );
        if ( !trustStoreFile.exists() )
        {
            throw new CertificateException( "FortressTrustStoreManager.getTrustManagers : file not found" );
        }
        return loadTrustManagers( getTrustStore() );
    }

    /**
     * Return an array of X.509 TrustManagers.
     *
     * @param trustStore handle to input trustStore
     * @return array of trust managers
     * @throws CertificateException if problem occurs during TrustManager initialization.
     */
    private X509TrustManager[] loadTrustManagers( final KeyStore trustStore ) throws CertificateException
    {
        final X509TrustManager[] x509TrustManagers;
        try
        {
            final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance( TrustManagerFactory
                .getDefaultAlgorithm() );
            trustManagerFactory.init( trustStore );
            final TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            x509TrustManagers = new X509TrustManager[trustManagers.length];
            for ( int i = 0; i < trustManagers.length; i++ )
            {
                x509TrustManagers[i] = ( X509TrustManager ) trustManagers[i];
            }
        }
        catch ( NoSuchAlgorithmException e )
        {
            throw new CertificateException( "FortressTrustStoreManager.loadTrustManagers caught " +
                "NoSuchAlgorithmException", e );
        }
        catch ( KeyStoreException e )
        {
            throw new CertificateException( "FortressTrustStoreManager.loadTrustManagers caught KeyStoreException", e );
        }
        return x509TrustManagers;
    }

    /**
     * Load the TrustStore file into JSSE KeyStore instance.
     *
     * @return instance of JSSE KeyStore containing the LDAP Client's TrustStore file info.     *
     * @throws CertificateException if cannot process file load.
     */
    private KeyStore getTrustStore() throws CertificateException
    {
        final KeyStore trustStore;
        try
        {
            trustStore = KeyStore.getInstance( trustStoreFormat );
        }
        catch ( KeyStoreException e )
        {
            throw new CertificateException( "FortressTrustStoreManager.getTrustManagers caught KeyStoreException", e );
        }
        FileInputStream trustStoreInputStream = null;
        try
        {
            trustStoreInputStream = new FileInputStream( trustStoreFile );
            trustStore.load( trustStoreInputStream, trustStorePw );
        }
        catch ( NoSuchAlgorithmException e )
        {
            throw new CertificateException( "FortressTrustStoreManager.getTrustManagers caught " +
                "NoSuchAlgorithmException", e );
        }
        catch ( IOException e )
        {
            throw new CertificateException( "FortressTrustStoreManager.getTrustManagers caught KeyStoreException", e );
        }
        finally
        {
            // Close the input stream.
            if ( trustStoreInputStream != null )
            {
                try
                {
                    trustStoreInputStream.close();
                }
                catch ( IOException e )
                {
                    // Eat this ioexception because it shouldn't be a problem, but log just in case:
                    LOG.warn( "FortressTrustStoreManager.getTrustManagers finally block on input stream close " +
                        "operation caught IOException=" + e.getMessage() );
                }
            }
        }
        return trustStore;
    }
}
