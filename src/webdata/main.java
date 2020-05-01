package webdata;

import java.io.*;
import java.util.*;

public class main {
    public static void main(String[] args) throws IOException {
        String dir = "/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1";
        String file = "/1000.txt";

        SlowIndexWriter siw = new SlowIndexWriter();
        siw.slowWrite(dir + file, dir);
        IndexReader ir = new IndexReader(dir);

        test1(ir);

//        siw.removeIndex(dir);
    }

    private static void test1(IndexReader ir) {
        System.out.println("--- Checking reviewId functions ---");
        int[] vals = {-1, 0, 1, 99, 100, 101};
        for (int val: vals) {
            System.out.println("Value: " + val);
            System.out.println("Product ID: " + ir.getProductId(val));
            System.out.println("Review Score: " + ir.getReviewScore(val));
            System.out.println("Review Helpfulness Numerator: " + ir.getReviewHelpfulnessNumerator(val));
            System.out.println("Review Helpfulness Denominator: " + ir.getReviewHelpfulnessDenominator(val));
            System.out.println("Review Length: " + ir.getReviewLength(val));
        }

        System.out.println();
        System.out.println("--- Checking token functions ---");
        String[] svals = {"the", "popcorn", "zip", "Popcorn", "zohar", "t", "s"};
        for (String s: svals) {
            System.out.println("Value: " + s);
            System.out.println("Token Frequency: " + ir.getTokenFrequency(s));
            System.out.println("Token Collection Frequency: " + ir.getTokenCollectionFrequency(s));
            System.out.println("Tuple Enumeration:");
            Enumeration<Integer> e = ir.getReviewsWithToken(s);
            while (e.hasMoreElements()) {
                System.out.println(e.nextElement());
            }
        }

        System.out.println();
        System.out.println("--- Checking Getters ---");
        System.out.println("Number of Reviews: " + ir.getNumberOfReviews());
        System.out.println("Total Tokens (with repetition): " + ir.getTokenSizeOfReviews());

        System.out.println();
        System.out.println("--- Checking productId Enumeration (not tuples) ---");
        String[] pvals = {"B001E4KFG0", "B0019CW0HE", "B0019CW0HF"};
        for (String s: pvals) {
            System.out.println("Value: " + s);
            Enumeration<Integer> e = ir.getProductReviews(s);
            while (e.hasMoreElements()) {
                System.out.println(e.nextElement());
            }
        }
    }
}
