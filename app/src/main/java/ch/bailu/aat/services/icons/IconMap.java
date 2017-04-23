package ch.bailu.aat.services.icons;

import android.util.SparseArray;

public class IconMap {
    private final static String SVG_PREFIX="symbols/";
    private final static String SVG_SUFFIX=".svg";

   public class Icon {
        public final String svg;

        public Icon(String file_name) {
            svg = SVG_PREFIX + file_name + SVG_SUFFIX;
        }
    }



    private final SparseArray<SparseArray<Icon>> key_list = new SparseArray<>(50);


    public void add(String key, String value, String file_name) {
        SparseArray<Icon>  value_list = key_list.get(key.hashCode());
        
        if (value_list == null) {
            value_list = new SparseArray<>(10);
        }
        
        value_list.put(value.hashCode(), new Icon(file_name));
        key_list.put(key.hashCode(), value_list);
    }


    public Icon get(int keyHash, int valueHash) {
        final SparseArray<Icon> value_list=key_list.get(keyHash);

        if (value_list == null) {
            return null;
        }
        return value_list.get(valueHash);
    }


    public Icon get(String key, String value) {
        return get(key.hashCode(), value.hashCode());
    }


}
