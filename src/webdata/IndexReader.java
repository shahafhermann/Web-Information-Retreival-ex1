package webdata;

import java.util.Enumeration;

/**
 *
 */
public class IndexReader {

    Dictionary tokenDict;
    Dictionary productDict;
    ReviewData rd;

    /**
     * Creates an IndexReader which will read from the given directory
     * @param dir The directory to read from.
     */
    public IndexReader(String dir) {
        //TODO: Read dictionaries from disk
        ReviewsParser parser = new ReviewsParser();
        parser.parseFile(dir);
        tokenDict = new Dictionary(parser.getTokenDict(), false);
        productDict = new Dictionary(parser.getProductDict(), true);
        rd = new ReviewData(parser.getProductId(), parser.getReviewHelpfulness(), parser.getReviewScore(),
                            parser.getTokensPerReview(), parser.getNumOfReviews());
    }

    /**
     * @param reviewId The review to get the product id for.
     * @return The product identifier for the given review.
     *         Returns null if there is no review with the given identifier.
     */
    public String getProductId(int reviewId) {
        return rd.getProductId(reviewId);
    }

    /**
     * @param reviewId The review to get the score for.
     * @return The score for a given review.
     *         Returns -1 if there is no review with the given identifier.
     */
    public int getReviewScore(int reviewId) {
        String score = rd.getReviewScore(reviewId);
        return Integer.parseInt(score);  //TODO: taking care of the decimal dot?
    }

    /**
     * @param reviewId The review to get the numerator for the helpfulness for.
     * @return The numerator for the helpfulness of a given review
     *         Returns -1 if there is no review with the given identifier
     */
    public int getReviewHelpfulnessNumerator(int reviewId) {
        String helpfulness = rd.getReviewHelpfulness(reviewId);
        String numerator = helpfulness.split("/")[0];
        return Integer.parseInt(numerator);  //TODO: taking care of the decimal dot?
    }

    /**
     * @param reviewId The review to get the denominator for the helpfulness for.
     * @return The denominator for the helpfulness of a given review
     *         Returns -1 if there is no review with the given identifier
     */
    public int getReviewHelpfulnessDenominator(int reviewId) {
        String helpfulness = rd.getReviewHelpfulness(reviewId);
        String denominator = helpfulness.split("/")[1];
        return Integer.parseInt(denominator);  //TODO: taking care of the decimal dot?
    }

    /**
     * @param reviewId The review to get the number of tokens for.
     * @return The number of tokens in a given review
     *         Returns -1 if there is no review with the given identifier
     */
    public int getReviewLength(int reviewId) {
        return Integer.parseInt(rd.getTokensPerReview(reviewId));  // TODO: Check with Sara, we return duplicates
    }


    // ------------------------------------------------------------ //


    /**
     * @param token The token to check.
     * @return The number of reviews containing a given token (i.e., word)
     *         Returns 0 if there are no reviews containing this token
     */
    public int getTokenFrequency(String token) {

    }

    /**
     * @param token The token to check.
     * @return The number of times that a given token (i.e., word) appears in the reviews indexed
     *         Returns 0 if there are no reviews containing this token
     */
    public int getTokenCollectionFrequency(String token) {
        int i = tokenDict.searchTerm(token);
        return tokenDict.table.getFrequency(i);
    }

    /**
     * @param token The token to check.
     * @return A series of integers of the form id-1, freq-1, id-2, freq-2, ... such that
     *         id-n is the n-th review containing the given token and freq-n is the number of times that the token
     *         appears in review id-n.
     *         Note that the integers should be sorted by id.
     *         Returns an empty Enumeration if there are no reviews containing this token.
     */
     public Enumeration<Integer> getReviewsWithToken(String token) {

     }


     // --------------------------------------------------------- //


     /**
     * @return The number of product reviews available in the system.
     */
    public int getNumberOfReviews() {
        return rd.getNumOfReviews();
    }

    /**
     * @return The number of tokens in the system (Tokens should be counted as many times as they appear).
     */
    public int getTokenSizeOfReviews() {
        int tokenCount = 0;
        for (int i = 0; i < tokenDict.getNumOfTerms(); ++i) {
            tokenCount += tokenDict.table.getFrequency(i);
        }
        return tokenCount;
    }


    // ---------------------------------------------------------- //


    /**
     * @param productId The id of the product to check.
     * @return The ids of the reviews for a given product identifier.
     *         Note that the integers returned should be sorted by id.
     *         Returns an empty Enumeration if there are no reviews for this product.
     */
    public Enumeration<Integer> getProductReviews(String productId) {

    }
}
