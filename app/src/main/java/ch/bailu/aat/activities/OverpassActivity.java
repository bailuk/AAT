package ch.bailu.aat.activities;

import android.view.View;
import android.widget.LinearLayout;

import java.io.IOException;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.util.OsmApiConfiguration;
import ch.bailu.aat.util.OverpassApi;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.osm_features.OsmFeaturesView;
import ch.bailu.aat.views.osm_features.OnSelected;

public class OverpassActivity extends AbsOsmApiActivity  {

    private final static String KEY = OverpassActivity.class.getSimpleName();

    private OsmFeaturesView osmFeatures;
    private MultiView multiView = null;


    @Override
    public View createNodeListView() {
        if (AppLayout.isTablet(this)) {
            final PercentageLayout mainView = new PercentageLayout(this);
            mainView.setOrientation(LinearLayout.HORIZONTAL);

            mainView.add(super.createNodeListView(),50);
            mainView.add(createOsmFeaturesView(), 50);

            return mainView;
        } else {

            multiView = new MultiView(this, KEY);
            multiView.add(super.createNodeListView());
            multiView.add(createOsmFeaturesView());
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

                //if (multiView != null) multiView.setNext();

            } else if (action == OnSelected.SHOW) {

            }
        });

        AppTheme.alt.background(osmFeatures);
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
