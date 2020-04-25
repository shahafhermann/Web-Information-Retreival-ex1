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
    private String path;

    /**
     * Constructor.
     * @param termDict A mapping of tokens to their relevant frequencies.
     * @param isProduct Indicate whether the given map is of token or products.
     */
    Dictionary(TreeMap<String, TreeMap<Integer, Integer>> termDict, Boolean isProduct, String dir) {
        this.isProduct = isProduct;
        numOfTerms = termDict.size();
        numOfBlocks = (int)Math.ceil(numOfTerms / (double)K);
        termPtr = new int[numOfBlocks];
        table = new TableOfContents(numOfTerms);
        path = (isProduct) ? dir + "tempProduct" : dir + "tempToken";

        build(termDict);
    }

    /**
     * @return The number of terms in this dictionary (without duplicates)
     */
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
                termPtr[(i / K)] = concatStr.length();
                table.setPrefixSize(i, (byte)0);
                concatStr = concatStr.concat(term);
            }
            else {
                byte psize = findPrefix(prevTerm, term);
                table.setPrefixSize(i, psize);
                concatStr = concatStr.concat(term.substring(psize));
            }

            buildFrequency(termDict.get(term), i);

            buildPostingList(termDict.get(term), i);

            table.setLength(i, (byte) term.length());
            prevTerm = term;
            ++i;
        }
    }

    /**
     * Populate the frequency data structure under TableOfContents.
     * @param termData The data for the currently processed term
     * @param i Index to add at
     */
    private void buildFrequency(TreeMap<Integer, Integer> termData, int i) {
        Collection<Integer> allFrequencies = termData.values();
        table.setFrequency(i, allFrequencies.stream().mapToInt(Integer::intValue).sum());  // Sum all values
    }

    /**
     * Populate the posting list data structure under TableOfContents.
     * @param termData The data for the currently processed term
     * @param i Index to add at
     */
    private void buildPostingList(TreeMap<Integer, Integer> termData, int i) {
        ArrayList<Integer> reviews = new ArrayList<>(termData.keySet());
        ArrayList<Byte> encodedReviews = Encoder.encode(reviews, true);
        table.setPostingPtr(i, write(encodedReviews));
        if (!isProduct) {
            ArrayList<Integer> frequencies = new ArrayList<>(termData.values());
            ArrayList<Byte> encodedFrequencies = Encoder.encode(frequencies, false);
            write(encodedFrequencies);
        }
    }

    /**
     * Write an array to a file specified in path.
     * @param arr Array to write
     * @return Position that written started
     */
    public long write(ArrayList<Byte> arr){
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")){
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
     *
     * @param pos
     * @return
     */
    public int readLength(long pos){
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")){
            raf.seek(pos);
            return raf.readInt();  // Read the first 4 bytes, hence the length of the wanted array
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return -1;  // Will never happen
    }

    /**
     *
     * @param pos
     * @return
     */
    public Integer[] read(long pos, long nextPos) {
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")){
            nextPos = (nextPos == -1) ? raf.length(): nextPos;
            raf.seek(pos);
            int len = raf.readInt();  // Read the first 4 bytes, hence the length of the wanted array
            raf.seek(pos);  // Reset the pos
            byte[] byteArray = new byte[(int) (nextPos - pos)];
            raf.read(byteArray);

            long[] endPtr = new long[1];  // TODO does this change
            Integer[] reviews = Encoder.decode(byteArray, true, endPtr);
            assert (len == reviews.length);
            if (!isProduct) {
                raf.seek(pos + endPtr[0]);
                raf.read(byteArray);
                Integer[] frequencies = Encoder.decode(byteArray, false, endPtr);
                return weave(reviews, frequencies);
            }
            return reviews;

        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;  // Will never happen
    }

    /**
     * Weave two arrays with elements alternatively inserted into the output.
     * @param a1 First array
     * @param a2 Second array
     * @return The two arrays weaved together
     */
    private Integer[] weave(Integer[] a1, Integer[] a2) {
        Integer[] output = new Integer[a1.length + a2.length];
        int i = 0, j = 0, k = 0;

        // Traverse both array
        while (i < a1.length && j < a2.length)
        {
            output[k++] = a1[i++];
            output[k++] = a2[j++];
        }

        // Store remaining elements of first array
        while (i < a1.length)
            output[k++] = a1[i++];

        // Store remaining elements of second array
        while (j < a2.length)
            output[k++] = a2[j++];

        return output;
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
            if (term.equals(concatStr.substring(termPtr[left], termPtr[left] + table.getLength(left * K))))
                return left * K;
            return rangeSearch(left, term);
        }
        if (right > left) {
            int mid = left + (right - left) / 2;


            // If the element is present at the
            // middle itself
            if (term.equals(concatStr.substring(termPtr[mid], termPtr[mid] + table.getLength(mid * K))))
                return mid * K;

            // If element is smaller than mid, then
            // it can only be present in left subarray
            if (term.compareTo(concatStr.substring(termPtr[mid], termPtr[mid] + table.getLength(mid * K))) < 0)
                return binarySearch(left, mid - 1, term);

            // Else the element can only be present
            // in right subarray
            if (term.compareTo(concatStr.substring(termPtr[mid + 1],
                                                   termPtr[mid + 1] + table.getLength((mid + 1) * K))) < 0) {
                return binarySearch(mid, mid, term);
            }

            return binarySearch(mid + 1, right, term);

        }

        // We reach here when element is not present in array
        return -1;
    }

    private int rangeSearch(int left, String term) {
        int basePtr = termPtr[left];
        int i = left * K ;
        String prevTerm = concatStr.substring(basePtr, basePtr + table.getLength(i));
        basePtr += table.getLength(i);

        String curr;
        // Set the bound to fit the number of terms in the current block (starts at index left)
        int bound = ((left == termPtr.length - 1) && (numOfTerms - i < K)) ?  numOfTerms - i : i + K;
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
