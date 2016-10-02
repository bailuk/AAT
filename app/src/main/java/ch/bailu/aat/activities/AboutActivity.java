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
import ch.bailu.aat.views.HtmlScrollTextView;
import ch.bailu.aat.views.HtmlTextView;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.MultiView;
import ch.bailu.aat.views.TrackDescriptionView;
import ch.bailu.aat.views.ViewWrapper;
import ch.bailu.aat.views.preferences.MapTilePreferencesView;


public class AboutActivity extends AbsDispatcher implements View.OnClickListener {

    private static String SOLID_KEY=AboutActivity.class.getSimpleName();

    private View next, prev;
    private MultiView multiView;

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



        TrackDescriptionView multiViewLayout[] = {
                new ViewWrapper(new HtmlScrollTextView(this, R.string.README_about_html)),
                new ViewWrapper(new HtmlScrollTextView(this, R.string.README_enduser_html)),
        };

        return new MultiView(this, SOLID_KEY, GpxInformation.ID.INFO_ID_ALL, multiViewLayout);
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