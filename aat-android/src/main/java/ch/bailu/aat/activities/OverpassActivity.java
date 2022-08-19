package ch.bailu.aat.activities;

import android.view.View;
import android.widget.LinearLayout;

import ch.bailu.aat_lib.search.poi.OsmApiConfiguration;
import ch.bailu.aat.util.OverpassApi;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.mview.MultiView;
import ch.bailu.aat.views.osm_features.OnSelected;
import ch.bailu.aat.views.osm_features.OsmFeaturesView;
import ch.bailu.aat_lib.coordinates.BoundingBoxE6;

public class OverpassActivity extends AbsOsmApiActivity  {

    private final static String KEY = OverpassActivity.class.getSimpleName();

    private OsmFeaturesView osmFeatures;
    private MultiView multiView = null;


    @Override
    public View createNodeListView(ContentView contentView) {
        if (AppLayout.isTablet(this)) {
            final PercentageLayout mainView = new PercentageLayout(this);
            mainView.setOrientation(LinearLayout.HORIZONTAL);

            mainView.add(super.createNodeListView(contentView),50);
            mainView.add(createOsmFeaturesView(), 50);

            return mainView;
        } else {

            multiView = new MultiView(this, KEY);
            multiView.add(super.createNodeListView(contentView));
            multiView.add(createOsmFeaturesView());
            contentView.addMvIndicator(multiView);
            return multiView;
        }

    }

    private View createOsmFeaturesView() {
        osmFeatures = new OsmFeaturesView(getServiceContext());
        osmFeatures.setOnTextSelected((e, action, variant) -> {
            if (action == OnSelected.FILTER) {
                osmFeatures.setFilterText(e.getSummaryKey());

            } else if (action == OnSelected.EDIT){
                insertLine(variant);
            }
        });

        theme.background(osmFeatures);
        return osmFeatures;
    }

    @Override
    public void onResumeWithService() {
        super.onResumeWithService();
        osmFeatures.onResume(getServiceContext());
    }

    @Override
    public void onPause() {
        osmFeatures.onPause(getServiceContext());
        super.onPause();
    }

    @Override
    public OsmApiConfiguration createApiConfiguration(BoundingBoxE6 boundingBox) {
        return new OverpassApi(this, boundingBox) {
            @Override
            protected String getQueryString() {
                return editorView.toString();
            }
        };
    }

    @Override
    public void addCustomButtons(MainControlBar bar) {
        if (!AppLayout.isTablet(this)) {
            bar.addMvNext(multiView);
        } else {
            bar.addSpace();
        }
    }
}
