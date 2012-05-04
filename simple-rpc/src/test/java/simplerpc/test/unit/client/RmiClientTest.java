package simplerpc.test.unit.client;

import static org.mockito.Mockito.mock;

import org.junit.Test;

import simplrpc.client.RpcClient;
import simplrpc.client.internal.ConnectionHandler;



public class RmiClientTest{

    private static final String NOT_USED_HOST = "localhost";
    private static final int NOT_USED_PORT = -1;

    public class RmiClientUT extends RpcClient{

        public RmiClientUT(){
            super( NOT_USED_HOST, NOT_USED_PORT );
        }

        @Override
        protected ConnectionHandler createConnectionHandler(){
            return connectionHandlerMock;
        }
    }

    private final ConnectionHandler connectionHandlerMock = mock( ConnectionHandler.class );
    private final RpcClient rmiClient = new RmiClientUT();

    @Test(expected = NullPointerException.class)
    public void hasImpmenetationThrowsNullpointerWhenClassIsNull(){
        rmiClient.hasImplementation( null );
    }

}
