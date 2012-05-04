package simplrpc.shared;


public class SimpleRpcIOException extends SimpleRpcException{

    private static final long serialVersionUID = 1L;

    public SimpleRpcIOException(){
        super();
    }

    public SimpleRpcIOException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ){
        super( message, cause, enableSuppression, writableStackTrace );
    }

    public SimpleRpcIOException( String message, Throwable cause ){
        super( message, cause );
    }

    public SimpleRpcIOException( String message ){
        super( message );
    }

    public SimpleRpcIOException( Throwable cause ){
        super( cause );
    }
    
    

}
