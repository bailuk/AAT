package ch.bailu.aat.activities;

import android.os.Bundle;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.util.fs.foc.FocAsset;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.html.HtmlScrollTextView;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.html.LinkHandler;
import ch.bailu.util_java.util.FUtil;


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
        final MultiView mv = new MultiView(this, SOLID_KEY);
        final LinkHandler linkHandler = new LinkHandler() {
            @Override
            public boolean openLink(String url) {
                if (url.contains("README.get")) {
                    mv.setActive(1);
                    return true;
                }

                if (url.contains("README.end")) {
                    mv.setActive(2);
                    return true;
                }
                return false;
            }
        };

        mv.add(new HtmlScrollTextView(this,
                        toStr("documentation/README.about.html"),
                        linkHandler),
                getString(R.string.intro_about));

        mv.add(new HtmlScrollTextView(this,
                        toStr("documentation/README.gettingstarted.html")),
                getString(R.string.intro_uiguide));

        mv.add(new HtmlScrollTextView(this,
                toStr("documentation/README.enduser.html")),
                getString(R.string.intro_readme));

        return mv;
    }

    private String toStr(String asset) {
        return FUtil.toStr(new FocAsset(getAssets(),asset));
    }
}