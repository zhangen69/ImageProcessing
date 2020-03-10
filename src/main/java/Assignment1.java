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

public class Assignment1 {

    public static String byteOrder = null;

    public static void main(String[] args) {
        try {
//            String fileName = "yoda.tif";
            String fileName = "Imgpro.tif";
//            String fileName = "lena.tif";
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
            System.out.println("File Name: " + file.getName());
            int value;
            int index = 0;
            int offsetNextIFD = -1;
            int stripOffsets = -1;
            int dataColumnCount = 0;
            String[][] dataEntryArr = null;
            int dataEntryIndex1 = 0;
            int totalDE = -1;
            byte[] rawBytes = null;
            int rawBytesCount = 0;
            File outputFile = new File("assignment1.raw");
            FileOutputStream fop = new FileOutputStream(outputFile);
            while ((value = fis.read()) != -1) {
                if (index == 0) {
                    int value2 = fis.read();
                    String hex = getHexString(value) + getHexString(value2);
                    if (hex.equals("4949")) {
                        byteOrder = "LSB";
                    } else if (hex.equals("4D4D")) {
                        byteOrder = "MSB";
                    }
                    System.out.println("--------------------- Header File Data -------------------------");
                    System.out.println("Byte Order: " + byteOrder);
                    index++;
                } else if (index == 2) {
                    int value2 = fis.read();
                    int[] nums = {value, value2};

                    String hexString = sumDecsToHexString(nums);
                    System.out.println("Version: " + hexString);

                    index++;
                } else if (index == 4) {
                    int value2 = fis.read();
                    int value3 = fis.read();
                    int value4 = fis.read();
                    int[] nums = {value, value2, value3, value4};

                    String hexString = sumDecsToHexString(nums);
                    System.out.println("First Offset IFD: " + Integer.parseInt(hexString, 16));

                    index += 3;
                } else if (index == 8) {
                    int value2 = fis.read();
                    int[] nums = {value, value2};

                    String hexString = sumDecsToHexString(nums);
                    totalDE = Integer.parseInt(hexString, 16);

                    System.out.println("--------------------- IFD Data -------------------------");
                    System.out.println("Total DE: " + totalDE);
                    dataEntryArr = new String[totalDE][8];
                    index++;
                } else if ((index >= 10 && index < (10 + 12 * totalDE)) && ((stripOffsets > 0 && index <= stripOffsets && ((index + 12) <= stripOffsets)) || (stripOffsets == -1))) {
                    int tag1 = value;
                    int tag2 = fis.read();

                    int type1 = fis.read();
                    int type2 = fis.read();

                    int length1 = fis.read();
                    int length2 = fis.read();
                    int length3 = fis.read();
                    int length4 = fis.read();

                    int value1 = fis.read();
                    int value2 = fis.read();
                    int value3 = fis.read();
                    int value4 = fis.read();

                    int[] tagNameNums = { tag1, tag2 };
                    int[] tagTypeNums = { type1, type2 };
                    
                    String tagName = getTagName(tagNameNums);
                    String tagTypeName = getTagTypeName(tagTypeNums);

                    if (tagName != null && tagTypeName != null) {
                        String tagValue = sumDecsToHexString(new int[] {value1, value2, value3, value4});
                        String tagLength = sumDecsToHexString(new int[] {length1, length2, length3, length4});

                        int tagValueDec = Integer.parseInt(tagValue, 16);
                        int tagLengthDec = Integer.parseInt(tagLength, 16);
                        int wordLength1 = 29 - tagName.length();
                        int wordLength2 = 9 - tagTypeName.length();

                        String wordSpaces1 = "";
                        String wordSpaces2 = "";

                        for (int i = 0; i < wordLength1; i++) {
                            wordSpaces1 += " ";
                        }

                        for (int i = 0; i < wordLength2; i++) {
                            wordSpaces2 += " ";
                        }
                        
                        if (tagName == "StripOffsets") {
                            stripOffsets = tagValueDec;
                            System.out.println("Size of IFD: " + stripOffsets);
                        }

                        dataEntryArr[dataEntryIndex1][0] = Integer.toString(Integer.parseInt(sumDecsToHexString(tagNameNums), 16));
                        dataEntryArr[dataEntryIndex1][1] = tagName;
                        dataEntryArr[dataEntryIndex1][2] = wordSpaces1;
                        dataEntryArr[dataEntryIndex1][3] = Integer.toString(Integer.parseInt(sumDecsToHexString(tagTypeNums), 16));
                        dataEntryArr[dataEntryIndex1][4] = tagTypeName;
                        dataEntryArr[dataEntryIndex1][5] = wordSpaces2;
                        dataEntryArr[dataEntryIndex1][6] = Integer.toString(tagLengthDec);
                        dataEntryArr[dataEntryIndex1][7] = Integer.toString(tagValueDec);
                        dataEntryIndex1++;
                    }

                    index += 11;
                } else if (index == (10 + 12 * totalDE)) {
                    int value2 = fis.read();
                    int[] nums = {value, value2};
                    String offsetNextIFDHex = sumDecsToHexString(nums);
                    offsetNextIFD = Integer.parseInt(offsetNextIFDHex, 16);
                    System.out.println("Consecutive Offset IFD (Offset of Next IFD): " + offsetNextIFD);
                    index++;
                } else if (index >= 10 && stripOffsets > 0 && index >= stripOffsets) {
                    if (index == (stripOffsets)) {

                        int totalRawBytesLength = (int) (file.length() - stripOffsets);
                        rawBytes = new byte[totalRawBytesLength];

                        System.out.println("--------------------- DE Data -------------------------");
                        System.out.println("Tag                                Type          Length     Value");
                        System.out.println("----------------------------------------------------------");

                        for (int i = 0; i < (dataEntryArr.length - 1); i++) {
                            if (dataEntryArr[i][0] != null) {
                                int tagDec = Integer.parseInt(dataEntryArr[i][0]);
                                String tagName = dataEntryArr[i][1];
                                String wordSpaces1 = dataEntryArr[i][2];
                                int tagTypeDec = Integer.parseInt(dataEntryArr[i][3]);
                                String tagTypeName = dataEntryArr[i][4];
                                String wordSpaces2 = dataEntryArr[i][5];
                                int tagLengthDec = Integer.parseInt(dataEntryArr[i][6]);
                                int tagValueDec = Integer.parseInt(dataEntryArr[i][7]);
                                System.out.println(tagDec + " (" + tagName + ")" + wordSpaces1 + tagTypeDec + " (" + tagTypeName + ")" + wordSpaces2 + tagLengthDec + "         " + tagValueDec);
                            }
                        }

                        System.out.println("--------------------- Data Table -------------------------");
                    }

                    String valueHex = String.format("%02X", value);
                    System.out.print(valueHex);
                    fop.write(value);

                    if (dataColumnCount == 15) {
                        System.out.println("");
                        dataColumnCount = 0;
                    } else {
                        if (dataColumnCount % 2 != 0) {
                            System.out.print(" ");
                        }
                        dataColumnCount++;
                    }
                }
                index++;
            }
            fop.close();
            fis.close();
        } catch (IOException e) {
            System.out.println("File is not exists");
        }
    }

