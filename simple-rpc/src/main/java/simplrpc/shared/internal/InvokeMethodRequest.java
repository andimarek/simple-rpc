package simplrpc.shared.internal;

import java.util.ArrayList;
import java.util.List;

public class InvokeMethodRequest extends Request{

    private static final long serialVersionUID = 1L;

    private final String interfaceName;
    private final String methodName;
    private final List<Class< ? >> parameterTypes = new ArrayList<Class< ? >>();

    private final List<Object> arguments = new ArrayList<Object>();

    public InvokeMethodRequest( String interfaceName, String methodName, List<Object> arguments, List<Class< ? >> parameterTypes ){
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.arguments.addAll( arguments );
        this.parameterTypes.addAll( parameterTypes );
    }

    public String getInterfaceName(){
        return interfaceName;
    }

    public List<Object> getArguments(){
        return new ArrayList<Object>( arguments );
    }

    public Object[] getArgumentsAsArray(){
        return arguments.toArray( new Object[ arguments.size() ] );
    }

    public String getMethodName(){
        return methodName;
    }

    public List<Class< ? >> getParameterTypes(){
        return new ArrayList<Class< ? >>( parameterTypes );
    }

    public Class< ? >[] getParameterTypesAsArray(){
        return parameterTypes.toArray( new Class< ? >[ parameterTypes.size() ] );
    }

}
