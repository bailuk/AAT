package ch.bailu.aat.util.fs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;

import java.io.File;
import java.util.List;

import ch.bailu.aat.util.ui.AppLog;

public class AndroidVolumes {
    private static File[] volumes, files, caches;


    public AndroidVolumes(Context context) {
        if (files == null)
            init(context);
    }

    public File[] getVolumes() {
        return volumes;
    }

    public File[] getCaches() {
        return caches;
    }

    public File[] getFiles() {
        return files;
    }


    public void log() {
        for (File f : volumes) {
            AppLog.d(f, f.toString());
        }
        for (File f : caches) {
            AppLog.d(f, f.toString());
        }
        for (File f : files) {
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


    private static File[] getMounted(File file, File[] files) {
        File[] mounted = new File[countMounted(file, files)];

        int i=0;

        if (file != null) {
            mounted[i]=file;
            i++;
        }

        for (File f : files) {
            if (f!= null) {
                mounted[i]=f;
                i++;
            }
        }
        return mounted;
    }


    private static File[] volumesFromFiles(File externalVolume, File[] files) {
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



    public void askForPermission(Activity c, File f) {
        AFile.logInfoAcess(c,f);

        if (Build.VERSION.SDK_INT >= 24) {
            askForPermissionSDK24(c, f);
        }
    }


    private static String volumePathFromFile(File f) {
        for (File v : volumes) {
            final String sf = f.getAbsolutePath();
            final String sv = v.getAbsolutePath();

            if (sf.startsWith(sv)) {
                return sv;
            }
        }
        return null;
    }


    @TargetApi(24)
    private void askForPermissionSDK24(Activity c, File f) {
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
                        if (intent != null) c.startActivityForResult(intent, 5);
                        break;
                    }
                }
            }
        }
    }
}
