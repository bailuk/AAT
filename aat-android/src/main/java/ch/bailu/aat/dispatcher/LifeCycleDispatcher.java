package ch.bailu.aat.dispatcher;

import java.util.ArrayList;

import ch.bailu.aat_lib.dispatcher.LifeCycleInterface;

public class LifeCycleDispatcher implements LifeCycleInterface {
    private final ArrayList<LifeCycleInterface> targets = new ArrayList<>(10);

    private boolean awake = false;

    @Override
    public void onResumeWithService() {
        for (LifeCycleInterface t: targets) t.onResumeWithService();
        awake = true;
    }

    @Override
    public void onPauseWithService() {
        for (LifeCycleInterface t: targets) t.onPauseWithService();
        awake = false;
    }

    @Override
    public void onDestroy() {
        for (LifeCycleInterface t: targets) t.onDestroy();
        awake = false;
    }

    public void add(LifeCycleInterface t) {
        targets.add(t);
        if (awake) t.onResumeWithService();
    }
}
