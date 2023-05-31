package ch.bailu.aat.activities;

import android.os.Bundle;
import android.widget.LinearLayout;

import ch.bailu.aat.BuildConfig;
import ch.bailu.aat.app.AndroidAppConfig;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.description.mview.MultiView;
import ch.bailu.aat.views.html.HtmlScrollTextView;
import ch.bailu.aat_lib.app.AppConfig;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.resources.ToDo;
import ch.bailu.aat_lib.util.fs.FocUtil;
import ch.bailu.foc_android.FocAsset;


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
        contentView.add(getErrorView());
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

        final HtmlScrollTextView about = new HtmlScrollTextView(this,
                        getInfoText());

        final HtmlScrollTextView readme = new HtmlScrollTextView(this,
                assetToStr("documentation/manual.html"));

        mv.add(readme, Res.str().intro_readme());
        mv.add(about, Res.str().intro_about());

        status = new HtmlScrollTextView(this);
        mv.add(status, ToDo.translate("Status"));

        status.themify(THEME);
        readme.themify(THEME);
        about.themify(THEME);
        THEME.background(mv);
        return mv;
    }

    private String getInfoText() {
        var config = AppConfig.getInstance();
        return "<h1>" +
                config.getLongName() +
                "</h1><p>" +
                config.getVersionName() +
                "</p><p><a href=\"" +
                config.getWebsite() +
                "\">Website</a></p><p>" +
                config.getCopyright() +
                "</p>";
    }

    private String assetToStr(String asset) {
        return FocUtil.toStr(new FocAsset(getAssets(),asset));
    }

    @Override
    public void onResumeWithService() {
        if (status != null) {
            final StringBuilder builder = new StringBuilder();

            new AndroidAppConfig().appendStatusText(builder);

            if (BuildConfig.DEBUG) {
                new AppThread().appendStatusText(builder);
            }

            getServiceContext().appendStatusText(builder);

            status.setHtmlText(builder.toString());
        }
    }
}
