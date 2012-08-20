/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import GUI.InventoryData;
import Store.Product;
import Store.Transaction;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Hanzen
 */
public class ConnectToDB {
    
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    
    private createTables cTables;
    
    private executeQuery execQuery;
    
    
    public ConnectToDB()
    {
       
    }
    
    public void tryConnectDB()
    {
       
      // The URL specifying the 675Examples database to which 
      // this program connects using JDBC to connect to a
      // Microsoft ODBC database.
      String url = "jdbc:postgresql://localhost:5432/postgres";  //CHANGE THIS!

	String username = "postgres";
	String password = "luckylight";

      // Load the driver to allow connection to the database
      try {
         Class.forName( "org.postgresql.Driver" );

         connection = DriverManager.getConnection( 
            url, username, password );
      } 
      catch ( ClassNotFoundException cnfex ) {
         System.err.println( 
            "Failed to load JDBC/ODBC driver." );
         cnfex.printStackTrace();
         System.exit( 1 );  // terminate program
      }
      catch ( SQLException sqlex ) {
         System.err.println( "Unable to connect" );
         //sqlex.printStackTrace();
         JOptionPane.showMessageDialog(null, "Error in Database, check database connection Exiting", "Exiting", JOptionPane.ERROR_MESSAGE);
         System.exit(1);
      }
    }
    
   //close SQL connection
   public void closeConnection()
   {
      try {
         connection.close();
      }
      catch ( SQLException sqlex ) {
         System.err.println( "Unable to disconnect" );
         sqlex.printStackTrace();
      }
   }
   
   public boolean createTables(int i)
   {
       this.tryConnectDB();
       cTables = new createTables(connection);
       boolean result = false;
       switch(i){
           case 1:
               result=this.cTables.createSales();
               break;
           case 2:
               result = this.cTables.createDailySales();
               break;
           case 3:
               result = this.cTables.createProductCatalog();
               break;
           case 4:
               result = this.cTables.createcurrBalance();
               break;
           case 5:
               result = this.cTables.createDelivery();
               break;
           case 6:
               result = this.cTables.createAdmin();
               break;
           default:
               System.out.println("Create Table in Maintenance Error");
               break;
             
       }
      this.closeConnection();
   return result;          
   }
  
   public ArrayList<Product> getProdCatalog()
   {
       this.tryConnectDB();
       this.execQuery = new executeQuery(connection);
       ArrayList<Product> temp=this.execQuery.getProdCatalog();
       this.closeConnection();
   return temp;
   }
   
   public int getLastKeyInserted()
   {
       this.tryConnectDB();
       this.execQuery = new executeQuery(connection);
       int x=this.execQuery.getLastInsertedKey();
       this.closeConnection();
   return x;
   }
   
   public int insertTransaction(Transaction trans)
   {
       int result=0;
     
       this.tryConnectDB();
       this.execQuery = new executeQuery(connection);
        result = this.execQuery.insertTransaction(trans);
         if(result==-78)
           return -78;
       
        int result2 = this.execQuery.updateCurBalance(trans);
        if(result2==-78)
           return -78;
       
        this.execQuery.checkIfDailySalesHasBeenInit();
     
       int result3= this.execQuery.updateDailySales(trans);
        if(result3==-78)
           return -78;
        
       this.closeConnection();
       
   return result;
   }
   
   public int initLoginData(String user, String pass)
   {
       
        this.tryConnectDB();
        this.execQuery = new executeQuery(connection);
        int result = this.execQuery.checkUserPass(user, pass);
        this.closeConnection();
   return result;
   }
   
   //get InventoryData
   public ArrayList<InventoryData> getInventoryData()
   {
       this.tryConnectDB();
       this.execQuery = new executeQuery(connection);
       
       ArrayList<InventoryData> invenDataList=this.execQuery.retriveInventoryData();
       
       this.closeConnection();
   return invenDataList;
   }
   
   public int insertCreditSales(Transaction trans,String name, String invoice)
   {
        int result=0;
       this.tryConnectDB();
       this.execQuery = new executeQuery(connection);
       result = this.execQuery.insertCreditSales(trans, name,invoice);
       
       if(result==-78)
           return -78;
       int result2 = this.execQuery.updateCurBalance(trans);
      
//       if(result2==-78)
//           return -78;
//       this.execQuery.checkIfDailySalesHasBeenInit();
//     
//       int result3= this.execQuery.updateDailySales(trans);
//        if(result3==-78)
//           return -78;
       this.closeConnection();
       
   return result;
   }
   
}
