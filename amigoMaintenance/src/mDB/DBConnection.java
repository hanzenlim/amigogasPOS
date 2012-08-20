/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mDB;

import MStore.InventoryData;
import MStore.Product;
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
    
    private executeQuery eQuery;
    
    
    private Login login;
    
    private createTables cTables;
    
    public DBConnection()
    {

    
    }
    
    //return 0 if pass is incorrect
    //return -5 if username does not exists
    //returns greater than 0, then returns the status i.e. 1 has highest power
    public int initLoginData(String user, String pass)
    {
      
        this.tryConnectDB();
        this.eQuery = new executeQuery(connection);
        int result = this.eQuery.checkUserPass(user, pass);
        this.closeConnection();
    return result;
    }
    
    //add item
    public int addItemtoDB(Product prod)
    {
        this.tryConnectDB();
        this.eQuery = new executeQuery(connection);
        int result= this.eQuery.addItemProdCatalog(prod);
        this.closeConnection();  
    return result;
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
         System.err.println( "Unable to connect database" );
         sqlex.printStackTrace();
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
               result = this.cTables.createcreditsales();
               break;
           case 9:
               this.cTables.insertAdminData();
           default:
               System.out.println("Create Table in Maintenance Error");
               break;
             
       }
      this.closeConnection();
   return result;          
   }
   
   //get InventoryData
   public ArrayList<InventoryData> getInventoryData()
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       
       ArrayList<InventoryData> invenDataList=this.eQuery.retriveInventoryData();
       
       this.closeConnection();
   return invenDataList;
   }
   
   //set Unit Price
   //returns 0 if query is unsucessful, 1 if successful
   public int setUnitPrice(int upc, double oldPrice, double newPrice)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       int n=this.eQuery.setNewUnitPrice(upc, oldPrice, newPrice);
       
       this.closeConnection();
   return n;
   }
   
    public int setItemDescription(int upc, String item)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       int n=this.eQuery.setItemDescription(upc, item);
       
       this.closeConnection();
   return n;
   }
   
   public ArrayList<Product> getProdCatalog()
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       ArrayList<Product> temp = this.eQuery.getProdCatalog();
       this.closeConnection();
       
   return temp;    
   }
   
   //delivery
   public int addDeliveryItem(ArrayList upc, ArrayList stocks, ArrayList product, String name, String invoiceNumber)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       int result = this.eQuery.saveDeliveryItems(upc, stocks, product, name, invoiceNumber);
       this.closeConnection();
   return result;
   }
   
   //updates currBalance
   //return 1 if successful, otherwise -78
   public int updateCurrBalance(ArrayList upc, ArrayList stocks)
   {
       int result=0;
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       
       for(int i=0; i<upc.size(); i++)
       {
           System.out.println((Integer)upc.get(i));
           System.out.println((Double)stocks.get(i));
           result = this.eQuery.updateCurBalance((Integer)upc.get(i), (Double)stocks.get(i));
           if(result == -78)
               return -78;
       }
       
       this.closeConnection();
   return result;    
   }
   
   //generate EndOFTheday
   //return 0 is unsucessful
   //will return 0 if date is not inputed
   //1 succeussful
   public int endOfTheDay(String date, double amt)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       int result = this.eQuery.generateEOD(date, amt);
       
       this.closeConnection();
   return result;
   }
   
   //get Total Sales
   //use in End of the Day
   public double getTotalSales(String date)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       double result = this.eQuery.getTotalSales(date);
       this.closeConnection();
   return result;
   }
   
   public double getAmtTendered(String date)
   {
        this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       double result = this.eQuery.getAmtTenderedDailySales(date);
       this.closeConnection();
   return result;
   }
   
   //1 means EOD has been generated
   //0 means EOD has not been generated 
   //-5 no records
   public int checkEOD(String date)
   {
       int result=0;
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
        result = this.eQuery.checkIfEOD(date);
       this.closeConnection();
   return result;
   }
   
   
   
   public ArrayList generateReport(String date)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       ArrayList temp = this.eQuery.generateReport(date);
     
       //can't close it cause the values will be deleted
       //this.closeConnection();
       
   return temp;   
   }
   
   public int updatePumpReadingBeginning(String date, ArrayList<String> product, ArrayList<String> beginning, ArrayList<String> ending, ArrayList<String> uPrice, ArrayList<String> calib)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       int result = this.eQuery.updatePumpBeginningEnding(date, product, beginning, ending, uPrice,calib);
       this.closeConnection();
       
   return result;
   }
   
   public ArrayList getPumpReading(String date)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       ArrayList temp = this.eQuery.getPumpReading(date);
     
       //can't close it cause the values will be lost
       //this.closeConnection();
       
   return temp;  
   }
   
   public ArrayList getPumpReadingComplete(String date)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       ArrayList temp = this.eQuery.getPumpReadingComplete(date);
     
       //can't close it cause the values will be lost
       //this.closeConnection();
       
   return temp;  
   }
   
   public ArrayList getTotalPumpReading(String date)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       ArrayList temp = this.eQuery.getTotalPumpReading(date);
     
       //can't close it cause the values will be lost
       //this.closeConnection();
       
   return temp;  
   }
   
   
   
   //returns true if you can still edit pump reading, otherwise false
   public boolean checkPumpReadingStatus(String date)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       boolean result = this.eQuery.checkPumpReadingStatus(date);
       this.closeConnection();
       
   return result;  
   }
   
   public int updatePumpReadingLitersTotal(String date, ArrayList<String> liters, ArrayList<String> total, int status)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       int result = this.eQuery.updatePumpReadingLitersTotal(date, liters, total, status);
       this.closeConnection();
   return result;
   }
   
   public ArrayList<SalesData> getSalesTrans(String date)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       return this.eQuery.getSalesTransaction(date);
     //  this.closeConnection();
  
   }
   
   public ArrayList<DeliveryData> getDelivery(String invoice)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       return this.eQuery.getDelivery(invoice);
       
   }
   
   public ArrayList<creditData> getcreditData(String name, int status, String year)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       return this.eQuery.getcreditSales(name, status, year);
     //  this.closeConnection();
   }
   
    public ArrayList<creditData> getPaymentData(String name, String year)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       return this.eQuery.getPaymentSales(name, year);
     //  this.closeConnection();
   }
   
   public int savePaymentSales(String name, String invoice, String checkNumber)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       int result = this.eQuery.savePaymentSales(name, invoice, checkNumber);
       
       this.closeConnection();
   return result;
   }
   
   public int updateEODcreditSales()
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       int result = this.eQuery.updateEODcreditSales();
       this.closeConnection();
   return result;
   }
   
   public int updateEODpaymentSales()
   {
        this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       int result = this.eQuery.updateEODpaymentSales();
       this.closeConnection();
   return result;
   }
   
   public int updateEODAllvoidsales(int mode)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       int result = this.eQuery.updateEODAllvoidSales(mode);
       this.closeConnection();
   return result; 
   }
   
   public double getTotalCreditForReport(String date, int status)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       double amt = this.eQuery.getTotalCreditForReport(date, status);
       this.closeConnection();
   return amt;
   }
   public double getTotalPaymentForReport(String date)
   {
        this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       double amt = this.eQuery.getTotalPaymentForReport(date);
       this.closeConnection();
   return amt;
   }
   
   public ArrayList<creditData> getCreditForGeneratingReport(String date, int status)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       return this.eQuery.getCreditReport(date, status);
     //  this.closeConnection();
   }
   public ArrayList<creditData> getPaymentForGeneratingReport(String date)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       return this.eQuery.getPaymentReport(date);
     //  this.closeConnection();
   }
   
   public int deleteSaleTrans(int transID)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       
       int result1 = this.eQuery.decreaseDailySales(transID);
       
       int result2 = this.eQuery.deleteSaleTrans(transID);
       
       this.closeConnection();
       
   if(result1 ==1 && result2 ==1)   
       return 1;
   else
       return 0;
   }
   
   public int deletecreditTrans(String invoice)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       int result = this.eQuery.voidcreditsales(invoice);
       this.closeConnection();
    return result;
   }
   
   public int deletepaymentTrans(String invoice)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       int result = this.eQuery.voidpaymentsales(invoice);
       this.closeConnection();
    return result;
   }
   
   //for generating report for all void salss
   public ArrayList<creditData> getAllVoidSaleForGeneratingReport(String date, int mode)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       return this.eQuery.getAllVoidReport(date, mode);
     //  this.closeConnection();
   }
   
    public ArrayList<DeliveryData> getDeliveryReport(String date)
   {
       this.tryConnectDB();
       this.eQuery = new executeQuery(connection);
       return this.eQuery.getDeliveryReport(date);
       
   }
   
   
   
    
   
}
