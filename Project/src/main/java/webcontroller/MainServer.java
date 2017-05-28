package webcontroller;

import com.sun.net.httpserver.HttpContext;
import webcontroller.controllers.Controller;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import java.util.logging.Level;
import java.util.logging.Logger;
import webcontroller.controllers.LoginController;
import webcontroller.controllers.RegistrationController;
import webcontroller.controllers.TelescopeController;
import webcontroller.websocket.WSServer;
import static rts2ui.Config.WebConfig.GLOBAL_WEB_PATH;
import static rts2ui.Config.WebConfig.GLOBAL_WEB_API_PATH;
import static rts2ui.Config.WebConfig.GLOBAL_WEB_INDEX_NAME;
import static rts2ui.Config.WebConfig.GLOBAL_WEB_PORT_NUMBER;
import static rts2ui.Config.WebConfig.GLOBAL_WEB_NAME;
import static rts2ui.Config.WebConfig.GLOBAL_WEB_FOLDER_PATH;
import webcontroller.controllers.ImageController;
import webcontroller.controllers.UserController;
import webcontroller.filter.TokenFilter;

/**
 * MainServer is the main service running and listening socket communication. 
 * Object includes array of controllers, port, HttpServer, WSServer etc.
 * @author Radek
 */
public class MainServer {
    private HttpServer server;
    private WSServer wsServer;
    private final String APIPath;
    private final String WebPath;
    private final String WebName;
    private final int portNumber;
    private final String webFolderName;
    private final String indexName;
    
    private final List<Controller> controllers;
    private WebFaceController mainController;
    
            
    /**
     * Create webserver and controllers.
     */
    public MainServer(){
        
        this.WebPath = GLOBAL_WEB_PATH;
        this.WebName = GLOBAL_WEB_NAME;
        this.APIPath = (this.WebPath + GLOBAL_WEB_API_PATH).replace("//", "/");
        this.portNumber = GLOBAL_WEB_PORT_NUMBER;
        this.webFolderName = GLOBAL_WEB_FOLDER_PATH;
        this.indexName = GLOBAL_WEB_INDEX_NAME;
        
        this.controllers = new ArrayList<>();
        this.initWebServer();
        
        this.wsServer = new WSServer();
        //wsServer.run();
    }
    
    /**
     * Info
     */
    private void optionsInfo(){
        System.out.printf("Server X běží:\nWeb: localhost:%d%s\nAPI: localhost:%d%s\n", portNumber, WebPath, portNumber, APIPath);
    }
    
    /**
     * 
     */
    private void controllersInfo(){
        System.out.println("Controllers:");
        if(this.mainController != null){
            System.out.println(mainController.path);
        } else {
            System.out.println("MainController is not defined.");
        }
        
        System.out.println("Specific controllers count: " + controllers.size());
        for (Controller controller : controllers) {
            System.out.println(controller.path);
        }
    }
    
    private void initControllers(){
        
        this.mainController = new WebFaceController(WebPath, webFolderName, indexName, WebName);
        
        RegistrationController regCtrl = new RegistrationController();
        this.controllers.addAll(regCtrl.ctrls);
        
        LoginController loginCtrl = new LoginController();
        this.controllers.addAll(loginCtrl.ctrls);
        
        TelescopeController telescopeCtrl = new TelescopeController();
        this.controllers.addAll(telescopeCtrl.ctrls);
        
        UserController userCtrl = new UserController();
        this.controllers.addAll(userCtrl.ctrls);
        
        ImageController imageCtrl = new ImageController();
        this.controllers.addAll(imageCtrl.ctrls);
    }
    
    private void initWebServer(){
        
        try {
            server = HttpServer.create(new InetSocketAddress(this.portNumber), 0);
            this.optionsInfo();
            
            this.initControllers();
            controllersInfo();
            
            for (Controller controller : controllers) {
                System.out.println("Init ctrl: " + this.APIPath + controller.path);
                HttpContext context = server.createContext(this.APIPath + controller.path, controller);
                if(controller.isUserInfoRequired) {
                    context.getFilters().add(TokenFilter.GetInstance());
                }
            }
            
            if(mainController != null){
                server.createContext(mainController.path, mainController);
            }
            
            server.setExecutor(Executors.newCachedThreadPool()); // creates a default executor
            server.start();
            System.out.println("Server started!\n\n");
        } catch (IOException ex) {
            if(ex.getMessage().equals("Address already in use: bind")){
                System.out.println("Server is not running, already bind.");
            }else {
                Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void stop() {
        server.stop(1);
    }
}
