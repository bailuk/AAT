package ch.bailu.aat_awt.preferences;

import java.io.File;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class AppPreferences implements StorageInterface {


    private final static Preferences NODE = Preferences.userRoot().node("ch/bailu/aat");

    private final static ArrayList<OnPreferencesChanged> OBSERVERS = new ArrayList<>(50);

    @Override
    public void backup() {}

    @Override
    public File getSharedPrefsDirectory() {
        return new File(NODE.absolutePath());
    }

    @Override
    public void restore() {}

    @Override
    public String readString(String key) {
        return NODE.get(key,"").toString();
    }

    @Override
    public void writeString(String key, String value) {
        NODE.put(key, value);
        propagate(key);
    }

    @Override
    public int readInteger(String key) {
        return NODE.getInt(key, 0);
    }

    @Override
    public void writeInteger(String key, int v) {
        NODE.putInt(key, v);
        propagate(key);
    }

    @Override
    public void writeIntegerForce(String key, int v) {
        writeInteger(key, v);
        propagate(key);
    }

    @Override
    public long readLong(String key) {
        return NODE.getLong(key, 0);
    }

    @Override
    public void writeLong(String key, long v) {
        NODE.putLong(key, v);
        propagate(key);
    }

    @Override
    public void register(OnPreferencesChanged listener) {
        if (!OBSERVERS.contains(listener)) {
            OBSERVERS.add(listener);
        }
    }

    @Override
    public void unregister(OnPreferencesChanged l) {
        OBSERVERS.remove(l);
    }

    @Override
    public boolean isDefaultString(String s) {
        return "".equals(s);
    }

    @Override
    public String getDefaultString() {
        return "";
    }

    private void propagate(String key) {
        try {
            NODE.sync();
            for(OnPreferencesChanged l : OBSERVERS) {
                l.onPreferencesChanged(this, key);
            }

        } catch (BackingStoreException e) {
            AppLog.e(this, e);
        }
    }


    public static void save() {
        try {
            NODE.sync();
            NODE.flush();
        } catch (BackingStoreException e) {
            AppLog.e(e);
        }
    }
}
