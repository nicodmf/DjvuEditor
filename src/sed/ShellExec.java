/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sed;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Administrateur
 */
public class ShellExec {

    static String getFileContents(File file) throws FileNotFoundException, IOException {
        //objet de la famille InputStream
        FileInputStream fichier = new FileInputStream(file);
        StringWriter writer = new StringWriter();
        InputStreamReader streamReader = new InputStreamReader(fichier);
//le buffer permet le readline
        BufferedReader buffer = new BufferedReader(streamReader);
        String line = "";
        while (null != (line = buffer.readLine())) {
            writer.write(line);
        }
        // Sortie finale dans le String
        return writer.toString();
    }

    static int putFileContents(File file, String string) throws FileNotFoundException, IOException {
        return putFileContents(file, string, true);
    }

    static int putFileContents(File file, String string, boolean append) throws FileNotFoundException, IOException {
        //objet de la famille InputStream
        /**
         * BufferedWriter a besoin d un FileWriter,
         * les 2 vont ensemble, on donne comme argument le nom du fichier
         * true signifie qu on ajoute dans le fichier (append), on ne marque pas par dessus
        
         */
        FileWriter fw = new FileWriter(file, false);

        // le BufferedWriter output auquel on donne comme argument le FileWriter fw cree juste au dessus
        BufferedWriter output = new BufferedWriter(fw);

        //on marque dans le fichier ou plutot dans le BufferedWriter qui sert comme un tampon(stream)
        output.write(string);
        output.flush();
        output.close();
        return 1;
    }

    static int execute(String command, File redirectionFile) {
        try {
            Process proc = Runtime.getRuntime().exec(command);
            InputStream in = proc.getInputStream();
            BufferedWriter out = new BufferedWriter(new FileWriter(redirectionFile));

            int c;
            while ((c = in.read()) != -1) {
                out.write((char) c);
            }

            in.close();
            out.flush();
            out.close();
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    static String execute(String command) {
        try {
            Process proc = Runtime.getRuntime().exec(command);
            StringWriter writer = new StringWriter();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = "";
            while (null != (line = buffer.readLine())) {
                writer.write(line);
            }
// Sortie finale dans le String
            return writer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    static void simpleExecute(String command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException ex) {
            Logger.getLogger(ShellExec.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void processBuilderExecution(String command, String arg1, String arg2) {
        ProcessBuilder pb = null;
        try {
            pb = new ProcessBuilder(searchForCmd(command, System.getenv("PATH")), arg1, arg2);
        } catch (IOException ex) {
            Logger.getLogger(ShellExec.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            //pb.directory("myDir");
            pb.start();
        } catch (IOException ex) {
            Logger.getLogger(ShellExec.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static Image getPPMImage(String command) {
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec(command);
        } catch (IOException ex) {
            Logger.getLogger(ShellExec.class.getName()).log(Level.SEVERE, null, ex);
        }
        Image img = null;
        try {
            img = PPMStreamToImage(proc.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ShellExec.class.getName()).log(Level.SEVERE, null, ex);
        }
        return img;
    }

    static Image PPMStreamToImage(InputStream stream) throws IOException {
        Dimension dim = new Dimension();
        int[] data;
        //FileInputStream fis = new FileInputStream(filename);

        // XXX: The source data to the StreamTokenizer can't buffered, since we
        // only need a few lines of the beginning of the file, and all remain
        // data must be processed later. Even a InputStreamReader does some
        // internal buffering, so we must use a deprecated constructor, create
        // our own Reader or create some methods to simulate a StreamTokenizer.
        // Easy way chosen :-)
        StreamTokenizer st = new StreamTokenizer(stream);
        st.commentChar('#');

        /* PPM file format:
         * 
         * #			  --> Comments allowed anywere before binary data
         * P3|P6          --> ASCII/Binary
         * WIDTH          --> image width, in ascii
         * HEIGHT         --> image height, in ascii
         * COLORS		  --> num colors, in ascii
         * [data]		  --> if P6, data in binary, 3 RGB bytes per pixel 
         */

        st.nextToken();
        if (!st.sval.equals("P6")) {
            throw new UnsupportedEncodingException("Not a P6 (binary) PPM");
        }

        st.nextToken();
        dim.width = (int) Math.round(st.nval);
        st.nextToken();
        dim.height = (int) Math.round(st.nval);
        data = new int[dim.width * dim.height];

        st.nextToken();
        int maxVal = (int) Math.round(st.nval);
        String s = st.sval;
        if (maxVal != 255) {
            throw new UnsupportedEncodingException("Not a 255 color PPM");
        }

        // Binary data cann be buffered
        InputStream in = new BufferedInputStream(stream);
        int numPixels = dim.width * dim.height;
        for (int i = 0; i < numPixels; i++) {
            int r = in.read();
            int g = in.read();
            int b = in.read();
            if (r == -1 || g == -1 || b == -1) {
                throw new IOException("EOF:" + r + " " + g + " " + b);
            }
            data[i] = rgb(r, g, b);
        }
        in.close();
        MemoryImageSource memoryImageSource = new MemoryImageSource(dim.width, dim.height, data, 0, dim.width);
        return Toolkit.getDefaultToolkit().createImage(memoryImageSource);
    }

    /**
     * Makes an ARGB pixel from three color values
     * @param r The red value
     * @param g The green value
     * @param b The blue value
     * @return The ARGB value
     * 
     */
    private static int rgb(int r, int g, int b) {
        return (255 << 24 | (r << 16) | (g << 8) | b);
    }

    public static String searchForCmd(String pCmd, String pPath)
            throws IOException, FileNotFoundException {
        // check is pCmd is absolute
        if ((new File(pCmd)).isAbsolute()) {
            return pCmd;
        }

        // special processing on windows-systems.
        // File.pathSeparator is hopefully more robust than 
        // System.getProperty("os.name") ?!
        boolean isWindows = File.pathSeparator.equals(";");

        String dirs[] = pPath.split(File.pathSeparator);
        for (int i = 0; i < dirs.length; ++i) {
            if (isWindows) {
                // try thre typical extensions
                File cmd = new File(dirs[i], pCmd + ".exe");
                if (cmd.exists()) {
                    return cmd.getCanonicalPath();
                }
                cmd = new File(dirs[i], pCmd + ".cmd");
                if (cmd.exists()) {
                    return cmd.getCanonicalPath();
                }
                cmd = new File(dirs[i], pCmd + ".bat");
                if (cmd.exists()) {
                    return cmd.getCanonicalPath();
                }
            } else {
                File cmd = new File(dirs[i], pCmd);
                if (cmd.exists()) {
                    return cmd.getCanonicalPath();
                }
            }
        }
        throw new FileNotFoundException(pCmd);
    }
}
