package ch.bailu.aat.services.location;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.ServiceContext;

public class NewAltitudeFromBarometer extends LocationStackChainedItem {
    private final ServiceContext scontext;

    public NewAltitudeFromBarometer(LocationStackItem n, ServiceContext sc) {
        super(n);
        scontext = sc;
    }

    @Override
    public void passLocation(LocationInformation l) {

        GpxInformation info =
                scontext.getSensorService().getInformationOrNull(InfoID.BAROMETER_SENSOR);

        if (info != null) {
            double altitude = info.getAltitude();
            l.setAltitude(altitude);
        }

        super.passLocation(l);
    }
}
