package BO;

import static BO.TelescopeBO.GetTelescope;
import static BO.TelescopeBO.GetURIBuilder;
import DO.ImageDO;
import VO.HttpResponseVO;
import VO.ImageVO;
import VO.TelescopeVO;
import client.RTS2Client;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Admin
 */
public class ImagesBO {
    public static HttpResponseVO GetCurrent(long idTelescope) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        return RTS2Client.GetCurrent(GetURIBuilder(t));
    }
    
    private static List<ImageVO> GetListFromHTML(String html) {
        List<ImageVO> result = new ArrayList<>();
        
        if(!html.contains("<img")) {
            return result;
        }

        while(html.contains("<img")) {
            
            html = html.substring(html.indexOf("<img") + 4);
            
            result.add(GetImageVOFromHTMLString(html));
        }
        
        return result;
    }
    
    public static List<ImageVO> GetImagesListForDate(long idTelescope, String date) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        HttpResponseVO htmlListOfImages = RTS2Client.GetHTMLPageOfImagesFromDate(GetURIBuilder(t), date);
        
        return GetListFromHTML(htmlListOfImages.Body);
    }
    
    public static List<ImageVO> GetImagesListForTarget(long idTelescope, int idTarget) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        HttpResponseVO htmlListOfImages = RTS2Client.GetHTMLPageOfImagesFromTarget(GetURIBuilder(t), idTarget);
        
        return GetListFromHTML(htmlListOfImages.Body);
    }
    
    public static HttpResponseVO GetImage(long idTelescope, String path) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        HttpResponseVO response;
        
        String data = ImageDO.GetImageData((int)idTelescope, path);
        
        // this image is not in local db
        if(data == null){
            response = RTS2Client.GetImage(GetURIBuilder(t), path);
            
            if(response.Code == 200)
                ImageDO.InserImage(path, response.Body, (int)idTelescope);
        } else { // image found in local db
            response = new HttpResponseVO();
            
            response.Body = data;
            response.Code = 200;
            response.Message = "OK";
        }
        
        return response;
    }
    
    public static HttpResponseVO GetFitsImage(long idTelescope, String path) throws Exception {
        TelescopeVO t = GetTelescope(idTelescope);
        
        HttpResponseVO response;
        
        String data = ImageDO.GetFitsImageData((int)idTelescope, path);
        
        // this image is not in local db
        if(data == null){
            response = RTS2Client.GetFitsImage(GetURIBuilder(t), path);
            
            if(response.Code == 200)
                ImageDO.UpdateFitsImage((int)idTelescope, path, response.Body);
        } else { // image found in local db
            response = new HttpResponseVO();
            
            response.Body = data;
            response.Code = 200;
            response.Message = "OK";
        }
        
        return response;
    }
    
    private static ImageVO GetImageVOFromHTMLString(String html) {
        ImageVO result = new ImageVO();
        
        html = html.substring(0, html.indexOf(">"));
        
        if(html.contains("src=")){
            result.previewPath = html.substring(html.indexOf("src=") + 5);
            result.previewPath = result.previewPath.substring(0, result.previewPath.indexOf("'"));
        }
        
        if(html.contains("onClick")) {
            result.imagePath = html.substring(html.indexOf("onClick"));
            result.imagePath = result.imagePath.substring(result.imagePath.indexOf("\"") + 1);
            result.imagePath = result.imagePath.substring(0, result.imagePath.indexOf("\""));
        }
        
        return result;
    }
    
}
