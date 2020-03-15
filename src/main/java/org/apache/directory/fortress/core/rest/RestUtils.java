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
package org.apache.directory.fortress.core.rest;


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.GlobalErrIds;
import org.apache.directory.fortress.core.RestException;
import org.apache.directory.fortress.core.model.FortRequest;
import org.apache.directory.fortress.core.model.FortResponse;
import org.apache.directory.fortress.core.model.ObjectFactory;
import org.apache.directory.fortress.core.model.Props;
import org.apache.directory.fortress.core.util.Config;
import org.apache.directory.fortress.core.util.EncryptUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.directory.fortress.core.GlobalIds.*;


/**
 * This utility class provides methods that wrap Apache's HTTP Client APIs.  This class is thread safe.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public final class RestUtils
{
    private static final String CLS_NM = RestUtils.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private static final int HTTP_OK = 200;
    private static final int HTTP_400_VALIDATION_EXCEPTION = 400;
    private static final int HTTP_401_UNAUTHORIZED = 401;
    private static final int HTTP_403_FORBIDDEN = 403;
    private static final int HTTP_404_NOT_FOUND = 404;
    private static final int HTTP_500_INTERNAL_SERVER_ERROR = 500;
    private static final String VALID_RESPONSE = "FortResponse";
    private static CachedJaxbContext cachedJaxbContext = new CachedJaxbContext();

    // static member contains this
    private static volatile RestUtils sINSTANCE = null;

    /**
     * Used to manage trust store properties.  If enabled, create SSL connection.
     *
     */
    private String trustStore;
    private String trustStorePw;

    // These members contain the http coordinates to a running fortress-rest instance:
    private String httpUid, httpPw, httpHost, httpPort, httpProtocol, fortressRestVersion, serviceName, uri;

    /**
     * create a new request and set its tenant id.
     * @param szContextId contains the tenant id
     * @return a brand new FortRequest
     */
    static FortRequest getRequest( String szContextId )
    {
        FortRequest request = new FortRequest();
        request.setContextId(szContextId);
        return request;
    }

    public static RestUtils getInstance()
    {
        if(sINSTANCE == null)
        {
            synchronized (RestUtils.class)
            {
                if(sINSTANCE == null)
                {
                    sINSTANCE = new RestUtils();
                }
            }
        }
        return sINSTANCE;
    }

    private void init()
    {
        httpUid = Config.getInstance().getProperty( HTTP_UID_PROP );
        httpPw = ( ( EncryptUtil.isEnabled() ) ? EncryptUtil.getInstance().decrypt( Config
            .getInstance().getProperty( HTTP_PW_PROP ) ) : Config.getInstance().getProperty( HTTP_PW_PROP ) );
        httpHost = Config.getInstance().getProperty( "http.host" );
        httpPort = Config.getInstance().getProperty( "http.port" );
        httpProtocol = Config.getInstance().getProperty( "http.protocol", "http" );
        trustStore = Config.getInstance().getProperty( "trust.store" );
        trustStorePw = Config.getInstance().getProperty( "trust.store.password" );
        fortressRestVersion = System.getProperty( "version" );
        serviceName = "fortress-rest-" + fortressRestVersion;
        uri = httpProtocol + "://" + httpHost + ":" + httpPort + "/" + serviceName + "/";
        LOG.info("HTTP Connect Properties: host:{}, port:{}, protocol:{}, version:{}, service:{}, uri:{}", httpHost, httpPort, httpProtocol, fortressRestVersion, serviceName, uri);
        if( StringUtils.isNotEmpty(trustStore ) && StringUtils.isNotEmpty(trustStorePw ) )
        {
            LOG.info( "javax.net.ssl.trustStore: {}", trustStore );
            System.setProperty( "javax.net.ssl.trustStore", trustStore );
            System.setProperty( "javax.net.ssl.trustStorePassword", trustStorePw );
        }
        //System.setProperty( "http.maxConnections", "50" );
    }

    private RestUtils(){
        init();
    }

    /**
     * Marshall the request into an XML String.
     *
     * @param request
     * @return String containing xml request
     * @throws RestException
     */
    public static String marshal( FortRequest request ) throws RestException
    {
        String szRetValue;
        try
        {
            // Create a JAXB context passing in the class of the object we want to marshal/unmarshal
            final JAXBContext context = cachedJaxbContext.getJaxbContext( FortRequest.class );
            // =============================================================================================================
            // Marshalling OBJECT to XML
            // =============================================================================================================
            // Create the marshaller, that will transform the object into XML
            final Marshaller marshaller = context.createMarshaller();
            // Create a stringWriter to hold the XML
            final StringWriter stringWriter = new StringWriter();
            // Marshal the javaObject and write the XML to the stringWriter
            marshaller.marshal( request, stringWriter );
            szRetValue = stringWriter.toString();
        }
        catch ( JAXBException je )
        {
            String error = "marshal caught JAXBException=" + je;
            throw new RestException( GlobalErrIds.REST_MARSHALL_ERR, error, je );
        }
        return szRetValue;
    }


    /**
     * Unmarshall the XML response into its associated Java objects.
     *
     * @param szResponse
     * @return FortResponse
     * @throws RestException
     */
    public static FortResponse unmarshall( String szResponse ) throws RestException
    {
        FortResponse response;
        try
        {
            // Create a JAXB context passing in the class of the object we want to marshal/unmarshal
            final JAXBContext context = cachedJaxbContext.getJaxbContext( FortResponse.class );

            // Create the unmarshaller, that will transform the XML back into an object
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            response = ( FortResponse ) unmarshaller.unmarshal( new StringReader( szResponse ) );
        }
        catch ( JAXBException je )
        {
            String error = "unmarshall caught JAXBException=" + je;
            throw new RestException( GlobalErrIds.REST_UNMARSHALL_ERR, error, je );
        }
        return response;
    }


    /**
     * Perform HTTP Get REST request.
     *
     * @param userId
     * @param password
     * @param id
     * @param id2
     * @param id3
     * @param function
     * @return String containing response
     * @throws RestException
     */
    public String get( String userId, String password, String id, String id2, String id3, String function )
        throws RestException
    {
        String url = uri + function + "/" + id;
        if ( id2 != null )
        {
            url += "/" + id2;
        }
        if ( id3 != null )
        {
            url += "/" + id3;
        }
        LOG.debug( "get function1:{}, id1:{}, id2:{}, id3:{}, url:{}", function, id, id2, id3, url );

        String szResponse;
        HttpGet get = null;
        try
        {
            get = new HttpGet(url);
            setMethodHeaders( get );
            szResponse = handleHttpMethod( get ,HttpClientBuilder.create().useSystemProperties()
                .setDefaultCredentialsProvider(getCredentialProvider(userId, password)).build() );
        }
        catch ( WebApplicationException we )
        {
            String error = generateErrorMessage( uri,function, "caught WebApplicationException=" + we.getMessage() );
            LOG.error( error, we);
            throw new RestException( GlobalErrIds.REST_WEB_ERR, error, we );
        }
        catch ( java.lang.NoSuchMethodError e )
        {
            String error = generateErrorMessage( uri, function, "caught NoSuchMethodError = "+ e.getMessage() );
            LOG.error( error, e );
            throw new RestException( GlobalErrIds.REST_UNKNOWN_ERR, error);
        }
        finally
        {
            // Release current connection to the connection pool.
            if ( get != null )
                get.releaseConnection();
        }

        return szResponse;
    }


    /**
     * Perform HTTP Get REST request.
     *
     * @param id
     * @param id2
     * @param id3
     * @param function
     * @return String containing response
     * @throws RestException
     */
    public String get( String id, String id2, String id3, String function ) throws RestException
    {
        return get( null, null, id, id2, id3, function );
    }


    /**
     * Perform an HTTP Post REST operation.
     *
     * @param userId
     * @param password
     * @param szInput
     * @param function
     * @return String containing response
     * @throws RestException
     */
    public String post( String userId, String password, String szInput, String function ) throws RestException
    {
        LOG.debug( "post uri=[{}], function=[{}], request=[{}]", uri, function, szInput );
        String szResponse = null;
        HttpPost post = new HttpPost( uri + function);
        post.addHeader( "Accept", "text/xml" );
        setMethodHeaders( post );
        try
        {
            HttpEntity entity = new StringEntity( szInput, ContentType.TEXT_XML );
            post.setEntity( entity );
            org.apache.http.client.HttpClient httpclient = HttpClientBuilder.create().useSystemProperties()
                .setDefaultCredentialsProvider(getCredentialProvider(userId, password)).build();
            HttpResponse response = httpclient.execute( post );
            String error;

            switch ( response.getStatusLine().getStatusCode() )
            {
                case HTTP_OK :
                    szResponse = IOUtils.toString( response.getEntity().getContent(), "UTF-8" );
                    if( StringUtils.isNotEmpty( szResponse ) && szResponse.contains(VALID_RESPONSE) )
                    {
                        LOG.debug( "post uri=[{}], function=[{}], response=[{}]", uri, function, szResponse );
                    }
                    else
                    {
                        error = generateErrorMessage( uri, function, "invalid response" );
                        LOG.error( error );
                        throw new RestException( GlobalErrIds.REST_NOT_FOUND_ERR, error );
                    }
                    break;
                case HTTP_401_UNAUTHORIZED :
                    error = generateErrorMessage( uri, function, "401 function unauthorized on host" );
                    LOG.error( error );
                    throw new RestException( GlobalErrIds.REST_UNAUTHORIZED_ERR, error );
                case HTTP_403_FORBIDDEN :
                    error = generateErrorMessage( uri, function, "403 function forbidden on host" );
                    LOG.error( error );
                    throw new RestException( GlobalErrIds.REST_FORBIDDEN_ERR, error );
                case HTTP_404_NOT_FOUND:
                    szResponse = IOUtils.toString( response.getEntity().getContent(), "UTF-8" );
                    // Crack the response and see if it can be parsed as a valid Fortress Response object or generic HTTP:
                    if( StringUtils.isNotEmpty( szResponse ) && szResponse.contains(VALID_RESPONSE) )
                    {
                        LOG.debug( "HTTP: 404: post uri=[{}], function=[{}], response=[{}]", uri, function, szResponse );
                    }
                    else
                    {
                        error = generateErrorMessage( uri, function, "HTTP Error:" + response.getStatusLine().getStatusCode());
                        LOG.error( error );
                        throw new RestException( GlobalErrIds.REST_NOT_FOUND_ERR, error );
                    }
                    break;
                case HTTP_500_INTERNAL_SERVER_ERROR:
                    szResponse = IOUtils.toString( response.getEntity().getContent(), "UTF-8" );
                    // Crack the response and see if it can be parsed as a valid Fortress Response object or generic HTTP:
                    if( StringUtils.isNotEmpty( szResponse ) && szResponse.contains(VALID_RESPONSE) )
                    {
                        LOG.debug( "HTTP 500: post uri=[{}], function=[{}], response=[{}]", uri, function, szResponse );
                    }
                    else
                    {
                        error = generateErrorMessage( uri, function, "HTTP 500 Internal Error:" + response.getStatusLine().getStatusCode());
                        LOG.error( error );
                        throw new RestException( GlobalErrIds.REST_INTERNAL_ERR, error );
                    }
                    break;
                case HTTP_400_VALIDATION_EXCEPTION:
                    szResponse = IOUtils.toString( response.getEntity().getContent(), "UTF-8" );
                    // Crack the response and see if it can be parsed as a valid Fortress Response object or generic HTTP:
                    if( StringUtils.isNotEmpty( szResponse ) && szResponse.contains(VALID_RESPONSE) )
                    {
                        LOG.debug( "HTTP 400: post uri=[{}], function=[{}], response=[{}]", uri, function, szResponse );
                    }
                    else
                    {
                        error = generateErrorMessage( uri, function, "HTTP 400 Validation Error:" + response.getStatusLine().getStatusCode());
                        LOG.error( error );
                        throw new RestException( GlobalErrIds.REST_VALIDATION_ERR, error );
                    }
                    break;
                default :
                    error = generateErrorMessage( uri, function, "error received from host: " + response.getStatusLine().getStatusCode() );
                    LOG.error( error );
                    throw new RestException( GlobalErrIds.REST_UNKNOWN_ERR, error );
            }
        }
        catch ( IOException ioe )
        {
            String error = generateErrorMessage( uri, function, "caught IOException=" + ioe.getMessage() );
            LOG.error( error, ioe );
            throw new RestException( GlobalErrIds.REST_IO_ERR, error, ioe );
        }
        catch ( WebApplicationException we )
        {
            String error = generateErrorMessage( uri,function, "caught WebApplicationException=" + we.getMessage() );
            LOG.error( error, we);
            throw new RestException( GlobalErrIds.REST_WEB_ERR, error, we );
        }
        catch ( java.lang.NoSuchMethodError e )
        {
            String error = generateErrorMessage( uri, function, "caught Exception = "+ e.getMessage() );
            LOG.error( error, e );
            throw new RestException( GlobalErrIds.REST_UNKNOWN_ERR, error);
        }
        finally
        {
            // Release current connection to the connection pool.
            post.releaseConnection();
        }
        return szResponse;
    }

    private String generateErrorMessage( String uri, String function, String messageToShow ) {
        return new StringBuilder().append( "post uri=[" ).append( uri) .append( "], function=[" )
                .append( function ).append( "], " ).append( messageToShow ).toString();
    }


    /**
     * Perform an HTTP Post REST operation.
     *
     * @param szInput
     * @param function
     * @return String containing response
     * @throws RestException
     */
    public String post( String szInput, String function ) throws RestException
    {
        return post(null,null,szInput, function);
    }

    private CredentialsProvider getCredentialProvider(String uid, String password) {
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials( new AuthScope( httpHost,Integer.valueOf( httpPort )),
            new UsernamePasswordCredentials(uid==null? httpUid :uid,password==null? httpPw :password) );
        return credentialsProvider;
    }

    /**
     * Set these params into their associated HTTP header vars.
     *
     * @param httpRequest
     */
    private static void setMethodHeaders( HttpRequest httpRequest )
    {
        if ( httpRequest instanceof HttpPost || httpRequest instanceof HttpPut)
        {
            httpRequest.addHeader( "Content-Type", "application/xml" );
            httpRequest.addHeader( "Accept", "application/xml" );
        }
    }


    /**
     * Convert from non-Base64 to Base64 encoded.
     *
     * @param value
     * @return String contains encoded data
     */
    private static String base64Encode( String value )
    {
        return new String ( Base64.encodeBase64( value.getBytes() ) );
    }

