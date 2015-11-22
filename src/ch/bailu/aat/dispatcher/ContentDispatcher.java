package ch.bailu.aat.dispatcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.CleanUp;

public class ContentDispatcher implements DescriptionInterface, CleanUp, OnSharedPreferenceChangeListener {
    public static final DescriptionInterface NULL_LIST[] = new DescriptionInterface[]{};
    public static final ContentDispatcher NULL=new ContentDispatcher() {
        @Override
        public void cleanUp(){};
    };


    private final DescriptionInterface TARGET_LIST[];
    
    private DescriptionInterface targetList[];
    private ContentSource sourceList[];



    private ContentDispatcher() {
        TARGET_LIST = NULL_LIST;
        targetList = NULL_LIST;
        sourceList = ContentSource.NULL_LIST;
    }

    public ContentDispatcher(Context c, ContentSource s[],DescriptionInterface t[]) {
        TARGET_LIST = t;
        targetList = t;
        sourceList = s;

        for (ContentSource source: sourceList) {
            source.setDispatcher(this);
        }
        forceUpdate();
    }

    public void forceUpdate() {
        for (ContentSource source: sourceList)
            source.forceUpdate();
    }


    public void pause() {
        targetList = NULL_LIST;
    }

    public void resume() {
        targetList = TARGET_LIST;
        forceUpdate();
    }

    @Override
    public void updateGpxContent(GpxInformation info) {
        for (DescriptionInterface target: targetList)
            target.updateGpxContent(info);
    }


    @Override
    public void cleanUp() {
        pause();

        for (ContentSource source: sourceList)
            source.cleanUp();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {

        forceUpdate();
    }
}
