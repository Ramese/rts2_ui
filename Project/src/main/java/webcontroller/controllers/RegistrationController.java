package webcontroller.controllers;

import BO.UserBO;
import VO.UserVO;
import com.sun.net.httpserver.HttpExchange;
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
public class RegistrationController {
    
    public List<Controller> ctrls = new ArrayList<>();
    
    public RegistrationController() {
        ctrls.add(GetRegistrationCtrl());   
        ctrls.add(GetCheckUserNameCtrl());
        ctrls.add(GetCheckUserEmailCtrl());
    }
    
    private Controller GetRegistrationCtrl() {
        return new Controller("/register", false) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception{
                if(GLOBAL_DEBUG) {
                    System.out.println("RegistrationController:");
                }

                UserVO regUser = (UserVO)Controller.GetObjectFromBody(t.getRequestBody(), UserVO.class);

                // check if the object is ok
                if(!UserBO.IsUserValidForInsert(regUser)) {
                    Controller.ExpectationFailed("Wrong user data. inputJSON: ", t, "Registration of new user is not successful. User data are incorrect.");
                    return;
                }

                // save object
                UserBO.InsertUser(regUser);

                // return new ID, token and 200
                regUser = UserBO.GetUserByUserName(regUser.UserName);

                Controller.SendGoodResponse(regUser, t);
                
            }
        };
    }
    
    private Controller GetCheckUserNameCtrl() {
        return new Controller("/register/isUsernameFree", false) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception {
                if(GLOBAL_DEBUG) {
                    System.out.println("CheckUserNameController:");
                }

                UserVO regUser = (UserVO)Controller.GetObjectFromBody(t.getRequestBody(), UserVO.class);

                // return new ID, token and 200
                try {
                    regUser = UserBO.GetUserByUserName(regUser.UserName);
                } catch (SQLException ex) {
                    Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
                }

                Controller.SendGoodResponse(regUser == null, t);
            }
        };
    }
    
    private Controller GetCheckUserEmailCtrl() {
        return new Controller("/register/isUserEmailFree", false) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception {
                if(GLOBAL_DEBUG) {
                    System.out.println("CheckUserEmailController:");
                }

                UserVO regUser = (UserVO)Controller.GetObjectFromBody(t.getRequestBody(), UserVO.class);

                // return new ID, token and 200
                regUser = UserBO.GetUserByEmail(regUser.Email);

                Controller.SendGoodResponse(regUser == null, t);
            }
        };
    }
}
