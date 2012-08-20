
package DB;

import GUI.InventoryData;
import Store.Item;
import Store.Product;
import Store.Transaction;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class executeQuery {
    
    private Connection connection;
    private Statement statement;
    
    //0 = get unit price, 
    private final String [] SQL_retrieve ={"SELECT * FROM ProdCatalog", "select max(sid) from sales"};
    
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
   
   public int insertTransaction(Transaction trans)
   {
      int result=0;
      String query="insert into sales (date, time, upc, items, liters, unitPrice, total, amttendered, change) values (current_date,";
       
         
      DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
      Date date = new Date();
      query +="'" + dateFormat.format(date) + "',";
      
      ArrayList<Item> tempList = trans.getList();
      
      String tempUPC="'{" + tempList.get(0).getUPC();
      String tempItems="'{" + tempList.get(0).getDescription();
      String tempLiters="'{" + tempList.get(0).getQuantity();
      String tempUnitPrice="'{" + tempList.get(0).getUnitPrice();
      
      
      for(int i=1; i<tempList.size(); i++)
      {
          int upc =tempList.get(i).getUPC();
          String desc =tempList.get(i).getDescription();
          double qty =tempList.get(i).getQuantity();
          double subtotal =tempList.get(i).getSubTotal();
          
          tempUPC+= "," + upc;
          tempItems+= ", " + tempList.get(i).getDescription();
          tempLiters+= " ," + qty;
          tempUnitPrice+= " ," + tempList.get(i).getUnitPrice();
          
      }
      
      tempUPC +="}'";
      tempItems +="}'";
      tempLiters +="}'";
      tempUnitPrice +="}'";   
      
      query+= tempUPC + "," + tempItems + "," + tempLiters + "," + tempUnitPrice + "," + trans.getTotalAmount() + "," +trans.getAmtTendered() +"," + trans.getChange() + ")";
   
      try {
        this.statement = connection.createStatement();
                
        result = statement.executeUpdate(query);
        
        statement.close();

        }
        catch ( SQLException sqlex ) {
                 sqlex.printStackTrace();
                 return -78;
        } 
   return result;
   }
   
   //update Daily Sales
   public int updateDailySales(Transaction trans)
   {
      Integer data[] = null;
      String query = "select upc from dailysales where date=current_date";
         
      Statement statement;
      ResultSet resultSet;
      
          try {
      
         statement = connection.createStatement();
         resultSet = statement.executeQuery( query );
         
          // position to first record
          boolean moreRecords = resultSet.next();  

          // If there are no records, display a message
          if ( ! moreRecords ) {
//                     JOptionPane.showMessageDialog(this, 
//                        "ResultSet contained no records" );
//                     setTitle( "No records to display" );
             
              System.out.println("NO RECORDS FOUND in updateDailySales");
            //return null;
          }
         
             // get row data
             do {
              Array array = resultSet.getArray("upc");
              if(array==null)
                  data = new Integer[0];
              else
                  data = (Integer[]) array.getArray();
             
             } while ( resultSet.next() );
            
      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
          
      //cotains the saved UPC in DB
      ArrayList<Integer> temp = new ArrayList<Integer>();
          
      for(Integer i : data)
      {
          temp.add(i);
      }
      
      ArrayList<Item> transList = trans.getList();
      int result=0;
      //traversing through the transaction
      for(Item i : transList)
      {
          //if upc already contains in db
          if(temp.contains(i.getUPC()))
          {
              int index = temp.indexOf(i.getUPC());
              result = this.addToCurrDailySales(i, index);
          }
          else{
             result = this.addUPCtoDailySales(i,temp.size()+1);
             temp.add(i.getUPC());
          }
              
      }
   return result;   
   }
   
   //upc is already added to daily sales
   //remem i did not update unitPrice
   public int addToCurrDailySales(Item i, int index)
   {
       //System.out.println("UPC existed");
       int result=0;
       String query="update dailysales SET ";
       int newIndex = index + 1;
       
       query += "liters[" + newIndex + "]=" + "liters[" + newIndex + "]+" + i.getQuantity() + ", totalSales=totalSales + " + i.getSubTotal()
               + " where date=current_date;";
   
        try {
             this.statement = connection.createStatement();               
             result =statement.executeUpdate(query); 
             statement.close();
            }
            catch ( SQLException sqlex ) {
            sqlex.printStackTrace();
             
      } 
   return result;
   }
   
   //remem to add unitprice
   public int addUPCtoDailySales(Item i, int size)
   {
       System.out.println("ADDE UPC::" + size);
       int result=0;
       String query="update dailysales SET upc[";
       query += size + "]=" + i.getUPC() + ", liters[" + size + "]=" + i.getQuantity() + ", unitprice[" + size + "]=" +
               i.getUnitPrice() + ", totalsales=totalsales +" + i.getSubTotal() + " where date=current_date;";
       
        try {
             this.statement = connection.createStatement();               
             result =statement.executeUpdate(query); 
             statement.close();
            }
            catch ( SQLException sqlex ) {
            sqlex.printStackTrace();     
      }

   return result;
   }
   
   //returns 0 if unsuccessful, otherwise 1
   public int checkIfDailySalesHasBeenInit()
   {
       int result=1;
       String query ="insert into dailysales (date, totalsales, status) values (current_date, 0, 0);";
       try {
             this.statement = connection.createStatement();               
             result =statement.executeUpdate(query); 
             statement.close();
            }
            catch ( SQLException sqlex ) {      
            //sqlex.printStackTrace();  
            return -78;
           } 
    return 0;           
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
              Vector currentRow = this.extractRow(resultSet);
              key = (Integer)currentRow.firstElement();
              
              
             } while ( resultSet.next() );

      statement.close();
      }
      catch ( SQLException sqlex ) {
         sqlex.printStackTrace();
      }
   return key;
   }
   
   
   public Vector extractRow(ResultSet resultSet)
   {
       ResultSetMetaData rsmd;
       Vector currentRow = new Vector();
        try {
            rsmd = resultSet.getMetaData();
            
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
        } catch (SQLException ex) {
            Logger.getLogger(executeQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    return currentRow;
   }
   
   //update currBalance table
   //will deduct the balance
   public int updateCurBalance(Transaction trans)
   {
       int result=0;
       ArrayList<Item> tempList = trans.getList();
       
       for(int i=0; i<tempList.size(); i++)
       {
           int upc=tempList.get(i).getUPC();
           double amount = tempList.get(i).getQuantity();
           
            //sample query
            //UPDATE weather SET temp_lo = temp_lo+1, temp_hi = temp_lo+15, prcp = DEFAULT
            //WHERE city = 'San Francisco' AND date = '2003-07-03';
            String query = "update currbalance SET balance=balance-" + String.valueOf(amount) + " WHERE upc =" + upc +";";
        
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
   
   //get Inventory data
   public ArrayList<InventoryData> retriveInventoryData()
   {
      Statement statement;
      ResultSet resultSet;
      ArrayList<InventoryData> tempList = new ArrayList<InventoryData>();
      
      try {
         String query =  "select currbalance.upc, prodcatalog.description, unitprice, balance from currbalance, prodcatalog where currbalance.upc = prodcatalog.upc";  //CHANGE THIS!

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
   
   public int insertCreditSales(Transaction trans, String name, String invoice)
   {
       int result=0;
      String query="insert into creditsales (date, time, name, invoice, upc, items, liters, unitPrice, "
              + "amtcredit, status, generated) values (current_date,";
       
      DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
      Date date = new Date();
      query +="'" + dateFormat.format(date) + "',";
      
      query +="'" + name + "', '" + invoice + "', ";  
      
      ArrayList<Item> tempList = trans.getList();
      
      String tempUPC="'{" + tempList.get(0).getUPC();
      String tempItems="'{" + tempList.get(0).getDescription();
      String tempLiters="'{" + tempList.get(0).getQuantity();
      String tempUnitPrice="'{" + tempList.get(0).getUnitPrice();
       
      for(int i=1; i<tempList.size(); i++)
      {
          int upc =tempList.get(i).getUPC();
          String desc =tempList.get(i).getDescription();
          double qty =tempList.get(i).getQuantity();
          double subtotal =tempList.get(i).getSubTotal();
          
          tempUPC+= "," + upc;
          tempItems+= ", " + tempList.get(i).getDescription();
          tempLiters+= " ," + qty;
          tempUnitPrice+= " ," + tempList.get(i).getUnitPrice();
          
      }
      
      tempUPC +="}'";
      tempItems +="}'";
      tempLiters +="}'";
      tempUnitPrice +="}'";   
      
      query+=  tempUPC + "," + tempItems + "," + tempLiters + "," + tempUnitPrice + "," + trans.getTotalAmount() + ", 0, 0)";
   
      try {
        this.statement = connection.createStatement();           
        result = statement.executeUpdate(query);
        statement.close();
        }
        catch ( SQLException sqlex ) {
                 sqlex.printStackTrace();
                 return -78;
        } 
   return result;
   }
  
}
