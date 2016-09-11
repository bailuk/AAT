package ch.bailu.aat.activities;

import android.os.Bundle;
import ch.bailu.aat.dispatcher.ContentDispatcher;

public abstract class AbsDispatcher extends AbsMenu {
    private ContentDispatcher dispatcher;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dispatcher = ContentDispatcher.NULL;
    }


    public ContentDispatcher getDispatcher() {
        return dispatcher;
    }


    public void setDispatcher(ContentDispatcher d) {
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
        dispatcher = ContentDispatcher.NULL;
        super.onDestroy();
    }

}
