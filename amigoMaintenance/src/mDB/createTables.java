/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mDB;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hanzen
 */
public class createTables {
    
    private Statement statement;
    private ResultSet resultSet;
    
    private Connection connection;
    private DBConnection DB;
    
    public createTables(Connection con)
    {
        this.connection = con;
        
    }
    
    public boolean createSales()
   {
    if(!this.checkIfTableExists("sales"))
    {
        try {
	statement = connection.createStatement();
        
        statement.executeUpdate("create table sales (sID bigserial PRIMARY KEY, "
                + "date date, time varchar(20), upc integer[], items text[], liters double precision[], unitPrice double precision[], total double precision, amtTendered double precision, change double precision)");
        
        statement.close();
        
        return true;
        }
        catch ( SQLException sqlex ) {
                 sqlex.printStackTrace();
        }
      
//        statement.executeUpdate("create table gasBalance (sID bigserial PRIMARY KEY, date date, "
//                + "Diesel1 integer, Diesel2 integer, Unleaded1 integer, Unleaded2 integer, Regular1 integer,"
//                + "Regular2 integer)");
//        
//        
//        statement.executeUpdate("create table currPrice (Diesel numeric, Unleaded numeric, Premium numeric, "
//                + "Regular numeric)");
//
//        
//        
//        //statement.executeUpdate("create table sales (sID bigserial PRIMARY KEY, dateOT date, type varchar(15), liters numeric, amount numeric)");
//        
//        statement.executeUpdate("insert into currBalance (date, Diesel, Unleaded, Premium, Regular) values (current_date, '2000', '3000', '4000', '5000')");
//        statement.executeUpdate("insert into currBalance (date, Diesel, Unleaded, Premium, Regular) values (current_date, '1995', '2995', '3995', '4995')");
//
//        
//        statement.executeUpdate("insert into currPrice values ('41.44', '51.33', '55.99', '60.11')");
//        statement.executeUpdate("insert into newSales (date, items, liters, unitPrice, total) values (current_date, '{\"Diesel 1\", \"Unleaded\"}',  '{23 ,44, 55.55}', '{40.11 ,50.55, 60.99}', 800)");
//
////        statement.executeUpdate("insert into sales (dateOT, type, liters, amount) values ('2012-01-04','diesel', 12.45, 340.34)");
////	statement.executeUpdate("insert into sales (dateOT, type, liters, amount) values ('2012-01-04','diesel', 12.45, 340.34)");

//	statement.close();
//      }
//      catch ( SQLException sqlex ) {
//         sqlex.printStackTrace();
//      }
    }
    
   return false;
   }
    
    
   public boolean createcurrBalance()
   {
     if(!this.checkIfTableExists("currbalance"))
     {
        try {
	statement = connection.createStatement();
        
       statement.executeUpdate("create table currbalance (ID bigserial PRIMARY KEY,"
               + "upc integer UNIQUE REFERENCES prodcatalog (upc), balance double precision)");
        
        statement.close();
        
        return true;
        }
        catch ( SQLException sqlex ) {
                 sqlex.printStackTrace();
        } 
         
         return true;
     }
   return false;
   }
   
   public boolean createDailySales()
   {
       if(!this.checkIfTableExists("dailysales"))
       {
            try {
            statement = connection.createStatement();
        
            statement.executeUpdate("create table dailySales (ID bigserial PRIMARY KEY, date date UNIQUE, "
               + "upc integer[], liters double precision[], unitprice double precision[], totalsales double precision, amttendered double precision, status integer)");
        
            statement.close();
        
            return true;
            }
            catch ( SQLException sqlex ) {
                 sqlex.printStackTrace();
            }  
        return true;
       }
   return false;
   }
   
   public boolean createProductCatalog()
   {
     if(!this.checkIfTableExists("prodcatalog"))
       {
            try {
            statement = connection.createStatement();
        
            statement.executeUpdate("create table prodcatalog (ID bigserial PRIMARY KEY,"
               + "UPC integer UNIQUE, Description varchar(40), unitPrice double precision)");
        
            statement.close();
        
            return true;
            }
            catch ( SQLException sqlex ) {
                 sqlex.printStackTrace();
            }  
        return true;
       }
   return false;  
   }
   
