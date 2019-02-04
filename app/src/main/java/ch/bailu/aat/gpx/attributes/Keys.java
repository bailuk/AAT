package ch.bailu.aat.gpx.attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Keys {



    private static HashMap<String, Integer> indexes = new HashMap();
    private static ArrayList<String> strings = new ArrayList(100);


    private ArrayList<Integer> keys = new ArrayList<>(10);

    public int add(String string) {
        int keyIndex = toIndex(string);

        keys.add(keyIndex);
        return keyIndex;
    }

    public boolean hasKey(int keyIndex) {
        for (int k : keys) {
            if (k == keyIndex) return true;
        }
        return false;
    }


    public static String toString(int keyIndex) {
        return strings.get(keyIndex);
    }


    public static int toIndex(String string) {
        String keyString = string.toLowerCase(Locale.ROOT);

        Integer keyIndex = indexes.get(keyString);

        if (keyIndex == null) {
            keyIndex = add(keyString, string);
        }

        return keyIndex;
    }


    private static int add(String keyString, String string) {
        int keyIndex = strings.size();

        strings.add(string);
        indexes.put(keyString, keyIndex);

        return keyIndex;
    }

    public int size() {
        return keys.size();
    }

    public int getKeyIndex(int i) {
        return keys.get(i);
    }
}
