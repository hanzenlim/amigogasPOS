/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PumpReading.java
 *
 * Created on Jun 16, 2012, 1:11:31 AM
 */
package GUI;

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
public class PumpReading extends javax.swing.JPanel {

    public void initPumpReading(DBConnection db, int status)
    {
        this.db = db;
        this.status=status;
        this.beginningList = new ArrayList<String>();
        this.endingList = new ArrayList<String>();
        this.productList = new ArrayList<String>();
        this.uPriceList = new ArrayList<String>();
        this.calibList = new ArrayList<String>();
        this.total = new ArrayList<String>();
        this.liters = new ArrayList<String>();
        
        this.dateTxtField.setText(this.getDate());
        
        this.initJTableList();
        
        this.getPumpReadingData(this.getDate());
        
        this.dateTxtField.setEditable(false);
        
    }
    
    public int populateLitersAndTotal()
    { 

      this.liters.clear();
        for(int i=0; i<25; i++)
        {
           try{
     
           Double ending= Double.valueOf((String)this.model.getValueAt(i, 2));
           Double beginning= Double.valueOf((String)this.model.getValueAt(i, 1));
           Double calib= Double.valueOf((String)this.model.getValueAt(i, 4));
           Double uprice =Double.valueOf((String)this.model.getValueAt(i,3));
           
           Double liters=ending-beginning-calib;
           liters=(double)Math.round( liters* 1000 ) / 1000;
          
           this.liters.add(String.valueOf(liters));
           
           double total = liters * uprice;
                   
           this.total.add(String.valueOf(total));
          
           }catch(NullPointerException e){
               //System.out.println("Null");
           }
        }
        
         int result = this.db.updatePumpReadingLitersTotal(this.dateTxtField.getText(), this.liters, this.total,0);
    return result;
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
          this.model.setValueAt(String.valueOf(uprice[i]), i, 3);
     }
     for(int i=0; i<calib.length; i++)
     {
          this.model.setValueAt(String.valueOf(calib[i]), i, 4);
     }
     
     this.model.fireTableDataChanged(); 
     
     this.db.closeConnection();
    }
    
     public String getDate()
     {
           DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
           Date date = new Date();
           return dateFormat.format(date);
     }
    //init JTable
    public void initJTableList()
    {
        String data [][] = {{null,null,null,null,null},{null,null,null,null,null},{null,null,null,null,null},{null,null,null,null,null},{null,null,null,null,null},
            {null,null,null,null,null},{null,null,null,null,null},{null,null,null,null,null},{null,null,null,null,null},{null,null,null,null,null},
            {null,null,null,null,null},{null,null,null,null,null},{null,null,null,null,null},{null,null,null,null,null},{null,null,null,null,null},
            {null,null,null,null,null},{null,null,null,null,null},{null,null,null,null,null},{null,null,null,null,null},{null,null,null,null,null},
            {null,null,null,null,null},{null,null,null,null,null},{null,null,null,null,null},{null,null,null,null,null},{null,null,null,null,null}};
        
         String col[] = {"Product", "Beginning", "Ending", "Unit Price", "Calibration"};
         this.model= new DefaultTableModel(data,col);
//         {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                //all cells false
//                return false;
//            }
//        };
         
        this.jTable1.setModel(model);
        this.jTable1.setRowHeight(20);
        
    }
    //sets the column width for JTable
    public void setColJTable()
    {
        TableColumn column = null;
        for (int i = 0; i < 4; i++) {
            column = this.jTable1.getColumnModel().getColumn(i);
           if (i == 0) {
                column.setPreferredWidth(150); 
           }
           else if (i==1)
               column.setPreferredWidth(80);
           else if(i==2)
                column.setPreferredWidth(80);  
           else 
               column.setPreferredWidth(80);
        }
    }
    
    
    /** Creates new form PumpReading */
    public PumpReading() {
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
        jLabel1 = new javax.swing.JLabel();
        dateTxtField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        saveReadingBtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBackground(new java.awt.Color(204, 204, 255));

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
        jLabel2.setText("Pump Reading");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(CloseButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 206, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(CloseButton)
                    .add(jLabel2))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 15)); // NOI18N
        jLabel1.setText("Date:");

        jButton1.setText("Date Picker");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        saveReadingBtn.setText("Save Reading");
        saveReadingBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveReadingBtnActionPerformed(evt);
            }
        });

        jButton2.setText("View");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(28, 28, 28)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(jLabel1)
                                .add(1, 1, 1)
                                .add(dateTxtField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 107, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButton1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButton2))
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                .add(saveReadingBtn)
                                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 564, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 37, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(dateTxtField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton1)
                    .add(jButton2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(saveReadingBtn)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void CloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseButtonActionPerformed
// TODO add your handling code here:
    this.setVisible(false);
}//GEN-LAST:event_CloseButtonActionPerformed

