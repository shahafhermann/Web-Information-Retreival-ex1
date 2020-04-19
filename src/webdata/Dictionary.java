package webdata;

import java.io.*;
import java.util.Collection;
import java.util.TreeMap;

public class Dictionary implements Serializable {

    private static final int K = 100;
    boolean isProduct;

    String concatStr = "";
    private int[] frequency;
    private long[] postingPtr;
    private byte[] length;
    private byte[] prefixSize;
    private int[] termPtr;
    private int numOfBlocks;
    private int numOfTerms;
    private String outputPath;

    /**
     * Constructor.
     * @param termDict A mapping of tokens to their relevant frequencies.
     * @param isProduct Indicate whether the given map is of token or products.
     */
    Dictionary(TreeMap<String, TreeMap<Integer, Integer>> termDict, Boolean isProduct, String path) {
        this.isProduct = isProduct;
        numOfTerms = termDict.size();
        numOfBlocks = (int)Math.ceil(numOfTerms / (double)K);
        termPtr = new int[numOfBlocks];
        frequency = new int[numOfTerms];
        postingPtr = new long[numOfTerms];
        length = new byte[numOfTerms];
        prefixSize = new byte[numOfTerms];
        this.outputPath = path;
        buildString(termDict);
    }

    /**
     * Build the concatenated String with all known tokens.
     * Update all data structures with it's info.
     */
    private void buildString(TreeMap<String, TreeMap<Integer, Integer>> termDict) {
        int i = 0;
        String prevTerm = "";

        RandomAccessFile raf = null;
        try{
            raf = new RandomAccessFile(outputPath, "rw");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        for (String term: termDict.keySet()) {  // For each token
            if (i % K == 0) {
                termPtr[(i / 100)] = concatStr.length();
                prefixSize[i] = 0;
                concatStr = concatStr.concat(term);
            }
            else {
                byte psize = findPrefix(prevTerm, term);
                prefixSize[i] = psize;
                concatStr = concatStr.concat(term.substring(psize));
            }

            TreeMap<Integer, Integer> termData = termDict.get(term);
            Collection<Integer> allFrequencies = termData.values();
            frequency[i] = allFrequencies.stream().mapToInt(Integer::intValue).sum();  // Sum all values

//            postingPtr[i] = Encoder.encode(termData, isProduct, raf);

            length[i] = (byte) term.length();
            prevTerm = term;
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

    public int searchTerm(String term) {
        return binarySearch(0, termPtr.length - 1, term);
    }


    private int binarySearch(int left, int right, String term) {
        if (right == left) {
            if (term.equals(concatStr.substring(termPtr[left], termPtr[left] + length[left * 100])))
                return left;  // TODO: What do we return?
            return rangeSearch(left, term);
        }
        if (right > left) {
            int mid = left + (right - left) / 2;


            // If the element is present at the
            // middle itself
            if (term.equals(concatStr.substring(termPtr[mid], termPtr[mid] + length[mid * 100])))
                return mid;  // TODO: What do we return?

            // If element is smaller than mid, then
            // it can only be present in left subarray
            if (term.compareTo(concatStr.substring(termPtr[mid], termPtr[mid] + length[mid * 100])) < 0)
                return binarySearch(left, mid - 1, term);

            // Else the element can only be present
            // in right subarray
            if (term.compareTo(concatStr.substring(termPtr[mid + 1], termPtr[mid + 1] + length[(mid + 1) * 100])) < 0) {
                return binarySearch(mid, mid, term);  // TODO: What do we return?
            }

            return binarySearch(mid + 1, right, term);

        }

        // We reach here when element is not present
        // in array
        return -1;  // TODO: Return what?
    }

    private int rangeSearch(int left, String term) {
        int basePtr = termPtr[left];
        int i = left * 100 ;
        String prevTerm = concatStr.substring(basePtr, basePtr + length[i]);
        basePtr += length[i];

        String curr;
        // Set the bound to fit the number of terms in the current block (starts at index left)
        int bound = ((left == termPtr.length - 1) && (length.length - i < 100)) ?  length.length - i : i + 100;
        ++i;
        while (i < bound) {
            curr = concatStr.substring(basePtr, basePtr + length[i] - prefixSize[i]);
            String prefix = prevTerm.substring(0, prefixSize[i]);
            curr = prefix.concat(curr);
            if (term.equals(curr)) {
                return 0;  // TODO: ??
            }

            prevTerm = curr;
            basePtr += length[i] - prefixSize[i];
            ++i;
        }
        return -1;  // TODO: ??
    }
}
