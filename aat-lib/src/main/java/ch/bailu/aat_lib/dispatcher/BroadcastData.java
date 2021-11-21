package ch.bailu.aat_lib.dispatcher;

import ch.bailu.aat_lib.util.Objects;

public class BroadcastData {
    public static boolean hasFile(Object[] objs, String vid) {
        return objs.length > 0 && Objects.equals(objs[0], vid);
    }

    public static String getFile(Object[] objs) {
        return getString(0);
    }

    public static String getUrl(Object[] objs) {
        return getString(1, objs);
    }

    public static String getString(int index, Object ...objs) {
        if (objs.length > index && objs[index] instanceof String) return (String) objs[index];
        return "";
    }
}
