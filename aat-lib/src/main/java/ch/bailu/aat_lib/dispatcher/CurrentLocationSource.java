package ch.bailu.aat_lib.dispatcher;


import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.service.ServicesInterface;

public class CurrentLocationSource extends ContentSource {

    private final ServicesInterface services;
    private final Broadcaster broadcaster;


    private final BroadcastReceiver onLocationChange = new BroadcastReceiver () {
        @Override
        public void onReceive(Object ...o) {
            sendUpdate(InfoID.LOCATION, services.getLocationService().getLocationInformation());
        }

    };



    public CurrentLocationSource(ServicesInterface services, Broadcaster broadcaster) {
        this.services = services;
        this.broadcaster = broadcaster;
    }





    @Override
    public void requestUpdate() {
        sendUpdate(InfoID.LOCATION, services.getLocationService().getLocationInformation());
    }


    @Override
    public void onPause() {
        broadcaster.unregister(onLocationChange);
    }

    @Override
    public void onResume() {
        broadcaster.register(onLocationChange, AppBroadcaster.LOCATION_CHANGED);
    }

    @Override
    public int getIID() {
        return InfoID.LOCATION;
    }

    @Override
    public GpxInformation getInfo() {
        return services.getLocationService().getLocationInformation();
    }

}
