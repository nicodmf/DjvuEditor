/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frame.listener;

import frame.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.Timer;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.TaskMonitor;

/**
 *
 * @author Administrateur
 */
public class Loading {

    int messageTimeout;
    ResourceMap resourceMap;
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    public MainFrame mainframe;

    public Loading(final MainFrame mainframe) {
        this.mainframe = mainframe;
        resourceMap = mainframe.getResourceMap();
        messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mainframe.statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                mainframe.statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        mainframe.statusAnimationLabel.setIcon(idleIcon);
        mainframe.progressBar.setVisible(false);
        // connect action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(mainframe.getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        mainframe.statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    mainframe.progressBar.setVisible(true);
                    mainframe.progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    mainframe.statusAnimationLabel.setIcon(idleIcon);
                    mainframe.progressBar.setVisible(false);
                    mainframe.progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    mainframe.statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    mainframe.progressBar.setVisible(true);
                    mainframe.progressBar.setIndeterminate(false);
                    mainframe.progressBar.setValue(value);
                }
            }
        });
    }
}
