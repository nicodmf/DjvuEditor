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
abstract public class Part {

    public ArrayList<Part> parts = new ArrayList<Part>();
    public Position pos;
    protected Part parent;

    public Part() {
    }

    public Part(Part parent) {
        this.parent = parent;
    }

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
        ArrayList<Part> studiesParts = getParts();
        Part part;

        for (int i = 0; i < studiesParts.size(); i++) {
            part = studiesParts.get(i);
            res += part.save();
        }
        return save(res);
    }

    public String save(String res) {
        return start() + pos.get() + res + end();

    }

    public String start() {
        return "To implemented";
    }

    public String end() {
        return "To implemented";
    }

    static public String start(Part part) {
        return part.start();
    }

    static public String end(Part part) {
        return part.end();
    }

    public ArrayList<Part> getParts() {
        return parts;
    }

    public void addPart(Part part) {
        parts.add(part);
    }

    Part getPage() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public ArrayList<Part> getLines() {
        ArrayList<Part> lines = new ArrayList<Part>();
        for (int i = 0; i < parts.size(); i++) {
            Part part = parts.get(i);
            if (part instanceof Line) {
                lines.add(part);
            } else if ((part instanceof Para) || part instanceof Region) {
                lines.addAll(part.getLines());
            }
        }
        return lines;
    }

    public Part getFirstWord() {
        Part part = parts.get(0);
        if (part instanceof Word) {
            return part;
        } else {
            return part.getFirstWord();
        }
    }
    public Part getLastWord() {
        Part part = parts.get(parts.size()-1);
        if (part instanceof Word) {
            return part;
        } else if(part!=null){
            return part.getLastWord();
        }
        return null;
    }
}