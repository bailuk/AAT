package ch.bailu.aat.activities;


import android.os.Bundle;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.ServiceLink;

public abstract class AbsServiceLink extends AbsActivity implements GpxInformation.ID{


    private ServiceLink serviceLink=null;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        serviceLink = new ServiceLink(this) {

            private boolean firstRun=true;
            
            @Override
            public void onServicesUp() {
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
        
        if (serviceLink.areAllServicesUp()) 
            onResumeWithService();
    }

    
    @Override
    public void onPause() {
        if (serviceLink.areAllServicesUp()) {
            onPauseWithService();
        }
        serviceLink.down();
        
        super.onPause();
    }
    
    
    public void onResumeWithService() {};
    public void onPauseWithService() {};
    public void onServicesUp(boolean firstRun) {};


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
