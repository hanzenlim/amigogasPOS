import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.*;

public class InsertRowJTable{
  public static void main(String[] args) {
  new InsertRowJTable();
  }
  DefaultTableModel model;
  JTable table;
  

  public InsertRowJTable(){
      
  JButton button1= new JButton(); 
  
    button1.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                addData();
                
            }
        });  
  
  
  
  JFrame frame = new JFrame("Inserting rows in the table!");
  JPanel panel = new JPanel();
  String data[][] = {{"Vinod","100"},{"Raju","200"},{"Ranju","300"}};
  String col[] = {"Name","code"};
  model = new DefaultTableModel(data,col);
  table = new JTable(model);
 
  panel.add(table);
  panel.add(button1);
  frame.add(panel);
  frame.setSize(300,300);
  frame.setVisible(true);
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  
  
  public void addData()
  {
       //Insert first position
    model.insertRow(0,new Object[]{"Ranjan","50"});
  //Insert 4 position
    model.insertRow(3,new Object[]{"Amar","600"});
  //Insert last position
  model.insertRow(table.getRowCount(),new Object[]{"Sushil","600"});
  
  
  
  }
}