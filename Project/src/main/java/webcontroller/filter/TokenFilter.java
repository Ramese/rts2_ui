package webcontroller.filter;

import BO.UserBO;
import VO.UserVO;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import webcontroller.controllers.Controller;
import webcontroller.HTTPProtocol;

/**
 *
 * @author Radek
 */
public class TokenFilter extends Filter {
    private static TokenFilter instance;
    
    public static String USER_OBJECT_STRING = "user";
    
    private TokenFilter() {
        
    }
    
    public static TokenFilter GetInstance() {
        if(instance == null) {
            instance = new TokenFilter();
        }
        return instance;
    }
    
    @Override
    public void doFilter(HttpExchange he, Chain chain) {
        try {
            System.out.println("Headers: ");
            System.out.println(HTTPProtocol.headerInfo(he.getRequestHeaders()));
            
            Headers headers = he.getRequestHeaders();
            
            if(!(headers.containsKey("Token") && headers.containsKey("UserName"))){
                Controller.TokenUnparsable("No token info in header.", he);
                return;
            }
            
            String token = headers.get("Token").get(0);
            String userName = headers.get("UserName").get(0);
            UserVO user = UserBO.IsLoggedIn(token, userName);
            
            if(user == null) {
                Controller.NotLoggedIn("Token expired.", he);
                return;
            } else {
                he.setAttribute(USER_OBJECT_STRING, user);
            }

            chain.doFilter(he);
        } catch (Exception ex) {
            try {
                // something wrong - response for client:
                Controller.Exception("Token filter exception", he, "Server error (token filter).");
            } catch (IOException ex1) {
                Logger.getLogger(TokenFilter.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    @Override
    public String description() {
        return "Token filter look for Token header and try to get User from DB. If no, response is not authorised request.";
    }
    
}
