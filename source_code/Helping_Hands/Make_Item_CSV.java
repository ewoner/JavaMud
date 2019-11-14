/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Helping_Hands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class Make_Item_CSV {

    static File dir = new File("data/templates/items/");

    /**
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        if (!dir.isDirectory()) {
            System.out.println("Not a direcotry");
            System.exit(1);
        }
        for (File f : dir.listFiles()) {
            int result = saveFile(f);
            if (result > 0) {
                System.exit(result);
            }
        }
        System.exit(0);
    }

    private static int saveFile(File file) {
        String newSep = "\\b\\s+";
        String sep = "\\s*::\\s*";

        File savefilename = new File(dir.getName() + file.getName().substring(0, file.getName().indexOf('.')) + ".csv");
        PrintWriter writer;
        BufferedReader reader;
        try {
            writer = new PrintWriter(new FileWriter(savefilename));
        } catch (IOException ex) {
            Logger.getLogger(Make_Item_File.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Make_Item_File.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
        try {
            writer.println("ID,NAME,DESC,isquantity,quantity,value,weight,size");
            while (reader.ready()) {
                //ID
                reader.readLine();
                writer.print(reader.readLine() + ",");
                reader.readLine();
                writer.print(reader.readLine() + ",");
                reader.readLine();
                writer.print(reader.readLine() + ",");
                reader.readLine();
                writer.print(reader.readLine() + ",");
                reader.readLine();
                writer.print(reader.readLine() + ",");

                // save my attributes to disk
                String temp = reader.readLine();//chews up [databank]
                temp = reader.readLine();//next item
                while (!temp.contains("[/DATABANK]")) {
                    String[] results;
                    if (temp.contains("::")) {
                        results = temp.split(sep);
                    } else {
                        results = temp.split(newSep);
                    }
                    writer.print(results[0] + "," + results[1] + ",");
                    temp = reader.readLine();
                }
                // save my logic
                temp = reader.readLine();
                temp = reader.readLine();
                while (!temp.contains("[/LOGICS]")) {
                    writer.print(temp + ",");
                    temp = reader.readLine();
                }
                writer.println();
            }
        } catch (IOException ex) {
            Logger.getLogger(Make_Item_CSV.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
        writer.close();
        try {
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(Make_Item_File.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
        return 0;
    }
}
