package simplrpc.server.internal;

import java.util.LinkedHashMap;
import java.util.Map;

public class ImplementationContainer{

    private final Map<Class< ? >, Object> implByClass = new LinkedHashMap<Class< ? >, Object>();

    public <T> void addImplementation( Class<T> clazz, T impl ){
        implByClass.put( clazz, impl );
    }

    public boolean containsImplementation( Class< ? > clazz ){
        return implByClass.containsKey( clazz );
    }

    public boolean containsImplementation( String fqClassName ){
        for( Class< ? > clazz : implByClass.keySet() ){
            if( clazz.getName().equals( fqClassName ) ) return true;
        }
        return false;
    }

    public Object getImplementation( Class< ? > clazz ){
        return implByClass.get( clazz );
    }

    public Object getImplementation( String fqClassName ){
        for( Class< ? > clazz : implByClass.keySet() ){
            if( clazz.getName().equals( fqClassName ) ) return implByClass.get( clazz );
        }
        return null;
    }

}
