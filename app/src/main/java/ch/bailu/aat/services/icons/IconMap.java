package ch.bailu.aat.services.icons;

import android.util.SparseArray;

public class IconMap {
    private final SparseArray<SparseArray<String>> key_list = new SparseArray<>(50);
    

    public void add(String key, String value, String file) {
        SparseArray<String>  value_list = key_list.get(key.hashCode());
        
        if (value_list == null) {
            value_list = new SparseArray<>(10);
        }
        
        value_list.put(value.hashCode(), file);
        key_list.put(key.hashCode(), value_list);
    }


    public String get(String key, String value) {
        final SparseArray<String> value_list=key_list.get(key.hashCode());

        if (value_list == null) {
            return null;
        }
        return value_list.get(value.hashCode());
    }
    
}
