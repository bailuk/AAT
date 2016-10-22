package ch.bailu.aat.dispatcher;

/*
public class RootDispatcher implements OnContentUpdatedInterface, Closeable {
    public static final OnContentUpdatedInterface NULL_LIST[] = new OnContentUpdatedInterface[]{};
    public static final RootDispatcher NULL=new RootDispatcher() {
        @Override
        public void close(){}
    };


    private final OnContentUpdatedInterface TARGET_LIST[];
    
    private OnContentUpdatedInterface targetList[];
    private final ContentSource[] sourceList;



    private RootDispatcher() {
        TARGET_LIST = NULL_LIST;
        targetList = NULL_LIST;
        sourceList = ContentSource.NULL_LIST;
    }

    public RootDispatcher(ContentSource s[], OnContentUpdatedInterface t[]) {
        TARGET_LIST = t;
        targetList = t;
        sourceList = s;

        for (ContentSource source: sourceList) {
            source.setTarget(this);
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
            source.requestUpdate();
    }
    
    
    @Override
    public void onContentUpdated(GpxInformation info) {
        for (OnContentUpdatedInterface target: targetList)
            target.onContentUpdated(info);
    }


    @Override
    public void close() {
        for (ContentSource source: sourceList)
            source.close();
    }
}
*/