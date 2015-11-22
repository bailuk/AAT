package ch.bailu.aat.preferences;

import ch.bailu.aat.helpers.CleanUp;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

public class IndexListPreference extends Preference implements OnPreferenceClickListener, OnSharedPreferenceChangeListener, CleanUp {
    private final SolidIndexList slist;


    public IndexListPreference(Context context, SolidIndexList l) {
        super(context);

        slist=l;
        setTitle(slist.getLabel());
        setSummary(slist.getString());

        setOnPreferenceClickListener(this);
        slist.getStorage().register(this);
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (slist.length()<3) {
            if (slist.getIndex()==0)
                slist.setIndex(1);
            else slist.setIndex(0);
        } else {
            new IndexListDialog(getContext(), slist);
        }
        return false;
    }

    
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        if (slist.hasKey(key)) {
            setSummary(slist.getString());
        }
    }

    
    @Override
    public void cleanUp() {
        slist.getStorage().unregister(this);
    }
}
