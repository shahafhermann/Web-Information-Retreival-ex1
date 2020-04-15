package webdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class SlowIndexWriter{

    Dictionary tokenIndex;
    Dictionary productIndex;
    TreeMap<String, TreeMap<Integer, Integer>> tokenDict = new TreeMap<>();
    TreeMap<String, TreeMap<Integer, Integer>> productDict = new TreeMap<>();
    ArrayList<String> reviewScore;
    ArrayList<String> reviewHelpfulness;
    int numOfReviews = 0;

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

    private void breakText(String text, int reviewId) {
        String[] tokens = text.split("[^A-Za-z0-9]");
        for (String token: tokens) {
            addTerm(tokenDict, token, reviewId);
        }
    }

    private void parseFile(String inputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(inputFile)))){
            String line = reader.readLine();
            while (line != null){  // TODO: How to separate reviews
                Matcher term;
                ++numOfReviews;  // TODO: add a condition


                term = Pattern.compile("^product/productId: (.*)").matcher(line);
                if (term.find()) {
                    addTerm(productDict, term.group(1), numOfReviews);
                    line = reader.readLine();
                    continue;
                }

                term = Pattern.compile("^review/helpfulness: (.*)").matcher(line);
                if (term.find()) {
                    reviewHelpfulness.add(term.group(1));
                    line = reader.readLine();
                    continue;
                }

                term = Pattern.compile("^review/score: (.*)").matcher(line);
                if (term.find()) {
                    reviewScore.add(term.group(1));
                    line = reader.readLine();
                    continue;
                }

                term = Pattern.compile("^review/text: (.*)").matcher(line);  // TODO: What happens when there's a \n
                if (term.find()) {
                    breakText(term.group(1), numOfReviews);
                    line = reader.readLine();
                    continue;
                }

                line = reader.readLine();  // TODO: ??
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        parseFile("/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1/100.txt");
    }


}
