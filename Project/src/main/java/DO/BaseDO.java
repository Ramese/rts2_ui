package DO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import rts2ui.Config;

/**
 *
 * @author Radek
 */
abstract class BaseDO {
    protected static void Execute(String SQL) throws SQLException {

        Connection con = GetConnection();

        if(con == null) {
            System.err.println("Connection failed!");
            return;
        }

        Statement st = con.createStatement();

        st.execute(SQL);

        st.close();
        con.close();
    }
    
    protected static Connection GetConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            
            Connection con = DriverManager.getConnection(Config.DATABASE_PATH, Config.DATABASE_USER_NAME, Config.DATABASE_USER_PASSWORD);
            
            return con;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
