package webdata;

import java.util.Enumeration;

/**
 *
 */
public class IndexReader {

    /**
     * Creates an IndexReader which will read from the given directory
     * @param dir The directory to read from.
     */
    public IndexReader(String dir) {

    }

    /**
     * @param reviewId The review to get the product id for.
     * @return The product identifier for the given review.
     *         Returns null if there is no review with the given identifier.
     */
    public String getProductId(int reviewId) {

    }

    /**
     * @param reviewId The review to get the score for.
     * @return The score for a given review.
     *         Returns -1 if there is no review with the given identifier.
     */
    public int getReviewScore(int reviewId) {

    }

    /**
     * @param reviewId The review to get the numerator for the helpfulness for.
     * @return The numerator for the helpfulness of a given review
     *         Returns -1 if there is no review with the given identifier
     */
    public int getReviewHelpfulnessNumerator(int reviewId) {

    }

    /**
     * @param reviewId The review to get the denominator for the helpfulness for.
     * @return The denominator for the helpfulness of a given review
     *         Returns -1 if there is no review with the given identifier
     */
    public int getReviewHelpfulnessDenominator(int reviewId) {

    }

    /**
     * @param reviewId The review to get the number of tokens for.
     * @return The number of tokens in a given review
     *         Returns -1 if there is no review with the given identifier
     */
    public int getReviewLength(int reviewId) {

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

    }

    /**
     * @return The number of tokens in the system (Tokens should be counted as many times as they appear).
     */
    public int getTokenSizeOfReviews() {

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
