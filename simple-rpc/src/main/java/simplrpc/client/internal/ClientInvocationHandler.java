package simplrpc.client.internal;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import simplrpc.shared.SimpleRpcException;
import simplrpc.shared.SimpleRpcIOException;
import simplrpc.shared.SimpleRpcRemoteException;
import simplrpc.shared.internal.InvokeMethodRequest;
import simplrpc.shared.internal.InvokeMethodResponse;



public class ClientInvocationHandler implements InvocationHandler{

    private final ConnectionHandler connectionHandler;
    private final Class< ? > interfaceClazz;

    public ClientInvocationHandler( ConnectionHandler connectionHandler, Class< ? > interfaceClazz ){
        this.connectionHandler = connectionHandler;
        this.interfaceClazz = interfaceClazz;
    }

    @Override
    public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable{
        try{
            InvokeMethodRequest request = createRequest( method, args );
            InvokeMethodResponse response = ( InvokeMethodResponse )connectionHandler.sendRequest( request );
            switch ( response.getType() ){
                case OK:
                    return response.getReturnValue();
                case EXCEPTION:
                    throw new SimpleRpcRemoteException( response.getExceptionToString() );
                default:
                    throw new RuntimeException();
            }
        }
        catch( SimpleRpcException e ){
            throw e;
        }
        catch( IOException ioException ){
            throw new SimpleRpcIOException( ioException );
        }
        catch( Throwable e ){
            throw new SimpleRpcException( e );
        }

    }

    private InvokeMethodRequest createRequest( Method method, Object[] args ){
        String interfaceName = interfaceClazz.getName();
        String methodName = method.getName();
        List<Object> arguments = args != null ? Arrays.asList( args ) : new ArrayList<Object>();
        List<Class< ? >> parameterTypes = Arrays.asList( method.getParameterTypes() );
        InvokeMethodRequest request = new InvokeMethodRequest( interfaceName, methodName, arguments, parameterTypes );
        return request;
    }

}
