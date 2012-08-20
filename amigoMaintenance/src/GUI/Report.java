    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EndOfTheDayPanel.java
 *
 * Created on Jun 13, 2012, 2:38:20 AM
 */
package GUI;

import MStore.Product;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import mDB.DBConnection;
import mDB.DeliveryData;
import mDB.creditData;

/**
 *
 * @author Hanzen
 */
public class Report extends javax.swing.JPanel {

    public void initReport(DBConnection db)
    {
        this.db = db;
      this.datetextField.setEditable(false);
      this.datetextField.setText(this.getDate());
      
      this.initJTableList();
      this.setColJTable();
      
      //sets the JTable for pumpreading
      this.initJTableList2();
        
    }
    
    public void createFileReport(String filename)
    { 
      boolean success = (new File("amigoreport")).mkdirs();
       
        filename=filename.replace("/", "-");
        //File file = new File("C:\\Users\\OEM\\Desktop\\amigoreport\\" + filename + "-dailyReport.txt");
       File file = new File("amigoreport\\" + filename + "-dailyReport.txt");

        try {
        //if(!file.exists())
        {
            file.createNewFile();
            
            file.setWritable(true);
        FileWriter fw = new FileWriter(file);
        fw.write(this.report1);
        fw.write("\r\n\r\n");
        fw.write("Pump Reading Report\r\n");
        String temp= String.format("%-10s | %-10s | %-10s | %-10s | %-10s | %-10s", "  Product ", "Beginning  ", "  Ending  ", "  Liters  ", "Unit Price ", "  Total  ");
        fw.write(temp + "\r\n");
        fw.write(this.report2);

        file.setReadOnly();

        fw.close();
        }
        } catch (IOException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }
    
    //init JTable
    public void initJTableList()
    {
        
         String data[][] = null;
         String col[] = {"    ", "    ", "    ", "    "};
         model= new DefaultTableModel(data,col)
         {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
         
        this.jTable1.setModel(model);    
     
    }
    
    //init JTable
    public void initJTableList2()
    {
        
         String data[][] = null;
         String col[] = {"Product", "Beginning", "Ending","Calibration", "Liters","Unit Price","Total"};
         model2= new DefaultTableModel(data,col)
         {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
         
        this.jTable2.setModel(model2);    
     
    }
    //sets the column width for JTable
    public void setColJTable()
    {
        TableColumn column = null;
        for (int i = 0; i < 4; i++) {
            column = this.jTable1.getColumnModel().getColumn(i);
           if (i == 0) {
                column.setPreferredWidth(100); 
           }
           else if (i==1)
               column.setPreferredWidth(50);
           else if(i==2)
                column.setPreferredWidth(80);  
           else {
                column.setPreferredWidth(250);
            }
        }
    }
    
   public void getPumpReadingData(String date)
    {  
        ArrayList<Array> list= this.db.getPumpReadingComplete(date);
     
     String [] product = new String[0];
     String [] beginning = new String[0];
     String [] ending= new String[0];
     String [] uprice = new String[0];
     String [] liters = new String[0];
     String [] total = new String[0];
     String [] calib = new String[0];
     
     //System.out.println("SIZE::"+ list.size());
     
     if(list !=null)
     {      
         for(int i=0; i<7; i++)
         {
            try{
               if(i==0)
                  product=(String[]) list.get(0).getArray();
               if(i==1)
                  beginning=(String[]) list.get(1).getArray();
               if(i==2)
                  ending=(String[]) list.get(2).getArray();
               if(i==3)
                  uprice=(String[]) list.get(3).getArray();
               if(i==4)
                  liters=(String[]) list.get(4).getArray();
               if(i==5)
                   calib = (String[]) list.get(5).getArray();
               if(i==6)
                   total = (String[]) list.get(6).getArray();
               
            }catch(NullPointerException e){
                 System.out.println("NULLPOINTER in report.java");
                 return;
            }catch (SQLException ex) {
               Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
         }         
     }
     if(list == null){
         System.out.println("list is empty: PumpReading.java");
         return;
     }
   
     this.report2="";
     for(int i=0; i<product.length; i++)
     {
         String temptotal = String.valueOf((double) Math.round(Double.valueOf(total[i]) * 1000) /1000);
         this.model2.insertRow(this.model2.getRowCount(), new Object[]{product[i],beginning[i], ending[i], calib[i], liters[i], uprice[i], temptotal });
  //String tempString=String.format("%-20s  @   %-10s  X  %-7s  liters = P %-10s \r\n",pCatalog.get(upc[i]).getDesciption(), this.roundOff(uprice[i]),this.roundOff(liters[i]), temp);
        String tempString=String.format("%-10s |  %-10s | %-10s | %-10s | %-10s | %-10s | %-10s", product[i],beginning[i], ending[i], calib[i], liters[i], uprice[i], total[i]);
        this.report2 +=tempString + "\r\n";
     }
     
     double totalamount=this.getTotalPumpReading(date);
     totalamount = (double) Math.round(totalamount * 1000) / 1000;
     //inserts the total
     this.model2.insertRow(this.model2.getRowCount(), new Object[]{" "," ", " ", " "," ", " Total: ", totalamount});
     this.report2+="Total: P " + totalamount;
     this.model2.fireTableDataChanged(); ;
     
     this.db.closeConnection();
      
    }
   
    public double getTotalPumpReading(String date)
    {
       ArrayList<Array> list= this.db.getTotalPumpReading(date);
     
     String [] total = new String[0];
    
     if(list !=null)
     { 
          try{
              total=(String[])list.get(0).getArray();
               
            }catch(NullPointerException e){
                 System.out.println("NULLPOINTER");
            }catch (SQLException ex) {
               Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }              
     }
     if(list == null){
         System.out.println("list is empty: PumpReading.java");
         return -5;
     }
     
     Double amount = 0.0;
     for(int i=0; i<total.length; i++)
     {    
         amount += Double.valueOf(total[i]);
     }
        
     this.db.closeConnection();
     return amount;
 
    }
     
    public void populateCollectionReport(int status)
    {
 
    ArrayList<creditData> data= this.db.getCreditForGeneratingReport(this.datetextField.getText(), status);    
    if(data==null)
    {
       // JOptionPane.showMessageDialog(this, "No Accounts Receivable found");
        return;
    }

     String [] items = new String[0];
     Double [] liters = new Double[0];
     Double [] uprice= new Double[0];

     this.model.insertRow(model.getRowCount(), new Object[]{"Collections Sales", " ", " ", " "});
     this.model.insertRow(model.getRowCount(), new Object[]{"==============","","",""});
     this.report1+="\r\n\r\nCollection Sales\r\n";
     this.report1+="========================\r\n";
     for(int i=0; i<data.size(); i++)
     {
        try {
            items=(String[])data.get(i).arrayData.get(0).getArray();
            liters=(Double[])data.get(i).arrayData.get(1).getArray();
            uprice=(Double[])data.get(i).arrayData.get(2).getArray();
            
        } catch (SQLException ex) {
            Logger.getLogger(SaleTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        String stats="";
        if(data.get(i).status==0)
            stats="unpaid";
        else if(data.get(i).status==1)
            stats="paid";
        
        this.model.insertRow(model.getRowCount(), new Object[]{"Date: " + data.get(i).date, "Invoice: " + data.get(i).invoice, " ", " "});
        this.report1+="Date: " + data.get(i).date + "     Invoice: " + data.get(i).invoice +" \r\n";
        
        this.model.insertRow(model.getRowCount(), new Object[]{"Name: " + data.get(i).name," ", " ", " "});
        this.report1 +="Name: " + data.get(i).name + "  \r\n";
     
        for(int y=0; y<items.length; y++)
        {
           this.model.insertRow(model.getRowCount(), new Object[]{items[y], liters[y] + " liters", uprice[y] + " unit price", " "}); 
           this.report1 +=items[y] + "     " + liters[y] + " liters     @P " + uprice[y] + "\r\n";
        }
        this.model.insertRow(model.getRowCount(), new Object[]{"Amount: " + data.get(i).amtcredit, "status: " + stats, "Date Paid: " + data.get(i).datepaid});
        this.report1+="Amount: " + data.get(i).amtcredit + "      status:" + stats + "     Date Paid: " + data.get(i).datepaid + "\r\n";
        this.model.insertRow(model.getRowCount(), new Object[]{"===============","","",""});
        this.report1+="=======================================================\r\n";
     }
    }
    
    public void populatePaymentReport(int status)
    {
 
    ArrayList<creditData> data= this.db.getPaymentForGeneratingReport(this.datetextField.getText());    
    if(data==null)
    {
        //JOptionPane.showMessageDialog(this, "No Accounts Receivable found");
        return;
    }
     String [] items = new String[0];
     Double [] liters = new Double[0];
     Double [] uprice= new Double[0];
     
     this.model.insertRow(model.getRowCount(), new Object[]{"","","",""});
     this.model.insertRow(model.getRowCount(), new Object[]{"Payment Sales", " ", " ", " "});
     this.model.insertRow(model.getRowCount(), new Object[]{"======================","","",""});
     this.report1+="\r\n\r\nPayment Sales\r\n";
     this.report1+="======================\r\n";
     for(int i=0; i<data.size(); i++)
     {
        try {
            items=(String[])data.get(i).arrayData.get(0).getArray();
            liters=(Double[])data.get(i).arrayData.get(1).getArray();
            uprice=(Double[])data.get(i).arrayData.get(2).getArray();
            
        } catch (SQLException ex) {
            Logger.getLogger(SaleTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        String stats="";
        if(data.get(i).status==0)
            stats="unpaid";
        else if(data.get(i).status==1)
            stats="paid";
        
        this.model.insertRow(model.getRowCount(), new Object[]{"Date: " + data.get(i).date, "Invoice: " + data.get(i).invoice, " ", " "});
        this.report1+="Date: " + data.get(i).date + "     Invoice: " + data.get(i).invoice +" \r\n";
        
        this.model.insertRow(model.getRowCount(), new Object[]{"Name: " + data.get(i).name," ", " ", " "});
        this.report1 +="Name: " + data.get(i).name + "  \r\n";
     
        for(int y=0; y<items.length; y++)
        {
           this.model.insertRow(model.getRowCount(), new Object[]{items[y], liters[y] + " liters", uprice[y] + " unit price", " "}); 
           this.report1 +=items[y] + "     " + liters[y] + " liters    @P " + uprice[y] + "\r\n";
        }
        this.model.insertRow(model.getRowCount(), new Object[]{"Amount: " + data.get(i).amtcredit, "status: paid ", "Date Paid: " + data.get(i).datepaid});
        this.report1+="Amount: " + data.get(i).amtcredit + "      status: paid        Date Paid: " + data.get(i).datepaid + "\r\n";
        this.model.insertRow(model.getRowCount(), new Object[]{"=====================","","",""});
        this.report1+="=======================================================\r\n";
     }
    }
    
    public void populateVoidCreditSalesReport(String date)
    {
 
    ArrayList<creditData> data= this.db.getAllVoidSaleForGeneratingReport(date, 0);   
    if(data==null)
        return;
    
     String [] items = new String[0];
     BigDecimal [] liters = new BigDecimal[0];
     BigDecimal [] uprice= new BigDecimal[0];

     this.model.insertRow(model.getRowCount(), new Object[]{"**Voided Collection Sale Trans.**", " ", " ", " "});
     this.model.insertRow(model.getRowCount(), new Object[]{"**************","","",""});
     this.report1+="\r\n\r\n**Voided Collection Sale Trans.**\r\n";
     this.report1+="*****************************\r\n";
     for(int i=0; i<data.size(); i++)
     {
        try {
            items=(String[])data.get(i).arrayData.get(0).getArray();
            liters=(BigDecimal[])data.get(i).arrayData.get(1).getArray();
            uprice=(BigDecimal[])data.get(i).arrayData.get(2).getArray();
            
        } catch (SQLException ex) {
            Logger.getLogger(SaleTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.model.insertRow(model.getRowCount(), new Object[]{"Date: " + data.get(i).date, "Invoice: " + data.get(i).invoice, " ", " "});
        this.report1+="Date: " + data.get(i).date + "     Invoice: " + data.get(i).invoice +" \r\n";
        
        this.model.insertRow(model.getRowCount(), new Object[]{"Name: " + data.get(i).name," ", " ", " "});
        this.report1 +="Name: " + data.get(i).name + "  \r\n";
     
        for(int y=0; y<items.length; y++)
        {
           this.model.insertRow(model.getRowCount(), new Object[]{items[y], liters[y] + " liters", uprice[y] + " unit price", " "}); 
           this.report1 +=items[y] + "     " + liters[y] + " liters      @P " + uprice[y] + "\r\n";
        }
        this.model.insertRow(model.getRowCount(), new Object[]{"Amount: " + data.get(i).amtcredit});
        this.report1+="Amount: " + data.get(i).amtcredit + "      \r\n";
        this.model.insertRow(model.getRowCount(), new Object[]{"===============","","",""});
        this.report1+="==========================\r\n";
     }
    }
    
     public void populateVoidPaymentSalesReport(String date)
    {
 
    ArrayList<creditData> data= this.db.getAllVoidSaleForGeneratingReport(date, 1);   
    if(data==null)
        return;
    
     String [] items = new String[0];
    BigDecimal [] liters = new BigDecimal[0];
     BigDecimal [] uprice= new BigDecimal[0];

     this.model.insertRow(model.getRowCount(), new Object[]{"**Voided Payment Sale Trans.**", " ", " ", " "});
     this.model.insertRow(model.getRowCount(), new Object[]{"*********************","","",""});
     this.report1+="\r\n\r\n**Voided Payment Sale Trans.**\r\n";
     this.report1+="***************************\r\n";
     for(int i=0; i<data.size(); i++)
     {
        try {
            items=(String[])data.get(i).arrayData.get(0).getArray();
            liters=(BigDecimal[])data.get(i).arrayData.get(1).getArray();
            uprice=(BigDecimal[])data.get(i).arrayData.get(2).getArray();
            
        } catch (SQLException ex) {
            Logger.getLogger(SaleTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.model.insertRow(model.getRowCount(), new Object[]{"Date: " + data.get(i).date, "Invoice: " + data.get(i).invoice, " ", " "});
        this.report1+="Date: " + data.get(i).date + "     Invoice: " + data.get(i).invoice +" \r\n";
        
        this.model.insertRow(model.getRowCount(), new Object[]{"Name: " + data.get(i).name," ", " ", " "});
        this.report1 +="Name: " + data.get(i).name + "  \r\n";
     
        for(int y=0; y<items.length; y++)
        {
           this.model.insertRow(model.getRowCount(), new Object[]{items[y], liters[y] + " liters", uprice[y] + " unit price", " "}); 
           this.report1 +=items[y] + "     " + liters[y] + " liters     @P " + uprice[y] + "\r\n";
        }
        this.model.insertRow(model.getRowCount(), new Object[]{"Amount: " + data.get(i).amtcredit});
        this.report1+="Amount: " + data.get(i).amtcredit + "      \r\n";
        this.model.insertRow(model.getRowCount(), new Object[]{"===============","","",""});
        this.report1+="=======================\r\n";
     }
    }
     
    public void populateVoidSalesReport(String date)
    {
 
    ArrayList<creditData> data= this.db.getAllVoidSaleForGeneratingReport(date, 2);   
    if(data==null)
        return;
    
     String [] items = new String[0];
     BigDecimal [] liters = new BigDecimal[0];
     BigDecimal [] uprice= new BigDecimal[0];

     this.model.insertRow(model.getRowCount(), new Object[]{"**Voided Sale Transaction**", " ", " ", " "});
     this.model.insertRow(model.getRowCount(), new Object[]{"**************","","",""});
     this.report1+="\r\n\r\n**Voided Sale Transaction**\r\n";
     this.report1+="*****************************\r\n";
     for(int i=0; i<data.size(); i++)
     {
        try {
            items=(String[])data.get(i).arrayData.get(0).getArray();
            liters=(BigDecimal[])data.get(i).arrayData.get(1).getArray();
            uprice=(BigDecimal[])data.get(i).arrayData.get(2).getArray();
            
        } catch (SQLException ex) {
            Logger.getLogger(SaleTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.model.insertRow(model.getRowCount(), new Object[]{"Date: " + data.get(i).date, "Time: " + data.get(i).time, " ", " "});
        this.report1+="Date: " + data.get(i).date + "     Time: " + data.get(i).time +" \r\n";
        
        for(int y=0; y<items.length; y++)
        {
           this.model.insertRow(model.getRowCount(), new Object[]{items[y], liters[y] + " liters", uprice[y] + " unit price", " "}); 
           this.report1 +=items[y] + "     " + liters[y] + " liters     @P " + uprice[y] + "\r\n";
        }
        this.model.insertRow(model.getRowCount(), new Object[]{"Amount: " + data.get(i).total});
        this.report1+="Amount: " + data.get(i).total + "      \r\n";
         this.model.insertRow(model.getRowCount(), new Object[]{"Amt Tendered: " + data.get(i).amtcredit});
        this.report1+="Amt Temdered: " + data.get(i).amtcredit + "      \r\n";
        this.model.insertRow(model.getRowCount(), new Object[]{"===============","","",""});
        this.report1+="=========================\r\n";
     }
    }
    
    public void populateDeliveryReport()
    {
       ArrayList<DeliveryData> list =  this.db.getDeliveryReport(this.datetextField.getText());
       String [] product = new String[0];
       Double [] stock = new Double[0];
       
          this.model.insertRow(model.getRowCount(), new Object[]{"","","",""});
    
     if(list !=null)
     {
       this.model.insertRow(model.getRowCount(), new Object[]{"Delivery Report "});
       this.report1+="Delivery Report            \r\n";
         for(int i=0; i<list.size(); i++)
         {
           this.model.insertRow(model.getRowCount(), new Object[]{"===============","==============="});
           this.report1+="========================================= \r\n";
           this.model.insertRow(model.getRowCount(), new Object[]{"Date:" + list.get(i).date, "  ","  ", " "});
           this.report1+="Date:" + list.get(i).date + "\r\n";
           this.model.insertRow(model.getRowCount(), new Object[]{"Name:" + list.get(i).name, "Invoice:" + list.get(i).invoice," ", " "});
           this.report1+="Name:" + list.get(i).name + "      Invoice:" + list.get(i).invoice + "\r\n";

           try{
               product=(String[]) list.get(i).arrayData.get(0).getArray();
               stock =(Double[]) list.get(i).arrayData.get(1).getArray();
               
               for(int y=0; y<product.length; y++)
               {
               this.model.insertRow(model.getRowCount(), new Object[]{product[y], "  Quantity:" + stock[y],"  ", "  "});
               this.report1+=product[y] + "    Quantity:" + stock[y]+ "\r\n";

               }
            }catch(NullPointerException e){
                 System.out.println("NULLPOINTER");
            }catch (SQLException ex) {
               Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
         }         
     }
     if(list == null){
         System.out.println("list is empty: Report Delivery");
         return;
     }
     this.db.closeConnection();
 
    }
    
     
    
    public double roundOff(double amt)
    {
        return Math.round( amt * 100.0 ) / 100.0;
    }
    
    public String getDate()
        {
           DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
           Date date = new Date();
           return dateFormat.format(date);
        }
    /** Creates new form EndOfTheDayPanel */
    public Report() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        CloseButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        datetextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        datePickerFrombtn = new javax.swing.JButton();
        generateReportBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 255, 204));

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        CloseButton.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        CloseButton.setText("Close");
        CloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 255));
        jLabel2.setText("Report");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(5, 5, 5)
                .add(CloseButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 206, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(505, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(CloseButton))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(8, 8, 8)
                        .add(jLabel2)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        datetextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                datetextFieldActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jLabel3.setText("Date:");

        datePickerFrombtn.setText("Date Picker");
        datePickerFrombtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                datePickerFrombtnActionPerformed(evt);
            }
        });

        generateReportBtn.setText("Generate Report");
        generateReportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateReportBtnActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTable2.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Pump Reading");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 754, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createSequentialGroup()
                                .add(jLabel3)
                                .add(1, 1, 1)
                                .add(datetextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 133, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(21, 21, 21)
                                .add(datePickerFrombtn)
                                .add(18, 18, 18)
                                .add(generateReportBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 172, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(datetextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(datePickerFrombtn)
                    .add(generateReportBtn))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 251, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 235, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void CloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseButtonActionPerformed
// TODO add your handling code here:
    this.setVisible(false);
}//GEN-LAST:event_CloseButtonActionPerformed

private void generateReportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateReportBtnActionPerformed
    this.initJTableList();
    this.initJTableList2();
    this.report1="";
    this.report2="";
    
    int result = 0;
    // TODO add your handling code here:
    if(!this.datetextField.getText().equals("none"))
        result =this.db.checkEOD(this.datetextField.getText());
   
  //0 means EOD has not been generated, -5 means no records in dailySales
  if(result == 0 || result ==-5)
  {
    JOptionPane.showMessageDialog(this, "Cannot generate report becase this date:" + this.datetextField.getText() + " has not generated EndOfTheDay. \r\nGenerate it first before you can view the report");      
  }
  //means EOD has been generated so we can generate a report
  else if(result==1)
     {
       //gets total sale
       double totalSales = this.db.getTotalSales(this.datetextField.getText());
       double amtTendered = this.db.getAmtTendered(this.datetextField.getText());
       
       this.getPumpReadingData(this.datetextField.getText());
       
       //get total credit amt for collections
       double amtCollection = this.db.getTotalCreditForReport(this.datetextField.getText(), 0);
       //gets total credit amt for payments
       double amtPayments = this.db.getTotalPaymentForReport(this.datetextField.getText());
   
       //gets the pCatalog, use for searching UPC  
       ArrayList<Product> pList = this.db.getProdCatalog();
       HashMap<Integer, Product> pCatalog = new HashMap<Integer, Product>();
     
       for(Product i : pList)
           pCatalog.put(i.getUPC(), i);
    
     ArrayList<Array> reportList = this.db.generateReport(this.datetextField.getText());
    
     
     Integer [] upc = new Integer[0];
     Double [] liters = new Double[0];
     Double [] uprice = new Double[0];

        try {
            upc=(Integer[]) reportList.get(0).getArray();
            liters=(Double[]) reportList.get(1).getArray();
            uprice=(Double[]) reportList.get(2).getArray();
        
        
        } catch (SQLException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
        
      this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"Report"});
      this.report1="Report\r\n";
      this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"Day: " +this.datetextField.getText()});
      this.report1+="Day: " + this.datetextField.getText();
  
      this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"  "});
      this.report1+="\r\n     \r\n";
     
      this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"Sales:       ", "P " + totalSales});
      this.report1+="Sales: P " + totalSales + "\r\n";
      
       this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"Amount Tendered:", "P " + amtTendered});
      this.report1+="Amount Tendered: P " + amtTendered + "\r\n";
      
      this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"Collections Sales: ", "P " + amtCollection});
      this.report1+="Collection Sales: P " + amtCollection + "\r\n";
     
