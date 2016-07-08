package ch.bailu.aat.services.tracker.location;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.interfaces.GpxBigDeltaInterface;
import ch.bailu.aat.gpx.parser.SimpleGpxListReader;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.Timer;
import ch.bailu.aat.preferences.SolidMockLocationFile;

public class MockLocation extends LocationStackChainedItem implements Runnable, GpxInformation.ID{

    private static long INTERVAL=1*1000;
    
    private GpxList mockData;
    private GpxPointNode node;
    private Timer timer;
    private int state;
    
    public MockLocation(Context c, LocationStackItem i) {
        super(i);
        
        mockData = new GpxList(GpxBigDeltaInterface.TRK);
        timer=new Timer(this, INTERVAL);
        
        try {
        	File file = new File(new SolidMockLocationFile(c).getValue());
        	mockData = new SimpleGpxListReader(file).getGpxList();
            
            timer.kick();
            sendState(STATE_WAIT);
            
        } catch (IOException e) {
            AppLog.e(c, e);
            sendState(STATE_OFF);
        }
    }

    
    @Override
    public void close() {
        timer.close();
    }
    
    @Override
    public void run() {
        if (sendLocation()) {
            timer.kick();
        } else {
            node = (GpxPointNode) mockData.getPointList().getFirst(); 
            if (sendLocation()) {
                sendState(STATE_ON);
                timer.kick();
            } else {
                sendState(STATE_OFF);
            }
        }
    }

    private boolean sendLocation() {
        if (node != null) {
            
            sendLocation(new MockLocationInformation(node));
            
            node = (GpxPointNode)node.getNext();
            return true;
        }
        return false;
    }
    

    private class MockLocationInformation extends LocationInformation {

        private GpxPointNode node;
        private long creationTime = System.currentTimeMillis();
        
        public MockLocationInformation(GpxPointNode n) {
            setVisibleTrackPoint(n);
            node=n;
        }
        @Override
        public int getID() {
            return ID.INFO_ID_LOCATION;
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
