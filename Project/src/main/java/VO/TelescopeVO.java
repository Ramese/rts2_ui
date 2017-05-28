package VO;

import java.sql.Timestamp;

/**
 *
 * @author Radek
 */
public class TelescopeVO {
    public long Id;
    public String Name;
    public String Address;
    public String AccountName;
    public String AccountPassword;
    public Timestamp UpdateDate;
    public Timestamp CreateDate;
    public Timestamp PublicTimeStart;
    public Timestamp PublicTimeEnd;
    public boolean IsPublicEnabled;
}
