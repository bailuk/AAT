package ch.bailu.aat_awt.services;

import ch.bailu.aat_awt.preferences.SolidAwtDataDirectory;
import ch.bailu.aat_awt.preferences.AwtSolidLocationProvider;
import ch.bailu.aat_awt.window.AwtStatusIcon;
import ch.bailu.aat_lib.dispatcher.Broadcaster;
import ch.bailu.aat_lib.factory.FocFactory;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.IconMapServiceInterface;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.cache.ObjImageInterface;
import ch.bailu.aat_lib.service.elevation.ElevetionServiceInterface;
import ch.bailu.aat_lib.service.location.LocationService;
import ch.bailu.aat_lib.service.location.LocationServiceInterface;
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface;
import ch.bailu.aat_lib.service.tracker.TrackerService;
import ch.bailu.aat_lib.service.tracker.TrackerServiceInterface;
import ch.bailu.foc.FocFile;

public class AwtServices implements ServicesInterface {
    private final LocationService locationService;
    private final TrackerService trackerService;



    public AwtServices (StorageInterface storage, Broadcaster broadcaster) {
        FocFactory factory = string -> new FocFile(string);

        locationService = new LocationService(new AwtSolidLocationProvider(storage), broadcaster);
        trackerService = new TrackerService(new SolidAwtDataDirectory(storage,factory), new AwtStatusIcon(),broadcaster, this);
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
    public boolean lock() {
        return true;
    }

    @Override
    public void free() {}

    @Override
    public ElevetionServiceInterface getElevationService() {
        return new ElevetionServiceInterface() {
            @Override
            public short getElevation(int latitudeE6, int longitudeE6) {
                return 0;
            }
        };
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

    @Override
    public IconMapServiceInterface getIconMapService() {
        return new IconMapServiceInterface() {

            @Override
            public ObjImageInterface getIconSVG(GpxPointInterface point, int icon_size) {
                return null;
            }

            @Override
            public String toAssetPath(int key, String value) {
                return "";
            }

            @Override
            public String toAssetPath(GpxPointNode gpxPointNode) {
                return "";
            }
        };
    }

}
