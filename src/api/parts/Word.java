package api.parts;

import api.Lists;
import frame.Application;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JTextField;

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
        textField.addKeyListener(new KeyListener(){
            @Override public void keyTyped(KeyEvent e){ jtSetWord(e); setPosition(e);}
            @Override public void keyPressed(KeyEvent e){ setPosition(e);}
            @Override public void keyReleased(KeyEvent e){ jtSetWord(e); }
            public void jtSetWord(KeyEvent e){
                setWord(textField.getText());
                Application.getApplication().setModified(true);
                
            }
            public void setPosition(KeyEvent e){
                int cpos = textField.getCaretPosition();
                if(e.getKeyCode() == KeyEvent.VK_RIGHT && cpos==textField.getText().length()){
                    int index = Lists.words.indexOf(getInstance())+1;
                    while(((Word) Lists.words.get(index)).textField==null)
                        index++;
                    ((Word) Lists.words.get(index)).textField.requestFocus ();
                    ((Word) Lists.words.get(index)).textField.setCaretPosition(0);
                }
                if(e.getKeyCode() == KeyEvent.VK_LEFT && cpos==0){
                    int index = Lists.words.indexOf(getInstance())-1;
                    while(((Word) Lists.words.get(index)).textField==null)
                        index--;
                    int ncpos = ((Word) Lists.words.get(index)).textField.getText().length();
                    ((Word) Lists.words.get(index)).textField.requestFocus ();
                    ((Word) Lists.words.get(index)).textField.setCaretPosition(ncpos);
                }
                
            }
        });
        return textField;
    }
    @Override
    public String save(String res){
        if(!"\"\"".equals(res))
            return "\n  "+start() + pos.get() + " " + res + end();
        return"";
        
    }
    public Part getInstance(){
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