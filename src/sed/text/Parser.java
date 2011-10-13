/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sed.text;

import java.util.Arrays;
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
    static String pdocument = new Document().end();
    static String ppage = new Page().start();
    static String pregion = new Region().start();
    static String ppara = new Para().start();
    static String pline = new Line().start();
    static String pword = new Word().start();
    static String pstringword = new StringWord().start();
    static int l, pp, pr, pa, pl, pw, ps, ep, min;
    static int nums[] = new int[7];

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

    static Part getChild1(String txt, Part parent) {
        Part child = null;
        //TreeMap hm = new TreeMap<Number, Part>();
        //int l, pp, pr, pa, pl, pw, ps, ep,
        min=txt.length();
        //int[] nums = new int[7];  
        //l = txt.length();
        nums[0] = pp = txt.indexOf(ppage);
        if(region)nums[1] = pr = txt.indexOf(pregion);else nums[1]=-1;
        if(para)nums[2] = pa = txt.indexOf(ppara);else nums[2]=-1;
        nums[3] = pl = txt.indexOf(pline);
        nums[4] = pw = txt.indexOf(pword);
        nums[5] = ps = txt.indexOf(pstringword);
        nums[6] = ep = txt.indexOf(parent.end());
        for(int i=0; i<7; i++){
            if(nums[i]>=0)
                min=Math.min(nums[i],min);
        }
        
        if(min==ep)
            return parent;     
        else if(min == pw)
            child = new Word(parent);
        else if(min== ps)
            child = new StringWord(parent);
        else if(min== pl)
            child = new Line(parent);
        else if(min== pp)
            child = new Page(parent);
        else if(para && min== pa)
            child = new Para(parent);
        else if(para && min== pr)
            child = new Region(parent);
        
        if(child!=null)addList(child);
        
        return child;
    }
    static Part getChild(String txt, Part parent) {
        Part child;
        TreeMap hm = new TreeMap<Number, Part>();
        hm.put(txt.indexOf(ppage), new Page(parent));
        if(region)hm.put(txt.indexOf(pregion), new Region(parent));
        if(para)hm.put(txt.indexOf(ppara), new Para(parent));
        hm.put(txt.indexOf(pline), new Line(parent));
        hm.put(txt.indexOf(pword), new Word(parent));
        hm.put(txt.indexOf(pstringword), new StringWord(parent));
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
