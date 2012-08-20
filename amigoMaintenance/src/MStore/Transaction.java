/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MStore;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Hanzen
 */
public class Transaction {
    
    private Date date;
    private int tID;
    private Cashier cashierName;
    private ArrayList<Item> itemList;
    private double totalAmount;
    private double amountTendered;
    private double change;
    
    
    public Transaction(int id)
    {
        this.tID = id;
            
        this.date = new Date();
        
        cashierName = new Cashier();
        itemList = new ArrayList<Item>();
        totalAmount=0;
        
        
    }
    
    public void addItem(Item e)
    {
        totalAmount += e.getSubTotal();
        itemList.add(e);
        
    }
    
    public void removeItem(int n)
    {
        Item item=itemList.get(n);
        
        this.totalAmount -= item.getSubTotal();
        
        itemList.remove(n);
    }
    
    public double getTotalAmount()
    {
//        DecimalFormat df = new DecimalFormat("####0.0000");
//        String temp=df.format(subTotal);
//        
//        return Double.valueOf(temp);
        
        return (Math.round( this.totalAmount * 100.00 ) / 100.00);
    }
    
    public void setAmtTendered(double amt)
    {
        this.amountTendered = Math.round( amt * 100.0 ) / 100.0;
    }
    
    public void setTotal(double total)
    {
         this.totalAmount = total;
    }
    
    public void setChange()
    {
        this.change = this.amountTendered - this.totalAmount;
        double finalValue = Math.round( this.change * 100.0 ) / 100.0;
        this.change= finalValue;
        
    }
    
    public double getChange()
    { return this.change;  }
    
    public double getAmtTendered()
    { return this.amountTendered; }
    public String getDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        
        return dateFormat.format(date);
    }
    
    public int getTransID()
    {
        return this.tID;
    }
    
    public ArrayList<Item> getList()
    {
        return this.itemList;
    }
    
    public void setDate(Date date)
    {
         this.date = date;
    }
    
    
}
