/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import MStore.InventoryData;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import mDB.DBConnection;

/**
 *
 * @author OEM
 */
public class EditItemPanel extends javax.swing.JPanel {

     //init InventoryPanel
    public void initEditItem(DBConnection db)
    {
        this.db = db;
        this.populateJTable();
    }
    //init JTable
    public void initJTableList()
    {
        
         String data[][] = null;
         String col[] = {"UPC", "Description"};
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
   
     //populate JTable with data
    public void populateJTable()
    {
       this.initJTableList(); 
        
       ArrayList<InventoryData> inven = this.db.getInventoryData(); 
        
       if(inven != null)
       {
           java.util.Collections.sort(inven, new MyIntComparable());
       
           for(InventoryData i : inven)
           {
           this.prodMap.put(i.getUPC(), i);  
           this.model.insertRow(this.jTable1.getRowCount(),  
                   new Object[]{i.getUPC(), i.getDescription()});
           }
       }  
    }
    
    //returns the UPC number in JTable
    public int getSelectedItem()
    {
        if(this.jTable1.getSelectedRow() != -1)
        {
            //returns the UPC number
            int a=(Integer)this.jTable1.getValueAt(this.jTable1.getSelectedRow(), 0);
          return a;
        }
    return -78;
    }
    
 
    /**
     * Creates new form EditItemPanel
     */
    public EditItemPanel() {
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        editItemBtn = new javax.swing.JButton();

        setBackground(new java.awt.Color(204, 204, 255));

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        CloseButton.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        CloseButton.setText("Close");
        CloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 255));
        jLabel2.setText("Edit item");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(CloseButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CloseButton))
                .addContainerGap())
        );

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        editItemBtn.setText("Edit Item");
        editItemBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editItemBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(editItemBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(editItemBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void CloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseButtonActionPerformed
// TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_CloseButtonActionPerformed

    private void editItemBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editItemBtnActionPerformed
    // TODO add your handling code here:
    int n =this.getSelectedItem();
    int ans;
    
    if(n==-78)
        JOptionPane.showMessageDialog(this, "Please select an item");
    else
    {
        //selected inventory data
        //System.out.println("CHosen:" + n);
        InventoryData temp=this.prodMap.get(n);
        
        this.editItem.setLabel(temp.getDescription());
        //this.setPriceDialog.setLabel(temp.getDescription(), temp.getUnitPrice());
        ans = JOptionPane.showConfirmDialog(null, this.editItem, "Edit Item", JOptionPane.PLAIN_MESSAGE);

        //if set price dialog is yes
        if(ans == JOptionPane.YES_OPTION)
        {
            //int x=this.db.setUnitPrice(temp.getUPC(), temp.getUnitPrice(), Double.valueOf(this.editItem.getNewDescription()));
            int x = this.db.setItemDescription(temp.getUPC(), this.editItem.getNewDescription());
            if(x==0)
                JOptionPane.showMessageDialog(this, "Error Occurred: Did not Item Description");
            else
            {
                JOptionPane.showMessageDialog(this, "Successfully change item description");
                this.populateJTable();
            }
        }
    }
    }//GEN-LAST:event_editItemBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CloseButton;
    private javax.swing.JButton editItemBtn;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
    DefaultTableModel model;
    private DBConnection db;
    private HashMap<Integer, InventoryData> prodMap = new HashMap<Integer, InventoryData>();
    private SetEditItemDialog editItem = new SetEditItemDialog();
}
