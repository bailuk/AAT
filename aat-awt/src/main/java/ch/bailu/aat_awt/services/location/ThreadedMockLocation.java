package ch.bailu.aat_awt.services.location;

import ch.bailu.aat_awt.preferences.SolidAwtDataDirectory;
import ch.bailu.aat_lib.factory.FocFactory;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.StateID;
import ch.bailu.aat_lib.gpx.attributes.AutoPause;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.location.LocationInformation;
import ch.bailu.aat_lib.service.location.LocationServiceInterface;
import ch.bailu.aat_lib.service.location.LocationStackChainedItem;
import ch.bailu.aat_lib.service.location.LocationStackItem;
import ch.bailu.aat_lib.service.location.MockLocationInformation;
import ch.bailu.aat_lib.xml.parser.gpx.GpxListReader;
import ch.bailu.foc.Foc;

public class ThreadedMockLocation  extends LocationStackChainedItem {

    private GpxPointNode node;
    private int state = StateID.NOSERVICE;

    private final Foc file;

    private final InnerThread innerThread;

    private final Object lock;

    public ThreadedMockLocation(LocationServiceInterface locationService, LocationStackItem i, StorageInterface s, FocFactory foc) {
        super(i);

        lock = locationService;

        file = new SolidAwtDataDirectory(s,foc).getValueAsFile().child("test.gpx");
        // file = foc.toFoc(new SolidMockLocationFile(s).getValueAsString());
        passState(StateID.WAIT);

        innerThread = new InnerThread();
        innerThread.start();
    }

    private class InnerThread extends Thread {
        public boolean running = true;
        private GpxList list;


        @Override
        public void run() {
            list = new GpxListReader(file, AutoPause.NULL).getGpxList();
            node = (GpxPointNode) list.getPointList().getFirst();

            passState(StateID.ON);

            while (running && node != null) {

                passLocation(new MockLocationInformation(file, state, node));

                node = (GpxPointNode) node.getNext();

                if (running && node != null) {
                    try {
                        sleep(node.getTimeDelta());
                    } catch (InterruptedException e) {
                        AppLog.e(this,e);
                    }
                }
            }

            passState(StateID.OFF);
        }
    }

    @Override
    public void close() {
        innerThread.running = false;
        innerThread.interrupt();
    }

    @Override
    public void passLocation(LocationInformation location) {
        synchronized (lock) {
            super.passLocation(location);
        }
    }

    @Override
    public void passState(int s) {
        synchronized (lock) {
            if (state != s) {
                state = s;
                super.passState(s);
            }
        }
    }
}
