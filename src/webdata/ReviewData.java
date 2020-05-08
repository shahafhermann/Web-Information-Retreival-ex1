package webdata;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * An object representing the data for the reviews
 */
class ReviewData implements Serializable {
    /* A String with all product IDs concatenated */
    private String productId;

    /* The length of a single product ID */
    private byte productIdLen;

    /* Array holding the numerator part of the review's helpfulness */
    private short[] reviewHelpfulnessNumerator;

    /* Array holding the denominator part of the review's helpfulness */
    private short[] reviewHelpfulnessDenominator;

    /* Array holding the review scores */
    private byte[] reviewScore;

    /* Array holding the number of tokens per review */
    private short[] tokensPerReview;

    /* The total number of reviews */
    private int numOfReviews;

    /**
     * Construct the review data object
     * @param productId ArrayList of Strings representing all product IDs
     * @param reviewHelpfulness ArrayList of Strings representing all helpfulness data
     * @param reviewScore ArrayList of Strings representing all review scores
     * @param tokensPerReview ArrayList of Strings representing the number of tokens per review
     * @param numOfReviews The total number of reviews
     */
    ReviewData (ArrayList<String> productId, ArrayList<String> reviewHelpfulness, ArrayList<String> reviewScore,
                ArrayList<String> tokensPerReview, int numOfReviews) {
        productIdLen = (byte) productId.get(0).length();
        this.productId = "";
        for (String product: productId) {
            this.productId = this.productId.concat(product);
        }

        this.reviewHelpfulnessNumerator = new short[numOfReviews];
        this.reviewHelpfulnessDenominator = new short[numOfReviews];
        toPrimitiveArray(reviewHelpfulness, this.reviewHelpfulnessNumerator, this.reviewHelpfulnessDenominator);

        this.reviewScore = new byte[numOfReviews];
        toPrimitiveArray(reviewScore, this.reviewScore);

        this.tokensPerReview = new short[numOfReviews];
        toPrimitiveArray(tokensPerReview, this.tokensPerReview);
        this.numOfReviews = numOfReviews;
    }

    /**
     * Convert an ArrayList of String (with '/' delimiter) to 2 arrays seperated by the delimiter.
     * @param list The array list of strings
     * @param numeratorArr Array of words before the delimiter
     * @param denominatorArr Array of words after the delimiter
     */
    private void toPrimitiveArray(ArrayList<String> list, short[] numeratorArr, short[] denominatorArr) {
        for (int i = 0; i < list.size(); ++i) {
            String[] split = list.get(i).split("/");
            numeratorArr[i] = Short.parseShort(split[0]);
            denominatorArr[i] = Short.parseShort(split[1]);
        }
    }

    /**
     * Convert an ArrayList of String to short array
     * @param list ArrayList of String
     * @param arr Array to populate
     */
    private void toPrimitiveArray(ArrayList<String> list, short[] arr) {
        for (int i = 0; i < list.size(); ++i) {
            arr[i] = Short.parseShort(list.get(i));
        }
    }

    /**
     * Convert an ArrayList of String to byte array
     * @param list ArrayList of String
     * @param arr Array to populate
     */
    private void toPrimitiveArray(ArrayList<String> list, byte[] arr) {
        for (int i = 0; i < list.size(); ++i) {
            arr[i] = Byte.parseByte(list.get(i).split("\\.")[0]);
        }
    }

    /**
     * Return the score for the requested review i
     */
    byte getScore(int i) { return reviewScore[i]; }

    /**
     * Return the helpfulness numerator for the requested review i
     */
    short getHelpfulnessNumerator(int i) { return reviewHelpfulnessNumerator[i]; }

    /**
     * Return the helpfulness denominator for the requested review i
     */
    short getHelpfulnessDenominator(int i) { return reviewHelpfulnessDenominator[i]; }

    /**
     * Return review i's productID
     */
    String getReviewProductId(int i) {
        return productId.substring(i * productIdLen, (i * productIdLen) + productIdLen);
    }

    /**
     * Return the number of tokens in review i
     */
    short getTokensPerReview(int i) { return tokensPerReview[i]; }

    /**
     * Return the number of reviews
     */
    int getNumOfReviews() { return numOfReviews; }
}
