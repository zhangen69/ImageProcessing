
import java.io.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author User
 */
public class Assignment2 {

    public static void main(String[] args) {
        String fileName = "yoda.raw";
        File file = new File(fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            File outputFile = new File("assignment2_pattern.raw");
            FileOutputStream fout = new FileOutputStream(outputFile);
            System.out.println("File Name: " + file.getName());
            int value;
            int colCount = 0;
            int rowCount = 0;
            int[][] patterningData = new int[369][186];
            int index = 0;
            System.out.println("---------------- Patterning Data ----------------");
            while ((value = fis.read()) != -1) {
                int[][] patternDataInByte = getPattern(value);
//
//                int rowIndex1 = rowCount * 3;
//                int rowIndex2 = rowCount * 3 + 1;
//                int rowIndex3 = rowCount * 3 + 2;
//                int colIndex1 = colCount * 3;
//                int colIndex2 = colCount * 3 + 1;
//                int colIndex3 = colCount * 3 + 2;
                
                for (int i = 0; i < 3; i++) {
                    patterningData[rowCount * 3 + i][colCount * 3] = patternDataInByte[i][0];
                    patterningData[rowCount * 3 + i][colCount * 3 + 1] = patternDataInByte[i][1];
                    patterningData[rowCount * 3 + i][colCount * 3 + 2] = patternDataInByte[i][2];
                }

//                System.out.println("( " + rowIndex1 + ", " + colIndex1 + " ) = " + patternDataInByte[0][0]);
//                System.out.println("( " + rowIndex1 + ", " + colIndex2 + " ) = " + patternDataInByte[0][1]);
//                System.out.println("( " + rowIndex1 + ", " + colIndex3 + " ) = " + patternDataInByte[0][2]);
//                System.out.println("( " + rowIndex2 + ", " + colIndex1 + " ) = " + patternDataInByte[1][0]);
//                System.out.println("( " + rowIndex2 + ", " + colIndex2 + " ) = " + patternDataInByte[1][1]);
//                System.out.println("( " + rowIndex2 + ", " + colIndex3 + " ) = " + patternDataInByte[1][2]);
//                System.out.println("( " + rowIndex3 + ", " + colIndex1 + " ) = " + patternDataInByte[2][0]);
//                System.out.println("( " + rowIndex3 + ", " + colIndex2 + " ) = " + patternDataInByte[2][1]);
//                System.out.println("( " + rowIndex3 + ", " + colIndex3 + " ) = " + patternDataInByte[2][2]);
//                patterningData[rowIndex1][colIndex1] = patternDataInByte[0][0];
//                patterningData[rowIndex1][colIndex2] = patternDataInByte[0][1];
//                patterningData[rowIndex1][colIndex3] = patternDataInByte[0][2];
//                patterningData[rowIndex2][colIndex1] = patternDataInByte[1][0];
//                patterningData[rowIndex2][colIndex2] = patternDataInByte[1][1];
//                patterningData[rowIndex2][colIndex3] = patternDataInByte[1][2];
//                patterningData[rowIndex3][colIndex1] = patternDataInByte[2][0];
//                patterningData[rowIndex3][colIndex2] = patternDataInByte[2][1];
//                patterningData[rowIndex3][colIndex3] = patternDataInByte[2][2];

                if (colCount == 61) {
                    colCount = 0;
                    rowCount++;
                } else {
                    colCount++;
                }

                index++;
            }

            for (int i = 0; i < patterningData.length; i++) {
                for (int j = 0; j < patterningData[i].length; j++) {
                    int streamVal = patterningData[i][j];
                    fout.write(streamVal);
                }
            }
            fout.close();
            fis.close();
        } catch (IOException ex) {
            System.out.println("File is not exists");
        }
    }

    private static int[][] getPattern(int colorDec) {
        int[][] result = new int[3][3];

        for (int i = 0; i < 10; i++) {
            int start = i * 25;
            int end = start + 25;
            if (isBetween(colorDec, start, end)) {
                int[] patternArr = getPatternArr(start / 25);
                int count = 0;
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        result[j][k] = patternArr[count];
                        count++;
                    }
                }
                break;
            }
        }

        return result;
    }

    private static int[] getPatternArr(int pattern) {
        int[] arr = new int[] {};

        switch (pattern) {
            case 1:
                arr = new int[] { 8 };
                break;
            case 2:
                arr = new int[] { 0, 8 };
                break;
            case 3:
                arr = new int[] { 0, 2, 8 };
                break;
            case 4:
                arr = new int[] { 0, 2, 6, 8 };
                break;
            case 5:
                arr = new int[] { 0, 2, 6, 7, 8 };
                break;
            case 6:
                arr = new int[] { 0, 2, 3, 6, 7, 8 };
                break;
            case 7:
                arr = new int[] { 0, 1, 2, 3, 6, 7, 8 };
                break;
            case 8:
                arr = new int[] { 0, 1, 2, 3, 5, 6, 7, 8 };
                break;
            case 9:
                arr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
                break;
        }

        return getColorArr(arr);
    }

    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    private static int[] getColorArr(int[] whiteIndexes) {
        int[] arrResult = new int[10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < whiteIndexes.length; j++) {
                if (i == whiteIndexes[j]) {
                    arrResult[i] = 255;
                    break;
                } 
            }
        }
        return arrResult;
    }
}
