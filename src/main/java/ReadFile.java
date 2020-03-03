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
            FileInputStream myInputFile = new FileInputStream("D:\\Google Drive\\SUC BSE\\Fundamental Image Processing\\Lectures\\ImejTiff tif\\yoda.tif");
            int value;
            int index = 0;
            while((value = myInputFile.read()) != -1) {
                int dec = value;
                String hex = Integer.toHexString(dec);
                System.out.println("index " + (index) + ": " + hex);
                index++;
            }
            myInputFile.close();
        } catch (IOException e) {
            System.out.println("File error!");
        }
    }
}
