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
import ch.bailu.aat.views.preferences.TitleView;

public class PoiActivity extends AbsOsmApiActivity {

    private final static String KEY = PoiActivity.class.getSimpleName();
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
    protected View createMainContentView() {
        LinearLayout linear = new LinearLayout(this);

        linear.setOrientation(LinearLayout.VERTICAL);
        linear.addView(new TitleView(this, osmApi.getApiName()));
        linear.addView(createNodeListView());

        return linear;
    }


    @Override
    protected View createNodeListView() {
        if (AppLayout.isTablet(this)) {
            PercentageLayout mainView = new PercentageLayout(this);
            mainView.setOrientation(LinearLayout.HORIZONTAL);

            mainView.add(super.createNodeListView(),50);
            mainView.add(createPoiListView(), 50);


            return mainView;
        } else {

            multiView = new MultiView(this, KEY);
            multiView.add(super.createNodeListView());
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
