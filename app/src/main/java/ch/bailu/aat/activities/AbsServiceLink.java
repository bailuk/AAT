package ch.bailu.aat.activities;


import android.os.Bundle;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.ServiceLink;

public abstract class AbsServiceLink extends AbsActivity {


    private ServiceLink serviceLink=null;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        serviceLink = new ServiceLink(this) {

            private boolean firstRun=true;
            
            @Override
            public void onServiceUp() {
                AbsServiceLink.this.onServicesUp(firstRun);
                onResumeWithService();
                firstRun=false;
            }
            
        };
    }


    
    
    @Override
    public void onResume() {
        super.onResume();
        serviceLink.up();
        
        if (serviceLink.isUp())
            onResumeWithService();
    }

    
    @Override
    public void onPause() {
        if (serviceLink.isUp()) {
            onPauseWithService();
        }
        serviceLink.down();
        
        super.onPause();
    }
    
    
    public void onResumeWithService() {}
    public void onPauseWithService() {}
    public void onServicesUp(boolean firstRun) {}


    @Override
    public void onDestroy() {
        serviceLink.close();
        serviceLink=null;
        super.onDestroy();
    }    


    public ServiceContext getServiceContext() {
        return serviceLink;
    }
    
  
}
