package Entities.Templates;

import Databank.DataBank;
import Entities.bases.Entity;
import Entities.interfaces.HasData;
import Entities.interfaces.Template;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * The Character Template represents a template to use to quickly make
 * new instances of a similar character.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class CharacterTemplate extends Entity implements HasData,
        Template {

    private DataBank<Integer> attributes = new DataBank<Integer>();
    private Set<String> commands = new HashSet<String>();
    private Set<String> logics = new HashSet<String>();

    
    
    @Override
    public void load(BufferedReader p_stream) throws IOException   {
        String temp;
        temp = p_stream.readLine();
        temp = p_stream.readLine();
        setName(temp);
        temp = p_stream.readLine();
        temp = p_stream.readLine();
        setDescription(temp);
        getAttributes().load(p_stream);
        temp = p_stream.readLine();
        temp = p_stream.readLine();
        while (!temp.contains("[/COMMANDS]")) {
            commands.add(temp);
            temp = p_stream.readLine();
        }
        temp = p_stream.readLine();
        temp = p_stream.readLine();
        while (!temp.contains("[/LOGICS]")) {
            logics.add(temp);
            temp = p_stream.readLine();

        }
    }

    /**
     * Gets the Name of all commands that are normally with the template
     *
     * @return Set of Command Names
     */
    public Set<String> getTemplateCommands() {
        return commands;
    }

    /**
     *  Gets the names of all the logics that are normally with this template.
     *
     * @return Set of Logic Names.
     */
    public Set<String> getTemplateLogics() {
        return logics;
    }

    @Override
    public int getAttribute(String p_name) {
        return attributes.get(p_name);
    }

    @Override
    public void setAttribute(String p_name, int p_val) {
        attributes.set(p_name, p_val);
    }

    @Override
    public boolean hasAttribute(String p_name) {
        return attributes.has(p_name);
    }

    @Override
    public void addAttribute(String p_name, int p_initialval) {
        attributes.add(p_name, p_initialval);
    }

    @Override
    public void delAttribute(String p_name) {
        attributes.del(p_name);
    }

    @Override
    public DataBank<Integer> getAttributes() {
        return attributes;
    }

    @Override
    public void setAttributes(DataBank<Integer> data) {
        this.attributes = data;
    }

    @Override
    public void save(PrintWriter p_stream)   {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void copyAttributes(DataBank<Integer> attributes){
        for (String key : attributes.getAttributeNames()){
            this.attributes.set(key, attributes.get(key));
        }
    }
}
