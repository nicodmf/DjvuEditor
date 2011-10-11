package text.api;

import text.api.parts.Page;
import text.api.parts.Part;
import text.api.parts.Word;
import frame.MainFrame;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Formatter;
import javax.swing.JTextField;
//import org.netbeans.lib.awtextra.AbsoluteConstraints;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrateur
 */
public class Document extends Part {

    public ArrayList<Part> pages = new ArrayList<Part>();
    private Djvused sed;

    public void parse(String str) {
        Parser.parse(this, str);
    }

    public void showPage(int npage, MainFrame frame, double zoom, int[] dims) {
        frame.Texts.removeAll();
        Part page = pages.get(npage - 1);
        for (int i = 0; i < page.parts.size(); i++) {
            Part line = page.parts.get(i);
            for (int j = 0; j < line.parts.size(); j++) {
                Word word = (Word) line.parts.get(j);
                word.pos.setDims(dims);
                word.pos.setZoom(zoom);
                JTextField textField = word.getTextField();
                //textField.setBounds(word.pos.getX1(), word.pos.getY1(), word.pos.getWidth(), word.pos.getHeight());
                textField.setBorder(null);
                textField.setFont(new Font("Times New Roman", 0, word.pos.getHeight() / 7 * 6));
                textField.addFocusListener(frame);
                frame.Texts.add(textField);
                textField.setBounds(word.pos.getX1(), word.pos.getY1(), word.pos.getWidth(), word.pos.getHeight());
            }
            //JTextField
        }
        //frame.getFrame().paintAll(null);
    }

    @Override
    public String save() {
        String res = "select; remove-txt\n";
        Page page;

        for (int i = 0; i < pages.size(); i++) {
            Number n = i;
            res += "# ------------------------- \n";
            StringBuilder sb = new StringBuilder();
            // Send all output to the Appendable object sb
            Formatter formatter = new Formatter(sb);
            res += "select \"p" + formatter.format("%04d", i + 1) + ".djvu\"\n";
            res += "set-txt\n";
            page = (Page) pages.get(i);
            res += page.save();
            res += "\n\n.\n";
        }
        res += "save";
        return res;
    }

    @Override
    public String end() {
        return ("EOF....");
    }

    @Override
    public void addPart(Part page) {
        pages.add(page);
        parts = pages;
    }
}