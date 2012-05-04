package simplrpc.server.internal;

import java.lang.reflect.Method;

import simplrpc.shared.internal.InvokeMethodRequest;
import simplrpc.shared.internal.InvokeMethodResponse;
import simplrpc.shared.internal.InvokeMethodResponse.Type;



public class InvokeMethodProcessor{

    private final InvokeMethodRequest invokeMethodRequest;
    private final ImplementationContainer implementationContainer;

    public InvokeMethodProcessor( InvokeMethodRequest invokeMethodRequest, ImplementationContainer implementationContainer ){
        this.invokeMethodRequest = invokeMethodRequest;
        this.implementationContainer = implementationContainer;
    }

    public InvokeMethodResponse process(){
        try{
            Object implementation = implementationContainer.getImplementation( invokeMethodRequest.getInterfaceName() );
            if( implementation == null ) throw new RuntimeException( "Internal Error: No impl found" );
            Method method = findMethod( implementation );
            Object[] arguments = invokeMethodRequest.getArgumentsAsArray();
            Object result = method.invoke( implementation, arguments );
            InvokeMethodResponse response = new InvokeMethodResponse( result, null, Type.OK );
            return response;
        }
        catch( Exception e ){
            return createExceptionResponse( e );
        }
    }

    private InvokeMethodResponse createExceptionResponse( Exception e ){
        InvokeMethodResponse failedResponse = new InvokeMethodResponse( null, e.toString(), Type.EXCEPTION );
        return failedResponse;
    }

    private Method findMethod( Object implementation ) throws NoSuchMethodException, SecurityException{
        Class< ? >[] parameterTypes = invokeMethodRequest.getParameterTypesAsArray();
        String methodName = invokeMethodRequest.getMethodName();

        Method method = implementation.getClass().getMethod( methodName, parameterTypes );
        return method;
    }

}
