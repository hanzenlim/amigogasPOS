/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sampleTest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hanzen
 */
public class test {
    
    
    public static void main(String args[])
    {

        try {
           PrintStream out = new PrintStream(new FileOutputStream("output.txt", true));
           System.setOut(out);
 
        } catch (FileNotFoundException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }
       
         yahjoo b = new yahjoo();
         System.out.println("helfwfeweflo");
    }
    
}
