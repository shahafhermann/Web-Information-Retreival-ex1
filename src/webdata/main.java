package webdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class main {
    public static void main(String[] args) {
        byte[] test = Encoder.intToByte(3000);
        System.out.println(test);

        byte controlByte = (byte)((4 << 4) + 1);
        System.out.println(controlByte);

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
