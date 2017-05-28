package webcontroller.controllers;

import BO.TelescopeBO;
import VO.InputObjectVO;
import VO.PaginationVO;
import VO.TelescopeVO;
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
public class TelescopeController {
    public List<Controller> ctrls = new ArrayList<>();
    
    public TelescopeController() {
        ctrls.add(GetTelescopesCtrl());
        ctrls.add(GetTelescopeCtrl());
        ctrls.add(GetSaveTelescopeCtrl());
        ctrls.add(GetTelescopeDevicesCtrl());
        ctrls.add(GetDevicePropertiesCtrl());
        ctrls.add(GetCurrentImageCtrl());
        ctrls.add(GetExposeDataCtrl());
        ctrls.add(SetValueCtrl());
        ctrls.add(GetTargetsCtrl());
        ctrls.add(GetTargetCtrl());
        ctrls.add(CreateTargetCtrl());
        ctrls.add(UpdateTargetCtrl());
        ctrls.add(SetAutonomousObservationsForTargetCtrl());
        ctrls.add(GetPlanCtrl());
        ctrls.add(GetTelescopesForPublicCtrl());
        ctrls.add(SearchTargetsCtrl());
        ctrls.add(GetTargetByIdCtrl());
        ctrls.add(GetAddToQueueCtrl());
    } 
    
    private Controller GetTelescopesCtrl() {
        return new Controller("/telescopes", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception {
                if(GLOBAL_DEBUG) {
                    System.out.println("TelescopesController:");
                }

                PaginationVO pagination = (PaginationVO)GetObjectFromBody(t.getRequestBody(), PaginationVO.class);

                UserVO user = (UserVO)t.getAttribute("user");
                
                if(!(user.Role.toLowerCase().equals("admin") || user.Role.toLowerCase().equals("scientist"))){
                    Controller.Unauthorized("Nemáte dostatené oprávnění.", t);
                }
                
                Controller.SendGoodResponse(TelescopeBO.GetTelescopes(user), t);
            }
        };
    }
    
