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
 * @author OEM
 */
public class creditData {
    public ArrayList<Array> arrayData=new ArrayList<Array>();
    public Date date;
    public String name;
    public String invoice;
    public double amtcredit;
    public int status;
    public Date datepaid;
    public String info;
    public String time;
    public double total;
    
    
    public creditData()
    {
        
    }
}
