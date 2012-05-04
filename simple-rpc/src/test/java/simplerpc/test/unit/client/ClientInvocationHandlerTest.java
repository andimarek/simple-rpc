package simplerpc.test.unit.client;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.mockito.Mockito;

import simplrpc.client.internal.ClientInvocationHandler;
import simplrpc.client.internal.ConnectionHandler;
import simplrpc.shared.SimpleRmiException;
import simplrpc.shared.SimpleRmiIOException;
import simplrpc.shared.SimpleRmiRemoteException;
import simplrpc.shared.internal.InvokeMethodResponse;
import simplrpc.shared.internal.Request;
import simplrpc.shared.internal.InvokeMethodResponse.Type;



public class ClientInvocationHandlerTest{

    public interface Interface{

        void method1();

        String method2();

    }

    private final ConnectionHandler connectionHandler = mock( ConnectionHandler.class );
    private final ClientInvocationHandler clientInvocationHandler = new ClientInvocationHandler( connectionHandler, Interface.class );

    @Test(expected = SimpleRmiIOException.class)
    public void simpleRmiIOExceptionThrown() throws Throwable{

        when( connectionHandler.sendRequest( Mockito.any( Request.class ) ) ).thenThrow( new IOException() );

        Interface impl = mock( Interface.class );
        Method method = impl.getClass().getMethod( "method1" );
        clientInvocationHandler.invoke( impl, method, new Object[ 0 ] );
    }

    @Test(expected = SimpleRmiException.class)
    public void simpleRmiExceptionThrown() throws Throwable{
        when( connectionHandler.sendRequest( Mockito.any( Request.class ) ) ).thenThrow( new NoClassDefFoundError() );

        Interface impl = mock( Interface.class );
        Method method = impl.getClass().getMethod( "method1" );
        clientInvocationHandler.invoke( impl, method, new Object[ 0 ] );
    }

    @Test(expected = SimpleRmiRemoteException.class)
    public void simpleRmiRemoteExceptionThrown() throws Throwable{
        InvokeMethodResponse failureResponse = new InvokeMethodResponse( null, "exeptionToString", Type.EXCEPTION );
        when( connectionHandler.sendRequest( Mockito.any( Request.class ) ) ).thenReturn( failureResponse );

        Interface impl = mock( Interface.class );
        Method method = impl.getClass().getMethod( "method1" );
        clientInvocationHandler.invoke( impl, method, new Object[ 0 ] );
    }

    @Test
    public void normalResponse() throws Throwable{
        final String RETURN_VALUE = "RETURN_VALUE";
        InvokeMethodResponse okResponse = new InvokeMethodResponse( RETURN_VALUE, null, Type.OK );
        when( connectionHandler.sendRequest( Mockito.any( Request.class ) ) ).thenReturn( okResponse );

        Interface impl = mock( Interface.class );
        Method method = impl.getClass().getMethod( "method2" );
        Object result = clientInvocationHandler.invoke( impl, method, new Object[ 0 ] );
        assertEquals( RETURN_VALUE, result );
    }

}
