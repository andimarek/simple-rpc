package simplrpc.shared;


public class SimpleRpcRemoteException extends SimpleRpcException{

    private static final long serialVersionUID = 1L;

    public SimpleRpcRemoteException(){
        super();
    }

    public SimpleRpcRemoteException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ){
        super( message, cause, enableSuppression, writableStackTrace );
    }

    public SimpleRpcRemoteException( String message, Throwable cause ){
        super( message, cause );
    }

    public SimpleRpcRemoteException( String message ){
        super( message );
    }

    public SimpleRpcRemoteException( Throwable cause ){
        super( cause );
    }

}
