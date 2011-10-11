/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frame.listener;

import frame.MainFrame;
import java.awt.Adjustable;
import java.awt.Component;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 *
 * @author Administrateur
 */
public class ScrollbarAdjustment implements AdjustmentListener {
    // This method is called whenever the value of a scrollbar is changed,
    // either by the user or programmatically.
    MainFrame mainFrame;
        static int i=0;
    public ScrollbarAdjustment(MainFrame mainframe){
        mainFrame = mainframe;
    }
    @Override
    public void adjustmentValueChanged(AdjustmentEvent evt) {
        Adjustable source = evt.getAdjustable();

        // getValueIsAdjusting() returns true if the user is currently
        // dragging the scrollbar's knob and has not picked a final value
        if (evt.getValueIsAdjusting()) {
            // The user is dragging the knob
            return;
        }

        // Determine which scrollbar fired the event
        int orient = source.getOrientation();
        if (orient == Adjustable.HORIZONTAL) {
            // Event from horizontal scrollbar
        } else {
            // Event from vertical scrollbar
        }

        // Determine the type of event
        int type = evt.getAdjustmentType();
        switch (type) {
          case AdjustmentEvent.UNIT_INCREMENT:
              // Scrollbar was increased by one unit
              break;
          case AdjustmentEvent.UNIT_DECREMENT:
              // Scrollbar was decreased by one unit
              break;
          case AdjustmentEvent.BLOCK_INCREMENT:
              // Scrollbar was increased by one block
              break;
          case AdjustmentEvent.BLOCK_DECREMENT:
              // Scrollbar was decreased by one block
              break;
          case AdjustmentEvent.TRACK:
              // The knob on the scrollbar was dragged
              break;
        }

        // Get current value
        int value = evt.getValue();
                /*mainFrame.getRootPane().repaint();
                mainFrame.getRootPane().revalidate();
                mainFrame.ImageScroller.repaint();
                mainFrame.ImageScroller.revalidate();
                mainFrame.Image.repaint();
                mainFrame.Image.revalidate();
                mainFrame.ImagePanel.repaint();
                mainFrame.ImagePanel.revalidate();*/
/*                mainFrame.vp.update(mainFrame.vp.getGraphics());
                mainFrame.vp.paint(mainFrame.vp.getGraphics());
                mainFrame.vp.paintAll(mainFrame.vp.getGraphics());
                try{
                mainFrame.vp.paintComponent(mainFrame.vp.getGraphics());
                mainFrame.vp.paintComponents(mainFrame.vp.getGraphics());
                }catch(Exception e){
                    
                }
                i++;
                System.out.println("move scrollbar"+i);*/
                //mainFrame.vp.recursiveRevalidate();
                //getFrame().repaint();
        //mainFrame.getFrame().revalidate();//(mainFrame.Texts.getGraphics());
/*            try
    {
      for(
        Component component = mainFrame.getComponent();
        (component != null) && (component.isShowing());
        component = component.getParent())
      {
        component.validate();
      }
    }
    catch(final Throwable ignored) {}
*/
    }
}