/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Maintenance.java
 *
 * Created on Jun 10, 2012, 8:40:15 PM
 */
package GUI;

import mDB.DBConnection;
import mDB.Login;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 *
 * @author Hanzen
 */
public class Maintenance extends javax.swing.JFrame {

    //init Maintenance
    public void initMaintenance()
    {
        //this will attempt connection in database
        //it will also init login
        this.db = new DBConnection();
        
       
        PrintStream out;
        try {
            out = new PrintStream(new FileOutputStream("output.txt", true));
            System.setErr(out);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Maintenance.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    //show the panels
    //status 1 super user, 2, manager, 3 cashier
    public void showPanels(boolean value, int status)
    {
        this.jMenuBar1.setVisible(value);
        this.jLayeredPane1.setVisible(value);
        
        if(status==3){
            this.managerMenu.setVisible(false);   
            //this.genReportMenu.setVisible(false);
        }
    }
    //hide all panel
    public void hideAllMenuItemPanel()
    {
       this.addItemPanel1.setVisible(false);
       this.inventoryPanel1.setVisible(false);
       this.changePricePanel1.setVisible(false);
       this.deliveryPanel1.setVisible(false);
       this.endOFTheDay1.setVisible(false);
       this.report1.setVisible(false);
       this.pumpReading1.setVisible(false);
       this.saleTransaction1.setVisible(false);
       this.viewDelivery1.setVisible(false);
       this.collections1.setVisible(false);
       this.payments1.setVisible(false);
       this.editItemPanel1.setVisible(false);
       
    }
    
    //init username and password
     public void initUsernameDialog()
    {
        this.passwordField.setText("");
        
        usernamePanel.setLayout(new GridLayout(2,2));
        usernamePanel.add(usernameLabel);
        usernamePanel.add(username);
        usernamePanel.add(passwordLabel);
        usernamePanel.add(passwordField);
      
        //show username/password dialog
        int input = JOptionPane.showConfirmDialog(this, usernamePanel, "Enter your password:"
                            ,JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
            
        String inputUser = username.getText();
        String inputPass = String.copyValueOf(passwordField.getPassword());
        if (input==0)
        {
            int result = this.db.initLoginData(inputUser, inputPass);
            
            if(result==0){
                 JOptionPane.showMessageDialog(this, "Incorrect Username/Password");
                     this.initUsernameDialog();
            }
            //1 means super user, 2 manager, 3 cashier
            else if(result==1){
                this.showPanels(true,1);
            }
            else if(result==2){
                this.showPanels(true,2);
            }
            else if(result==3){
                this.showPanels(true,3);
            }
            else if(result==-5){
                 JOptionPane.showMessageDialog(this, "Incorrect Username/Password");
                     this.initUsernameDialog();
            }
          
        }
        else
        {
            this.showPanels(false, 0);
            System.exit(1);
        }     
    }
     
    
    /** Creates new form Maintenance */
    public Maintenance() {
        initComponents();
        
        this.setVisible(true);
        
        this.showPanels(false,0);
        
        this.hideAllMenuItemPanel();
        
        this.initMaintenance();
        
        this.initUsernameDialog();
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        pumpReading1 = new GUI.PumpReading();
        editItemPanel1 = new GUI.EditItemPanel();
        addItemPanel1 = new GUI.AddItemPanel();
        inventoryPanel1 = new GUI.InventoryPanel();
        viewDelivery1 = new GUI.ViewDelivery();
        changePricePanel1 = new GUI.ChangePricePanel();
        endOFTheDay1 = new GUI.EndOFTheDay();
        payments1 = new GUI.Payments();
        report1 = new GUI.Report();
        saleTransaction1 = new GUI.SaleTransaction();
        deliveryPanel1 = new GUI.DeliveryPanel();
        collections1 = new GUI.Collections();
        jPanel1 = new javax.swing.JPanel();
        LogoLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        ExitMenu = new javax.swing.JMenuItem();
        AddInvenMenu = new javax.swing.JMenu();
        AddItemMenu = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        ChangePriceMenu = new javax.swing.JMenuItem();
        saleTransMenu = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        InventoryMenu = new javax.swing.JMenuItem();
        AddIventoryMenu = new javax.swing.JMenuItem();
        viewDeliveryMenu = new javax.swing.JMenuItem();
        reportMenu = new javax.swing.JMenu();
        endOfTheDayMenu = new javax.swing.JMenuItem();
        genReportMenu = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        arMenu = new javax.swing.JMenuItem();
        managerMenu = new javax.swing.JMenu();
        regenEODmenu = new javax.swing.JMenuItem();
        editPumpReadingMenu = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLayeredPane1.setBackground(new java.awt.Color(0, 204, 51));
        pumpReading1.setBounds(190, 0, 629, 633);
        jLayeredPane1.add(pumpReading1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        editItemPanel1.setBounds(220, 0, 524, 595);
        jLayeredPane1.add(editItemPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        addItemPanel1.setBounds(0, 0, 794, 553);
        jLayeredPane1.add(addItemPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        inventoryPanel1.setBounds(0, 0, 869, 638);
        jLayeredPane1.add(inventoryPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        viewDelivery1.setBounds(0, 0, 571, 651);
        jLayeredPane1.add(viewDelivery1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        changePricePanel1.setBounds(0, 0, 832, 633);
        jLayeredPane1.add(changePricePanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        endOFTheDay1.setBounds(0, 0, 791, 623);
        jLayeredPane1.add(endOFTheDay1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        payments1.setBounds(0, 0, 994, 622);
        jLayeredPane1.add(payments1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        report1.setBounds(0, 0, 791, 617);
        jLayeredPane1.add(report1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        saleTransaction1.setBounds(0, 0, 1004, 593);
        jLayeredPane1.add(saleTransaction1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        deliveryPanel1.setBounds(0, 0, 795, 603);
        jLayeredPane1.add(deliveryPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        collections1.setBounds(0, 0, 973, 651);
        jLayeredPane1.add(collections1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jPanel1.setBackground(new java.awt.Color(102, 102, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        LogoLabel.setBackground(new java.awt.Color(255, 0, 0));
        LogoLabel.setFont(new java.awt.Font("Lucida Sans Typewriter", 1, 34)); // NOI18N
        LogoLabel.setForeground(new java.awt.Color(204, 0, 0));
        LogoLabel.setText("AMIGO MAINTENANCE");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(32, Short.MAX_VALUE)
                .add(LogoLabel)
                .add(24, 24, 24))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(LogoLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBounds(310, 280, 400, 86);
        jLayeredPane1.add(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jMenu1.setText("File");

        ExitMenu.setText("Exit");
        ExitMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitMenuActionPerformed(evt);
            }
        });
        jMenu1.add(ExitMenu);

        jMenuBar1.add(jMenu1);

        AddInvenMenu.setText("Maintenance");

        AddItemMenu.setText("Add Item");
        AddItemMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddItemMenuActionPerformed(evt);
            }
        });
        AddInvenMenu.add(AddItemMenu);

        jMenuItem3.setText("Edit Item");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        AddInvenMenu.add(jMenuItem3);

        ChangePriceMenu.setText("Change Price");
        ChangePriceMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChangePriceMenuActionPerformed(evt);
            }
        });
        AddInvenMenu.add(ChangePriceMenu);

        saleTransMenu.setText("Sale Transaction");
        saleTransMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saleTransMenuActionPerformed(evt);
            }
        });
        AddInvenMenu.add(saleTransMenu);

        jMenuBar1.add(AddInvenMenu);

        jMenu3.setText("Inventory");

        InventoryMenu.setText("Inventory");
        InventoryMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InventoryMenuActionPerformed(evt);
            }
        });
        jMenu3.add(InventoryMenu);

        AddIventoryMenu.setText("Add Inventory");
        AddIventoryMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddIventoryMenuActionPerformed(evt);
            }
        });
        jMenu3.add(AddIventoryMenu);

        viewDeliveryMenu.setText("View Delivery");
        viewDeliveryMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewDeliveryMenuActionPerformed(evt);
            }
        });
        jMenu3.add(viewDeliveryMenu);

        jMenuBar1.add(jMenu3);

        reportMenu.setText("Report");
        reportMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportMenuActionPerformed(evt);
            }
        });

        endOfTheDayMenu.setText("EndOfTheDay");
        endOfTheDayMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endOfTheDayMenuActionPerformed(evt);
            }
        });
        reportMenu.add(endOfTheDayMenu);

        genReportMenu.setText("Generate Report");
        genReportMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genReportMenuActionPerformed(evt);
            }
        });
        reportMenu.add(genReportMenu);

        jMenuItem1.setText("Pump Reading");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        reportMenu.add(jMenuItem1);

        jMenuBar1.add(reportMenu);

        jMenu2.setText("Customer");

        jMenuItem2.setText("Payments");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        arMenu.setText("Collections");
        arMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arMenuActionPerformed(evt);
            }
        });
        jMenu2.add(arMenu);

        jMenuBar1.add(jMenu2);

        managerMenu.setText("Manager");

        regenEODmenu.setText("Re-generate End Of The Day");
        regenEODmenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regenEODmenuActionPerformed(evt);
            }
        });
        managerMenu.add(regenEODmenu);

        editPumpReadingMenu.setText("Edit Pump Reading");
        editPumpReadingMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPumpReadingMenuActionPerformed(evt);
            }
        });
        managerMenu.add(editPumpReadingMenu);

        jMenuBar1.add(managerMenu);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLayeredPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 998, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLayeredPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 645, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void AddItemMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddItemMenuActionPerformed
