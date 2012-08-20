/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mDB;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Hanzen
 */
public class SalesData {
    
    public ArrayList<Array> arrayData=new ArrayList<Array>();
    public Date date;
    public String time;
    public double total;
    public double amttendered;
    public double change;
    public int transID;
    
    public SalesData()
    {
        
    }
    
}
