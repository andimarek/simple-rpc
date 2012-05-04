package simplrpc.shared;

public class SimpleRmiException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public SimpleRmiException(){
        super();
    }

    public SimpleRmiException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ){
        super( message, cause, enableSuppression, writableStackTrace );
    }

    public SimpleRmiException( String message, Throwable cause ){
        super( message, cause );
    }

    public SimpleRmiException( String message ){
        super( message );
    }

    public SimpleRmiException( Throwable cause ){
        super( cause );
    }

}
