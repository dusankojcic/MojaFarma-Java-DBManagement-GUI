
package farma2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Konekcija {
    
    public static Statement poveziSe() {
        Statement st=null;
        try {
            Connection con = DriverManager.getConnection
            ("jdbc:mysql://localhost/farma","root","");
            st = con.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
         return st;
      }
        
}
    

