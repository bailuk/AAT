package ch.bailu.aat.util.filter_list;


import java.util.ArrayList;

public class KeyList {
    public static final int MIN_KEY_LENGTH = 3;

    private final ArrayList<String> keys = new ArrayList<>(10);

    public KeyList(String s) {
        addKeys(s);
    }

    public KeyList() {}


    public void addKeys(String s) {
        addKeys(s.split(" |=|\\.|;|:|/"));
    }


    private void addKeys(String[] s) {
        for (String k : s) {
            addKey(k);
        }
    }


    private void addKey(String k) {
        if (k.length() >= MIN_KEY_LENGTH) {
            k = k.toLowerCase();
            if (hasKey(k) == false) {
                keys.add(k);
            }
        }
    }

    public boolean hasKey(String k) {
        return hasKey(k, keys.size());
    }


    private boolean hasKey(String k, int limit) {
        limit = Math.min(keys.size(), limit);

        for (int i=0; i < limit ; i++) {
            if (keys.get(i).equals(k)) return true;
        }

        return false;
    }



    public boolean fits(KeyList list) {
        for (String k : list.keys)
            if (fits(k) == false) return false;

        return true;
    }

    private boolean fits(String k) {
        for (String x : keys) {
            if (x.contains(k)) return true;
        }
        return false;
    }

    public void addKeys(KeyList list) {
        final int limit = keys.size();

        for (String k : list.keys) {
            addKey(k, limit);
        }
    }

    private void addKey(String k, int limit) {
        if (hasKey(k, limit) == false)
            keys.add(k);
    }

    public boolean isEmpty() {
        return keys.isEmpty();
    }

    public int length() {
        int l = 0;
        for (String k : keys) {
            l+=k.length();
        }
        return l;
    }
}