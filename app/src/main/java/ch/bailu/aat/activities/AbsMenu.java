package ch.bailu.aat.activities;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.menus.OptionsMenu;

public abstract class AbsMenu extends AbsServiceLink  {

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

}
