package webdata;

public class main {
    public static void main(String[] args) {
        SlowIndexWriter siw = new SlowIndexWriter();
        siw.parseFile("/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1/testReview.txt");

        Dictionary td = new Dictionary(siw.tokenDict, false);
        Dictionary pd = new Dictionary(siw.productDict, true);

        System.out.println(td.rangeSearch(0, "vitality"));
    }
}
