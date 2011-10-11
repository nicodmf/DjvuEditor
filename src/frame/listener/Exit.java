/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frame.listener;

import frame.Application;
import java.util.EventObject;
import javax.swing.JOptionPane;
import org.jdesktop.application.Application.ExitListener;

/**
 *
 * @author Administrateur
 */
    public class Exit implements ExitListener {
        Application app;
        public Exit(Application app){
            this.app=app;
        }
    @Override
        public boolean canExit(EventObject e) {
            if (app.isModified()) {
                String confirmExitText = app.getFrame().getResourceMap().getString("confirmTextExit", app.getFile());
                int option = JOptionPane.showConfirmDialog(app.getFrame().getFrame(), confirmExitText);
                return option == JOptionPane.YES_OPTION;
                // TODO: also offer saving
            } else {
                return true;
            }
        }
    @Override
        public void willExit(EventObject e) { }
    }