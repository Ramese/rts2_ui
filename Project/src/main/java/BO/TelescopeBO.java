package BO;

import DO.TelescopeDO;
import VO.HttpResponseVO;
import VO.TargetVO;
import VO.TelescopeVO;
import VO.UserVO;
import client.RTS2Client;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.utils.URIBuilder;

/**
 *
 * @author Radek
 */
public class TelescopeBO {
    
    public static String GetCommandForAddToQueue(String queueName, int idTarget, String from, String to, String rep, String sep) {
        
        String command = "queue_at_nrep ";
                
        command += queueName + " ";
        command += idTarget + " ";

        if(from == null || from.trim().equals("")){
            command += "nan ";
        } else {
            command += from + " ";
        }

        if(to == null || to.trim().equals("")){
            command += "nan ";
        } else {
            command += to + " ";
        }

        if(rep == null || rep.trim().equals("")){
            command += "-1 ";
        } else {
            command += rep + " ";
        }

        if(sep == null || sep.trim().equals("")){
            command += "nan ";
        } else {
            command += sep + "";
        }
                
        return command;
    }
    
    public static List<TelescopeVO> GetTelescopes(UserVO user) throws SQLException {
        
        if(user.Role.toLowerCase().equals("admin")){
            return TelescopeDO.GetTelescopes();
        }
        
        List<TelescopeVO> all = TelescopeDO.GetTelescopes();
        
        List<Integer> approved = UserBO.GetTelescopesForUser((int)user.Id);
        
        List<TelescopeVO> result = new ArrayList<>();
        
        for(TelescopeVO t : all) {
            if(approved.contains((int)t.Id)){
                result.add(t);
            }
        }
        
        return result;
    }
    
    public static List<TelescopeVO> GetTelescopesForPublic() throws SQLException {
        List<TelescopeVO> tels = TelescopeDO.GetTelescopes();
        
        for(int i = 0; i < tels.size(); i++) {
            TelescopeVO t = tels.get(i);
            
            if(!t.IsPublicEnabled) {
                tels.remove(t);
                i--;
            }
            
            t.AccountName = "";
            t.AccountPassword = "";
            t.Address = "";
        }
        
        return tels;
    }
     
    public static TelescopeVO GetTelescope(long idTelescope) throws SQLException {
        return TelescopeDO.GetTelescope(idTelescope);
    }
    
    public static HttpResponseVO GetDevices(long idTelescope) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        return RTS2Client.GetDevices(GetURIBuilder(t));
    }
    
    public static HttpResponseVO GetDeviceProperties(long idTelescope, String device) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        return RTS2Client.GetDeviceProperties(GetURIBuilder(t), device);
    }
    
    public static HttpResponseVO GetCurrentImage(long idTelescope, String ccdSensor) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        return RTS2Client.GetCurrentImage(GetURIBuilder(t), ccdSensor);
    }
    
    public static HttpResponseVO GetExposeData(long idTelescope, String ccdSensor) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        return RTS2Client.GetExposeData(GetURIBuilder(t), ccdSensor);
    }
    
    public static HttpResponseVO SetValue(long idTelescope, String device, String valueName, String value) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        return RTS2Client.SetValue(GetURIBuilder(t), device, valueName, value);
    }
    
    public static HttpResponseVO SendCommand(long idTelescope, String device, String command) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        return RTS2Client.SendCommand(GetURIBuilder(t), device, command);
    }
    
    public static HttpResponseVO GetTargets(long idTelescope) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        return RTS2Client.GetTargets(GetURIBuilder(t));
    }
    
    public static HttpResponseVO SearchTargets(long idTelescope, String targetName) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        return RTS2Client.SearchTargets(GetURIBuilder(t), targetName);
    }
    
    public static HttpResponseVO GetTargetById(long idTelescope, long idTarget) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        return RTS2Client.GetTargetById(GetURIBuilder(t), idTarget);
    }
    
    public static HttpResponseVO CreateTarget(long idTelescope, TargetVO target) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        return RTS2Client.CreateTarget(GetURIBuilder(t), target);
    }
    
    public static HttpResponseVO UpdateTarget(long idTelescope, TargetVO target) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        return RTS2Client.UpdateTarget(GetURIBuilder(t), target);
    }
    
    public static HttpResponseVO SetAutonomousObservationsForTarget(long idTelescope, TargetVO target) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        return RTS2Client.SetAutonomousObservationsForTarget(GetURIBuilder(t), target);
    }
    
    public static HttpResponseVO GetPlan(long idTelescope, String from, String to) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        return RTS2Client.GetPlan(GetURIBuilder(t), from, to);
    }
    
    public static TelescopeVO GetTelescope(String name) throws SQLException {
        return TelescopeDO.GetTelescopeByName(name);
    }
    
    public static boolean IsTelescopeValidForInsert(TelescopeVO telescopeVO) {
        if(telescopeVO == null) {
            return false;
        }
        
        if(telescopeVO.Name == null || telescopeVO.Name.trim().length() < 3){
            return false;
        }
        
        return true;
    }
    
    public static void InsertTelescope(TelescopeVO telescopeVO) throws Exception {
        if(IsTelescopeValidForInsert(telescopeVO))
        {
            TelescopeDO.InsertTelescope(telescopeVO);
        }
        else
            throw new Exception("Telescope data are invalid.");
    }
    
    public static void UpdateTelescope(TelescopeVO telescopeVO) throws Exception {
        if(IsTelescopeValidForInsert(telescopeVO)) {
            TelescopeDO.UpdateTelescope(telescopeVO);
        } else {
            throw new Exception("Telescope data are invalid.");
        }
    }
    
    public static URIBuilder GetURIBuilder(TelescopeVO t) throws URISyntaxException {
        URIBuilder ub;
        
        if(t.Address.startsWith("http") || t.Address.startsWith("https")){
            ub = new URIBuilder(t.Address);
        } else {
            
            ub = new URIBuilder("http://" + t.Address);
        }
        
        ub.setUserInfo(t.AccountName, t.AccountPassword);
        
        return ub;
    }
}
