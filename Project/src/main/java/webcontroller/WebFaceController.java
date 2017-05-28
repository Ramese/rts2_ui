package webcontroller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static rts2ui.Config.GLOBAL_DEBUG;
import logger.LogObject;
/**
 *
 * @author Radek
 */
public class WebFaceController implements HttpHandler {
    public final String path;  // /Web
    public final String webFolderPath; // ./Content
    public final String indexName; // index.htm
    public final String webName; // title of web
    
    /**
     * Controller for web face. If requested file exists the controller make response including
     * the file. In other case response includes index page which make error page.
     * @param path Part of URL string. URL: http://server/path Default value is "/Web".
     * @param webFolderName Folder includes static web content. Default value "./Web" which is located in project folder.
     * @param indexName Main index file. Default value "index.htm".
     * @param webName Title of web.
     */
    public WebFaceController(String path, String webFolderName, String indexName, String webName) {
//        if(!path.matches(Common.PATTERN_WEB_FACE)){
//            throw new UnsupportedOperationException("Path to web interface is incorrect. Checked with pattern: \"" + Common.PATTERN_WEB_FACE + "\"");
//        }
//        
//        if(!webFolderName.matches(Common.PATTERN_WEB_CONTENT_FOLDER)){
//            throw new UnsupportedOperationException("Path to web content is incorrect. Checked with pattern: \"" + Common.PATTERN_WEB_CONTENT_FOLDER + "\"");
//        }
//        
//        if(!indexName.matches(Common.PATTERN_WEB_INDEX_NAME)){
//            throw new UnsupportedOperationException("Index name is incorrect. Checked with pattern: \"" + Common.PATTERN_WEB_INDEX_NAME + "\"");
//        }
        
        this.indexName = indexName;
        this.path = path;
        this.webFolderPath = webFolderName;
        this.webName = webName;
    }

    /**
     * Get HTML page with internal server error response.
     * @return 
     */
    private String getBadFolderPathError(){
        String badResponse = "<!doctype html>\n";
        badResponse += "<html>\n";
        badResponse += "\t<head>\n";
        badResponse += "\t\t<title>" + this.webName + " - Web folder does not exist.</title>\n";
        badResponse += "\t</head>\n";
        badResponse += "\t<body>\n";
        badResponse += "\t\t<h1>WebFaceController</h1>\n";
        badResponse += "\t\t<span>Web folder does not exist. Path to web folder: " + this.webFolderPath + "</span>\n";
        badResponse += "\t</body>\n";
        badResponse += "</html>\n";
        
        return badResponse;
    }
    
    private byte[] readBinaryFile(String aFileName) throws IOException {
        Path p = Paths.get(aFileName);
        return Files.readAllBytes(p);
    }
    
    private String getFileContent(String file) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            
            br.close();
            
            return everything;
        } catch (IOException ex) {
            LogObject.getInstance().logMessage(LogObject.LOGGER_LEVEL_ERROR, "File does not exist: " + file);
        }  finally {
            
        }
        
        return "";
    }
    
    private boolean existsWebFolder(){
        File f = new File(this.webFolderPath);
        return f.isDirectory();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean isPlainText = true;
        byte[] file = null;
        
        System.out.println("WebFaceController:");
        System.out.println(exchange.getRequestURI());
//        System.out.println("-RequestHeaders:");
//        Common.headerInfo(exchange.getRequestHeaders());

        if(!existsWebFolder()){
            String badResponse = this.getBadFolderPathError();
            exchange.sendResponseHeaders(HTTPProtocol.RESPONSE_CODE_INTERNAL_ERROR, badResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(badResponse.getBytes());
            os.close();
            
            System.out.println("Web folder does not exists. Folder: " + this.webFolderPath);
            return;
        }
        
        
        
        File f;
        String fileName = webFolderPath;
        if(exchange.getRequestURI().toString().startsWith("~/")) {
            fileName += exchange.getRequestURI().toString().substring(2);
        } else if(exchange.getRequestURI().toString().startsWith(path)) {
            fileName += exchange.getRequestURI().toString().substring(path.length());
        } else { 
            fileName += exchange.getRequestURI().toString();
        }
        
        if(fileName.indexOf("?") > 0) {
            fileName = fileName.substring(0, fileName.indexOf("?"));
        }
        
        System.out.println("Cheking file: " + fileName);
        
        f = new File(fileName);
        
        String response = "";
        if(f.isFile()){
            System.out.println("File exists.");
            
            if(fileName.endsWith(".mp3")){
                exchange.getResponseHeaders().add(HTTPProtocol.HEADER_CONTENT_TYPE, HTTPProtocol.HEADER_CONTENT_TYPE_MP3);
                System.out.println("MP3");
                isPlainText = false;
                file = readBinaryFile(fileName);
            } else if(fileName.endsWith(".png")){
                exchange.getResponseHeaders().add(HTTPProtocol.HEADER_CONTENT_TYPE, HTTPProtocol.HEADER_CONTENT_TYPE_PNG);
                System.out.println("PNG");
                isPlainText = false;
                file = readBinaryFile(fileName);
            } else if(fileName.endsWith(".woff2")){
                exchange.getResponseHeaders().add(HTTPProtocol.HEADER_CONTENT_TYPE, HTTPProtocol.HEADER_CONTENT_TYPE_WOFF2);
                System.out.println("WOFF2");
                isPlainText = false;
                file = readBinaryFile(fileName);
            } else if(fileName.endsWith(".css")) {
                exchange.getResponseHeaders().add(HTTPProtocol.HEADER_CONTENT_TYPE, HTTPProtocol.HEADER_CONTENT_TYPE_CSS);
                System.out.println("CSS");
                isPlainText = true;
                response = getFileContent(fileName);
            } else if(fileName.endsWith(".jpg")) {
                exchange.getResponseHeaders().add(HTTPProtocol.HEADER_CONTENT_TYPE, HTTPProtocol.HEADER_CONTENT_TYPE_JPG);
                System.out.println("JPG");
                isPlainText = false;
                response = getFileContent(fileName);
            } else {
                response = getFileContent(fileName);
                System.out.println("Len: " + response.length());
            }

        } else {
            if(exchange.getRequestURI().toString().contains(".")){
                exchange.sendResponseHeaders(404, -1);
                return;
            }
            fileName = webFolderPath + "/" + indexName;
            
            System.out.println("File does not exist. Using :" + fileName);
            
            response = getFileContent(fileName);
            System.out.println("Len: " + response.length());
            //exchange.sendResponseHeaders(200, response.length());
        }
        
        OutputStream os = exchange.getResponseBody();
        if(isPlainText){
            file = response.getBytes();
            exchange.sendResponseHeaders(200, 0);
            
            os.write(file);
        } else {
            if(file != null){
                exchange.sendResponseHeaders(200, 0);
                os.write(file);
            }
        }
        
        os.close();
        System.out.println("");
    }
    
}
