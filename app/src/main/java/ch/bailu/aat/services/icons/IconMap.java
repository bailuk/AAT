package ch.bailu.aat.services.icons;

import java.util.HashMap;

import ch.bailu.aat.util.WithStatusText;

public final class IconMap implements WithStatusText {


    public class Icon {
        public final String svg;

        public Icon(String file_name) {
            svg = IconMapService.SVG_DIRECTORY + file_name + IconMapService.SVG_SUFFIX;
        }
    }


    private final HashMap<Integer, HashMap<String, Icon>> key_list =
            new HashMap<Integer, HashMap<String, Icon>>();


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


    @Override
    public void appendStatusText(StringBuilder builder) {
        builder.append("IconMap (key_list) size: ").append(key_list.size()).append("<br>");
    }

}
