package ch.bailu.aat.menus;

import org.osmdroid.util.BoundingBoxE6;

import ch.bailu.aat.R;
import ch.bailu.aat.activities.ActivitySwitcher;
import ch.bailu.aat.activities.NominatimActivity;
import ch.bailu.aat.preferences.CheckListDialog;
import ch.bailu.aat.preferences.IndexListDialog;
import ch.bailu.aat.preferences.SolidAutopause;
import ch.bailu.aat.preferences.SolidBacklight;
import ch.bailu.aat.preferences.SolidMapTileStack;
import ch.bailu.aat.preferences.SolidOverlayFileList;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.tracker.State;
import android.content.Context;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;

public class OptionsMenu extends AbsMenu {
    private MenuItem start, pause, backlight, autopause, map, overlays, nominatim;

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

        nominatim = menu.add(R.string.intro_nominatim);
        nominatim.setIcon(R.drawable.edit_find_inverse);

        map = menu.add(R.string.p_map);

        overlays = menu.add(R.string.p_overlay);
        overlays.setIcon(R.drawable.view_paged_inverse);

        backlight = menu.add(R.string.p_backlight_title);
    }

    @Override
    public void inflateWithHeader(ContextMenu menu) {
        menu.setHeaderTitle(R.string.app_sname);
        inflate(menu);
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
            new IndexListDialog(c, new SolidBacklight(c, new SolidPreset(c).getIndex()));

        } else if (item == autopause) {
            new IndexListDialog(c, new SolidAutopause(c, new SolidPreset(c).getIndex()));

        } else if (item == map) {
            new CheckListDialog(c,new SolidMapTileStack(c, new SolidPreset(c).getIndex()));

        } else if (item == overlays) {
            new CheckListDialog(c,new SolidOverlayFileList(c));

        } else if (item == nominatim) {
            ActivitySwitcher.start(c, NominatimActivity.class,new BoundingBoxE6(0,0,0,0));


        } else {
            return false;

        }



        return true;
    }

}
