package ch.bailu.aat.activities;

import android.os.Bundle;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

import ch.bailu.aat.dispatcher.ContentSource;
import ch.bailu.aat.dispatcher.Dispatcher;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.InfoID;

public abstract class AbsDispatcher extends AbsMenu
        implements DispatcherInterface {

    private Dispatcher dispatcher = null;

    private ArrayList<Closeable> toClose;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dispatcher = new Dispatcher();

        toClose = new ArrayList(10);
    }


    public void toClose(Closeable c) {
        toClose.add(c);
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
        dispatcher.onResume();
    }

    @Override
    public void onPauseWithService() {
        dispatcher.onPause();
    }

    @Override
    public void onDestroy() {
        for (Closeable c: toClose)
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        toClose.clear();
        dispatcher = null;
        super.onDestroy();

    }

}
