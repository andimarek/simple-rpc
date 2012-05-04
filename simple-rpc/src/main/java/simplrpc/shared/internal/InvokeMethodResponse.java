package simplrpc.shared.internal;

public class InvokeMethodResponse extends Response{

    private static final long serialVersionUID = 1L;

    public enum Type {
        OK, EXCEPTION
    }

    private final Object returnValue;
    private final String exceptionToString;
    private final Type type;

    public InvokeMethodResponse( Object returnValue, String exceptionToString, Type type ){
        this.returnValue = returnValue;
        this.exceptionToString = exceptionToString;
        this.type = type;
    }

    public String getExceptionToString(){
        return exceptionToString;
    }

    public Type getType(){
        return type;
    }

    public Object getReturnValue(){
        return returnValue;
    }

}
