package ch.bailu.aat.preferences;

import java.io.Closeable;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;

public class IntegerPreference extends EditTextPreference 
implements OnPreferenceChangeListener, OnSharedPreferenceChangeListener, Closeable{
    private final SolidInteger sinteger;
    private final Storage storage;
    
    public IntegerPreference(Context context, SolidInteger i) {
        super(context);
    
        sinteger=i;
        setTitle(sinteger.getLabel());
        setSummary(String.valueOf(sinteger.getValue()));
        setDefaultValue(String.valueOf(sinteger.getValue()));
    
        setOnPreferenceChangeListener(this);
        
        storage=sinteger.getStorage();
        storage.register(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        if (sinteger.hasKey(key)) {
            setSummary(String.valueOf(sinteger.getValue()));
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        sinteger.setValue(Integer.valueOf(newValue.toString()));
        return false;
    }

    @Override
    public void close() {
        storage.unregister(this);
        
    }

}
