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
public class Make_Portal_File {

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
            Logger.getLogger(Make_Portal_File.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Make_Portal_File.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
        String[] splitline;
        String regionID;
        String logics = "";
        String databank = "";
        boolean addingentries = false;
        try {
            splitline = reader.readLine().split(",");
            regionID = splitline[1];
            reader.readLine();
            reader.readLine();
            while (reader.ready()) {
                splitline = reader.readLine().split(",");
                for (int i = 0; i < splitline.length; i++) {
                    splitline[i] = splitline[i].replaceAll(";;", ",");
                }
                if (!splitline[0].isEmpty()) {
                    if (addingentries) {
                        addingentries = false;
                        writer.println("[/ENTRIES]");
                        writer.println("[DATABANK]");
                        if (!databank.equalsIgnoreCase("")) {
                            writer.print(databank);
                        }
                        writer.println("[/DATABANK]");
                        writer.println("[LOGICS]");
                        if (!logics.equalsIgnoreCase("")) {
                            writer.print(logics);
                        }
                        writer.println("[/LOGICS]");
                        logics = "";
                        databank = "";
                    }
                    writer.println("[ID]");
                    writer.println(splitline[0]);
                    writer.println("[REGION]");
                    writer.println(regionID);
                    writer.println("[NAME]");
                    writer.println(splitline[2]);
                    writer.println("[DESCRIPTION]");
                    writer.println(splitline[3]);
                    if (splitline.length > 4) {
                        for (int i = 4; i < splitline.length; i++) {
                            logics += splitline[i] + "\n";
                        }
                    }
                } else {
                    addingentries = true;
                    writer.println("[ENTRY]");
                    writer.println("[START ROOM]");
                    writer.println(splitline[1]);
                    writer.println("[DIRECTION]");
                    writer.println(splitline[2]);
                    writer.println("[DEST ROOM]");
                    writer.println(splitline[3]);
                    writer.println("[/ENTRY]");
                    if (splitline.length > 4) {
                        for (int i = 4; i < splitline.length; i += 2) {
                            databank += splitline[i] + "\t\t" + splitline[i + 1] + "\n";
                        }
                    }
                }
            }
            writer.println("[/ENTRIES]");
            writer.println("[DATABANK]");
            if (!databank.equalsIgnoreCase("")) {
                writer.print(databank);
            }
            writer.println("[/DATABANK]");
            writer.println("[LOGICS]");
            if (!logics.equalsIgnoreCase("")) {
                writer.print(logics);
            }
            writer.println("[/LOGICS]");
        } catch (IOException ex) {
            Logger.getLogger(Make_Portal_File.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
        writer.close();
        try {
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(Make_Portal_File.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
        return 0;
    }
}
