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
public class Make_Room_File {

    public static int returncode = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFileChooser chooser = new JFileChooser("f:/D Drive/Java Products/Netbeans/D_and_D_Mud");
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
        String directory = filename.getParent();
        System.out.println(directory);
        File newfilename = new File(directory + "/" + filename.getName().substring(0, filename.getName().indexOf('.')) + ".data");
        System.out.println(newfilename.getName());
        PrintWriter writer;
        BufferedReader reader;
        try {
            writer = new PrintWriter(new FileWriter(newfilename));
        } catch (IOException ex) {
            Logger.getLogger(Make_Room_File.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Make_Room_File.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
        String[] splitline;
        String regionID;
        try {
            splitline = reader.readLine().split(",");
            regionID = splitline[1];
            reader.readLine();
            while (reader.ready()) {
                splitline = reader.readLine().split(",");
                for (int i = 0; i < splitline.length; i++) {
                    splitline[i] = splitline[i].replaceAll(";;", ",");
                }
                writer.println("[ID]");
                writer.println(splitline[0]);
                writer.println("[REGION]");
                writer.println(regionID);
                writer.println("[NAME]");
                writer.println(splitline[1]);
                writer.println("[DESCRIPTION]");
                writer.println(splitline[2]);
                writer.println("[DATABANK]\n[/DATABANK]");
                if (splitline.length > 3) {
                    writer.println("[LOGICS]");
                    for (int i = 3; i < splitline.length; i++) {
                        writer.println(splitline[i]);
                        writer.println("[DATA]\n[/DATA]");
                    }
                    writer.println("[/LOGICS]");
                } else {
                    writer.println("[LOGICS]");
                    writer.println("[/LOGICS]");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Make_Room_File.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
        writer.close();
        try {
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(Make_Room_File.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
        return 0;
    }
}
