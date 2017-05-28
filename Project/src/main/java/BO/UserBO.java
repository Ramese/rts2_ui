package BO;

import DO.UserDO;
import VO.RoleVO;
import VO.UserVO;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Radek
 */
public class UserBO {
    private static final long MS_IN_EIGHT_HOURS = 8*60*60*1000;
    
    public static List<UserVO> GetUsers() throws SQLException {
        return UserDO.GetUsers();
    }
    
    /**
     * Check if user token is expired, if it is not it will extend expiration date.
     * @param token
     * @param userName
     * @return  User if user's token is not expired.
     * @throws SQLException 
     */
    public static UserVO IsLoggedIn(String token, String userName) throws SQLException {
        UserVO user = UserDO.GetUserByToken(token, userName);
        
        if(user == null) {
            return null;
        }
        
        if(user.TokenExpiration.before(new Timestamp(System.currentTimeMillis()))) {
            return null;
        }
        
        return ExtendTokenExpiration(token, userName);
    }
    
    /**
     * Method sets token expiration date on value now + 8 hours. No conditions.
     * @param token
     * @param userName
     * @return  User info including new token expiration date.
     * @throws SQLException 
     */
    private static UserVO ExtendTokenExpiration(String token, String userName) throws SQLException {
        UserVO user = UserDO.GetUserByToken(token, userName);
        
        long eightHours = MS_IN_EIGHT_HOURS;
        user.TokenExpiration = new Timestamp(System.currentTimeMillis() + eightHours);
        
        UserDO.UpdateUser(user);
        
        user.Password = null;
        
        return user;
    }
    
    /**
     * Check credentials in database.
     * @param userName
     * @param password
     * @return
     * @throws SQLException 
     */
    public static UserVO Login(String userName, String password) throws SQLException {
        UserVO user = UserDO.GetUserByUserName(userName);
        
        if(user == null) {
            return null;
        }
        
        if(user.Password.equals(password)) {
            //if token is expired - generate new token
            if(!user.TokenExpiration.after(new Timestamp(System.currentTimeMillis()))) {
                user.Token = UUID.randomUUID();
            }

            //save
            UserDO.UpdateUser(user);
            
            //extend token time
            user = ExtendTokenExpiration(user.Token.toString(), user.UserName);
            
            //erase pass for client site
            user.Password = null;
            
            //return token
            return user;
        } else {
            return null;
        }
    }
    
    public static boolean IsPasswordStrong(String pass){
        if(pass == null || pass.equals(""))
            return false;
        if(pass.trim().length() < 8)
            return false;
        
        return true;
    }
    
    /**
     * 
     * @param userVO
     * @return 
     */
    public static boolean IsUserValidForInsert(UserVO userVO) {
        if(userVO == null) {
            return false;
        }
        
        if(userVO.UserName == null || userVO.UserName.trim().length() < 3){
            return false;
        }
        
        if(userVO.Email == null)
            return false;
        
        if(!IsPasswordStrong(userVO.Password))
            return false;
        
        if(userVO.Role == null)
            return false;
        
        if(!(userVO.Role.equals(RoleVO.ADMIN) || userVO.Role.equals(RoleVO.PUBLIC) || userVO.Role.equals(RoleVO.SCIENTIST)))
            return false;
        
        return true;
    }
    
    public static UserVO GetUser(long id) throws SQLException {
        return UserDO.GetUser(id);
    }
    
    public static UserVO GetUserByUserName(String userName) throws SQLException {
        return UserDO.GetUserByUserName(userName);
    }
    
    public static UserVO GetUserByEmail(String userName) throws SQLException {
        return UserDO.GetUserByEmail(userName);
    }
    
    public static UserVO GetUserByToken(String token, String userName) throws SQLException {
        return UserDO.GetUserByToken(token, userName);
    }
    
    public static void InsertUser(UserVO userVO) throws Exception {
        if(IsUserValidForInsert(userVO))
        {
            UserDO.InsertUser(userVO);
        }
        else
            throw new Exception("User data are invalid.");
    }
    
    public static void UpdateUser(UserVO userVO) throws Exception {
        if(IsUserValidForInsert(userVO))
        {
            UserDO.UpdateUser(userVO);
        }
        else
            throw new Exception("User data are invalid.");
    }
    
    public static List<Integer> GetTelescopesForUser(int idUser) throws SQLException {
        return UserDO.GetTelescopesForUser(idUser);
    }
    
    public static boolean SetTelescopesForUser(int idUser, List<Integer> telescopes) throws SQLException {
        UserDO.SetTelescopesForUser(idUser, telescopes);
        
        return true;
    }
}
