package ch.bailu.aat_lib.service.location;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.service.sensor.SensorServiceInterface;

public class AltitudeFromBarometer extends LocationStackChainedItem {
    private final SensorServiceInterface sensorService;

    public AltitudeFromBarometer(LocationStackItem n, SensorServiceInterface sensorService) {
        super(n);
        this.sensorService = sensorService;
    }

    @Override
    public void passLocation(LocationInformation l) {
        GpxInformation info = sensorService.getInformationOrNull(InfoID.BAROMETER_SENSOR);

        if (info != null) {
            double altitude = info.getAltitude();
            l.setAltitude(altitude);
        }

        super.passLocation(l);
    }
}
