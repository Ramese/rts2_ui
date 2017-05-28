package webcontroller.controllers;

import BO.UserBO;
import VO.UserVO;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static rts2ui.Config.GLOBAL_DEBUG;

/**
 *
 * @author Radek
 */
public class LoginController {
    public List<Controller> ctrls = new ArrayList<>();
    
    public LoginController() {
        ctrls.add(GetLoginCtrl());
    }
    
    private Controller GetLoginCtrl() {
        return new Controller("/login", false) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception {
                
                if(GLOBAL_DEBUG) {
                    System.out.println("LoginController:");
                }

                UserVO userCredentials = (UserVO)Controller.GetObjectFromBody(t.getRequestBody(), UserVO.class);

                // save object
                UserVO user = UserBO.Login(userCredentials.UserName, userCredentials.Password);

                if(user == null || !user.IsActive) {
                    Controller.ExpectationFailed("Incorrect credentials.", t, "Incorrect user name or password.");
                    return;
                }

                Controller.SendGoodResponse(user, t);
            }
        };
    }
}
