package webdata;

import java.io.*;
import java.util.*;

//public class main {
//    public static void main(String[] args) throws IOException {
//        File dir = new File("/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1/shahafIsRight");
//        System.out.println(dir.exists());
////        String dir = "/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1";
////        String file = "/1000.txt";
////
////        SlowIndexWriter siw = new SlowIndexWriter();
////        siw.slowWrite(dir + file, dir);
////        IndexReader ir = new IndexReader(dir);
////
////        test1(ir);
//
////        siw.removeIndex(dir);
//    }
//
//    private static void test1(IndexReader ir) {
//        System.out.println("--- Checking reviewId functions ---");
//        int[] vals = {-1, 0, 1, 99, 100, 101};
//        for (int val: vals) {
//            System.out.println("Value: " + val);
//            System.out.println("Product ID: " + ir.getProductId(val));
//            System.out.println("Review Score: " + ir.getReviewScore(val));
//            System.out.println("Review Helpfulness Numerator: " + ir.getReviewHelpfulnessNumerator(val));
//            System.out.println("Review Helpfulness Denominator: " + ir.getReviewHelpfulnessDenominator(val));
//            System.out.println("Review Length: " + ir.getReviewLength(val));
//        }
//
//        System.out.println();
//        System.out.println("--- Checking token functions ---");
//        String[] svals = {"the", "popcorn", "zip", "Popcorn", "zohar", "t", "s"};
//        for (String s: svals) {
//            System.out.println("Value: " + s);
//            System.out.println("Token Frequency: " + ir.getTokenFrequency(s));
//            System.out.println("Token Collection Frequency: " + ir.getTokenCollectionFrequency(s));
//            System.out.println("Tuple Enumeration:");
//            Enumeration<Integer> e = ir.getReviewsWithToken(s);
//            while (e.hasMoreElements()) {
//                System.out.println(e.nextElement());
//            }
//        }
//
//        System.out.println();
//        System.out.println("--- Checking Getters ---");
//        System.out.println("Number of Reviews: " + ir.getNumberOfReviews());
//        System.out.println("Total Tokens (with repetition): " + ir.getTokenSizeOfReviews());
//
//        System.out.println();
//        System.out.println("--- Checking productId Enumeration (not tuples) ---");
//        String[] pvals = {"B001E4KFG0", "B0019CW0HE", "B0019CW0HF"};
//        for (String s: pvals) {
//            System.out.println("Value: " + s);
//            Enumeration<Integer> e = ir.getProductReviews(s);
//            while (e.hasMoreElements()) {
//                System.out.println(e.nextElement());
//            }
//        }
//    }
//}

//import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import webdata.IndexReader;
import webdata.SlowIndexWriter;

import java.io.*;
import java.util.Enumeration;

public class main {

    static final String INDICES_DIR_NAME = "indices";
    static final String REVIEWS_FILE_NAME_1000 = "1000.txt";

    public static void main(String[] args) {
        final String dir = System.getProperty("user.dir");
        final String indicesDir = dir + File.separatorChar + INDICES_DIR_NAME;

        buildIndex(indicesDir);
        queryWordIndex(indicesDir);
        queryMetaData(indicesDir);
        queryReviewMetaData(indicesDir);
        queryProductIndex(indicesDir);

        deleteIndex(indicesDir);

    }

    private static void queryReviewMetaData(String indicesDir) {
        IndexReader indexReader = new IndexReader(indicesDir);
        int[] ridsTestCases = {1, 11, 12, 999, 1001, 0, -2, 10, 32, 522};
        for (int rid : ridsTestCases) {
            System.out.println("rid: " + rid + " " +
                    indexReader.getProductId(rid) + " " +
                    indexReader.getReviewScore(rid) + " " +
                    indexReader.getReviewHelpfulnessNumerator(rid) + " " +
                    indexReader.getReviewHelpfulnessDenominator(rid) + " " +
                    indexReader.getReviewLength(rid));
        }
    }

    private static void queryMetaData(String indicesDir) {
        IndexReader indexReader = new IndexReader(indicesDir);
        System.out.println(indexReader.getNumberOfReviews());
        System.out.println(indexReader.getTokenSizeOfReviews());
    }


    private static void testGetReviewsWithToken(IndexReader indexReader,
                                                String[] wordTestCases) {
        System.out.println("Checking getReviewsWithToken...");
        for (String word : wordTestCases) {
            Enumeration<Integer> res = indexReader.getReviewsWithToken(word);
            System.out.print(word + ": " + System.lineSeparator());
            while (res.hasMoreElements()) {
                System.out.print(res.nextElement().toString() + " ");
            }
            System.out.println();
        }
    }

    private static void testGetTokenFrequency(IndexReader indexReader,
                                              String[] wordTestCases) {
        System.out.println("Checking getTokenFrequency...");
        for (String word : wordTestCases) {
            int numOfReviews = indexReader.getTokenFrequency(word);
            System.out.println(word + ": " + "numOfReviews: " + numOfReviews);
        }
    }

    private static void testGetTokenCollectionFrequency(IndexReader indexReader,
                                                        String[] wordTestCases) {
        System.out.println("Checking getTokenCollectionFrequency...");
        for (String word : wordTestCases) {
            int numOfMentions = indexReader.getTokenCollectionFrequency(word);
            System.out.println(word + ": " + "numOfMentions: " + numOfMentions);
        }
    }


    private static void queryWordIndex(String indicesDir) {
        IndexReader indexReader = new IndexReader(indicesDir);
        String[] wordTestCases = {"0", "bulba", "zzz", "1", "9oz", "a", "crunchy", "how", "laxative",
                "prefer", "storebought", "zucchini", "the"};
        testGetReviewsWithToken(indexReader, wordTestCases);
        testGetTokenFrequency(indexReader, wordTestCases);
        testGetTokenCollectionFrequency(indexReader, wordTestCases);

    }


    private static void queryProductIndex(String indicesDir) {
        IndexReader indexReader = new IndexReader(indicesDir);
        String[] productTestCases = {"A009ASDF5", "B099ASDF5", "B0001PB9FE", "B0002567IW", "B000ER6YO0",
                "B000G6RYNE", "B006F2NYI2", "B009HINRX8", "B001E4KFG0"};
        for (String pid : productTestCases) {
            Enumeration<Integer> res = indexReader.getProductReviews(pid);
            System.out.print(pid + ": " + System.lineSeparator());
            while (res.hasMoreElements()) {
                System.out.print(res.nextElement().toString() + " ");
            }
            System.out.println();
        }
    }


    private static void deleteIndex(String indicesDir) {
        SlowIndexWriter slowIndexWriter = new SlowIndexWriter();
        slowIndexWriter.removeIndex(indicesDir);
    }

    private static void buildIndex(String indicesDir) {

        SlowIndexWriter slowIndexWriter = new SlowIndexWriter();
        slowIndexWriter.slowWrite(REVIEWS_FILE_NAME_1000, indicesDir);
    }
}
