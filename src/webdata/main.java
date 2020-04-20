package webdata;

import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class main {
    public static void main(String[] args) throws IOException {
        int[] encodeTestKeys = {1, 50, 500, 5000, 50000, 90000, 100000, 1000000, 10000000, 100000000, 2000000000};
        int[] encodeTestValues = {5, 1000, 50189};

        RandomAccessFile encodeOutput = new RandomAccessFile(
                "/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1/encodeOutput.txt", "rw");
        ArrayList<Byte> a = Encoder.encode(encodeTestKeys, true);
        byte[] b = new byte[a.size()];
        for (int i = 0; i < a.size(); ++i) {
            b[i] = a.get(i);
        }
        int[] c = Encoder.decode(b, true);
        System.out.println(c);

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
