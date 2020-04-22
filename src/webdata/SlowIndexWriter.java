package webdata;

import java.io.BufferedWriter;

/**
 *
 */
public class SlowIndexWriter{

    /**
     * Given product review data, creates an on disk index.
     * @param inputFile The path to the file containing the review data.
     * @param dir the directory in which all index files will be created if the directory does not exist, it should be
     *            created.
     */
    public void slowWrite(String inputFile, String dir) {
//        ReviewsParser parser = new ReviewsParser();
//        parser.parseFile(inputFile);
//
//        Dictionary tokenDict = new Dictionary(parser.getTokenDict(), false);
//        Dictionary productDict = new Dictionary(parser.getProductDict(), true);
//
//        ReviewData rd = new ReviewData(parser.getProductId(), parser.getReviewHelpfulness(),
//                                       parser.getReviewScore(), parser.getTokensPerReview(), parser.getNumOfReviews());
//
//        ////
//        PostingList tokenPostingList = new...
//
//        tokenPostingList.write(parser.getTokenDict());
//        productPostingList.write(parser.getProductDict());
//
//        tokenDict.table.setPostingPtr(tokenDict.postingList.read(pos));
//        ////
//
//        writeObject(tokenDict);

    }

    /**
     * Delete all index files by removing the given directory.
     * @param dir The directory to remove the index from.
     */
    public void removeIndex(String dir) {

    }
}
