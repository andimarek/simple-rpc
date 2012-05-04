package simplrpc.shared.internal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class StreamUtil{

    public static Object read( InputStream is, ClassLoader classLoader ) throws IOException, ClassNotFoundException{
        BufferedInputStream bufIs = new BufferedInputStream( is );
        ZipInputStream zis = new ZipInputStream( bufIs );
        zis.getNextEntry();
        ObjectInputStream ois = classLoader != null ? new OISExplicitClassLoader( zis, classLoader ) : new ObjectInputStream( zis );
        Object result = ois.readObject();
        return result;
    }

    public static Object read( InputStream is ) throws IOException, ClassNotFoundException{
        return read( is, null );
    }

    public static void write( OutputStream os, Object toWrite ) throws IOException{
        BufferedOutputStream bufOs = new BufferedOutputStream( os );
        ZipOutputStream zos = new ZipOutputStream( bufOs );
        zos.putNextEntry( new ZipEntry( "IGNORED" ) );

        ObjectOutputStream oos = new ObjectOutputStream( zos );

        oos.writeObject( toWrite );
        zos.closeEntry();
        oos.flush();
    }

    public static byte[] toByteArray( Object toWrite ) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bufOs = new BufferedOutputStream( baos );
        ZipOutputStream zos = new ZipOutputStream( bufOs );
        zos.putNextEntry( new ZipEntry( "IGNORED" ) );

        ObjectOutputStream oos = new ObjectOutputStream( zos );

        oos.writeObject( toWrite );
        zos.closeEntry();
        oos.close();
        return baos.toByteArray();
    }

    private static class OISExplicitClassLoader extends ObjectInputStream{

        private final ClassLoader classLoader;

        public OISExplicitClassLoader( InputStream in, ClassLoader classLoader ) throws IOException{
            super( in );
            this.classLoader = classLoader;
        }

        @Override
        protected Class< ? > resolveClass( ObjectStreamClass desc ) throws IOException, ClassNotFoundException{
            try{
                return classLoader.loadClass( desc.getName() );
            }
            catch( ClassNotFoundException e ){
                return super.resolveClass( desc );
            }
        }

    }
}
