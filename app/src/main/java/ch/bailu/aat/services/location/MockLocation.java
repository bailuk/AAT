package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.gpx.AltitudeDelta;
import ch.bailu.aat.gpx.AutoPause;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.MaxSpeed;
import ch.bailu.aat.gpx.StateID;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.gpx.parser.GpxListReader;
import ch.bailu.aat.preferences.SolidMockLocationFile;
import ch.bailu.aat.util.Timer;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.foc.FocName;

public class MockLocation extends LocationStackChainedItem implements Runnable{

    private static final long INTERVAL=1*1000;

    private GpxList mockData;
    private GpxPointNode node;
    private final Timer timer;
    private int state;

    private long nextInterval = INTERVAL;


    public MockLocation(Context c, LocationStackItem i) {
        super(i);

        mockData = new GpxList(GpxType.TRK, MaxSpeed.NULL, AutoPause.NULL, AltitudeDelta.NULL);
        timer=new Timer(this, INTERVAL);

        try {
            Foc file = FocAndroid.factory(c,(new SolidMockLocationFile(c).getValueAsString()));
            mockData = new GpxListReader(file, AutoPause.NULL).getGpxList();

            timer.kick();
            passState(StateID.WAIT);

        } catch (Exception e) {
            AppLog.e(c, e);
            passState(StateID.OFF);
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
                passState(StateID.ON);
                kickTimer();
            } else {
                passState(StateID.OFF);
            }
        }
    }

    private boolean sendLocation() {
        if (node != null) {
            super.passLocation(new MockLocationInformation(node));

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


    private final static Foc MOCK_NAME = new FocName("Mock location");


    private class MockLocationInformation extends LocationInformation {
        private final GpxPointNode node;
        private final long creationTime = System.currentTimeMillis();

        public MockLocationInformation(GpxPointNode n) {
            setVisibleTrackPoint(n);
            node=n;
        }
        /*
        @Override
        public int getID() {
            return InfoID.LOCATION;
        }
        */
        @Override
        public int getState() {
            return state;
        }

        @Override
        public Foc getFile() {
            return MOCK_NAME;
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
        public BoundingBoxE6 getBoundingBox() {
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
//        @Override
//        public double getBearing() {
//            return node.getBearing();
//        }
    }

    @Override
    public void passState(int s) {
        state = s;
        super.passState(s);
    }


}
