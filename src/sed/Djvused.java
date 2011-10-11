/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sed;

//import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
//import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Administrateur
 */
public class Djvused {

    File file;
    File inputFile;
    File logFile;
    int nbPages = 0;
    int[][] dimensions = null;
    String[] images = null;

    public Djvused() {
        inputFile = new File("tmp.file");
    }

    public Djvused(File file) {
        inputFile = new File("tmp.file");
        setFile(file);
    }

    private void init() {
        nbPages = 0;
        dimensions = null;
        images = null;
    }

    public void setFile(File file) {
        this.file = file;
        init();
    }

    public String getText() {
        String command = "djvused -u -e print-txt \"" + file.getAbsolutePath() + "\"";
        String result = ShellExec.execute(command);
       // try {
            //System.out.print(result);
            return new String(result.getBytes());//, "ISO-8859-1" );
        /*} catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Djvused.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        //return "";
    }

    public void saveText(String text) {
        File tfile = new File("temp.txt");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(tfile));            
            out.write(text.replaceAll("\n", System.getProperty("line.separator")));
            out.close();
        } catch (IOException e) {
            System.out.println("Exception ");
        }
        String command = "djvused -f \""+tfile.getAbsolutePath()+"\" \"" + file.getAbsolutePath() + "\"";
        ShellExec.execute(command);
    }

    private int sedNbPages() {
        String command = "djvused -e n \"" + file.getAbsolutePath() + "\"";
        String result = ShellExec.execute(command);
        return Integer.parseInt(result);
    }

    private int[] sedDimensions(int page) {
        String command = "djvused -e \"select " + page + "; size\" \"" + file.getAbsolutePath() + "\"";
        return transDimensions(ShellExec.execute(command));
    }

    private int[] transDimensions(String pageDim) {
        String[] ce = pageDim.split("=");
        return new int[]{Integer.parseInt(ce[1].split(" ")[0]), Integer.parseInt(ce[2])};
    }

    public int[][] getDimensions() {
        if (dimensions == null) {
            int nbPages = getNbPages();
            dimensions = new int[nbPages][2];
            for (int i = 0; i < nbPages; i++) {
                dimensions[i] = sedDimensions(i + 1);
            }
        }
        return dimensions;

    }

    public String sedImage(int page, File inImage, File outImage) {

        String command;//1, command2, res1, res2;
        command = "ddjvu -page=" + page + " -format=tiff \"" + file.getAbsolutePath() + "\" \"" + inImage.getAbsolutePath() + "\"";
        //command2 = "convert \"" + inImage.getAbsolutePath() + "\" \""+outImage.getAbsolutePath()+"\"";
        //command2 = "convert >z";
        if((inImage.exists()&&inImage.lastModified()<file.lastModified()) || ! inImage.exists()){
        ShellExec.execute(command);
        // ShellExec.processBuilderExecution("convert", inImage.getAbsolutePath(), outImage.getAbsolutePath());
        //boolean r = inImage.delete();
        }
        return inImage.getAbsolutePath();
    }
    public File getImages() {

        String command;//1, command2, res1, res2;
        File imageFile = new File(file.getName().replaceAll(".djvu", "") + ".tiff");
        command = "ddjvu -format=tiff \"" + file.getAbsolutePath() + "\" \"" + imageFile.getAbsolutePath() + "\"";
        ShellExec.execute(command);
        return imageFile;
    }

    public String getImage(int page) {
        if (images == null) {
            int nbPages = getNbPages();
            images = new String[nbPages];
            for (int i = 0; i < nbPages; i++) {
                images[i] = sedImage(i + 1, new File("t_" + i + ".tiff"), new File("t_" + i + ".png"));
            }
        }
        return images[page];
    }

    BufferedImage toBufferedImage(Image image) {
        /** On test si l'image n'est pas déja une instance de BufferedImage */
        if (image instanceof BufferedImage) {
            return ((BufferedImage) image);
        } else {
            /** On s'assure que l'image est complètement chargée */
            image = new ImageIcon(image).getImage();

            /** On crée la nouvelle image */
            BufferedImage bufferedImage = new BufferedImage(
                    image.getWidth(null),
                    image.getHeight(null),
                    BufferedImage.TYPE_INT_RGB);

            Graphics g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();

            return (bufferedImage);
        }
    }

    public int getNbPages() {
        if (nbPages == 0) {
            nbPages = sedNbPages();
        }
        return nbPages;
    }
}
