/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sed.text.parts;

import java.awt.Rectangle;

/**
 *
 * @author Administrateur
 */
public class Position {
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    private double zoom = 1;
    private int[] dims = new int[2];
    public void Position(){
        dims[0] =0;
        dims[1] =0;
    }
    void set(int x1, int y1, int x2, int y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;        
    }
    public String get(){
        return x1 + " " + y1 + " " + x2 + " " + y2+ "";        
    }
    public String load(String str){
        for(int i=0; i<4; i++){
            int end = str.indexOf(" ");
            //System.out.println(i);
            //System.out.println(str.substring(0, 20));
            switch(i){
                case 0: x1 =  Integer.parseInt(str.substring(0, end).trim()); break;
                case 1: y1 =  Integer.parseInt(str.substring(0, end).trim()); break;
                case 2: x2 =  Integer.parseInt(str.substring(0, end).trim()); break;
                case 3: y2 =  Integer.parseInt(str.substring(0, end).trim()); break;                
            }
            str = str.substring(end).trim();
        }
        return str;        
    }
    public int getX1(){
        return (int)((x1)*zoom);
    }
    public int getY1(){
        return (int)((dims[1]-y2)*zoom);
    }
    public int getX2(){
        return (int)((x2)*zoom);
    }
    public int getY2(){
        return (int)((dims[1]-y1)*zoom);
    }
    public int getWidth(){
        return getX2()-getX1();
    }
    public int getHeight(){
        return getY2()-getY1();
    }
    public Rectangle getRectangle(){
        return new Rectangle(getX1(), getY1(), getX2()-getX1(), getY2()-getY1());
    }
    public void setZoom(double zoom){
        this.zoom = zoom;
    }
    public void setDims(int[]dims){
        this.dims = dims;
    }
}