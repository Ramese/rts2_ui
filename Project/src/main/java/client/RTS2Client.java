package client;

import VO.HttpResponseVO;
import VO.TargetVO;
import VO.TelescopeImageVO;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import webcontroller.controllers.Controller;


public class RTS2Client {
    
    // <editor-fold defaultstate="collapsed" desc="Base methods">
    private static HttpResponseVO GetResponse(URIBuilder requestURL) throws Exception {
        CloseableHttpResponse response = GetResponseInternal(requestURL);
        
        HttpResponseVO result = new HttpResponseVO();
        
        result.Code = response.getStatusLine().getStatusCode();
        //result.Body = Controller.GetStringFromBody(response.getEntity().getContent());
        
        if(response.getFirstHeader("Content-Type").getValue().equals("image/jpeg")){
            result.Body = "data:image/jpeg;base64,";
            
            byte[] bytes = IOUtils.toByteArray(response.getEntity().getContent());
            result.Body += Base64.getEncoder().encodeToString(bytes);
            
        } else if(response.getFirstHeader("Content-Type").getValue().equals("image/png")){
            result.Body = "data:image/png;base64,";
            
            byte[] bytes = IOUtils.toByteArray(response.getEntity().getContent());
            result.Body += Base64.getEncoder().encodeToString(bytes);
            
        } else if(response.getFirstHeader("Content-Type").getValue().equals("image/fits")){
            result.Body = "data:image/fits;base64,";
            
            byte[] bytes = IOUtils.toByteArray(response.getEntity().getContent());
            result.Body += Base64.getEncoder().encodeToString(bytes);
            
        } else {
            result.Body = EntityUtils.toString(response.getEntity());
        }
        
        result.Message = response.getStatusLine().getReasonPhrase();
        
        response.close();
        return result;
    }
    
    private static HttpResponseVO GetResponse(URIBuilder requestURL, boolean ImageExpected) throws Exception {
        if(!ImageExpected)
            return GetResponse(requestURL);
        
        CloseableHttpResponse response = GetResponseInternal(requestURL);
        
        HttpResponseVO result = new HttpResponseVO();
        
        result.Code = response.getStatusLine().getStatusCode();
        result.Message = response.getStatusLine().getReasonPhrase();
        
        if(result.Code != 200){
            result.Body = Controller.GetStringFromBody(response.getEntity().getContent());
            
            response.close();
            return result;
        }
        
        result.Body = "image included";

        InputStream is = response.getEntity().getContent();
        DataInputStream dis = new DataInputStream(is);

        result.Image = TelescopeImageVO.Fill(dis);
        
        response.close();
        return result;
    }
    
    /**
     * Universal method for http request. Just to reduce code.
     * @param serverUrl
     * @param query
     * @param userName
     * @param password
     * @return
     * @throws IOException 
     */
    private static CloseableHttpResponse GetResponseInternal(URIBuilder requestURL) throws IOException, Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        
        if(requestURL.getUserInfo() == null) {
            
        } else {
            if(requestURL.getUserInfo().indexOf(":") != requestURL.getUserInfo().lastIndexOf(":")) {
                throw new Exception("User name or password contains char ':'");
            }
            
            int index = requestURL.getUserInfo().indexOf(":");
            
            if(index < 1) {
                throw new Exception("Credentials contain only user name. Missing char ':'.");
            }
            
            String user = requestURL.getUserInfo().substring(0, index);
            String password = requestURL.getUserInfo().substring(index+1);
            
            UsernamePasswordCredentials creds;
            creds = new UsernamePasswordCredentials(user, password);
            
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(new AuthScope(requestURL.getHost(), AuthScope.ANY_PORT), creds);
            
            context.setCredentialsProvider(credsProvider);
            
            requestURL.setUserInfo(null);
        }
        
        String url = requestURL.build().toString();
        
        HttpGet httpget = new HttpGet(requestURL.build());
        return httpclient.execute(httpget, context);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Devices and properties">
    public static HttpResponseVO GetDevices(URIBuilder serverInfo) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/api/devices");
        
        HttpResponseVO result = GetResponse(serverInfo);
        
