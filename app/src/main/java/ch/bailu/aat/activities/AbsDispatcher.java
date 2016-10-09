package ch.bailu.aat.activities;

import android.os.Bundle;

import ch.bailu.aat.dispatcher.RootDispatcher;

public abstract class AbsDispatcher extends AbsMenu {
    private RootDispatcher dispatcher;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dispatcher = RootDispatcher.NULL;
    }


    public void setDispatcher(RootDispatcher d) {
        dispatcher.close();
        dispatcher = d;
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
        dispatcher.close();
        dispatcher = RootDispatcher.NULL;
        super.onDestroy();
    }

}
