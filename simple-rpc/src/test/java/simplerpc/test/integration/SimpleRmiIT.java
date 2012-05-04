package simplerpc.test.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import simplerpc.test.util.PortUtil;
import simplrpc.client.RpcClient;
import simplrpc.server.RpcServer;



public class SimpleRmiIT{

    private RpcServer rmiServer;
    private RpcClient rmiClient;

    public interface Interface{

        String testMethod1();

        void testMethod2( String arg1, int arg2 );
    }

    @Before
    public void setUp(){
        int port = PortUtil.getFreePort();
        rmiServer = new RpcServer( port );
        rmiClient = new RpcClient( "localhost", port );
    }

    @After
    public void cleanUp(){
        rmiClient.disconnect();
        rmiServer.stop();
    }

    @Test
    public void serverHasNoImplementation(){
        boolean hasImpl = rmiClient.hasImplementation( Interface.class );
        assertFalse( hasImpl );
    }

    @Test
    public void serverHasImplementation(){
        Interface impl = mock( Interface.class );
        rmiServer.addImplementation( Interface.class, impl );
        boolean hasImpl = rmiClient.hasImplementation( Interface.class );
        assertTrue( hasImpl );
    }

    @Test
    public void callMethodWithNoArgumentAndReturnValue(){
        final String RESULT_TEST_METHOD1 = "RESULT_TEST_METHOD1";
        Interface impl = mock( Interface.class );
        when( impl.testMethod1() ).thenReturn( RESULT_TEST_METHOD1 );
        rmiServer.addImplementation( Interface.class, impl );
        Interface clientProxy = rmiClient.getImplementation( Interface.class );
        String returnValue = clientProxy.testMethod1();
        assertEquals( RESULT_TEST_METHOD1, returnValue );
    }

    @Test
    public void callVoidMethodWithArguments(){
        final String ARG1 = "ARG1";
        final int ARG2 = 123456;
        Interface impl = mock( Interface.class );
        rmiServer.addImplementation( Interface.class, impl );
        Interface clientProxy = rmiClient.getImplementation( Interface.class );
        clientProxy.testMethod2( ARG1, ARG2 );
        
        verify( impl ).testMethod2( ARG1, ARG2 );
    }

}
