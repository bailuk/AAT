package ch.bailu.aat.services.tracker;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.AbsService;

public class TrackerService extends AbsService {
    private TrackerInternals internal;
    
    
    @Override
    public void onCreate() {
        super.onCreate();
        internal = new TrackerInternals(this);
    }


 
    @Override
    public void onDestroy() {

        internal.cleanUp();
        internal=null;
        super.onDestroy();
    }





    public State getState() {
        return internal.state;
    }

    
    public GpxInformation getTrackerInformation() {
        return internal.logger;
    }


    public GpxInformation getLocation() {
        return internal.location.getLocationInformation();
    }


    @Override
    public void appendStatusText(StringBuilder builder) {
        super.appendStatusText(builder);
                
        

        builder.append("<p>Log to: ");
        builder.append(internal.logger.getPath());
        builder.append("</p>");
        
        internal.location.appendStatusText(builder);
    }


    @Override
    public void onServicesUp() {}
}
