/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frame.listener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Administrateur
 */
public class TextsMouse implements MouseListener, MouseMotionListener{

    private JPanel panel;
    private JLabel label;
    private int xo, yo;
    private Component[] components;
    public TextsMouse(JPanel panel){
        this.panel=panel;
        label = new JLabel();
        label.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        label.setBackground(Color.CYAN);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) 
    {
        components = panel.getComponents();
        panel.add(label);
        panel.setComponentZOrder(label, 0);
        //label.setBounds(5, 5, 100, 100);
        System.out.println("Pressed at [" + e.getX() + ", " + e.getY() + "]");        
        xo = e.getX();
        yo = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Realised at [" + e.getX() + ", " + e.getY() + "]");        
        panel.remove(label);
        panel.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("Entered at [" + e.getX() + ", " + e.getY() + "]");        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("Exited at [" + e.getX() + ", " + e.getY() + "]");        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //System.out.println("Dragged at [" + e.getX() + ", " + e.getY() + "]");
        
        int x,y,w,h;
        if(xo>e.getX()){
            x=e.getX();
            w=xo-e.getX();
        }else{
            x=xo;
            w=e.getX()-xo;
        }
        if(yo>e.getY()){
            y=e.getY();
            h=yo-e.getY();
        }else{
            y=yo;
            h=e.getY()-yo;
        }
        label.setBounds(x,y,w,h);        
        for(int i=0; i<components.length; i++){
            if(components[i].getBounds().intersects(label.getBounds()))
                components[i].setBackground(Color.blue);
            else
                components[i].setBackground(null);            
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
       // System.out.println("Mouved at [" + e.getX() + ", " + e.getY() + "]");        

    }
    
}
