package ch.bailu.aat.activities;

import android.view.View;
import android.widget.LinearLayout;

import org.mapsforge.poi.storage.PoiCategory;

import java.util.ArrayList;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.util.OsmApiConfiguration;
import ch.bailu.aat.util.PoiApi;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.mview.MultiView;
import ch.bailu.aat.views.osm_features.PoiView;
import ch.bailu.aat.views.preferences.TitleView;

public class PoiActivity extends AbsOsmApiActivity {

    private final static String KEY = PoiActivity.class.getSimpleName();
    private MultiView multiView;
    private PoiView poiView;

    @Override
    public OsmApiConfiguration createApiConfiguration(BoundingBoxE6 boundingBox) {
        return new PoiApi(this, boundingBox) {

            @Override
            protected ArrayList<PoiCategory> getSelectedCategories() {
                poiView.saveSelected(getQueryFile());
                return poiView.getSelectedCategories();
            }
        };
    }


    @Override
    protected View createMainContentView(ContentView contentView) {
        LinearLayout linear = new LinearLayout(this);

        linear.setOrientation(LinearLayout.VERTICAL);
        linear.addView(new TitleView(this, getConfiguration().getApiName(), theme));
        linear.addView(createNodeListView(contentView));

        return linear;
    }


    @Override
    protected View createNodeListView(ContentView contentView) {
        if (AppLayout.isTablet(this)) {
            PercentageLayout mainView = new PercentageLayout(this);
            mainView.setOrientation(LinearLayout.HORIZONTAL);

            mainView.add(super.createNodeListView(contentView),50);
            mainView.add(createPoiListView(), 50);


            return mainView;
        } else {

            multiView = new MultiView(this, KEY);
            multiView.add(super.createNodeListView(contentView));
            multiView.add(createPoiListView());

            contentView.addMvIndicator(multiView);
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
        poiView = new PoiView(getServiceContext(),
                getConfiguration().getBaseDirectory().child(PoiApi.SELECTED), theme);

        theme.background(poiView);
        return poiView;

    }

    @Override
    public void onDestroy() {
        poiView.close(getServiceContext());
        super.onDestroy();
    }

}
