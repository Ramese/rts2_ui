package Tests;

import VO.UserVO;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.UUID;

/**
 *
 * @author Radek
 */
public class ConnectionTest {
    
    public static void p(Object o) {
        for(Field var : o.getClass().getFields()){
            if(Modifier.isStatic(var.getModifiers())) {
                continue;
            }
            
            //System.out.println(var);
            try {
                System.out.println(var.getName() + ": " + var.get(o));
            } catch (IllegalArgumentException ex) {
                //Logger.getLogger(ConnectionTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                //Logger.getLogger(ConnectionTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception e) {
                
            }
        }
    }
    
    public static void pokus() throws Exception {
        String SQL = "SELECT * FROM \"Role\"";
        
        Class.forName("org.postgresql.Driver");
            
        Connection con = DriverManager.getConnection("jdbc:postgresql://192.168.56.101:5432/rts2_web", "postgres", "postgres");
        Statement st = con.createStatement();
        
        ResultSet rs = st.executeQuery(SQL);
        
        while (rs.next())
        {
            String Name = rs.getString("Name");
            Timestamp ts = rs.getTimestamp("CreateDate");
            
            System.out.println(Name);
            System.out.println(ts);
        } 
        
        rs.close();
        st.close();
        con.close();
    }
    
    public static void main(String[] args) {
        try {
            UserVO o = new UserVO();
            
            o.UserName = "ahoj";
            o.CreateDate = new Timestamp(System.currentTimeMillis());
            o.Token = UUID.randomUUID();
            System.out.println(o);
            
           
            pokus();
            
            Class.forName("org.postgresql.Driver");
            
            Connection con = DriverManager.getConnection("jdbc:postgresql://192.168.56.101:5432/rts2_web", "postgres", "postgres");
            
            if(con != null)
                System.out.println("Connected");
            
            Statement st = con.createStatement();
            
            String sql = "INSERT INTO \"User\" (\"UserName\", \"FirstName\", \"LastName\", \"Role\", \"Email\") VALUES ('user2', 'Radek', 'Mečiar', 'Admin', 'meciarad@fel.cvut.cz');";
            
            st.execute(sql);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
