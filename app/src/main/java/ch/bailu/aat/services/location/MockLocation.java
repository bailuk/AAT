package ch.bailu.aat.services.location;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.gpx.MaxSpeed;
import ch.bailu.aat.gpx.StateID;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.gpx.parser.SimpleGpxListReader;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.Timer;
import ch.bailu.aat.helpers.file.FileAccess;
import ch.bailu.aat.preferences.SolidMockLocationFile;

public class MockLocation extends LocationStackChainedItem implements Runnable{

    private static final long INTERVAL=1*1000;
    
    private GpxList mockData;
    private GpxPointNode node;
    private final Timer timer;
    private int state;

    private long nextInterval = INTERVAL;


    public MockLocation(Context c, LocationStackItem i) {
        super(i);
        
        mockData = new GpxList(GpxType.TRK, new MaxSpeed.Raw());
        timer=new Timer(this, INTERVAL);
        
        try {
        	File file = new File(new SolidMockLocationFile(c).getValueAsString());
        	mockData = new SimpleGpxListReader(new FileAccess(file)).getGpxList();
            
            timer.kick();
            sendState(StateID.WAIT);
            
        } catch (IOException e) {
            AppLog.e(c, e);
            sendState(StateID.OFF);
        }
    }

    
    @Override
    public void close() {
        timer.close();
    }
    
    @Override
    public void run() {
        if (sendLocation()) {
            kickTimer();
        } else {
            node = (GpxPointNode) mockData.getPointList().getFirst(); 
            if (sendLocation()) {
                sendState(StateID.ON);
                kickTimer();
            } else {
                sendState(StateID.OFF);
            }
        }
    }

    private boolean sendLocation() {
        if (node != null) {
            sendLocation(new MockLocationInformation(node));
            
            node = (GpxPointNode)node.getNext();
            if (node != null) {
                nextInterval=node.getTimeDelta();
            }
            return true;
        }

        return false;
    }

    private void kickTimer() {
        if (nextInterval <= 0 || nextInterval > 10*INTERVAL) {
            timer.kick();

        } else {
            timer.kick(nextInterval);

        }

        nextInterval = INTERVAL;
    }


    private class MockLocationInformation extends LocationInformation {

        private final GpxPointNode node;
        private final long creationTime = System.currentTimeMillis();
        
        public MockLocationInformation(GpxPointNode n) {
            setVisibleTrackPoint(n);
            node=n;
        }
        @Override
        public int getID() {
            return InfoID.LOCATION;
        }
        @Override
        public int getState() {
            return state;
        }
        @Override
        public String getName() {
            return "Mock location";
        }
        @Override
        public long getTimeStamp() {
            return creationTime;
        }
        @Override
        public float getDistance() {
            return node.getDistance();
        }
        @Override
        public float getSpeed() {
            return node.getSpeed();
        }
        @Override
        public float getAcceleration() {
            return node.getAcceleration();
        }
        @Override
        public long getTimeDelta() {
            return node.getTimeDelta();
        }
        @Override
        public BoundingBox getBoundingBox() {
            return node.getBoundingBox();
        }
        @Override
        public boolean hasAccuracy() {
            return true;
        }
        @Override
        public boolean hasSpeed() {
            return true;
        }
        @Override
        public boolean hasAltitude() {
            return true;
        }
        @Override
        public boolean hasBearing() {
            return true;
        }
        @Override
        public float getAccuracy() {
            return 5f;
        }
        @Override
        public double getBearing() {
            return node.getBearing();
        }
    }
    
    @Override
    public void sendState(int s) {
        state = s;
        super.sendState(s);
    }
    

    @Override
    public void newLocation(LocationInformation location) {}


    @Override
    public void preferencesChanged(Context c, int i) {}
    
    

}
