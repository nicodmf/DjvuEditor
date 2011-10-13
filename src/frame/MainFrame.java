package frame;

import frame.bean.Viewport;
import frame.filter.DjvuFile;
import frame.listener.Exit;
import frame.listener.Loading;
import frame.listener.TextsMouse;
import frame.tasks.SaveFile;
import frame.tasks.LoadFile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import org.jdesktop.application.Action;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

/**
 * This application is a simple text editor. This class displays the main frame
 * of the application and provides much of the logic. This class is called by
 * the main application class, Application. For an overview of the
 * application see the comments for the Application class.
 */
public class MainFrame extends FrameView implements FocusListener {

    public Application app;
    private boolean modified = false;
    public Viewport vp = null;
    private TextsMouse tml;

    public void setModified(boolean modified) {
        boolean oldValue = this.modified;
        this.modified = modified;
        firePropertyChange("modified", oldValue, this.modified);
    }

    public boolean isModified() {
        //return true;
        return modified;
    }

    public MainFrame(Application app) {
        super(app);
        this.app = app;

        setTitle();

        // generated GUI builder code
        initComponents();
        CentralSplit.setDividerLocation(0.5);
        TextScroller.getVerticalScrollBar().setModel(ImageScroller.getVerticalScrollBar().getModel());
        TextScroller.getHorizontalScrollBar().setModel(ImageScroller.getHorizontalScrollBar().getModel());

        frame.listener.ScrollbarAdjustment listener = new frame.listener.ScrollbarAdjustment(this);
        ImageScroller.getHorizontalScrollBar().addAdjustmentListener(listener);
        ImageScroller.getVerticalScrollBar().addAdjustmentListener(listener);



        search.setVisible(false);
        editMenu.setVisible(false);
        cutToolBarButton.setVisible(false);
        copyToolBarButton.setVisible(false);
        pasteToolBarButton.setVisible(false);
        saveAsMenuItem.setVisible(false);
        main.setVisible(false);
        text.setVisible(false);

        // status bar initialization - message timeout, idle icon and busy animation, etc
        //loading = new Loading(this);

        // ask for confirmation on exit
        getApplication().addExitListener(new Exit(app));
        display.setRequestFocusEnabled(false);
        display.addFocusListener(this);
        
        tml = new TextsMouse(Texts);
        Texts.addMouseListener(tml);
        Texts.addMouseMotionListener(tml);
    }

    public void setTitle() {
        getFrame().setTitle("DejaVu editeur");
    }

    public void setTitle(String filename) {
        getFrame().setTitle(filename + " - DejaVu editeur");
    }

    /**
     * Prompt the user for a filename and then attempt to load the file.
     * <p>
     * The file is loaded on a worker thread because we don't want to
     * block the EDT while the file system is accessed.  To do that,
     * this Action method returns a new LoadFileTask instance, if the
     * user confirms selection of a file.  The task is executed when
     * the "open" Action's actionPerformed method runs.  The
     * LoadFileTask is responsible for updating the GUI after it has
     * successfully completed loading the file.
     * 
     * @return a new LoadFileTask or null
     */
    @Action
    @SuppressWarnings("static-access")
    public Task open() {
        JFileChooser fc = createFileChooser("openFileChooser");
        int option = fc.showOpenDialog(getFrame());
        LoadFile task = null;
        if (JFileChooser.APPROVE_OPTION == option) {
            task = new LoadFile(app, fc.getSelectedFile());
            app.setFile(task.getFile());
            //document = app.getDocument();
            //djVuBean.setDocument(document);

        }
        return task;
    }

    /**
     * Save the contents of the textArea to the current {@link #getFile file}.
     * <p>
     * The text is written to the file on a worker thread because we don't want to 
     * block the EDT while the file system is accessed.  To do that, this
     * Action method returns a new SaveFileTask instance.  The task
     * is executed when the "save" Action's actionPerformed method runs.
     * The SaveFileTask is responsible for updating the GUI after it
     * has successfully completed saving the file.
     * 
     * @see #getFile
     */
    @Action(enabledProperty = "modified")
    public Task save() {
        return new SaveFile(app, app.getFile(), app.getText());
    }

