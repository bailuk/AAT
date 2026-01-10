package ch.bailu.aat.activities

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import ch.bailu.aat.R
import ch.bailu.aat.api.OsmApiController
import ch.bailu.aat.menus.ResultFileMenu
import ch.bailu.aat.util.AppIntent
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.util.ui.tooltip.ToolTip
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.busy.BusyViewControl
import ch.bailu.aat.views.image.ImageButtonViewGroup
import ch.bailu.aat.views.layout.ContentView
import ch.bailu.aat.views.list.NodeListView
import ch.bailu.aat.views.osm.OsmApiEditorView
import ch.bailu.aat_lib.api.ApiConfiguration
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.dispatcher.source.FileViewSource
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerAlwaysEnabled
import ch.bailu.aat_lib.gpx.information.InfoID

abstract class AbsOsmApiActivity : ActivityContext(), View.OnClickListener {
    private var download: ImageButtonViewGroup? = null
    private var downloadBusy: BusyViewControl? = null
    private var fileMenu: View? = null
    private var list: NodeListView? = null

    protected var editorView: OsmApiEditorView? = null
    protected var controller: OsmApiController? = null
    protected val theme: UiTheme = AppTheme.search

    private val onFileTaskChanged = BroadcastReceiver { setDownloadStatus() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val boundingBox = AppIntent.getBoundingBox(intent)
        val api = createApiConfiguration()
        controller = OsmApiController(api, appContext, boundingBox)
        setContentView(createContentView())

        val list = list
        if (list is NodeListView) {
            dispatcher.addSource(FileViewSource(appContext, UsageTrackerAlwaysEnabled()).apply {
                setFile(api.resultFile)
            })
            dispatcher.addTarget(list, InfoID.FILE_VIEW)

            appContext.broadcaster.register(
                AppBroadcaster.FILE_BACKGROUND_TASK_CHANGED,
                onFileTaskChanged
            )
        }
    }

    private fun createContentView(): View {
        val bar = createControlBar()
        val contentView = ContentView(this, theme)
        contentView.add(bar)
        contentView.add(createMainContentView(contentView))
        addDownloadButton(bar)
        addCustomButtons(bar)
        addButtons(bar)
        return contentView
    }


    private fun addDownloadButton(bar: MainControlBar) {
        download = bar.addImageButton(R.drawable.go_bottom_inverse).apply {
            downloadBusy = BusyViewControl(this)
            setOnClickListener(this@AbsOsmApiActivity)
            ToolTip.set(this, R.string.tt_nominatim_query)
        }
        setDownloadStatus()
    }

    private fun setDownloadStatus() {
        controller?.apply {
            if (api.isTaskRunning(serviceContext)) {
                downloadBusy?.startWaiting()
            } else {
                downloadBusy?.stopWaiting()
            }
        }
    }

    private fun addButtons(bar: MainControlBar) {
        fileMenu = bar.addImageButton(R.drawable.edit_select_all_inverse)
    }

    protected open fun createMainContentView(contentView: ContentView): View {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        controller?.apply {
            layout.addView(createEditorView(this))
        }
        layout.addView(createNodeListView(contentView))
        return layout
    }

    protected open fun createNodeListView(contentView: ContentView): View {
        val list = NodeListView(this)
        this.list = list
        return list
    }

    private fun createEditorView(controller: OsmApiController): View {
        val editorView = OsmApiEditorView(this, controller.boundingBox, controller.api, theme)
        this.editorView = editorView
        return editorView
    }

    private fun createControlBar(): MainControlBar {
        val bar = MainControlBar(this)
        bar.addOnClickListener(this)
        return bar
    }

    protected abstract fun createApiConfiguration(): ApiConfiguration
    protected abstract fun addCustomButtons(bar: MainControlBar)
    override fun onClick(view: View) {
        if (view === download) {
            controller?.download()
        } else if (view === fileMenu) {
            showFileMenu(view)
        }
    }

    private fun showFileMenu(parent: View) {
        controller?.apply {
            ResultFileMenu(this@AbsOsmApiActivity, api.resultFile).showAsPopup(this@AbsOsmApiActivity, parent)
        }
    }

    protected fun insertLine(line: String) {
        editorView?.insertLine(line)
    }

    override fun onDestroy() {
        appContext.broadcaster.unregister(onFileTaskChanged)
        super.onDestroy()
    }
}
