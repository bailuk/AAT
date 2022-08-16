package ch.bailu.aat.activities;

import ch.bailu.aat.util.NominatimApi;
import ch.bailu.aat_lib.search.poi.OsmApiConfiguration;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat_lib.coordinates.BoundingBoxE6;

public class NominatimActivity extends AbsOsmApiActivity {

    @Override
    public OsmApiConfiguration createApiConfiguration(BoundingBoxE6 boundingBox)  {
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
