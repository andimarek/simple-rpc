package simplrpc.server.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import simplrpc.shared.internal.HasImplementationRequest;
import simplrpc.shared.internal.InvokeMethodRequest;
import simplrpc.shared.internal.Request;
import simplrpc.shared.internal.Response;
import simplrpc.shared.internal.StreamUtil;
import simplrpc.shared.internal.WrappedInvokeMethodRequest;



public class SingleConnectionHandler{

    private final Socket socket;
    private Thread handlingThread;
    private final ImplementationContainer implementationContainer;

    public SingleConnectionHandler( Socket socket, ImplementationContainer implementationContainer ){
        this.socket = socket;
        this.implementationContainer = implementationContainer;
    }

    public void start(){
        handlingThread = new Thread( new Runnable(){

            @Override
            public void run(){
                handleConnection();
            }
        } );
        handlingThread.start();
    }

    private void handleConnection(){
        try{
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            while( !Thread.currentThread().isInterrupted() ){
                readRequestAndSendResponse( is, os );
            }
        }
        catch( Exception e ){
            exception( e );
        }
    }

    private void readRequestAndSendResponse( InputStream is, OutputStream os ){
        try{
            Request request = readRequest( is );
            Response response = processRequest( request );
            StreamUtil.write( os, response );
        }
        catch( Exception e ){
            exception( e );
        }
    }

    private Request readRequest( InputStream is ) throws ClassNotFoundException, IOException{
        Request request = ( Request )StreamUtil.read( is );
        if( !( request instanceof WrappedInvokeMethodRequest ) ) return request;
        /*
         * Special handling for WrappedInvocationRequest in order to use the right class loader.
         */
        WrappedInvokeMethodRequest wrappedRequest = ( WrappedInvokeMethodRequest )request;
        ByteArrayInputStream bais = new ByteArrayInputStream( wrappedRequest.getRawMethodInvocationRequest() );
        Object implementation = implementationContainer.getImplementation( wrappedRequest.getInterfaceName() );
        return ( Request )StreamUtil.read( bais, implementation.getClass().getClassLoader() );
    }

    private void exception( Exception e ){

    }

    private Response processRequest( Request request ){
        if( request instanceof HasImplementationRequest ){ return process( ( HasImplementationRequest )request ); }
        if( request instanceof InvokeMethodRequest ) return process( ( InvokeMethodRequest )request );
        return null;
    }

    private Response process( InvokeMethodRequest request ){
        InvokeMethodProcessor processor = new InvokeMethodProcessor( request, implementationContainer );
        return processor.process();
    }

    private Response process( HasImplementationRequest request ){
        HasImplementationProcessor processor = new HasImplementationProcessor( implementationContainer, request );
        return processor.process();
    }

    public void stop(){
        handlingThread.interrupt();
    }
}
