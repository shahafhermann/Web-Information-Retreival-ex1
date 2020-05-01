package webdata;

import java.io.Serializable;
import java.util.ArrayList;

class ReviewData implements Serializable {
    private String[] productId;
    private short[] reviewHelpfulnessNumerator;
    private short[] reviewHelpfulnessDenominator;
    private byte[] reviewScore;
    private short[] tokensPerReview;
    private int numOfReviews;

    ReviewData (ArrayList<String> productId, ArrayList<String> reviewHelpfulness, ArrayList<String> reviewScore,
                ArrayList<String> tokensPerReview, int numOfReviews) {
        this.productId = new String[numOfReviews];
        this.productId = productId.toArray(this.productId);

        this.reviewHelpfulnessNumerator = new short[numOfReviews];
        this.reviewHelpfulnessDenominator = new short[numOfReviews];
        toPrimitiveArray(reviewHelpfulness, this.reviewHelpfulnessNumerator, this.reviewHelpfulnessDenominator);

        this.reviewScore = new byte[numOfReviews];
        toPrimitiveArray(reviewScore, this.reviewScore);

        this.tokensPerReview = new short[numOfReviews];
        toPrimitiveArray(tokensPerReview, this.tokensPerReview);
        this.numOfReviews = numOfReviews;
    }

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

    byte getScore(int i) { return reviewScore[i]; }

    short getHelpfulnessNumerator(int i) { return reviewHelpfulnessNumerator[i]; }

    short getHelpfulnessDenominator(int i) { return reviewHelpfulnessDenominator[i]; }

    String getReviewProductId(int i) { return productId[i]; }

    short getTokensPerReview(int i) { return tokensPerReview[i]; }

    int getNumOfReviews() { return numOfReviews; }
}
