/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sed.text.parts.listener;

import frame.Application;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JTextField;
import sed.text.Lists;
import sed.text.parts.Part;
import sed.text.parts.Word;

/**
 *
 * @author Administrateur
 */
public class WordTextField implements KeyListener {
    private JTextField textField;
    private Word word;
    
    public WordTextField(Word word){
        this.word = word;
        textField = word.textField;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        jtSetWord(e);
        setPosition(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        setPosition(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        jtSetWord(e);
    }

    public void jtSetWord(KeyEvent e) {
        word.setWord(textField.getText());
        Application.getApplication().setModified(true);

    }

    public void setPosition(KeyEvent e) {
        int cpos = textField.getCaretPosition();
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && cpos == textField.getText().length()) {
            try {
                int index = Lists.words.indexOf(word) + 1;
                ArrayList<Part> words = Lists.words;
                while (((Word) Lists.words.get(index)).textField == null) {
                    index++;
                }
                ((Word) Lists.words.get(index)).textField.requestFocus();
                ((Word) Lists.words.get(index)).textField.setCaretPosition(0);
            } catch (Exception ex) {
                Application.getApplication().next();
            }

        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && cpos == 0) {
            try {
                int index = Lists.words.indexOf(word) - 1;
                while (((Word) Lists.words.get(index)).textField == null) {
                    index--;
                }
                int ncpos = ((Word) Lists.words.get(index)).textField.getText().length();
                ((Word) Lists.words.get(index)).textField.requestFocus();
                ((Word) Lists.words.get(index)).textField.setCaretPosition(ncpos);
            } catch (Exception ex) {
                Application.getApplication().prev();
            }
        }

    }
}
