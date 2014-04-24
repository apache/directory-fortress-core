package us.jts.fortress.jmeter;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.LoggerFactory;
import us.jts.fortress.AccelMgr;
import us.jts.fortress.AccelMgrFactory;
import us.jts.fortress.rbac.Session;
import us.jts.fortress.rbac.TestUtils;
import us.jts.fortress.rbac.User;

import static org.junit.Assert.*;

/**
 * Description of the Class
 *
 * @author Shawn McKinney
 */
public class AccelCreateSession extends AbstractJavaSamplerClient
{

    private boolean echoRequest = false;
    private boolean returnResult = false;
    private AccelMgr accelMgr;
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger( AccelCreateSession.class );
    private static int count = 0;
    private int ctr = 0;

    /**
     * Description of the Method
     *
     * @param samplerContext Description of the Parameter
     * @return Description of the Return Value
     */
    public SampleResult runTest( JavaSamplerContext samplerContext )
    {
        SampleResult sampleResult = new SampleResult();
        try
        {
            sampleResult.sampleStart();
            String fiKey = getKey( Thread.currentThread().getId() );
            assertNotNull( accelMgr );
            String message = "AC CreateSession TID: " + getThreadId() + " #:" + ctr++;
            LOG.info( message );
            System.out.println( message );
            Session session;
            User user = new User();
            // positive test case:
            user.setUserId( "rbacuser1" );
            user.setPassword( "secret".toCharArray() );
            session = accelMgr.createSession( user, false );
            assertNotNull( session );
            assertTrue( session.isAuthenticated() );
            sampleResult.sampleEnd();
            sampleResult.setBytes(1);
            sampleResult.setResponseMessage("test completed");
            sampleResult.setSuccessful(true);
            //accelMgr.deleteSession( session );
        }
        catch ( us.jts.fortress.SecurityException se )
        {
            se.printStackTrace();
            System.out.println( "ThreadId:" + getThreadId() + "Error running test: " + se );
            se.printStackTrace();
            sampleResult.setSuccessful( false );
        }

        return sampleResult;
    }

    /**
     * @param threadId
     * @return
     */
    synchronized private String getKey( long threadId )
    {
        return threadId + "-" + count++;
    }


    private String getThreadId()
    {
        return "" + Thread.currentThread().getId();
    }

    /**
     * Description of the Method
     *
     * @param samplerContext Description of the Parameter
     */
    public void setupTest( JavaSamplerContext samplerContext )
    {
        getKey( Thread.currentThread().getId() );
        String message = "AC SETUP CreateSession TID: " + getThreadId();
        LOG.info( message );
        System.out.println( message );
        try
        {
            accelMgr = AccelMgrFactory.createInstance( TestUtils.getContext() );
        }
        catch ( us.jts.fortress.SecurityException se )
        {
            se.printStackTrace();
            System.out.println( "AC SETUP ThreadId:" + getThreadId() + "Error setup test: " + se );
            se.printStackTrace();
        }
    }

    /**
     * Description of the Method
     *
     * @param samplerContext Description of the Parameter
     */
    public void teardownTest( JavaSamplerContext samplerContext )
    {
        String message = "AC TEARDOWN CreateSession TID: " + getThreadId();
        LOG.info( message );
        System.out.println( message );
    }
}

