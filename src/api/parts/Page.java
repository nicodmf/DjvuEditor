/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api.parts;

import java.util.ArrayList;

/**
 *
 * @author Administrateur
 */
public class Page extends Part {

    public ArrayList<Part> lines = new ArrayList<Part>();

    @Override
    public void addPart(Part line) {
        lines.add(line);
        parts = lines;
    }

    @Override
    public String start() {
        return "(page ";
    }

    @Override
    public String end() {
        return ")";
    }
    @Override
    public String save(String res){
        return ""+start() + pos.get() +""+ res + end();
        
    }  
}