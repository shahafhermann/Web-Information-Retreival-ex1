package webdata;

import java.io.Serializable;
import java.util.Collection;
import java.util.TreeMap;

public class Dictionary implements Serializable {

    private static final int K = 100;
    boolean isProduct;

    private String concatStr = "";
    private int[] frequency;
    private long[] postingPtr;
    private byte[] length;
    private byte[] prefixSize;
    private int[] termPtr;
    private int numOfBlocks;
    private int numOfTerms;

    /**
     * Constructor.
     * @param termDict A mapping of tokens to their relevant frequencies.
     * @param isProduct Indicate whether the given map is of token or products.
     */
    Dictionary(TreeMap<String, TreeMap<Integer, Integer>> termDict, Boolean isProduct) {
        this.isProduct = isProduct;
        numOfTerms = termDict.size();
        numOfBlocks = (int)Math.ceil(numOfTerms / (double)K);
        termPtr = new int[numOfBlocks];
        frequency = new int[numOfTerms];
        postingPtr = new long[numOfTerms];
        length = new byte[numOfTerms];
        prefixSize = new byte[numOfTerms];
        buildString(termDict);
    }

    /**
     * Build the concatenated String with all known tokens.
     * Update all data structures with it's info.
     */
    private void buildString(TreeMap<String, TreeMap<Integer, Integer>> termDict) {
        int i = 0;
        String prevTerm = "";
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

            postingPtr[i] = encodePostingList(termData, term);

            length[i] = (byte) term.length();
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

    public void searchTerm(String term) {
        binarySearch(0, termPtr.length - 1, term);
    }


    private int binarySearch(int left, int right, String term) {
        if (right >= left) {
            if (right - left == 1) {
                rangeSearch(left, term);
            }
            int mid = left + (right - left) / 2;


            // If the element is present at the
            // middle itself
            if (term.equals(concatStr.substring(termPtr[mid], length[mid * 100])))
                return mid;  // TODO: What do we return?

            // If element is smaller than mid, then
            // it can only be present in left subarray
            if (term.compareTo(concatStr.substring(termPtr[mid], length[mid * 100])) < 0)
                return binarySearch(left, mid, term);  // TODO: mid - 1?

            // Else the element can only be present
            // in right subarray
            return binarySearch(mid, right, term);  // TODO: mid + 1?
        }

        // We reach here when element is not present
        // in array
        return -1;  // TODO: Return what?
    }

    private void rangeSearch(int left, String term) {
        int basePtr = termPtr[left];
        int i = left * 100 ;
        String prevTerm = concatStr.substring(basePtr, basePtr + length[i]);
        basePtr += length[i];

        String curr;
        ++i;
        while (i < i + 100) {
            curr = concatStr.substring(basePtr, basePtr + length[i] - prefixSize[i]);
            String prefix = prevTerm.substring(0, prefixSize[i]);
            curr = prefix.concat(curr);
            if (term.equals(curr)) {
                return;  // TODO: ??
            }

            prevTerm = curr;
            basePtr += length[i];
            ++i;
        }
        return;  // TODO: ??
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
