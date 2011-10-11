/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frame.filter;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Administrateur
 */
public class DjvuFile extends FileFilter {

    private final String description;

    public DjvuFile(String description) {
        this.description = description;
    }

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String fileName = f.getName();
        int i = fileName.lastIndexOf('.');
        if ((i > 0) && (i < (fileName.length() - 1))) {
            String fileExt = fileName.substring(i + 1);
            if ("djvu".equalsIgnoreCase(fileExt)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
