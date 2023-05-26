package ch.bailu.aat.services.location;

import android.content.Context;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.AndroidTimer;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.StateID;
import ch.bailu.aat_lib.gpx.attributes.AutoPause;
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;
import ch.bailu.aat_lib.preferences.location.SolidMockLocationFile;
import ch.bailu.aat_lib.service.location.LocationStackChainedItem;
import ch.bailu.aat_lib.service.location.LocationStackItem;
import ch.bailu.aat_lib.service.location.MockLocationInformation;
import ch.bailu.aat_lib.xml.parser.gpx.GpxListReader;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroid;

public final class MockLocation extends LocationStackChainedItem implements Runnable{

    private static final long INTERVAL=1000L;

    private GpxList list;
    private GpxPointNode node;
    private int state = StateID.NOSERVICE;

    private long interval = INTERVAL;
    private final Foc file;

    private final AndroidTimer timer;

    public MockLocation(Context c, LocationStackItem i) {
        super(i);

        list = new GpxList(GpxType.TRACK, GpxListAttributes.NULL);
        timer = new AndroidTimer();

        file = FocAndroid.factory(c,(new SolidMockLocationFile(new Storage(c)).getValueAsString()));
        list = new GpxListReader(file, AutoPause.NULL).getGpxList();

        timer.kick(INTERVAL, this);
        passState(StateID.WAIT);

    }


    @Override
    public void close() {
        timer.cancel();
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
            timer.kick(INTERVAL,this);

        } else {
            timer.kick(interval, this);

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
