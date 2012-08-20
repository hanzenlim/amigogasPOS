
package GUI;


import DB.ConnectToDB;
import GUI.EnterAmount;
import GUI.KeyBindingEvent;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import Store.*;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author OEM
 */
public class Sale extends javax.swing.JFrame {
   //set visible to false
    public void showPanels(boolean value)
    {
        if(!value)
        {
        this.OverallPanel.setVisible(false);
        }
        else
        {
        this.OverallPanel.setVisible(true);
        }
            
    }
    
    public void initStore()
    {

        this.db = new ConnectToDB();
       
        loginData = new Login("test", "test");
        
        //gets the unit price from DB
        ArrayList<Product> temp = this.db.getProdCatalog();
        if(temp == null)
        {
            JOptionPane.showMessageDialog(this, "Product Catalog is still empty please add items using Amigo Maintenance");
            System.exit(1);
        }
        this.pCatalog = new ProductCatalog(this.db.getProdCatalog());
        
        
        //shows the username dialog and checks for username/password
        this.initUsernameDialog();
        
        //add product data to lookUpForm
      
        //this.lookUpForm.populateDataJTable(this.pCatalog.getCatalog());
        
        //init new transaction
        this.newTransaction(this.db.getLastKeyInserted() + 1);
        
        //clears the Jtable list
        this.initJTableList();
        
        this.setColJTable();
        
        this.resetTransaction();
        
        //this.addKeyEvent();
        
        this.keyBind = new KeyBindingEvent(this);
        
        this.keyBind.addKeyEvent();
       
    }
    
    public void addKeyEvent()
    {
        //for action command, use in keybinding
        enterAction = new GUI.Sale.EnterAction();
        
        KeyStroke ctrlAKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_A,
       InputEvent.CTRL_DOWN_MASK);
        
        this.D1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( ctrlAKeyStroke,
                "doEnterAction" );
        this.D1.getActionMap().put( "doEnterAction", enterAction );
    }
    
