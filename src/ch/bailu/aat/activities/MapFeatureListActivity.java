package ch.bailu.aat.activities;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.views.FeaturesList;

public class MapFeatureListActivity extends AbsDispatcher {
    private FeaturesList list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        


    }
    

    @Override
    public void onServicesUp(boolean firstRun) {
        final Intent intent = getIntent();
        final String file = AppBroadcaster.getFile(intent);
        
        list = new FeaturesList(getServiceContext(), new File(file));
        setContentView(list);

    }
    
}
