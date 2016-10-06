package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.CurrentSpeedDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.TimeDescription;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.tileremover.TileRemoverService;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.preferences.GeneralPreferencesView;
import ch.bailu.aat.views.preferences.MapTilePreferencesView;

public class PreferencesActivity extends AbsDispatcher implements View.OnClickListener {

    private static String SOLID_KEY=PreferencesActivity.class.getSimpleName();

    private View next, prev;
    private MultiView multiView;
    private MapTilePreferencesView mapTilePreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViews();
    }


    private void createViews() {
        LinearLayout contentView = new ContentView(this);

        contentView.addView(createButtonBar());
        multiView = createMultiView();
        contentView.addView(multiView);

        setContentView(contentView);
    }

    private LinearLayout createButtonBar() {
        final ControlBar bar = new MainControlBar(getServiceContext());

        prev = bar.addImageButton(R.drawable.go_previous_inverse);
        bar.addImageButton(R.drawable.content_loading_inverse);
        next = bar.addImageButton(R.drawable.go_next_inverse);

        bar.setOnClickListener1(this);

        return bar;
    }


    private MultiView createMultiView() {
        ContentDescription[] data = new ContentDescription[] {
                new CurrentSpeedDescription(this),
                new AltitudeDescription(this),
                new TimeDescription(this),
                new DistanceDescription(this),
                new AverageSpeedDescription(this),
                new MaximumSpeedDescription(this),
        };

        mapTilePreferences = new MapTilePreferencesView(this, getServiceContext());


        View views[] = {
                mapTilePreferences,
                new GeneralPreferencesView(this),
        };

        return new MultiView(this, SOLID_KEY, GpxInformation.ID.INFO_ID_ALL, views);
    }


    @Override
    public void onResumeWithService() {
        super.onResumeWithService();
        mapTilePreferences.updateText();
    }

    @Override
    public void onDestroy() {
        TileRemoverService ts = getServiceContext().getTileRemoverService();

        if (ts != null) ts.getState().reset();

        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        if (v == prev) multiView.setPrevious();
        else if (v == next) multiView.setNext();
    }
}
