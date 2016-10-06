package ch.bailu.aat.dispatcher;

import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.views.description.TrackDescriptionView;

public class SubDispatcher implements DescriptionInterface {

    private final TrackDescriptionView.Filter filter;
    private final DescriptionInterface targetList[];


    public SubDispatcher(DescriptionInterface t[], int f) {
        filter = new TrackDescriptionView.Filter(f);
        targetList = t;
    }


    @Override
    public void updateGpxContent(GpxInformation info) {
        if (filter.pass(info)) {
            for (DescriptionInterface target : targetList)
                target.updateGpxContent(info);
        }
    }
}
