package ch.bailu.aat.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.SensorSource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.aat.preferences.general.SolidPresetCount;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ErrorView;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.mview.MultiView;
import ch.bailu.aat.views.preferences.GeneralPreferencesView;
import ch.bailu.aat.views.preferences.MapTilePreferencesView;
import ch.bailu.aat.views.preferences.PresetPreferencesView;

public class PreferencesActivity extends ActivityContext implements SharedPreferences.OnSharedPreferenceChangeListener {

    public final static String SOLID_KEY=PreferencesActivity.class.getSimpleName();

    private final UiTheme theme = AppTheme.preferences;

    private MapTilePreferencesView mapTilePreferences;

    private MultiView multiView;
    private SolidPresetCount spresetCount;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spresetCount = new SolidPresetCount(this);
        spresetCount.register(this);
        createViews();

        addSource(new SensorSource(getServiceContext(), InfoID.SENSORS));

    }



    private void createViews() {
        ContentView contentView = new ContentView(this, theme);

        multiView = createMultiView(theme);
        contentView.addMvIndicator(multiView);
        contentView.add(new MainControlBar(this, multiView));

        contentView.add(getErrorView());
        contentView.add(multiView);

        setContentView(contentView);
    }



    private MultiView createMultiView(UiTheme theme) {
        multiView = new MultiView(this, SOLID_KEY);

        mapTilePreferences = new MapTilePreferencesView(this, getServiceContext(), theme);
        multiView.add(new GeneralPreferencesView(this, theme),
                getString(R.string.p_general)+ "/"+ getString(R.string.p_system));
        multiView.add(mapTilePreferences,
                getString(R.string.p_tiles));

        addPresetPreferences(theme);

        return multiView;
    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        SolidFile.onActivityResult(this, requestCode, resultCode, data);
    }


    @Override
    public void onResumeWithService() {
        super.onResumeWithService();
        mapTilePreferences.updateText();
    }

    @Override
    public void onDestroy() {
        spresetCount.unregister(this);
        super.onDestroy();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (spresetCount.hasKey(s)) {
            addPresetPreferences(theme);
        }
    }

    private void addPresetPreferences(UiTheme theme) {
        while (multiView.pageCount() > 2)
            multiView.remove(multiView.pageCount()-1);

        for (int i = 0; i < spresetCount.getValue(); i++) {
                multiView.add(new PresetPreferencesView(this, i, theme),
                        getString(R.string.p_preset) + " " + (i+1));
        }

    }
}
