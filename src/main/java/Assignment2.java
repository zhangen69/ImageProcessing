
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

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
//        String srcFileName = "yoda.raw";
        String srcFileName = "bedroom.raw";
        File file = new File(srcFileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            File outputFile = new File("assignment2_pattern.raw");
            FileOutputStream fout = new FileOutputStream(outputFile);
            String fileName = file.getName();
            int fileSize = (int) file.length();
            System.out.println("---------------- Source File Info ----------------");
            System.out.println("File Name: " + fileName);
            System.out.println("File Size: " + fileSize);
            System.out.println("---------------- Patterning Data ----------------");
            int index = 0;
            int orginalHeight = 600; // bedroom.raw
            int orginalWeight = 600; // bedroom.raw
//            int orginalHeight = 62; // yoda.raw
//            int orginalWeight = 123; // yoda.raw
            int totalPatternSeq = (orginalHeight * 3) * (orginalWeight * 3);
            int value;
            int[][] data = new int[orginalHeight * 3][orginalWeight * 3];
            int rowCount = 0;
            int colCount = 0;
            while ((value = fis.read()) != -1) {
                int[] patternDataInByte = getPattern(value);
                int patternWriteIndex = 0;
                
//                for (int i = 0; i < 3; i++) {
//                    for (int j = 0; j < 3; j++) {
//                        int patternVal = patternDataInByte[patternWriteIndex];
//                        data[rowCount * i][colCount * 3 + j] = patternVal;
////                        fout.write(patternVal);
////                        patternWriteIndex++;
//                    }
//                }

                int rowIndex1 = rowCount * 3;
                int rowIndex2 = rowIndex1 + 1;
                int rowIndex3 = rowIndex2 + 1;
                int colIndex1 = colCount * 3;
                int colIndex2 = colIndex1 + 1;
                int colIndex3 = colIndex2 + 1;

                writeData(data, rowIndex1, colIndex1, patternDataInByte[0]);
                writeData(data, rowIndex1, colIndex2, patternDataInByte[1]);
                writeData(data, rowIndex1, colIndex3, patternDataInByte[2]);
                writeData(data, rowIndex2, colIndex1, patternDataInByte[3]);
                writeData(data, rowIndex2, colIndex2, patternDataInByte[4]);
                writeData(data, rowIndex2, colIndex3, patternDataInByte[5]);
                writeData(data, rowIndex3, colIndex1, patternDataInByte[6]);
                writeData(data, rowIndex3, colIndex2, patternDataInByte[7]);
                writeData(data, rowIndex3, colIndex3, patternDataInByte[8]);
                
//                data[rowIndex1][colIndex1] = patternDataInByte[0];
//                data[rowIndex1][colIndex2] = patternDataInByte[1];
//                data[rowIndex1][colIndex3] = patternDataInByte[2];
//                data[rowIndex2][colIndex1] = patternDataInByte[3];
//                data[rowIndex2][colIndex2] = patternDataInByte[4];
//                data[rowIndex2][colIndex3] = patternDataInByte[5];
//                data[rowIndex3][colIndex1] = patternDataInByte[6];
//                data[rowIndex3][colIndex2] = patternDataInByte[7];
//                data[rowIndex3][colIndex3] = patternDataInByte[8];
                
                if (colCount == (orginalWeight - 1) && rowCount < orginalHeight) {
                    colCount = 0;
                    rowCount++;
                } else if (colCount < orginalWeight && rowCount < orginalHeight) {
                    colCount++;
                } else {
                    throw new Error("condition error");
                }

//                for (int i = 0; i < patternDataInByte.length; i++) {
//                    fout.write(patternDataInByte[i]);
//                }
                index++;
            }

            if (index != fileSize || (index != (totalPatternSeq / 9))) {
                System.out.println("last index " + index);
                System.out.println("totalPatternSeq " + totalPatternSeq);
                System.out.println("colCount " + colCount);
                System.out.println("orginalWeight " + orginalWeight);
                System.out.println("rowCount " + rowCount);
                System.out.println("orginalHeight " + orginalHeight);
                throw new Error("Something went wrong...");
            }
            
            int writeCount = 0;
            
            for (int[] data1 : data) {
                for (int j = 0; j < data1.length; j++) {
                    fout.write(data1[j]);
                    writeCount++;
                }
            }
            
            System.out.println("Count: " + writeCount);

//            fout.flush();
            fout.close();
            fis.close();
        } catch (IOException ex) {
            System.out.println("File is not exists");
        }
    }

    private static int[] getPattern(int colorDec) {
        int[] result = new int[9];
        int baseNum = 255 / 10;

        for (int i = 0; i < 10; i++) { // 10 type pattern
            int start = i * baseNum;
            int end = start + baseNum;
            if (isBetween(colorDec, start, end)) {
                result = getPatternArr(i);
                break;
            }
        }

        return result;
    }

    private static int[] getPatternArr(int pattern) {
        int[] arr = new int[]{};

        switch (pattern) {
            case 0:
                arr = new int[]{
                    0, 0, 0,
                    0, 0, 0,
                    0, 0, 0};
                break;
            case 1:
                arr = new int[]{
                    0, 0, 0,
                    0, 0, 0,
                    0, 0, 255};
                break;
            case 2:
                arr = new int[]{
                    255, 0, 0,
                    0, 0, 0,
                    0, 0, 255};
                break;
            case 3:
                arr = new int[]{
                    255, 0, 255,
                    0, 0, 0,
                    0, 0, 255};
                break;
            case 4:
                arr = new int[]{
                    255, 0, 255,
                    0, 0, 0,
                    255, 0, 255};
                break;
            case 5:
                arr = new int[]{
                    255, 0, 255,
                    0, 0, 0,
                    255, 255, 255};
                break;
            case 6:
                arr = new int[]{
                    255, 0, 255,
                    255, 0, 0,
                    255, 255, 255};
                break;
            case 7:
                arr = new int[]{
                    255, 255, 255,
                    255, 0, 0,
                    255, 255, 255};
                break;
            case 8:
                arr = new int[]{
                    255, 255, 255,
                    255, 0, 255,
                    255, 255, 255};
                break;
            case 9:
                arr = new int[]{
                    255, 255, 255,
                    255, 255, 255,
                    255, 255, 255};
                break;
        }

        return arr;
    }

    private static boolean isBetween(int x, int start, int end) {
        return start <= x && x <= end;
    }
    
    private static void writeData(int[][] data, int x, int y, int value) {
        if (data[x][y] == 0) {
            data[x][y] = value;
        } else {
            throw new Error("the array position has value");
        }
    }
}
