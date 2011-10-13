/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sed.text;

import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import sed.text.Document;
import sed.text.parts.Line;
import sed.text.parts.Page;
import sed.text.parts.Para;
import sed.text.parts.Part;
import sed.text.parts.Region;
import sed.text.parts.StringWord;
import sed.text.parts.Word;

/**
 *
 * @author Administrateur
 */
public class Parser {

    Document document;
    String string;
    static boolean region = false, para = false;
    static Document pdocument = new Document();
    static Page ppage = new Page();
    static Region pregion = new Region();
    static Para ppara = new Para();
    static Line pline = new Line();
    static Word pword = new Word();
    static StringWord pstringword = new StringWord();

    public Parser(Document document, String string) {
        this.document = document;
        this.string = string;
    }

    static public void parse(Document document, String string) {
        para = string.indexOf("(para") != -1;
        region = string.indexOf("(region") != -1;
        document.reinit();
        Lists.reinit();
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
        int posRealChildStart = -1;
        boolean end = false;
        do {
            child = getChild(str, parent);

            if (child != null) {
                posRealChildStart = str.indexOf(child.start()) + child.start().length();
            }
            if (parent instanceof Document) {
                posRealEnd = str.length() - 1;
            } else {
                posRealEnd = str.indexOf(parent.end()) + parent.end().length();
            }

            if (child != parent && child != null) {

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
                try {
                    str = str.substring(posRealEnd);
                } catch (Exception e) {
                }
                end = true;
            }
        } while (end == false);

        return str;
    }

    static Part getChild(String txt, Part parent) {
        Part child;
        TreeMap hm = new TreeMap<Number, Part>();
        hm.put(txt.indexOf(ppage.start()), new Page(parent));
        hm.put(txt.indexOf(pregion.start()), new Region(parent));
        hm.put(txt.indexOf(ppara.start()), new Para(parent));
        hm.put(txt.indexOf(pline.start()), new Line(parent));
        hm.put(txt.indexOf(pword.start()), new Word(parent));
        hm.put(txt.indexOf(pstringword.start()), new StringWord(parent));
        hm.put(txt.indexOf(parent.end()), parent);
        try {
            SortedMap tm = hm.tailMap(0);
            Object key = tm.firstKey();
            child =(Part) tm.get(key);
        } catch (Exception e) {
            return null;
        }
        if(child!=parent)addList(child);
        return child;
    }

    static void addList(Part part) {
        if (part instanceof Page) {
            Lists.pages.add(part);
       /* }else  if (part instanceof Region) {
            Lists.regions.add(part);
        }else if (part instanceof Para) {
            Lists.paras.add(part);*/
        }else if (part instanceof Line) {
            Lists.lines.add(part);
        }else if (part instanceof Word) {
            //if(Lists.words.indexOf(part)==-1)
                Lists.words.add(part);
        }
    }
    static Part createChild(Part parent) {
        Part child = new Page(parent);
        if (parent instanceof Document) {
            Page page = new Page(parent);
            Lists.pages.add(page);
            return page;

        }
        if (parent instanceof Page && !region) {
            Line line = new Line(parent);
            Lists.lines.add(line);
            return line;

        }
        if (parent instanceof Page && region) {
            Region region = new Region(parent);
            Lists.regions.add(region);
            return region;

        }
        if (parent instanceof Region && !para) {
            Line line = new Line(parent);
            Lists.lines.add(line);
            return line;

        }
        if (parent instanceof Region && para) {
            Para para = new Para(parent);
            Lists.paras.add(para);
            return para;

        }
        if (parent instanceof Para && !region) {
            Line line = new Line(parent);
            Lists.lines.add(line);
            return line;

        }
        if (parent instanceof Line) {
            Word word = new Word(parent);
            Lists.words.add(word);
            return word;
        }
        if (parent instanceof Word) {
            return new StringWord(parent);
        }
        return child;
    }
}
