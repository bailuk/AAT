package ch.bailu.aat.dispatcher;

import java.io.Closeable;

import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.gpx.GpxInformation;

public abstract class ContentSource implements Closeable, DescriptionInterface {

    public static final ContentSource NULL_LIST[] = new ContentSource[]{};
    private RootDispatcher dispatcher = RootDispatcher.NULL;


    public void setDispatcher(RootDispatcher d) {
        dispatcher = d;
    }


    @Override
    public void updateGpxContent(GpxInformation info) {
        dispatcher.updateGpxContent(info);
    }


    public abstract void forceUpdate();
    
    
    @Override
    public void close() {
        
    }


    public abstract void onPause();
    public abstract void onResume();

}
