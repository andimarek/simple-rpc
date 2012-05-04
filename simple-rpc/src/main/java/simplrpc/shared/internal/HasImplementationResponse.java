package simplrpc.shared.internal;

public class HasImplementationResponse extends Response{

    private static final long serialVersionUID = 1L;

    private final boolean hasImplementation;

    public HasImplementationResponse( boolean hasImplementation ){
        this.hasImplementation = hasImplementation;
    }

    public boolean isHasImplementation(){
        return hasImplementation;
    }

}