//    public class EnterAction extends AbstractAction {
//    
//        public pressedAction(String text, String desc)
//        {  
//           
//           super(text);
//            
//           putValue(SHORT_DESCRIPTION, desc);
//           //putValue(MNEMONIC_KEY, mnemonic);
//       }
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        //displayResult("Action for first button/menu item", e);
//        System.out.println("HAHAHAHAHA");
//    }
//}
    class EnterAction extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            // provides feedback to the console to show that the enter key has
            // been pressed
            
            
            // pressing the enter key then 'presses' the enter button by calling
            // the button's doClick() method
            LookUpButton.doClick();
  
        } // end method actionPerformed()
        
    } // end class EnterAction
    
    
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
            //incorrect password
             if(result==0 || result==-5){
                 JOptionPane.showMessageDialog(this, "Incorrect Username/Password");
                     this.initUsernameDialog();
            }
            //1 means super user, 2 manager, 3 cashier
            else 
                this.showPanels(true); 
           
        }
        else
        {
            this.showPanels(false);
            System.exit(1);
        }     
    }
    
    //showing quantity dialog box 
    public int quantityDialogBox(String msg)
    {    
        int quantity = 0;
        boolean isNumber=false;
        while(!isNumber)
        {
           try
           {           
            String s = (String)JOptionPane.showInputDialog(this,"Enter Quantity for " + msg,"Enter Quantity",
            JOptionPane.PLAIN_MESSAGE, null, null, "");
            
            //it means user click on cancel
            if(s == null)
                return -78;
            else
            {
              quantity = Integer.valueOf(s);
              isNumber = true; 
              
              //check if qty is negative
              if(quantity < 1)
              {
                  JOptionPane.showMessageDialog(this, "Please enter quantity greater than 0");
                  isNumber =false;
              }
        
            }
           } catch (NumberFormatException e){
                JOptionPane.showMessageDialog(this, "Not a number, Please input a number");
           }
        }

    return quantity;
    }
    
    //show enterAmount Dialog
    public double enterAmountDialog(String text)
    {
      boolean nonZero=false;
      double doubleAmt=-1;
      double result =-78;
      
      while(!nonZero )
      {

         nonZero=false;
         
         EnterAmount dialog = new EnterAmount(this, true, text);
         dialog.setVisible(true);
      
         System.out.println(dialog.getReturnStatus());
        //if dialog inputs ok
        if(dialog.getReturnStatus()==1)
        {     
          try{
               String amt = dialog.getInputAmt();
               doubleAmt = Double.valueOf(amt);
               
               if(doubleAmt > 0)
                 nonZero=true;
               else
                 JOptionPane.showMessageDialog(this, "Cannot be zero or negative number"); 

       
          }catch(NumberFormatException e) {
             JOptionPane.showMessageDialog(this, "Not a number, Please input a number"); 
          }
        }
        else
            return -78;
      }
      result=doubleAmt;
     return result;

    }
    
    
    //init JTable
    public void initJTableList()
    {
        
         String data[][] = null;
         String col[] = {"Qty","UPC", "Description", "UPrice", "Gross Sale", "12% Vat", "Total"};
         model= new DefaultTableModel(data,col)
         {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
         
        this.ListItemTable.setModel(model);
    
    }
    
    //add row to JTable
    public void addDataJTableList(Item item)
    {
      double vat = item.getSubTotal() -(item.getSubTotal() / 1.12);
      vat =Math.round( vat * 100.0 ) / 100.0;
      double gross=item.getSubTotal()-vat;
      this.model.insertRow(this.ListItemTable.getRowCount(),new Object[]{ item.getQuantity(), item.getUPC(), item.getDescription(), item.getUnitPrice(), 
          Math.round(gross * 100.0)/ 100.0 ,vat, item.getSubTotal()});
   
      this.model.fireTableDataChanged();
      
      //scrolls the JTable at the bottom
      //source 
      //http://stackoverflow.com/questions/5956603/jtable-autoscrolling-to-bottom-problem
      Rectangle rect = this.ListItemTable.getCellRect(this.ListItemTable.getRowCount(), 0, true);
      Rectangle r2 = this.ListItemTable.getVisibleRect();
      
      this.ListItemTable.scrollRectToVisible(new Rectangle(rect.x, rect.y, (int) r2.getWidth(), (int) r2.getHeight()));
      
      //update total label
      this.updateTotalPanel();
    }
   
    
    //delete row to JTable
    public void removeDataJTableList(int row)
    {
        this.model.removeRow(row);
        this.updateTotalPanel();
    }
   
    
    //sets the column width for JTable
    public void setColJTable()
    {
        TableColumn column = null;
        for (int i = 0; i < 5; i++) {
            column = this.ListItemTable.getColumnModel().getColumn(i);
           if (i == 0) {
                column.setPreferredWidth(70); //third column is bigger
           }
           else if (i==1)
               column.setPreferredWidth(60);
           else if(i==2)
                column.setPreferredWidth(120);  
            else if(i==3)
               column.setPreferredWidth(60);
            else if(i==4)
               column.setPreferredWidth(110);
           else if(i==5)
               column.setPreferredWidth(110);
            else if(i==6)
               column.setPreferredWidth(200);
           else {
                column.setPreferredWidth(130);
            }
        }
    }
    
    
    //new Transaction
    public void newTransaction(int id)
    { 
        this.transaction = new Transaction(id);
        this.currDateLabel.setText(this.transaction.getDate());
        this.TransIDLabel.setText(String.valueOf(this.transaction.getTransID()));
      
    }
    
    //update total JLabel
    public void updateTotalPanel()
    {
       this.totalLabel.setText(String.valueOf(this.transaction.getTotalAmount()));
    }
    
    //resetTransaction
    public void resetTransaction()
    {
        this.transaction=null;
        
        //clears total label
        this.AmounTenderedLabel.setText("");
        this.totalLabel.setText("");
        this.ChangeLabel.setText("");
       
        //removes the data in JTable
        while(this.model.getRowCount()!=0)
        {
            this.model.removeRow(0);
        }
  
//        this.model.fireTableDataChanged();
//        this.ListItemTable.repaint();
      
        this.newTransaction(this.db.getLastKeyInserted() + 1);
    }

    
    /**
     * Creates new form Sale
     */
    public Sale() {
        initComponents();
        
         //sets the Frame to visible
        this.setVisible(true);
        
        //initially hides the panel
        this.showPanels(false);
        
        //use for testing only, initialize user and pass
        this.initStore();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        OverallPanel = new javax.swing.JPanel();
        LogoPanel = new javax.swing.JPanel();
        LogoLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        DateLabel = new javax.swing.JLabel();
        TransLabel = new javax.swing.JLabel();
        currDateLabel = new javax.swing.JLabel();
        TransIDLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        InfoPanel = new javax.swing.JPanel();
        TotalPanel = new javax.swing.JPanel();
        totalLabel = new javax.swing.JLabel();
        AmountTenderedPanel = new javax.swing.JPanel();
        AmounTenderedLabel = new javax.swing.JLabel();
        ChangePanel = new javax.swing.JPanel();
        ChangeLabel = new javax.swing.JLabel();
        GasolinePanel = new javax.swing.JPanel();
        D1 = new javax.swing.JButton();
        D2 = new javax.swing.JButton();
        D3 = new javax.swing.JButton();
        D4 = new javax.swing.JButton();
        U1 = new javax.swing.JButton();
        U2 = new javax.swing.JButton();
        U3 = new javax.swing.JButton();
        U4 = new javax.swing.JButton();
        R1 = new javax.swing.JButton();
        R2 = new javax.swing.JButton();
        S1 = new javax.swing.JButton();
        S2 = new javax.swing.JButton();
        MiscPanel = new javax.swing.JPanel();
        voidButton = new javax.swing.JButton();
        LookUpButton = new javax.swing.JButton();
        LogOutButton = new javax.swing.JButton();
        invenBtn = new javax.swing.JButton();
        TotalButton = new javax.swing.JButton();
        creditButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ListItemTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        OverallPanel.setBackground(new java.awt.Color(204, 204, 255));

        LogoPanel.setBackground(new java.awt.Color(255, 255, 51));
        LogoPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        LogoPanel.setForeground(new java.awt.Color(255, 0, 0));

        LogoLabel.setFont(new java.awt.Font("LilyUPC", 1, 60)); // NOI18N
        LogoLabel.setForeground(new java.awt.Color(255, 0, 0));
        LogoLabel.setText("AMIGO GASOLINE");

        jPanel1.setBackground(new java.awt.Color(255, 204, 102));

        DateLabel.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        DateLabel.setForeground(new java.awt.Color(0, 153, 51));
        DateLabel.setText("Date:");

        TransLabel.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        TransLabel.setForeground(new java.awt.Color(0, 153, 51));
        TransLabel.setText("Trans #");

        currDateLabel.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N

        TransIDLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(TransLabel)
                        .addGap(4, 4, 4)
                        .addComponent(TransIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(DateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(currDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(currDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(DateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TransIDLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(TransLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("San Nicolas, Tarlac City");

        javax.swing.GroupLayout LogoPanelLayout = new javax.swing.GroupLayout(LogoPanel);
        LogoPanel.setLayout(LogoPanelLayout);
        LogoPanelLayout.setHorizontalGroup(
            LogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LogoPanelLayout.createSequentialGroup()
                .addGroup(LogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LogoPanelLayout.createSequentialGroup()
                        .addGap(194, 194, 194)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(LogoPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(LogoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(40, 40, 40)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        LogoPanelLayout.setVerticalGroup(
            LogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LogoPanelLayout.createSequentialGroup()
                .addGroup(LogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, LogoPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(LogoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(LogoPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(LogoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(LogoPanelLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(8, 8, 8)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        InfoPanel.setBackground(new java.awt.Color(102, 204, 255));
        InfoPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        TotalPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Total"));

        totalLabel.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        totalLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout TotalPanelLayout = new javax.swing.GroupLayout(TotalPanel);
        TotalPanel.setLayout(TotalPanelLayout);
        TotalPanelLayout.setHorizontalGroup(
            TotalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TotalPanelLayout.createSequentialGroup()
                .addContainerGap(256, Short.MAX_VALUE)
                .addComponent(totalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        TotalPanelLayout.setVerticalGroup(
            TotalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TotalPanelLayout.createSequentialGroup()
                .addComponent(totalLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addContainerGap())
        );

        AmountTenderedPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Amount Tendered"));

        AmounTenderedLabel.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        AmounTenderedLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout AmountTenderedPanelLayout = new javax.swing.GroupLayout(AmountTenderedPanel);
        AmountTenderedPanel.setLayout(AmountTenderedPanelLayout);
        AmountTenderedPanelLayout.setHorizontalGroup(
            AmountTenderedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AmountTenderedPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(AmounTenderedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        AmountTenderedPanelLayout.setVerticalGroup(
            AmountTenderedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AmountTenderedPanelLayout.createSequentialGroup()
                .addComponent(AmounTenderedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        ChangePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Change"));

        ChangeLabel.setFont(new java.awt.Font("Lucida Grande", 0, 30)); // NOI18N
        ChangeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout ChangePanelLayout = new javax.swing.GroupLayout(ChangePanel);
        ChangePanel.setLayout(ChangePanelLayout);
        ChangePanelLayout.setHorizontalGroup(
            ChangePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ChangePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ChangeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );
        ChangePanelLayout.setVerticalGroup(
            ChangePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ChangePanelLayout.createSequentialGroup()
                .addComponent(ChangeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout InfoPanelLayout = new javax.swing.GroupLayout(InfoPanel);
        InfoPanel.setLayout(InfoPanelLayout);
        InfoPanelLayout.setHorizontalGroup(
            InfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InfoPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(InfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TotalPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AmountTenderedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ChangePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        InfoPanelLayout.setVerticalGroup(
            InfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, InfoPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(TotalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AmountTenderedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ChangePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        GasolinePanel.setBackground(new java.awt.Color(255, 102, 102));

        D1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        D1.setText("Diesel ");
        D1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                D1ActionPerformed(evt);
            }
        });

        D2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        D2.setText("Premium");
        D2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                D2ActionPerformed(evt);
            }
        });

        D3.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        D3.setText("Unleaded");
        D3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                D3ActionPerformed(evt);
            }
        });

        D4.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        D4.setText("Regular");
        D4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                D4ActionPerformed(evt);
            }
        });

        U1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        U1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                U1ActionPerformed(evt);
            }
        });

        U2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        U2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                U2ActionPerformed(evt);
            }
        });

        U3.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        U3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                U3ActionPerformed(evt);
            }
        });

        U4.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        U4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                U4ActionPerformed(evt);
            }
        });

        R1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        R1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                R1ActionPerformed(evt);
            }
        });

        R2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        R2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                R2ActionPerformed(evt);
            }
        });

        S1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        S1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S1ActionPerformed(evt);
            }
        });

        S2.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        S2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                S2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout GasolinePanelLayout = new javax.swing.GroupLayout(GasolinePanel);
        GasolinePanel.setLayout(GasolinePanelLayout);
        GasolinePanelLayout.setHorizontalGroup(
            GasolinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GasolinePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(GasolinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(GasolinePanelLayout.createSequentialGroup()
                        .addComponent(D1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(D2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(D3, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(D4, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(GasolinePanelLayout.createSequentialGroup()
                        .addGroup(GasolinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(GasolinePanelLayout.createSequentialGroup()
                                .addComponent(U1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(U2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(U3, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(GasolinePanelLayout.createSequentialGroup()
                                .addComponent(R1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(R2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(S1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(GasolinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(S2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(U4, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        GasolinePanelLayout.setVerticalGroup(
            GasolinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GasolinePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(GasolinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(D1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(D2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(D3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(D4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(GasolinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(U1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(U2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(U3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(U4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(GasolinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(R1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(R2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(S1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(S2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        MiscPanel.setBackground(new java.awt.Color(255, 255, 153));

        voidButton.setText("Void Item (F6)");
        voidButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voidButtonActionPerformed(evt);
            }
        });

        LookUpButton.setText("LookUp Item ( F1 )");
        LookUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LookUpButtonActionPerformed(evt);
            }
        });

        LogOutButton.setText("Log Out");
        LogOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogOutButtonActionPerformed(evt);
            }
        });

        invenBtn.setText("Inventory ( F2 )");
        invenBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invenBtnActionPerformed(evt);
            }
        });

        TotalButton.setText("TOTAL ( F10 )");
        TotalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TotalButtonActionPerformed(evt);
            }
        });

        creditButton.setText("Credit Payment");
        creditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MiscPanelLayout = new javax.swing.GroupLayout(MiscPanel);
        MiscPanel.setLayout(MiscPanelLayout);
        MiscPanelLayout.setHorizontalGroup(
            MiscPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MiscPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MiscPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(voidButton, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(LogOutButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(MiscPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(LookUpButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(invenBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(creditButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(TotalButton, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        MiscPanelLayout.setVerticalGroup(
            MiscPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MiscPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MiscPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MiscPanelLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(MiscPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(invenBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LogOutButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(creditButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1))
                    .addGroup(MiscPanelLayout.createSequentialGroup()
                        .addComponent(voidButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(45, 45, 45))
                    .addComponent(TotalButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(MiscPanelLayout.createSequentialGroup()
                        .addComponent(LookUpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        ListItemTable.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        jScrollPane1.setViewportView(ListItemTable);

        javax.swing.GroupLayout OverallPanelLayout = new javax.swing.GroupLayout(OverallPanel);
        OverallPanel.setLayout(OverallPanelLayout);
        OverallPanelLayout.setHorizontalGroup(
            OverallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OverallPanelLayout.createSequentialGroup()
                .addGroup(OverallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(GasolinePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LogoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(MiscPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(OverallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(InfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)))
        );
        OverallPanelLayout.setVerticalGroup(
            OverallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OverallPanelLayout.createSequentialGroup()
                .addGroup(OverallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(OverallPanelLayout.createSequentialGroup()
                        .addComponent(LogoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(GasolinePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(MiscPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 75, Short.MAX_VALUE))
                    .addGroup(OverallPanelLayout.createSequentialGroup()
                        .addComponent(InfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(OverallPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(OverallPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void D1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_D1ActionPerformed
        // TODO add your handling code here:
    Product prod = this.pCatalog.getProduct(bD1);
   if(prod == null)
   {JOptionPane.showMessageDialog(this, "This button hasn't been added in Product Catalog, Please add it using add items in Amigo Maintenance");
     return;
   }
   double n;
   n=this.enterAmountDialog("Diesel");
   
   if(n!=-78)
   {
      
      Item item = new Item(prod, n);
      this.transaction.addItem(item); 
      
      this.addDataJTableList(item);
   }
    }//GEN-LAST:event_D1ActionPerformed

    private void D2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_D2ActionPerformed
        // TODO add your handling code here:
        Product prod = this.pCatalog.getProduct(bD2);
   if(prod == null)
   {
     JOptionPane.showMessageDialog(this, "This button hasn't been added in Product Catalog, Please add it using add items in Amigo Maintenance");
     return;
   }
   
   double n;
   n=this.enterAmountDialog("Premium");
   
   if(n!=-78)
   {
      Item item = new Item(prod, n);
      this.transaction.addItem(item); 
      this.addDataJTableList(item);
   }
    }//GEN-LAST:event_D2ActionPerformed

    private void D3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_D3ActionPerformed
        // TODO add your handling code here:
        Product prod = this.pCatalog.getProduct(bD3);
   if(prod == null)
   {
     JOptionPane.showMessageDialog(this, "This button hasn't been added in Product Catalog, Please add it using add items in Amigo Maintenance");
     return;
   }
   double n;
   n=this.enterAmountDialog("Unleaded");
   
   if(n!=-78)
   {
      
      Item item = new Item(prod, n);
      this.transaction.addItem(item); 
      
      this.addDataJTableList(item);
   }
    }//GEN-LAST:event_D3ActionPerformed

    private void D4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_D4ActionPerformed
        // TODO add your handling code here:
        Product prod = this.pCatalog.getProduct(bD4);
   if(prod == null)
   {
     JOptionPane.showMessageDialog(this, "This button hasn't been added in Product Catalog, Please add it using add items in Amigo Maintenance");
     return;
   }
   
   double n;
   n=this.enterAmountDialog("Regular");
   
   if(n!=-78)
   {
      
      Item item = new Item(prod, n);
      this.transaction.addItem(item); 
      
      this.addDataJTableList(item);
   }
    }//GEN-LAST:event_D4ActionPerformed

    private void U1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_U1ActionPerformed
        // TODO add your handling code here:
//        Product prod = this.pCatalog.getProduct(bU1);
//   if(prod == null)
//   {
//     JOptionPane.showMessageDialog(this, "This button hasn't been added in Product Catalog, Please add it using add items in Amigo Maintenance");
//     return;
//   }
//   
//   double n;
//   n=this.enterAmountDialog("Premium");
//   
//   if(n!=-78)
//   {
//      
//      Item item = new Item(prod, n);
//      this.transaction.addItem(item); 
//      
//      this.addDataJTableList(item);
//   }
    }//GEN-LAST:event_U1ActionPerformed

    private void U2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_U2ActionPerformed
        // TODO add your handling code here:
//        Product prod = this.pCatalog.getProduct(bU2);
//   if(prod == null)
//   {
//     JOptionPane.showMessageDialog(this, "This button hasn't been added in Product Catalog, Please add it using add items in Amigo Maintenance");
//     return;
//   }
//   
//   double n;
//   n=this.enterAmountDialog("Premium SP");
//   
//   if(n!=-78)
//   {
//      
//      Item item = new Item(prod, n);
//      this.transaction.addItem(item); 
//      
//      this.addDataJTableList(item);
//   }
    }//GEN-LAST:event_U2ActionPerformed

    private void U3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_U3ActionPerformed
        // TODO add your handling code here:
//        Product prod = this.pCatalog.getProduct(bU3);
//   if(prod == null)
//   {
//     JOptionPane.showMessageDialog(this, "This button hasn't been added in Product Catalog, Please add it using add items in Amigo Maintenance");
//     return;
//   }
//   
//   double n;
//   n=this.enterAmountDialog("Regular");
//   
//   if(n!=-78)
//   {
//      
//      Item item = new Item(prod, n);
//      this.transaction.addItem(item); 
//      
//      this.addDataJTableList(item);
//   }
    }//GEN-LAST:event_U3ActionPerformed

    private void U4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_U4ActionPerformed
        // TODO add your handling code here:
//        Product prod = this.pCatalog.getProduct(bU4);
//   if(prod == null)
//   {
//     JOptionPane.showMessageDialog(this, "This button hasn't been added in Product Catalog, Please add it using add items in Amigo Maintenance");
//     return;
//   }
//   
//   double n;
//   n=this.enterAmountDialog("Regular SP");
//   
//   if(n!=-78)
//   {
//      
//      Item item = new Item(prod, n);
//      this.transaction.addItem(item); 
//      
//      this.addDataJTableList(item);
//   }
    }//GEN-LAST:event_U4ActionPerformed

    private void R1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_R1ActionPerformed
        // TODO add your handling code here:
//        Product prod = this.pCatalog.getProduct(bR1);
//   if(prod == null)
//   {
//     JOptionPane.showMessageDialog(this, "This button hasn't been added in Product Catalog, Please add it using add items in Amigo Maintenance");
//     return;
//   }
//   
//   double n;
//   n=this.enterAmountDialog("Kerosene");
//   
//   if(n!=-78)
//   {
//      
//      Item item = new Item(prod, n);
//      this.transaction.addItem(item); 
//      
//      this.addDataJTableList(item);
//   }
    }//GEN-LAST:event_R1ActionPerformed

    private void R2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_R2ActionPerformed
        // TODO add your handling code here:
//                Product prod = this.pCatalog.getProduct(bR2);
//   if(prod == null)
//   {
//     JOptionPane.showMessageDialog(this, "This button hasn't been added in Product Catalog, Please add it using add items in Amigo Maintenance");
//     return;
//   }
//   
//   double n;
//   n=this.enterAmountDialog("Kerosene");
//   
//   if(n!=-78)
//   {
//      
//      Item item = new Item(prod, n);
//      this.transaction.addItem(item); 
//      
//      this.addDataJTableList(item);
//   }
    }//GEN-LAST:event_R2ActionPerformed

    private void S1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S1ActionPerformed
        // TODO add your handling code here:
//                Product prod = this.pCatalog.getProduct(bS1);
//   if(prod == null)
//   {
//     JOptionPane.showMessageDialog(this, "This button hasn't been added in Product Catalog, Please add it using add items in Amigo Maintenance");
//     return;
//   }
//   
//   double n;
//   n=this.enterAmountDialog("Kerosene");
//   
//   if(n!=-78)
//   {
//      
//      Item item = new Item(prod, n);
//      this.transaction.addItem(item); 
//      
//      this.addDataJTableList(item);
//   }
    }//GEN-LAST:event_S1ActionPerformed

    private void S2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_S2ActionPerformed
        // TODO add your handling code here:
//             Product prod = this.pCatalog.getProduct(bS2);
//   if(prod == null)
//   {
//     JOptionPane.showMessageDialog(this, "This button hasn't been added in Product Catalog, Please add it using add items in Amigo Maintenance");
//     return;
//   }
//   
//   double n;
//   n=this.enterAmountDialog("Kerosene");
//   
//   if(n!=-78)
//   {
//      
//      Item item = new Item(prod, n);
//      this.transaction.addItem(item); 
//      
//      this.addDataJTableList(item);
//   }
    }//GEN-LAST:event_S2ActionPerformed

    private void TotalButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TotalButtonActionPerformed
        // TODO add your handling code here:
     boolean nonZero = false;
     double amtTendered = 0;
    
    while(!nonZero)
    {
     if(this.transaction!=null && this.transaction.getList().size()!=0)
     {
         SubTotalDialog dialog = new SubTotalDialog(this,true,String.valueOf(transaction.getTotalAmount()));
         dialog.setVisible(true);
         if(dialog.getReturnStatus()==1)
         {
             try{
             amtTendered = Double.valueOf(dialog.getAmtTendered());
             
             if(amtTendered >= this.transaction.getTotalAmount())
                 nonZero=true;
               else
                 JOptionPane.showMessageDialog(this, "Amount Tendered cannot be less than Total Amount"); 
             
             }catch(NumberFormatException e){
                 JOptionPane.showMessageDialog(this, "Error: Please enter a number"); 
             }
         }
         else
            return;
             
     }
     else 
         return;
    }
    
    //saving transaction    
    this.transaction.setAmtTendered(amtTendered);
    this.transaction.setChange();

    //sets the amt Tendered label
    this.AmounTenderedLabel.setText(String.valueOf(amtTendered));

    //sets the change label
    this.ChangeLabel.setText(String.valueOf(this.transaction.getChange()));

    //saving transaction to DB
    int result =this.db.insertTransaction(this.transaction);

    if(result == -78) 
        JOptionPane.showMessageDialog(this,"Something Went Wrong, Unable to save transaction");
    else if(result ==1){
        ChangeDialog change = new ChangeDialog(this, true, transaction.getChange());
        change.setVisible(true);
        
    }

    //needs to show message first before resetting transaction or else won't see the amount tndered
    //and change label
    this.resetTransaction();  
    
    }//GEN-LAST:event_TotalButtonActionPerformed

    private void voidButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voidButtonActionPerformed
        // TODO add your handling code here:
         VoidLookUp voidlookup = new VoidLookUp(this, true, this.model);
    voidlookup.setVisible(true);
    
    //means dialog entered yes
    if(voidlookup.getReturnStatus()==1)
    {
        if(voidlookup.getSelectedItem() != -1)
        {
        this.transaction.removeItem(voidlookup.getSelectedItem());
        this.removeDataJTableList(voidlookup.getSelectedItem());

        }
        else
            JOptionPane.showMessageDialog(this, "No Items Deleted");
    }
 
    }//GEN-LAST:event_voidButtonActionPerformed

    private void LookUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LookUpButtonActionPerformed
        // TODO add your handling code here:
        // this.lookUp.populateDataJTable(this.pCatalog.getCatalog());
      this.lookUp = new LookUp(this, true, this.pCatalog.getCatalog() );
      this.lookUp.setVisible(true);
      if(lookUp.getReturnStatus()==1)
      {
         Object[] possibilities = {"Quantity", "Amount"};
           String s = (String)JOptionPane.showInputDialog(this,"Please choose if you want to Enter Amount or Quantity",
                 "Dialog",JOptionPane.PLAIN_MESSAGE,null, possibilities,"Quantity");
      
        //If a string was returned, say so.
        if ((s != null) && (s.length() > 0)) {
            //if user choose to input quantity
            if(s.equals("Quantity"))
            {
               int x=this.lookUp.getSelectedItem();

                //returns -78 if nothing is selected
                if(x!=-78)
                {
                    Product prod = this.pCatalog.getProduct(x);

                    int qty=this.quantityDialogBox(prod.getDesciption());

                    //if cancel was click in quantity dialog box
                    if(qty==-78)
                        return;

                    //adds to transaction
                    Item item = new Item(prod, qty);
                    this.transaction.addItem(item);

                    //addds to JTable List
                    this.addDataJTableList(item);
                }
            }
            else if(s.equals("Amount"))
            {
                int upc=this.lookUp.getSelectedItem();
                Product prod = this.pCatalog.getProduct(upc);
                if(prod == null)
                {
                    JOptionPane.showMessageDialog(this, "This button hasn't been added in Product Catalog, Please add it using add items in Amigo Maintenance");
                    return;
                }
                double p=this.enterAmountDialog(prod.getDesciption());
                if(p!=-78)
                 {
                    Item item = new Item(prod, p);
                    this.transaction.addItem(item); 
      
                     this.addDataJTableList(item);
                 }
            }
            
        }
        //if choosing quantity or amount was canceled
        else
            return;
     
      }
      //if LookUp was cancel
      else
          return;
     
    }//GEN-LAST:event_LookUpButtonActionPerformed

    private void LogOutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogOutButtonActionPerformed
        // TODO add your handling code here:
         this.showPanels(false);
    this.initStore();
    
    }//GEN-LAST:event_LogOutButtonActionPerformed

    private void invenBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invenBtnActionPerformed
        // TODO add your handling code here:\
        invenPanel.initInventoryPanel(this.db);
        JOptionPane.showMessageDialog(null, invenPanel);
    }//GEN-LAST:event_invenBtnActionPerformed

    private void creditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditButtonActionPerformed
     boolean nonZero = false;
     double amtTendered = 0;
     CreditTotalDialog dialog;
    
   // while(!nonZero)
    
     if(this.transaction!=null && this.transaction.getList().size()!=0)
     {
         dialog = new CreditTotalDialog(this,true,String.valueOf(transaction.getTotalAmount()));
         dialog.setVisible(true);
         if(dialog.getReturnStatus()==1)
         {
         }
         else
            return;
     }
     else 
         return;
    
    
    //saving transaction    
    this.transaction.setAmtTendered(transaction.getTotalAmount());
    this.transaction.setChange();

    //sets the amt Tendered label
    this.AmounTenderedLabel.setText(String.valueOf(transaction.getTotalAmount()));

    //sets the change label
    this.ChangeLabel.setText(String.valueOf(this.transaction.getChange()));

    //saving transaction to DB
    int result =this.db.insertCreditSales(transaction, dialog.getName() , dialog.getInvoice());

    if(result == -78) {
        JOptionPane.showMessageDialog(this,"Error: Invoice no./Name already exists \r\n Please input a different invoice/name");
        return;
    }
    else if(result ==1){
        JOptionPane.showMessageDialog(this,"Successfully save A/R transaction");
        
    }

    //needs to show message first before resetting transaction or else won't see the amount tndered
    //and change label
    this.resetTransaction();  
    }//GEN-LAST:event_creditButtonActionPerformed

  
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Sale().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AmounTenderedLabel;
    private javax.swing.JPanel AmountTenderedPanel;
    private javax.swing.JLabel ChangeLabel;
    private javax.swing.JPanel ChangePanel;
    protected javax.swing.JButton D1;
    protected javax.swing.JButton D2;
    protected javax.swing.JButton D3;
    protected javax.swing.JButton D4;
    private javax.swing.JLabel DateLabel;
    private javax.swing.JPanel GasolinePanel;
    private javax.swing.JPanel InfoPanel;
    private javax.swing.JTable ListItemTable;
    private javax.swing.JButton LogOutButton;
    private javax.swing.JLabel LogoLabel;
    private javax.swing.JPanel LogoPanel;
    protected javax.swing.JButton LookUpButton;
    private javax.swing.JPanel MiscPanel;
    private javax.swing.JPanel OverallPanel;
    protected javax.swing.JButton R1;
    protected javax.swing.JButton R2;
    protected javax.swing.JButton S1;
    protected javax.swing.JButton S2;
    protected javax.swing.JButton TotalButton;
    private javax.swing.JPanel TotalPanel;
    private javax.swing.JLabel TransIDLabel;
    private javax.swing.JLabel TransLabel;
    protected javax.swing.JButton U1;
    protected javax.swing.JButton U2;
    protected javax.swing.JButton U3;
    protected javax.swing.JButton U4;
    protected javax.swing.JButton creditButton;
    private javax.swing.JLabel currDateLabel;
    protected javax.swing.JButton invenBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel totalLabel;
    protected javax.swing.JButton voidButton;
    // End of variables declaration//GEN-END:variables
     //added private variable
    //for login dialog 
    private JPanel usernamePanel = new JPanel();
    private JLabel usernameLabel = new JLabel("Username:");
    private JLabel passwordLabel = new JLabel("Password:");
    private JTextField username = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private Login loginData;
    
    
    //for JTable, table model
    private DefaultTableModel model;
    
    
    //for ProductCatalog
    private ProductCatalog pCatalog;
    
    //for Transaction
    private Transaction transaction;
    

    //DB
    private ConnectToDB db;
    
    //d1,d2,d3,u1,u2,u3 etc
    private final int bD1=1, bD2=2, bD3=3, bD4=4;
    private final int bU1=5, bU2=6, bU3=7, bU4=8;
    private final int bR1=9, bR2=10, bS1=11, bS2=12;
    
    
    private Action enterAction; 
    
    //key binding
    private KeyBindingEvent keyBind;
    private LookUp lookUp;
    private InventoryPanel invenPanel = new InventoryPanel();
}
