package ch.bailu.aat.activities;


import android.os.Bundle;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.ServiceContext.ServiceNotUpException;
import ch.bailu.aat.services.ServiceLink;

public abstract class AbsServiceLink extends AbsActivity implements GpxInformation.ID{


    private ServiceLink serviceLink=null;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        serviceLink = new ServiceLink(this) {

            @Override
            public void onServicesUp() {
                AbsServiceLink.this.onServicesUp();
            }
            
        };
            
        
    }


    @Override
    public void onResume() {
        super.onResume();
        serviceLink.up();
    }

    
    @Override
    public void onPause() {
        serviceLink.down();
        super.onPause();
    }
    
    
    public abstract void onServicesUp();


    @Override
    public void onDestroy() {
        serviceLink.close();
        serviceLink=null;
        super.onDestroy();
    }    


    public ServiceContext getServiceContext() {
        return serviceLink;
    }
    
    
    public void onStartPauseClick() throws ServiceNotUpException {
        getServiceContext().getTrackerService().getState().onStartPauseResume();
    }
}
