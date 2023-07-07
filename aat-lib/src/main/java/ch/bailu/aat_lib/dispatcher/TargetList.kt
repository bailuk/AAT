package ch.bailu.aat_lib.dispatcher;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.gpx.GpxInformation;

public class TargetList implements OnContentUpdatedInterface{
    private final ArrayList<OnContentUpdatedInterface> targets =
            new ArrayList<>(10);

    @Override
    public void onContentUpdated(int iid, @Nonnull GpxInformation info) {
        for (OnContentUpdatedInterface target: targets) {
            target.onContentUpdated(iid, info);
        }
    }

    public void add(@Nonnull OnContentUpdatedInterface t) {
        targets.add(t);
    }
}
