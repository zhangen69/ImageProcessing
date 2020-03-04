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

public class ReadFile {
    public static void main(String[] args) {
        try {
            File myFile = new File("yoda_out.tif");
            FileOutputStream fop = new FileOutputStream(myFile);
            FileInputStream myInputFile = new FileInputStream("yoda.tif");
            int value;
            int index = 0;
            while((value = myInputFile.read()) != -1) {
//                int dec = value;
//                String hex = Integer.toHexString(dec);
//                System.out.println("index " + (index) + ": " + hex);
//                index++;
                fop.write(value);
            }
            fop.close();
            myInputFile.close();
        } catch (IOException e) {
            System.out.println("File error!");
        }
    }
}
