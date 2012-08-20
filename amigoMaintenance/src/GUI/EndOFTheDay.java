/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EndOFTheDay.java
 *
 * Created on Jun 13, 2012, 3:20:54 AM
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Array;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import mDB.DBConnection;

/**
 *
 * @author Hanzen
 */
public class EndOFTheDay extends javax.swing.JPanel {

    public void initEndOfTheDay(DBConnection db, int status)
    {
        this.db = db;
        this.status=status;
        
        this.dateTxtField.setText(this.getDate());
        this.totalSalesTxtField.setEditable(false);
        this.dateTxtField.setEditable(false);
        this.amtTenderedTxtField.setText("");
        
        double temp = this.db.getTotalSales(this.getDate());
        if(temp!=-78)
           this.totalSalesTxtField.setText(String.valueOf(temp));
        
        this.initJTableList();
        this.setColJTable();
        
        this.getPumpReadingData(this.getDate());
        
        this.populateLiters();
        this.populateTotal();
    }
    
    //init JTable
    public void initJTableList()
    {
         String data [][] = {{null,null,null,null,null,null,null},{null,null,null,null,null,null,null},{null,null,null,null,null,null,null},{null,null,null,null,null,null,null},{null,null,null,null,null,null,null},
           {null,null,null,null,null,null,null},{null,null,null,null,null,null,null},{null,null,null,null,null,null,null},{null,null,null,null,null,null,null},{null,null,null,null,null,null,null},
           {null,null,null,null,null,null,null},{null,null,null,null,null,null,null},{null,null,null,null,null,null,null},{null,null,null,null,null,null,null},{null,null,null,null,null,null,null},
           {null,null,null,null,null,null,null},{null,null,null,null,null,null,null},{null,null,null,null,null,null,null},{null,null,null,null,null,null,null},{null,null,null,null,null,null,null},
           {null,null,null,null,null,null,null},{null,null,null,null,null,null,null},{null,null,null,null,null,null,null},{null,null,null,null,null,null,null},{null,null,null,null,null,null,null}};
         
        
         String col[] = {"Product", "Beginning", "Ending","Calibration", "Liters", "Unit Price", "Total"};
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
    
     //sets the column width for JTable
    public void setColJTable()
    {
        TableColumn column = null;
        for (int i = 0; i < 6; i++) {
            column = this.jTable1.getColumnModel().getColumn(i);
           if (i == 0) {
                column.setPreferredWidth(150); 
           }
           else if (i==1)
               column.setPreferredWidth(80);
           else if(i==2)
                column.setPreferredWidth(80);  
           else if(i==4)
               column.setPreferredWidth(50);
           else {
                column.setPreferredWidth(80);
            }
        }
    }
    
    public String getDate()
    {
           DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
           Date date = new Date();
           return dateFormat.format(date);
    }
    
     public void getPumpReadingData(String date)
    {
        
        ArrayList<Array> list= this.db.getPumpReading(date);
     
     String [] product = new String[0];
     String [] beginning = new String[0];
     String [] ending= new String[0];
     String [] uprice = new String[0];
     String [] calib = new String[0];
     
     //System.out.println("SIZE::"+ list.size());
     
     if(list !=null)
     {
         for(int i=0; i<5; i++)
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
                   calib=(String[]) list.get(4).getArray();
               
            }catch (SQLException ex) {
               Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }catch(NullPointerException e){
               //  System.err.println("NULLPOINTER");
            }
         } 
         
     }
     if(list == null){
         System.err.println("list is empty: PumpReading.java");
         return;
     }
   
  
    
//     for(int i=0; i<product.length; i++)
//     {
//         
//         Vector<String> temp = new Vector<String>();
//         temp.add(String.valueOf(product[i]));
//         temp.add(String.valueOf(beginning[i]));
//         temp.add(String.valueOf(ending[i]));
//         temp.add(String.valueOf(uprice[i]));
//    
//         for(int y=0; y<4; y++)
//         {
//             this.model.setValueAt(temp.get(y), i, y);
//             
//         }
//     }
     for(int i=0; i<product.length; i++)
     {
         this.model.setValueAt(String.valueOf(product[i]), i, 0);
     }
     for(int i=0;i<beginning.length; i++)
     {
          this.model.setValueAt(String.valueOf(beginning[i]), i, 1);
     }
     for(int i=0; i<ending.length; i++)
     {
          this.model.setValueAt(String.valueOf(ending[i]), i, 2);
     }
     for(int i=0; i<uprice.length; i++)
     {
          this.model.setValueAt(String.valueOf(uprice[i]), i, 5);
     }
     for(int i=0; i<calib.length; i++)
     {
          this.model.setValueAt(String.valueOf(calib[i]), i, 3);
     }
     this.model.fireTableDataChanged(); 
     
