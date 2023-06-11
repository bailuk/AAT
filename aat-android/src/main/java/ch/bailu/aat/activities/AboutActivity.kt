package ch.bailu.aat.activities

import android.os.Bundle
import android.widget.LinearLayout
import ch.bailu.aat.BuildConfig
import ch.bailu.aat.app.AndroidAppConfig
import ch.bailu.aat.app.AppThread
import ch.bailu.aat.util.ui.AppTheme
import ch.bailu.aat.views.ContentView
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.description.mview.MultiView
import ch.bailu.aat.views.html.HtmlScrollTextView
import ch.bailu.aat_lib.app.AppConfig
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.fs.FocUtil
import ch.bailu.foc_android.FocAsset

class AboutActivity : ActivityContext() {
    private var status: HtmlScrollTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createViews()
    }

    private fun createViews() {
        val multiView = createMultiView()
        val contentView = ContentView(this, THEME)
        contentView.addMvIndicator(multiView)
        contentView.add(createButtonBar(multiView))
        contentView.add(errorView)
        contentView.add(multiView)
        setContentView(contentView)
    }

    private fun createButtonBar(mv: MultiView): LinearLayout {
        val bar = MainControlBar(this)
        bar.addAll(mv)
        return bar
    }

    private fun createMultiView(): MultiView {
        val mv = MultiView(this, SOLID_KEY)
        val about = HtmlScrollTextView(
            this,
            infoText
        )
        val readme = HtmlScrollTextView(
            this,
            assetToStr("documentation/manual.html")
        )
        mv.add(readme, Res.str().intro_readme())
        mv.add(about, Res.str().intro_about())
        status = HtmlScrollTextView(this).apply {
            mv.add(this, Res.str().intro_status())
            themify(THEME)
        }
        readme.themify(THEME)
        about.themify(THEME)
        THEME.background(mv)
        return mv
    }

    private val infoText: String
        get() {
            val config = AppConfig.getInstance()
            return "<h1>${config.longName}</h1><p>${config.versionName}</p><p><a href=\"${config.website}\">Website</a></p><p>${config.copyright}</p>"
        }

    private fun assetToStr(asset: String): String {
        return FocUtil.toStr(FocAsset(assets, asset))
    }

    override fun onResumeWithService() {
        status?.apply {
            val builder = StringBuilder()
            AndroidAppConfig().appendStatusText(builder)
            if (BuildConfig.DEBUG) {
                AppThread().appendStatusText(builder)
            }
            serviceContext.appendStatusText(builder)
            setHtmlText(builder.toString())
        }
    }

    companion object {
        private val SOLID_KEY = AboutActivity::class.java.simpleName
        private val THEME = AppTheme.doc
    }
}
