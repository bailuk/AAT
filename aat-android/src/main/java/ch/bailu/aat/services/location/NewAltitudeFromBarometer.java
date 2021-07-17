package ch.bailu.aat.services.location;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.service.location.LocationInformation;
import ch.bailu.aat_lib.service.location.LocationStackChainedItem;
import ch.bailu.aat_lib.service.location.LocationStackItem;

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
