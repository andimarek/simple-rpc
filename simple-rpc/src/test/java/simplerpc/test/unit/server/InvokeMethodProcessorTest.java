package simplerpc.test.unit.server;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import simplrpc.server.internal.ImplementationContainer;
import simplrpc.server.internal.InvokeMethodProcessor;
import simplrpc.shared.internal.InvokeMethodRequest;
import simplrpc.shared.internal.InvokeMethodResponse;
import simplrpc.shared.internal.InvokeMethodResponse.Type;



public class InvokeMethodProcessorTest{

    public interface Interface{

        String testMethod();

        void testMethod2( String argument, int argument2 );

    }

    @Test
    public void methodInvocationWithNoArgmumentsAndStringReturn(){
        Interface impl = mock( Interface.class );
        final String methodReturnValue = "TEST_RETURN";
        when( impl.testMethod() ).thenReturn( methodReturnValue );

        InvokeMethodResponse response = createRequestAndProcess( impl, "testMethod", new ArrayList<Object>(), new ArrayList<Class< ? >>() );

        verify( impl ).testMethod();
        assertEquals( Type.OK, response.getType() );
        assertThat( ( String )response.getReturnValue(), is( methodReturnValue ) );
    }

    @Test
    public void typeIsExceptionWhenMethodThrowsException(){
        Interface impl = mock( Interface.class );
        when( impl.testMethod() ).thenThrow( new RuntimeException() );

        InvokeMethodResponse response = createRequestAndProcess( impl, "testMethod", new ArrayList<Object>(), new ArrayList<Class< ? >>() );
        assertEquals( Type.EXCEPTION, response.getType() );
    }

    @Test
    public void methodInvocationWithArgumentsAndVoidReturn(){
        Interface impl = mock( Interface.class );

        final String arg1 = "ARG1";
        final int arg2 = 1234567;
        List<Object> arguments = new ArrayList<Object>();
        arguments.add( arg1 );
        arguments.add( arg2 );
        List<Class< ? >> argumentTypes = new ArrayList<Class< ? >>();
        argumentTypes.add( String.class );
        argumentTypes.add( int.class );
        
        InvokeMethodResponse response = createRequestAndProcess( impl, "testMethod2", arguments, argumentTypes );
        
        verify( impl ).testMethod2( arg1, arg2 );
        assertEquals( Type.OK, response.getType() );
    }

    private InvokeMethodResponse createRequestAndProcess( Interface implementation, String methodName, List<Object> arguments, List<Class< ? >> argumentTypes ){
        String interfaceName = Interface.class.getName();
        InvokeMethodRequest request = new InvokeMethodRequest( interfaceName, methodName, arguments, argumentTypes );

        ImplementationContainer implContainer = new ImplementationContainer();
        implContainer.addImplementation( Interface.class, implementation );

        InvokeMethodProcessor invokeMethodProcessor = new InvokeMethodProcessor( request, implContainer );
        InvokeMethodResponse response = invokeMethodProcessor.process();
        return response;
    }
}
