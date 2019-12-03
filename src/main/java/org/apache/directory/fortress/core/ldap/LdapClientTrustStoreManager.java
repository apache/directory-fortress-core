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


import org.apache.directory.fortress.core.CfgRuntimeException;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.util.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;


/**
 * Implement the X509TrustManager interface which will be used during JSSE truststore manager initialization for LDAP
 * client-to-server communications over TLS/SSL.
 * It is used during certificate validation operations within JSSE.
 * <p>
 * There are the controlling fortress.properties:
 * <ul>
 *     <li>trust.store : contains the name of the truststore (must be fully qualified iff trust.store.onclasspath=false</li>
 *     <li>trust.store.password : contains the pw for the specified truststore</li>
 *     <li>trust.onclasspath : if false name must be fully qualified, otherwise file must be on classpath as named</li>
 * </ul>
 *
 * Note: This class allows self-signed certificates to pass the validation checks, if its root certificate is found in the truststore.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class LdapClientTrustStoreManager implements X509TrustManager, Serializable
{
    /** Default serialVersionUID */
    private static final long serialVersionUID = 1L;
    // Logging
    private static final String CLS_NM = LdapClientTrustStoreManager.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    // Config variables
    private final boolean isExamineValidityDates;
    private final char[] trustStorePw;
    // This is found on the classpath if trust.store.onclasspath = true (default), otherwise must include exact location on filepath:
    private final String trustStoreFile;
    private final String trustStoreFormat;


    /**
     * Constructor used by connection configuration utility to load trust store manager.
     *
     * @param trustStoreFile    contains name of trust store file.
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
                "LdapClientTrustStoreManager constructor : input file name is null" );
        }
        // contains the file name of a valid JSSE TrustStore found on classpath:
        this.trustStoreFile = trustStoreFile;
        // the password to the JSSE TrustStore:
        this.trustStorePw = trustStorePw.clone();
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
        String szTrustStoreOnClasspath = Config.getInstance().getProperty( GlobalIds.TRUST_STORE_ON_CLASSPATH );

        // If false or null, read the truststore from a fully qualified filename.
        if( szTrustStoreOnClasspath != null && szTrustStoreOnClasspath.equalsIgnoreCase( "false" ))
        {
            LOG.info( CLS_NM + ".getTrustManagers on filepath" );
            return getTrustManagersOnFilepath( x509Chain );
        }
        // Get it off the classpath
        else
        {
            LOG.info( CLS_NM + ".getTrustManagers on classpath" );
            return getTrustManagersOnClasspath( x509Chain );
        }
    }


    /**
     * Return array of trust managers to caller.  Will verify that current date is within certs validity period.
     *
     * @param x509Chain contains input X.509 certificate chain.
     * @return array of X.509 trust managers.
     * @throws CertificateException if trustStoreFile instance variable is null.
     */
    private synchronized X509TrustManager[] getTrustManagersOnClasspath( final X509Certificate[] x509Chain ) throws
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
        InputStream trustStoreInputStream = getTrustStoreInputStream();
        if (trustStoreInputStream == null)
        {
            throw new CertificateException("LdapClientTrustStoreManager.getTrustManagers : file not found");
        }
        try
        {
            trustStoreInputStream.close();
        }
        catch (IOException e)
        {
            // Eat this ioexception because it shouldn't be a problem, but log just in case:
            LOG.warn("LdapClientTrustStoreManager.getTrustManagers on input stream close " +
                    "operation caught IOException={}", e.getMessage());
        }
        return loadTrustManagers( getTrustStore() );
    }


    /**
     * Return array of trust managers to caller.  Will verify that current date is within certs validity period.
     *
     * @param x509Chain contains input X.509 certificate chain.
     * @return array of X.509 trust managers.
     * @throws CertificateException if trustStoreFile instance variable is null.
     */
    private synchronized X509TrustManager[] getTrustManagersOnFilepath( final X509Certificate[] x509Chain ) throws
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
            throw new CertificateException( "LdapClientTrustStoreManager.loadTrustManagers caught " +
                "NoSuchAlgorithmException", e );
        }
        catch ( KeyStoreException e )
        {
            throw new CertificateException( "LdapClientTrustStoreManager.loadTrustManagers caught KeyStoreException", e );
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
            throw new CertificateException( "LdapClientTrustStoreManager.getTrustManagers caught KeyStoreException", e );
        }
        InputStream trustStoreInputStream = null;
        try
        {
            trustStoreInputStream = getTrustStoreInputStream();
            trustStore.load( trustStoreInputStream, trustStorePw );
        }
        catch ( NoSuchAlgorithmException e )
        {
            throw new CertificateException( "LdapClientTrustStoreManager.getTrustManagers caught " +
                "NoSuchAlgorithmException", e );
        }
        catch ( IOException e )
        {
            throw new CertificateException( "LdapClientTrustStoreManager.getTrustManagers caught KeyStoreException", e );
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
                    LOG.warn( "LdapClientTrustStoreManager.getTrustStore finally block on input stream close " +
                        "operation caught IOException={}", e.getMessage() );
                }
            }
        }
        return trustStore;
    }


    /**
     * Read the trust store off the classpath.
     *
     * @return handle to inputStream containing the trust store
     * @throws CertificateException
     */
    private InputStream getTrustStoreInputStream() throws CertificateException
    {
        InputStream result = ResourceUtil.getInputStream(trustStoreFile);
        if (null == result)
        {
            throw new CertificateException("LdapClientTrustStoreManager.getTrustStoreInputStream file does not exist on fortress classpath" );
        }
        return result;
    }
}
