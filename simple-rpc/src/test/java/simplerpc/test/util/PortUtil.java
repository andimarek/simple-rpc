package simplerpc.test.util;

import java.io.IOException;
import java.net.ServerSocket;

public class PortUtil{

    public static int getFreePort(){
        ServerSocket ss = null;
        try{
            ss = new ServerSocket( 0 );
            return ss.getLocalPort();
        }
        catch( IOException e ){
            throw new RuntimeException( e );
        }
        finally{
            close( ss );
        }
    }

    private static void close( ServerSocket ss ){
        if( ss == null ) return;
        try{
            ss.close();
        }
        catch( IOException e ){
        }
    }

}
