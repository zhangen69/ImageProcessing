
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
 * @author Jacob
 */
public class Assignment5 {

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

            int[][] hMatric_lowpass = {
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1},};

            int[][] hMatric_highpass = {
                {-1, -1, -1},
                {-1, 8, -1},
                {-1, -1, -1},};

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

            File outputFile_lowpass = new File("assignment5_lowpass.raw");
            File outputFile_highpass = new File("assignment5_highpass.raw");
            FileOutputStream fout_lowpass = new FileOutputStream(outputFile_lowpass);
            FileOutputStream fout_highpass = new FileOutputStream(outputFile_highpass);

            for (int rowIndex = 1; rowIndex < originalImgArr.length - 2; rowIndex++) {
                for (int colIndex = 1; colIndex < originalImgArr[rowIndex].length - 2; colIndex++) {
                    int kernel1 = calulateKernelNum(hMatric_lowpass[0][0], originalImgArr[rowIndex + 1][colIndex + 1], true);
                    int kernel2 = calulateKernelNum(hMatric_lowpass[0][1], originalImgArr[rowIndex + 1][colIndex], true);
                    int kernel3 = calulateKernelNum(hMatric_lowpass[0][2], originalImgArr[rowIndex + 1][colIndex - 1], true);
                    int kernel4 = calulateKernelNum(hMatric_lowpass[1][0], originalImgArr[rowIndex][colIndex + 1], true);
                    int kernel5 = calulateKernelNum(hMatric_lowpass[1][1], originalImgArr[rowIndex][colIndex], true);
                    int kernel6 = calulateKernelNum(hMatric_lowpass[1][2], originalImgArr[rowIndex][colIndex - 1], true);
                    int kernel7 = calulateKernelNum(hMatric_lowpass[2][0], originalImgArr[rowIndex - 1][colIndex + 1], true);
                    int kernel8 = calulateKernelNum(hMatric_lowpass[2][1], originalImgArr[rowIndex - 1][colIndex], true);
                    int kernel9 = calulateKernelNum(hMatric_lowpass[2][2], originalImgArr[rowIndex - 1][colIndex - 1], true);

                    int sum = kernel1 + kernel2 + kernel3 + kernel4 + kernel5 + kernel6 + kernel7 + kernel8 + kernel9;

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
                    fout_lowpass.write(imgSet[i]);
                }
            }

            for (int rowIndex = 1; rowIndex < originalImgArr.length - 2; rowIndex++) {
                for (int colIndex = 1; colIndex < originalImgArr[rowIndex].length - 2; colIndex++) {
                    int kernel1 = calulateKernelNum(hMatric_highpass[0][0], originalImgArr[rowIndex + 1][colIndex + 1], false);
                    int kernel2 = calulateKernelNum(hMatric_highpass[0][1], originalImgArr[rowIndex + 1][colIndex], false);
                    int kernel3 = calulateKernelNum(hMatric_highpass[0][2], originalImgArr[rowIndex + 1][colIndex - 1], false);
                    int kernel4 = calulateKernelNum(hMatric_highpass[1][0], originalImgArr[rowIndex][colIndex + 1], false);
                    int kernel5 = calulateKernelNum(hMatric_highpass[1][1], originalImgArr[rowIndex][colIndex], false);
                    int kernel6 = calulateKernelNum(hMatric_highpass[1][2], originalImgArr[rowIndex][colIndex - 1], false);
                    int kernel7 = calulateKernelNum(hMatric_highpass[2][0], originalImgArr[rowIndex - 1][colIndex + 1], false);
                    int kernel8 = calulateKernelNum(hMatric_highpass[2][1], originalImgArr[rowIndex - 1][colIndex], false);
                    int kernel9 = calulateKernelNum(hMatric_highpass[2][2], originalImgArr[rowIndex - 1][colIndex - 1], false);

                    int sum = kernel1 + kernel2 + kernel3 + kernel4 + kernel5 + kernel6 + kernel7 + kernel8 + kernel9;

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
                    fout_highpass.write(imgSet[i]);
                }
            }

            fout_lowpass.flush();
            fout_lowpass.close(); // close file output stream
            fout_highpass.flush();
            fout_highpass.close(); // close file output stream
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

    private static int calulateKernelNum(int hVal, int fValue, boolean dividable) {
        if (dividable) {
            return Math.round((hVal * fValue) / 9);
        } else {
            return hVal * fValue;
        }
    }
}
