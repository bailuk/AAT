package ch.bailu.aat.activities;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.icons.IconMapService;
import ch.bailu.aat.views.FeaturesList;

public class MapFeatureListActivity extends AbsDispatcher {
    private FeaturesList list;

    private static final Class<?> SERVICES[] = {
        IconMapService.class, 
    };    
    

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        connectToServices(SERVICES);
        


    }
    

    @Override
    public void onServicesUp() {
        final Intent intent = getIntent();
        final String file = AppBroadcaster.getFile(intent);
        final IconMapService.Self iconMap = getServiceContext().getIconMapService();
        
        list = new FeaturesList(this, iconMap, new File(file));
        setContentView(list);

    }
    
}
