package webdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A parser for a file of reviews.
 */
public class ReviewsParser {

    private TreeMap<String, TreeMap<Integer, Integer>> tokenDict = new TreeMap<>();
    private TreeMap<String, TreeMap<Integer, Integer>> productDict = new TreeMap<>();
    private ArrayList<String> reviewScore = new ArrayList<>();
    private ArrayList<String> reviewHelpfulness = new ArrayList<>();
    private ArrayList<String> productId = new ArrayList<>();
    private ArrayList<String> tokensPerReview = new ArrayList<>();
    private int numOfReviews = 0;

    /**
     * Return the token dictionary as a tree map
     */
    TreeMap<String, TreeMap<Integer, Integer>> getTokenDict() {
        return tokenDict;
    }

    /**
     * Return the product dictionary as a tree map
     */
    TreeMap<String, TreeMap<Integer, Integer>> getProductDict() { return productDict; }

    /**
     * Return the review scores as an ArrayList of Strings
     */
    ArrayList<String> getReviewScore() {
        return reviewScore;
    }

    /**
     * Return the review helpfulness as an ArrayList of Strings
     */
    ArrayList<String> getReviewHelpfulness() {
        return reviewHelpfulness;
    }

    /**
     * Return the review product IDs as an ArrayList of Strings
     */
    ArrayList<String> getProductId() {
        return productId;
    }

    /**
     * Return the number of token per review as an ArrayList of Strings
     */
    ArrayList<String> getTokensPerReview() {
        return tokensPerReview;
    }

    /**
     * Return the number of reviews
     */
    int getNumOfReviews() { return numOfReviews;}

    /**
     * Adds a new term to the given dictionary at the correct review
     * @param termDict The dictionary to add the term to
     * @param term The term to add
     * @param reviewId The review to which the term belongs
     */
    private void addTerm(TreeMap<String, TreeMap<Integer, Integer>> termDict, String term, int reviewId) {
        if (!termDict.containsKey(term)) {
            TreeMap<Integer, Integer> termData = new TreeMap<>();
            termData.put(reviewId, 1);
            termDict.put(term, termData);
        }
        else {
            TreeMap<Integer, Integer> termData = termDict.get(term);
            Integer lastReview = termData.lastKey();
            Integer lastFrequency = termData.get(lastReview);
            if (lastReview == reviewId) {
                termData.replace(lastReview, lastFrequency + 1);
            }
            else {
                termData.put(reviewId, 1);
            }
            termDict.replace(term, termData);
        }
    }

    /**
     * Break a text to all it's tokens (alphanumeric).
     * @param text The text to break
     * @param reviewId The review to which the text belongs
     */
    private void breakText(String text, int reviewId) {
        String[] tokens = text.split("[^A-Za-z0-9]+");
        int tokenCounter = 0;
        for (String token: tokens) {
            if (!token.isEmpty()) {
                addTerm(tokenDict, token, reviewId);
                ++tokenCounter;
            }
        }
        tokensPerReview.add(String.valueOf(tokenCounter));
    }

    /**
     * Parse the file
     * @param inputFile The file to parse
     */
    void parseFile(String inputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(inputFile)))){
            String line = reader.readLine();
            String textBuffer = "";
            boolean textFlag = false;
            while (line != null){
                Matcher term;

                if (textFlag && !line.contains("product/productId:")) {
                    textBuffer = textBuffer.concat(" ").concat(line);
                    line = reader.readLine();
                    continue;
                }

                term = Pattern.compile("^product/productId: (.*)").matcher(line);
                if (term.find()) {
                    textFlag = false;
                    if (!textBuffer.isEmpty()) {
                        breakText(textBuffer.toLowerCase(), numOfReviews);
                    }
                    ++numOfReviews;
                    productId.add(term.group(1));
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

                term = Pattern.compile("^review/text:(.*)").matcher(line);
                if (term.find()) {
                    textFlag = true;
                    textBuffer = term.group(1);
                    line = reader.readLine();
                    continue;
                }

                line = reader.readLine();
            }

            if (!textBuffer.isEmpty()) {
                breakText(textBuffer.toLowerCase(), numOfReviews);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