// TODO add your handling code here:  
    this.hideAllMenuItemPanel();
    
    this.addItemPanel1.initaddItemPanel(this.db);
    
    this.addItemPanel1.setVisible(true); 
}//GEN-LAST:event_AddItemMenuActionPerformed

private void InventoryMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InventoryMenuActionPerformed
// TODO add your handling code here:
   
    this.hideAllMenuItemPanel();
    
    this.inventoryPanel1.initInventoryPanel(this.db);
    this.inventoryPanel1.populateJTable();
   
    this.inventoryPanel1.setVisible(true);
}//GEN-LAST:event_InventoryMenuActionPerformed

private void ExitMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitMenuActionPerformed
// TODO add your handling code here:
    
    //close all DB connection 
    
    System.exit(1);
}//GEN-LAST:event_ExitMenuActionPerformed

private void ChangePriceMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChangePriceMenuActionPerformed
// TODO add your handling code here:
     
    this.hideAllMenuItemPanel();
    this.changePricePanel1.initChangePricePanel(this.db);
    this.changePricePanel1.populateJTable();
    
    this.changePricePanel1.setVisible(true);
    
}//GEN-LAST:event_ChangePriceMenuActionPerformed

private void AddIventoryMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddIventoryMenuActionPerformed
// TODO add your handling code here:
      
    this.hideAllMenuItemPanel();
    this.deliveryPanel1.initDeliveryPanel(this.db);
    
    this.deliveryPanel1.setVisible(true);

}//GEN-LAST:event_AddIventoryMenuActionPerformed

