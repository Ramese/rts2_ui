package rts2ui;

/**
 *
 * @author Radek
 */
public class Config {
    public static final boolean GLOBAL_DEBUG = true;
    public static final String DATABASE_PATH = "jdbc:postgresql://192.168.56.101:5432/rts2_web";
    public static final String DATABASE_USER_NAME = "postgres";
    public static final String DATABASE_USER_PASSWORD = "postgres";
    
    public static final class WebConfig{
        public static final String GLOBAL_WEB_NAME = "RTS2";
        public static final String GLOBAL_WEB_PATH = "/rts2";
        public static final String GLOBAL_WEB_API_PATH = "/API";
        public static final String GLOBAL_WEB_FOLDER_PATH = "./Web";
        public static final String GLOBAL_WEB_INDEX_NAME = "index.html";
        public static final int GLOBAL_WEB_PORT_NUMBER = 4458;
        public static int GLOBAL_WEB_SOCKET_PORT_NUMBER = 8080;
    }
    
}
