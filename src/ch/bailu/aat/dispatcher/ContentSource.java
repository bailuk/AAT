package ch.bailu.aat.dispatcher;

import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.CleanUp;

public abstract class ContentSource implements CleanUp, DescriptionInterface {

    public static final ContentSource NULL_LIST[] = new ContentSource[]{};
    private ContentDispatcher dispatcher = ContentDispatcher.NULL; 


    public void setDispatcher(ContentDispatcher d) {
        dispatcher = d;
    }


    @Override
    public void updateGpxContent(GpxInformation info) {
        dispatcher.updateGpxContent(info);
    }


    public abstract void forceUpdate();

}