    private Controller GetTelescopesForPublicCtrl() {
        return new Controller("/publicTelescopes", false) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception {
                if(GLOBAL_DEBUG) {
                    System.out.println("TelescopesController:");
                }

                //PaginationVO pagination = (PaginationVO)GetObjectFromBody(t.getRequestBody(), PaginationVO.class);

                Controller.SendGoodResponse(TelescopeBO.GetTelescopesForPublic(), t);
            }
        };
    }
    
    private Controller GetTelescopeCtrl() {
        return new Controller("/telescope", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception {
                if(GLOBAL_DEBUG) {
                    System.out.println("TelescopeController:");
                }

                InputObjectVO id = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);

                UserVO user = (UserVO)t.getAttribute("user");
                
                if(!user.Role.toLowerCase().equals("admin")){
                    Controller.Unauthorized("Neopravněný přístup.", t);
                }

                Controller.SendGoodResponse(TelescopeBO.GetTelescope(id.Id), t);
            }
        };
    }
    
    private Controller GetSaveTelescopeCtrl() {
        return new Controller("/savetelescope", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception {
                if(GLOBAL_DEBUG) {
                    System.out.println("TelescopeController:");
                }

                TelescopeVO telescope = (TelescopeVO)GetObjectFromBody(t.getRequestBody(), TelescopeVO.class);

                UserVO user = (UserVO)t.getAttribute("user");

                if(telescope.Id == 0) { // insert 
                    TelescopeBO.InsertTelescope(telescope);
                } else { // update
                    TelescopeBO.UpdateTelescope(telescope);
                }

                Controller.SendGoodResponse(TelescopeBO.GetTelescope(telescope.Name), t);
            }
        };
    }
    
    private Controller GetExposeDataCtrl() {
        return new Controller("/exposedata", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception{
                if(GLOBAL_DEBUG) {
                    System.out.println("GetExposeDataController:");
                }

                InputObjectVO telescope = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);

                UserVO user = (UserVO)t.getAttribute("user");

                Controller.SendGoodResponse(TelescopeBO.GetExposeData(telescope.Id, telescope.Device), t);

            }
        };
    }
    
    private Controller GetCurrentImageCtrl() {
        return new Controller("/currentimage", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception{
                if(GLOBAL_DEBUG) {
                    System.out.println("CurrentImageController:");
                }

                InputObjectVO telescope = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);

                UserVO user = (UserVO)t.getAttribute("user");

                Controller.SendGoodResponse(TelescopeBO.GetCurrentImage(telescope.Id, telescope.Device), t);

            }
        };
    }
    
    private Controller GetTelescopeDevicesCtrl() {
        return new Controller("/devices", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception{
                if(GLOBAL_DEBUG) {
                    System.out.println("GetTelescopeDevices:");
                }

                InputObjectVO telescopeID = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);

                UserVO user = (UserVO)t.getAttribute("user");
                
                

                Controller.SendGoodResponse(TelescopeBO.GetDevices(telescopeID.Id), t);

            }
        };
    }
    
    private Controller GetDevicePropertiesCtrl() {
        return new Controller("/deviceproperties", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception{
                if(GLOBAL_DEBUG) {
                    System.out.println("GetDevicePropertiesCtrl:");
                }

                InputObjectVO telescopeID = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);

                UserVO user = (UserVO)t.getAttribute("user");

                Controller.SendGoodResponse(TelescopeBO.GetDeviceProperties(telescopeID.Id, telescopeID.Device), t);

            }
        };
    }
    
    private Controller SetValueCtrl() {
        return new Controller("/set", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception{
                if(GLOBAL_DEBUG) {
                    System.out.println("SetValueCtrl:");
                }

                InputObjectVO telescopeID = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);

                UserVO user = (UserVO)t.getAttribute("user");

                Controller.SendGoodResponse(TelescopeBO.SetValue(telescopeID.Id, telescopeID.Device, telescopeID.ValueName, telescopeID.Value), t);

            }
        };
    }
    
    private Controller GetAddToQueueCtrl() {
        return new Controller("/addToQueue", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception{
                if(GLOBAL_DEBUG) {
                    System.out.println("AddToQueueCtrl:");
                }

                InputObjectVO telescopeID = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);

                UserVO user = (UserVO)t.getAttribute("user");

                String command = TelescopeBO.GetCommandForAddToQueue(telescopeID.Queue, telescopeID.IdTarget, telescopeID.From, telescopeID.To, telescopeID.Rep, telescopeID.Sep);
                
                Controller.SendGoodResponse(TelescopeBO.SendCommand(telescopeID.Id, telescopeID.Device, command), t);

            }
        };
    }
    
    private Controller GetTargetsCtrl() {
        return new Controller("/targets", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception{
                if(GLOBAL_DEBUG) {
                    System.out.println("GetTargetsCtrl:");
                }

                InputObjectVO telescopeID = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);

                UserVO user = (UserVO)t.getAttribute("user");

                Controller.SendGoodResponse(TelescopeBO.GetTargets(telescopeID.Id), t);

            }
        };
    }
    
    
    private Controller SearchTargetsCtrl() {
        return new Controller("/searchTargets", false) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception{
                if(GLOBAL_DEBUG) {
                    System.out.println("GetTargetsCtrl:");
                }

                InputObjectVO telescopeID = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);

                Controller.SendGoodResponse(TelescopeBO.SearchTargets(telescopeID.Id, telescopeID.Value), t);

            }
        };
    }
    
    private Controller GetTargetByIdCtrl() {
        return new Controller("/getTargetById", false) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception{
                if(GLOBAL_DEBUG) {
                    System.out.println("GetTargetByIdCtrl:");
                }

                InputObjectVO telescopeID = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);

                Controller.SendGoodResponse(TelescopeBO.GetTargetById(telescopeID.Id, telescopeID.IdTarget), t);

            }
        };
    }
    
    private Controller GetTargetCtrl() {
        return new Controller("/target", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception{
                if(GLOBAL_DEBUG) {
                    System.out.println("GetTargetCtrl:");
                }

                InputObjectVO telescopeID = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);

                UserVO user = (UserVO)t.getAttribute("user");

                Controller.SendGoodResponse(TelescopeBO.GetTargetById(telescopeID.Id, telescopeID.Target.Id), t);

            }
        };
    }
    
    private Controller CreateTargetCtrl() {
        return new Controller("/createTarget", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception{
                if(GLOBAL_DEBUG) {
                    System.out.println("CreateTargetCtrl:");
                }

                InputObjectVO telescopeID = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);

                UserVO user = (UserVO)t.getAttribute("user");

                Controller.SendGoodResponse(TelescopeBO.CreateTarget(telescopeID.Id, telescopeID.Target), t);

            }
        };
    }
    
    private Controller UpdateTargetCtrl() {
        return new Controller("/updateTarget", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception{
                if(GLOBAL_DEBUG) {
                    System.out.println("UpdateTargetCtrl:");
                }

                InputObjectVO telescopeID = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);

                UserVO user = (UserVO)t.getAttribute("user");

                Controller.SendGoodResponse(TelescopeBO.UpdateTarget(telescopeID.Id, telescopeID.Target), t);

            }
        };
    }
    
    private Controller SetAutonomousObservationsForTargetCtrl() {
        return new Controller("/autonomousTarget", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception{
                if(GLOBAL_DEBUG) {
                    System.out.println("SetAutonomousObservationsForTargetCtrl:");
                }

                InputObjectVO telescopeID = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);

                UserVO user = (UserVO)t.getAttribute("user");

                Controller.SendGoodResponse(TelescopeBO.SetAutonomousObservationsForTarget(telescopeID.Id, telescopeID.Target), t);

            }
        };
    }
    
    private Controller GetPlanCtrl() {
        return new Controller("/getPlan", true) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception{
                if(GLOBAL_DEBUG) {
                    System.out.println("GetPlanCtrl:");
                }

                InputObjectVO telescopeID = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);

                UserVO user = (UserVO)t.getAttribute("user");

                Controller.SendGoodResponse(TelescopeBO.GetPlan(telescopeID.Id, telescopeID.From, telescopeID.To), t);

            }
        };
    }
}
