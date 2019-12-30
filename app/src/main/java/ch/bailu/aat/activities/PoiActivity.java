package ch.bailu.aat.activities;

import android.view.View;
import android.widget.LinearLayout;

import org.mapsforge.poi.storage.PoiCategory;

import java.io.IOException;
import java.util.ArrayList;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.util.OsmApiHelper;
import ch.bailu.aat.util.PoiApi;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.osm_features.PoiView;

public class PoiActivity extends AbsOsmApiActivity {

    private MultiView multiView;
    private PoiView poiView;

    @Override
    public OsmApiHelper getApiHelper(BoundingBoxE6 boundingBox) throws SecurityException, IOException {
        return new PoiApi(this, boundingBox) {
            @Override
            protected ArrayList<PoiCategory> getCategories() {
                return poiView.getCategories();
            }
        };
    }




    @Override
    public View createMainContentView() {
        if (AppLayout.isTablet(this)) {
            PercentageLayout mainView = new PercentageLayout(this);
            mainView.setOrientation(LinearLayout.HORIZONTAL);

            mainView.add(super.createMainContentView(),50);
            mainView.add(createPoiListView(), 50);


            return mainView;
        } else {

            multiView = new MultiView(this, OverpassActivity.class.getSimpleName());
            multiView.add(super.createMainContentView());
            multiView.add(createPoiListView());

            return multiView;
        }

    }


    @Override
    public void addCustomButtons(MainControlBar bar) {
        if (!AppLayout.isTablet(this)) {
            bar.addMvNext(multiView);
        } else {
            bar.addSpace();
        }
    }

    private View createPoiListView() {
        poiView = new PoiView(getServiceContext());

        AppTheme.alt.background(poiView);
        return poiView;

    }

    @Override
    public void onDestroy() {
        poiView.close(getServiceContext());
        super.onDestroy();
    }

}
