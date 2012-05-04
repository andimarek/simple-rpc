package simplrpc.shared;


public class SimpleRmiRemoteException extends SimpleRmiException{

    private static final long serialVersionUID = 1L;

    public SimpleRmiRemoteException(){
        super();
    }

    public SimpleRmiRemoteException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ){
        super( message, cause, enableSuppression, writableStackTrace );
    }

    public SimpleRmiRemoteException( String message, Throwable cause ){
        super( message, cause );
    }

    public SimpleRmiRemoteException( String message ){
        super( message );
    }

    public SimpleRmiRemoteException( Throwable cause ){
        super( cause );
    }

}
