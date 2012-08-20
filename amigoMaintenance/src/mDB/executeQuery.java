
package mDB;


import MStore.InventoryData;
import MStore.Item;
import MStore.Product;
import MStore.Transaction;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import mDB.SalesData;


public class executeQuery {
    
    private Connection connection;
    private Statement statement;
    
    //0 = get unit price, 1=get last inserted key, 2 = get inventory data
    private final String [] SQL_retrieve ={"SELECT * FROM ProdCatalog", "select max(sid) from sales",
    "select currbalance.upc, prodcatalog.description, unitprice, balance from currbalance, prodcatalog where currbalance.upc = prodcatalog.upc"};
    
    public executeQuery(Connection con)
    {
        this.connection = con;
  
    }
    
   //Retrieve Data 
   //gets unit price
   public ArrayList<Product> getProdCatalog()  
   {
      Statement statement;
      ResultSet resultSet;
      ArrayList<Product> tempList = new ArrayList<Product>();
      
      try {
         String query = SQL_retrieve[0];  //CHANGE THIS!

         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {
//                     JOptionPane.showMessageDialog(this, 
//                        "ResultSet contained no records" );
//                     setTitle( "No records to display" );
             
              System.out.println("NO RECORDS FOUND");
             return null;
          }
          Vector columnHeads = new Vector();
       
             //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();

             for ( int i = 1; i <= rsmd.getColumnCount(); ++i ) 
                columnHeads.addElement( rsmd.getColumnName( i ) );

             // get row data
             do {
              Vector currentRow = new Vector();
              for ( int i = 1; i <= rsmd.getColumnCount(); ++i )
              { 
                 switch( rsmd.getColumnType( i ) ) 
                 {
                    case Types.BIGINT:
                       // System.out.println("BIGINT");
                          currentRow.addElement( resultSet.getInt(i));
                       break;
                    case Types.NUMERIC:
                        //System.out.println("NUMERIC");
                          currentRow.addElement( resultSet.getDouble(i));
                       break;
                    case Types.INTEGER:
                        //System.out.println("INTEGER");
                          currentRow.addElement( resultSet.getInt(i));
                       break;
                    case Types.VARCHAR:
                       //System.out.println("VARCHAR");
                          currentRow.addElement( resultSet.getString(i));
                       break;
                    case Types.DOUBLE:
                        //System.out.println("DOUBLE");
                          currentRow.addElement(  resultSet.getDouble(i));
                        break;
                    default: 
                       System.out.println("Type was" +
                          rsmd.getColumnTypeName( i ) );
                 }
              }

              int upc = (Integer)currentRow.get(1);
              String desc = (String)currentRow.get(2);
              double price = (Double)currentRow.get(3);
              
              Product prod= new Product(desc, price, upc);
              tempList.add(prod);
              
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
   return tempList;
   }
   
   //save item to productCatalog table in DB
   //and create an item in currbalance table
   public int addItemProdCatalog(Product prod)
   {
       //insert into prodcatalog (upc, description, unitprice) values ('5', 'Unleaded 5', '52.29')
       String query="insert into prodcatalog (upc, description, unitprice) values (";
       
       query+="'" + prod.getUPC() + "', '" + prod.getDesciption() + "', '" + prod.getPrice() + "')";
       
       String query2="insert into currbalance (upc, balance) values (" + prod.getUPC() + ", 0)";
       
        try {
        this.statement = connection.createStatement();
                
        statement.executeUpdate(query);
        statement.executeUpdate(query2);
        
        statement.close();
      
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace(); 
             return -5;
        } 
   return 1;    
   }
   
   public int getLastInsertedKey()
   {
     Statement statement;
     ResultSet resultSet;
     int key = 0;
      
      try {
         String query = SQL_retrieve[1];  //CHANGE THIS!

         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {        
              System.out.println("NO RECORDS FOUND");
          }
//          Vector columnHeads = new Vector();
//          //get column heads
//          ResultSetMetaData rsmd = resultSet.getMetaData();
//
//          for ( int i = 1; i <= rsmd.getColumnCount(); ++i ) 
//              columnHeads.addElement( rsmd.getColumnName( i ) );

             // get row data
             do {
              ArrayList currentRow = this.extractRow(resultSet);
              key = (Integer)currentRow.get(0);
              
              
             } while ( resultSet.next() );

      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
   return key;
   }
   
   
   public ArrayList extractRow(ResultSet resultSet)
   {
       ResultSetMetaData rsmd;
       ArrayList currentRow = new ArrayList();
     //  Vector currentRow = new Vector();
        try {
            rsmd = resultSet.getMetaData();
            
        for ( int i = 1; i <= rsmd.getColumnCount(); ++i )
              { 
                 switch( rsmd.getColumnType( i ) ) 
                 {
                    case Types.BIGINT:
                       // System.out.println("BIGINT");
                          currentRow.add( resultSet.getInt(i));
                       break;
                    case Types.NUMERIC:
                        //System.out.println("NUMERIC");
                          currentRow.add(resultSet.getDouble(i));
                       break;
                    case Types.INTEGER:
                        //System.out.println("INTEGER");
                          currentRow.add( resultSet.getInt(i));
                       break;
                    case Types.VARCHAR:
                       //System.out.println("VARCHAR");
                          currentRow.add( resultSet.getString(i));
                       break;
                    case Types.DOUBLE:
                        //System.out.println("DOUBLE");
                          currentRow.add(  resultSet.getDouble(i));
                        break;
                    default: 
                       System.out.println("Type was" +
                          rsmd.getColumnTypeName( i ) );
                 }
              }
        } catch (SQLException ex) {
            Logger.getLogger(executeQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    return currentRow;
   }
   
   //get Inventory data
   public ArrayList<InventoryData> retriveInventoryData()
   {
      Statement statement;
      ResultSet resultSet;
      ArrayList<InventoryData> tempList = new ArrayList<InventoryData>();
      
      try {
         String query = SQL_retrieve[2];  //CHANGE THIS!

         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {
//                     JOptionPane.showMessageDialog(this, 
//                        "ResultSet contained no records" );
//                     setTitle( "No records to display" );
             
              System.out.println("NO RECORDS FOUND");
            return null;
          }
          Vector columnHeads = new Vector();
       
             //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();

             for ( int i = 1; i <= rsmd.getColumnCount(); ++i ) 
                columnHeads.addElement( rsmd.getColumnName( i ) );

             // get row data
             do {
              Vector currentRow = new Vector();
              for ( int i = 1; i <= rsmd.getColumnCount(); ++i )
              { 
                 switch( rsmd.getColumnType( i ) ) 
                 {
                    case Types.BIGINT:
                       // System.out.println("BIGINT");
                          currentRow.addElement( resultSet.getInt(i));
                       break;
                    case Types.NUMERIC:
                        //System.out.println("NUMERIC");
                          currentRow.addElement( resultSet.getDouble(i));
                       break;
                    case Types.INTEGER:
                        //System.out.println("INTEGER");
                          currentRow.addElement( resultSet.getInt(i));
                       break;
                    case Types.VARCHAR:
                       //System.out.println("VARCHAR");
                          currentRow.addElement( resultSet.getString(i));
                       break;
                    case Types.DOUBLE:
                        //System.out.println("DOUBLE");
                          currentRow.addElement(  resultSet.getDouble(i));
                        break;
                    default: 
                       System.out.println("Type was" +
                          rsmd.getColumnTypeName( i ) );
                 }
              }

              int upc = (Integer)currentRow.get(0);
              String desc = (String)currentRow.get(1);
              double price = (Double)currentRow.get(2);
              double stock = (Double)currentRow.get(3);
              
              InventoryData inven= new InventoryData(upc, desc, price,stock);
              tempList.add(inven);
              
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
   return tempList;
   }
   
   //set Unit Price
   //returns 1 if success, 2 returns unsuccessful
   public int setNewUnitPrice(int upc, double oldPrice, double newPrice)
   {
       int result = -7;
           
       String query="update prodcatalog set unitprice=" + newPrice + " where unitprice=" + oldPrice + " and upc=" + upc;
        
        try {
        this.statement = connection.createStatement();
                 
        result = statement.executeUpdate(query);
        System.out.println("setUnitPrice:" + result);
         
        statement.close();

        }
        catch ( SQLException sqlex ) {
                 sqlex.printStackTrace();
        } 
   return result;   
   }
   
   //sets the item description
   //returns 1 if success, 2 returns unsuccessful
   public int setItemDescription(int upc, String item)
   {
       int result = -5;  
       String query="update prodcatalog set description='" + item + "' where upc=" + upc;
        
        try {
        this.statement = connection.createStatement();          
        result = statement.executeUpdate(query);
      
        statement.close();
        }
        catch ( SQLException sqlex ) {
                 sqlex.printStackTrace();
        }
   return result;   
   }
   
   //save delivery items
   //returns 1 if success, 2 returns unsuccessful
   public int saveDeliveryItems(ArrayList upc, ArrayList stocks, ArrayList product, String name, String invoice)
   {
       int result = 0;
       //sample query
       //insert into delivery (date, name, upc, stock) values (current_date, 'bading', '{1,2}', '{40,30}')
       String query = "insert into delivery (date, name, invoice, upc, stock, product) values (current_date, '" + name + "', '" + invoice + "', '{";
       
       query +=upc.get(0);
       
       for(int i=1; i<upc.size(); i++)
       {
           query += "," + upc.get(i);
       }
       query +="}', '{" + stocks.get(0);
      
       for(int i=1; i<upc.size(); i++)
       {
           query += "," + stocks.get(i);
       }
       
       query +="}', '{" + product.get(0);
      
       for(int i=1; i<upc.size(); i++)
       {
           query += "," + product.get(i);
       }
       query +="}')";
       
       try {
        this.statement = connection.createStatement();
                
        result =statement.executeUpdate(query);
    
        statement.close();

        }
        catch ( SQLException sqlex ) {
                 sqlex.printStackTrace();
                 return -78;
        } 
   return result;   
   }
   
   
   //update currBalance table
   public int updateCurBalance(int upc, double amount)
   {
       //sample query
       //UPDATE weather SET temp_lo = temp_lo+1, temp_hi = temp_lo+15, prcp = DEFAULT
       //WHERE city = 'San Francisco' AND date = '2003-07-03';
       
       String query = "update currbalance SET balance=balance+" + String.valueOf(amount) + " WHERE upc =" + upc +";";
       int result=0;
       try {
        this.statement = connection.createStatement();               
        result =statement.executeUpdate(query); 
        statement.close();
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace();
            return -78;
        } 
       
   return result;
   }
   
   //generate end of the day
   //return 0 is unsucessful
   //will return 0 if dailysales has not been inputed
   public int generateEOD(String date, double amtTendered)
   {  
       int result=0;
       
       //statement.executeUpdate("insert into currBalance (date, Diesel, Unleaded, Premium, Regular) values (current_date, '2000', '3000', '4000', '5000')");
       String query = "update dailysales SET amttendered=" + amtTendered + ",status=1 where date='" + date +"';";
       
        try {
        this.statement = connection.createStatement();               
        result =statement.executeUpdate(query); 
        statement.close();
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace();
            return -78;
        } 
      
   return result;     
   }
   
   //use in EOD
   //returns true if EOD is already generated
   //1 means EOD has been generated
   //0 means EOD has not been generated 
   //-5 no records
   public int checkIfEOD(String date)
   {
       String query="select status from dailysales where date='" + date + "';";
       int result = -1;
      Statement statement;
      ResultSet resultSet;

      try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
              System.out.println("NO RECORDS FOUND in checkIfEOD");
             return -5;
          }
     
             // get row data
             do {
               result = resultSet.getInt("status");
              
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
   if(result==1)
       return 1;
   else
       return 0;
     
   }
   
   //get totalSales
   //use in end of day
   public double getTotalSales(String date)
   {
      double result = 0;
      String query="select totalsales from dailysales where date='" + date +"';";
        
      Statement statement;
      ResultSet resultSet;
     
      
      try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
              System.out.println("NO RECORDS FOUND in getTotalSales!!!");
             return -78;
          }
     
             // get row data
             do {
               result = resultSet.getDouble("totalsales");
              
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
   return result;
   }
   
   public double getAmtTenderedDailySales(String date)
   {
       String query = "select amttendered from dailysales where date='" + date + "';";
       double result = 0;
      Statement statement;
      ResultSet resultSet;
       
        try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
              System.out.println("NO RECORDS FOUND in getAmtTenderedDailySales");
             return -78;
          }
     
             // get row data
             do {
               result = resultSet.getDouble("amttendered");
              
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
   return result;
   }
   
   //generate Report
   public ArrayList generateReport(String date)
   {
      String query = "select upc, liters, unitprice from dailysales where date='" +date + "';";
      Statement statement;
      ResultSet resultSet;
      ArrayList<Array> tempList =new ArrayList<Array>();
      Array first, second, third;
      
      try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
              System.out.println("NO RECORDS FOUND in generateReport");
             //return -78;
          }
           //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();
           
      
             // get row data
             do {
              for ( int i = 1; i <= rsmd.getColumnCount(); i++ )
              { 
                 
                 switch( rsmd.getColumnType( i ) ) 
                 {                   
                    case Types.ARRAY:
                      
                          tempList.add(resultSet.getArray(i));
                        break;
                    default: 
                       System.out.println("Type was" +
                          rsmd.getColumnTypeName( i ) );
                 }
                 
              }
              
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
   return tempList;
   }
   
   public int checkUserPass(String user, String pass)
   {
       int result=0;
       String resultPass = null;
       int status=-1;
       String query = "select password, status from admin where username='" + user + "';";
        Statement statement;
      ResultSet resultSet;
     
      try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
              System.out.println("NO RECORDS FOUND in checkUserPass");
             return -5;
          }
     
             ResultSetMetaData rsmd = resultSet.getMetaData();
             // get row data
             do {
               for ( int i = 1; i <= rsmd.getColumnCount(); ++i )
              { 
                 switch( rsmd.getColumnType( i ) ) 
                 {
                    case Types.VARCHAR:
                          resultPass = resultSet.getString("password");
                       break;                 
                    case Types.INTEGER:
                          status = resultSet.getInt("status");
                       break;                  
                    default: 
                       System.out.println("Type was" +
                          rsmd.getColumnTypeName( i ) );
                 }
              }
                
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
      
      if(pass.equals(resultPass))
          result=status;
      else
          result=0;
      
   return result;
   }
   
   
   //returns 0 if unsuccessful
   public int updatePumpBeginningEnding(String date, ArrayList<String> product, ArrayList<String> beginning,ArrayList<String> ending, ArrayList<String> uPrice, ArrayList<String> calib)
   {
       int result=0;
       String query = "update pumpreading SET beginning='{";
       
       query +=beginning.get(0);
       
       for(int i=1; i<beginning.size(); i++)
           query +="," + beginning.get(i);
       
       query +="}', product= '{";
       
       query +=product.get(0) + "";
       
       for(int i=1; i<product.size(); i++)
           query +="," + product.get(i) ;
       
       query +="}', ending= '{";
       
       query +=ending.get(0);
       
       for(int i=1; i<ending.size(); i++)
           query +="," + ending.get(i);
       
       query +="}', unitprice='{";
       
       query +=uPrice.get(0);
       
       for(int i=1; i<uPrice.size(); i++)
           query += "," + uPrice.get(i); 
       
       query +="}', calib='{";
       
       query +=calib.get(0);
       
        for(int i=1; i<calib.size(); i++)
           query += "," + calib.get(i); 
       
       query +="}' where date='" + date + "';";
      
       try {
        this.statement = connection.createStatement();               
        result =statement.executeUpdate(query);
        statement.close();
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace();
            return -78;
        }
       
       //0 means, no row has been inserted;
       if(result==0)
       {
           query = "insert into pumpreading (date, product, beginning, ending, unitprice, calib, status) values ('";
           query += date + "', '{" + product.get(0);
           
           for(int i=1; i<product.size(); i++)
                query +="," + product.get(i);
           
           query +="}', '{" + beginning.get(0);
           
           for(int i=1; i<beginning.size(); i++)
               query +="," + beginning.get(i);
           
           query+="}', '{" + ending.get(0);
           
           for(int i=1; i<ending.size(); i++)
               query +="," + ending.get(i);
           
           query +="}', '{" + uPrice.get(0);
           
           for(int i=1; i<uPrice.size(); i++)
               query += "," + uPrice.get(i);
           
           query +="}', '{" + calib.get(0);
           
             for(int i=1; i<calib.size(); i++)
               query += "," + calib.get(i);
           
           query +="}', 0);";
           
           try {
           this.statement = connection.createStatement();               
           result =statement.executeUpdate(query);
           System.out.println("Executed query");
           statement.close();
           }
           catch ( SQLException sqlex ) {
            sqlex.printStackTrace();
            return -78;
           }   
         
       } 
   //insert the products for next day
    this.insertProductPumpReading(product, date);
   return result;    
   }
  
   
   //use for udpating product for next day in pump reading
   public int insertProductPumpReading(ArrayList<String> product, String date)
   {   int result=0;
       String query = "insert into pumpreading (date, product,status) values (date '";
           query += date + "' + interval '1 day', '{" + product.get(0);
       String query2 = "insert into pumpreading (date, product,status) values (date '";
           query2 += date + "' + interval '2 day', '{" + product.get(0);
           
           for(int i=1; i<product.size(); i++)
                query +="," + product.get(i);
           
           query +="}', 0);";
           
           for(int i=1; i<product.size(); i++)
                query2 +="," + product.get(i);
           
           query2 +="}', 0);";
           
           try {
           this.statement = connection.createStatement();               
           result =statement.executeUpdate(query);
    
           statement.close();
           }
           catch ( SQLException sqlex ) {
           // sqlex.printStackTrace();
         
           }  
             try {
           this.statement = connection.createStatement();               
  
           result =statement.executeUpdate(query2);
           statement.close();
           }
           catch ( SQLException sqlex ) {
           // sqlex.printStackTrace();
          
           }  
     return result;
   }
   
   
   public ArrayList getPumpReading(String date)
   {
       String query = "select product, beginning, ending, unitprice, calib from pumpreading where date='" + date + "';";
       ArrayList<Array> tempList =new ArrayList<Array>();
       
      Statement statement;
      ResultSet resultSet;
       try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
              System.out.println("NO RECORDS FOUND in pump reading executeQuery");
              return tempList=null;
            
          }
           //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();
           
      
             // get row data
             do {
              for ( int i = 1; i <= rsmd.getColumnCount(); i++ )
              { 
                 
                 switch( rsmd.getColumnType( i ) ) 
                 {                   
                    case Types.ARRAY:
                          tempList.add(resultSet.getArray(i));
                        break;
                    default: 
                       System.out.println("Type was" +
                          rsmd.getColumnTypeName( i ) );
                 }
                 
              }
              
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
       
       
   return tempList;
   }
   
   //use for getting reading pump, use in report, includes liters and total
   public ArrayList getPumpReadingComplete(String date)
   {
       String query = "select product, beginning, ending, unitprice, liters, calib, total from pumpreading where date='" + date + "';";
       ArrayList<Array> tempList =new ArrayList<Array>();
       
      Statement statement;
      ResultSet resultSet;
       try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
              System.out.println("NO RECORDS FOUND in pump reading executeQuery");
              return tempList=null;
            
          }
           //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();
         
             // get row data
             do {
              for ( int i = 1; i <= rsmd.getColumnCount(); i++ )
              { 
                 
                 switch( rsmd.getColumnType( i ) ) 
                 {                   
                    case Types.ARRAY:
                          tempList.add(resultSet.getArray(i));
                        break;
                    default: 
                       System.out.println("Type was" +
                          rsmd.getColumnTypeName( i ) );
                 }
                 
              }
              
             } while ( resultSet.next() );
             
        if(tempList.size()==1)
        {
            tempList=null;
            return tempList;
        }
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
       
       
   return tempList;
   }
   
   //returns true if you can still edit pumpreading
   //returns false if you can't edit pumpreading
   public boolean checkPumpReadingStatus(String date)
   {
      boolean status = false;
      int result=0;
      String query="select status from pumpreading where date='" + date +"';";
        
      Statement statement;
      ResultSet resultSet;
     
      
      try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
              System.out.println("NO RECORDS FOUND in umpReading, getting status");
             return true; 
          }
     
             // get row data
             do {
               result = resultSet.getInt("status");
              
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
   
      if(result==1)
          return false;
      else
          return true;
   }
   
   public int updatePumpReadingLitersTotal(String date, ArrayList<String> liters, ArrayList<String> total, int status)
   {
       int result=0;
       String query = "update pumpreading SET liters='{";
       
       query +=liters.get(0);
       
       for(int i=1; i<liters.size(); i++)
           query +="," + liters.get(i);
       
       query +="}', total= '{";
       
       query +=total.get(0) + "";
       
       for(int i=1; i<total.size(); i++)
           query +="," + total.get(i) ;
       
       if(status==0)
         query+="}', status=0 where date='" + date + "';";
       else
        query +="}', status=1 where date='" + date + "';";
      
   
       try {
        this.statement = connection.createStatement();               
        result =statement.executeUpdate(query);
        statement.close();
       }
       catch ( SQLException sqlex ) {
            sqlex.printStackTrace();
            return -78;
       }
       
   return result;    
   }
   
   public ArrayList<Array> getTotalPumpReading(String date)
   {
       
       String query = "select total from pumpreading where date='" + date + "';";
       ArrayList<Array> tempList =new ArrayList<Array>();
       
      Statement statement;
      ResultSet resultSet;
       try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
              System.out.println("NO RECORDS FOUND in pump reading executeQuery");
              return tempList=null;
            
          }
           //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();
           
      
             // get row data
             do {
               tempList.add(resultSet.getArray("total"));
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
       
       
   return tempList; 
   }
   
   public ArrayList<SalesData> getSalesTransaction(String date)
   {
       String query= "select date, time, sid, items, liters, unitprice, total,amttendered, change from sales where date='" + date +"';";
 
       ArrayList<SalesData> dataList = new ArrayList<SalesData>();
       
      Statement statement;
      ResultSet resultSet;
       try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
              System.out.println("NO RECORDS FOUND in getSalesTransaction");
             return null;
            
          }
           //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();
         
             // get row data
             do {
                 SalesData data= new SalesData();
              for ( int i = 1; i <= rsmd.getColumnCount(); i++ )
              {                
                 switch( rsmd.getColumnType( i ) ) 
                 {                   
                    case Types.ARRAY:
                          data.arrayData.add(resultSet.getArray(i));
                        break;
                    case Types.DATE:
                          data.date=resultSet.getDate("date");
                        break;
                    case Types.VARCHAR:
                          data.time=resultSet.getString("time");
                    case Types.DOUBLE:
                          data.total=resultSet.getDouble("total");
                          data.amttendered=resultSet.getDouble("amttendered");
                          data.change=resultSet.getDouble("change");
                        break;
                    case Types.BIGINT:
                          data.transID = resultSet.getInt("sid");
                          break;
                    default: 
                       System.out.println("Type was" +
                          rsmd.getColumnTypeName( i ) );
                 }
                 
              }
              dataList.add(data);
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
       
       
   return dataList;
   }
   
   public ArrayList<DeliveryData> getDelivery(String invoice)
   {
       String query= "select date, name, invoice, product, stock from delivery where invoice='" + invoice +"';";
 
       ArrayList<DeliveryData> dataList = new ArrayList<DeliveryData>();
       
      Statement statement;
      ResultSet resultSet;
       try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
              System.out.println("NO RECORDS FOUND in getDeliveryTrans");
             return null;
            
          }
           //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();
         
             // get row data
             do {
                 DeliveryData data= new DeliveryData();
              for ( int i = 1; i <= rsmd.getColumnCount(); i++ )
              {                
                 switch( rsmd.getColumnType( i ) ) 
                 {                   
                    case Types.ARRAY:
                          data.arrayData.add(resultSet.getArray(i));
                        break;
                    case Types.DATE:
                          data.date=resultSet.getDate("date");
                        break;
                    case Types.VARCHAR:
                          data.name=resultSet.getString("name");
                          data.invoice=resultSet.getString("invoice");
                        break;
                    default: 
                       System.out.println("Type was" +
                          rsmd.getColumnTypeName( i ) );
                 }
                 
              }
              dataList.add(data);
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
       
       
   return dataList;
   }
   
   //used in collections view
   public ArrayList<creditData> getcreditSales(String name, int status, String year)
   {
       String query="";
       if(status==0)
           query= "select date, name, invoice, items, liters, unitprice, amtcredit, status from creditsales where name='" + name +"' and status=" + status +";";
     
       ArrayList<creditData> dataList = new ArrayList<creditData>();
       
      Statement statement;
      ResultSet resultSet;
       try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
             //System.out.println("NO RECORDS FOUND in getcreditSales");
             return null;
            
          }
           //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();
             // get row data
             do {
                 creditData data= new creditData();
              for ( int i = 1; i <= rsmd.getColumnCount(); i++ )
              {                
                 switch( rsmd.getColumnType( i ) ) 
                 {                   
                    case Types.ARRAY:
                          data.arrayData.add(resultSet.getArray(i));
                        break;
                    case Types.DATE:
                          data.date=resultSet.getDate("date");
                          if(status==1)
                            data.datepaid=resultSet.getDate("datepaid");
                        break;
                    case Types.VARCHAR:
                          data.name=resultSet.getString("name");
                          data.invoice=resultSet.getString("invoice");
                          if(status==1)
                              data.info = resultSet.getString("checknumber");
                        break;
                    case Types.DOUBLE:
                          data.amtcredit=resultSet.getDouble("amtcredit");
                        break;
                    case Types.INTEGER:
                          data.status=resultSet.getInt("status");
                        break;
                    default: 
                       System.out.println("Type was" +
                          rsmd.getColumnTypeName( i ) );
                 }
                 
              }
              dataList.add(data);
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
       
   return dataList;
   }
   //use in payment view
   public ArrayList<creditData> getPaymentSales(String name, String year)
   {
       String query= "select date, datepaid, name, invoice, items, liters, unitprice, amtpayment, checknumber from paymentsales where name='" + name +"' and datepaid <= current_date and datepaid > '" + year + "-01-01';";
     
       ArrayList<creditData> dataList = new ArrayList<creditData>();
       
      Statement statement;
      ResultSet resultSet;
       try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
             //System.out.println("NO RECORDS FOUND in getcreditSales");
             return null;
            
          }
           //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();
             // get row data
             do {
                 creditData data= new creditData();
              for ( int i = 1; i <= rsmd.getColumnCount(); i++ )
              {                
                 switch( rsmd.getColumnType( i ) ) 
                 {                   
                    case Types.ARRAY:
                          data.arrayData.add(resultSet.getArray(i));
                        break;
                    case Types.DATE:
                          data.date=resultSet.getDate("date");
                            data.datepaid=resultSet.getDate("datepaid");
                        break;
                    case Types.VARCHAR:
                          data.name=resultSet.getString("name");
                          data.invoice=resultSet.getString("invoice");
                              data.info = resultSet.getString("checknumber");
                        break;
                    case Types.DOUBLE:
                          data.amtcredit=resultSet.getDouble("amtpayment");
                        break;
                
                    default: 
                       System.out.println("Type was" +
                          rsmd.getColumnTypeName( i ) );
                 }
                 
              }
              dataList.add(data);
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
       
   return dataList;
   }
  
   //use for payments
   public ArrayList<creditData> getspecificCreditSale(String name, String invoice)
   {
       String query="select date, name, invoice, items, liters, unitprice, upc, amtcredit, status from creditsales where name='" + name +"' and status=0 and invoice='" + invoice + "';";
    
       ArrayList<creditData> dataList = new ArrayList<creditData>();
      Statement statement;
      ResultSet resultSet;
       try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );       
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
             //System.out.println("NO RECORDS FOUND in getcreditSales");
             return null;
          }
           //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();
             // get row data
             do {
                 creditData data= new creditData();
              for ( int i = 1; i <= rsmd.getColumnCount(); i++ )
              {                
                 switch( rsmd.getColumnType( i ) ) 
                 {                   
                    case Types.ARRAY:
                          data.arrayData.add(resultSet.getArray(i));
                        break;
                    case Types.DATE:
                          data.date=resultSet.getDate("date");
                        break;
                    case Types.VARCHAR:
                          data.name=resultSet.getString("name");
                          data.invoice=resultSet.getString("invoice");
                        break;
                    case Types.DOUBLE:
                          data.amtcredit=resultSet.getDouble("amtcredit");
                        break;
                    case Types.INTEGER:
                          data.status=resultSet.getInt("status");
                        break;
                    default: 
                       System.out.println("Type was" +
                          rsmd.getColumnTypeName( i ) );
                 }          
              }
              dataList.add(data);
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
       
   return dataList;
   }
   
   //use in collection payment v2
   public int savePaymentSales(String name, String invoice, String checkNumber)
   {
       
       int result=0;
       ArrayList<creditData> data = this.getspecificCreditSale(name, invoice);
       
       String query="insert into paymentsales (datepaid, date, name, invoice, items, liters, unitprice, upc,amtpayment, checknumber, generated) values"
               + "(current_date, '" + data.get(0).date  + "', '" +data.get(0).name + "', '" + data.get(0).invoice + "', '{";
        try {
            String [] items = (String[])data.get(0).arrayData.get(0).getArray();
            Double [] liters = (Double[])data.get(0).arrayData.get(1).getArray();
            Double [] uprice = (Double[])data.get(0).arrayData.get(2).getArray();
            Integer [] upc = (Integer[])data.get(0).arrayData.get(3).getArray();
            
            query += items[0];
            for(int i=1; i<items.length; i++)
                query += ", " + items[i]; 
            
            query += "}', '{";
            
            query += liters[0];
            for(int i=1; i<liters.length; i++)
                query += ", " + liters[i];
            
            query += "}', '{";
            query += uprice[0];
            for(int i=1; i<uprice.length; i++)
                query += ", " + uprice[i];
            
            query += "}', '{";
            query += upc[0];
            for(int i=1; i<upc.length; i++)
                query += ", " + upc[i];
            
            query +="}', " + data.get(0).amtcredit + ", '" + checkNumber + "', 0);";
            
            
        } catch (SQLException ex) {
            Logger.getLogger(executeQuery.class.getName()).log(Level.SEVERE, null, ex);
        } 
      
      try {
        this.statement = connection.createStatement();               
        result =statement.executeUpdate(query);
        statement.close();
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace();
            return -78;
      }   
   
      //changing status of creditsales to 1 or paid
       String query2 ="update creditsales SET status=1 where name='" + name +"' and invoice='" + invoice +"'";
        try {
        this.statement = connection.createStatement();               
        statement.executeUpdate(query2);
        statement.close();
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace();
            return -78;
        }
 
   return result;    
   }
   
    public int updateEODcreditSales()
   {
     int result=0;
     ArrayList<Integer> dataList = new ArrayList<Integer>();
     String query ="select id from creditsales where generated=0";
     Statement statement;
     ResultSet resultSet;
      
      try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {        
              System.out.println("NO RECORDS FOUND in updateEODcreditSales");
              return -78;
          }
         do {
          dataList.add(resultSet.getInt("id"));

         } while ( resultSet.next() );

      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
      
      for(Integer i : dataList)
      {
          String query2="update creditsales SET generated=1, dategenerated=current_date where id=" + i ;
           try {
             statement = connection.createStatement();               
             result =statement.executeUpdate(query2); 
             statement.close();
            }
            catch ( SQLException sqlex ) {
            sqlex.printStackTrace(); 
            return -78;
            }
        if(result==0)
            return 0;
      }
   return 1;
   }
    
    public int updateEODpaymentSales()
   {
     int result=0;
     ArrayList<Integer> dataList = new ArrayList<Integer>();
     String query ="select id from paymentsales where generated=0";
     Statement statement;
     ResultSet resultSet;
      
      try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {        
              System.out.println("NO RECORDS FOUND in updateEODpaymentSales");
              return -78;
          }
         do {
          dataList.add(resultSet.getInt("id"));

         } while ( resultSet.next() );

      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
      for(Integer i : dataList)
      {
          String query2="update paymentsales SET generated=1, dategenerated=current_date where id=" + i ;
           try {
             statement = connection.createStatement();               
             result =statement.executeUpdate(query2); 
             statement.close();
            }
            catch ( SQLException sqlex ) {
            sqlex.printStackTrace(); 
            return -78;
            }
        if(result==0)
            return 0;
      }
   return 1;
   }
    
    //returns the amt credit, @param 0 means retrieve unpaid, 1 means retrieve paid
    public double getTotalCreditForReport(String date, int status)
    {
       String query ="select sum(amtcredit) from creditsales where dategenerated='" + date +"';";
       Statement statement;
       ResultSet resultSet;
       double amtcredit=0;
        try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {        
              System.out.println("NO RECORDS FOUND in getTotalCreditForReport");
              return 0;
          }
         do {
          amtcredit=resultSet.getDouble("sum");
         } while ( resultSet.next() );

      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
    return amtcredit;
    }
    
    //returns the amt credit, @param 0 means retrieve unpaid, 1 means retrieve paid
    public double getTotalPaymentForReport(String date)
    {
       String query ="select sum(amtpayment) from paymentsales where dategenerated='" + date +"';";
       Statement statement;
       ResultSet resultSet;
       double amtcredit=0;
        try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {        
              System.out.println("NO RECORDS FOUND in getTotalCreditForReport");
              return 0;
          }
         do {
          amtcredit=resultSet.getDouble("sum");
         } while ( resultSet.next() );

      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
    return amtcredit;
    }
    
    //used in generate Report to retrieve all collections and payments
   public ArrayList<creditData> getCreditReport(String date, int status)
   {
       //select date, name, items,liters,unitprice, amtcredit,status,datepaid from creditsales where dategenerated=current_date
       String query="";
       if(status==0)
           query= "select date, invoice, name, items, liters, unitprice, amtcredit, status from creditsales where dategenerated='"+ date +"';";
       else if(status==1)
            query= "select date, invoice, name, items, liters, unitprice, amtcredit, status, datepaid from creditsales where dategenerated='" + date +"' and status=" + status + ";";

       ArrayList<creditData> dataList = new ArrayList<creditData>();
       
      Statement statement;
      ResultSet resultSet;
       try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
              System.out.println("NO RECORDS FOUND in getcreditSales");
             return null;
            
          }
           //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();
             // get row data
             do {
                 creditData data= new creditData();
              for ( int i = 1; i <= rsmd.getColumnCount(); i++ )
              {                
                 switch( rsmd.getColumnType( i ) ) 
                 {                   
                    case Types.ARRAY:
                          data.arrayData.add(resultSet.getArray(i));
                        break;
                    case Types.DATE:
                          data.date=resultSet.getDate("date");
                        break;
                    case Types.VARCHAR:
                          data.name=resultSet.getString("name");
                          data.invoice=resultSet.getString("invoice");
                         
                        break;
                    case Types.DOUBLE:
                          data.amtcredit=resultSet.getDouble("amtcredit");
                        break;
                    case Types.INTEGER:
                          data.status=resultSet.getInt("status");
                         
                        break;
                    default: 
                       System.out.println("Type was" +
                          rsmd.getColumnTypeName( i ) );
                 }
                 
              }
              dataList.add(data);
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
       
   return dataList;
   }
   
    //used in generate Report to retrieve all collections and payments
   public ArrayList<creditData> getPaymentReport(String date)
   {
       //select date, name, items,liters,unitprice, amtcredit,status,datepaid from creditsales where dategenerated=current_date
       String query="";
           query= "select date, datepaid, invoice, name, items, liters, unitprice, amtpayment from paymentsales where dategenerated='"+ date +"';";
      
       ArrayList<creditData> dataList = new ArrayList<creditData>();
      Statement statement;
      ResultSet resultSet;
       try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
              System.out.println("NO RECORDS FOUND in getcreditSales");
             return null;
            
          }
           //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();
             // get row data
             do {
                 creditData data= new creditData();
              for ( int i = 1; i <= rsmd.getColumnCount(); i++ )
              {                
                 switch( rsmd.getColumnType( i ) ) 
                 {                   
                    case Types.ARRAY:
                          data.arrayData.add(resultSet.getArray(i));
                        break;
                    case Types.DATE:
                          data.date=resultSet.getDate("date");
                            data.datepaid=resultSet.getDate("datepaid");
                        break;
                    case Types.VARCHAR:
                          data.name=resultSet.getString("name");
                          data.invoice=resultSet.getString("invoice");
                         
                        break;
                    case Types.DOUBLE:
                          data.amtcredit=resultSet.getDouble("amtpayment");
                        break;
                    default: 
                       System.out.println("Type was" +
                          rsmd.getColumnTypeName( i ) );
                 }
                 
              }
              dataList.add(data);
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
       
   return dataList;
   }
   
   //use for voiding sales transaction
   public int deleteSaleTrans(int transID)
   {
       String query = "insert into voidsales select * from sales where sid=" + transID; 
       int result=0, result2=0;
        try {
        this.statement = connection.createStatement();         
        result = statement.executeUpdate(query);
        statement.close();
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace(); 
             return -5;
        } 
        
         query = "update voidsales SET generated=0 where sid='" + transID + "'";
           try {
          this.statement = connection.createStatement();         
          result2 = statement.executeUpdate(query);
          statement.close();
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace(); 
             return -5;
        } 
       
       query = "delete from sales where sid=" + transID;
       
        try {
        this.statement = connection.createStatement();         
        result = statement.executeUpdate(query);
        statement.close();
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace(); 
             return -5;
        } 
   return result;  
   }
   
   public int decreaseDailySales(int transID)
   {
       String query="select * from sales where sid=" + transID;
      Statement statement;
      ResultSet resultSet;
      Transaction trans = null;
      ArrayList<Array> tempData = new ArrayList<Array>();
      double totalAmt=0;
      ArrayList<InventoryData> tempList = new ArrayList<InventoryData>();
      
      try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {
              System.out.println("NO RECORDS FOUND in decreaseDailySales");
            return -5;
          }         
             //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();

             // get row data
             do {           
              for ( int i = 1; i <= rsmd.getColumnCount(); ++i )
              { 
                 switch( rsmd.getColumnType( i ) ) 
                 {
                    case Types.ARRAY:
                          tempData.add( resultSet.getArray("upc"));
                          tempData.add((resultSet.getArray("liters")));
                          tempData.add(resultSet.getArray("items"));
                          tempData.add(resultSet.getArray("unitprice"));
                          tempData.add(resultSet.getArray("upc"));
                   
                       break;
                    case Types.BIGINT:
                          int id = resultSet.getInt("sid");
                          trans = new Transaction(id);
                        break;
                   
                    case Types.DOUBLE:
                          trans.setTotal(resultSet.getDouble("total"));
                        break;
                    case Types.DATE:
                           trans.setDate(resultSet.getDate("date"));
                    default: 
                      // System.out.println("Type was" +
                      //    rsmd.getColumnTypeName( i ) );
                 }
              }
      
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
     
      int result = 0;
      int result2= 0;
      try {
         //getting upc from array
         Integer[] tempUPC = (Integer[])tempData.get(0).getArray();
         BigDecimal [] tempLiters = (BigDecimal[]) tempData.get(1).getArray();
         Double [] doubleLiters = new Double[tempLiters.length];
         
         for(int i=0; i<tempLiters.length;i++)
         {
             doubleLiters[i] = tempLiters[i].doubleValue();
         }
         
        result = this.decreaseLitersDailySales(tempUPC, doubleLiters, trans.getTotalAmount(), trans.getDate()); 
        result2= this.addCurrBalance(tempUPC, doubleLiters); 
      } catch (SQLException ex) {
            Logger.getLogger(executeQuery.class.getName()).log(Level.SEVERE, null, ex);
      }
      if(result==1 && result2==1)
            return result;
      else
          return -78;
   }
   
   public int decreaseLitersDailySales(Integer[] upcSales, Double[] litersSales, double total, String date)
   {
      String query = "select upc from dailysales where date='" + date + "';";
      Statement statement = null;
      ResultSet resultSet;
      Integer [] upcDailySales = null;
      
      try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {
              System.out.println("NO RECORDS FOUND in decreaseLitersDailySales");
            //return null;
          }         
             //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();

             // get row data
             do {
                 upcDailySales = (Integer[]) resultSet.getArray("upc").getArray();
             } while ( resultSet.next() );
  
     
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
      
      
      int index=-1;
      int result1 = 0, result2=0;
      for(int y=0; y<upcSales.length; y++)
      {
       for(int i=0; i<upcDailySales.length; i++)
       {
        if(upcSales[y].equals(upcDailySales[i]))
        {      index=i+1;
      
        query="update dailysales SET ";
       
        query += "liters[" + index + "]=" + "liters[" + index + "]-" + litersSales[y] + " where date='" + date + "';";
   
            try {
             this.statement = connection.createStatement();               
             result1 = statement.executeUpdate(query); 
             //statement.close();
            
            }
            catch ( SQLException sqlex ) {
            sqlex.printStackTrace();            
            } 
         }
       }
      }
        
      //decrease total amount
      query = "update dailysales SET totalsales=totalsales - " + total + " where date='" + date + "'";
       
     try {
       this.statement = connection.createStatement();               
       result2 = statement.executeUpdate(query); 
       statement.close();
     }
       catch ( SQLException sqlex ) {
       sqlex.printStackTrace();            
     }  
      
   if(result1 == 1 && result2==1)
       return 1;
   else
       return 0;
   }
   
   //used in void sale trans
   public int addCurrBalance(Integer[] upcSales, Double[] litersSales)
   {
       int result=0;
     
       for(int i=0; i<upcSales.length; i++)
       {
            //sample query
            //UPDATE weather SET temp_lo = temp_lo+1, temp_hi = temp_lo+15, prcp = DEFAULT
            //WHERE city = 'San Francisco' AND date = '2003-07-03';
            String query = "update currbalance SET balance=balance+" + String.valueOf(litersSales[i]) + " WHERE upc =" + upcSales[i] +";";
        
            try {
             this.statement = connection.createStatement();               
             result =statement.executeUpdate(query); 
             statement.close();
            }
            catch ( SQLException sqlex ) {
            sqlex.printStackTrace();
               return -78;
            } 
       }       
   return result;
   }
   
   public int voidcreditsales(String invoice)
   {
      String query="select upc, liters from creditsales where invoice='" + invoice + "'";
      Statement statement;
      ResultSet resultSet;
      ArrayList<Array> tempData = new ArrayList<Array>();
      double totalAmt=0;
      
      try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {
              System.out.println("NO RECORDS FOUND in decreaseDailySales");
            return -5;
          }         
             //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();
             // get row data
             do {           
              for ( int i = 1; i <= rsmd.getColumnCount(); ++i )
              { 
                 switch( rsmd.getColumnType( i ) ) 
                 {
                    case Types.ARRAY:
                          tempData.add( resultSet.getArray("upc"));
                          tempData.add((resultSet.getArray("liters")));
                    break;                
                 }
              }
      
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
      
      int result=0, result2=0;
        try {
            Integer[] upc = (Integer[]) tempData.get(0).getArray();
            Double[] liters = (Double[]) tempData.get(1).getArray();
            
            result = this.addCurrBalance(upc, liters);
            result2= this.deletecreditsaleTrans(invoice);
        } catch (SQLException ex) {
            Logger.getLogger(executeQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(result==1 & result2==1)
            return result;
        else 
            return -5;
   }
   
   //deletes the credit sale 
   //& insert into void credit sale and change generated=0
    public int deletecreditsaleTrans(String invoice)
   {
       String query ="insert into voidcreditsales select * from creditsales where invoice='" + invoice + "'";   
       int result=0, result2=0, result3=0;

        try {
        this.statement = connection.createStatement();         
        result = statement.executeUpdate(query);
        statement.close();
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace(); 
             return -5;
        } 
        
        query = "delete from creditsales where invoice='" + invoice + "'";
        
        try {
          this.statement = connection.createStatement();         
          result2 = statement.executeUpdate(query);
          statement.close();
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace(); 
             return -5;
        } 
        
        query = "update voidcreditsales SET generated=0 where invoice='" + invoice + "'";
           try {
          this.statement = connection.createStatement();         
          result3 = statement.executeUpdate(query);
          statement.close();
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace(); 
             return -5;
        } 
        if(result==1 && result2==1 && result3!=0)
              return result;
        else
            return -5;
   }
    
   public int voidpaymentsales(String invoice)
   {
       String query ="update creditsales SET status=0 where invoice='" + invoice + "'";   
       int result=0, result2=0, result3=0, result4=0;

        try {
        this.statement = connection.createStatement();         
        result = statement.executeUpdate(query);
        statement.close();
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace(); 
             return -5;
        } 
        
        query = "insert into voidpaymentsales select * from paymentsales where invoice='" + invoice + "';";
        try {
        this.statement = connection.createStatement();         
        result2 = statement.executeUpdate(query);
        statement.close();
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace(); 
             return -5;
        } 
        
         query = "update voidpaymentsales SET generated=0 where invoice='" + invoice + "'";
           try {
          this.statement = connection.createStatement();         
          result3 = statement.executeUpdate(query);
          statement.close();
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace(); 
             return -5;
        } 
       
        query = "delete from paymentsales where invoice='" + invoice + "';";
        try {
        this.statement = connection.createStatement();         
        result4 = statement.executeUpdate(query);
        statement.close();
        }
        catch ( SQLException sqlex ) {
            sqlex.printStackTrace(); 
             return -5;
        } 
        System.out.println(result);
        System.out.println(result2);
        System.out.println(result3);
        System.out.println(result4);
        
        if(result==1 && result2 ==1  && result4==1)
            return result;
        else
            return -5;
   }
   
   //use in EOD to update voidcreditsale, voidpaymentsales, voidsales
   //@param 0=voidcreditsale, 1=voidpaymentsale, 2=voidsale
   public int updateEODAllvoidSales(int mode)
   {
     int result=0;
     ArrayList<Integer> dataList = new ArrayList<Integer>();
     String query="";
     if(mode==0)
         query ="select id from voidcreditsales where generated=0";
     else if(mode==1)
         query ="select id from voidpaymentsales where generated=0";
     else if(mode==2)
         query="select sid from voidsales where generated=0";
     
     Statement statement;
     ResultSet resultSet;
      
      try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );  
          // position to first record
          boolean moreRecords = resultSet.next();  
          // If there are no records, display a message
          if ( ! moreRecords ) {        
              System.out.println("NO RECORDS FOUND in updateEODALLvoidSales");
              return -78;
          }
         do {
          if(mode==2)
            dataList.add(resultSet.getInt("sid"));
          else
              dataList.add(resultSet.getInt("id")); 

         } while ( resultSet.next() );

      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
      
      for(Integer i : dataList)
      {
          String query2="";
          if(mode==0)
             query2="update voidcreditsales SET generated=1, dategenerated=current_date where id=" + i ;
          else if(mode==1)
             query2="update voidpaymentsales SET generated=1, dategenerated=current_date where id=" + i ;
          else if(mode==2)
             query2="update voidsales SET generated=1, dategenerated=current_date where sid=" + i ;
  
          try {
             statement = connection.createStatement();               
             result =statement.executeUpdate(query2); 
             statement.close();
            }
            catch ( SQLException sqlex ) {
            sqlex.printStackTrace(); 
            return -78;
            }
        if(result==0)
            return 0;
      }
   return 1;
   }
   
    //used in generate Report to retrieve all collections and payments
   //@param mode 1=voidcredit, 2=voidpayment, 3=voidsale
   public ArrayList<creditData> getAllVoidReport(String date, int mode)
   {
       //select date, name, items,liters,unitprice, amtcredit,status,datepaid from creditsales where dategenerated=current_date
       String query="";
       if(mode==0)
           query="select date, time, name, invoice, items, liters, unitprice, amtcredit from voidcreditsales where dategenerated='" + date + "';";
       else if(mode==1)
           query="select date, time, name, invoice, items, liters, unitprice, amtpayment from voidpaymentsales where dategenerated='" + date + "';";
       else if(mode==2)
           query="select date, time, items,liters, unitprice, total, amttendered from voidsales where dategenerated='" + date + "';";
       
       ArrayList<creditData> dataList = new ArrayList<creditData>();
       
      Statement statement;
      ResultSet resultSet;
       try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
              System.out.println("NO RECORDS FOUND in getcreditSales");
             return null;
          }
           //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();
             // get row data
             do {
                 creditData data= new creditData();
              for ( int i = 1; i <= rsmd.getColumnCount(); i++ )
              {                
                 switch( rsmd.getColumnType( i ) ) 
                 {                   
                    case Types.ARRAY:   
                          data.arrayData.add(resultSet.getArray(i));
                        break;
                    case Types.DATE:
                          data.date=resultSet.getDate("date");
                        break;
                    case Types.VARCHAR:
                         if(mode==0){
                            data.time=resultSet.getString("time");
                            data.name=resultSet.getString("name");
                            data.invoice=resultSet.getString("invoice");
                         }
                        else if(mode==1){
                            data.time=resultSet.getString("time");
                            data.name=resultSet.getString("name");
                            data.invoice=resultSet.getString("invoice");
                        }
                        else if(mode==2){
                          data.time=resultSet.getString("time");
                        }
                         
                        break;
                    case Types.DOUBLE:
                         if(mode==0){
                             data.amtcredit = resultSet.getDouble("amtcredit");
                         }
                        else if(mode==1){
                            data.amtcredit = resultSet.getDouble("amtpayment");
                         }   
                        else if(mode==2){
                            data.total = resultSet.getDouble("total");
                            data.amtcredit = resultSet.getDouble("amttendered");
                        }
                        break;
                 
                    default: 
                       System.out.println("Type was" +
                          rsmd.getColumnTypeName( i ) );
                 }
              }
              dataList.add(data);
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
       
   return dataList;
   }
   
   public ArrayList<DeliveryData> getDeliveryReport(String date)
   {
       String query= "select date, name, invoice, product, stock from delivery where date='" + date +"';";
 
       ArrayList<DeliveryData> dataList = new ArrayList<DeliveryData>();
       
      Statement statement;
      ResultSet resultSet;
       try {
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {            
              System.out.println("NO RECORDS FOUND in getDeliveryTrans");
             return null; 
          }
           //get column heads
             ResultSetMetaData rsmd = resultSet.getMetaData();
             // get row data
             do {
                 DeliveryData data= new DeliveryData();
              for ( int i = 1; i <= rsmd.getColumnCount(); i++ )
              {                
                 switch( rsmd.getColumnType( i ) ) 
                 {                   
                    case Types.ARRAY:
                          data.arrayData.add(resultSet.getArray(i));
                        break;
                    case Types.DATE:
                          data.date=resultSet.getDate("date");
                        break;
                    case Types.VARCHAR:
                          data.name=resultSet.getString("name");
                          data.invoice=resultSet.getString("invoice");
                        break;
                    default: 
                       System.out.println("Type was" +
                          rsmd.getColumnTypeName( i ) );
                 }            
              }
              dataList.add(data);
             } while ( resultSet.next() );
  
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
     
   return dataList;
   }
  
}

