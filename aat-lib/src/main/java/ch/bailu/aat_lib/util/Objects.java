package ch.bailu.aat_lib.util;

public class Objects {
    public static boolean equals(Object a, Object b) {
        if (a != null && b!= null) {
            return a.equals(b);
        }
        return a == b;
    }


    public static String toString(Object o) {
        if (o != null) {
            final String r = o.toString();

            if (r != null) return r;
        }
        return "";
    }


    public static long toLong(Object o) {
        try {
            return Long.parseLong(toString(o));
        } catch (NumberFormatException e) {
            return 0L;
        }
    }


    public static int toInt(Object o) {
        try {
            return Integer.parseInt(toString(o));
        } catch (NumberFormatException e) {
            return 0;
        }

    }

    public static float toFloat(Object o) {
        try {
            return Float.parseFloat(toString(o));
        } catch (NumberFormatException e) {
            return 0f;
        }

    }

    public static boolean toBoolean(Object o) {
        String s = toString(o);
        char c = 'f';
        if (s.length() > 0)
            c = s.charAt(0);

        return c == 't' || c == 'T';
    }

}
