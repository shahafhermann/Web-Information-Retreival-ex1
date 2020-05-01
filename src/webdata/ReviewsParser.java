package webdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReviewsParser {

    private TreeMap<String, TreeMap<Integer, Integer>> tokenDict = new TreeMap<>();
    private TreeMap<String, TreeMap<Integer, Integer>> productDict = new TreeMap<>();
    private ArrayList<String> reviewScore = new ArrayList<>();
    private ArrayList<String> reviewHelpfulness = new ArrayList<>();
    private ArrayList<String> productId = new ArrayList<>();
    private ArrayList<String> tokensPerReview = new ArrayList<>();
    private int numOfReviews = 0;

    public TreeMap<String, TreeMap<Integer, Integer>> getTokenDict() {
        return tokenDict;
    }

    public TreeMap<String, TreeMap<Integer, Integer>> getProductDict() {
        return productDict;
    }

    public ArrayList<String> getReviewScore() {
        return reviewScore;
    }

    public ArrayList<String> getReviewHelpfulness() {
        return reviewHelpfulness;
    }

    public ArrayList<String> getProductId() {
        return productId;
    }

    public ArrayList<String> getTokensPerReview() {
        return tokensPerReview;
    }

    public int getNumOfReviews() { return numOfReviews;}

    /**
     *
     * @param termDict
     * @param token
     * @param reviewId
     */
    private void addTerm(TreeMap<String, TreeMap<Integer, Integer>> termDict, String token, int reviewId) {
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

    /**
     * Break a text to all it's tokens (alphanumeric).
     * @param text
     * @param reviewId
     */
    private void breakText(String text, int reviewId) {
        String[] tokens = text.split("[^A-Za-z0-9]+");
//        tokensPerReview.add(String.valueOf(tokens.length));
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
     *
     * @param inputFile
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

                term = Pattern.compile("^review/text: (.*)").matcher(line);
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
