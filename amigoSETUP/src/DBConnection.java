/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Hanzen
 */
public class DBConnection {
    
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
 
    private createTables cTables;
    
    public DBConnection()
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
   
   //create tables
   
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
           case 7:
               result = this.cTables.createPumpReading();
               break;
          case 8:
                this.cTables.insertAdminData();
               break;
          case 9:
               result=this.cTables.createcreditsales();
              break;
          case 10:
               result = this.cTables.createpaymentsales();
              break;
               case 11:
               result = this.cTables.createvoidcreditsales();
              break;
               case 12:
               result = this.cTables.createvoidpaymentsales();
              break;
               case 13:
               result = this.cTables.createvoidsales();
              break;     
           default:
               System.out.println("Create Table in Maintenance Error");
               break;
             
       }
      this.closeConnection();
   return result;          
   }
   
   public void droppAllTable()
   {
       this.tryConnectDB();
        cTables = new createTables(connection);
        cTables.dropAllTables();
        this.closeConnection();
    
   }
   
   
  
   
   
    
   
}
