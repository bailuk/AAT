package ch.bailu.aat.activities;


import org.osmdroid.util.BoundingBoxE6;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.preferences.CheckListDialog;
import ch.bailu.aat.preferences.IndexListDialog;
import ch.bailu.aat.preferences.PreferenceLoadDefaults;
import ch.bailu.aat.preferences.SolidAutopause;
import ch.bailu.aat.preferences.SolidBacklight;
import ch.bailu.aat.preferences.SolidMapTileStack;
import ch.bailu.aat.preferences.SolidOverlayFileList;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.services.MultiServiceLink.ServiceNotUpException;
import ch.bailu.aat.services.tracker.State;
import ch.bailu.aat.R;

public abstract class AbsMenu extends AbsServiceLink 
implements DescriptionInterface{

    private MenuItem start, pause, backlight, autopause, map, overlays, nominatim;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        start = menu.add(R.string.tracker_start);
        start.setIcon(R.drawable.media_playback_start);

        pause = menu.add(R.string.tracker_pause);
        pause.setIcon(R.drawable.media_playback_pause);

        nominatim = menu.add(R.string.intro_nominatim);
        nominatim.setIcon(R.drawable.edit_find);

        map = menu.add(R.string.p_map);

        overlays = menu.add(R.string.p_overlay);
        overlays.setIcon(R.drawable.view_paged);
        
        backlight = menu.add(R.string.p_backlight_title);


        return true;
    }


    @Override 
    public boolean onPrepareOptionsMenu(Menu menu) {

        try {
            updateMenuText(getTrackerService().getState());
        } catch (ServiceNotUpException e) {
            e.printStackTrace();
        }

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {

            if (item == start) {
                getTrackerService().getState().onStartStop();

            } else if (item == pause) {
                getTrackerService().getState().onPauseResume();

            } else if (item == backlight) {
                new IndexListDialog(this, new SolidBacklight(this, new SolidPreset(this).getIndex()));

            } else if (item == autopause) {
                new IndexListDialog(this, new SolidAutopause(this, new SolidPreset(this).getIndex()));

            } else if (item == map) {
                new CheckListDialog(this,new SolidMapTileStack(this, new SolidPreset(this).getIndex()));

            } else if (item == overlays) {
                new CheckListDialog(this,new SolidOverlayFileList(this));

            } else if (item == nominatim) {
                ActivitySwitcher.start(this, NominatimActivity.class,new BoundingBoxE6(0,0,0,0));

                
            } else {
                return false;

            }


        } catch (ServiceNotUpException e) {
            AppLog.e(this, e);
            return false;
        }

        return true;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new PreferenceLoadDefaults(this);
    }



    @Override
    public void updateGpxContent(GpxInformation info) {}

/*
    @Override
    public boolean update(GpxInformation info) {
        return false;
    }
*/


    public void updateStartButtonText(Button v, GpxInformation info) {
        if (info.getID()== GpxInformation.ID.INFO_ID_TRACKER) {
            v.setText(getStartButtonTextResource(info.getState()));
        }
    }




    private int getStartButtonTextResource(int state) {

        try {
            return getTrackerService().getState().getStartPauseResumeTextID();
        } catch (ServiceNotUpException e) {
            return R.string.tracker_start;
        }
    }



    private void updateMenuText(State state) {


        start.setTitle(state.getStartStopTextID());
        start.setIcon(state.getStartStopIconID());
        pause.setTitle(state.getPauseResumeTextID());
        // FIXME: pause.setEnabled(false);
    }

/*
    @Override
    public int getUpdatePriority() {
        return 1;
    }
*/


}
