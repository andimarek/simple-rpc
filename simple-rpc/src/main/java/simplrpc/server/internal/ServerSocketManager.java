package simplrpc.server.internal;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import simplrpc.shared.SimpleRmiException;



public class ServerSocketManager{

    private final int port;
    private final NewSocketCallback newSocketCallback;
    private Thread acceptingThread;
    private ServerSocket serverSocket;

    public ServerSocketManager( int port, NewSocketCallback newSocketCallback ){
        this.port = port;
        this.newSocketCallback = newSocketCallback;
    }

    public void start(){
        try{
            serverSocket = createServerSocket();
        }
        catch( IOException e ){
            throw new SimpleRmiException( e );
        }
        Runnable runnable = new Runnable(){

            @Override
            public void run(){
                listenForNewConnections( serverSocket );

            }
        };
        execute( runnable );

    }

    protected ServerSocket createServerSocket() throws IOException{
        return new ServerSocket( port );
    }

    private void listenForNewConnections( ServerSocket serverSocket ){
        while( !Thread.currentThread().isInterrupted() ){
            try{
                Socket socket = serverSocket.accept();
                handleNewConnection( socket );
            }
            catch( IOException e ){
                if( checkForSocketClosed( e ) ){ return; }
                e.printStackTrace();
            }
        }
    }

    private boolean checkForSocketClosed( IOException e ){
        if( !( e instanceof SocketException ) ) return false;
        if( !e.getMessage().contains( "Socket closed" ) ) return false;
        return true;

    }

    protected void execute( Runnable runnable ){
        acceptingThread = new Thread( runnable );
        acceptingThread.start();
    }

    private void handleNewConnection( Socket socket ){
        newSocketCallback.newSocketAccepted( socket );
    }

    public void stop(){
        if( acceptingThread != null ) acceptingThread.interrupt();
        closeServerSocket();
    }

    private void closeServerSocket(){
        if( serverSocket == null ) return;
        try{
            serverSocket.close();
        }
        catch( IOException e ){
            e.printStackTrace();
        }

    }

}
