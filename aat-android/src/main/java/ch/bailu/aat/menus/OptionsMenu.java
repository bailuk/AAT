package ch.bailu.aat.menus;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.activities.PreferencesActivity;
import ch.bailu.aat.preferences.map.SolidMapTileStack;
import ch.bailu.aat.preferences.presets.SolidBacklight;
import ch.bailu.aat.preferences.presets.SolidPreset;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.tracker.State;
import ch.bailu.aat.views.preferences.SolidCheckListDialog;
import ch.bailu.aat.views.preferences.SolidIndexListDialog;

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
                updateMenuText(scontext.getTrackerService().
                        getState());
            }
        };
    }


    private void updateMenuText(State state) {
        start.setTitle(state.getStartStopTextID());
        start.setIcon(state.getStartStopIconID());
        pause.setTitle(state.getPauseResumeTextID());
    }


    @Override
    public boolean onItemClick(MenuItem item) {
        final Context c = scontext.getContext();

        if (item == start) {
            new InsideContext(scontext) {
                @Override
                public void run() {
                    scontext.getTrackerService().getState().onStartStop();
                }

            };

        } else if (item == pause) {
            new InsideContext(scontext) {
                @Override
                public void run() {
                    scontext.getTrackerService().getState().onPauseResume();
                }
            };

        } else if (item == backlight) {
            new SolidIndexListDialog(new SolidBacklight(c, new SolidPreset(c).getIndex()));


        } else if (item == preferences) {
            ActivitySwitcher.start(c, PreferencesActivity.class);


        } else if (item == map) {
                new SolidCheckListDialog(new SolidMapTileStack(c));
        } else {
            return false;
        }

        return true;
    }
}
