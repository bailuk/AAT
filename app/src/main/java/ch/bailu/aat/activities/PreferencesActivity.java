package ch.bailu.aat.activities;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import ch.bailu.aat.R;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.preferences.IndexListPreference;
import ch.bailu.aat.preferences.IntegerPreference;
import ch.bailu.aat.preferences.SolidAccelerationFilter;
import ch.bailu.aat.preferences.SolidAccuracyFilter;
import ch.bailu.aat.preferences.SolidAutopause;
import ch.bailu.aat.preferences.SolidDataDirectory;
import ch.bailu.aat.preferences.SolidDistanceFilter;
import ch.bailu.aat.preferences.SolidLocationProvider;
import ch.bailu.aat.preferences.SolidMET;
import ch.bailu.aat.preferences.SolidMissingTrigger;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.preferences.SolidTileCacheDirectory;
import ch.bailu.aat.preferences.SolidTileSize;
import ch.bailu.aat.preferences.SolidUnit;
import ch.bailu.aat.preferences.SolidWeight;

public class PreferencesActivity extends PreferenceActivity {
    
    private PreferenceScreen screen;
    private PreferenceCategory category;
    private final ArrayList<Closeable> toClose= new ArrayList<>();

    private AppLog logger;
    
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        addPreferencesFromResource(R.xml.preferences);
        screen = this.getPreferenceScreen();

        addCategory(getString(R.string.p_general));
        
        addPreference(new IndexListPreference(this, new SolidUnit(this)));
        addPreference(new IntegerPreference(this, new SolidWeight(this)));

        final SolidPreset spreset= new SolidPreset(this);
        for (int i=0; i<spreset.length(); i++) {
            addCategory(getString(R.string.p_preset) + " " + (i+1));
            addPreference(new IndexListPreference(this, new SolidMET(this,i)));
            addPreference(new IndexListPreference(this, new SolidAutopause(this,i)));
            addPreference(new IndexListPreference(this, new SolidDistanceFilter(this,i)));
            addPreference(new IndexListPreference(this, new SolidAccelerationFilter(this,i)));
            addPreference(new IndexListPreference(this, new SolidAccuracyFilter(this,i)));
            addPreference(new IndexListPreference(this, new SolidMissingTrigger(this,i)));
        }

        addCategory(getString(R.string.p_system));
        addPreference(new IndexListPreference(this, new SolidLocationProvider(this)));
        addPreference(new IndexListPreference(this, new SolidTileSize(this)));
        addPreference(new IndexListPreference(this, new SolidTileCacheDirectory(this)));
        addPreference(new IndexListPreference(this, new SolidDataDirectory(this)));
    }

    @Override
    public void onDestroy() {
        while (!toClose.isEmpty())
            try {
                toClose.remove(0).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        
        super.onDestroy();
    }
    
    
    @Override
    public void onResume() {
        super.onResume();
        logger=new AppLog(this);
    }

    
    @Override
    public void onPause() {
        logger.close();
        logger=null;
        super.onPause();
    }

    
    private void addPreference(IndexListPreference pref) {
        toClose.add(pref);
        category.addPreference(pref);
    }
    
    
    private void addPreference(IntegerPreference pref) {
        toClose.add(pref);
        category.addPreference(pref);
    }
    
    private void addCategory(String title) {
        category = new PreferenceCategory(this);
        category.setTitle(title);
        screen.addPreference(category);

    }
}
