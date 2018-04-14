package ch.bailu.aat.activities;

import android.os.Bundle;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.util.fs.foc.FocAsset;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.HtmlScrollTextView;
import ch.bailu.aat.views.bar.MainControlBar;
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
        ContentView contentView = new ContentView(this);

        contentView.add(createButtonBar(multiView));
        contentView.add(multiView);

        setContentView(contentView);
    }

    private LinearLayout createButtonBar(MultiView mv) {
        final MainControlBar bar = new MainControlBar(this);

        bar.addAll(mv);
        return bar;
    }


    private MultiView createMultiView() {
        MultiView mv = new MultiView(this, SOLID_KEY);
        mv.add(new HtmlScrollTextView(this,
                new FocAsset(getAssets(),"documentation/README.about.html")),
                getString(R.string.intro_about));

        mv.add(new HtmlScrollTextView(this,
                new FocAsset(getAssets(),"documentation/README.enduser.html")),
                getString(R.string.intro_readme));

        return mv;
    }
}