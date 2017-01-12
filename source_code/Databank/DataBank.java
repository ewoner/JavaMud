package Databank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.Hashtable;
import java.util.Set;

/**
 * The basic attribute storage device.  Works right now with any class.  The
 * underlying structure deals with a String Key and a Value of T.  There can not
 * be any VALUES = null.  The DataBank will only return NULLs if the KEY does
 * exsist in the bank.
 *
 * As such, the programer should check for the null value and handle them as
 * needed to control program flow and prevent adverse reactions in the code.
 *
 *
 *
 *  @param <T> Normally a Double, Integer, String or Booloean.  Since the
 * current class just deals with a text file input, this should work.
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *  Version 1.3.0
 * 1.  Removed static final variables for several default attributes as these are applicaion specific.  New method is to declare a seperate class with attributes only.
 * 2.  Changed the underlying data structure from "HashMap" to "Hashtable".  The DataBank is not designed to hold null values.  No changes in the usage of the class.
 * Version 1.2.0
 * 1.  Add static final variables for several default attributes. (at end of class)
 * Version 1.1.0
 * 1. Removed the limitation of only Integer class.  Added support for Double, String, and Boolean as well.
 * 2. Added support for Regular Expressions, backwards compatible with version 1.0.0
 * 3. Added a Formatter to the <code>save(PrintWriter)</code> method.
 * Version 1.0.0
 */
public class DataBank<T> {
//template< typename type >

    /**
     * Used to keep the data and names seperate in the output file.  This
     * version of sep is from Version 1.0.0 but updated in Version 1.1.0 to use
     * Regular Expressions.
     */
    private String sep = "\\s*::\\s*";
    /**
     * Used to keep the data and names seperate in the output file.  New to
     * Version 1.1.0.
     */
    private String newSep = "\\b\\s+";
    /**
     * The main storage collection, holding the name (String) and value (T).
     */
    private Hashtable<String, T> bank = new Hashtable<String, T>();
    /**
     * Checks to see if the DataBank as the following attribute.
     *
     * @param attribute To check.
     * @return TRUE if DataBank contains attribute.
     */
    public boolean has(String attribute) {
        return bank.containsKey(attribute);
    }

    /**
     * Set's either a new or old attribute to value.  If the attribute does
     * exist, it is overwritten.  Calls the add() method to such.
     *
     * @param attribute The Attribute to set.
     * @param value The value to set.
     */
    public void set(String attribute, T value) {
        add(attribute, value);
    }

    /**
     * Gets an attributes value.
     *
     * @param attribute The attribute to get.
     * @return The value of attribute.  Null returns if there is no key maped.
     */
    public T get(String attribute) {
        return bank.get(attribute);
    }

    /**
     * Adds an attribute with value to the DataBank.  Note - This will
     * overwrite an existing attribute with the same name.
     *
     * @param attribute The attribute to add.
     * @param value The value of the attribute.
     */
    public void add(String attribute, T value) {
        bank.put(attribute, value);
    }

    /**
     * Deletes an attribute from the DataBank.
     *
     * @param attribute The attribute to be removed.
     */
    public void del(String attribute) {
        bank.remove(attribute);
    }

    /**
     * Saves a DataBank to a PrintWriter.  Updated in Ver 1.1.0 to use a
     * Formatter.
     *
     * @param writer The writer to save on to.
     */
    public void save(PrintWriter writer) {
        writer.println("[DATABANK]");
        Formatter fmt = new Formatter();
        for (String key : bank.keySet()) {
            writer.printf("%-25s %s\n", key, bank.get(key));
        }
        writer.println("[/DATABANK]");
    }

    /**
     * Loads a DataBank from a BufferedReader.
     *
     * @param reader Reader to load from.
     * @throws java.io.IOException Handled by calling method.
     */
    public void load(BufferedReader reader) throws IOException {
        String temp = reader.readLine();//chews up [databank]
        temp = reader.readLine();//next item
        while (!temp.contains("[/DATABANK]")) {
            String[] results;
            if (temp.contains("::")) {
                results = temp.split(sep);
            } else {
                results = temp.split(newSep);
            }
            T t = parsData(results[1]);
            add(results[0], t);

            temp = reader.readLine();
        }
    }

    @SuppressWarnings("unchecked")
    private T parsData(String data) {
        Boolean testbool = null;
        Integer testint = null;
        Double testdouble = null;
        try {
            testint = Integer.parseInt(data);
            return (T) testint;
        } catch (Exception ex) {
        }
        try {
            testdouble = Double.parseDouble(data);
            return (T) (Object) testdouble;
        } catch (Exception ex) {
        }
        try {
            testbool = Boolean.parseBoolean(data);
            return (T) testbool;
        } catch (Exception ex) {
        }
        return (T) data;
    }

    /**
     * Purges all attributes out of the bank.
     */
    public void purge() {
        bank.clear();
    }

    /**
     * The number of attributes in the DataBank.
     *
     * @return THe DataBank's size
     */
    public int size() {
        return bank.size();
    }

    /**
     * Returns all the names of the attributes stored in the DataBank.
     *
     * @return A Set<String> of all the names.
     */
    public Set<String> getAttributeNames() {
        return bank.keySet();
    }
}