     this.db.closeConnection();
    }
     
    public void populateLiters()
    { 

      this.liters.clear();
        for(int i=0; i<25; i++)
        {
           try{
     
           Double ending= Double.valueOf((String)this.model.getValueAt(i, 2));
           Double beginning= Double.valueOf((String)this.model.getValueAt(i, 1));
           Double calib= Double.valueOf((String)this.model.getValueAt(i, 3));
           
           Double liters=ending-beginning-calib;
           liters=Math.round( liters* 100.0 ) / 100.0;
           
           this.model.setValueAt(liters, i, 4);
           this.liters.add(String.valueOf(liters));
          
           }catch(NullPointerException e){
               //System.out.println("Null");
           }
        }
    }
    
    public void populateTotal()
    { this.total.clear();
 
        for(int i=0; i<25; i++)
        {
            try{
            double tempLiters=(Double)this.model.getValueAt(i, 4);
            String tempPrice=(String)this.model.getValueAt(i,5);
            
            
            Double uprice = Double.valueOf(tempPrice);
            Double total =  tempLiters * uprice;
            total=Math.round( total * 100.0 ) / 100.0;
            
            this.model.setValueAt(total, i, 6);
            this.total.add(String.valueOf(total));
            }catch(NullPointerException e){
                
            }
            
        }
    }
    
    /** Creates new form EndOFTheDay */
    public EndOFTheDay() {
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        amtTenderedTxtField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        totalSalesTxtField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        dateTxtField = new javax.swing.JTextField();
        DatePickerButton = new javax.swing.JButton();
        generateEODbtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

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
        jLabel2.setText("End of the Day");

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
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel1.setText("Amount Tendered:");

        amtTenderedTxtField.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        amtTenderedTxtField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        amtTenderedTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                amtTenderedTxtFieldActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel4.setText("Total Sales:");

        totalSalesTxtField.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        totalSalesTxtField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalSalesTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalSalesTxtFieldActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(40, 40, 40)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(jLabel4))
                .add(18, 18, 18)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(amtTenderedTxtField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 167, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(totalSalesTxtField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 167, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(12, 12, 12)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(totalSalesTxtField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(amtTenderedTxtField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jLabel3.setText("Date:");

        dateTxtField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        dateTxtField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateTxtFieldActionPerformed(evt);
            }
        });

        DatePickerButton.setText("Date Picker");
        DatePickerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DatePickerButtonActionPerformed(evt);
            }
        });

        generateEODbtn.setText("Generate End of the Day");
        generateEODbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateEODbtnActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {" ", "", "", "", "", ""},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 93, Short.MAX_VALUE)
                        .add(generateEODbtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 169, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(84, 84, 84))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 754, Short.MAX_VALUE)
                            .add(layout.createSequentialGroup()
                                .add(jLabel3)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(dateTxtField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 121, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(7, 7, 7)
                                .add(DatePickerButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 132, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(layout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel3)
                            .add(dateTxtField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(DatePickerButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(20, 20, 20))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(generateEODbtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(65, 65, 65)))
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void CloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseButtonActionPerformed
// TODO add your handling code here:
    this.setVisible(false);
}//GEN-LAST:event_CloseButtonActionPerformed

private void amtTenderedTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_amtTenderedTxtFieldActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_amtTenderedTxtFieldActionPerformed

private void totalSalesTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalSalesTxtFieldActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_totalSalesTxtFieldActionPerformed

private void DatePickerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DatePickerButtonActionPerformed
// TODO add your handling code here:
    String date=new DatePicker(this).setPickedDate();
    System.out.println("date:"+ date);
    if(!date.equals("none"))
    {
        System.out.println("bababab");
         //returns -78 if no records found, it means no transaction at that date
        double temp=this.db.getTotalSales(date);
    
        if(temp<0)
        JOptionPane.showMessageDialog(this, "No sales transaction at that day, Cannot generate EndOfTheDay report");
        else   
        {
         this.dateTxtField.setText(date);
         this.totalSalesTxtField.setText(String.valueOf(temp));
         
         this.getPumpReadingData(date);
        this.populateLiters();
        this.populateTotal();
        }
    }
    
}//GEN-LAST:event_DatePickerButtonActionPerformed

private void generateEODbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateEODbtnActionPerformed
// TODO add your handling code here:
    double amtTendered;
    String date;
    try{
       amtTendered = Double.valueOf(this.amtTenderedTxtField.getText());
       date = this.dateTxtField.getText();
    
    }catch(NumberFormatException e)
    {
        JOptionPane.showMessageDialog(this, "Please enter a number");
        return;
    }
    int k=this.db.checkEOD(date);
    //status==1 means manager can regen EOD
    if(k==1 && status==0)
    {
       JOptionPane.showMessageDialog(this, "SORRY: END OF THE DAY WAS ALREADY GENERATED\n YOU CAN ONLY GENERATE IT ONCE");
    }
    else if((status==1 || status==0) && k!=-5)
    {
       int result = this.db.endOfTheDay(date, amtTendered);
       
       
       int result2 = 0;
       //saves the pumpreading
       if(liters.size()!=0 || this.total.size()!=0)
       {
          result2= this.db.updatePumpReadingLitersTotal(date, this.liters, this.total, 1);
       }
       
       int result3=this.db.updateEODcreditSales();
       int result4=this.db.updateEODpaymentSales();
       int result5= this.db.updateEODAllvoidsales(0);
       int result6= this.db.updateEODAllvoidsales(1);
       int result7 = this.db.updateEODAllvoidsales(2);
 
       if(result==1)
       {
           JOptionPane.showMessageDialog(this, "Successfully Generated End of the day");
           this.amtTenderedTxtField.setText("");
           this.setVisible(false);
       }
       else if(result==-78)
           JOptionPane.showMessageDialog(this, "Something went wrong, generateEOD code -78");
       else if(result==0)
            JOptionPane.showMessageDialog(this, "Input Date was incorrect");       
    }
    else if(k==-5)
         JOptionPane.showMessageDialog(this, "No Transaction at this date:" + date);
    
  this.total.clear();
  this.liters.clear();
}//GEN-LAST:event_generateEODbtnActionPerformed

