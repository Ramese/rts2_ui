package DO;

import static DO.BaseDO.Execute;
import VO.TelescopeVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Radek
 */
public class TelescopeDO extends BaseDO {
    public static void InsertTelescope(TelescopeVO telescopeVO) throws SQLException {
        String insertSQL = "INSERT INTO \"Telescope\" (";
        
        insertSQL += "\"Name\", ";
        insertSQL += "\"Address\", ";
        insertSQL += "\"AccountName\", ";
        insertSQL += "\"AccountPassword\", ";
        insertSQL += "\"UpdateDate\", ";
        insertSQL += "\"CreateDate\", ";
        insertSQL += "\"PublicTimeStart\", ";
        insertSQL += "\"PublicTimeEnd\", ";
        insertSQL += "\"IsPublicEnabled\"";
        
        insertSQL +=") VALUES (";
        
        insertSQL += "'" + telescopeVO.Name + "', ";
        insertSQL += "'" + telescopeVO.Address + "', ";
        insertSQL += "'" + telescopeVO.AccountName + "', ";
        insertSQL += "'" + telescopeVO.AccountPassword + "', ";
        insertSQL += "'" + new Timestamp(System.currentTimeMillis()) + "', ";
        insertSQL += "'" + new Timestamp(System.currentTimeMillis()) + "', ";
        insertSQL += "'" + telescopeVO.PublicTimeStart + "', ";
        insertSQL += "'" + telescopeVO.PublicTimeEnd + "',";
        insertSQL += "'" + telescopeVO.IsPublicEnabled + "'";
        
        insertSQL += ");";
        
        Execute(insertSQL);
    }
    
    public static void UpdateTelescope(TelescopeVO telescopeVO) throws SQLException{
        
        String insertSQL = "UPDATE \"Telescope\" SET " +
        "\"Name\"='" + telescopeVO.Name + "', " +
        "\"Address\"='" + telescopeVO.Address + "', " + 
        "\"AccountName\"='" + telescopeVO.AccountName + "', " +
        "\"AccountPassword\"='" + telescopeVO.AccountPassword + "', " + 
        "\"UpdateDate\"='" + new Timestamp(System.currentTimeMillis()) + "', " +
        "\"PublicTimeStart\"='" + telescopeVO.PublicTimeStart + "', " + 
        "\"PublicTimeEnd\"='" + telescopeVO.PublicTimeEnd + "', " + 
        "\"IsPublicEnabled\"='" + telescopeVO.IsPublicEnabled + "'" + 
        " WHERE \"Id\" = " + telescopeVO.Id + ";";
        
        Execute(insertSQL);
    }
    
    public static TelescopeVO GetTelescope(long Id) throws SQLException {
        TelescopeVO result = new TelescopeVO();
        
        String SQL = "SELECT * FROM \"Telescope\" WHERE \"Id\" = " + Id;
        
        Connection conn = GetConnection();
        
        Statement st = conn.createStatement();
        
        ResultSet rs = st.executeQuery(SQL);
        
        while (rs.next())
        {
           result = FillTelescopeVO(rs, true);
        } 
        
        rs.close();
        st.close(); 
        conn.close();
        
        return result;
    }
    
    public static TelescopeVO GetTelescopeByName(String name) throws SQLException {
        TelescopeVO result = null;
        
        String SQL = "SELECT * FROM \"Telescope\" WHERE \"Name\" = '" + name + "';";
        
        Connection conn = GetConnection();
        
        Statement st = conn.createStatement();
        
        ResultSet rs = st.executeQuery(SQL);
        
        while (rs.next())
        {
           result = FillTelescopeVO(rs, true);
        } 
        
        rs.close();
        st.close(); 
        conn.close();
        
        return result;
    }
    
    public static List<TelescopeVO> GetTelescopes() throws SQLException {
        List<TelescopeVO> telescopes = new ArrayList<>();
        
        String SQL = "SELECT * FROM \"Telescope\"";
        
        Connection conn = GetConnection();
        
        Statement st = conn.createStatement();
        
        ResultSet rs = st.executeQuery(SQL);
        
        while (rs.next())
        {
            telescopes.add(FillTelescopeVO(rs, false));
        } 
        
        rs.close();
        st.close(); 
        conn.close();
        
        return telescopes;
    }
    
    private static TelescopeVO FillTelescopeVO(ResultSet rs, boolean fillPassword) throws SQLException {
        TelescopeVO telescope = new TelescopeVO();
        
        telescope.Id = rs.getLong("Id");
        telescope.Name = rs.getString("Name");
        telescope.Address = rs.getString("Address");
        if(fillPassword) {
            telescope.AccountName = rs.getString("AccountName");
            telescope.AccountPassword = rs.getString("AccountPassword");
        }
        telescope.CreateDate = rs.getTimestamp("CreateDate");
        telescope.UpdateDate = rs.getTimestamp("UpdateDate");
        
        telescope.PublicTimeStart = rs.getTimestamp("PublicTimeStart");
        telescope.PublicTimeEnd = rs.getTimestamp("PublicTimeEnd");
        telescope.IsPublicEnabled = rs.getBoolean("IsPublicEnabled");
        
        return telescope;
    }
}
