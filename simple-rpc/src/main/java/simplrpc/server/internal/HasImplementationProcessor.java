package simplrpc.server.internal;

import simplrpc.shared.internal.HasImplementationRequest;
import simplrpc.shared.internal.HasImplementationResponse;

public class HasImplementationProcessor{

    private final ImplementationContainer implementationContainer;
    private final HasImplementationRequest request;

    public HasImplementationProcessor( ImplementationContainer implementationContainer, HasImplementationRequest request ){
        this.implementationContainer = implementationContainer;
        this.request = request;
    }

    public HasImplementationResponse process(){
        boolean hasImplementation = implementationContainer.containsImplementation( request.getFqInterfaceName() );
        HasImplementationResponse response = new HasImplementationResponse( hasImplementation );
        return response;
    }

}
