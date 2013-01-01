package com.jts.fortress.rest;

import com.jts.fortress.GlobalErrIds;
import com.jts.fortress.ObjectFactory;
import com.jts.fortress.RestException;
import com.jts.fortress.cfg.Config;
import com.jts.fortress.rbac.Props;
import com.jts.fortress.util.crypto.EncryptUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.helpers.IOUtils;
import org.apache.log4j.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;


/**
 * This utility class provides methods that wrap Apache's HTTP Client APIs.  This class is thread safe.
 *
 * @author Shawn McKinney
 */
public class RestUtils
{
    private static final String CLS_NM = RestUtils.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);
    private final static String HTTP_UID = Config.getProperty("http.user");
    private final static String HTTP_PW_PARAM = "http.pw";
    private final static String HTTP_PW = ((EncryptUtil.isEnabled()) ? EncryptUtil.decrypt(Config.getProperty(HTTP_PW_PARAM)) : Config.getProperty(HTTP_PW_PARAM));
    private final static String HTTP_HOST = Config.getProperty("http.host");
    private final static String HTTP_PORT = Config.getProperty("http.port");
    private static final String VERSION = System.getProperty("version");
    private static final String SERVICE = "enmasse-" + VERSION;
    private static final String URI = "http://" + HTTP_HOST + ":" + HTTP_PORT + "/" + SERVICE + "/";
    private static final int HTTP_OK = 200;
    private static final int HTTP_404_NOT_FOUND = 404;

    /**
     * Marshall the request into an XML String.
     *
     * @param request
     * @return String containing xml request
     * @throws RestException
     */
    public static String marshal(FortRequest request) throws RestException
    {
        String szRetValue;
        try
        {
            // Create a JAXB context passing in the class of the object we want to marshal/unmarshal
            final JAXBContext context = JAXBContext.newInstance(FortRequest.class);
            // =============================================================================================================
            // Marshalling OBJECT to XML
            // =============================================================================================================
            // Create the marshaller, that will transform the object into XML
            final Marshaller marshaller = context.createMarshaller();
            // Create a stringWriter to hold the XML
            final StringWriter stringWriter = new StringWriter();
            // Marshal the javaObject and write the XML to the stringWriter
            marshaller.marshal(request, stringWriter);
            szRetValue = stringWriter.toString();
        }
        catch (JAXBException je)
        {
            String error = CLS_NM + ".marshal caught JAXBException=" + je;
            throw new RestException(GlobalErrIds.REST_MARSHALL_ERR, error, je);
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
    public static FortResponse unmarshall(String szResponse) throws RestException
    {
        FortResponse response;
        try
        {
            // Create a JAXB context passing in the class of the object we want to marshal/unmarshal
            final JAXBContext context = JAXBContext.newInstance(FortResponse.class);
            // Create the unmarshaller, that will transform the XML back into an object
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            response = (FortResponse) unmarshaller.unmarshal(new StringReader(szResponse));
        }
        catch (JAXBException je)
        {
            String error = CLS_NM + ".unmarshall caught JAXBException=" + je;
            throw new RestException(GlobalErrIds.REST_UNMARSHALL_ERR, error, je);
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
    public static String get(String userId, String password, String id, String id2, String id3, String function) throws RestException
    {
        String url = URI + function + "/" + id;
        if (id2 != null)
        {
            url += "/" + id2;
        }
        if (id3 != null)
        {
            url += "/" + id3;
        }
        log.debug(CLS_NM + ".get function:" + function + ", id1:" + id + ", id2:" + id2 + ", id3:" + id3 + ", url: " + url);
        GetMethod get = new GetMethod(url);
        setMethodHeaders(get, userId, password);
        return handleHttpMethod(get);
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
    public static String get(String id, String id2, String id3, String function) throws RestException
    {
        String url = URI + function + "/" + id;
        if (id2 != null)
        {
            url += "/" + id2;
        }
        if (id3 != null)
        {
            url += "/" + id3;
        }
        log.debug(CLS_NM + ".get function:" + function + ", id1:" + id + ", id2:" + id2 + ", id3:" + id3 + ", url: " + url);
        GetMethod get = new GetMethod(url);
        setMethodHeaders(get, HTTP_UID, HTTP_PW);
        return handleHttpMethod(get);
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
    public static String post(String userId, String password, String szInput, String function) throws RestException
    {
        log.debug(CLS_NM + ".post URI=[" + URI + "], function=[" + function + "], request=" + szInput);
        String szResponse = null;
        PostMethod post = new PostMethod(URI + function);
        post.addRequestHeader("Accept", "text/xml");
        setMethodHeaders(post, userId, password);
        try
        {
            RequestEntity entity = new StringRequestEntity(szInput, "text/xml; charset=ISO-8859-1", null);
            post.setRequestEntity(entity);
            HttpClient httpclient = new HttpClient();
            int result = httpclient.executeMethod(post);
            szResponse = IOUtils.toString(post.getResponseBodyAsStream(), "UTF-8");
            log.debug(CLS_NM + ".post URI=[" + URI + "], function=[" + function + "], response=" + szResponse);
        }
        catch (IOException ioe)
        {
            String error = CLS_NM + ".post URI=[" + URI + "], [" + function + "] caught IOException=" + ioe;
            log.error(error);
            throw new RestException(GlobalErrIds.REST_IO_ERR, error, ioe);
        }
        catch (WebApplicationException we)
        {
            String error = CLS_NM + ".post URI=[" + URI + "], function=[" + function + "] caught WebApplicationException=" + we;
            log.error(error);
            throw new RestException(GlobalErrIds.REST_WEB_ERR, error, we);
        }
        finally
        {
            // Release current connection to the connection pool.
            post.releaseConnection();
        }
        return szResponse;
    }

    /**
     * Perform an HTTP Post REST operation.
     *
     * @param szInput
     * @param function
     * @return String containing response
     * @throws RestException
     */
    public static String post(String szInput, String function) throws RestException
    {
        log.debug(CLS_NM + ".post URI=[" + URI + "], function=[" + function + "], request=" + szInput);
        String szResponse = null;
        PostMethod post = new PostMethod(URI + function);
        post.addRequestHeader("Accept", "text/xml");
        setMethodHeaders(post, HTTP_UID, HTTP_PW);
        try
        {
            RequestEntity entity = new StringRequestEntity(szInput, "text/xml; charset=ISO-8859-1", null);
            post.setRequestEntity(entity);
            HttpClient httpclient = new HttpClient();
            int result = httpclient.executeMethod(post);
            if(result == HTTP_OK)
            {
                szResponse = IOUtils.toString(post.getResponseBodyAsStream(), "UTF-8");
                log.debug(CLS_NM + ".post URI=[" + URI + "], function=[" + function + "], response=" + szResponse);
            }
            else if(result == HTTP_404_NOT_FOUND)
            {
                String error = CLS_NM + ".post URI=[" + URI + "], function=[" + function + "], 404 not found from host";
                log.error(error);
                throw new RestException(GlobalErrIds.REST_NOT_FOUND_ERR, error);
            }
            else
            {
                String error = CLS_NM + ".post URI=[" + URI + "], function=[" + function + "], error received from host: " + result;
                log.error(error);
                throw new RestException(GlobalErrIds.REST_UNKNOWN_ERR, error);
            }
        }
        catch (IOException ioe)
        {
            String error = CLS_NM + ".post URI=[" + URI + "], function=[" + function + "] caught IOException=" + ioe;
            log.error(error);
            throw new RestException(GlobalErrIds.REST_IO_ERR, error, ioe);
        }
        catch (WebApplicationException we)
        {
            String error = CLS_NM + ".post URI=[" + URI + "], function=[" + function + "] caught WebApplicationException=" + we;
            log.error(error);
            throw new RestException(GlobalErrIds.REST_WEB_ERR, error, we);
        }
        finally
        {
            // Release current connection to the connection pool.
            post.releaseConnection();
        }
        return szResponse;
    }

    /**
     * Set these params into their associated HTTP header vars.
     *
     * @param httpMethod
     * @param name
     * @param password
     */
    public static void setMethodHeaders(HttpMethod httpMethod, String name, String password)
    {
        if (httpMethod instanceof PostMethod || httpMethod instanceof PutMethod)
        {
            httpMethod.setRequestHeader("Content-Type", "application/xml");
            httpMethod.setRequestHeader("Accept", "application/xml");
        }
        //httpMethod.setDoAuthentication(false);
        httpMethod.setDoAuthentication(true);
        httpMethod.setRequestHeader("Authorization",
            "Basic " + base64Encode(name + ":" + password));
    }

    /**
     * Convert from non-Base64 to Base64 encoded.
     *
     * @param value
     * @return String contains encoded data
     */
    public static String base64Encode(String value)
    {
        return Base64Utility.encode(value.getBytes());
    }

    /**
     * Process the HTTP method request.
     *
     * @param httpMethod
     * @return String containing response
     * @throws Exception
     */
    public static String handleHttpMethod(HttpMethod httpMethod) throws RestException
    {
        HttpClient client = new HttpClient();
        String szResponse = null;
        try
        {
            int statusCode = client.executeMethod(httpMethod);
            log.debug(CLS_NM + ".handleHttpMethod Response status : " + statusCode);

            Response.Status status = Response.Status.fromStatusCode(statusCode);

            if (status == Response.Status.OK)
            {
                szResponse = httpMethod.getResponseBodyAsString();
                log.debug(CLS_NM + szResponse);
            }
            else if (status == Response.Status.FORBIDDEN)
            {
                log.debug(CLS_NM + ".handleHttpMethod Authorization failure");
            }
            else if (status == Response.Status.UNAUTHORIZED)
            {
                log.debug(CLS_NM + ".handleHttpMethod Authentication failure");
            }
            else
            {
                log.debug(CLS_NM + ".handleHttpMethod Unknown error");
            }
        }
        catch (IOException ioe)
        {
            String error = CLS_NM + ".handleHttpMethod caught IOException=" + ioe;
            log.error(error);
            throw new RestException(GlobalErrIds.REST_IO_ERR, error, ioe);
        }
        finally
        {
            // Release current connection to the connection pool.
            httpMethod.releaseConnection();
        }
        return szResponse;
    }

    /**
     * @param inProps
     * @return Properties
     */
    public static final Properties getProperties(Props inProps)
    {
        Properties properties = null;
        List<Props.Entry> props = inProps.getEntry();
        if (props.size() > 0)
        {
            properties = new Properties();
            int size = props.size();
            for (int i = 0; i < size; i++)
            {
                Props.Entry entry = props.get(i);
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                properties.setProperty(key, val);
            }
        }
        return properties;
    }


    /**
     *
     * @param properties
     * @return Prop contains name value pairs.
     */
    public static final Props getProps(Properties properties)
    {
        Props props = null;
        if (properties != null)
        {
            props = new ObjectFactory().createProps();
            for (Enumeration e = properties.propertyNames(); e.hasMoreElements(); )
            {
                String key = (String) e.nextElement();
                String val = properties.getProperty(key);
                Props.Entry entry = new Props.Entry();
                entry.setKey(key);
                entry.setValue(val);
                props.getEntry().add(entry);
            }
        }
        return props;
    }
}