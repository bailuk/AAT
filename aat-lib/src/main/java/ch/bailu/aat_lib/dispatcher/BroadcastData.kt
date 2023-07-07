package ch.bailu.aat_lib.dispatcher;

import ch.bailu.aat_lib.util.Objects;

public class BroadcastData {

    public static boolean has(String[] args, String vid) {
        for (String arg : args) {
            if (Objects.equals(arg, vid)) {
                return true;
            }
        }
        return false;
    }

    public static String getFile(String[] args) {
        return get(args,0);
    }

    public static String getUrl(String[] args) {
        return get(args, 1);
    }

    public static String get(String[] args, int index) {
        if (args.length > index) return args[index];
        return "";
    }
}
