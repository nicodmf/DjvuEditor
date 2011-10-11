/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frame;

/**
 *
 * @author Administrateur
 */
import java.io.File;
import java.io.IOException;
import java.awt.image.RenderedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.NullOpImage;
import javax.media.jai.OpImage;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageCodec;
import java.awt.RenderingHints;
/*
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import javax.media.jai.JAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.PlanarImage;
 */
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.SubsampleAverageDescriptor;
import com.sun.media.jai.widget.DisplayJAI;
import java.util.HashMap;

public class TiffReader {

    private class SavImages{
        HashMap map = new HashMap();
        public RenderedImage get(double zoom){
            return (RenderedImage) map.get(zoom);
        }       
        public void set(double zoom, RenderedImage image){
            map.put(zoom, image);
        }       
        public boolean exists(double zoom){
            return map.containsKey(zoom);
        }       
    }
    ImageDecoder dec;
    SavImages[] images;

    DisplayJAI panel;

    public TiffReader(){
        System.setProperty("com.sun.media.jai.disableMediaLib", "true");
        panel = new DisplayJAI();        
        
    }
    public TiffReader(String filename) throws IOException {

        this();
        setFile(filename);

        //System.out.println("Number of images in this TIFF: " +     dec.getNumPages());

        // Which of the multiple images in the TIFF file do we want to load
        // 0 refers to the first, 1 to the second and so on.
        //int imageToLoad = 0;



        // Display the original in a 800x800 scrolling window
        //panel = new ScrollingImagePanel(op, 800, 800);
        //add(panel);
    }
    public void setFile(String filename) throws IOException{
        File file = new File(filename);
        SeekableStream s = new FileSeekableStream(file);

        TIFFDecodeParam param = null;

        dec = ImageCodec.createImageDecoder("tiff", s, param);
        images = new SavImages[dec.getNumPages()];

    }

    public NullOpImage getImage(int imageToLoad) {
        NullOpImage op = null;
        try {
            return new NullOpImage(dec.decodeAsRenderedImage(imageToLoad),
                    null,
                    OpImage.OP_IO_BOUND,
                    null);
        } catch (IOException ex) {
            Logger.getLogger(TiffReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return op;
    }

    public RenderedImage get(int imageToLoad, double zoom) {
        if (images[imageToLoad] != null && images[imageToLoad].exists(zoom)) {
            return images[imageToLoad].get(zoom);
        }
        images[imageToLoad] = new SavImages();
        images[imageToLoad].set(zoom, scale(getImage(imageToLoad), zoom));
        return images[imageToLoad].get(zoom);

    }

    public DisplayJAI getPanel() {
        return panel;
    }

    public void setPage(int page, int width, int height, double zoom) {
        panel.set(get(page, zoom));
        panel.setSize(width, height);
        panel.repaint();
        panel.validate();
    }

    public static RenderedImage scale(NullOpImage image, double scaleFactor) {
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        RenderedOp resizeOp = SubsampleAverageDescriptor.create(image, scaleFactor, scaleFactor, hints);
        return resizeOp;
    }
/*
    public static BufferedImage PaletteToRGB(PlanarImage op) {
        PlanarImage src = op;

        PlanarImage dst = null;

        if (src.getColorModel() instanceof IndexColorModel) {
            IndexColorModel icm = (IndexColorModel) src.getColorModel();
            byte[][] data = new byte[3][icm.getMapSize()];

            icm.getReds(data[0]);
            icm.getGreens(data[1]);
            icm.getBlues(data[2]);

            LookupTableJAI lut = new LookupTableJAI(data);

            dst = JAI.create("lookup", src, lut);
        } else {
            dst = src;
        }
        return dst.getAsBufferedImage();

    }

    public static BufferedImage convertRenderedImageToBufferedImage(final RenderedImage ri) {
        // Return as-is if already a BufferedImage.
        if (ri instanceof BufferedImage) {
            return (BufferedImage) ri;
        }

        // Extract basic metadata.
        final ColorModel cm = ri.getColorModel();
        final int width = ri.getWidth();
        final int height = ri.getHeight();
        final boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();

        // Extract properties if any.
        final String[] keys = ri.getPropertyNames();
        Hashtable<String, Object> properties = null;
        if (keys != null) {
            properties = new Hashtable<String, Object>(keys.length * 2 + 1);
            for (int i = 0; i < keys.length; i++) {
                properties.put(keys[i], ri.getProperty(keys[i]));
            }
        }

        // Construct BufferedImage and copy (raster) data in.
        final WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
        final BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
        ri.copyData(raster);

        return (result);
    }
*/
}