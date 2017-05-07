package ch.bailu.aat.util.fs;

import java.io.File;
import java.text.DecimalFormat;

public class JFile {
    public final static long KB=1024;
    public final static long MB=1024*KB;
    public final static long GB=1024*MB;

    public final static DecimalFormat dec = new DecimalFormat("0.00");

    public final static long[] ldivider = {
            1, KB, MB, GB
    };

    public final static double[] ddivider = {
            1, KB, MB, GB
    };

    public final static String[] unit = {
            "B", "K", "M", "G",
    };


    public static StringBuilder reportFileSize(StringBuilder out, double size) {


        int i = ddivider.length;

        while (i>0) {
            i--;
            if (Math.abs(size) >= ddivider[i])
                break;
        }


        out.append(dec.format(size / ddivider[i]));
        out.append(unit[i]);
        return out;
    }



    public static StringBuilder reportFileSize(StringBuilder out, long size) {
        int i = ldivider.length;

        while (i>0) {
            i--;
            if (Math.abs(size) >= ldivider[i])
                break;
        }

        out.append(size/ ldivider[i]);
        out.append(unit[i]);
        return out;
    }



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


    public static boolean delete(File f) {
        try {
            return f.delete();
        } catch (SecurityException e){
            return false;
        }
    }

    public static void deleteEmptiyDirectoriesRecursive(File dir) {
        if (dir.isDirectory()) {
            for (File child : dir.listFiles())
                deleteEmptiyDirectoriesRecursive(child);

            delete(dir);
        }
    }

    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);


        delete(fileOrDirectory);
    }


}
