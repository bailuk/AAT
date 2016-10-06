package ch.bailu.aat.dispatcher;

import android.content.Context;

import java.io.Closeable;

import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.gpx.GpxInformation;

public class RootDispatcher implements DescriptionInterface, Closeable {
    public static final DescriptionInterface NULL_LIST[] = new DescriptionInterface[]{};
    public static final RootDispatcher NULL=new RootDispatcher() {
        @Override
        public void close(){}
    };


    private final DescriptionInterface TARGET_LIST[];
    
    private DescriptionInterface targetList[];
    private final ContentSource[] sourceList;



    private RootDispatcher() {
        TARGET_LIST = NULL_LIST;
        targetList = NULL_LIST;
        sourceList = ContentSource.NULL_LIST;
    }

    public RootDispatcher(Context c, ContentSource s[], DescriptionInterface t[]) {
        TARGET_LIST = t;
        targetList = t;
        sourceList = s;

        for (ContentSource source: sourceList) {
            source.setDispatcher(this);
        }
    }


    public void onPause() {
        targetList = NULL_LIST;
        for (ContentSource source: sourceList) {
            source.onPause();
        }

    }

    public void onResume() {
        targetList = TARGET_LIST;
        
        
        for (ContentSource source: sourceList) {
            source.onResume();
        }

        forceUpdate();
    }

    public void forceUpdate() {
        for (ContentSource source: sourceList)
            source.forceUpdate();
    }
    
    
    @Override
    public void updateGpxContent(GpxInformation info) {
        for (DescriptionInterface target: targetList)
            target.updateGpxContent(info);
    }


    @Override
    public void close() {
        for (ContentSource source: sourceList)
            source.close();
    }
}
