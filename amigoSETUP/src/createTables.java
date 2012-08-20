/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



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
                + "date date, time varchar(20), upc integer[], items text[], liters numeric[], unitPrice numeric[], total double precision, amtTendered double precision, change double precision)");
        
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
               + "date date, name varchar(40), invoice varchar(20) UNIQUE, upc integer[], stock double precision[], product varchar(40)[])");
      
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
      
        statement.executeUpdate("drop table dailysales");
        
        statement.executeUpdate("drop table admin");
        statement.executeUpdate("drop table pumpreading");
        statement.executeUpdate("drop table creditsales");
        statement.executeUpdate("drop table currbalance");
        statement.executeUpdate("drop table delivery");
        statement.executeUpdate("drop table prodcatalog");
         statement.executeUpdate("drop table paymentsales");
           statement.executeUpdate("drop table voidcreditsales");
             statement.executeUpdate("drop table voidpaymentsales");
               statement.executeUpdate("drop table voidsales");
        
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
        
            statement.executeUpdate("create table pumpreading (id bigserial PRIMARY KEY, date date UNIQUE, product varchar(30)[], beginning varchar(20)[], ending varchar(20)[]," +
"liters varchar(20)[], unitprice varchar(20)[], calib varchar(20)[], total varchar(20)[], status integer);"); 
        
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
            String query ="insert into admin (firstname, lastname, username, password, status) values ('admin', 'admin', 'admin', 'nestea', 1);"; 
            result = statement.executeUpdate(query); 
             query ="insert into admin (firstname, lastname, username, password, status) values ('cashier', 'cashier', 'cashier', 'cashier', 3);"; 
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
   
   public void random()
   {
         boolean yahoo=true;
         for(int i=0; i<10000; i++)
         {
         try {
            String query ="insert into credit (date,name,amount,status) values (current_date, 'hello', 500, 0);"; 
            
             if(yahoo==true){  
             statement = connection.createStatement();
            int result = statement.executeUpdate(query); 
            
             statement.close();
             
             }
             else{
                query ="insert into credit (date,name,amount,status) values (current_date, 'hello', 500, 1);"; 

                statement = connection.createStatement();
                int result = statement.executeUpdate(query); 
            
                statement.close();
             }
         
          }
          catch ( SQLException sqlex ) {
              sqlex.printStackTrace();
          }
            if(i%50==0)
                yahoo=false;
            else
                yahoo=true;
         }
   }
   
     public boolean createcreditsales()
   {
       if(!this.checkIfTableExists("creditsales"))
       {
            try {
            statement = connection.createStatement();
        
            statement.executeUpdate("create table creditsales (id bigserial PRIMARY KEY, date date, time varchar(20), dategenerated date,"
                    + " name varchar(40), invoice varchar(20) UNIQUE, upc integer[], items varchar(40)[]," +
"liters double precision[], unitprice double precision[], amtcredit double precision, status integer, generated integer);"); 
        
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
     
   public boolean createpaymentsales()
   {
       if(!this.checkIfTableExists("paymentsales"))
       {
            try {
            statement = connection.createStatement();
        
            statement.executeUpdate("create table paymentsales (id bigserial PRIMARY KEY, date date, time varchar(20), datepaid date,"
                    + " name varchar(40), invoice varchar(20) UNIQUE, upc integer[], items varchar(40)[]," +
"liters double precision[], unitprice double precision[], amtpayment double precision, checkNumber varchar(30), dategenerated date, generated integer);"); 
        
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
   
    public boolean createvoidcreditsales()
   {
       if(!this.checkIfTableExists("voidcreditsales"))
       {
            try {
            statement = connection.createStatement();
        
            statement.executeUpdate("create table voidcreditsales (id bigserial PRIMARY KEY, date date, time varchar(20), dategenerated date," +
 "name varchar(30), invoice varchar(30), upc integer[],  items varchar(30)[], liters numeric[], unitprice numeric[]," +
"amtcredit double precision, status integer, generated integer)"); 
        
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
    
     public boolean createvoidpaymentsales()
   {
       if(!this.checkIfTableExists("voidpaymentsales"))
       {
            try {
            statement = connection.createStatement();
        
            statement.executeUpdate("create table voidpaymentsales (id bigserial PRIMARY KEY, date date, time varchar(20), datepaid date,name varchar(30), invoice varchar(30), upc integer[], items varchar(30)[], liters numeric[], unitprice numeric[],amtpayment double precision, checknumber varchar(30), dategenerated date, generated integer);");      
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
     
      public boolean createvoidsales()
   {
       if(!this.checkIfTableExists("voidsales"))
       {
            try {
            statement = connection.createStatement();
        
            statement.executeUpdate("create table voidsales (sid bigserial PRIMARY KEY, date date, time varchar(20), upc integer[],items text[], liters numeric[], unitprice numeric[],total double precision, amttendered double precision, change double precision, dategenerated date, generated integer);");    
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
   
    
    
   
    
}
