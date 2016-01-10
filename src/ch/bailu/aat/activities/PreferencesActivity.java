package ch.bailu.aat.activities;

import java.util.ArrayList;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.CleanUp;
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
import ch.bailu.aat.R;

public class PreferencesActivity extends PreferenceActivity {
    
    private PreferenceScreen screen;
    private PreferenceCategory category;
    private SolidPreset spreset;
    private ArrayList<CleanUp> cleanUp=new ArrayList<CleanUp>();

    private AppLog logger;
    
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        addPreferencesFromResource(R.xml.preferences);
        screen = this.getPreferenceScreen();

        addCategory(getString(R.string.p_general));
        
        addPreference(new IndexListPreference(this, new SolidUnit(this)));
        addPreference(new IntegerPreference(this, new SolidWeight(this)));
        
        spreset = new SolidPreset(this);
        for (int i=0; i<spreset.length(); i++) {
            addCategory(getString(R.string.p_preset) + " " + (i+1));
            addPreference(new IndexListPreference(this, new SolidMET(this,i)));
            addPreference(new IndexListPreference(this, new SolidAutopause(this,i)));
            addPreference(new IndexListPreference(this, new SolidDistanceFilter(this,i)));
            addPreference(new IndexListPreference(this, new SolidAccelerationFilter(this,i)));
            addPreference(new IndexListPreference(this, new SolidAccuracyFilter(this,i)));
            addPreference(new IndexListPreference(this, new SolidMissingTrigger(this,i)));
        }

        addCategory("System*");
        addPreference(new IndexListPreference(this, new SolidLocationProvider(this)));
        addPreference(new IndexListPreference(this, new SolidTileSize(this)));
        addPreference(new IndexListPreference(this, new SolidTileCacheDirectory(this)));
        addPreference(new IndexListPreference(this, new SolidDataDirectory(this)));
        //addPreference(new IndexListPreference(this, new SolidMapTileShade(this)));

        
    }
    

    @Override
    public void onDestroy() {
        while (!cleanUp.isEmpty()) 
            cleanUp.remove(0).cleanUp();
        
        super.onDestroy();
    }
    
    
    @Override
    public void onResume() {
        super.onResume();
        logger=new AppLog(this);
    }

    
    @Override
    public void onPause() {
        logger.cleanUp();
        logger=null;
        super.onPause();
    }

    
    private void addPreference(IndexListPreference pref) {
        cleanUp.add(pref);
        category.addPreference(pref);
    }
    
    
    private void addPreference(IntegerPreference pref) {
        cleanUp.add(pref);
        category.addPreference(pref);
    }
    
    private void addCategory(String title) {
        category = new PreferenceCategory(this);
        category.setTitle(title);
        screen.addPreference(category);

    }
}
