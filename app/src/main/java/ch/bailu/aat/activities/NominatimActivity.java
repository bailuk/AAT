package ch.bailu.aat.activities;

import org.osmdroid.util.BoundingBoxOsm;

import java.io.IOException;

import ch.bailu.aat.helpers.NominatimApi;
import ch.bailu.aat.helpers.OsmApiHelper;
import ch.bailu.aat.views.ControlBar;

public class NominatimActivity extends AbsOsmApiActivity {

    @Override
    public OsmApiHelper createUrlGenerator(BoundingBoxOsm boundingBox) throws SecurityException, IOException {
        return new NominatimApi(this, boundingBox);
    }

    @Override
    public void addButtons(ControlBar bar) {}

    

}
