package ch.bailu.aat.preferences;

import java.io.File;
import java.util.Map.Entry;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Environment;
import ch.bailu.aat.helpers.AppFile;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.ContextWrapperInterface;

public class Storage  implements ContextWrapperInterface {
    private final static String DEF_VALUE="0";

    private final static String GLOBAL_NAME="Preferences";

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    private final Context context;


    private Storage(Context c, String fileName) {
        context=c;
        preferences = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        editor = preferences.edit();
    }


    public static Storage global(Context c) {
        return new Storage(c, GLOBAL_NAME);
    }


    public static Storage map(Context context) {
        return global(context);  // TODO remove
    }




    public static Storage activity(Context c) {
        return global(c); // TODO remove
    }


    public static Storage preset(Context c) {
        return global(c); // TODO remove
    }


    public void backup() throws Exception {
        final File source =new File(getSharedPrefsDirectory(context),  GLOBAL_NAME + ".xml");
        final File target = new File(Environment.getExternalStorageDirectory(), "aat_preferences.xml");

        AppFile.copy(source, target);
    }


    public static File getSharedPrefsDirectory(Context context) {
        final File data = context.getFilesDir();
        AppLog.d(data, data.toString());
        
        return new File(data.getParent(), "shared_prefs");
    }


    public void restore() throws Exception {
        final File target = new File(getSharedPrefsDirectory(context) +  "restore.xml");
        final File source = new File(Environment.getExternalStorageDirectory(), "aat_preferences.xml");

        if (target.exists()) target.delete();

        AppFile.copy(source, target);

        final SharedPreferences restore = context.getSharedPreferences("restore", Context.MODE_PRIVATE);

        for(Entry<String,?> entry : restore.getAll().entrySet()){ 
            Object v = entry.getValue(); 
            String key = entry.getKey();

            if(v instanceof Boolean) 
                editor.putBoolean(key, ((Boolean)v).booleanValue());
            else if(v instanceof Float)
                editor.putFloat(key, ((Float)v).floatValue());
            else if(v instanceof Integer)
                editor.putInt(key, ((Integer)v).intValue());
            else if(v instanceof Long)
                editor.putLong(key, ((Long)v).longValue());
            else if(v instanceof String)
                editor.putString(key, ((String)v));         
        }
        editor.commit();

        if (target.exists()) target.delete();
    }


    public String readString(String key) {
        return preferences.getString(key, DEF_VALUE);
    }

    public void writeString(String key, String value) {
        if (!readString(key).equals(value)) {
            editor.putString(key, value);
            editor.commit();
        }
    }

    public int readInteger(String key) {
        return preferences.getInt(key, 0);
    }

    public void writeBoolean(String key, boolean v) {
        if (readBoolean(key) != v) {
            editor.putBoolean(key, v);
            editor.commit();
        }        
    }

    public void writeInteger(String key, int v) {
        if (readInteger(key) != v) {
            editor.putInt(key, v);
            editor.commit();
        }
    }

    public boolean readBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public long readLong(String key) {
        return preferences.getLong(key, 0);
    }

    public void writeLong(String key, long v) {
        if (readLong(key) != v) {
            editor.putLong(key, v);
            editor.commit();
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
