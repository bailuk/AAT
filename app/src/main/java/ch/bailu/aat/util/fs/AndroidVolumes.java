package ch.bailu.aat.util.fs;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.simpleio.foc.Foc;
import ch.bailu.simpleio.foc.FocFile;

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


    public void log() {
        for (Foc f : volumes) {
            AppLog.d(f, f.toString());
        }
        for (Foc f : caches) {
            AppLog.d(f, f.toString());
        }
        for (Foc f : files) {
            AppLog.d(f, f.toString());
        }

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



    private static Foc getParent(Foc file, int i) {
        while (i > 0) {
            i--;
            if (file != null) file = file.parent();
        }
        return file;
    }

    private static File getParent(File file, int i) {
        while (i > 0) {
            i--;
            if (file != null) file = file.getParentFile();
        }
        return file;
    }
/*
    public void askForPermission(Activity c, Foc f) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            askForPermissionSDK24(c, f);
        }
    }
*/

    private static String volumePathFromFile(Foc f) {
        for (Foc v : volumes) {
            final String sf = f.toString();
            final String sv = v.toString();

            if (sf.startsWith(sv)) {
                return sv;
            }
        }
        return null;
    }

/*
    @TargetApi(24)
    private void askForPermissionSDK24(Activity c, Foc f) {
        Object s = c.getSystemService(Context.STORAGE_SERVICE);

        if (s != null && s instanceof StorageManager) {
            StorageManager storage = (StorageManager) s;

            String vol = volumePathFromFile(f);
            if (vol != null) {
                List<StorageVolume> storageVolumes = storage.getStorageVolumes();

                for (StorageVolume v : storageVolumes) {
                    AppLog.d(v, v.getUuid());

                    final String id = v.getUuid();
                    if (id != null && vol.contains(id)) {
                        Intent intent = v.createAccessIntent(null);
                        if (intent != null) {
                            c.startActivityForResult(intent, DirectoryMenu.PERMISSION);
                            AppLog.d(this, intent.toString());
                        }
                        break;
                    }
                }
            }
        }
    }
*/
}
