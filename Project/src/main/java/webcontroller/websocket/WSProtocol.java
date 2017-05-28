package webcontroller.websocket;

//import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class WSProtocol {
    private static final String MAGIC_STRING = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    
    /**
     * According to protocol specification (page 7):\n
     * http://datatracker.ietf.org/doc/rfc6455/ \n
     * \n
     * Metohod concatenate clientKey with magic string "258EAFA5-E914-47DA-95CA-C5AB0DC85B11" and make SHA1 then Base64 and returns result.
     * @param clientKey Client key from header properties Sec-WebSocket-Key.
     * @return Base64(SHA1(clientKey + magicString))
     * @throws NoSuchAlgorithmException 
     */
    public static String getServerKey(String clientKey) throws NoSuchAlgorithmException{
        String result = clientKey + MAGIC_STRING;
        
        result = Base64.getEncoder().encodeToString(SHA1(result));
        
        return result;
    }
    
    private static byte[] SHA1(String in) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        
        byte[] result = md.digest(in.getBytes());
        return result;
    }
    
    
}
