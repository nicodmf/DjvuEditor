/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package djvusedtojava;

import api.Arguments;
import api.Djvused;
import api.Document;
import api.Parser;
import com.beust.jcommander.JCommander;
import java.io.File;

/**
 *
 * @author Administrateur
 */
public class DjVuSedToJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //System.out.println(args.length);

        Arguments jct = new Arguments();
        JCommander jc = new JCommander(jct);

        String[] argv = { "-l", "coriolis.djvu" };

        try{
            jc.parse(argv);
        }catch(Exception e){
            System.out.print(e.getLocalizedMessage());
            System.out.println();
            jc.usage();
        }
        
        File file = new File(jct.files.get(0));
        //System.out.print("test"+jct.load);
        if(jct.load==true){
            Djvused sed = new Djvused(file);
            Document doc = new Document();
            //System.out.println(sed.getText());
            String text = sed.getText();
            Parser.parse(doc, text);
            System.out.print("Loaded");
        }

        //Assert assert = new Assert();//.assertEquals(jct, 2);

    }
}
