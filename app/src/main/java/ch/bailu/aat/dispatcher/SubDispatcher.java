package ch.bailu.aat.dispatcher;

import ch.bailu.aat.description.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.views.description.TrackDescriptionView;

public class SubDispatcher implements OnContentUpdatedInterface {

    private final TrackDescriptionView.Filter filter;
    private final OnContentUpdatedInterface targetList[];


    public SubDispatcher(OnContentUpdatedInterface t[], int f) {
        filter = new TrackDescriptionView.Filter(f);
        targetList = t;
    }


    @Override
    public void updateGpxContent(GpxInformation info) {
        if (filter.pass(info)) {
            for (OnContentUpdatedInterface target : targetList)
                target.updateGpxContent(info);
        }
    }
}
