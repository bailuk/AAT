package ch.bailu.aat.menus;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.activities.NominatimActivity;
import ch.bailu.aat.preferences.SolidBacklight;
import ch.bailu.aat.preferences.SolidMapTileStack;
import ch.bailu.aat.preferences.SolidOverlayFileList;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.tracker.State;
import ch.bailu.aat.views.preferences.SolidCheckListDialog;
import ch.bailu.aat.views.preferences.SolidIndexListDialog;

public class OptionsMenu extends AbsMenu {
    private MenuItem start, pause, backlight, map, overlays, nominatim;

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

        map = menu.add(R.string.p_map);

        overlays = menu.add(R.string.p_overlay);
        overlays.setIcon(R.drawable.view_paged_inverse);

        nominatim = menu.add(R.string.intro_nominatim);
        nominatim.setIcon(R.drawable.edit_find_inverse);

        backlight = menu.add(R.string.p_backlight_title);
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
        updateMenuText(scontext.getTrackerService().getState());
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
            scontext.getTrackerService().getState().onStartStop();

        } else if (item == pause) {
            scontext.getTrackerService().getState().onPauseResume();

        } else if (item == backlight) {
            new SolidIndexListDialog(new SolidBacklight(c, new SolidPreset(c).getIndex()));

        } else if (item == map) {
            new SolidCheckListDialog(new SolidMapTileStack(c));

        } else if (item == overlays) {
            new SolidCheckListDialog(new SolidOverlayFileList(c));

        } else if (item == nominatim) {
            ActivitySwitcher.start(c, NominatimActivity.class);


        } else {
            return false;

        }



        return true;
    }
}
