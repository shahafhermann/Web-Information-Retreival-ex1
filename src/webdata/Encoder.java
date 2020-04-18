package webdata;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.TreeMap;

public final class Encoder {

    private Encoder() {}

    public static long encode(TreeMap<Integer, Integer> termData, String token, boolean isProduct,
                              RandomAccessFile raf) {
        try {
            long pos = raf.getFilePointer();
            for (Integer review: termData.keySet()) {
                byte[] reviewAsBytes = intToByte(review);
                byte[] freqAsBytes = intToByte(termData.get(review));
                // First we encode the review number and than the frequency in the review
                byte controlByte = (byte)((reviewAsBytes.length << 4) + freqAsBytes.length);

                raf.writeByte(controlByte);
                raf.write(reviewAsBytes);
                raf.write(freqAsBytes);
            }
            return pos;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return -1;
    }

    public static long decode(boolean isProduct, RandomAccessFile raf) { }

    static byte[] intToByte(final Integer i) {
        BigInteger bi = BigInteger.valueOf(i);
        return bi.toByteArray();
    }

    private static int byteArrayToInt(byte[] intBytes){
        ByteBuffer byteBuffer = ByteBuffer.wrap(intBytes);
        return byteBuffer.getInt();
    }
}