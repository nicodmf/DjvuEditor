package sed.text;

import sed.Djvused;
import sed.text.parts.Page;
import sed.text.parts.Part;
import sed.text.parts.Word;
import frame.MainFrame;
import java.awt.Font;
import java.awt.Rectangle;
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
        Rectangle lastRect = null, rect = null;

        ArrayList<Part> lines = page.getLines();

        for (int i = 0; i < lines.size(); i++) {
            Part line = lines.get(i);
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
                rect = new Rectangle(word.pos.getX1(), word.pos.getY1(), word.pos.getWidth(), word.pos.getHeight());
                /*if(lastRect!=null && rect.x==lastRect.x){
                rect = new Rectangle(rect.x+lastRect.width, rect.y, rect.width, rect.height);
                }*/
                textField.setBounds(rect);
                lastRect = rect;
            }
            lastRect = null;
            //JTextField
        }
        setCaret(frame);
        //frame.getFrame().paintAll(null);
    }

    public void setCaret(MainFrame frame) {
        if (frame.app.direction) {
            JTextField jt = (JTextField)frame.Texts.getComponent(0);
            jt.requestFocus();
            jt.setCaretPosition(0);
        } else {
            JTextField jt = (JTextField)frame.Texts.getComponent(frame.Texts.getComponentCount()-1);
            jt.requestFocus();
            jt.setCaretPosition(jt.getText().length()-1);
        }
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

    void reinit() {
        pages = new ArrayList<Part>();
    }
}