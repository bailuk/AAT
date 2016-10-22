package ch.bailu.aat.dispatcher;

import android.util.SparseArray;

import java.util.ArrayList;

import ch.bailu.aat.gpx.GpxInformation;

public class Dispatcher implements DispatcherInterface, OnContentUpdatedInterface {


    private final SparseArray<TargetList>
        targets = new SparseArray(10);

    private final ArrayList<ContentSource> sources = new ArrayList(5);


    private OnContentUpdatedInterface updater = OFF;

    @Override
    public void addTarget(OnContentUpdatedInterface t, int iid) {
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
        s.add(this);
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
    public void onContentUpdated(GpxInformation info) {
        updater.onContentUpdated(info);
    }


      private static class TargetList implements OnContentUpdatedInterface{
        public final static TargetList NULL_LIST = new TargetList();

        private final ArrayList<OnContentUpdatedInterface> targets =
                new ArrayList(10);


        @Override
        public void onContentUpdated(GpxInformation info) {
            for (OnContentUpdatedInterface target: targets) {
                target.onContentUpdated(info);
            }
        }

        public void add(OnContentUpdatedInterface t) {
            targets.add(t);
        }
    }

    private static final OnContentUpdatedInterface
            OFF = new OnContentUpdatedInterface() {
        @Override
        public void onContentUpdated(GpxInformation info) {}
    };


    private final OnContentUpdatedInterface
            ON = new  OnContentUpdatedInterface () {
        @Override
        public void onContentUpdated(GpxInformation info) {
            update(info.getID(), info);
            update(GpxInformation.ID.INFO_ID_ALL, info);
        }


        public void update(int iid, GpxInformation info) {
            final TargetList l = targets.get(iid, TargetList.NULL_LIST);
             l.onContentUpdated(info);
        }
    };
}
