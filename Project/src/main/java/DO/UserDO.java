package DO;

import VO.UserVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Radek
 */
public class UserDO extends BaseDO {
    
    /**
     * Insert statement includuing all propertis.
     * @param userVO 
     * @throws java.sql.SQLException 
     */
    public static void InsertUser(UserVO userVO) throws SQLException {
        String insertSQL = "INSERT INTO \"User\" (";
        
        insertSQL += "\"UserName\", ";
        insertSQL += "\"FirstName\", ";
        insertSQL += "\"LastName\", ";
        insertSQL += "\"Role\", ";
        insertSQL += "\"Email\", ";
        insertSQL += "\"UpdateDate\", ";
        insertSQL += "\"CreateDate\", ";
        insertSQL += "\"Token\", ";
        insertSQL += "\"TokenExpiration\", ";
        insertSQL += "\"Password\", ";
        insertSQL += "\"IsActive\"";
        
        insertSQL +=") VALUES (";
        
        insertSQL += "'" + userVO.UserName + "', ";
        insertSQL += "'" + userVO.FirstName + "', ";
        insertSQL += "'" + userVO.LastName + "', ";
        insertSQL += "'" + userVO.Role + "', ";
        insertSQL += "'" + userVO.Email + "', ";
        insertSQL += "'" + new Timestamp(System.currentTimeMillis()) + "', ";
        insertSQL += "'" + new Timestamp(System.currentTimeMillis()) + "', ";
        insertSQL += "'" + UUID.randomUUID() + "', ";
        insertSQL += "'" + new Timestamp(System.currentTimeMillis()) + "', ";
        insertSQL += "'" + userVO.Password + "', ";
        insertSQL += "'" + userVO.IsActive + "'";
        
        insertSQL += ");";
        
        Execute(insertSQL);
    }
    
    public static UserVO GetUser(long Id) throws SQLException {
        UserVO result = new UserVO();
        
        String SQL = "SELECT * FROM \"User\" WHERE \"Id\" = " + Id;
        
        Connection conn = GetConnection();
        
        Statement st = conn.createStatement();
        
        ResultSet rs = st.executeQuery(SQL);
        
        while (rs.next())
        {
           result = FillUserVO(rs, true);
        } 
        
        rs.close();
        st.close(); 
        conn.close();
        
        return result;
    }
    
    public static List<Integer> GetTelescopesForUser(long Id) throws SQLException {
        List<Integer> result = new ArrayList<>();
        
        String SQL = "SELECT * FROM \"UserHasTelescope\" WHERE \"IdUser\" = " + Id;
        
        Connection conn = GetConnection();
        
        Statement st = conn.createStatement();
        
        ResultSet rs = st.executeQuery(SQL);
        
        while (rs.next())
        {
           result.add(rs.getInt("IdTelescope"));
        } 
        
        rs.close();
        st.close(); 
        conn.close();
        
        return result;
    }
    
    public static void SetTelescopesForUser(long Id, List<Integer> telescopes) throws SQLException {
        String SQL = "DELETE FROM \"UserHasTelescope\" WHERE \"IdUser\" = " + Id + ";";
        
        for (Integer telescope : telescopes) {
            SQL += "INSERT INTO \"UserHasTelescope\" (\"IdUser\", \"IdTelescope\") VALUES (" + Id + ", " + telescope + " );";
        }
        
        Connection conn = GetConnection();
        
        Statement st = conn.createStatement();
        
        st.execute(SQL);
        
        st.close(); 
        conn.close();
    }
    
    public static UserVO GetUserByUserName(String userName) throws SQLException {
        UserVO result = null;
        
        String SQL = "SELECT * FROM \"User\" WHERE \"UserName\" = '" + userName + "';";
        
        Connection conn = GetConnection();
        
        Statement st = conn.createStatement();
        
        ResultSet rs = st.executeQuery(SQL);
        
        while (rs.next())
        {
           result = FillUserVO(rs, true);
        } 
        
        rs.close();
        st.close(); 
        conn.close();
        
        return result;
    }
    
    public static UserVO GetUserByEmail(String email) throws SQLException {
        UserVO result = null;
        
        String SQL = "SELECT * FROM \"User\" WHERE \"Email\" = '" + email + "';";
        
        Connection conn = GetConnection();
        
        Statement st = conn.createStatement();
        
        ResultSet rs = st.executeQuery(SQL);
        
        while (rs.next())
        {
           result = FillUserVO(rs, true);
        } 
        
        rs.close();
        st.close(); 
        conn.close();
        
        return result;
    }
    
    public static UserVO GetUserByToken(String token, String userName) throws SQLException {
        UserVO result = null;
        
        String SQL = "SELECT * FROM \"User\" WHERE \"Token\" = '" + token + "'";
        SQL += " AND \"UserName\" = '" + userName + "';";
        
        Connection conn = GetConnection();
        
        Statement st = conn.createStatement();
        
        ResultSet rs = st.executeQuery(SQL);
        
        while (rs.next())
        {
           result = FillUserVO(rs, true);
        } 
        
        rs.close();
        st.close(); 
        conn.close();
        
        return result;
    }
    
    public static List<UserVO> GetUsers() throws SQLException {
        List<UserVO> users = new ArrayList<>();
        
        String SQL = "SELECT * FROM \"User\"";
        
        Connection conn = GetConnection();
        
        Statement st = conn.createStatement();
        
        ResultSet rs = st.executeQuery(SQL);
        
        while (rs.next())
        {
            users.add(FillUserVO(rs, false));
        } 
        
        rs.close();
        st.close(); 
        conn.close();
        
        return users;
    }
    
    private static UserVO FillUserVO(ResultSet rs, boolean fillPassword) throws SQLException {
        UserVO user = new UserVO();
        
        user.Id = rs.getLong("Id");
        user.UserName = rs.getString("UserName");
        user.FirstName = rs.getString("FirstName");
        user.LastName = rs.getString("LastName");
        user.Email = rs.getString("Email");
        user.Role = rs.getString("Role");
        user.Token = rs.getObject("Token", UUID.class);
        user.TokenExpiration = rs.getTimestamp("TokenExpiration");
        user.CreateDate = rs.getTimestamp("CreateDate");
        user.UpdateDate = rs.getTimestamp("UpdateDate");
        user.IsActive = rs.getBoolean("IsActive");
        
        if(fillPassword)
            user.Password = rs.getString("Password");
        
        return user;
    }
    
    /**
     * Method updates all user properties in database. UpdateDate is automaticly
     * set to now. Id is used to search for user.
     * @param userVO
     * @throws SQLException 
     */
    public static void UpdateUser(UserVO userVO) throws SQLException{
        
        String insertSQL = "UPDATE \"User\" SET " +
        "\"FirstName\"='" + userVO.FirstName + "', " +
        "\"LastName\"='" + userVO.LastName + "', " + 
        "\"Role\"='" + userVO.Role + "', " +
        "\"Email\"='" + userVO.Email + "', " + 
        "\"UpdateDate\"='" + new Timestamp(System.currentTimeMillis()) + "', " + 
        "\"Token\"='" + userVO.Token + "', " + 
        "\"TokenExpiration\"='" + userVO.TokenExpiration + "', " +
        "\"IsActive\"='" + userVO.IsActive + "', " +
        "\"Password\"='" + userVO.Password + "'" +
        " WHERE \"Id\" = " + userVO.Id + ";";
        
        Execute(insertSQL);
    }
}
