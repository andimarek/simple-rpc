package simplrpc.client.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import simplrpc.shared.internal.InvokeMethodRequest;
import simplrpc.shared.internal.Request;
import simplrpc.shared.internal.Response;
import simplrpc.shared.internal.StreamUtil;
import simplrpc.shared.internal.WrappedInvokeMethodRequest;



public class ConnectionHandler{

    private final String host;
    private final int port;
    private Socket socket;
    private OutputStream os;
    private InputStream is;

    public ConnectionHandler( String host, int port ){
        this.host = host;
        this.port = port;
    }

    public void connect() throws UnknownHostException, IOException{
        socket = new Socket( host, port );
        os = socket.getOutputStream();
        is = socket.getInputStream();
    }

    public Response sendRequest( Request request ) throws Exception{
        writeRequestToStream( request );
        return ( Response )StreamUtil.read( is, ConnectionHandler.class.getClassLoader() );
    }

    private void writeRequestToStream( Request request ) throws IOException{
        if( !( request instanceof InvokeMethodRequest ) ){
            StreamUtil.write( os, request );
            return;
        }
        InvokeMethodRequest invokeRequest = ( InvokeMethodRequest )request;
        byte[] rawRequestData = StreamUtil.toByteArray( invokeRequest );
        WrappedInvokeMethodRequest wrappedRequest = new WrappedInvokeMethodRequest( invokeRequest.getInterfaceName(), rawRequestData );
        StreamUtil.write( os, wrappedRequest );
    }

    public void close(){
        try{
            is.close();
            os.close();
            socket.close();
        }
        catch( IOException e ){
        }
    }

}
