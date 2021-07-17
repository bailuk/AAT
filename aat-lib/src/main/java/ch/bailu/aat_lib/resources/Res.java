package ch.bailu.aat_lib.resources;

public class Res {
    private static  final Strings STR = new Strings();
    private Res() {}

    public static Strings str() {
        return STR;
    }

    public static int getIconResource(String s) {
        return 0;
    }
}
