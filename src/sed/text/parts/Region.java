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
public class Region extends Part {

    public ArrayList<Part> paras  = new ArrayList<Part>();

    public Region(Part parent) {
        super(parent);
    }

    public Region() {}

    @Override
    public void addPart(Part para) {
        paras.add(para);
        parts = paras;
    }

    @Override
    public String start() {
        return "(region ";
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