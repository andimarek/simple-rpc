package simplrpc.shared;


public class SimpleRmiIOException extends SimpleRmiException{

    private static final long serialVersionUID = 1L;

    public SimpleRmiIOException(){
        super();
    }

    public SimpleRmiIOException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ){
        super( message, cause, enableSuppression, writableStackTrace );
    }

    public SimpleRmiIOException( String message, Throwable cause ){
        super( message, cause );
    }

    public SimpleRmiIOException( String message ){
        super( message );
    }

    public SimpleRmiIOException( Throwable cause ){
        super( cause );
    }
    
    

}
