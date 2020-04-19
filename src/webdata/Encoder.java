package webdata;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public final class Encoder {

    private Encoder() {}

    public static ArrayList<Byte> encode(int[] values, boolean codeAsGap) {
        int size = values.length;
        ArrayList<Byte> encoded = new ArrayList<>(padByte(intToByte(size)));
        int counter = 1;
        ArrayList<Byte> tempGroup = new ArrayList<>();
        byte controlByte = 0;
        int prevVal = 0;
        for (int val: values) {
            ArrayList<Byte> valAsByte = intToByte(val - prevVal);
            int valSize = valAsByte.size();
            controlByte = (byte)((controlByte << 2) + (valSize - 1));
            tempGroup.addAll(valAsByte);
            if (counter % 4 == 0) {
                encoded.add(controlByte);
                encoded.addAll(tempGroup);
                controlByte = 0;
                tempGroup.clear();
            }
            prevVal = (codeAsGap) ? val : 0;
            ++counter;
        }
        encoded.add(controlByte);
        encoded.addAll(tempGroup);
        return encoded;




//        try {
//            long pos = raf.getFilePointer();  // TODO: make sure it's EOF

//            int prevReview = termData.firstKey();
//            byte[] numOfReviews = intToByte(termData.size());
//            byte[] reviewAsBytes = intToByte(prevReview);
//            byte[] freqAsBytes = intToByte(termData.get(prevReview));
//            // First we encode the review number and than the frequency in the review
//            byte controlByte = (byte)(((numOfReviews.length - 1) << 4) + ((reviewAsBytes.length - 1) << 2) +
//                                freqAsBytes.length - 1);
//
//            raf.writeByte(controlByte);
//            raf.write(numOfReviews);
//            raf.write(reviewAsBytes);
//            raf.write(freqAsBytes);
//
//            termData.remove(prevReview);
//            for (Integer review: termData.keySet()) {
//                reviewAsBytes = intToByte(review - prevReview);
//                freqAsBytes = intToByte(termData.get(review));
//                // First we encode the review number and than the frequency in the review
//                controlByte = (byte)(((reviewAsBytes.length - 1) << 2) + freqAsBytes.length - 1);
//
//                raf.writeByte(controlByte);
//                raf.write(reviewAsBytes);
//                raf.write(freqAsBytes);
//
//                prevReview = review;
//            }
//            return pos;
//
//        } catch (IOException e) {
//            System.err.println(e.getMessage());
//            System.exit(1);
//        }
//        return -1;
    }

    public static int[] decode(byte[] values, boolean codeAsGap) {
        int size = byteArrayToInt(Arrays.copyOfRange(values, 0, 4));
        int[] decoded = new int[size];
        int groupCounter = 0;
        int[] groupSizes = new int[4];
        int groupIndex = 4; // First index of data
        int prevVal = 0;

        while (groupCounter < size) {
            int curGroupPerControl = groupCounter % 4;
            if (curGroupPerControl == 0) {  // This is a control byte
                groupSizes = decodeControlByte(values[groupIndex]);
                ++groupIndex;
            }
            int groupSize = groupSizes[curGroupPerControl];
            byte[] sliced = padByte(Arrays.copyOfRange(values, groupIndex, groupIndex + groupSize));
            decoded[groupCounter] = byteArrayToInt(sliced) + prevVal;
            prevVal = (codeAsGap) ? decoded[groupCounter] : 0;
            groupIndex += groupSize;
            ++groupCounter;
        }
        return decoded;
    }

    /**
     * Decode the control byte of the format (--|--|--|--)
     * @param b The control byte
     * @return An array of integers corresponding the decode
     */
    private static int[] decodeControlByte(byte b) {
        int[] res = new int[4];
        byte slicer = 3;
        for (int i = 3; i >= 0; --i) {
            res[i] = (b & slicer) + 1;  // TODO: remember that this doesnt include the remain (?)
            b >>= 2;
        }
        return res;
    }

    /**
     * Returns a byte array of exactly 4 bytes.
     * @param arr Byte array to pad
     * @return Byte array of size 4
     */
    private static ArrayList<Byte> padByte(ArrayList<Byte> arr) {
        int pad = 4 - arr.size();
        int i = 0;
        Byte zero = 0;
        while (i < pad) {
            arr.add(0, zero);
            ++i;
        }
        return arr;
    }

    private static byte[] padByte(byte[] arr) {
        byte[] newArr = new byte[4];
        int pad = 4 - arr.length;
        for (int i = 0; i < arr.length; ++i) {
            newArr[i + pad] = arr[i];
            ++i;
        }
        return newArr;
    }

    private static ArrayList<Byte> intToByte(final Integer i) {
        BigInteger bi = BigInteger.valueOf(i);
        ArrayList<Byte> bigByte = new ArrayList<>();
        for (byte b: bi.toByteArray()) {
            bigByte.add(b);
        }
        return bigByte;
    }

    private static int byteArrayToInt(byte[] intBytes){
        ByteBuffer byteBuffer = ByteBuffer.wrap(intBytes);
        return byteBuffer.getInt();
    }
}