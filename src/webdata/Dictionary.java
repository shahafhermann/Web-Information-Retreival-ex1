package webdata;

import java.io.Serializable;
import java.util.Collection;
import java.util.TreeMap;

public class Dictionary implements Serializable {

    private static final int K = 100;

    private TreeMap<String, TreeMap<Integer, Integer>> termDict;
    boolean isProduct;

    private String concatStr = "";
    private int[] frequency;
    private long[] postingPtr;
    private byte[] length;
    private byte[] prefixSize;
    private long[] termPtr;
    private int numOfBlocks;
    private int numOfTerms;

    /**
     * Constructor.
     * @param termDict A mapping of tokens to their relevant frequencies.
     * @param isProduct Indicate whether the given map is of token or products.
     */
    Dictionary(TreeMap<String, TreeMap<Integer, Integer>> termDict, Boolean isProduct) {
        this.termDict = termDict;
        this.isProduct = isProduct;
        numOfTerms = termDict.size();
        numOfBlocks = (int)Math.ceil(numOfTerms / (double)K);
        termPtr = new long[numOfBlocks];
        frequency = new int[numOfTerms];
        postingPtr = new long[numOfTerms];
        length = new byte[numOfTerms];
        prefixSize = new byte[numOfTerms];
    }

    /**
     * Build the concatenated String with all known tokens.
     * Update all data structures with it's info.
     */
    public void buildString() {
        int i = 0;
        String prevTerm = "";
        for (String term: termDict.keySet()) {  // For each token
            if (i % K == 0) {
                termPtr[(i / 100)] = concatStr.length();
            }
            TreeMap<Integer, Integer> termData = termDict.get(term);
            Collection<Integer> allFrequencies = termData.values();
            frequency[i] = allFrequencies.stream().mapToInt(Integer::intValue).sum();  // Sum all values

            postingPtr[i] = encodePostingList(termData, term);

            length[i] = (byte) term.length();
            byte psize = findPrefix(prevTerm, term);
            prefixSize[i] = psize;
            concatStr = concatStr.concat(term.substring(psize));
            ++i;
        }
    }

    /**
     * Find the longest common prefix for two given Strings.
     * @param prev The first string to check
     * @param curr The second string to check
     * @return The length of the longest common prefix.
     */
    private byte findPrefix(String prev, String curr) {
        int minLength = Math.min(prev.length(), curr.length());
        for (int i = 0; i < minLength; i++) {
            if (prev.charAt(i) != curr.charAt(i)) {
                return (byte)i;
            }
        }
        return (byte)minLength;
    }

    /**
     *
     * @param termData
     * @param token
     * @return
     */
    private long encodePostingList(TreeMap<Integer, Integer> termData, String token) {
        return 1;
    }
}
