package ch.bailu.aat.services.tracker;

import java.io.Closeable;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.AbsService;

public class TrackerService extends AbsService {

    private Self self = new Self();
    
    public Self getSelf() {
        return self;
    }

    
    @Override
    public void onCreate() {
        super.onCreate();
        self.close();
        self = new SelfOn();
    }


    @Override
    public void onDestroy() {
        self.close();
        self = new Self();
        super.onDestroy();
    }
    
    
    public static class Self implements Closeable {
        @Override
        public void close() {}
        public void appendStatusText(StringBuilder builder) {}
        
        public State getState() {
            return new NullState();
        }

        
        public GpxInformation getTrackerInformation() {
            return GpxInformation.NULL;
        }


        public GpxInformation getLocation() {
            return GpxInformation.NULL;
        }

    }

    
    public class SelfOn extends Self {
        private final TrackerInternals internal;
        
        public SelfOn() {
            internal = new TrackerInternals(TrackerService.this);
        }
        
        @Override
        public State getState() {
            return internal.state;
        }

        @Override
        public GpxInformation getTrackerInformation() {
            return internal.logger;
        }

        @Override
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
        public void close() {
            internal.close();
        }

    }



    @Override
    public void appendStatusText(StringBuilder builder) {
        super.appendStatusText(builder);
        self.appendStatusText(builder);
    }


    @Override
    public void onServicesUp() {}
}
