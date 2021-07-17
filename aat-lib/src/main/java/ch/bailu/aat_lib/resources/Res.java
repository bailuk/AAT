package ch.bailu.aat_lib.resources;

import java.util.Locale;

public class Res {
    private static  Strings STR = null;
    private Res() {}

    public static Strings str() {
        if (STR == null) {
            STR = setStrings(Locale.getDefault().getLanguage());
        }
        return STR;
    }

    private static Strings setStrings(String lang) {

        if (is(lang, "de")) {
            return new Strings_de();
        }
        if (is(lang, "fr")) {
            return new Strings_fr();
        }
        if (is(lang, "cs")) {
            return new Strings_de();
        }
        if (is(lang, "nl")) {
            return new Strings_nl();
        }

        return new Strings();
    }


    private static boolean is(String lang, String code) {
        return lang.equals(new Locale(code).getLanguage());
    }

    public static int getIconResource(String s) {
        return 0;
    }
}
