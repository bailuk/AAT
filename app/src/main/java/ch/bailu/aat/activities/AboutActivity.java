package ch.bailu.aat.activities;

import android.os.Bundle;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.HtmlScrollTextView;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.description.MultiView;


public class AboutActivity extends AbsDispatcher {

    private final static String SOLID_KEY = AboutActivity.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViews();
    }


    private void createViews() {
        MultiView multiView = createMultiView();
        LinearLayout contentView = new ContentView(this);

        contentView.addView(createButtonBar(multiView));
        contentView.addView(multiView);

        setContentView(contentView);
    }

    private LinearLayout createButtonBar(MultiView mv) {
        final MainControlBar bar = new MainControlBar(getServiceContext());

        bar.addAll(mv);
        return bar;
    }


    private MultiView createMultiView() {
        MultiView mv = new MultiView(this,
                SOLID_KEY,
                GpxInformation.ID.INFO_ID_ALL);
        mv.add(new HtmlScrollTextView(this, R.string.README_about_html),
                getString(R.string.intro_about));
        mv.add(new HtmlScrollTextView(this, R.string.README_enduser_html),
                getString(R.string.intro_readme));

        return mv;
    }

}