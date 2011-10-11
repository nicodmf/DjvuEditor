/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frame.listener;

import frame.Application;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Administrateur
 */
public class Document implements DocumentListener {

    Application app;

    public Document(Application app) {
        this.app = app;
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        app.setModified(true);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        app.setModified(true);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        app.setModified(true);
    }
}
