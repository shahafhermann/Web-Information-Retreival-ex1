package webdata;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

public class Dictionary implements Serializable {

    public TableOfContents table;

    private static final int K = 100;
    private boolean isProduct;
    private String concatStr = "";
    private int[] termPtr;
    private int numOfBlocks;
    private int numOfTerms;

//    private static class TableOfContents {
//        private int[] frequency;
//        private long[] postingPtr;
//        private byte[] length;
//        private byte[] prefixSize;
//    }

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
        table = new TableOfContents(numOfTerms);
        build(termDict);
    }

    public int getNumOfTerms() {
        return numOfTerms;
    }

    /**
     * Build the concatenated String with all known tokens.
     * Update all data structures with it's info.
     */
    private void build(TreeMap<String, TreeMap<Integer, Integer>> termDict) {
        int i = 0;
        String prevTerm = "";

        for (String term: termDict.keySet()) {  // For each token
            if (i % K == 0) {
                termPtr[(i / 100)] = concatStr.length();
                table.setPrefixSize(i, (byte)0);
                concatStr = concatStr.concat(term);
            }
            else {
                byte psize = findPrefix(prevTerm, term);
                table.setPrefixSize(i, psize);
                concatStr = concatStr.concat(term.substring(psize));
            }

            buildFrequency(termDict.get(term), i);

            buildPostingList(termDict.get(term), i);  // TODO: WHAT

            table.setLength(i, (byte) term.length());
            prevTerm = term;
            ++i;
        }
    }

    private void buildFrequency(TreeMap<Integer, Integer> termData, int i) {
        Collection<Integer> allFrequencies = termData.values();
        table.setFrequency(i, allFrequencies.stream().mapToInt(Integer::intValue).sum());  // Sum all values
    }

    private void buildPostingList(TreeMap<Integer, Integer> termData, int i) {
        ArrayList<Integer> reviews = new ArrayList<>(termData.keySet());
        ArrayList<Byte> encodedReviews = Encoder.encode(reviews, true);
        table.setPostingPtr(i, write(encodedReviews, dir));
        if (!isProduct) {
            ArrayList<Integer> frequencies = new ArrayList<>(termData.values());
            ArrayList<Byte> encodedFrequencies = Encoder.encode(frequencies, false);
            write(encodedFrequencies, dir);
        }
    }

    public static long write(ArrayList<Byte> arr, String dir){
        try (RandomAccessFile raf = new RandomAccessFile(dir, "rw")){
            raf.seek(raf.length());
            long pos = raf.getFilePointer();
            for (byte b: arr) {
                raf.writeByte(b);
            }
            return pos;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return -1;  // Will never happen
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
     * @param term
     * @return
     */
    public int searchTerm(String term) {
        return binarySearch(0, numOfBlocks - 1, term);  // TODO: debug numOfBlocks - 1
    }

    /**
     *
     * @param left
     * @param right
     * @param term
     * @return
     */
    private int binarySearch(int left, int right, String term) {
        if (right == left) {
            if (term.equals(concatStr.substring(termPtr[left], termPtr[left] + table.getLength(left * 100))))
                return left * 100;  // TODO: What do we return?
            return rangeSearch(left, term);
        }
        if (right > left) {
            int mid = left + (right - left) / 2;


            // If the element is present at the
            // middle itself
            if (term.equals(concatStr.substring(termPtr[mid], termPtr[mid] + table.getLength(mid * 100))))
                return mid * 100;

            // If element is smaller than mid, then
            // it can only be present in left subarray
            if (term.compareTo(concatStr.substring(termPtr[mid], termPtr[mid] + table.getLength(mid * 100))) < 0)
                return binarySearch(left, mid - 1, term);

            // Else the element can only be present
            // in right subarray
            if (term.compareTo(concatStr.substring(termPtr[mid + 1],
                                                   termPtr[mid + 1] + table.getLength((mid + 1) * 100))) < 0) {
                return binarySearch(mid, mid, term);
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
        String prevTerm = concatStr.substring(basePtr, basePtr + table.getLength(i));
        basePtr += table.getLength(i);

        String curr;
        // Set the bound to fit the number of terms in the current block (starts at index left)
        int bound = ((left == termPtr.length - 1) && (numOfTerms - i < 100)) ?  numOfTerms - i : i + 100;
        ++i;
        while (i < bound) {
            curr = concatStr.substring(basePtr, basePtr + table.getLength(i) - table.getPrefixSize(i));
            String prefix = prevTerm.substring(0, table.getPrefixSize(i));
            curr = prefix.concat(curr);
            if (term.equals(curr)) {
                return i;
            }

            prevTerm = curr;
            basePtr += table.getLength(i) - table.getPrefixSize(i);
            ++i;
        }
        return -1;
    }
}
