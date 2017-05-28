package webcontroller.controllers;

import BO.UserBO;
import VO.InputObjectVO;
import VO.PaginationVO;
import VO.UserVO;
import com.sun.net.httpserver.HttpExchange;
import java.util.ArrayList;
import java.util.List;
import static rts2ui.Config.GLOBAL_DEBUG;
import static webcontroller.controllers.Controller.GetObjectFromBody;

/**
 *
 * @author Radek
 */
public class UserController {
    
     public List<Controller> ctrls = new ArrayList<>();
    
    public UserController() {
        ctrls.add(GetUsersCtrl());
        ctrls.add(GetUserCtrl());
        ctrls.add(SaveUserCtrl());
        ctrls.add(GetTelescopesForUserCtrl());
        ctrls.add(SetTelescopesForUserCtrl());
    }
    
    private Controller GetUsersCtrl() {
        return new Controller("/getUsers", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception {
                if(GLOBAL_DEBUG) {
                    System.out.println("GetUsersController:");
                }

                PaginationVO pagination = (PaginationVO)GetObjectFromBody(t.getRequestBody(), PaginationVO.class);
                
                UserVO user = (UserVO)t.getAttribute("user");
                
                if(!user.Role.toLowerCase().equals("admin"))
                    Controller.Unauthorized("Nepovolený přístup.", t);

                Controller.SendGoodResponse(UserBO.GetUsers(), t);
            }
        };
    }
    
    private Controller GetTelescopesForUserCtrl() {
        return new Controller("/getTelescopesForUser", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception {
                if(GLOBAL_DEBUG) {
                    System.out.println("GetTelescopesForUserController:");
                }

                InputObjectVO id = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);
                
                UserVO user = (UserVO)t.getAttribute("user");
                
                if(!user.Role.toLowerCase().equals("admin"))
                    Controller.Unauthorized("Nepovolený přístup.", t);

                Controller.SendGoodResponse(UserBO.GetTelescopesForUser((int)id.Id), t);
            }
        };
    }
    
    private Controller SetTelescopesForUserCtrl() {
        return new Controller("/setTelescopesForUser", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception {
                if(GLOBAL_DEBUG) {
                    System.out.println("SetTelescopesForUserController:");
                }

                InputObjectVO id = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);
                
                UserVO user = (UserVO)t.getAttribute("user");
                
                if(!user.Role.toLowerCase().equals("admin"))
                    Controller.Unauthorized("Nepovolený přístup.", t);

                List<Integer> tels = new ArrayList<>();
                
                for(Integer i : id.Telescopes) {
                    tels.add(i);
                }
                
                Controller.SendGoodResponse(UserBO.SetTelescopesForUser((int)id.Id, tels), t);
            }
        };
    }
    
    private Controller GetUserCtrl() {
        return new Controller("/getUser", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception {
                if(GLOBAL_DEBUG) {
                    System.out.println("GetUserCtrl:");
                }

                InputObjectVO id = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);

                UserVO user = (UserVO)t.getAttribute("user");

                Controller.SendGoodResponse(UserBO.GetUser(id.Id), t);
            }
        };
    }
    
    private Controller SaveUserCtrl() {
        return new Controller("/saveUser", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception {
                if(GLOBAL_DEBUG) {
                    System.out.println("SaveUserCtrl:");
                }

                UserVO userFromJSON = (UserVO)GetObjectFromBody(t.getRequestBody(), UserVO.class);

                UserVO user = (UserVO)t.getAttribute("user");
                
                UserBO.UpdateUser(userFromJSON);

                Controller.SendGoodResponse(true, t);
            }
        };
    }
    
}
