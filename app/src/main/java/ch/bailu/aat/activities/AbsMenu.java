package ch.bailu.aat.activities;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.menus.OptionsMenu;
import ch.bailu.aat.preferences.PreferenceLoadDefaults;

public abstract class AbsMenu extends AbsServiceLink 
implements DescriptionInterface{

    private OptionsMenu menu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        menu = new OptionsMenu(getServiceContext());
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        
        menu.inflate(m);
        return true;
    }


    @Override 
    public boolean onPrepareOptionsMenu(Menu m) {
        menu.prepare(m);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menu.onItemClick(item);
    }



    @Override
    public void updateGpxContent(GpxInformation info) {}

/*
    public void updateStartButtonText(Button v, GpxInformation info) {
        if (info.getID()== GpxInformation.ID.INFO_ID_TRACKER) {
            v.setText(getStartButtonTextResource(info.getState()));
        }
    }
*/


/*
    private int getStartButtonTextResource(int state) {

        return getServiceContext().getTrackerService().getState().getStartPauseResumeTextID();
    }



    private void updateMenuText(State state) {


        start.setTitle(state.getStartStopTextID());
        start.setIcon(state.getStartStopIconID());
        pause.setTitle(state.getPauseResumeTextID());
        // FIXME: pause.setEnabled(false);
    }

*/
}