private void endOfTheDayMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endOfTheDayMenuActionPerformed
// TODO add your handling code here:
    this.hideAllMenuItemPanel();
    this.endOFTheDay1.initEndOfTheDay(this.db, 0);
    this.endOFTheDay1.setVisible(true);
    
}//GEN-LAST:event_endOfTheDayMenuActionPerformed

private void genReportMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genReportMenuActionPerformed
// TODO add your handling code here:
    this.hideAllMenuItemPanel();
    this.report1.initReport(this.db);
    this.report1.setVisible(true);
}//GEN-LAST:event_genReportMenuActionPerformed

private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
// TODO add your handling code here:
    this.hideAllMenuItemPanel();
    this.pumpReading1.initPumpReading(this.db, 0);
    this.pumpReading1.setVisible(true);
}//GEN-LAST:event_jMenuItem1ActionPerformed

private void saleTransMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saleTransMenuActionPerformed
// TODO add your handling code here:
   this.hideAllMenuItemPanel();
   this.saleTransaction1.initSaleTransaction(this.db);
   this.saleTransaction1.setVisible(true);
}//GEN-LAST:event_saleTransMenuActionPerformed

private void viewDeliveryMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewDeliveryMenuActionPerformed
// TODO add your handling code here:
    this.hideAllMenuItemPanel();
   this.viewDelivery1.initViewDelivery(this.db);
   this.viewDelivery1.setVisible(true);
}//GEN-LAST:event_viewDeliveryMenuActionPerformed

    private void regenEODmenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regenEODmenuActionPerformed
        // TODO add your handling code here:
    this.hideAllMenuItemPanel();
    this.endOFTheDay1.initEndOfTheDay(this.db, 1);
    this.endOFTheDay1.setVisible(true);
    }//GEN-LAST:event_regenEODmenuActionPerformed

    private void editPumpReadingMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPumpReadingMenuActionPerformed
        // TODO add your handling code here:
         this.hideAllMenuItemPanel();
        this.pumpReading1.initPumpReading(this.db, 1);
         this.pumpReading1.setVisible(true);
    }//GEN-LAST:event_editPumpReadingMenuActionPerformed

    private void reportMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reportMenuActionPerformed

    private void arMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arMenuActionPerformed
        // TODO add your handling code here:
        this.hideAllMenuItemPanel();
        this.collections1.initAccountReceivable(this.db);
        this.collections1.setVisible(true);
    }//GEN-LAST:event_arMenuActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
         this.hideAllMenuItemPanel();
        this.payments1.initPayments(this.db);
        this.payments1.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        this.hideAllMenuItemPanel();
        this.editItemPanel1.initEditItem(this.db);
        this.editItemPanel1.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Maintenance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Maintenance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Maintenance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Maintenance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Maintenance().setVisible(true);
             
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu AddInvenMenu;
    private javax.swing.JMenuItem AddItemMenu;
    private javax.swing.JMenuItem AddIventoryMenu;
    private javax.swing.JMenuItem ChangePriceMenu;
    private javax.swing.JMenuItem ExitMenu;
    private javax.swing.JMenuItem InventoryMenu;
    private javax.swing.JLabel LogoLabel;
    private GUI.AddItemPanel addItemPanel1;
    private javax.swing.JMenuItem arMenu;
    private GUI.ChangePricePanel changePricePanel1;
    private GUI.Collections collections1;
    private GUI.DeliveryPanel deliveryPanel1;
    private GUI.EditItemPanel editItemPanel1;
    private javax.swing.JMenuItem editPumpReadingMenu;
    private GUI.EndOFTheDay endOFTheDay1;
    private javax.swing.JMenuItem endOfTheDayMenu;
    private javax.swing.JMenuItem genReportMenu;
    private GUI.InventoryPanel inventoryPanel1;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JMenu managerMenu;
    private GUI.Payments payments1;
    private GUI.PumpReading pumpReading1;
    private javax.swing.JMenuItem regenEODmenu;
    private GUI.Report report1;
    private javax.swing.JMenu reportMenu;
    private javax.swing.JMenuItem saleTransMenu;
    private GUI.SaleTransaction saleTransaction1;
    private GUI.ViewDelivery viewDelivery1;
    private javax.swing.JMenuItem viewDeliveryMenu;
    // End of variables declaration//GEN-END:variables

    //added variable 
    
    //for username and password dialog 
    private JPanel usernamePanel = new JPanel();
    private JLabel usernameLabel = new JLabel("Username:");
    private JLabel passwordLabel = new JLabel("Password:");
    private JTextField username = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    
    
    //for DB
    private DBConnection db;
    
}
