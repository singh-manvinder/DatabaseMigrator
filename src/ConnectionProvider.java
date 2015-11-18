
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author manvinder
 */
public class ConnectionProvider {
    
    public static Connection getConnection(String serverName,String userName,String password,String host,String port) throws ClassNotFoundException, SQLException
    {
        Connection conn=null;
        String url=null;
        String driver="";
        if(serverName.equals("mysql"))
        {
            driver="com.mysql.jdbc.Driver";
            url="jdbc:mysql://"+host+":"+port;
            
        }
        
        if(serverName.equals("oracle"))
        {
            driver="oracle.jdbc.OracleDriver";
            url="jdbc:oracle:thin:@"+host+":"+port+":xe";
        }
        
            Class.forName(driver);
            conn=DriverManager.getConnection(url,userName,password);
       
        
        
        return conn;
    }
}
