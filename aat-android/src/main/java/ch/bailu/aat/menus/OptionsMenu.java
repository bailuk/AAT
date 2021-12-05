package ch.bailu.aat.menus;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.activities.PreferencesActivity;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.map.AndroidSolidMapsForgeDirectory;
import ch.bailu.aat.preferences.map.SolidMapTileStack;
import ch.bailu.aat.preferences.map.SolidRenderTheme;
import ch.bailu.aat.preferences.presets.SolidBacklight;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.views.preferences.SolidCheckListDialog;
import ch.bailu.aat.views.preferences.SolidIndexListDialog;
import ch.bailu.aat_lib.preferences.presets.SolidPreset;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat_lib.service.tracker.StateInterface;
import ch.bailu.foc_android.FocAndroidFactory;

public final class OptionsMenu extends AbsMenu {
    private MenuItem start, pause, backlight, preferences, map;

    private final ServiceContext scontext;


    public OptionsMenu(ServiceContext sc) {
        scontext = sc;
    }


    @Override
    public void inflate(Menu menu) {
        start = menu.add(R.string.tracker_start);
        start.setIcon(R.drawable.media_playback_start_inverse);

        pause = menu.add(R.string.tracker_pause);
        pause.setIcon(R.drawable.media_playback_pause_inverse);


        backlight = menu.add(R.string.p_backlight_title);

        preferences = menu.add(R.string.intro_settings);

        map = menu.add(R.string.p_map);
    }

    @Override
    public String getTitle() {
        return scontext.getContext().getString(R.string.app_sname);
    }

    @Override
    public Drawable getIcon() {
        return null;
    }


    @Override
    public void prepare(Menu menu) {
        new InsideContext(scontext) {
            @Override
            public void run() {
                updateMenuText(scontext.getTrackerService());
            }
        };
    }


    private void updateMenuText(StateInterface state) {
        start.setTitle(state.getStartStopText());
        start.setIcon(state.getStartStopIconID());
        pause.setTitle(state.getPauseResumeText());
    }


    @Override
    public boolean onItemClick(MenuItem item) {
        final Context c = scontext.getContext();

        if (item == start) {
            new InsideContext(scontext) {
                @Override
                public void run() {
                    scontext.getTrackerService().onStartStop();
                }

            };

        } else if (item == pause) {
            new InsideContext(scontext) {
                @Override
                public void run() {
                    scontext.getTrackerService().onPauseResume();
                }
            };

        } else if (item == backlight) {
            new SolidIndexListDialog(scontext.getContext(),new SolidBacklight(c, new SolidPreset(new Storage(c)).getIndex()));


        } else if (item == preferences) {
            ActivitySwitcher.start(c, PreferencesActivity.class);


        } else if (item == map) {
            SolidRenderTheme stheme = new SolidRenderTheme(new AndroidSolidMapsForgeDirectory(c), new FocAndroidFactory(c));
            new SolidCheckListDialog(c,new SolidMapTileStack(stheme));
        } else {
            return false;
        }

        return true;
    }
}
