/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sed.text.parts;

/**
 *
 * @author Administrateur
 */
public class StringWord extends Part {

    String word;
    
    @Override
    public String start() {
        return "\"";
    }

    @Override
    public String end() {
        return "\"";
    }

    @Override
    public String save() {
        return "\"" + word + "\"";
    }

    public String loadWord(String string) {
        int end = string.indexOf("\"",1);
        try{
            word = string.substring(0, end).replaceAll("__xx__","\\\\\"");
        }catch(Exception e){
            return "";
        }
        return string.substring(end+1);
    }
}
