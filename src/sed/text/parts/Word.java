package sed.text.parts;

import sed.text.Lists;
import frame.Application;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JTextField;
import sed.text.parts.listener.WordTextField;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrateur
 */
public class Word extends Part {

    public ArrayList<Part> words = new ArrayList<Part>();
    public JTextField textField = null;
    //public JTextField textField = null;
    //public Lists lists = null;

    public Word(Part parent) {
        super(parent);
    }

    public Word() {
    }

    /*public Word(Lists lists) {
    this.lists = lists;
    }*/
    @Override
    public void addPart(Part word) {
        words.add(word);
        parts = words;
    }

    public String getWord() {
        StringWord word = (StringWord) words.get(0);
        return word.word;
    }

    public void setWord(String word) {
        StringWord sword = (StringWord) words.get(0);
        sword.word = word;
    }

    public JTextField getTextField() {
        textField = new JTextField(getWord());
        textField.addKeyListener(new WordTextField(this));
        return textField;
    }

    @Override
    public String save(String res) {
        if (!"\"\"".equals(res)) {
            return "\n  " + start() + pos.get() + " " + res + end();
        }
        return "";

    }

    public Part getInstance() {
        return this;
    }

    @Override
    public String start() {
        return "(word ";
    }

    @Override
    public String end() {
        return ")";
    }
}