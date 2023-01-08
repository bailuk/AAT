package ch.bailu.aat.activities;

import android.os.Bundle;

import javax.annotation.Nonnull;

import ch.bailu.aat.dispatcher.LifeCycleDispatcher;
import ch.bailu.aat.util.AbsServiceLink;
import ch.bailu.aat_lib.dispatcher.ContentSource;
import ch.bailu.aat_lib.dispatcher.Dispatcher;
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat_lib.dispatcher.LifeCycleInterface;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.InfoID;

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
    public void addTarget(@Nonnull OnContentUpdatedInterface target, int... iid) {
        dispatcher.addTarget(target, iid);
    }

    @Override
    public void addSource(@Nonnull ContentSource source) {
        dispatcher.addSource(source);
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
