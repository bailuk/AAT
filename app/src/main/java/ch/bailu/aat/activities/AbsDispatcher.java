package ch.bailu.aat.activities;

import android.os.Bundle;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.ContentSource;
import ch.bailu.aat.dispatcher.Dispatcher;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.LifeCycleDispatcher;
import ch.bailu.aat.dispatcher.LifeCycleInterface;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.util.AbsServiceLink;
import ch.bailu.aat.util.ui.AppLog;

public abstract class AbsDispatcher extends AbsServiceLink
        implements DispatcherInterface {

    private Dispatcher dispatcher = null;
    private LifeCycleDispatcher lifeCycle = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dispatcher = new Dispatcher();
        lifeCycle = new LifeCycleDispatcher();
    }


    public void addLC(LifeCycleInterface t) {
        lifeCycle.add(t);
    }

    public void addTarget(OnContentUpdatedInterface target) {
        addTarget(target, InfoID.ALL);
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

        lifeCycle.onResumeWithService();
        dispatcher.onResume();


        super.onResumeWithService();

    }

    @Override
    public void onPauseWithService() {
        lifeCycle.onPauseWithService();
        dispatcher.onPause();
    }

    @Override
    public void onDestroy() {
        lifeCycle.onDestroy();
        lifeCycle = null;
        dispatcher = null;
        super.onDestroy();

    }
}
