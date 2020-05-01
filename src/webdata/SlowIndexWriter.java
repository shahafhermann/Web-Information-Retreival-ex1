package webdata;

import java.io.*;
import java.util.Arrays;

/**
 *
 */
public class SlowIndexWriter{

    static final String tokenDictFileName = "tokenDict";
    static final String productDictFileName = "productDict";
    static final String reviewDataFileName = "reviewData";
    static final String productPostingListFileName = "productPostingList";
    static final String tokenPostingListFileName = "tokenPostingList";

    /**
     * Given product review data, creates an on disk index.
     * @param inputFile The path to the file containing the review data.
     * @param dir the directory in which all index files will be created if the directory does not exist, it should be
     *            created.
     */
    public void slowWrite(String inputFile, String dir) {
        removeIndex(dir);

        ReviewsParser parser = new ReviewsParser();
        parser.parseFile(inputFile);

        Dictionary tokenDict = new Dictionary(parser.getTokenDict(), false, dir);
        Dictionary productDict = new Dictionary(parser.getProductDict(), true, dir);

        ReviewData rd = new ReviewData(parser.getProductId(), parser.getReviewHelpfulness(),
                parser.getReviewScore(), parser.getTokensPerReview(), parser.getNumOfReviews());

        try {
            /* Write the new files */
            ObjectOutputStream tokenDictWriter = new ObjectOutputStream(new FileOutputStream(dir + File.separator + tokenDictFileName));
            tokenDictWriter.writeObject(tokenDict);
            tokenDictWriter.close();

            ObjectOutputStream productDictWriter = new ObjectOutputStream(new FileOutputStream(dir + File.separator + productDictFileName));
            productDictWriter.writeObject(productDict);
            productDictWriter.close();

            ObjectOutputStream reviewDataWriter = new ObjectOutputStream(new FileOutputStream(dir + File.separator + reviewDataFileName));
            reviewDataWriter.writeObject(rd);
            reviewDataWriter.close();
        } catch(IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Delete all index files by removing the given directory.
     * @param dir The directory to remove the index from.
     */
    public void removeIndex(String dir) {
        File tokenDictFile = new File(dir + File.separator + tokenDictFileName);
        tokenDictFile.delete();
        File productDictFile = new File(dir + File.separator + productDictFileName);
        productDictFile.delete();
        File reviewDataFile = new File(dir + File.separator + reviewDataFileName);
        reviewDataFile.delete();
        File productPostingListFile = new File(dir + File.separator + productPostingListFileName);
        productPostingListFile.delete();
        File tokenPostingListFile = new File(dir + File.separator + tokenPostingListFileName);
        tokenPostingListFile.delete();
    }
}
