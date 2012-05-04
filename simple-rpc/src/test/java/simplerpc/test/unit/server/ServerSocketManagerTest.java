package simplerpc.test.unit.server;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import simplerpc.test.util.SimpleActionSynchronizer;
import simplrpc.server.internal.NewSocketCallback;
import simplrpc.server.internal.ServerSocketManager;



public class ServerSocketManagerTest{

    class ServerSocketManagerUT extends ServerSocketManager{

        public ServerSocketManagerUT( int port, NewSocketCallback newSocketCallback ){
            super( port, newSocketCallback );
        }

        @Override
        protected ServerSocket createServerSocket() throws IOException{
            return serverSocketMock;
        }

    }

    private static final int PORT_NOT_USED = 1;

    private ServerSocket serverSocketMock;
    private NewSocketCallback newSocketCallback;
    private ServerSocketManagerUT serverSocketManager;

    @Before
    public void setUp(){
        serverSocketMock = mock( ServerSocket.class );
        newSocketCallback = mock( NewSocketCallback.class );
        serverSocketManager = new ServerSocketManagerUT( PORT_NOT_USED, newSocketCallback );
    }

    private Answer<Object> acceptCalled( final SimpleActionSynchronizer simpleSynchronizer ){
        return new Answer<Object>(){

            @Override
            public Object answer( InvocationOnMock invocation ) throws Throwable{
                simpleSynchronizer.action();
                return null;
            }

        };
    }

    @Test
    public void startAcceptingNewConnections() throws IOException, InterruptedException{
        SimpleActionSynchronizer simpleSynchronizer = new SimpleActionSynchronizer( 5000 );
        doAnswer( acceptCalled( simpleSynchronizer ) ).when( serverSocketMock ).accept();
        serverSocketManager.start();
        simpleSynchronizer.waitForAction( "ServerSocket.accept not called" );
    }

    @Test
    public void serverSockedClosedAfterStopping() throws IOException{
        serverSocketManager.start();
        serverSocketManager.stop();
        verify( serverSocketMock ).close();
    }

}
