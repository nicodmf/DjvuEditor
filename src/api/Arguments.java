/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrateur
 */
public class Arguments {

    @Parameter(
      description = "<The djvu file>",
      arity=1,
      required=true)
    public List<String> files = new ArrayList<String>();

    @Parameter(
      names = {"save", "-save", "-s"},
      description = "Save a file with specifics commands in the djvu file")
    public String saveFile;

    @Parameter(
      names = { "load", "-load", "-l"},
      description = "Load the commands given by the djvu file")
    public boolean load=false;
}
