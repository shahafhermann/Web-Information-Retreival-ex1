package webdata;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 */
public class SlowIndexWriter{

    Dictionary tokenIndex;
    Dictionary productIndex;
    TreeMap<String, TreeMap<Integer, Integer>> tokenDict = new TreeMap<>();
    TreeMap<String, TreeMap<Integer, Integer>> productDict = new TreeMap<>();
    ArrayList<String> reviewScore;
    ArrayList<String> helpfulness;

    /**
     * Given product review data, creates an on disk index.
     * @param inputFile The path to the file containing the review data.
     * @param dir the directory in which all index files will be created if the directory does not exist, it should be
     *            created.
     */
    public void slowWrite(String inputFile, String dir) {

    }

    /**
     * Delete all index files by removing the given directory.
     * @param dir The directory to remove the index from.
     */
    public void removeIndex(String dir) {

    }

    public void addTerm(TreeMap<String, TreeMap<Integer, Integer>> termDict, String token, int reviewId) {
        if (!termDict.containsKey(token)) {
            TreeMap<Integer, Integer> tokenData = new TreeMap<>();
            tokenData.put(reviewId, 1);
            termDict.put(token, tokenData);
        }
        else {
            TreeMap<Integer, Integer> tokenData = termDict.get(token);
            Integer lastReview = tokenData.lastKey();
            Integer lastFrequency = tokenData.get(lastReview);
            if (lastReview == reviewId) {
                tokenData.replace(lastReview, lastFrequency + 1);
            }
            else {
                tokenData.put(reviewId, 1);
            }
            termDict.replace(token, tokenData);
        }
    }


}