        return result;
    }
    
    public static HttpResponseVO GetDeviceProperties(URIBuilder serverInfo, String device) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/api/get");
        serverInfo.addParameter("d", device);
        serverInfo.addParameter("e", "1");
        
        HttpResponseVO result = GetResponse(serverInfo);
        
        return result;
    }
    
    public static HttpResponseVO SetValue(URIBuilder serverInfo, String device, String ValueName, String Value) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/api/set");
        serverInfo.addParameter("d", device); // device name (C0, T0, ...)
        serverInfo.addParameter("n", ValueName); // value name (data_type, ra, .. )
        serverInfo.addParameter("v", Value); // new value
        
        HttpResponseVO result = GetResponse(serverInfo);
        
        return result;
    }
    
    public static HttpResponseVO SendCommand(URIBuilder serverInfo, String device, String command) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/api/cmd");
        serverInfo.addParameter("d", device); // device name (C0, T0, ...)
        serverInfo.addParameter("c", command); // new value
        
        HttpResponseVO result = GetResponse(serverInfo);
        
        return result;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Images methods">
    public static HttpResponseVO GetCurrentImage(URIBuilder serverInfo, String ccdSensor) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/api/currentimage");
        serverInfo.addParameter("ccd", ccdSensor);
        
        HttpResponseVO result = GetResponse(serverInfo, true);
        
        return result;
    }
    
    public static HttpResponseVO GetExposeData(URIBuilder serverInfo, String ccdSensor) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/api/exposedata");
        serverInfo.addParameter("ccd", ccdSensor);
        
        HttpResponseVO result = GetResponse(serverInfo, true);
        
        return result;
    }
    
    public static HttpResponseVO GetCurrent(URIBuilder serverInfo) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/current/");
        
        HttpResponseVO result = GetResponse(serverInfo);
        
        return result;
    }
    
    public static HttpResponseVO GetHTMLPageOfImagesFromDate(URIBuilder serverInfo, String date) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/preview/images/xmlrpcd/" + date);
        
        HttpResponseVO result = GetResponse(serverInfo);
        
        return result;
    }
    
    public static HttpResponseVO GetHTMLPageOfImagesFromTarget(URIBuilder serverInfo, int idTarget) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/targets/" + idTarget + "/images/");
        
        HttpResponseVO result = GetResponse(serverInfo);
        
        return result;
    }
    
    public static HttpResponseVO GetImage(URIBuilder serverInfo, String path) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/jpeg" + path);
        
        HttpResponseVO result = GetResponse(serverInfo);
        
        return result;
    }
    
    public static HttpResponseVO GetFitsImage(URIBuilder serverInfo, String path) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/fits" + path);
        
        HttpResponseVO result = GetResponse(serverInfo);
        
        return result;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Targets">
    public static HttpResponseVO GetTargets(URIBuilder serverInfo) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/targets/api");
        
        HttpResponseVO result = GetResponse(serverInfo);
        
        return result;
    }
    
    public static HttpResponseVO SearchTargets(URIBuilder serverInfo, String targetName) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/api/tbyname");
        serverInfo.addParameter("n", "%" + targetName + "%");
        serverInfo.addParameter("e", "1");
        
        
        HttpResponseVO result = GetResponse(serverInfo);
        
        return result;
    }
    
    public static HttpResponseVO GetTargetById(URIBuilder serverInfo, long id) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/api/tbyid");
        serverInfo.addParameter("id", ""+id);
        serverInfo.addParameter("e", "1");
        
        HttpResponseVO result = GetResponse(serverInfo);
        
        return result;
    }
    
    public static HttpResponseVO CreateTarget(URIBuilder serverInfo, TargetVO target) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/api/create_target");
        //serverInfo.addParameter("id", ""+target.Id);
        serverInfo.addParameter("tn", target.Name);
        serverInfo.addParameter("ra", target.RA);
        serverInfo.addParameter("dec", target.DEC);
        serverInfo.addParameter("type", target.Type);
        serverInfo.addParameter("info", target.Info);
        // not in doc:
        //serverInfo.addParameter("comment", target.Description);
        
        HttpResponseVO result = GetResponse(serverInfo);
        
        return result;
    }
    
    public static HttpResponseVO UpdateTarget(URIBuilder serverInfo, TargetVO target) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/api/update_target");
        serverInfo.addParameter("id", ""+target.Id);
        serverInfo.addParameter("tn", target.Name);
        serverInfo.addParameter("ra", target.RA);
        serverInfo.addParameter("dec", target.DEC);
        //serverInfo.addParameter("pm_ra", target.PM_RA);
        //serverInfo.addParameter("pm_dec", target.PM_DEC);
        //serverInfo.addParameter("enabled", target.Enabled);
        // not in code:
        //serverInfo.addParameter("desc", target.Description);
        
        HttpResponseVO result = GetResponse(serverInfo);
        
        return result;
    }
    
    public static HttpResponseVO SetAutonomousObservationsForTarget(URIBuilder serverInfo, TargetVO target) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/api/update_target");
        serverInfo.addParameter("id", ""+target.Id);
        serverInfo.addParameter("enabled", target.Enabled);
        
        HttpResponseVO result = GetResponse(serverInfo);
        
        return result;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Scheduling">
    public static HttpResponseVO GetPlan(URIBuilder serverInfo, String from, String to) throws Exception {
        serverInfo.setPath(serverInfo.getPath() + "/api/plan");
        serverInfo.addParameter("from", from);
        serverInfo.addParameter("to", to);
        
        HttpResponseVO result = GetResponse(serverInfo);
        
        return result;
    }
    // </editor-fold>

    
    
}
