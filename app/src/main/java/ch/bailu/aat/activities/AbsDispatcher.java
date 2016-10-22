package ch.bailu.aat.activities;

import android.os.Bundle;

import ch.bailu.aat.dispatcher.ContentSource;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.Dispatcher;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;

public abstract class AbsDispatcher extends AbsMenu
        implements GpxInformation.ID, DispatcherInterface {

    private Dispatcher dispatcher = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dispatcher = new Dispatcher();
    }


    public void addTarget(OnContentUpdatedInterface target) {
        addTarget(target, GpxInformation.ID.INFO_ID_ALL);
    }

    @Override
    public void addTarget(OnContentUpdatedInterface target, int iid) {
        dispatcher.addTarget(target, iid);
    }


    @Override
    public void addSource(ContentSource s) {
        dispatcher.addSource(s);
    }


    @Override
    public void onResumeWithService() {
        dispatcher.onResume();
    }

    @Override
    public void onPauseWithService() {
        dispatcher.onPause();
    }

    @Override
    public void onDestroy() {
        dispatcher = null;
        super.onDestroy();
    }

}
