/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frame.tasks;

import api.Djvused;
import frame.Application;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jdesktop.application.Task;

/**
 *
 * @author Administrateur
 */
public class SaveFile extends Task<Void, Void> {

        private final Application app;
        private File file;
        private String text;
        private static final Logger logger = Logger.getLogger(Application.class.getName());

        /**
         * Construct a SaveTextFileTask.
         *
         * @param file The file to save to
         * @param text The new contents of the file
         */
        public SaveFile(Application app, File file, String text) {
            super(app);
            this.app = app;
            this.file = file;
            this.text = text;
        }

        /**
         * Return the File that the {@link #getText text} will be
         * written to.
         *
         * @return the value of the read-only file property.
         */
        public final File getFile() {
            return file;
        }

        /**
         * Return the String that will be written to the
         * {@link #getFile file}.
         *
         * @return the value of the read-only text property.
         */
        public final String getText() {
            return text;
        }

        private void renameFile(File oldFile, File newFile) throws IOException {
            if (!oldFile.renameTo(newFile)) {
                String fmt = "file rename failed: %s => %s";
                throw new IOException(String.format(fmt, oldFile, newFile));
            }
        }

        /**
         * Writes the {@code text} to the specified {@code file}.  The
         * implementation is conservative: the {@code text} is initially
         * written to ${file}.tmp, then the original file is renamed
         * ${file}.bak, and finally the temporary file is renamed to ${file}.
         * The Task's {@code progress} property is updated as the text is
         * written.
         * <p>
         * If this Task is cancelled before writing the temporary file
         * has been completed, ${file.tmp} is deleted.
         * <p>
         * The conservative algorithm for saving to a file was lifted from
         * the FileSaver class described by Ian Darwin here:
         * <a href="http://javacook.darwinsys.com/new_recipes/10saveuserdata.jsp">
         * Save user data
         * </a>.
         *
         * @return null
         */
        @Override
        protected Void doInBackground() {
            Djvused sed = new Djvused(file);
            sed.saveText(text);
            /*
            String absPath = file.getAbsolutePath();
            File tmpFile = new File(absPath + ".tmp");
            tmpFile.createNewFile();
            tmpFile.deleteOnExit();
            File backupFile = new File(absPath + ".bak");
            BufferedWriter out = null;
            int fileLength = text.length();
            int blockSize = Math.max(1024, 1 + ((fileLength - 1) / 100));
            try {
                out = new BufferedWriter(new FileWriter(tmpFile));
                int offset = 0;
                while (!isCancelled() && (offset < fileLength)) {
                    int length = Math.min(blockSize, fileLength - offset);
                    out.write(text, offset, length);
                    offset += blockSize;
                    setProgress(Math.min(offset, fileLength), 0, fileLength);
                }
            } finally {
                if (out != null) {
                    out.close();
                }
            }
            if (!isCancelled()) {
                backupFile.delete();
                if (file.exists()) {
                    renameFile(file, backupFile);
                }
                renameFile(tmpFile, file);
            } else {
                tmpFile.delete();
            }
            return null;*/
            return null;
        }
        @Override protected void succeeded(Void ignored) {
	    setFile(getFile());
            app.setModified(false);
        }
        @Override protected void failed(Throwable e) {
            logger.log(Level.WARNING, "couldn't save " + getFile(), e);
            String msg = getResourceMap().getString("saveFailedMessage", getFile());
            String title = getResourceMap().getString("saveFailedTitle");
            int type = JOptionPane.ERROR_MESSAGE;
            JOptionPane.showMessageDialog(app.getFrame().getFrame(), msg, title, type);
        }

    private void setFile(File file) {
        this.file=file;
        
    }
        
}
