package ch.bailu.aat.services.location;

import android.content.Context;

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

    private final static Foc NULL_FILE = new FocName(MockLocation.class.getSimpleName());
    private static final long INTERVAL=1*1000;

    private GpxList list;
    private GpxPointNode node;
    private int state = StateID.NOSERVICE;

    private long interval = INTERVAL;
    private Foc file = NULL_FILE;

    private final Timer timer;

    public MockLocation(Context c, LocationStackItem i) {
        super(i);

        list = new GpxList(GpxType.TRACK, MaxSpeed.NULL, AutoPause.NULL, AltitudeDelta.NULL);
        timer = new Timer(this, INTERVAL);

        try {
            file = FocAndroid.factory(c,(new SolidMockLocationFile(c).getValueAsString()));
            list = new GpxListReader(file, AutoPause.NULL).getGpxList();

            timer.kick();
            passState(StateID.WAIT);

        } catch (Exception e) {
            file = NULL_FILE;
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
            node = (GpxPointNode) list.getPointList().getFirst();
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
            passLocation(new MockLocationInformation(file, state, node));

            node = (GpxPointNode)node.getNext();
            if (node != null) {
                interval = node.getTimeDelta();
            }
            return true;
        }

        return false;
    }

    private void kickTimer() {
        if (interval <= 0 || interval > 10*INTERVAL) {
            timer.kick(INTERVAL);

        } else {
            timer.kick(interval);

        }
    }


    @Override
    public void passState(int s) {
        if (state != s) {
            state = s;
            super.passState(s);
        }
    }
}
