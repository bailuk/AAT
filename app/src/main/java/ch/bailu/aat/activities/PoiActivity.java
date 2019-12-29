package ch.bailu.aat.activities;

import android.view.View;
import android.widget.LinearLayout;

import java.io.IOException;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.util.OsmApiHelper;
import ch.bailu.aat.util.PoiApi;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.MultiView;

public class PoiActivity extends AbsOsmApiActivity {

    private MultiView multiView;


    @Override
    public OsmApiHelper getApiHelper(BoundingBoxE6 boundingBox) throws SecurityException, IOException {
        return new PoiApi(this);
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
        return new View(this);
    }
}
