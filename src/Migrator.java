
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
       Statement stmt=destConn.createStatement();
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
           stmt.executeUpdate("create database "+db+";");
           stmt.executeUpdate("use "+db+";");
           //Connection conn_temp=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db,"root","rockstar");
           Statement stmt_temp=destConn.createStatement();
           
          // String name = rs.getString("name");
          // String salary = rs.getString("salary");
           ResultSet tableNames=metadata.getTables(db,null,"%",null);
           System.out.println("db name: "+db);
           while(tableNames.next()) //migrating tables
           {
            
               String table=tableNames.getString(3);
               System.out.println("TABLE NAME: "+table);
               String query="create table "+table+"(";
               //stmt_temp.executeUpdate("create table "+table+"_new");
           
               ResultSet columns=metadata.getColumns(db,null,table,null);
               //System.out.println("checkpoint1");
               boolean check=false;
               int count=0;
               while(columns.next())  //migrating columns
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
               
               ResultSet pkey=metadata.getPrimaryKeys(db,null, table);
               String pKeyColumn="";
               boolean temp=true;
                while (pkey.next()) 
                {
                    if(temp)
                        pKeyColumn = pkey.getString("COLUMN_NAME");
                  
                    else
                        pKeyColumn = pKeyColumn+","+pkey.getString("COLUMN_NAME");
                        
                    System.out.println("\ngetPrimaryKeys(): columnName=" + pKeyColumn);
                    temp=false;
                }
                if(!pKeyColumn.equals(""))
                {
                    String pKeyQuery="ALTER TABLE "+table+" ADD CONSTRAINT PK PRIMARY KEY ("+pKeyColumn+")";
                    stmt_temp.executeUpdate(pKeyQuery);
                    System.out.println("pkeyquery:"+pKeyQuery);
               
                }
                Statement stmt_source=sourceConn.createStatement();
                stmt_source.executeQuery("use "+db+";");
                ResultSet data=stmt_source.executeQuery("select * from "+db+"."+table);
               ResultSetMetaData metadata_temp= data.getMetaData();
              
               while(data.next())
               {
            
                   System.out.println("check1");
                 String insertQuery="insert into "+table+" values(";
                 System.out.println("count:"+count);
                 
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
                    stmt=destConn.createStatement();
                    stmt.executeUpdate(insertQuery);
               }
           }
           System.out.println("\n\n");
          // System.out.println(dbname.getString("TABLE_CAT"));
       }
    }
}
