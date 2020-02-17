package ch.bailu.aat.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Environment;

import java.io.File;
import java.util.Map.Entry;

import ch.bailu.aat.util.ContextWrapperInterface;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.foc.FocFile;

public class Storage  implements ContextWrapperInterface {
    public final static String DEF_VALUE="0";

    private final static String GLOBAL_NAME="Preferences";

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    private final Context context;


    public Storage(Context c) {
        context=c;
        preferences = context.getSharedPreferences(Storage.GLOBAL_NAME,Context.MODE_PRIVATE);
        editor = preferences.edit();
    }


    private FocFile getBackupFile() {
        return new FocFile(new File(Environment.getExternalStorageDirectory(),
                "aat_preferences.xml"));
    }

    public void backup() {
        final Foc source = new FocFile(new File(getSharedPrefsDirectory(), GLOBAL_NAME + ".xml"));
        final Foc target = getBackupFile();

        source.cp(target);
    }


    public File getSharedPrefsDirectory() {
        final File data = context.getFilesDir();

        return new File(data.getParent(), "shared_prefs");
    }


    public void restore() {
        final Foc target = new FocFile(new File(getSharedPrefsDirectory() +  "/restore.xml"));
        final Foc source = getBackupFile();

        target.rm();
        source.cp(target);



        final SharedPreferences restore = context.getSharedPreferences("restore", Context.MODE_PRIVATE);

        for(Entry<String,?> entry : restore.getAll().entrySet()){
            Object v = entry.getValue();
            String key = entry.getKey();

            if(v instanceof Boolean)
                editor.putBoolean(key, (Boolean) v);
            else if(v instanceof Float)
                editor.putFloat(key, (Float) v);
            else if(v instanceof Integer)
                editor.putInt(key, (Integer) v);
            else if(v instanceof Long)
                editor.putLong(key, (Long) v);
            else if(v instanceof String)
                editor.putString(key, ((String)v));
        }
        editor.commit();

        target.rm();
    }


    public String readString(String key) {
        return preferences.getString(key, DEF_VALUE);
    }

    public void writeString(String key, String value) {
        if (!readString(key).equals(value)) {
            editor.putString(key, value);
            editor.apply();
        }
    }

    public int readInteger(String key) {
        try {
            return preferences.getInt(key, 0);

        } catch (ClassCastException e) {
            return 0;
        }
    }


    public void writeInteger(String key, int v) {
        if (readInteger(key) != v) {
            editor.putInt(key, v);
            editor.apply();
        }
    }



    public void writeIntegerForce(String key, int v) {
        editor.remove(key);
        editor.apply();
        editor.putInt(key, v);
        editor.apply();
    }

    public long readLong(String key) {
        return preferences.getLong(key, 0);
    }

    public void writeLong(String key, long v) {
        if (readLong(key) != v) {
            editor.putLong(key, v);
            editor.apply();
        }
    }

    public void register(OnSharedPreferenceChangeListener listener)  {
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }


    public void unregister(OnSharedPreferenceChangeListener l) {
        preferences.unregisterOnSharedPreferenceChangeListener(l);
    }


    @Override
    public Context getContext() {
        return context;
    }
}
