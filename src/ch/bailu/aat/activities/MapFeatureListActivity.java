package ch.bailu.aat.activities;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.ServiceContext.ServiceNotUpException;
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
        Intent intent = getIntent();
        
        String file = AppBroadcaster.getFile(intent);
        
        IconMapService iconMap;
        try {
            iconMap = getServiceContext().getIconMapService();
        } catch (ServiceNotUpException e) {
            iconMap=null;
            e.printStackTrace();
        }
        
        list = new FeaturesList(this, iconMap, new File(file));
        setContentView(list);

    }
    
}
