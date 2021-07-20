package ch.bailu.aat_lib.dispatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;

public class Dispatcher implements DispatcherInterface, OnContentUpdatedInterface {


    private final Map<Integer, TargetList> targets = new HashMap<>(10);
    private final ArrayList<ContentSource> sources = new ArrayList<>(5);


    private OnContentUpdatedInterface updater = OFF;

    @Override
    public void addTarget(OnContentUpdatedInterface t, int... iid) {
        for (int i: iid) addSingleTarget(t, i);
    }


    private void addSingleTarget(OnContentUpdatedInterface t, int iid) {
        TargetList target = targets.get(iid);

        if (target == null) {
            target = new TargetList();
            targets.put(iid, target);
        }

        target.add(t);
    }

    @Override
    public void addSource(ContentSource s) {
        sources.add(s);
        s.setTarget(this);
    }


    public void onPause() {
        updater = OFF;

        for (ContentSource source: sources) {
            source.onPause();
        }
    }

    public void onResume() {
        updater = ON;

        for (ContentSource source : sources) {
            source.onResume();
        }
        requestUpdate();
    }


    public void requestUpdate() {
        for (ContentSource source: sources)
            source.requestUpdate();
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        updater.onContentUpdated(iid, info);
    }


    private static class TargetList implements OnContentUpdatedInterface{
        public final static TargetList NULL_LIST = new TargetList();

        private final ArrayList<OnContentUpdatedInterface> targets =
                new ArrayList<>(10);


        @Override
        public void onContentUpdated(int iid, GpxInformation info) {
            for (OnContentUpdatedInterface target: targets) {
                target.onContentUpdated(iid, info);
            }
        }

        public void add(OnContentUpdatedInterface t) {
            targets.add(t);
        }
    }

    private static final OnContentUpdatedInterface
            OFF = (iid, info) -> {};


    private final OnContentUpdatedInterface
            ON = new  OnContentUpdatedInterface () {
        @Override
        public void onContentUpdated(int infoID, GpxInformation info) {
            update(infoID,     infoID, info);
            update(InfoID.ALL, infoID, info);
        }


        public void update(int listID, int infoID, GpxInformation info) {
            TargetList l = targets.get(listID);
            if (l != null) l.onContentUpdated(infoID, info);
        }
    };
}
