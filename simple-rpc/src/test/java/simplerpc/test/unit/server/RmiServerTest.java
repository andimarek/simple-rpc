package simplerpc.test.unit.server;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import simplrpc.server.RpcServer;
import simplrpc.server.internal.ServerSocketManager;
import simplrpc.shared.SimpleRpcException;



public class RmiServerTest{

    private interface Interface{

    }

    private class ImplForInterface implements Interface{

    }

    class RmiServerUT extends RpcServer{

        public RmiServerUT( int port ){
            super( port );
        }

        @Override
        protected ServerSocketManager createServerSocketManager( int port ){
            return serverSocketManagerMock;
        }
    }

    private static final int NOT_USED_PORT = -1;
    private RpcServer rmiServer;
    private ServerSocketManager serverSocketManagerMock;

    @Before
    public void setUp(){
        serverSocketManagerMock = mock( ServerSocketManager.class );
        rmiServer = new RmiServerUT( NOT_USED_PORT );
    }

    @Test(expected = IllegalArgumentException.class)
    public void addImplementationNoInterfaceException(){
        rmiServer.addImplementation( Object.class, new Object() );
    }

    @Test(expected = NullPointerException.class)
    public void addImplementationNullPointerExceptionForInterface(){
        rmiServer.addImplementation( null, new Object() );
    }

    @Test(expected = NullPointerException.class)
    public void addImplementationNullPointerExceptionForImplementation(){
        rmiServer.addImplementation( Interface.class, null );
    }

    @Test(expected = IllegalArgumentException.class)
    public void moreThanOneImplementationThrowsIllegalArgumentException(){
        rmiServer.addImplementation( Interface.class, new Interface(){} );
        rmiServer.addImplementation( Interface.class, new Interface(){} );
    }

    @Test
    public void hasImplementationAfterAdding(){
        rmiServer.addImplementation( Interface.class, new ImplForInterface() );
        assertTrue( rmiServer.hasImplementation( Interface.class ) );
    }

    @Test
    public void rmiServerStarted(){
        verify( serverSocketManagerMock ).start();
    }

    @Test
    public void rmiServerStopped(){
        rmiServer.stop();
        verify( serverSocketManagerMock ).stop();
    }

}
