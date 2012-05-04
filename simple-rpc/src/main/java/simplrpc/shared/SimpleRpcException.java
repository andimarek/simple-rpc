package simplrpc.shared;

public class SimpleRpcException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public SimpleRpcException(){
        super();
    }

    public SimpleRpcException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ){
        super( message, cause, enableSuppression, writableStackTrace );
    }

    public SimpleRpcException( String message, Throwable cause ){
        super( message, cause );
    }

    public SimpleRpcException( String message ){
        super( message );
    }

    public SimpleRpcException( Throwable cause ){
        super( cause );
    }

}
