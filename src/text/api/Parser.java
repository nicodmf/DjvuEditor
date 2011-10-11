/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package text.api;

import text.api.parts.Line;
import text.api.parts.Page;
import text.api.parts.Part;
import text.api.parts.StringWord;
import text.api.parts.Word;

/**
 *
 * @author Administrateur
 */
public class Parser {

    Document document;
    String string;

    public Parser(Document document, String string) {
        this.document = document;
        this.string = string;
    }

    static public void parse(Document document, String string) {
        subParse(document, string.replaceAll("\\\\\"", "__xx__"));
        
    }

    static String subParse(Part parent, String str) {
        str = str.trim();
        Part child;
        String endString;
        String startString;
        int posEnd;
        int posChildStart;
        int posRealEnd;
        int posRealChildStart;
        boolean end = false;
        do {
            child = createChild(parent);

            endString = parent.end();
            startString = child.start();

            posEnd = str.indexOf(endString);
            posChildStart = str.indexOf(startString);
            posRealEnd = posEnd + endString.length();
            posRealChildStart = posChildStart + startString.length();

            if(parent instanceof Document){
                posEnd = str.length()-1;
                posRealEnd = str.length()-1;
            }
            if(posChildStart==-1){
                posChildStart = posEnd;
            }

            if (posEnd > posChildStart) {

                parent.addPart(child);

                str = str.substring(posRealChildStart).trim();
                if (false == (child instanceof StringWord)) {
                    str = child.loadPosition(str).trim();
                    str = subParse(child, str);
                } else {
                    StringWord sw = (StringWord) child;
                    str = sw.loadWord(str);
                }
            } else {
                try{
                    str = str.substring(posRealEnd);
                }catch(Exception e){
                    
                }
                end = true;
            }
        } while (end == false);

        return str;
    }

    static Part createChild(Part parent) {
        Part child = new Page();
        if (parent instanceof Document) {
            Page page = new Page();
            Lists.pages.add(page);
            return page;

        }
        if (parent instanceof Page) {
            Line line = new Line();
            Lists.lines.add(line);
            return line;

        }
        if (parent instanceof Line) {
            Word word = new Word();
            Lists.words.add(word);
            return word;
        }
        if (parent instanceof Word) {
            return new StringWord();
        }
        return child;
    }
}
