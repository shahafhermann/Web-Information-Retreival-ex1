package webdata;

import java.io.*;
import java.rmi.server.ExportException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

public class IndexReader {

    Dictionary tokenDict;
    Dictionary productDict;
    ReviewData rd;

    /**
     * Creates an IndexReader which will read from the given directory
     * @param dir The directory to read from.
     */
    public IndexReader(String dir) {
        try {
            ObjectInputStream tokenDictReader = new ObjectInputStream(new FileInputStream(dir + File.separator + SlowIndexWriter.tokenDictFileName));
            tokenDict = (Dictionary) tokenDictReader.readObject();
            tokenDictReader.close();

            ObjectInputStream productDictReader = new ObjectInputStream(new FileInputStream(dir + File.separator + SlowIndexWriter.productDictFileName));
            productDict = (Dictionary) productDictReader.readObject();
            productDictReader.close();

            ObjectInputStream reviewDataReader = new ObjectInputStream(new FileInputStream(dir + File.separator + SlowIndexWriter.reviewDataFileName));
            rd = (ReviewData) reviewDataReader.readObject();
            reviewDataReader.close();
        } catch(IOException|ClassNotFoundException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * @param reviewId The review to get the product id for.
     * @return The product identifier for the given review.
     *         Returns null if there is no review with the given identifier.
     */
    public String getProductId(int reviewId) {
        return ((1 <= reviewId) && (reviewId <= rd.getNumOfReviews())) ? rd.getReviewProductId(reviewId - 1) : null;
    }

    /**
     * @param reviewId The review to get the score for.
     * @return The score for a given review.
     *         Returns -1 if there is no review with the given identifier.
     */
    public int getReviewScore(int reviewId) {
        return ((1 <= reviewId) && (reviewId <= rd.getNumOfReviews())) ?
                rd.getScore(reviewId - 1) : -1;
    }

    /**
     * @param reviewId The review to get the numerator for the helpfulness for.
     * @return The numerator for the helpfulness of a given review
     *         Returns -1 if there is no review with the given identifier
     */
    public int getReviewHelpfulnessNumerator(int reviewId) {
        return ((1 <= reviewId) && (reviewId <= rd.getNumOfReviews())) ?
                rd.getHelpfulnessNumerator(reviewId - 1) : -1;
    }

    /**
     * @param reviewId The review to get the denominator for the helpfulness for.
     * @return The denominator for the helpfulness of a given review
     *         Returns -1 if there is no review with the given identifier
     */
    public int getReviewHelpfulnessDenominator(int reviewId) {
        return ((1 <= reviewId) && (reviewId <= rd.getNumOfReviews())) ?
                rd.getHelpfulnessDenominator(reviewId - 1) : -1;
    }

    /**
     * @param reviewId The review to get the number of tokens for.
     * @return The number of tokens in a given review
     *         Returns -1 if there is no review with the given identifier
     */
    public int getReviewLength(int reviewId) {
        return ((1 <= reviewId) && (reviewId <= rd.getNumOfReviews())) ?
                rd.getTokensPerReview(reviewId - 1) : -1;
    }


    // ------------------------------------------------------------ //


    /**
     * @param token The token to check.
     * @return The number of reviews containing a given token (i.e., word)
     *         Returns 0 if there are no reviews containing this token
     */
    public int getTokenFrequency(String token) {
        int i = tokenDict.searchTerm(token.toLowerCase());
        if (i < 0 || i >= tokenDict.getNumOfTerms()) {
            return 0;
        }
        long pos = tokenDict.getPostingPtr(i);
        return tokenDict.readLength(pos);
    }

    /**
     * @param token The token to check.
     * @return The number of times that a given token (i.e., word) appears in the reviews indexed
     *         Returns 0 if there are no reviews containing this token
     */
    public int getTokenCollectionFrequency(String token) {
        int i = tokenDict.searchTerm(token.toLowerCase());
        if (i < 0 || i >= tokenDict.getNumOfTerms()) {
            return 0;
        }
        return tokenDict.getFrequency(i);
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
         return enumHelper(tokenDict, token.toLowerCase());
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
        for (int i = 1; i <= getNumberOfReviews(); ++i) {
            tokenCount += getReviewLength(i);
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
        return enumHelper(productDict, productId);
    }


    // ---------------------------------------------------------- //


    /**
     * Get the Enumaration list for the given Dictionary and term.
     * @param dict Dictionary
     * @param term Term
     * @return Enumaration
     */
    private Enumeration<Integer> enumHelper(Dictionary dict, String term) {
        int i = dict.searchTerm(term);
        if (i < 0 || i >= tokenDict.getNumOfTerms()) {
            return new Vector<Integer>().elements();
        }
        long pos = dict.getPostingPtr(i);
        long nextPos = (i + 1 < dict.getNumOfTerms()) ? dict.getPostingPtr(i + 1) : -1;
        Integer[] list = dict.read(pos, nextPos);

        Vector<Integer> reviewsWithToken = new Vector<>(Arrays.asList(list));
        return reviewsWithToken.elements();
    }
}
