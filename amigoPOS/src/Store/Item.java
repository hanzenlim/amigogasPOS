/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Store;

import java.text.DecimalFormat;

/**
 *
 * @author Hanzen
 */

//contains the item, used in Transaction
public class Item {
    
    private Product product;
    private String description;
    private double quantity;
    private double unitPrice;
    private double subTotal;
    private int UPC;
    
    public Item(Product p1, double amt)
    {
        this.product = p1;
       // this.quantity = quan;
        
        this.unitPrice = (double)Math.round( p1.getPrice()* 1000 ) / 1000;
        
        this.quantity = amt / this.unitPrice;
        this.quantity =  (double)Math.round( quantity * 1000 ) / 1000;
       // this.quantity =Math.round( this.quantity * 100.0 ) / 100.0;
         
//        DecimalFormat df = new DecimalFormat("####0.0000");
//        String temp=df.format(this.quantity * p1.getPrice());
        
        this.subTotal = amt;
        this.UPC = p1.getUPC();
        this.description = p1.getDesciption();
    }
    
    public Item(Product p1, int quantity)
    {
        this.product = p1;
        this.quantity=quantity;
        this.unitPrice =Math.round( p1.getPrice()* 100.0 ) / 100.0;
        
        this.subTotal = this.unitPrice * this.quantity;
        this.UPC = p1.getUPC();
        this.description = p1.getDesciption();
        
        
    }
    
    //returns the total price for one item
    public double getSubTotal()
    { return subTotal; }
    
    public double getQuantity()
    { return quantity; }
    
    public double getUnitPrice()
    {  return unitPrice; }
    
    public String getDescription()
    {  return description;  }
    
    public int getUPC()
    {  return UPC; }
    
    
    
    
    
}
