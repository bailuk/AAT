package ch.bailu.aat.activities;

import android.content.Intent;
import android.os.Bundle;

import javax.annotation.Nonnull;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.SensorSource;
import ch.bailu.aat.preferences.SolidSAF;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.mview.MultiView;
import ch.bailu.aat.views.preferences.GeneralPreferencesView;
import ch.bailu.aat.views.preferences.MapPreferencesView;
import ch.bailu.aat.views.preferences.PresetPreferencesView;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidPresetCount;

public class PreferencesActivity extends ActivityContext implements OnPreferencesChanged {

    public final static String SOLID_KEY=PreferencesActivity.class.getSimpleName();

    private final UiTheme theme = AppTheme.preferences;

    private MapPreferencesView mapTilePreferences;

    private MultiView multiView;
    private SolidPresetCount spresetCount;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spresetCount = new SolidPresetCount(new Storage(this));
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

        mapTilePreferences = new MapPreferencesView(this, getServiceContext(), theme);
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
        SolidSAF.onActivityResult(this, requestCode, resultCode, data);
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
    public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {
        if (spresetCount.hasKey(key)) {
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
