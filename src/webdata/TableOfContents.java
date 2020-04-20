package webdata;

public class TableOfContents {
    private int[] frequency;
    private long[] postingPtr;
    private byte[] length;
    private byte[] prefixSize;

    TableOfContents (int numOfTerms) {
        frequency = new int[numOfTerms];
        postingPtr = new long[numOfTerms];
        length = new byte[numOfTerms];
        prefixSize = new byte[numOfTerms];
    }

    public int getFrequency(int i) {
        return frequency[i];
    }

    public long getPostingPtr(int i) {
        return postingPtr[i];
    }

    public byte getLength(int i) {
        return length[i];
    }

    public byte getPrefixSize(int i) {
        return prefixSize[i];
    }

    public void setFrequency(int i, int value) {
        frequency[i] = value;
    }

    public void setPostingPtr(int i, long value) {
        postingPtr[i] = value;
    }

    public void setLength(int i, byte value) {
        length[i] = value;
    }

    public void setPrefixSize(int i, byte value) {
        prefixSize[i] = value;
    }
}
