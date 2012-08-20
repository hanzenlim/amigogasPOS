/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

/**
 *
 * @author Hanzen
 */
public class KeyBindingEvent {
    
    private KeyStroke ctrlAKeyStroke;
    private Sale sale;
    private Action lookUp;
    private Action total;
    private Action Void, inven;
    private Action d1,d2,d3,d4;
    private Action u1,u2,u3,u4;
    private Action r1,r2,s1,s2;
   
    public KeyBindingEvent(Sale sale)
    {
        this.sale = sale;   
    }
    
    public void addKeyEvent()
    {
   
        
       //for look up
       lookUp = new LookUpAction();   
       this.sale.LookUpButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"),"doEnterAction" );
       this.sale.LookUpButton.getActionMap().put( "doEnterAction", lookUp );
       
       //for total
       total  = new TotalAction(); 
       this.sale.TotalButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F10"),"total" );
       this.sale.TotalButton.getActionMap().put( "total", total );
       
        //for void
       Void  = new VoidAction(); 
       this.sale.TotalButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( KeyStroke.getKeyStroke("F6"),"void" );
       this.sale.TotalButton.getActionMap().put( "void", Void );
       
        //for inventory
       inven  = new InventoryAction(); 
       this.sale.TotalButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( KeyStroke.getKeyStroke("F2"),"inven" );
       this.sale.TotalButton.getActionMap().put( "inven", inven );
       
       d1 = new D1Action();
       ctrlAKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK);
       this.sale.TotalButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( ctrlAKeyStroke,"d1" );
       this.sale.TotalButton.getActionMap().put( "d1", d1 );
       
        d2 = new D2Action();
       ctrlAKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK);
       this.sale.TotalButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( ctrlAKeyStroke,"d2" );
       this.sale.TotalButton.getActionMap().put( "d2", d2 );
       
        d3 = new D3Action();
       ctrlAKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK);
       this.sale.TotalButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( ctrlAKeyStroke,"d3" );
       this.sale.TotalButton.getActionMap().put( "d3", d3 );
       
        d4 = new D4Action();
       ctrlAKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK);
       this.sale.TotalButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( ctrlAKeyStroke,"d4" );
       this.sale.TotalButton.getActionMap().put( "d4", d4 );
       
        u1 = new U1Action();
       ctrlAKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK);
       this.sale.TotalButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( ctrlAKeyStroke,"u1" );
       this.sale.TotalButton.getActionMap().put( "u1", u1 );
       
        u2 = new U2Action();
       ctrlAKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
       this.sale.TotalButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( ctrlAKeyStroke,"u2" );
       this.sale.TotalButton.getActionMap().put( "u2", u2 );
       
        u3 = new U3Action();
       ctrlAKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK);
       this.sale.TotalButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( ctrlAKeyStroke,"u3" );
       this.sale.TotalButton.getActionMap().put( "u3", u3 );
       
        u4 = new U4Action();
       ctrlAKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK);
       this.sale.TotalButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( ctrlAKeyStroke,"u4" );
       this.sale.TotalButton.getActionMap().put( "u4", u4 );
       
        r1 = new R1Action();
       ctrlAKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
       this.sale.TotalButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( ctrlAKeyStroke,"r1" );
       this.sale.TotalButton.getActionMap().put( "r1", r1 );
       
        r2 = new R2Action();
       ctrlAKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK);
       this.sale.TotalButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( ctrlAKeyStroke,"r2" );
       this.sale.TotalButton.getActionMap().put( "r2", r2 );
       
        s1 = new S1Action();
       ctrlAKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK);
       this.sale.TotalButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( ctrlAKeyStroke,"s1" );
       this.sale.TotalButton.getActionMap().put( "s1", s1 );
       
        s2 = new S2Action();
       ctrlAKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK);
       this.sale.TotalButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( ctrlAKeyStroke,"s2" );
       this.sale.TotalButton.getActionMap().put( "s2", s2 );
       
    }
    
    
    
    class LookUpAction extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
           sale.LookUpButton.doClick();
        }        
    }
    
    class TotalAction extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            sale.TotalButton.doClick();
        }        
    }
    
    class VoidAction extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            sale.voidButton.doClick();
        }        
    }
    class InventoryAction extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            sale.invenBtn.doClick();
        }        
    }
    
    class D1Action extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            sale.D1.doClick();
        }        
    }
    class D2Action extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            sale.D2.doClick();
        }        
    }
    class D3Action extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            sale.D3.doClick();
        }        
    }
    class D4Action extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            sale.D4.doClick();
        }        
    }
    class U1Action extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            sale.U1.doClick();
        }        
    }
    class U2Action extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            sale.U2.doClick();
        }        
    }
    class U3Action extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            sale.U3.doClick();
        }        
    }
    class U4Action extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            sale.U4.doClick();
        }        
    }
    class R1Action extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            sale.R1.doClick();
        }        
    }
    class R2Action extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            sale.R2.doClick();
        }        
    }
    class S1Action extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            sale.S1.doClick();
        }        
    }
    class S2Action extends AbstractAction
    {
        public void actionPerformed( ActionEvent tf )
        {
            sale.S2.doClick();
        }        
    }
    
    
}
