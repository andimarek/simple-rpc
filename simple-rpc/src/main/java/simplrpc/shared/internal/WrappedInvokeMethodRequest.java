package simplrpc.shared.internal;

/**
 * Wraps a raw request data stream a byte array. This is needed to determine the right class loader before deserializing.
 */
public class WrappedInvokeMethodRequest extends Request{

    private static final long serialVersionUID = 1L;

    private final String interfaceName;
    private final byte[] rawMethodInvocationRequest;

    public WrappedInvokeMethodRequest( String interfaceName, byte[] methodInvocationRequest ){
        this.interfaceName = interfaceName;
        this.rawMethodInvocationRequest = methodInvocationRequest;
    }

    public String getInterfaceName(){
        return interfaceName;
    }

    public byte[] getRawMethodInvocationRequest(){
        return rawMethodInvocationRequest;
    }
}
