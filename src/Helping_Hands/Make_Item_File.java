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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class Make_Item_File {

    public static int returncode = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFileChooser chooser = new JFileChooser("D:/Java Products/Netbeans/D_and_D_Mud");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "CSV files", "csv");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(new JFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
            returncode = convertfile(chooser.getSelectedFile());
        }
        System.exit(returncode);

    }

    private static int convertfile(File filename) {
        File newfilename = new File(filename.getParent() + "/" + filename.getName().substring(0, filename.getName().indexOf('.')) + ".data");
        System.out.println(newfilename.getName());
        PrintWriter writer;
        BufferedReader reader;
        try {
            writer = new PrintWriter(new FileWriter(newfilename));
        } catch (IOException ex) {
            Logger.getLogger(Make_Item_File.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Make_Item_File.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
        String[] splitline;
        try {
            reader.readLine();
            while (reader.ready()) {
                splitline = reader.readLine().split(",");
                writer.println("[ID]");
                writer.println(splitline[0]);
                writer.println("[NAME]");
                writer.println(splitline[1]);
                writer.println("[DESCRIPTION]");
                writer.println(splitline[2]);
                writer.println("[ISQUANTITY]");
                writer.println(splitline[3]);
                writer.println("[QUANTITY]");
                writer.println(splitline[4]);
                writer.println("[DATABANK]");
                writer.println("value\t\t\t\t::" + splitline[5]);
                writer.println("weight\t\t\t::" + splitline[6]);
                writer.println("size\t\t\t\t::" + splitline[7]);
                if (splitline.length >= 9) {
                    if (!splitline[8].equals("")) {
                        writer.println("capacity\t\t\t::" + splitline[8]);
                    }
                }
                if (splitline.length >= 11) {
                    for (int c = 9; c + 1 < splitline.length; c += 2) {
                        writer.println(splitline[c] + "\t\t\t::" + splitline[c + 1]);
                    }
                }
                writer.println("[/DATABANK]\n[LOGICS]\n[/LOGICS]");
            }
        } catch (IOException ex) {
            Logger.getLogger(Make_Item_File.class.getName()).log(Level.SEVERE, null, ex);
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
