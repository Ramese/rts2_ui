package DO;

import static DO.BaseDO.Execute;
import VO.UserVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 *
 * @author Admin
 */
public class ImageDO extends BaseDO {
    
    public static void InserImage(String path, String imageData, int idTelescope) throws SQLException {
        String insertSQL = "INSERT INTO \"ImageCache\" (";
        
        insertSQL += "\"ImagePath\", ";
        insertSQL += "\"ImageData\", ";
        insertSQL += "\"IdTelescope\", ";
        insertSQL += "\"CreateDate\", ";
        insertSQL += "\"UpdateDate\"";
        
        insertSQL +=") VALUES (";
        
        insertSQL += "'" + path + "', ";
        insertSQL += "'" + imageData + "', ";
        insertSQL += "" + idTelescope + ",";
        insertSQL += "'" + new Timestamp(System.currentTimeMillis()) + "', ";
        insertSQL += "'" + new Timestamp(System.currentTimeMillis()) + "' ";
        
        insertSQL += ");";
        
        Execute(insertSQL);
    }
    
    public static void UpdateFitsImage(int idTelescope, String path, String fitsData) throws SQLException{
        
        String insertSQL = "UPDATE \"ImageCache\" SET " +
        "\"ImageFitsData\"='" + fitsData + "'" +
        " WHERE \"IdTelescope\" = " + idTelescope + " AND \"ImagePath\" = '" + path + "';";
        
        Execute(insertSQL);
    }
    
    public static String GetImageData(int idTelescope, String path) throws SQLException {
        String SQL = "SELECT * FROM \"ImageCache\" WHERE \"ImagePath\" = '" + path + "' AND \"IdTelescope\" = " + idTelescope + ";";
        
        String result = null;
        
        Connection conn = GetConnection();
        
        Statement st = conn.createStatement();
        
        ResultSet rs = st.executeQuery(SQL);
        
        while (rs.next())
        {
           result = rs.getString("ImageData");
        } 
        
        rs.close();
        st.close(); 
        conn.close();
        
        return result;
    }
    
    public static String GetFitsImageData(int idTelescope, String path) throws SQLException {
        String SQL = "SELECT * FROM \"ImageCache\" WHERE \"ImagePath\" = '" + path + "' AND \"IdTelescope\" = " + idTelescope + ";";
        
        String result = null;
        
        Connection conn = GetConnection();
        
        Statement st = conn.createStatement();
        
        ResultSet rs = st.executeQuery(SQL);
        
        while (rs.next())
        {
           result = rs.getString("ImageFitsData");
        } 
        
        rs.close();
        st.close(); 
        conn.close();
        
        return result;
    }
}