      this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"Payments Sales:    ", "P " + amtPayments});
      this.report1+="Payment Sales: P " + amtPayments + "\r\n";
     
      this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"================", "============="});
      this.report1+="============================================\r\n";
      
      
     
      
      //this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"Balance:", " P" + this.roundOff(amtTendered - totalSales)});
      //this.report1+="Balance: P " + this.roundOff(amtTendered - totalSales) + "\r\n\r\n";
      this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"  "});
      this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"Items Summary"});
      this.report1+="Items Summary \r\n";

      //for total amount liters
      double totAmtSummarize = 0;
      for(int i=0; i<upc.length; i++)
      {
          double temp=(double)Math.round( (liters[i] * uprice[i]) * 1000 ) / 1000;
          totAmtSummarize+=temp;
        this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"      " + pCatalog.get(upc[i]).getDesciption(), 
            "@" + this.roundOff(uprice[i]) + "     X   ", 
            this.roundOff(liters[i]) + "  liters", "= P " + temp});
        String tempString=String.format("%-20s  @   %-10s  X  %-7s  liters = P %-10s \r\n",pCatalog.get(upc[i]).getDesciption(), this.roundOff(uprice[i]),this.roundOff(liters[i]), temp);
        this.report1+=tempString;
      }
        this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"  ","   ", "---------------------------", "----------------------------"});
        this.report1 +="-----------------------------------------------------------------------\r\n";
        this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"    ", "    ","                        Total:", "= P " +totAmtSummarize});
        this.report1+="                                                Total: P " + totAmtSummarize;
        this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"  "});
        this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"  "});
        
        this.populateCollectionReport(0);
        this.populatePaymentReport(1);
        this.populateDeliveryReport();
        this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"  "});
        this.populateVoidCreditSalesReport(this.datetextField.getText());
        this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"  "});
        this.populateVoidPaymentSalesReport(this.datetextField.getText());
        this.model.insertRow(this.jTable1.getRowCount(),new Object[]{"  "});
        this.populateVoidSalesReport(this.datetextField.getText());
        
     
        //need to close connection here bec .getArray need to access DB
        this.db.closeConnection();
        
        this.createFileReport(this.datetextField.getText());
     
    
     }
  
    
     
     
}//GEN-LAST:event_generateReportBtnActionPerformed

private void datePickerFrombtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_datePickerFrombtnActionPerformed
     // TODO add your hanew DatePicker2ndling code here:
     String date=new DatePicker(this).setPickedDate();
     
     this.datetextField.setText(date);  
}//GEN-LAST:event_datePickerFrombtnActionPerformed

private void datetextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_datetextFieldActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_datetextFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CloseButton;
    private javax.swing.JButton datePickerFrombtn;
    private javax.swing.JTextField datetextField;
    private javax.swing.JButton generateReportBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables

    private DBConnection db;
    private DefaultTableModel model, model2;
    private String report1="", report2="";
}