/*    private static String base64Encode( String value )
    {
        return Base64Utility.encode( value.getBytes() );
    }*/


    /**
     * Process the HTTP method request.
     *
     * @param httpGetRequest
     * @return String containing response
     * @throws Exception
     */
    private static String handleHttpMethod( HttpRequestBase httpGetRequest ,org.apache.http.client.HttpClient client ) throws RestException
    {
        String szResponse = null;
        try
        {
            HttpResponse response = client.execute( httpGetRequest );
            LOG.debug( "handleHttpMethod Response status : {}", response.getStatusLine().getStatusCode() );

            Response.Status status = Response.Status.fromStatusCode( response.getStatusLine().getStatusCode() );

            if ( status == Response.Status.OK )
            {
                szResponse = IOUtils.toString( response.getEntity().getContent() );
                LOG.debug( szResponse );
            }
            else if ( status == Response.Status.FORBIDDEN )
            {
                LOG.debug( "handleHttpMethod Authorization failure" );
            }
            else if ( status == Response.Status.UNAUTHORIZED )
            {
                LOG.debug( "handleHttpMethod Authentication failure" );
            }
            else
            {
                LOG.debug( "handleHttpMethod Unknown error" );
            }
        }
        catch ( IOException ioe )
        {
            String error = "handleHttpMethod caught IOException=" + ioe;
            LOG.error( error );
            throw new RestException( GlobalErrIds.REST_IO_ERR, error, ioe );
        }
        finally
        {
            // Release current connection to the connection pool.
            httpGetRequest.releaseConnection();
        }
        return szResponse;
    }


    /**
     * @param inProps
     * @return Properties
     */
    public static Properties getProperties( Props inProps )
    {
        Properties properties = null;
        List<Props.Entry> props = inProps.getEntry();
        if ( props.size() > 0 )
        {
            properties = new Properties();
            //int size = props.size();
            for ( Props.Entry entry : props )
            {
                String key = entry.getKey();
                String val = entry.getValue();
                properties.setProperty( key, val );
            }
        }
        return properties;
    }


    /**
     *
     * @param properties
     * @return Prop contains name value pairs.
     */
    public static Props getProps( Properties properties )
    {
        Props props = null;
        if ( properties != null )
        {
            props = new ObjectFactory().createProps();
            for ( Enumeration<?> e = properties.propertyNames(); e.hasMoreElements(); )
            {
                String key = ( String ) e.nextElement();
                String val = properties.getProperty( key );
                Props.Entry entry = new Props.Entry();
                entry.setKey( key );
                entry.setValue( val );
                props.getEntry().add( entry );
            }
        }
        return props;
    }
}