package ch.bailu.aat.preferences;

import java.io.Closeable;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

public class IndexListPreference extends Preference implements OnPreferenceClickListener, OnSharedPreferenceChangeListener, Closeable {
    private final SolidIndexList slist;


    public IndexListPreference(Context context, SolidIndexList l) {
        super(context);

        slist=l;
        setTitle(slist.getLabel());
        setSummary(slist.getValueAsString());

        setOnPreferenceClickListener(this);
        slist.getStorage().register(this);
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (slist.length()<3) {
            slist.cycle();
        } else {
            new IndexListDialog(getContext(), slist);
        }
        return false;
    }

    
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        if (slist.hasKey(key)) {
            setSummary(slist.getValueAsString());
        }
    }

    
    @Override
    public void close() {
        slist.getStorage().unregister(this);
    }
}
