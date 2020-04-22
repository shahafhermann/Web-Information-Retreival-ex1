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

    /**
     * Empty and private constructor to make this class static.
     */
    private Encoder() {}

    /**
     *
     * @param values
     * @param codeAsGap
     * @return
     */
    public static ArrayList<Byte> encode(ArrayList<Integer> values, boolean codeAsGap) {
        int size = values.size();
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
        if (!tempGroup.isEmpty()) {
            encoded.add(controlByte);
            encoded.addAll(tempGroup);
        }
        return encoded;
    }

    /**
     * Decode a byte array to an int array using varint group decoding. If codeAsGap is true than the bytes should
     * represent a gap difference.
     * @param values The byte array
     * @param codeAsGap Indicate whether there's a gap difference
     * @return The corresponding int array.
     */
    public static int[] decode(byte[] values, boolean codeAsGap) {
        int size = byteArrayToInt(Arrays.copyOfRange(values, 0, 4));
        int numOfGroupsOfLastControlByte = size % 4;
        int[] decoded = new int[size];
        int groupCounter = 0;
        int[] groupSizes = new int[4];
        int groupIndex = 4; // First index of data
        int prevVal = 0, curGroupPerControl;
        boolean lastControl = false;

        while (groupCounter < size) {
            curGroupPerControl = groupCounter % 4;
            curGroupPerControl += (lastControl) ? (4 - numOfGroupsOfLastControlByte) : 0;
            if (curGroupPerControl == 0) {  // This is a control byte
                if (groupCounter == size - numOfGroupsOfLastControlByte) {  // This is the LAST control byte
                    lastControl = true;
                    curGroupPerControl += (4 - numOfGroupsOfLastControlByte);
                }
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