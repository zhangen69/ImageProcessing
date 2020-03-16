
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author User
 */
public class Assignment4 {

    public static void main(String[] args) {
        String srcFileName = "yoda.raw";
//        String srcFileName = "bedroom.raw";
//        String srcFileName = "Imgpro.raw";
        File file = new File(srcFileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            String fileName = file.getName();
            int fileSize = (int) file.length();
            int weight = 123;
            int height = 62;
            int[][] originalImgArr = new int[height][weight];
            int[][] convolutionImgArr = new int[height][weight];

            System.out.println("File Name: " + fileName);
            System.out.println("File Size: " + fileSize);

            int value;

            int colCount = 0;
            int rowCount = 0;

            int[][] hMatric = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1},};

            while ((value = fis.read()) != -1) {
//                System.out.println("Row: " + rowCount + " Col: " + colCount);
                originalImgArr[rowCount][colCount] = value;

                if (colCount == weight - 1) {
                    colCount = 0;
                    rowCount++;
                } else if (rowCount < height) {
                    colCount++;
                } else {
                    throw new Error("something went wrong...");
                }
            }

            File outputFile = new File("assignment4.raw");
            FileOutputStream fout = new FileOutputStream(outputFile);

            for (int rowIndex = 1; rowIndex < originalImgArr.length - 2; rowIndex++) {
                for (int colIndex = 1; colIndex < originalImgArr[rowIndex].length - 2; colIndex++) {
                    int kernel1 = hMatric[0][0] * originalImgArr[rowIndex + 1][colIndex + 1]; // h(0,0) f (X + 1, Y + 1) 
                    int kernel2 = hMatric[0][1] * convolutionImgArr[rowIndex + 1][colIndex]; // h(1,0) f (X, Y + 1) 
                    int kernel3 = hMatric[0][2] * originalImgArr[rowIndex + 1][colIndex - 1]; // h(2,0) f (X - 1, Y + 1) 

                    int kernel4 = hMatric[1][0] * originalImgArr[rowIndex][colIndex + 1]; // h(0,1) f(X+1,Y)
                    int kernel5 = hMatric[1][1] * convolutionImgArr[rowIndex][colIndex]; // h(1,1) f (X, Y)
                    int kernel6 = hMatric[1][2] * originalImgArr[rowIndex][colIndex - 1]; // h(2,1) f (X - 1, Y)

                    int kernel7 = hMatric[2][0] * originalImgArr[rowIndex - 1][colIndex + 1]; // h(0,2) f (X + 1, Y - 1) 
                    int kernel8 = hMatric[2][1] * convolutionImgArr[rowIndex - 1][colIndex]; // h(1,2) f (X, Y - 1) 
                    int kernel9 = hMatric[2][2] * originalImgArr[rowIndex - 1][colIndex - 1]; // h(2,2) f (X - 1, Y - 1)

//                    int sum = checkKernelValue(kernel1) + checkKernelValue(kernel3) + checkKernelValue(kernel4) + checkKernelValue(kernel6) + checkKernelValue(kernel7) + checkKernelValue(kernel9);
                    int sum = kernel1 + kernel3 + kernel4 + kernel6 + kernel7 + kernel9;

                    if (sum < 0) {
                        sum = 0;
                    } else if (sum > 255) {
                        sum = 255;
                    }

                    convolutionImgArr[rowIndex][colIndex] = sum;

                }
            }

            for (int[] imgSet : convolutionImgArr) {
                for (int i = 0; i < imgSet.length; i++) {
                    fout.write(imgSet[i]);
                }
            }

            fout.flush();
            fout.close(); // close file output stream
            fis.close(); // close file input stream

        } catch (IOException e) {
            System.out.println("Error: " + e.toString());
        }
    }

    private static int checkKernelValue(int value) {
        if (value < 0) {
            return 0;
        } else if (value > 255) {
            return 255;
        }

        return value;
    }
}
