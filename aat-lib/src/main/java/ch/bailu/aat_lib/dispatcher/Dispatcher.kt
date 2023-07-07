package ch.bailu.aat_lib.dispatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;

public class Dispatcher implements DispatcherInterface, OnContentUpdatedInterface {
    private final Map<Integer, TargetList> targets = new HashMap<>(10);
    private final ArrayList<ContentSourceInterface> sources = new ArrayList<>(5);

    private OnContentUpdatedInterface updater = OFF;

    @Override
    public void addTarget(@Nonnull OnContentUpdatedInterface target, int... iid) {
        for (int i: iid) addSingleTarget(target, i);
    }

    private void addSingleTarget(@Nonnull OnContentUpdatedInterface t, int iid) {
        getTargetList(iid).add(t);
    }

    private TargetList getTargetList(int iid) {
        if (!targets.containsKey(iid)) {
            targets.put(iid, new TargetList());
        }
        return targets.get(iid);
    }

    @Override
    public void addSource(@Nonnull ContentSourceInterface source) {
        sources.add(source);
        source.setTarget(this);
    }

    public void onPause() {
        updater = OFF;

        for (ContentSourceInterface source: sources) {
            source.onPause();
        }
    }

    public void onResume() {
        updater = ON;

        for (ContentSourceInterface source : sources) {
            source.onResume();
        }
        requestUpdate();
    }

    @Override
    public void requestUpdate() {
        for (ContentSourceInterface source: sources)
            source.requestUpdate();
    }

    @Override
    public void onContentUpdated(int iid, @Nonnull GpxInformation info) {
        updater.onContentUpdated(iid, info);
    }

    private static final OnContentUpdatedInterface  OFF = (iid, info) -> {};

    private final OnContentUpdatedInterface ON = new  OnContentUpdatedInterface () {
        @Override
        public void onContentUpdated(int infoID, @Nonnull GpxInformation info) {
            update(infoID,     infoID, info);
            update(InfoID.ALL, infoID, info);
        }

        public void update(int listID, int infoID, @Nonnull GpxInformation info) {
            TargetList l = targets.get(listID);
            if (l != null) l.onContentUpdated(infoID, info);
        }
    };
}