   public boolean createAdmin()
   {
       if(!this.checkIfTableExists("admin"))
       {
            try {
            statement = connection.createStatement();
        
            statement.executeUpdate("create table admin (ID bigserial PRIMARY KEY,"
               + "firstName varchar(20), lastName varchar(20), username varchar(20), password varchar(20), status integer)");
        
            statement.close();
        
            return true;
            }
            catch ( SQLException sqlex ) {
                 sqlex.printStackTrace();
            }  
        return true;
       }
   return false;  
   }
   
   public boolean createDelivery()
   {
       if(!this.checkIfTableExists("delivery"))
       {
            try {
            statement = connection.createStatement();
        
            statement.executeUpdate("create table delivery (id bigserial PRIMARY KEY,"
               + "date date, name varchar(40), invoice varchar(20) UNIQUE, upc integer[], stock double precision[], product varchar(20)[])");
      
            statement.close();
        
            return true;
            }
            catch ( SQLException sqlex ) {
                 sqlex.printStackTrace();
            }  
        return true;
       }
   return false; 
   }
   
   public void dropAllTables()
   {  
      try {
	statement = connection.createStatement();
	
	statement.executeUpdate("drop table sales");
        statement.executeUpdate("drop table gasBalance");
        statement.executeUpdate("drop table dailySales");
        statement.executeUpdate("drop table ProdCatalog");
        statement.executeUpdate("drop table admin");
        statement.executeUpdate("drop table pumpreading");
        statement.executeUpdate("drop table delivery");
        
        statement.close(); 
      } catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
   }
   
   private boolean checkIfTableExists(String tableName)
   {
        
        // check if table exists
        ResultSet tables;
        try {
            DatabaseMetaData dbm = connection.getMetaData();
            tables = dbm.getTables(null, null, tableName, null);
            
            if (tables.next()) {
            // Table exists
                //System.out.println("Tables exists");
                return true;
              }
            else {
                // Table does not exist
                //System.out.println("Tables  DOES not exists");
                return false;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
   return true;     
   }
   
   public boolean createPumpReading()
   {
       if(!this.checkIfTableExists("pumpreading"))
       {
            try {
            statement = connection.createStatement();
        
            statement.executeUpdate("create table pumpreading (id bigserial PRIMARY KEY, date date, product varchar(20)[], beginning varchar(20)[], ending varchar(20)[]," +
"liters varchar(20)[], unitprice varchar(20)[], total varchar(20)[], status integer);"); 
        
            statement.close();
        
            return true;
            }
            catch ( SQLException sqlex ) {
                 sqlex.printStackTrace();
            }  
        return true;
       }
   return false; 
   }
   
    public boolean createcreditsales()
   {
       if(!this.checkIfTableExists("creditsales"))
       {
            try {
            statement = connection.createStatement();
        
            statement.executeUpdate("create table creditsales (id bigserial PRIMARY KEY, date date, dategenerated date"
                    + " name varchar(40)[], invoice varchar(20)[], items varchar(40)[]," +
"liters varchar(20)[], unitprice varchar(20)[], total varchar(20)[], amtcredit double precision, status integer, generated integer);"); 
        
            statement.close();
        
            return true;
            }
            catch ( SQLException sqlex ) {
                 sqlex.printStackTrace();
            }  
        return true;
       }
   return false; 
   }
   
   
   public int insertAdminData()
   {int result =0;
            try {
            statement = connection.createStatement();
            String query ="insert into admin (firstname, lastname, username, password, status) values ('admin', 'admin', 'admin', 'luckylight', 1);"; 
            result = statement.executeUpdate(query); 
             query ="insert into admin (firstname, lastname, username, password, status) values ('cashier', 'cashier', 'cashier', 'cashier', 3);"; 
            result = statement.executeUpdate(query); 
            query ="insert into admin (firstname, lastname, username, password, status) values ('manager', 'manager', 'manager', 'brother789', 2);"; 
            result = statement.executeUpdate(query); 
             query ="insert into admin (firstname, lastname, username, password, status) values ('manager', 'manager', 'manager', 'brother789', 2);"; 
            result = statement.executeUpdate(query); 
            
            
            statement.close();
            }
            catch ( SQLException sqlex ) {
                 sqlex.printStackTrace();
            }  
   return result; 
   }
   
   
    
    
   
    
}
