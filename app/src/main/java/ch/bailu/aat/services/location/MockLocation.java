package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.gpx.attributes.AutoPause;
import ch.bailu.aat.gpx.attributes.GpxListAttributes;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.StateID;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.gpx.xml_parser.GpxListReader;
import ch.bailu.aat.preferences.location.SolidMockLocationFile;
import ch.bailu.aat.util.Timer;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.foc.FocName;

public final class MockLocation extends LocationStackChainedItem implements Runnable{

    private final static Foc NULL_FILE = new FocName(MockLocation.class.getSimpleName());
    private static final long INTERVAL=1000L;

    private GpxList list;
    private GpxPointNode node;
    private int state = StateID.NOSERVICE;

    private long interval = INTERVAL;
    private Foc file;

    private final Timer timer;

    public MockLocation(Context c, LocationStackItem i) {
        super(i);

        list = new GpxList(GpxType.TRACK, GpxListAttributes.NULL);
        timer = new Timer(this, INTERVAL);

        file = FocAndroid.factory(c,(new SolidMockLocationFile(c).getValueAsString()));
        list = new GpxListReader(file, AutoPause.NULL).getGpxList();

        timer.kick();
        passState(StateID.WAIT);

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
