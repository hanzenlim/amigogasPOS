/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Store;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Hanzen
 */

//contains product catalog, price, inventory

public class ProductCatalog {
    
    private HashMap<Integer,Product> pCatalogMap;
    
    
    //constructor
    //initialize product catalog 
    public ProductCatalog(ArrayList<Product> tempList)
    {
        pCatalogMap = new HashMap<Integer,Product>();
        if(tempList !=null)
        for(Product prod : tempList)
        {
            pCatalogMap.put(prod.getUPC(), prod);
        }
    }
    
    public void addProduct(Product prod, int UPC)
    {
        pCatalogMap.put(UPC, prod);
    }
    
    public Product getProduct(int UPC)
    {
        return pCatalogMap.get(UPC);
    }
    
    public HashMap<Integer, Product> getProductList()
    {
        return pCatalogMap;
    }
    
    public HashMap<Integer, Product> getCatalog()
    {
        return pCatalogMap;
    }
    
    
    
    
}
