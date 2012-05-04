package simplrpc.shared.internal;

public class HasImplementationRequest extends Request{

    private static final long serialVersionUID = 1L;

    private final String fqInterfaceName;

    public HasImplementationRequest( String fqInterfaceName ){
        this.fqInterfaceName = fqInterfaceName;
    }

    public String getFqInterfaceName(){
        return fqInterfaceName;
    }

}
