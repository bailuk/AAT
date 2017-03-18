package ch.bailu.aat.util.fs;

import java.io.File;

public class JFile {

    public static boolean canOnlyRead(File f) {
        return canWrite(f) == false && canRead(f) == true;
    }

    public static boolean canWrite(File f) {
        try {
            return f != null && f.canWrite();
        } catch (SecurityException e) {
            return false;
        }
    }

    public static boolean canRead(File f) {
        try {
            return f != null && f.canRead();
        } catch (SecurityException e) {
            return false;
        }
    }

}