private void saveReadingBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveReadingBtnActionPerformed
    productList.clear();
    endingList.clear();
    uPriceList.clear();
    beginningList.clear();
    this.calibList.clear();
    
    boolean test =this.db.checkPumpReadingStatus(this.dateTxtField.getText());
    if(test == false && status==0)
    {
       JOptionPane.showMessageDialog(this, "Cannot edit Pump Reading, cause you already generated End of the Day");     
       return;
    }
    
    for(int i=0; i<25; i++)
    {
       //getings the ending value
       String x=String.valueOf(this.jTable1.getValueAt(i, 0)); 
       String y=String.valueOf(this.jTable1.getValueAt(i, 1));
       String z=String.valueOf(this.jTable1.getValueAt(i, 2));
       String p=String.valueOf(this.jTable1.getValueAt(i, 3));
       String q=String.valueOf(this.jTable1.getValueAt(i, 4));
     
       if(!x.equals("null") && !y.equals("null") && !z.equals("null") && !p.equals("null") && !q.equals("null"))
       {
           try{
           Double temp = Double.valueOf(y);
           Double temp2 = Double.valueOf(z);
           Double temp3 = Double.valueOf(p);
           
           this.productList.add(x);
           this.beginningList.add(y);
           this.endingList.add(z);
           this.uPriceList.add(p);
           this.calibList.add(q);
           }catch(NumberFormatException e)
           {
               JOptionPane.showMessageDialog(this, "Error: Only numbers are allowed in Beginning,Ending,UnitPrice");
               return;
           }
            
       }
           
    }
    
    int result=0, result2=0;
    if(productList.size()!=0 &&  beginningList.size()!=0 && endingList.size()!=0 && uPriceList.size()!=0 && calibList.size()!=0){
        result= this.db.updatePumpReadingBeginning(this.dateTxtField.getText(), productList, beginningList, endingList, uPriceList, calibList);
        result2 = this.populateLitersAndTotal();
        JOptionPane.showMessageDialog(this, "Successfully saved Ending Reading");  
    }
    else
        JOptionPane.showMessageDialog(this, "Error: List is empty, cannot save to DB");
   
    productList.clear();
    beginningList.clear();
    endingList.clear();
    uPriceList.clear();
    calibList.clear();
    
   
}//GEN-LAST:event_saveReadingBtnActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
     String date=new DatePicker(this).setPickedDate();
     
     if(!date.equals("none"))
     {
       this.dateTxtField.setText(date); 
    
     }  
}//GEN-LAST:event_jButton1ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
// TODO add your handling code here:
    
      this.initJTableList();
       if(!this.dateTxtField.getText().equals("none")) 
            this.getPumpReadingData(this.dateTxtField.getText());
}//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CloseButton;
    private javax.swing.JTextField dateTxtField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton saveReadingBtn;
    // End of variables declaration//GEN-END:variables

    private DefaultTableModel model;
    
            
    private DBConnection db;
    
    private ArrayList<String> beginningList;
    private ArrayList<String> endingList;
    private ArrayList<String> productList;
    private ArrayList<String> uPriceList;
    private ArrayList<String> calibList;
    
    private int status=0;
    
    private ArrayList<String> total;
    private ArrayList<String> liters;
    

}


