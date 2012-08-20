/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Store;

/**
 *
 * @author Hanzen
 */

//contains info about product and price
public class Product {
    
    private int UPC;
    private String description;
    private double price;
    
    public Product(String desc, double price, int upc)
    {
        this.description = desc;
        this.price = price;
        this.UPC = upc;
    }
    
    public String getDesciption()
    {
        return description;
    }
    
    public double getPrice()
    {
        return price;
    }
    
    public int getUPC()
    {
        return UPC;
    }
}
