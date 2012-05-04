package simplrpc.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import simplrpc.server.internal.ImplementationContainer;
import simplrpc.server.internal.NewSocketCallback;
import simplrpc.server.internal.ServerSocketManager;
import simplrpc.server.internal.SingleConnectionHandler;



/**
 * A server providing remote access to implementations of Interfaces.
 */
public class RmiServer{

    private final int port;

    private final ImplementationContainer implContainer = new ImplementationContainer();

    private final List<SingleConnectionHandler> connectionHandlers = new ArrayList<SingleConnectionHandler>();

    private final ServerSocketManager serverSocketManager;
    private final NewSocketCallback newSocketCallback;

    /**
     * Creates a new Server listening on the specified port.
     * 
     * @param port
     */
    public RmiServer( int port ){
        super();
        this.port = port;
        this.newSocketCallback = new NewSocketCallback(){

            @Override
            public void newSocketAccepted( Socket newSocket ){
                RmiServer.this.newSocketAccepted( newSocket );
            }
        };
        this.serverSocketManager = createServerSocketManager( port );
        serverSocketManager.start();
    }

    protected ServerSocketManager createServerSocketManager( int port ){
        return new ServerSocketManager( port, newSocketCallback );
    }

    private void newSocketAccepted( Socket newSocket ){
        SingleConnectionHandler newConnectionHandler = new SingleConnectionHandler( newSocket, implContainer );
        newConnectionHandler.start();
        connectionHandlers.add( newConnectionHandler );
    }

    public void stop(){
        serverSocketManager.stop();
    }

    /**
     * Adds a new implementation for the specified interface.
     * 
     * @param interfaceClazz
     *        The interface which is implemented.
     * @param implementation
     *        The implementation of the interface
     * @throws NPE
     *         if one of the arguments is null
     * @throws IllegalArgumentException
     *         If interfaceClazz doesn't represent a interface
     * @throws IllegalArgumentException
     *         If there is already a implementation
     */
    public <T> void addImplementation( Class<T> interfaceClazz, T implementation ){
        if( interfaceClazz == null ) throw new NullPointerException( "interfaceClazz is null" );
        if( implementation == null ) throw new NullPointerException( "implementation is null" );
        if( !interfaceClazz.isInterface() ) throw new IllegalArgumentException( "no interface" );
        if( implContainer.containsImplementation( interfaceClazz ) ) throw new IllegalArgumentException( "duplicate implementation" );

        implContainer.addImplementation( interfaceClazz, implementation );
    }

    /**
     * If the server provides a implementation for the specified interface.
     * 
     * @param interfaceClazz
     * @return
     */
    public boolean hasImplementation( Class< ? > interfaceClazz ){
        return implContainer.containsImplementation( interfaceClazz );
    }

    /**
     * The port the server is listening on.
     * 
     * @return
     */
    public int getPort(){
        return port;
    }

}
