package ch.bailu.aat.services.icons;

import java.util.HashMap;

public class IconMap {
    private final static String SVG_PREFIX="symbols/";
    private final static String SVG_SUFFIX=".svg";

   public class Icon {
        public final String svg;

        public Icon(String file_name) {
            svg = SVG_PREFIX + file_name + SVG_SUFFIX;
        }
    }



    private final HashMap<Integer, HashMap<String, Icon>> key_list = new HashMap();
    //private final SparseArray<SparseArray<Icon>> key_list = new SparseArray<>(50);


    public void add(int key, String value, String file_name) {
        HashMap<String,Icon>  value_list = key_list.get(key);

        if (value_list == null) {
            value_list = new HashMap<>();
        }

        value_list.put(value, new Icon(file_name));
        key_list.put(key, value_list);
    }


    public Icon get(int keyIndex, String value) {
        final HashMap<String, Icon> value_list=key_list.get(keyIndex);

        if (value_list == null) {
            return null;
        }
        return value_list.get(value);
    }
}
