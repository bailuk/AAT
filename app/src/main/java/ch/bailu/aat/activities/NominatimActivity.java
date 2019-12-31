package ch.bailu.aat.activities;

import java.io.IOException;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.util.NominatimApi;
import ch.bailu.aat.util.OsmApiHelper;
import ch.bailu.aat.views.bar.MainControlBar;

public class NominatimActivity extends AbsOsmApiActivity {

    @Override
    public OsmApiHelper getApiHelper(BoundingBoxE6 boundingBox) throws SecurityException, IOException {
        return new NominatimApi(this, boundingBox) {
            @Override
            protected String getQueryString() {
                return editorView.toString();
            }
        };
    }

    @Override
    public void addCustomButtons(MainControlBar bar) {
        bar.addSpace();
    }



}
