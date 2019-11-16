package ch.bailu.aat.util;


import android.os.Bundle;

import ch.bailu.aat.activities.AbsVolumeKeys;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.ServiceLink;

public abstract class AbsServiceLink extends AbsVolumeKeys {


    private ServiceLink serviceLink=null;

    private enum State {
        destroyed,
        created,
        resumed,
        serviceUp
    }

    private State state=State.destroyed;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = State.created;

        serviceLink = new ServiceLink(this) {

            @Override
            public void onServiceUp() {
                if (state == State.resumed) {
                    onResumeWithService();
                    state = State.serviceUp;
                }
            }

        };
    }




    @Override
    public void onResume() {
        super.onResume();
        state = State.resumed;
        serviceLink.up();
    }


    @Override
    public void onPause() {
        if (state == State.serviceUp) {
            onPauseWithService();
        }
        serviceLink.down();

        state = State.created;
        super.onPause();
    }


    public void onResumeWithService() {}
    public void onPauseWithService() {}


    @Override
    public void onDestroy() {
        serviceLink.close();
        serviceLink=null;
        state = State.destroyed;
        super.onDestroy();
    }


    public ServiceContext getServiceContext() {
        return serviceLink;
    }
}
