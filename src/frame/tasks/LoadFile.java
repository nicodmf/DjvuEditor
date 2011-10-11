/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frame.tasks;

import frame.Application;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jdesktop.application.Task;

/**
 *
 * @author Administrateur
 */
    /**
     * A Task that loads the contents of a file into a String.  The
     * LoadFileTask constructor runs first, on the EDT, then the
     * #doInBackground methods runs on a background thread, and finally
     * a completion method like #succeeded or #failed runs on the EDT.
     * 
     * The resources for this class, like the message format strings are 
     * loaded from resources/LoadFileTask.properties.
     */
   public class LoadFile extends Task<String, Void> {

        private final File file;
        private final Application app;
        private static final Logger logger = Logger.getLogger(Application.class.getName());


        /**
         * Construct a LoadTextFileTask.
         *
         * @param file the file to load from.
         *         /**
         * Construct the LoadFileTask object.  The constructor
         * will run on the EDT, so we capture a reference to the 
         * File to be loaded here.  To keep things simple, the 
         * resources for this Task are specified to be in the same 
         * ResourceMap as the MainFrame class's resources.
         * They're defined in resources/MainFrame.properties.
         * @return nothing
         */
        public LoadFile(Application application, File file) {
            super(application);
            app = application;
            this.file = file;
        }

        /**
         * Return the file being loaded.
         *
         * @return the value of the read-only file property.
         */
        public final File getFile() {
            return file;
        }

        /**
         * Load the file into a String and return it.  The
         * {@code progress} property is updated as the file is loaded.
         * <p>
         * If this task is cancelled before the entire file has been
         * read, null is returned.
         *
         * @return the contents of the {code file} as a String or null
         */
        @Override
        protected String doInBackground() throws IOException {
            /*
            int fileLength = (int) file.length();
            int nChars = -1;
            // progress updates after every blockSize chars read
            int blockSize = Math.max(1024, fileLength / 100);
            int p = blockSize;
            char[] buffer = new char[32];
            StringBuilder contents = new StringBuilder();
            BufferedReader rdr = new BufferedReader(new FileReader(file));
            while (!isCancelled() && (nChars = rdr.read(buffer)) != -1) {
                contents.append(buffer, 0, nChars);
                if (contents.length() > p) {
                    p += blockSize;
                    setProgress(contents.length(), 0, fileLength);
                }
            }
            if (!isCancelled()) {
                return contents.toString();
            } else {
                return null;
            }
            */
            //djvu.Document doc = new djvu.Document(file.toURI().toURL());
            //return doc.getPage(0, 1, true).getText().toString();
            return "aa";
        }

        /**
         * Called on the EDT if doInBackground completes without 
         * error and this Task isn't cancelled.  We update the
         * GUI as well as the file and modified properties here.
         */
        @Override protected void succeeded(String fileContents) {
            app.setFile(getFile());
        }
        /**
         * Called on the EDT if doInBackground fails because
         * an uncaught exception is thrown.  We show an error
         * dialog here.  The dialog is configured with resources
         * loaded from this Tasks's ResourceMap.
         */
        @Override protected void failed(Throwable e) {
            logger.log(Level.WARNING, "couldn't load " + getFile(), e);
            String msg = getResourceMap().getString("loadFailedMessage", getFile());
            String title = getResourceMap().getString("loadFailedTitle");
            int type = JOptionPane.ERROR_MESSAGE;
            JOptionPane.showMessageDialog(app.getFrame().getFrame(), msg, title, type);
        }
    }