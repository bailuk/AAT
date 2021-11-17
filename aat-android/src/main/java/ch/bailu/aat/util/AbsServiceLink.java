package ch.bailu.aat.util;


import android.os.Bundle;

import ch.bailu.aat.activities.AbsHardwareButtons;
import ch.bailu.aat.dispatcher.AndroidBroadcaster;
import ch.bailu.aat.services.ServiceLink;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.Broadcaster;
import ch.bailu.aat_lib.service.ServicesInterface;

public abstract class AbsServiceLink extends AbsHardwareButtons {


    private ServiceLink serviceLink=null;
    private Broadcaster broadcaster;

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
        broadcaster = new AndroidBroadcaster(this);
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


    public AppContext getAppContext() {
        return new AppContext() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        };
    }


    public ServicesInterface getServiceContext() {
        return serviceLink;
    }
    public Broadcaster getBroadcaster() {return broadcaster;}
}
