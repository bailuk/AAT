package ch.bailu.simpleio.util;

public class Objects {
    public static boolean equals(Object a, Object b) {
        if (a != null && b!= null) {
            return a.equals(b);
        }
        return a == b;
    }
}
