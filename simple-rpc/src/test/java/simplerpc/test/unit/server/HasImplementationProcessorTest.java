package simplerpc.test.unit.server;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import simplrpc.server.internal.HasImplementationProcessor;
import simplrpc.server.internal.ImplementationContainer;
import simplrpc.shared.internal.HasImplementationRequest;
import simplrpc.shared.internal.HasImplementationResponse;



public class HasImplementationProcessorTest{

    interface Interface{

    }

    @Test
    public void hasImplementationReturnsTrue(){
        ImplementationContainer implementationContainer = new ImplementationContainer();
        Interface impl = new Interface(){};
        implementationContainer.addImplementation( Interface.class, impl );

        String fqName = Interface.class.getName();
        HasImplementationRequest request = new HasImplementationRequest( fqName );

        HasImplementationProcessor hasImplementationProcessor = new HasImplementationProcessor( implementationContainer, request );
        HasImplementationResponse response = hasImplementationProcessor.process();
        assertTrue( response.isHasImplementation() );
    }

    @Test
    public void hasImplementationReturnsFalse(){
        ImplementationContainer implementationContainer = new ImplementationContainer();

        String fqName = Interface.class.getName();
        HasImplementationRequest request = new HasImplementationRequest( fqName );

        HasImplementationProcessor hasImplementationProcessor = new HasImplementationProcessor( implementationContainer, request );
        HasImplementationResponse response = hasImplementationProcessor.process();
        assertFalse( response.isHasImplementation() );
    }
}
