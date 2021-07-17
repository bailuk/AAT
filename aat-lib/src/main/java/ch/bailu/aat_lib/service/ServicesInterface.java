package ch.bailu.aat_lib.service;

import ch.bailu.aat_lib.service.location.LocationServiceInterface;
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface;
import ch.bailu.aat_lib.service.tracker.TrackerServiceInterface;

public interface ServicesInterface {
    LocationServiceInterface getLocationService();
    SensorServiceInterface getSensorService();
    TrackerServiceInterface getTrackerService();

    void lock(String simpleName);

    void free(String simpleName);



}
