package ch.bailu.aat_awt.services;

import ch.bailu.aat_awt.window.AwtStatusIcon;
import ch.bailu.aat_lib.dispatcher.Broadcaster;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.preferences.SolidFactory;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.location.LocationService;
import ch.bailu.aat_lib.service.location.LocationServiceInterface;
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface;
import ch.bailu.aat_lib.service.tracker.TrackerService;
import ch.bailu.aat_lib.service.tracker.TrackerServiceInterface;

public class AwtServices implements ServicesInterface {
    private final LocationService locationService;
    private final TrackerService trackerService;



    public AwtServices (SolidFactory sfactory, Broadcaster broadcastInterface) {
        locationService = new LocationService(sfactory, broadcastInterface);
        trackerService = new TrackerService(sfactory.getDataDirectory(), new AwtStatusIcon(),broadcastInterface, this);
    }
    @Override
    public LocationServiceInterface getLocationService() {
        return locationService;
    }

    @Override
    public void lock(String simpleName) {

    }

    @Override
    public void free(String simpleName) {

    }

    @Override
    public SensorServiceInterface getSensorService() {
        return new SensorServiceInterface() {
            @Override
            public GpxInformation getInformationOrNull(int infoID) {
                return null;
            }

            @Override
            public GpxInformation getInformation(int iid) {
                return null;
            }

            @Override
            public void updateConnections() {

            }

            @Override
            public void scan() {

            }
        };
    }

    @Override
    public TrackerServiceInterface getTrackerService() {
        return trackerService;
    }

}