private void dateTxtFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateTxtFieldActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_dateTxtFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CloseButton;
    private javax.swing.JButton DatePickerButton;
    private javax.swing.JTextField amtTenderedTxtField;
    private javax.swing.JTextField dateTxtField;
    private javax.swing.JButton generateEODbtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField totalSalesTxtField;
    // End of variables declaration//GEN-END:variables

    private DBConnection db;
    private DefaultTableModel model;
    
    private ArrayList<String> liters= new ArrayList<String>();
    private ArrayList<String> total = new ArrayList<String>();
    
    private int status=0;

}

class DatePicker {
        int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
        int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        int currentDay = java.util.Calendar.getInstance().get(java.util.Calendar.DATE);

        JLabel l = new JLabel("", JLabel.CENTER);
        String day = "";
        JDialog d;
        JButton[] button = new JButton[49];

        public DatePicker(JPanel parent) {
                d = new JDialog();
             
                d.setModal(true);
                String[] header = { "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat" };
                JPanel p1 = new JPanel(new GridLayout(7, 7));
                p1.setPreferredSize(new Dimension(430, 160));

                for (int x = 0; x < button.length; x++) {
                        final int selection = x;
                        button[x] = new JButton();
                        button[x].setFocusPainted(false);
                        button[x].setBackground(Color.white);
                        if (x > 6)
                                button[x].addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent ae) {
                                                day = button[selection].getActionCommand();
                                                d.dispose();
                                        }
                                });
                        if (x < 7) {
                                button[x].setText(header[x]);
                                button[x].setForeground(Color.red);
                        }
                        p1.add(button[x]);
                }
                JPanel p2 = new JPanel(new GridLayout(1, 3));
                JButton previous = new JButton("<< Previous");
                previous.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                                month--;
                                displayDate();
                        }
                });
                p2.add(previous);
                p2.add(l);
                JButton next = new JButton("Next >>");
                next.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                                month++;
                                displayDate();
                        }
                });
                p2.add(next);
                d.add(p1, BorderLayout.CENTER);
                d.add(p2, BorderLayout.SOUTH);
                d.pack();
                d.setLocationRelativeTo(parent);
                displayDate();
                d.setVisible(true);
        }

        public void displayDate() {
                for (int x = 7; x < button.length; x++)
                        button[x].setText("");
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                                "MMMM yyyy");
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(year, month, 1);
         
                int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
                int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
                
                int currentMonth = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
                
                if(currentMonth == month)
                  for (int x = 6 + dayOfWeek, day = 1; day <= currentDay; x++, day++)
                        button[x].setText("" + day);
                else
                 for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++)
                        button[x].setText("" + day);    
                l.setText(sdf.format(cal.getTime()));
                d.setTitle("Date Picker");
        }
        
       

        public String setPickedDate() {
                if (day.equals(""))
                {
                    day="none";
                        return day;
                }
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                                "MM/dd/yyyy");
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(year, month, Integer.parseInt(day));
                return sdf.format(cal.getTime());
        }
}
