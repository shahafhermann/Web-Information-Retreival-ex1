package webdata;

import java.util.TreeMap;

public class Index {
    private Dictionary dictionary;
    private String dir;

    public Index(TreeMap<String, TreeMap<Integer, Integer>> termDict, String dir, boolean flag) {
        dictionary = new Dictionary(termDict, flag);
        this.dir = dir;
//        writePostingList();
    }
}
