package webdata;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class Dictionary implements Serializable {

    private final int K = 100;

    private TreeMap<String, TreeMap<Integer, Integer>> tokenDict;

    private String concatStr = "";
    private int[] frequency;
    private long[] postingPtr;
    private byte[] length;
    private byte[] prefixSize;
    private long[] termPtr;
    private int numOfBlocks;
    private int numOfTerms;

    /**
     * Constructor
     */
    Dictionary(TreeMap<String, TreeMap<Integer, Integer>> tokenDict) {
        this.tokenDict = tokenDict;
        numOfTerms = tokenDict.size();
        numOfBlocks = (int)Math.ceil(numOfTerms / (double)K);
        termPtr = new long[numOfBlocks];
        frequency = new int[numOfTerms];
        postingPtr = new long[numOfTerms];
        length = new byte[numOfTerms];
        prefixSize = new byte[numOfTerms];
    }

    public void buildString() {
        int i = 0;
        String prevTerm = "";
        for (String key: tokenDict.keySet()) {
            if (i % K == 0) {
                termPtr[(i / 100)] = concatStr.length();
            }
            TreeMap<Integer, Integer> termData = tokenDict.get(key);
            frequency[i] = termData.get(0);

            postingPtr

            length[i] = (byte) key.length();
            byte psize = findPrefix(prevTerm, key);
            prefixSize[i] = psize;
            concatStr = concatStr.concat(key.substring(psize));
            ++i;
        }
    }

    private byte findPrefix(String prev, String curr) {
        int minLength = Math.min(prev.length(), curr.length());
        for (int i = 0; i < minLength; i++) {
            if (prev.charAt(i) != curr.charAt(i)) {
                return (byte)i;
            }
        }
        return (byte)minLength;
    }



}
