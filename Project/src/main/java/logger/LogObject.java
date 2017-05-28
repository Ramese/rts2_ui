package logger;

/**
 *
 * @author Radek
 */
public class LogObject {
    public static final String LOGGER_LEVEL_ERROR   =   "ERROR";
    public static final String LOGGER_LEVEL_DEBUG   =   "DEBUG";
    public static final String LOGGER_LEVEL_WARNING =   "WARNING";
    public static final String LOGGER_LEVEL_INFO    =   "INFO";
    
    private boolean logToFile = false;
    private boolean logToDatabase = false;
    private boolean logToHttpAPI = false;
    
    private String filePath;
    private String apiPath;
    
    private static LogObject instance;
    
    private LogObject(){
        instance = new LogObject();
    }
    
    public static LogObject getInstance(){
        return instance;
    }
    
    public boolean setLogToFile(String file){
        filePath = file;
        
        if(true) {
            logToFile = true;
        } else {
            logToFile = false;
        }
        
        return logToFile;
    }
    
    public boolean setLogToHttpApI(String api){
        apiPath = api;
        
        if(true){
            logToHttpAPI = true;
        } else {
            logToHttpAPI = false;
        }
        
        return logToHttpAPI;
    }
    
    public boolean setLogToDatabase(){
        
        if(true) {
            logToDatabase = true;
        } else {
            logToDatabase = false;
        }
        
        return logToDatabase;
    }
    
    public void logMessage(String level, String message){
        if(logToDatabase)
            logMessageToDatabase(level, message);
        
        if(logToFile)
            logMessageToFile(level, message);
        
        if(logToHttpAPI)
            logMessageToHttpAPI(level, message);
    }
    
    private void logMessageToDatabase(String level, String message){
        
    }
    
    private void logMessageToHttpAPI(String level, String message){
        
    }
    
    private void logMessageToFile(String level, String message){
        
    }
    
    
}
