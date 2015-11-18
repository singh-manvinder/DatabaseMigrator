
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author manvinder
 */
/*
public class DbTest {
    public static void main(String... s) throws SQLException
    {
       Connection conn= ConnectionProvider.getConnection("mysql","root","rockstar","localhost","3306");
       Statement stmt=conn.createStatement();
       DatabaseMetaData metadata=conn.getMetaData();
       ResultSet dbname=metadata.getCatalogs();
       while(dbname.next())
       {
           String db=dbname.getString("TABLE_CAT");
           if(db.equals("information_schema")||db.equals("mysql")||db.equals("performance_schema"))
           {
               continue;
           }
           stmt.executeUpdate("use "+db+";");
           //Connection conn_temp=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db,"root","rockstar");
           Statement stmt_temp=conn.createStatement();
           
          // String name = rs.getString("name");
          // String salary = rs.getString("salary");
           ResultSet tableNames=metadata.getTables(db,null,"%",null);
           System.out.println("db name: "+db);
           while(tableNames.next())
           {
               String table=tableNames.getString(3);
               System.out.println("TABLE NAME: "+table);
               String query="create table "+table+"_new2(";
               //stmt_temp.executeUpdate("create table "+table+"_new");
         
               ResultSet columns=metadata.getColumns(db,null,table,null);
               //System.out.println("checkpoint1");
               boolean check=false;
               while(columns.next())
               {
                   if(check)
                   {
                       query=query+",";
                   }
                   //System.out.println("checkpoint2");
                   String type=columns.getString("TYPE_NAME");
                   if(type.equals("VARCHAR"))
                   {
                       type=type+"(20)";
                   }
                   String column=columns.getString("COLUMN_NAME");
                   System.out.println(type+"\t"+column);
                   query=query+column+" "+type;
                   check=true;
               }
               
               query=query+")";
               System.out.print(query);
               stmt_temp.executeUpdate(query);
           }
           System.out.println("\n\n");
          // System.out.println(dbname.getString("TABLE_CAT"));
       }
    }
    
}*/