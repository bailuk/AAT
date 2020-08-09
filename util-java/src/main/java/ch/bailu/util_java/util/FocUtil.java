package ch.bailu.util_java.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import ch.bailu.foc.Foc;


public class FocUtil {

    public static String toStr(Foc file) {
        try {
            return toString(file);
        } catch (IOException e) {
            return "";
        }
    }


    public static String toString(Foc file) throws IOException {

        StringBuilder builder = new StringBuilder((int) file.length()+1);
        InputStream in = null;

        try {
            in = new BufferedInputStream(file.openR());

            int b;

            while ((b = in.read()) > -1) {
                char c = (char) b;
                builder.append(c);
            }

        } finally {
            Foc.close(in);
        }

        return builder.toString();
    }
}
