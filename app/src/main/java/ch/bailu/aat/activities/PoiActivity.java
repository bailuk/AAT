package ch.bailu.aat.activities;

import java.io.IOException;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.util.OsmApiHelper;
import ch.bailu.aat.views.bar.ControlBar;

public class PoiActivity extends AbsOsmApiActivity {
    @Override
    public OsmApiHelper createUrlGenerator(BoundingBoxE6 boundingBox) throws SecurityException, IOException {
        return null;
    }

    @Override
    public void addButtons(ControlBar bar) {

    }
}
