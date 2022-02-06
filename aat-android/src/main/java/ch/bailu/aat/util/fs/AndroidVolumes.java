package ch.bailu.aat.util.fs;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import ch.bailu.foc.Foc;
import ch.bailu.foc.FocFile;

public class AndroidVolumes {
    private static Foc[] volumes, files, caches;


    public AndroidVolumes(Context context) {
        if (files == null)
            init(context);
    }

    public Foc[] getVolumes() {
        return volumes;
    }

    public Foc[] getCaches() {
        return caches;
    }

    public Foc[] getFiles() {
        return files;
    }

    public static void init(Context context) {
        File internal_cache = context.getCacheDir();
        File internal_file = context.getFilesDir();
        File external_volume = Environment.getExternalStorageDirectory();

        File[] external_files, external_caches;

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            external_files = context.getExternalFilesDirs(null);
            external_caches = context.getExternalCacheDirs();
        } else {
            external_files = new File[1];
            external_caches = new File[1];
            external_files[0] = context.getExternalFilesDir(null);
            external_caches[0] = context.getExternalCacheDir();
        }

        files = getMounted(internal_file, external_files);
        caches = getMounted(internal_cache, external_caches);

        volumes=volumesFromFiles(external_volume, external_files);
    }


    private static int countMounted(File file, File[] files) {
        int c=0;

        if (file != null) c++;
        for(File f: files) {
            if (f != null) c++;
        }
        return c;
    }


    private static Foc[] getMounted(File file, File[] files) {
        Foc[] mounted = new Foc[countMounted(file, files)];

        int i=0;

        if (file != null) {
            mounted[i]=toFoc(file);
            i++;
        }

        for (File f : files) {
            if (f!= null) {
                mounted[i]=toFoc(f);
                i++;
            }
        }
        return mounted;
    }

    private static Foc toFoc(File file) {
        return new FocFile(file);
    }

    private static Foc[] volumesFromFiles(File externalVolume, File[] files) {
        File[] volumes = new File[files.length];

        for (int i=0; i<volumes.length; i++) {
            volumes[i] = getParent(files[i], 4);

            if (volumes[i] != null && volumes[i].equals(externalVolume)) {
                externalVolume = null;
            }
        }


        return getMounted(externalVolume, volumes);
    }

    private static File getParent(File file, int i) {
        while (i > 0) {
            i--;
            if (file != null) file = file.getParentFile();
        }
        return file;
    }

}
