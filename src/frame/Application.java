/*
 * Application.java
 */
package frame;

import frame.bean.Viewport;
import api.Djvused;
import api.Document;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollBar;
import org.jdesktop.application.SingleFrameApplication;

public class Application extends SingleFrameApplication {

    private File file;
    //private TiffReader images;
    private int page = -1;
    private int nbPages = -1;
    private double zoom = 0.2;
    private double actualDpi = 100;
    private int zoomInc = 2;
    double[] zooms = {0.05, 0.1, 0.2, 0.33, 0.5, 1.0, 2.0, 4.0, 6.0, 12.0};
    Viewport bean;
    private MainFrame mainFrame;
    private Document document;
    private boolean modified = false;
    private Djvused sed;
    private Document doc;
    private Dimension dims = null;
    private int[] pageDimensions;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        mainFrame = new MainFrame(this);
        sed = new Djvused();
        doc = new Document();
        bean = new Viewport();
        mainFrame.vp = bean;

        show(mainFrame);
        //TODO: drop after test
        mainFrame.Image.add(bean);

        //images = new TiffReader();
        setFile(new File("D:\\dev\\java\\DjVuSedToJava\\coriolis2.djvu"));
//        mainFrame.Image.add(images.getPanel(page, page, page));*/
    }

    public MainFrame getFrame() {
        return mainFrame;
    }

    public String getText() {
        return doc.save();
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of Application
     */
    static public Application getApplication() {
        return Application.getInstance(Application.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(Application.class, args);

    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        mainFrame.zoom.setEnabled(true);
        mainFrame.page.setEnabled(true);
        if (file == this.file) {
            return;
        }
        firePropertyChange("file", this.file, file);
        this.file = file;
        sed.setFile(file);
        //Dimensions
        int[][] dims = sed.getDimensions();
        nbPages = dims.length;
        //Shadow texts
        doc.parse(sed.getText());
        //Pages in combo
        String[] itemsPage = new String[nbPages];
        String[] itemsZoom = new String[zooms.length];
        for (int i = 0; i < nbPages; i++) {
            itemsPage[i] = "Page " + (i + 1);
        }
        for (int i = 0; i < zooms.length; i++) {
            itemsZoom[i] = (zooms[i] * 100) + "%";
        }
        mainFrame.page.setModel(new DefaultComboBoxModel(itemsPage));
        mainFrame.zoom.setModel(new DefaultComboBoxModel(itemsZoom));


        page = 1;
        String str;

        try {
            bean.setURL(file.toURL());
        } catch (IOException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
        bean.setScrollbar(JScrollBar.HORIZONTAL, mainFrame.ImageScroller.getHorizontalScrollBar());
        bean.setScrollbar(JScrollBar.VERTICAL, mainFrame.ImageScroller.getVerticalScrollBar());


        bean.setPage(page);

        //bean.
        setPage(page);
        fitPage();
        mainFrame.setTitle(file.getName());


    }

    public void setPage(int page) {
        this.page = page;
        int realPage = page - 1;
        pageDimensions = sed.getDimensions()[realPage];

        int width = (int) (pageDimensions[0] * zoom);
        int height = (int) (pageDimensions[1] * zoom);
        dims = new Dimension(width, height);

        mainFrame.Texts.setPreferredSize(dims);
        mainFrame.Image.setPreferredSize(dims);
//        mainFrame.Image.setVisible(false);
        bean.setPage(page);

        String z = String.valueOf((int) (zoom * 100 * bean.getDPI() / actualDpi) + "%");
        bean.setZoom(z);
        Dimension d = new Dimension(pageDimensions[0], pageDimensions[1]);
        //mainFrame.ImagePanel.setPreferredSize(dims);
        //mainFrame.ImagePanel.setPreferredSize(dims);
        bean.setSize(dims);
        bean.setPreferredSize(dims);
        bean.setMinimumSize(dims);
        //bean.setImageSize(d);
        bean.setViewportSize(dims);
        bean.setVisible(true);


        mainFrame.page.setSelectedIndex(realPage);

        doc.showPage(page, mainFrame, zoom, pageDimensions);

        mainFrame.CentralSplit.setDividerLocation(0.5);//getWidth();
        //fitWidth();



        mainFrame.getFrame().repaint();
        mainFrame.getFrame().revalidate();

    }

    public void setModified(boolean modified) {
        if (this.modified != modified) {
            mainFrame.setModified(modified);
            firePropertyChange("modified", modified, this.modified);
            this.modified = modified;
        }
    }

    public boolean isModified() {
        return modified;
    }

    public Document getDocument() {
        return document;
    }

    public void first() {
        setPage(1);
    }

    public void prev() {
        if (page - 1 >= 1) {
            setPage(page - 1);
        }
    }

    public void next() {
        if (page + 1 <= nbPages) {
            setPage(page + 1);
        }
    }

    public void last() {
        setPage(nbPages);
    }

    public void zoomIn() {
        if (zoomInc < zooms.length - 1) {
            zoomInc += 1;
            zoom = zooms[zoomInc];
            setZoom();
        }
    }

    public void zoomOut() {
        if (zoomInc > 0) {
            zoomInc -= 1;
            zoom = zooms[zoomInc];
            setZoom();
        }
    }

    public void fitPage() {
        double width = mainFrame.CentralSplit.getWidth() / 2 - 15;
        double height = mainFrame.CentralSplit.getHeight() - 15;
        setZoom(Math.min(width / pageDimensions[0], height / pageDimensions[1]));
    }

    public void fitAll() {
        setZoom(1);
    }

    public void fitWidth() {
        double width = mainFrame.CentralSplit.getWidth() / 2 - 30;
        setZoom(width / pageDimensions[0]);
    }

    public void setZoomInc(int zoomInc) {
        this.zoomInc = zoomInc;
        zoom = zooms[zoomInc];
        setZoom();
    }

    public void setZoom(double zoom) {
        //if(zoom==this.zoom)return;
        boolean pass = false;
        for (int i = 0; i < zooms.length; i++) {
            if (zooms[i] <= zoom && zoom < zooms[i + 1]) {
                //if(this.zoom == zooms[i]) return;
                this.zoom = zoom;
                zoomInc = i;
                pass = true;
                break;
            }
        }
        if (pass == false) {
            if (zoom > zooms[0]) {
                zoomInc = zooms.length - 1;
            } else {
                zoomInc = 0;
            }
            this.zoom = zoom;
        }
        setZoom();
    }

    public void setZoom() {
        mainFrame.zoom.setAction(mainFrame.getAction(""));
        mainFrame.zoom.setSelectedIndex(zoomInc);
        mainFrame.zoom.setAction(mainFrame.getAction("setZoom"));
        setPage(page);
    }
}