    /**
     * Save the contents of the textArea to the current file.
     * <p>
     * This action is nearly identical to {@link #open open}.  In
     * this case, if the user chooses a file, a {@code SaveFileTask}
     * is returned.  Note that the selected file only becomes the
     * value of the {@code file} property if the file is saved
     * successfully.
     */
    @Action
    public Task saveAs() {
        JFileChooser fc = createFileChooser("saveAsFileChooser");
        int option = fc.showSaveDialog(getFrame());
        Task task = null;
        if (JFileChooser.APPROVE_OPTION == option) {
            task = new SaveFile(app, fc.getSelectedFile(), "");
        }
        return task;
    }

    @Action public void first() { app.first();}
    @Action public void prev() { app.prev();}
    @Action public void next() { app.next();}
    @Action public void last() { app.last();}
    @Action public void setPage() { app.setPage(page.getSelectedIndex() + 1);}
    
    @Action public void zoomIn() { app.zoomIn();}
    @Action public void zoomOut() { app.zoomOut();}
    @Action public void fitWidth() { app.fitWidth();}
    @Action public void fitPage() { app.fitPage();}
    @Action public void fitAll() { app.fitAll();}
    @Action public void setZoom() { app.setZoomInc(zoom.getSelectedIndex());}

    private JFileChooser createFileChooser(String name) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(getResourceMap().getString(name + ".dialogTitle"));
        String textFilesDesc = "DejaVu Files";//getResourceMap().getString("txtFileExtensionDescription");
        fc.setFileFilter(new DjvuFile(textFilesDesc));
        return fc;
    }

    @Override
    public void focusGained(FocusEvent e) {
        Rectangle rect = e.getComponent().getBounds();
        if (e.getComponent().getParent() == Texts) {
            Texts.scrollRectToVisible(rect);
        }
        int a = 2;
        a++;
    }

    @Override
    public void focusLost(FocusEvent e) {
        int a = 1;
        a++;
    }

    javax.swing.Action getAction(String name) {
        ActionMap actionMap = org.jdesktop.application.Application.getInstance(Application.class).getContext().getActionMap(MainFrame.class, this);
        return actionMap.get(name);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new JPanel();
        CentralSplit = new JSplitPane();
        ImageScroller = new JScrollPane();
        ImagePanel = new JPanel();
        Image = new JPanel();
        TextScroller = new JScrollPane();
        TextFlow = new JPanel();
        Texts = new JPanel();
        jTextField1 = new JTextField();
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu();
        JMenuItem openMenuItem = new JMenuItem();
        JMenuItem saveMenuItem = new JMenuItem();
        saveAsMenuItem = new JMenuItem();
        JSeparator fileMenuSeparator = new JSeparator();
        JMenuItem exitMenuItem = new JMenuItem();
        editMenu = new JMenu();
        JMenuItem cutMenuItem = new JMenuItem();
        JMenuItem copyMenuItem = new JMenuItem();
        JMenuItem pasteMenuItem = new JMenuItem();
        JMenu helpMenu = new JMenu();
        JMenuItem aboutMenuItem = new JMenuItem();
        statusPanel = new JPanel();
        JSeparator statusPanelSeparator = new JSeparator();
        statusMessageLabel = new JLabel();
        statusAnimationLabel = new JLabel();
        progressBar = new JProgressBar();
        toolbars = new JToolBar();
        search = new JToolBar();
        rechercher = new JButton();
        rechPremier = new JButton();
        rechPrecedent = new JButton();
        rechSuivant = new JButton();
        rechDernier = new JButton();
        display = new JToolBar();
        voirLargeur = new JButton();
        voir100 = new JButton();
        voirPage = new JButton();
        zoomPlus = new JButton();
        zoomMoins = new JButton();
        zoom = new JComboBox();
        edit = new JToolBar();
        openToolBarButton = new JButton();
        saveToolBarButton = new JButton();
        cutToolBarButton = new JButton();
        copyToolBarButton = new JButton();
        pasteToolBarButton = new JButton();
        main = new JButton();
        text = new JButton();
        nav = new JToolBar();
        pagePremier = new JButton();
        pagePrecedent = new JButton();
        page = new JComboBox();
        pageSuivant = new JButton();
        pageDernier = new JButton();

        mainPanel.setPreferredSize(new Dimension(600, 300));

        CentralSplit.setDividerLocation(350);
        CentralSplit.setOneTouchExpandable(true);
        CentralSplit.setPreferredSize(new Dimension(700, 300));

        ImageScroller.setBorder(null);
        ImageScroller.setAutoscrolls(true);
        ImageScroller.setPreferredSize(new Dimension(600, 300));

        ImagePanel.setLayout(new GridBagLayout());

        Image.setBackground(new Color(255, 255, 255));
        Image.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        Image.setDoubleBuffered(false);
        Image.setPreferredSize(new Dimension(300, 300));

        GroupLayout ImageLayout = new GroupLayout(Image);
        Image.setLayout(ImageLayout);
        ImageLayout.setHorizontalGroup(
            ImageLayout.createParallelGroup(Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
        );
        ImageLayout.setVerticalGroup(
            ImageLayout.createParallelGroup(Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
        );

        ImagePanel.add(Image, new GridBagConstraints());

        ImageScroller.setViewportView(ImagePanel);

        CentralSplit.setLeftComponent(ImageScroller);

        TextScroller.setBorder(null);
        TextScroller.setAutoscrolls(true);

        TextFlow.setLayout(new GridBagLayout());

        Texts.setBackground(new Color(255, 255, 255));
        Texts.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        Texts.setPreferredSize(new Dimension(300, 300));

        jTextField1.setBackground(new Color(255, 204, 102));
        ResourceBundle bundle = ResourceBundle.getBundle("frame/MainFrame"); // NOI18N
        jTextField1.setText(bundle.getString("MainFrame.jTextField1.text")); // NOI18N
        jTextField1.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        jTextField1.setEnabled(false);
        jTextField1.setOpaque(false);

        GroupLayout TextsLayout = new GroupLayout(Texts);
        Texts.setLayout(TextsLayout);
        TextsLayout.setHorizontalGroup(
            TextsLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(TextsLayout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );
        TextsLayout.setVerticalGroup(
            TextsLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(TextsLayout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(109, Short.MAX_VALUE))
        );

        TextFlow.add(Texts, new GridBagConstraints());

        TextScroller.setViewportView(TextFlow);

        CentralSplit.setRightComponent(TextScroller);

        GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(CentralSplit, GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(CentralSplit, GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
        );

        fileMenu.setText(bundle.getString("MainFrame.fileMenu.text")); // NOI18N

        ActionMap actionMap = org.jdesktop.application.Application.getInstance(Application.class).getContext().getActionMap(MainFrame.class, this);
        openMenuItem.setAction(actionMap.get("open")); // NOI18N
        openMenuItem.setText(bundle.getString("MainFrame.openMenuItem.text")); // NOI18N
        fileMenu.add(openMenuItem);

        saveMenuItem.setAction(actionMap.get("save")); // NOI18N
        saveMenuItem.setText(bundle.getString("MainFrame.saveMenuItem.text")); // NOI18N
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setAction(actionMap.get("saveAs")); // NOI18N
        saveAsMenuItem.setText(bundle.getString("MainFrame.saveAsMenuItem.text")); // NOI18N
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(fileMenuSeparator);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setText(bundle.getString("MainFrame.exitMenuItem.text")); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText(bundle.getString("MainFrame.editMenu.text")); // NOI18N

        cutMenuItem.setAction(actionMap.get("cut"));
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        cutMenuItem.setIcon(new ImageIcon(getClass().getResource("/frame/resources/images/cut.png"))); // NOI18N
        cutMenuItem.setText(bundle.getString("MainFrame.cutMenuItem.text")); // NOI18N
        editMenu.add(cutMenuItem);

        copyMenuItem.setAction(actionMap.get("copy"));
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        copyMenuItem.setIcon(new ImageIcon(getClass().getResource("/frame/resources/images/copy.png"))); // NOI18N
        copyMenuItem.setText(bundle.getString("MainFrame.copyMenuItem.text")); // NOI18N
        editMenu.add(copyMenuItem);

        pasteMenuItem.setAction(actionMap.get("paste"));
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        pasteMenuItem.setIcon(new ImageIcon(getClass().getResource("/frame/resources/images/paste.png"))); // NOI18N
        pasteMenuItem.setText(bundle.getString("MainFrame.pasteMenuItem.text")); // NOI18N
        editMenu.add(pasteMenuItem);

        menuBar.add(editMenu);

        helpMenu.setText(bundle.getString("MainFrame.helpMenu.text")); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setText(bundle.getString("MainFrame.aboutMenuItem.text")); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusAnimationLabel.setHorizontalAlignment(SwingConstants.LEFT);

        GroupLayout statusPanelLayout = new GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(statusPanelSeparator, GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(ComponentPlacement.RELATED, 548, Short.MAX_VALUE)
                .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        toolbars.setFloatable(false);
        toolbars.setRollover(true);
        toolbars.setAutoscrolls(true);
        toolbars.setMaximumSize(new Dimension(695, 30));
        toolbars.setMinimumSize(new Dimension(655, 30));
        toolbars.setPreferredSize(new Dimension(100, 30));

        search.setRollover(true);
        search.setPreferredSize(new Dimension(100, 28));

        rechercher.setIcon(new ImageIcon(getClass().getResource("/frame/resources/images/search.gif"))); // NOI18N
        rechercher.setToolTipText(bundle.getString("MainFrame.rechercher.toolTipText")); // NOI18N
        rechercher.setFocusable(false);
        rechercher.setHorizontalTextPosition(SwingConstants.CENTER);
        rechercher.setMaximumSize(new Dimension(24, 24));
        rechercher.setMinimumSize(new Dimension(24, 24));
        rechercher.setPreferredSize(new Dimension(24, 24));
        rechercher.setVerticalTextPosition(SwingConstants.BOTTOM);
        search.add(rechercher);

        rechPremier.setIcon(new ImageIcon(getClass().getResource("/frame/resources/images/searchbackdoc.gif"))); // NOI18N
        rechPremier.setToolTipText(bundle.getString("MainFrame.rechPremier.toolTipText")); // NOI18N
        rechPremier.setFocusable(false);
        rechPremier.setHorizontalTextPosition(SwingConstants.CENTER);
        rechPremier.setMaximumSize(new Dimension(24, 24));
        rechPremier.setMinimumSize(new Dimension(24, 24));
        rechPremier.setPreferredSize(new Dimension(24, 24));
        rechPremier.setVerticalTextPosition(SwingConstants.BOTTOM);
        search.add(rechPremier);

        rechPrecedent.setIcon(new ImageIcon(getClass().getResource("/frame/resources/images/searchback.gif"))); // NOI18N
        rechPrecedent.setToolTipText(bundle.getString("MainFrame.rechPrecedent.toolTipText")); // NOI18N
        rechPrecedent.setFocusable(false);
        rechPrecedent.setHorizontalTextPosition(SwingConstants.CENTER);
        rechPrecedent.setMaximumSize(new Dimension(24, 24));
        rechPrecedent.setMinimumSize(new Dimension(24, 24));
        rechPrecedent.setPreferredSize(new Dimension(24, 24));
        rechPrecedent.setVerticalTextPosition(SwingConstants.BOTTOM);
        search.add(rechPrecedent);

        rechSuivant.setIcon(new ImageIcon(getClass().getResource("/frame/resources/images/searchfwd.gif"))); // NOI18N
        rechSuivant.setToolTipText(bundle.getString("MainFrame.rechSuivant.toolTipText")); // NOI18N
        rechSuivant.setFocusable(false);
        rechSuivant.setHorizontalTextPosition(SwingConstants.CENTER);
        rechSuivant.setMaximumSize(new Dimension(24, 24));
        rechSuivant.setMinimumSize(new Dimension(24, 24));
        rechSuivant.setVerticalTextPosition(SwingConstants.BOTTOM);
        search.add(rechSuivant);

        rechDernier.setIcon(new ImageIcon(getClass().getResource("/frame/resources/images/searchfwddoc.gif"))); // NOI18N
        rechDernier.setToolTipText(bundle.getString("MainFrame.rechDernier.toolTipText")); // NOI18N
        rechDernier.setFocusable(false);
        rechDernier.setHorizontalTextPosition(SwingConstants.CENTER);
        rechDernier.setMaximumSize(new Dimension(24, 24));
        rechDernier.setMinimumSize(new Dimension(24, 24));
        rechDernier.setPreferredSize(new Dimension(24, 24));
        rechDernier.setVerticalTextPosition(SwingConstants.BOTTOM);
        search.add(rechDernier);

        toolbars.add(search);

        display.setRollover(true);
        display.setMaximumSize(new Dimension(210, 30));
        display.setMinimumSize(new Dimension(200, 24));
        display.setPreferredSize(new Dimension(100, 24));

        voirLargeur.setAction(actionMap.get("fitWidth")); // NOI18N
        voirLargeur.setFocusable(false);
        voirLargeur.setHorizontalTextPosition(SwingConstants.CENTER);
        voirLargeur.setMaximumSize(new Dimension(24, 24));
        voirLargeur.setMinimumSize(new Dimension(24, 24));
        voirLargeur.setPreferredSize(new Dimension(24, 24));
        voirLargeur.setVerticalTextPosition(SwingConstants.BOTTOM);
        display.add(voirLargeur);

        voir100.setAction(actionMap.get("fitAll")); // NOI18N
        voir100.setFocusable(false);
        voir100.setHorizontalTextPosition(SwingConstants.CENTER);
        voir100.setMaximumSize(new Dimension(24, 24));
        voir100.setMinimumSize(new Dimension(24, 24));
        voir100.setPreferredSize(new Dimension(24, 24));
        voir100.setVerticalTextPosition(SwingConstants.BOTTOM);
        display.add(voir100);

        voirPage.setAction(actionMap.get("fitPage")); // NOI18N
        voirPage.setFocusable(false);
        voirPage.setHorizontalTextPosition(SwingConstants.CENTER);
        voirPage.setMaximumSize(new Dimension(24, 24));
        voirPage.setMinimumSize(new Dimension(24, 24));
        voirPage.setPreferredSize(new Dimension(24, 24));
        voirPage.setVerticalTextPosition(SwingConstants.BOTTOM);
        display.add(voirPage);

        zoomPlus.setAction(actionMap.get("zoomIn")); // NOI18N
        zoomPlus.setFocusable(false);
        zoomPlus.setHorizontalTextPosition(SwingConstants.CENTER);
        zoomPlus.setMaximumSize(new Dimension(24, 24));
        zoomPlus.setMinimumSize(new Dimension(24, 24));
        zoomPlus.setPreferredSize(new Dimension(24, 24));
        zoomPlus.setVerticalTextPosition(SwingConstants.BOTTOM);
        display.add(zoomPlus);

        zoomMoins.setAction(actionMap.get("zoomOut")); // NOI18N
        zoomMoins.setFocusable(false);
        zoomMoins.setHorizontalTextPosition(SwingConstants.CENTER);
        zoomMoins.setMaximumSize(new Dimension(24, 24));
        zoomMoins.setMinimumSize(new Dimension(24, 24));
        zoomMoins.setPreferredSize(new Dimension(24, 24));
        zoomMoins.setVerticalTextPosition(SwingConstants.BOTTOM);
        display.add(zoomMoins);

        zoom.setModel(new DefaultComboBoxModel(new String[] { "Zoom..." }));
        zoom.setAction(actionMap.get("setZoom")); // NOI18N
        zoom.setMaximumSize(new Dimension(65, 22));
        zoom.setMinimumSize(new Dimension(65, 22));
        display.add(zoom);

        toolbars.add(display);

        edit.setRollover(true);
        edit.setMaximumSize(new Dimension(190, 30));
        edit.setMinimumSize(new Dimension(190, 30));
        edit.setName(""); // NOI18N
        edit.setPreferredSize(new Dimension(190, 30));

        openToolBarButton.setAction(actionMap.get("open")); // NOI18N
        openToolBarButton.setText(bundle.getString("MainFrame.openToolBarButton.text")); // NOI18N
        openToolBarButton.setToolTipText(bundle.getString("MainFrame.openToolBarButton.toolTipText")); // NOI18N
        openToolBarButton.setFocusable(false);
        openToolBarButton.setHorizontalTextPosition(SwingConstants.CENTER);
        openToolBarButton.setMaximumSize(new Dimension(24, 30));
        openToolBarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        edit.add(openToolBarButton);

        saveToolBarButton.setAction(actionMap.get("save")); // NOI18N
        saveToolBarButton.setToolTipText(bundle.getString("MainFrame.saveToolBarButton.toolTipText")); // NOI18N
        saveToolBarButton.setFocusable(false);
        saveToolBarButton.setHorizontalTextPosition(SwingConstants.CENTER);
        saveToolBarButton.setMaximumSize(new Dimension(24, 30));
        saveToolBarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        edit.add(saveToolBarButton);

        cutToolBarButton.setIcon(new ImageIcon(getClass().getResource("/frame/resources/images/cut.png"))); // NOI18N
        cutToolBarButton.setToolTipText(bundle.getString("MainFrame.cutToolBarButton.toolTipText")); // NOI18N
        cutToolBarButton.setFocusable(false);
        cutToolBarButton.setHorizontalTextPosition(SwingConstants.CENTER);
        cutToolBarButton.setMaximumSize(new Dimension(24, 24));
        cutToolBarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        edit.add(cutToolBarButton);

        copyToolBarButton.setAction(actionMap.get("copy"));
        copyToolBarButton.setIcon(new ImageIcon(getClass().getResource("/frame/resources/images/copy.png"))); // NOI18N
        copyToolBarButton.setToolTipText(bundle.getString("MainFrame.copyToolBarButton.toolTipText")); // NOI18N
        copyToolBarButton.setFocusable(false);
        copyToolBarButton.setHorizontalTextPosition(SwingConstants.CENTER);
        copyToolBarButton.setMaximumSize(new Dimension(24, 24));
        copyToolBarButton.setMinimumSize(new Dimension(24, 24));
        copyToolBarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        edit.add(copyToolBarButton);

        pasteToolBarButton.setAction(actionMap.get("paste"));
        pasteToolBarButton.setIcon(new ImageIcon(getClass().getResource("/frame/resources/images/paste.png"))); // NOI18N
        pasteToolBarButton.setToolTipText(bundle.getString("MainFrame.pasteToolBarButton.toolTipText")); // NOI18N
        pasteToolBarButton.setFocusable(false);
        pasteToolBarButton.setHorizontalTextPosition(SwingConstants.CENTER);
        pasteToolBarButton.setMaximumSize(new Dimension(24, 24));
        pasteToolBarButton.setMinimumSize(new Dimension(24, 24));
        pasteToolBarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        edit.add(pasteToolBarButton);

        main.setIcon(new ImageIcon(getClass().getResource("/frame/resources/images/hand.gif"))); // NOI18N
        main.setToolTipText(bundle.getString("MainFrame.main.toolTipText")); // NOI18N
        main.setFocusable(false);
        main.setHorizontalTextPosition(SwingConstants.CENTER);
        main.setMaximumSize(new Dimension(24, 24));
        main.setMinimumSize(new Dimension(24, 24));
        main.setPreferredSize(new Dimension(24, 24));
        main.setVerticalTextPosition(SwingConstants.BOTTOM);
        edit.add(main);

        text.setIcon(new ImageIcon(getClass().getResource("/frame/resources/images/textselect.gif"))); // NOI18N
        text.setToolTipText(bundle.getString("MainFrame.text.toolTipText")); // NOI18N
        text.setFocusable(false);
        text.setHorizontalTextPosition(SwingConstants.CENTER);
        text.setMaximumSize(new Dimension(24, 24));
        text.setMinimumSize(new Dimension(24, 24));
        text.setPreferredSize(new Dimension(24, 24));
        text.setVerticalTextPosition(SwingConstants.BOTTOM);
        edit.add(text);

        toolbars.add(edit);

        nav.setRollover(true);
        nav.setMinimumSize(new Dimension(173, 24));
        nav.setPreferredSize(new Dimension(100, 24));

        pagePremier.setAction(actionMap.get("first")); // NOI18N
        pagePremier.setFocusable(false);
        pagePremier.setHorizontalTextPosition(SwingConstants.CENTER);
        pagePremier.setMaximumSize(new Dimension(24, 24));
        pagePremier.setMinimumSize(new Dimension(24, 24));
        pagePremier.setPreferredSize(new Dimension(24, 24));
        pagePremier.setVerticalTextPosition(SwingConstants.BOTTOM);
        nav.add(pagePremier);

        pagePrecedent.setAction(actionMap.get("prev")); // NOI18N
        pagePrecedent.setFocusable(false);
        pagePrecedent.setHorizontalTextPosition(SwingConstants.CENTER);
        pagePrecedent.setMaximumSize(new Dimension(24, 24));
        pagePrecedent.setMinimumSize(new Dimension(24, 24));
        pagePrecedent.setPreferredSize(new Dimension(24, 24));
        pagePrecedent.setVerticalTextPosition(SwingConstants.BOTTOM);
        nav.add(pagePrecedent);

        page.setModel(new DefaultComboBoxModel(new String[] { "Pages..." }));
        page.setAction(actionMap.get("setPage")); // NOI18N
        page.setActionCommand(bundle.getString("MainFrame.page.actionCommand")); // NOI18N
        page.setMaximumSize(new Dimension(57, 22));
        nav.add(page);

        pageSuivant.setAction(actionMap.get("next")); // NOI18N
        pageSuivant.setFocusable(false);
        pageSuivant.setHorizontalTextPosition(SwingConstants.CENTER);
        pageSuivant.setMaximumSize(new Dimension(24, 24));
        pageSuivant.setMinimumSize(new Dimension(24, 24));
        pageSuivant.setPreferredSize(new Dimension(24, 24));
        pageSuivant.setVerticalTextPosition(SwingConstants.BOTTOM);
        nav.add(pageSuivant);

        pageDernier.setAction(actionMap.get("last")); // NOI18N
        pageDernier.setFocusable(false);
        pageDernier.setHorizontalTextPosition(SwingConstants.CENTER);
        pageDernier.setMaximumSize(new Dimension(24, 24));
        pageDernier.setMinimumSize(new Dimension(24, 24));
        pageDernier.setPreferredSize(new Dimension(24, 24));
        pageDernier.setVerticalTextPosition(SwingConstants.BOTTOM);
        nav.add(pageDernier);

        toolbars.add(nav);

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
        setToolBar(toolbars);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public JSplitPane CentralSplit;
    public JPanel Image;
    public JPanel ImagePanel;
    public JScrollPane ImageScroller;
    private JPanel TextFlow;
    private JScrollPane TextScroller;
    public JPanel Texts;
    private JButton copyToolBarButton;
    private JButton cutToolBarButton;
    private JToolBar display;
    private JToolBar edit;
    private JMenu editMenu;
    private JTextField jTextField1;
    private JButton main;
    private JPanel mainPanel;
    private JMenuBar menuBar;
    private JToolBar nav;
    private JButton openToolBarButton;
    public JComboBox page;
    private JButton pageDernier;
    private JButton pagePrecedent;
    private JButton pagePremier;
    private JButton pageSuivant;
    private JButton pasteToolBarButton;
    public JProgressBar progressBar;
    private JButton rechDernier;
    private JButton rechPrecedent;
    private JButton rechPremier;
    private JButton rechSuivant;
    private JButton rechercher;
    private JMenuItem saveAsMenuItem;
    private JButton saveToolBarButton;
    private JToolBar search;
    public JLabel statusAnimationLabel;
    public JLabel statusMessageLabel;
    private JPanel statusPanel;
    private JButton text;
    private JToolBar toolbars;
    private JButton voir100;
    private JButton voirLargeur;
    private JButton voirPage;
    public JComboBox zoom;
    private JButton zoomMoins;
    private JButton zoomPlus;
    // End of variables declaration//GEN-END:variables
    private JDialog aboutBox;
    private Loading loading;
    private static final Logger logger = Logger.getLogger(MainFrame.class.getName());
}
