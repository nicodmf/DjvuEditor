/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package text.api.parts;

import java.util.ArrayList;

/**
 *
 * @author Administrateur
 */
abstract public class Part {

    public ArrayList <Part> parts = new ArrayList<Part>();
    public Position pos;

    void load(String string) {
        int start = string.indexOf(start());

    }

    public String loadPosition(String string) {
        pos = new Position();
        string = pos.load(string.trim());
        return string;
    }

    public String save() {
        String res = "";
        ArrayList <Part> studiesParts = getParts();
        Part part;

        for (int i = 0; i < studiesParts.size(); i++) {
            part = studiesParts.get(i);
            res += part.save();
        }
        return save(res);
    }
    public String save(String res){
        return start() + pos.get() + res + end();
        
    }

    public String start() {
        return "To implemented";
    }

    public String end() {
        return "To implemented";
    }

    public ArrayList <Part> getParts() {
        return parts;
    }

    public void addPart(Part part) {
        parts.add(part);
    }
}
