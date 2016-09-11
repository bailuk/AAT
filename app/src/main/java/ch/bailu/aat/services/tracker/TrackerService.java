package ch.bailu.aat.services.tracker;

import java.io.Closeable;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;

public class TrackerService extends VirtualService {

    private final Self self;
    
    public Self getSelf() {
        return self;
    }

    
    
    public TrackerService(ServiceContext sc) {
        super(sc);
        self = new SelfOn();
    }


    @Override
    public void close() {
        self.close();
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
            internal = new TrackerInternals(getSContext());
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
        self.appendStatusText(builder);
    }

}
