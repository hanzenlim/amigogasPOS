/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import mDB.DBConnection;
import mDB.creditData;

/**
 *
 * @author OEM
 */
public class Collections extends javax.swing.JPanel {

    public void initAccountReceivable(DBConnection db)
    {
        this.db=db;
    }
     //init JTable
    public void initJTableList()
    {
         String data[][] = null;
         String col[] = {"Date","Invoice", "Name", "Items", "Liters", "Unit Price", "Amount", "Status"};
         model= new DefaultTableModel(data,col)
         {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };      
        this.jTable1.setModel(model);
        this.jTable1.setRowHeight(25);
   
    }
    
     public void setColJTable()
    {
        TableColumn column = null;
        for (int i = 0; i < 4; i++) {
            column = this.jTable1.getColumnModel().getColumn(i);
           if (i == 0) 
                column.setPreferredWidth(60); 
           else if (i==1)
               column.setPreferredWidth(60);
           else if(i==2)
                column.setPreferredWidth(80);
           else if(i==3)
                column.setPreferredWidth(150); 
           else if(i==4)
                column.setPreferredWidth(180); 
           else if(i==5)
                column.setPreferredWidth(110); 
           else if(i==6)
                column.setPreferredWidth(100); 
           else if(i==7)
                column.setPreferredWidth(80); 
           else {
                column.setPreferredWidth(110);
            }
        }
    }
     
    //returns the invoice number in the table
    public String getInvoiceSelectedItem()
    {
        if(this.jTable1.getSelectedRow() != -1)
        {
            //returns the UPC number
            String a=(String)this.jTable1.getValueAt(this.jTable1.getSelectedRow(), 1);
          return a;
        }

    return null;
    }
    public double getAmtSelectedItem()
    {
         if(this.jTable1.getSelectedRow() != -1)
        {
            //returns the amount
            double a=(Double)this.jTable1.getValueAt(this.jTable1.getSelectedRow(), 6);
          return a;
        }
    return 0;
    }
    public String getNameSelectedItem()
    {
        if(this.jTable1.getSelectedRow() != -1)
        {
            //returns the amount
            String a=(String)this.jTable1.getValueAt(this.jTable1.getSelectedRow(), 2);
          return a;
        }
    return null; 
    }
    
    //add row to JTable
    public void addDataJTableList(Date date, String name, String invoice, String items, String liters, String uprice, double amtcredit, String status)
    {
      this.model.insertRow(this.jTable1.getRowCount(),new Object[]{date, name,invoice,items,liters,uprice,amtcredit,status});
      this.model.fireTableDataChanged();
      
      //scrolls the JTable at the bottom
      //source 
      //http://stackoverflow.com/questions/5956603/jtable-autoscrolling-to-bottom-problem
      //Rectangle rect = this.jTable1.getCellRect(this.jTable1.getRowCount(), 0, true);
      //Rectangle r2 = this.jTable1.getVisibleRect();
      
      //this.jTable1.scrollRectToVisible(new Rectangle(rect.x, rect.y, (int) r2.getWidth(), (int) r2.getHeight()));
      
    }
    
    public void populateTable()
    {
    this.initJTableList();
    this.setColJTable();
    String name = this.nameTxtField.getText();
    ArrayList<creditData> data= this.db.getcreditData(name, 0, "0");
    
    if(data==null)
    {
        JOptionPane.showMessageDialog(this, "No Accounts Receivable found");
        return;
    }
    
     String [] items = new String[0];
     Double [] liters = new Double[0];
     Double [] uprice= new Double[0];

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
       this.addDataJTableList(data.get(i).date, data.get(i).invoice, data.get(i).name,
               data.get(i).arrayData.get(0).toString(), 
               data.get(i).arrayData.get(1).toString(), data.get(i).arrayData.get(2).toString(), 
               data.get(i).amtcredit, stats);
    
     }
    }
    /**
     * Creates new form AccountsReceivable
     */
    public Collections() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        CloseButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        nameTxtField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        paymentBtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

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
        jLabel2.setText("Collections");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(CloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(678, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CloseButton)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel1.setText("Name:");

        nameTxtField.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jButton1.setText("View");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        paymentBtn.setText("Payment");
        paymentBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentBtnActionPerformed(evt);
            }
        });

        jButton2.setText("Void");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(28, 28, 28)
                        .addComponent(paymentBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 909, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameTxtField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(paymentBtn)
                    .addComponent(jButton2))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void CloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseButtonActionPerformed
// TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_CloseButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.populateTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void paymentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentBtnActionPerformed
        // TODO add your handling code here:
        String invoice = this.getInvoiceSelectedItem();
        double amount = this.getAmtSelectedItem();
        String name = this.getNameSelectedItem();
        if(this.jTable1.getSelectedRow()==-1)
        {
            JOptionPane.showMessageDialog(this, "Please select an item to pay");
            return;
        }
        ARPaymentFormPanel arpayment = new ARPaymentFormPanel(invoice, name, amount);
        //0 yes, 1 no, 2 cancel
        int answer = JOptionPane.showConfirmDialog(null, arpayment);
        
        int result=0;
        if(answer==0)
        {
            String info = arpayment.getInfo();
           //result= this.db.savePaymentInfo(name, invoice, info);
           result = this.db.savePaymentSales(name, invoice, info);
            if(result==1)
                JOptionPane.showMessageDialog(this, "Paid Item");
            else
                JOptionPane.showMessageDialog(this, "Something went wrong, unable to paid item");
            
        }
      this.populateTable();  
    }//GEN-LAST:event_paymentBtnActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String invoice = (String)this.jTable1.getValueAt(jTable1.getSelectedRow(), 1);
        int result = this.db.deletecreditTrans(invoice);
         
        if(result == 1)
           JOptionPane.showMessageDialog(this, "Successfully deleted credit sales");
        else
           JOptionPane.showMessageDialog(this, "Error: Something went wrong");
        
    this.populateTable();

    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CloseButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField nameTxtField;
    private javax.swing.JButton paymentBtn;
    // End of variables declaration//GEN-END:variables
    private DefaultTableModel model;
    private DBConnection db;
}
