package simplrpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import simplrpc.client.internal.ClientInvocationHandler;
import simplrpc.client.internal.ConnectionHandler;
import simplrpc.shared.SimpleRmiException;
import simplrpc.shared.internal.HasImplementationRequest;
import simplrpc.shared.internal.HasImplementationResponse;



/**
 * Represents access to a RmiServer, through implementations of interfaces are provided.
 */
public class RmiClient{

    private final String host;
    private final int port;
    private ConnectionHandler connectionHandler;

    /**
     * A new RmiClient, which is connected to a RmiServer on the specified host and port.
     * 
     * @param host
     * @param port
     */
    public RmiClient( String host, int port ){
        this.host = host;
        this.port = port;
        connectionHandler = createConnectionHandler();
    }

    protected ConnectionHandler createConnectionHandler(){
        ConnectionHandler connectionHandler = new ConnectionHandler( host, port );
        try{
            connectionHandler.connect();
        }
        catch( Exception e ){
            throw new SimpleRmiException( e );
        }
        return connectionHandler;
    }

    /**
     * If the RmiServer contains a implementation.
     * 
     * @param interfaceClazz
     * @return
     */
    public boolean hasImplementation( Class< ? > interfaceClazz ){
        if( interfaceClazz == null ) throw new NullPointerException( "interfaceClazz is null" );
        try{

            HasImplementationRequest hasImplementationRequest = new HasImplementationRequest( interfaceClazz.getName() );
            HasImplementationResponse response = ( HasImplementationResponse )connectionHandler.sendRequest( hasImplementationRequest );
            return response.isHasImplementation();
        }
        catch( Exception e ){
            throw new SimpleRmiException( e );
        }
    }

    /**
     * Returns a proxy object for the specified interface. Every method invocation is send to the sever, executed and the result is send back to the proxy.
     * 
     * @param interfaceClazz
     * @return
     */
    public <T> T getImplementation( Class<T> interfaceClazz ){
        if( !hasImplementation( interfaceClazz ) ) throw new SimpleRmiException( "Server has no implementation for this" );
        return createProxy( interfaceClazz );
    }

    @SuppressWarnings("unchecked")
    private <T> T createProxy( Class<T> interfaceClazz ){
        InvocationHandler invocationHandler = new ClientInvocationHandler( connectionHandler, interfaceClazz );
        T proxy = ( T )Proxy.newProxyInstance( RmiClient.class.getClassLoader(), new Class[]{ interfaceClazz }, invocationHandler );
        return proxy;
    }

    /**
     * Closes the connection to the sever. After that, the RmiClient and all created proxy objects are not usable anymore.
     */
    public void disconnect(){
        connectionHandler.close();
    }
}