    private static String sumDecsToHexString(int[] decNums) {
        String hexString = "";

        for (int i = 0; i < decNums.length; i++) {
            if (byteOrder == "LSB") {
                hexString += getHexString(decNums[decNums.length - (i + 1)]);
            } else if (byteOrder == "MSB") {
                hexString += getHexString(decNums[i]);
            }
        }

        return hexString;
    }

    private static String getHexString(int decNum) {
        if (decNum < 10) {
            return "0" + decNum;
        } else {
            return String.format("%02X", decNum);
        }
    }

    private static String getTagName(int tagType) {
        String tagName = null;

        switch (tagType) {
            case 254:
                tagName = "NewSubfileType";
                break;
            case 256:
                tagName = "ImageWidth";
                break;
            case 257:
                tagName = "ImageLength";
                break;
            case 258:
                tagName = "BitsPerSample";
                break;
            case 259:
                tagName = "Compression";
                break;
            case 262:
                tagName = "PhotometricInterpretation";
                break;
            case 273:
                tagName = "StripOffsets";
                break;
            case 277:
                tagName = "SamplesPerPixel";
                break;
            case 278:
                tagName = "RowsPerStrip";
                break;
            case 279:
                tagName = "StripByteCounts";
                break;
            case 282:
                tagName = "XResolution";
                break;
            case 283:
                tagName = "YResolution";
                break;
            case 296:
                tagName = "ResolutionUnit";
                break;
        }

        return tagName;
    }

    private static String getTagTypeName(int tagTypeDec) {
        String tagTypeName = null;
        
        switch (tagTypeDec) {
            case 1:
                tagTypeName = "byte";
                break;
            case 2:
                tagTypeName = "ASCII";
                break;
            case 3:
                tagTypeName = "short";
                break;
            case 4:
                tagTypeName = "long";
                break;
            case 5:
                tagTypeName = "rational";
                break;
        }

        return tagTypeName;
    }

    private static String getTagName(String tagHex) {
        int tagType = Integer.parseInt(tagHex, 16);
        return getTagName(tagType);
    }

    private static String getTagName(int[] tagNums) {
        String tagHex = sumDecsToHexString(tagNums);
        return getTagName(tagHex);
    }

    private static String getTagTypeName(String tagTypeHex) {
        int tagType = Integer.parseInt(tagTypeHex, 16);
        return getTagTypeName(tagType);
    }
    
    private static String getTagTypeName(int[] tagTypeNums) {
        String tagTypeHex = sumDecsToHexString(tagTypeNums);
        return getTagTypeName(tagTypeHex);
    }
}
