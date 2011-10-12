/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sed.text.parts;

import java.util.ArrayList;

/**
 *
 * @author Administrateur
 */
public class Para extends Part {

    public ArrayList<Part> lines  = new ArrayList<Part>();

    public Para(Part parent) {
        super(parent);
    }

    public Para() {}

    @Override
    public void addPart(Part line) {
        lines.add(line);
        parts = lines;
    }

    @Override
    public String start() {
        return "(para ";
    }

    @Override
    public String end() {
        return ")";
    }
    @Override
    public String save(String res){
        return "\n "+start() + pos.get() +""+ res + end();
        
    }
    public Part getPage(){
        Part page = parent;
        if(! ( page instanceof Page))
            page = parent.getPage();
        return page;
    }

}