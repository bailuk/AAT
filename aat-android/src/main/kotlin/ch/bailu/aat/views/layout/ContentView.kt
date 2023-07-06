package ch.bailu.aat.views.layout

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import ch.bailu.aat.app.ActivitySwitcher
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.description.mview.MultiView
import ch.bailu.aat.views.description.mview.MultiViewIndicator
import ch.bailu.aat.views.msg.AbsMsgView
import ch.bailu.aat.views.msg.overlay.DownloadMsgView
import ch.bailu.aat.views.msg.overlay.DownloadSizeMsgView
import ch.bailu.aat.views.msg.overlay.FileChangeMsgView
import ch.bailu.aat.views.msg.overlay.InfoLogMsgView
import ch.bailu.aat.views.msg.overlay.MessageOverlay
import ch.bailu.aat.views.msg.overlay.TipMsgView
import ch.bailu.aat_lib.preferences.system.SolidStatusMessages

class ContentView(context: Context, theme: UiTheme) : FrameLayout(context) {
    private val mainContent: LinearLayout
    private val messages: MessageOverlay
    private val ttBottom: AbsMsgView
    private val ttTop: AbsMsgView

    init {
        theme.background(this)
        mainContent = LinearLayout(context)
        mainContent.orientation = LinearLayout.VERTICAL
        addView(mainContent)
        messages = MessageOverlay(context)
        addView(messages)
        val smessages = SolidStatusMessages(
            Storage(
                context
            )
        )
        if (smessages.showURL()) {
            messages.add(DownloadMsgView(context))
        }
        if (smessages.showPath()) {
            messages.add(FileChangeMsgView(context))
        }
        if (smessages.showSummary()) {
            messages.add(DownloadSizeMsgView(context))
        }
        ttTop = messages.add(InfoLogMsgView(context))
        messages.addSpace()
        ttBottom = messages.addR(TipMsgView(context))
        logActivityLabel()
    }

    private fun logActivityLabel() {
        val e = ActivitySwitcher[context]
        if (e != null) {
            ttTop.set(e.activityLabel)
        }
    }

    fun add(view: View) {
        mainContent.addView(view)
        messages.bringToFront()
    }

    fun addW(v: View) {
        add(v)
        val l = v.layoutParams as LinearLayout.LayoutParams
        l.weight = 1f
        v.layoutParams = l
    }

    fun addMvIndicator(mv: MultiView) {
        messages.addView(MultiViewIndicator(mv), 0)
    }

    fun showTip(string: String) {
        ttBottom.set(string)
    }
}
