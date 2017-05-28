package webcontroller.websocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static rts2ui.Config.WebConfig.GLOBAL_WEB_SOCKET_PORT_NUMBER;

public class WSServer implements Runnable {
    private ServerSocket ss;
    protected List<WSClient> wsClients;
    public Base64 base64;

    public WSServer(){
        wsClients = new ArrayList<>();
        
        try {
            this.ss = new ServerSocket(GLOBAL_WEB_SOCKET_PORT_NUMBER);
        } catch (IOException ex) {
            
        }
    } // WebSocketServer()

    @Override
    public void run() {
        
        try {
            System.err.println("Server - alive: " + InetAddress.getLocalHost());
        } catch (UnknownHostException ex) {
            System.err.println("Cant get InetAddress.getLocalHost()");
        }
        
        Socket s;
        Thread t;
        while(true){
            try {
                s = ss.accept();
                WSClient wsc = new WSClient(s, this);
                t = new Thread(wsc);
                t.setDaemon(true);
                t.start();
                wsClients.add(wsc);
                
            } catch (IOException ex) {
                System.err.println("Error handling client:");
                System.err.println(ex.getMessage());
            }
        }
    } // run()
    
    public synchronized void removeWsClient(WSClient ok){
        wsClients.remove(ok);
    } // removeOK()
    
} // WebSocketServer