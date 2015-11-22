
import javax.swing.JPanel;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author manvinder
 */
public class DbOperation {
    public static void addName(JPanel dbPanel,Connection conn) throws SQLException
    {
        Statement stmt=conn.createStatement();
       DatabaseMetaData metadata=conn.getMetaData();
       ResultSet dbname=metadata.getCatalogs();
       
        try {
                while(dbname.next())
                {
                    String db=dbname.getString("TABLE_CAT");
                    if(db.equals("information_schema")||
                   db.equals("mysql")||db.equals("performance_schema")||
                   db.equals("sakila")||db.equals("test")||db.equals("world"))
                    {
                   continue;
                    }
                    
                    else
                    {
                       
                        System.out.println(db);
                        JCheckBox cb=new JCheckBox(db);
                        dbPanel.add(cb);
                    }
                }
            } 
        catch (SQLException ex) {
            Logger.getLogger(DbOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
