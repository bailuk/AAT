package ch.bailu.aat.activities;

import android.os.Bundle;
import android.widget.LinearLayout;

import ch.bailu.aat.BuildConfig;
import ch.bailu.aat.R;
import ch.bailu.aat.util.ToDo;
import ch.bailu.aat.util.fs.foc.FocAsset;
import ch.bailu.aat.util.ui.AppConfig;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.mview.MultiView;
import ch.bailu.aat.views.html.HtmlScrollTextView;
import ch.bailu.aat.views.html.LinkHandler;
import ch.bailu.util_java.util.FUtil;
import ch.bailu.util_java.util.Objects;


public class AboutActivity extends ActivityContext {
    private final static String SOLID_KEY = AboutActivity.class.getSimpleName();


    private final static UiTheme THEME = AppTheme.doc;


    private HtmlScrollTextView status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViews();
    }



    private void createViews() {
        MultiView multiView = createMultiView();
        ContentView contentView = new ContentView(this, THEME);

        contentView.addMvIndicator(multiView);
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
                if (url.contains("README.end")) {
                    mv.setActive(1);
                    return true;
                }
                return false;
            }
        };

        final HtmlScrollTextView about = new HtmlScrollTextView(this,
                        toStr("documentation/README.about.html"),
                        linkHandler);

        final HtmlScrollTextView readme = new HtmlScrollTextView(this,
                toStr("documentation/README.enduser.html"));



        mv.add(about, getString(R.string.intro_about));
        mv.add(readme, getString(R.string.intro_readme));

        status = new HtmlScrollTextView(this);
        mv.add(status, ToDo.translate("Status"));

        status.themify(THEME);
        readme.themify(THEME);
        about.themify(THEME);
        THEME.background(mv);
        return mv;
    }

    private String toStr(String asset) {
        return FUtil.toStr(new FocAsset(getAssets(),asset));
    }

    @Override
    public void onResumeWithService() {
        if (status != null) {
            final StringBuilder builder = new StringBuilder();

            new AppConfig().appendStatusText(builder);

            if (BuildConfig.DEBUG) {
                new AppThread().appendStatusText(builder);
            }

            getServiceContext().appendStatusText(builder);

            status.setHtmlText(builder.toString());
        }
    }
}