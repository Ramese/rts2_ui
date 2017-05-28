package webcontroller.websocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WSClient implements Runnable{
    private final Socket wsSocket;
    private final WSServer wsServer;
    private PrintWriter writer = null;
    private BufferedReader reader = null;
    
    private String key;
    private String keySend;
    
    private boolean isComunicationUpgraded;
    
    public WSClient(Socket s, WSServer ss) {
        this.wsSocket = s;
        this.wsServer = ss;
        
        isComunicationUpgraded = false;
        
        key = "";
        keySend = "";
        
    } // WSClient()

    @Override
    public void run() {
        System.err.println("WSClient " + wsSocket.getRemoteSocketAddress() + " connected.");
        char tmp[] = new char[128];
        String tmpString = "";
        String request = "";
        
        try {
            writer = new PrintWriter(wsSocket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println("Cant get writer: " + wsSocket.toString());
            System.out.println(ex.getMessage());
            closeWsClient();
            return;
        }
        
        try {
            reader = new BufferedReader(new InputStreamReader(wsSocket.getInputStream()));
        } catch (IOException ex) {
            System.out.println("Cant get reader: " + wsSocket.toString());
            System.out.println(ex.getMessage());
            closeWsClient();
            return;
        }
        
        while(!isComunicationUpgraded) {
            try {
                request = reader.readLine();
                if(request == null) {
                    System.out.println("1 EOF");
                    closeWsClient();
                    return;
                }
                while(reader.ready()){
                    tmpString = reader.readLine();
                    if(tmpString == null){
                        System.out.println("EOF");
                        closeWsClient();
                        return;
                    }
                    request += tmpString + "\n";
                }
                
                System.out.println("Server dostal zpravu od " + wsSocket.getRemoteSocketAddress());
                System.out.println("---------request-----------");
                System.out.println(request);
                System.out.println("---------------------------");
                
                // ANALYZE REQUEST HERE:
                if(request.toUpperCase().startsWith("GET ")){
                    System.out.println("HTTP request method: GET");
                    //OTHER CONDITIONS
                    //int index = request.toLowerCase().indexOf("sec-websocket-key:");
                    String prop[] = request.split("\n");
                    for (String p : prop) {
                        if(p.toLowerCase().contains("sec-websocket-key")) {
                            p = p.trim();
                            key = p.substring(p.lastIndexOf(":")+1).trim();
                            break;
                        }
                    }
                    writeHTTPResponse(getWSUpdateResponse(key));
                }

                if(request.toUpperCase().startsWith("POST ")){
                    System.out.println("HTTP request method: POST");
                    writeHTTPResponse(getBadResponse());
                    closeWsClient();
                    return;
                }
                
            } catch (IOException ex) {
                System.out.println("Cant get next message");
                closeWsClient();
                return;
            }
            
            writer.println("konec ");
            break;
        } // while
        
        while(isComunicationUpgraded) {
            break;
        } // while
        
        closeWsClient();
        
        System.err.println("ObsluhaKlienta " + wsSocket.getRemoteSocketAddress() + " konci");
    } // run()
    
    private String getBadResponse() {
        String result = "HTTP/1.1 400 Bad Request\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: Closed\r\n\r\n";
        return result;
    }
    
    private String getWSUpdateResponse(String key){
        String serverKey = "";
        try {
            serverKey = WSProtocol.getServerKey(key);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(WSClient.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        
        String result = "HTTP/1.1 101 WebSocket Protocol Handshake\r\n" +
                        "Connection: Upgrade\r\n" +
                        "Upgrade: WebSocket\r\n" +
                        //"Access-Control-Allow-Origin: http://localhost\n" +
                        //"Access-Control-Allow-Credentials: true\n" +
                        "Sec-WebSocket-Accept: " + serverKey + "\r\n" +
                        "Access-Control-Allow-Headers: content-type\r\n\r\n";
        
        return result;
    }
    
    public synchronized boolean writeHTTPResponse(String response){
        writer.write(response);
        return true;
    }
    
    public synchronized boolean writeWSMessage(){
        return true;
    }
    
    /**
     * Before discarting reference!
     */
    public void closeWsClient(){
        try {
            if(reader != null){
                reader.close();
            }
        } catch (IOException ex) {

        }

        try {
            if(writer != null){
                writer.close();
            }
        } catch (Exception ex) {

        }

        try {
            if(wsSocket != null){
                wsSocket.close();
            }
        } catch (Exception ex) {

        }
        
        wsServer.removeWsClient(this);
    }
    
} // WSClient
