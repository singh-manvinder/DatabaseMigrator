
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author manvinder
 */
public class Migrator {
    public void migrate(Connection sourceConn,Connection destConn) throws SQLException
    {
        //Connection conn= ConnectionProvider.getConnection("mysql","root","rockstar","localhost","3306");
       Statement stmt=sourceConn.createStatement();
       DatabaseMetaData metadata= sourceConn.getMetaData();
       ResultSet dbname=metadata.getCatalogs();
       while(dbname.next())
       {
           String db=dbname.getString("TABLE_CAT");
           if(db.equals("information_schema")||
                   db.equals("mysql")||db.equals("performance_schema")||
                   db.equals("sakila")||db.equals("test")||db.equals("world"))
           {
               continue;
           }
           stmt.executeUpdate("create database "+db+"_3;");
           stmt.executeUpdate("use "+db+"_3;");
           //Connection conn_temp=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db,"root","rockstar");
           Statement stmt_temp=sourceConn.createStatement();
           
          // String name = rs.getString("name");
          // String salary = rs.getString("salary");
           ResultSet tableNames=metadata.getTables(db,null,"%",null);
           System.out.println("db name: "+db);
           while(tableNames.next())
           {
               String table=tableNames.getString(3);
               System.out.println("TABLE NAME: "+table);
               String query="create table "+table+"(";
               //stmt_temp.executeUpdate("create table "+table+"_new");
         
               ResultSet columns=metadata.getColumns(db,null,table,null);
               //System.out.println("checkpoint1");
               boolean check=false;
               int count=0;
               while(columns.next())
               {
                   count++;
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
               
               ResultSet data=stmt.executeQuery("select * from "+db+"."+table);
               ResultSetMetaData metadata_temp= data.getMetaData();
               while(data.next())
               {
            
                   System.out.println("check1");
                 String insertQuery="insert into "+table+" values(";
                    for(int i=1;i<=count;i++)
                    {
                        int colType = metadata_temp.getColumnType(i);
                        if(colType==Types.VARCHAR||colType==Types.CHAR)
                        {
                          insertQuery=insertQuery+"'"+data.getString(i)+"'";  
                        }
                        else
                        {
                            insertQuery=insertQuery+data.getString(i);
                        }
                     if(i==count)
                         break;
                     else
                       insertQuery=insertQuery+",";  
                    } 
                    insertQuery=insertQuery+")";
                    System.out.println(insertQuery);
                    stmt=sourceConn.createStatement();
                    stmt.executeUpdate(insertQuery);
               }
           }
           System.out.println("\n\n");
          // System.out.println(dbname.getString("TABLE_CAT"));
       }
    }
}
