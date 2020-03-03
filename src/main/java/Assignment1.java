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
            String fileName = "yoda.tif";
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
            // get file name
            System.out.println("File Name: " + file.getName());
            int value;
            int index = 0;
            int offsetNextIFD = -1;
            int stripOffsets = -1;
//            String byteOrder = null;
            int dataColumnCount = 0;
            String[][] dataEntryArr = null;
            int dataEntryIndex1 = 0;
            int totalDE = -1;
            while ((value = fis.read()) != -1) {
                // get byte order
                // check is LSB or MSB
                // 8 bytes - file header strucutre
                // 2 bytes - byte order 
                // 2 bytes - version
                // 4 bytes - offset of the first IFD
                if (index == 0) {
                    int value2 = fis.read();
                    int[] nums = {value, value2};
                    String hex = sumDecsToHexString(nums);
                    if (hex.equals("4949")) {
                        byteOrder = "LSB";
                    } else if (hex.equals("4d4d")) {
                        byteOrder = "MSB";
                    }
                    System.out.println("--------------------- Header File Data -------------------------");
                    System.out.println("Byte Order: " + byteOrder);
                    index++;
                }

                if (byteOrder == "LSB") {
                    // get version
                    if (index == 2) {
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

                        int[] tagNums = {tag1, tag2};
                        int[] tagTypeNums = {type1, type2};
                        int[] tagLengthNums = {length1, length2, length3, length4};
                        int[] valueNums = {value1, value2, value3, value4};

                        String tagHex = sumDecsToHexString(tagNums);
                        int tagDec = Integer.parseInt(tagHex, 16);

                        String tagTypeHex = sumDecsToHexString(tagTypeNums);
                        int tagTypeDec = Integer.parseInt(tagTypeHex, 16);

                        String tagName = getTagName(tagDec);
                        String tagTypeName = getTagTypeName(tagTypeDec);

                        if (tagName != null && tagTypeName != null) {
                            String tagValue = sumDecsToHexString(valueNums);
                            String tagLength = sumDecsToHexString(tagLengthNums);

                            int tagValueDec = Integer.parseInt(tagValue, 16);
                            int tagLengthDec = Integer.parseInt(tagLength, 16);
                            int wordLength1 = 35 - (Integer.toString(tagDec).length() + 3 + tagName.length());
                            int wordLength2 = 15 - (Integer.toString(tagTypeDec).length() + 3 + tagTypeName.length());

                            String wordSpaces1 = "";
                            String wordSpaces2 = "";

                            for (int i = 0; i < wordLength1; i++) {
                                wordSpaces1 += " ";
                            }

                            for (int i = 0; i < wordLength2; i++) {
                                wordSpaces2 += " ";
                            }

                            if (tagDec == 273) {
                                stripOffsets = tagValueDec;
                                System.out.println("Size of IFD: " + stripOffsets);
                            }

                            dataEntryArr[dataEntryIndex1][0] = Integer.toString(tagDec);
                            dataEntryArr[dataEntryIndex1][1] = tagName;
                            dataEntryArr[dataEntryIndex1][2] = wordSpaces1;
                            dataEntryArr[dataEntryIndex1][3] = Integer.toString(tagTypeDec);
                            dataEntryArr[dataEntryIndex1][4] = tagTypeName;
                            dataEntryArr[dataEntryIndex1][5] = wordSpaces2;
                            dataEntryArr[dataEntryIndex1][6] = Integer.toString(tagLengthDec);
                            dataEntryArr[dataEntryIndex1][7] = Integer.toString(tagValueDec);
                            dataEntryIndex1++;
                        }

                        index += 11;
                    } else if (index == (10 + 12 * totalDE)) {
                        int value2 = fis.read();
                        int[] nums = { value, value2 };
                        String offsetNextIFDHex = sumDecsToHexString(nums);
                        offsetNextIFD = Integer.parseInt(offsetNextIFDHex, 16);
                        System.out.println("Consecutive Offset IFD (Offset of Next IFD): " + offsetNextIFD);
                    } else if (index >= 10 && stripOffsets > 0 && index >= stripOffsets) {
                        if (index == (stripOffsets)) {
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

                        if (dataColumnCount == 15) {
                            System.out.println("");
                            dataColumnCount = 0;
                        } else {
                            System.out.print(" ");
                            dataColumnCount++;
                        }
                    }
                } else if (byteOrder == "MSB") {
                    // get version
                    if (index == 2) {
                        int value2 = fis.read();
                        int[] nums = {value, value2};

                        String hexString = sumDecsToHexString(nums);
                        System.out.println("Version: " + hexString);

                        index++;
                    }
                }

                index++;
            }
            // version
            // first offset IFD
            // DE total
            // size of IFD
            // Consecutive offset IFD
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
}
