/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frame.tasks;

/**
 *
 * @author Administrateur
 */
class Save extends org.jdesktop.application.Task<Object, Void> {

    Save(org.jdesktop.application.Application app) {
        // Runs on the EDT.  Copy GUI state that
        // doInBackground() depends on from parameters
        // to SaveTask fields, here.
        super(app);
    }

    @Override
    protected Object doInBackground() {
        // Your Task's code here.  This method runs
        // on a background thread, so don't reference
        // the Swing GUI from here.
        return null;  // return your result
    }

    @Override
    protected void succeeded(Object result) {
        // Runs on the EDT.  Update the GUI based on
        // the result computed by doInBackground().
    }
}