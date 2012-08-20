/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.util.ArrayList;

/**
 *
 * @author Hanzen
 */
public class InventoryData {
    
    private int UPC;
    private String description;
    private double price;
    private double stock;
    
    public InventoryData(int upc, String desc, double price, double stock)
    {
        this.description = desc;
        this.price = price;
        this.UPC = upc;
        this.stock = (double)Math.round(stock * 1000) /1000;
        
    }
    
    public int getUPC()
    { return this.UPC; }
    
    public String getDescription()
    { return this.description;  }
    
    public double getUnitPrice()
    { return this.price;  }
    
    public double getStock()
    {  return this.stock;  }
    
    
    
}
