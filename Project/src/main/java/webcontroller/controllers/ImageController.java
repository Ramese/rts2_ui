/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcontroller.controllers;

import BO.ImagesBO;
import VO.InputObjectVO;
import VO.UserVO;
import com.sun.net.httpserver.HttpExchange;
import java.util.ArrayList;
import java.util.List;
import static rts2ui.Config.GLOBAL_DEBUG;
import static webcontroller.controllers.Controller.GetObjectFromBody;

/**
 *
 * @author Admin
 */
public class ImageController {
    public List<Controller> ctrls = new ArrayList<>();

    public ImageController() {
        ctrls.add(GetCurrentCtrl());
        ctrls.add(GetImageCtrl());
        ctrls.add(GetLisOfImagesForDateCtrl());
        ctrls.add(GetFitsImageCtrl());
        ctrls.add(GetLisOfImagesForTargetCtrl());
    }
    
    private Controller GetCurrentCtrl() {
        return new Controller("/current", false) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception {
                if(GLOBAL_DEBUG) {
                    System.out.println("GetCurrentController:");
                }

                InputObjectVO input = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);
                
                UserVO user = (UserVO)t.getAttribute("user");

                Controller.SendGoodResponse(ImagesBO.GetCurrent(input.Id), t);
            }
        };
    }
    
    private Controller GetImageCtrl() {
        return new Controller("/getImage", false) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception {
                if(GLOBAL_DEBUG) {
                    System.out.println("GetImageController:");
                }

                InputObjectVO input = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);
                
                UserVO user = (UserVO)t.getAttribute("user");

                Controller.SendGoodResponse(ImagesBO.GetImage(input.Id, input.Value), t);
            }
        };
    }
    
    private Controller GetFitsImageCtrl() {
        return new Controller("/getFitsImage", false) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception {
                if(GLOBAL_DEBUG) {
                    System.out.println("GetFitsImageController:");
                }

                InputObjectVO input = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);
                
                UserVO user = (UserVO)t.getAttribute("user");

                Controller.SendGoodResponse(ImagesBO.GetFitsImage(input.Id, input.Value), t);
            }
        };
    }
    
    private Controller GetLisOfImagesForDateCtrl() {
        return new Controller("/getListOfImagesForDate", false) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception {
                if(GLOBAL_DEBUG) {
                    System.out.println("GetListOfImageForDateController:");
                }

                InputObjectVO input = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);
                
                UserVO user = (UserVO)t.getAttribute("user");

                Controller.SendGoodResponse(ImagesBO.GetImagesListForDate(input.Id, input.Value), t);
            }
        };
    }
    
    private Controller GetLisOfImagesForTargetCtrl() {
        return new Controller("/getListOfImagesForTarget", false) {
            @Override
            public void inerHandle(HttpExchange t) throws Exception {
                if(GLOBAL_DEBUG) {
                    System.out.println("GetListOfImageForTargetController:");
                }

                InputObjectVO input = (InputObjectVO)GetObjectFromBody(t.getRequestBody(), InputObjectVO.class);
                
                UserVO user = (UserVO)t.getAttribute("user");

                Controller.SendGoodResponse(ImagesBO.GetImagesListForTarget(input.Id, input.IdTarget), t);
            }
        };
    }
}
