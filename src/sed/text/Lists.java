/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sed.text;

import sed.text.parts.Part;
import java.util.ArrayList;

/**
 *
 * @author Administrateur
 */
public class Lists {

    public static ArrayList<Part> pages = new ArrayList<Part>();
    public static ArrayList<Part> lines = new ArrayList<Part>();
    public static ArrayList<Part> words = new ArrayList<Part>();

    public static void reinit() {
        pages = new ArrayList<Part>();
        lines = new ArrayList<Part>();
        words = new ArrayList<Part>();
    }
}
