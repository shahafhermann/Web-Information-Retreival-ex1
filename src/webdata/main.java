package webdata;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class main {
    public static void main(String[] args) throws IOException {
        String dir = "/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1";
        String file = "/1000.txt";

        SlowIndexWriter siw = new SlowIndexWriter();
        siw.slowWrite(dir + file, dir);

//            System.out.println(Arrays.equals(tokenDict.table.prefixSize, d.table.prefixSize));
//            System.out.println(Arrays.equals(tokenDict.table.length, d.table.length));
//            System.out.println(Arrays.equals(tokenDict.table.postingPtr, d.table.postingPtr));
//            System.out.println(Arrays.equals(tokenDict.table.frequency, d.table.frequency));
//
//            System.out.println(tokenDict.isProduct == d.isProduct);
//            System.out.println(tokenDict.numOfBlocks == d.numOfBlocks);
//            System.out.println(tokenDict.numOfTerms == d.numOfTerms);
//            System.out.println(Arrays.equals(tokenDict.termPtr, d.termPtr));
//            System.out.println(tokenDict.concatStr.equals(d.concatStr));
//            System.out.println(tokenDict.path.equals(d.path));

//        ReviewsParser parser = new ReviewsParser();
//        parser.parseFile(dir + file);
//
//        Dictionary tokenDict = new Dictionary(parser.getTokenDict(), false, dir);
//        Dictionary productDict = new Dictionary(parser.getProductDict(), true, dir);
//
//        ReviewData rd = new ReviewData(parser.getProductId(), parser.getReviewHelpfulness(),
//                                       parser.getReviewScore(), parser.getTokensPerReview(), parser.getNumOfReviews());

//        int i = tokenDict.searchTerm("pop");
//        if (!(i < 0 || i >= tokenDict.getNumOfTerms())) {
//            long pos = tokenDict.table.getPostingPtr(i);
//            long nextPos = (i + 1 < tokenDict.getNumOfTerms()) ? tokenDict.table.getPostingPtr(i + 1) : -1;
//            tokenDict.read(pos, nextPos);
//        }

//        File f = new File("/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1/testingFiles/tempToken");
//        f.delete();
//        f = new File("/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1/testingFiles/tempProduct");
//        f.delete();
//
//        IndexReader ir = new IndexReader(dir);
//        Enumeration<Integer> e = ir.getReviewsWithToken("ze");
//        while (e.hasMoreElements()) {
//            System.out.println(e.nextElement());
//        }
//        System.out.println(ir.getReviewsWithToken("the"));
//        System.out.println(ir.getReviewsWithToken("popcorn"));
//        System.out.println(ir.getReviewsWithToken("zohar"));




//        byte[] arr = {0, 0, 0, 2, 5, 10, 32, 11, 127};
//        ArrayList<Byte> arrB = new ArrayList<>();
//        for(byte a: arr) {
//            arrB.add(a);
//        }
//        long pos = Dictionary.write(arrB, "/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1/encodeOutput1.txt");
//        System.out.println(pos);
//
//        pos = Dictionary.write(arrB, "/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1/encodeOutput1.txt");
//        System.out.println(pos);



//        String score = "5.0".split("\\.")[0];
//        String numerator = "19/20".split("/")[0];
//        String denominator = "19/20".split("/")[1];
//        System.out.println(Integer.parseInt(numerator));
//        System.out.println(Integer.parseInt(denominator));
//        System.out.println(Integer.parseInt("5.5"));
//        System.out.println(Integer.parseInt("5"));
//        System.out.println(Integer.parseInt(" 5.0"));
//        System.out.println(Integer.parseInt("5.0 "));


//        int[] encodeTestKeys = {1, 50, 500, 5000, 50000, 90000, 100000, 1000000, 10000000, 100000000, 2000000000};
//        int[] encodeTestValues = {5, 1000, 50189};
//
//        RandomAccessFile encodeOutput = new RandomAccessFile(
//                "/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1/encodeOutput.txt", "rw");
//        ArrayList<Byte> a = Encoder.encode(encodeTestKeys, true);
//        byte[] b = new byte[a.size()];
//        for (int i = 0; i < a.size(); ++i) {
//            b[i] = a.get(i);
//        }
//        int[] c = Encoder.decode(b, true);
//        System.out.println(c);

//        encodeOutput.seek(0);
//        byte[] bytesFromFile = new byte[(int) encodeOutput.length()];
//        encodeOutput.readFully(bytesFromFile);
//        int x = 1;


//        SlowIndexWriter siw1 = new SlowIndexWriter();
//        String inputFile1 = "/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1/testReview1.txt";
//        siw1.parseFile(inputFile1);
//
//        Dictionary td1 = new Dictionary(siw1.tokenDict, false);
//        Dictionary pd1 = new Dictionary(siw1.productDict, true);
//
//        SlowIndexWriter siw2 = new SlowIndexWriter();
//        String inputFile2 = "/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1/testReview2.txt";
//        siw2.parseFile(inputFile2);
//
//        Dictionary td2 = new Dictionary(siw2.tokenDict, false);
//        Dictionary pd2 = new Dictionary(siw2.productDict, true);
//
//        SlowIndexWriter siw3 = new SlowIndexWriter();
//        String inputFile3 = "/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1/testReview3.txt";
//        siw3.parseFile(inputFile3);
//
//        Dictionary td3 = new Dictionary(siw3.tokenDict, false);
//        Dictionary pd3 = new Dictionary(siw3.productDict, true);
//
//        SlowIndexWriter siw4 = new SlowIndexWriter();
//        String inputFile4 = "/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1/testReview4.txt";
//        siw4.parseFile(inputFile4);
//
//        Dictionary td4 = new Dictionary(siw4.tokenDict, false);
//        Dictionary pd4 = new Dictionary(siw4.productDict, true);


//        System.out.println(siw1.tokenDict.equals(siw2.tokenDict));
//        System.out.println(siw1.tokenDict.equals(siw3.tokenDict));
//        System.out.println(siw1.tokenDict.equals(siw4.tokenDict));
//        System.out.println(siw1.productDict.equals(siw2.productDict));
//        System.out.println(siw1.productDict.equals(siw3.productDict));
//        System.out.println(siw1.productDict.equals(siw4.productDict));



//        String allwords = "";
//        try (BufferedReader reader = new BufferedReader(new FileReader(new File(inputFile1)))){
//            String line = reader.readLine();
//            while (line != null){
//                allwords = allwords.concat(line);
//                line = reader.readLine();
//            }
//        } catch (IOException e) {
//            System.err.println(e.getMessage());
//        }
//
//        String[] tokens = allwords.split("[^A-Za-z0-9]+");
//        for (String word: tokens) {
//            int ans = td1.searchTerm(word);
//
//            System.out.println(ans);
//        }
    }
}
