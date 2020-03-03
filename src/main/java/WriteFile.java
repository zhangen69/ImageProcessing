/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
import java.io.*;

public class WriteFile {
    public static void main(String[] args) {
        try {
            // File myOutputFile
//            FileOutputStream myOutputFile = new FileOutputStream("myFile.txt");
//            myOutputFile.write(1);
//            myOutputFile.close();
            File myFile = new File("myFile.tif");
            FileOutputStream fop = new FileOutputStream(myFile);
            // get the content in bytes
            byte[] contentInBytes = ("This is some content").getBytes();
            fop.write(123);
            fop.write(contentInBytes);
            fop.flush(); // force to write the file from the buffer
            fop.close();
            System.out.println("Done!");
        } catch (IOException ex) {
            System.out.println("File Output Error, failed to create the file 'myFile.txt'");
        }
    }
}
