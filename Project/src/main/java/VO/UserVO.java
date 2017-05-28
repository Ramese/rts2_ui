package VO;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Data transfer object for User.
 * @author Radek
 */
public class UserVO {
    public static String COL_ID = "Id";
    public static String COL_USERNAME = "UserName";
    public static String COL_FIRSTNAME = "FirstName";
    public static String COL_LASTNAME = "LastName";
    public static String COL_ROLE = "Role";
    public static String COL_EMAIL = "Email";
    public static String COL_UPDATEDATE = "UpdateDate";
    public static String COL_CREATEDATE = "CreateDate";
    public static String COL_TOKEN = "Token";
    public static String COL_TOKENEXPIRATION = "TokenExpiration";
    public static String COL_PASSWORD = "Password";
    
    public long Id;
    public String UserName;
    public String FirstName;
    public String LastName;
    public String Role;
    public String Email;
    public Timestamp UpdateDate;
    public Timestamp CreateDate;
    public UUID Token;
    public Timestamp TokenExpiration;
    public String Password;
    public boolean IsActive;
}

