package simplrpc.server.internal;

import java.net.Socket;

public interface NewSocketCallback{

    void newSocketAccepted( Socket newSocket );

}
