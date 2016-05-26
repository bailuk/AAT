package ch.bailu.aat.activities;


import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.MultiServiceLink;
import ch.bailu.aat.services.MultiServiceLink.ServiceContext;
import ch.bailu.aat.services.MultiServiceLink.ServiceNotUpException;

public abstract class AbsServiceLink extends AbsActivity implements GpxInformation.ID{


    private MultiServiceLink serviceLink=new MultiServiceLink() {
        @Override
        public void onServicesUp() {
            AbsServiceLink.this.onServicesUp();
        }
    };


    public void connectToServices(Class<?>[] services) {
        serviceLink.connectToServices(this, services);
    }

    public abstract void onServicesUp();


    @Override
    public void onDestroy() {
        serviceLink.close();
        serviceLink=null;
        super.onDestroy();
    }    


    public ServiceContext getServiceContext() {
        return serviceLink.getServiceContext(this);
    }
    
    
    public void onStartPauseClick() throws ServiceNotUpException {
        getServiceContext().getTrackerService().getState().onStartPauseResume();
    }
}
