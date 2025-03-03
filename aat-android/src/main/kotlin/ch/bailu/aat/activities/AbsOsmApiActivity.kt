package ch.bailu.aat.activities

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import ch.bailu.aat.R
import ch.bailu.aat.menus.ResultFileMenu
import ch.bailu.aat.util.AppIntent
import ch.bailu.aat_lib.util.fs.TextBackup
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.util.ui.tooltip.ToolTip
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.busy.BusyViewControl
import ch.bailu.aat.views.image.ImageButtonViewGroup
import ch.bailu.aat.views.layout.ContentView
import ch.bailu.aat.views.list.NodeListView
import ch.bailu.aat.views.osm.OsmApiEditorView
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.dispatcher.source.FileViewSource
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerAlwaysEnabled
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.search.poi.OsmApiConfiguration

abstract class AbsOsmApiActivity : ActivityContext(), View.OnClickListener {
    private var download: ImageButtonViewGroup? = null
    private var downloadBusy: BusyViewControl? = null
    private var fileMenu: View? = null
    private var list: NodeListView? = null
    protected var configuration: OsmApiConfiguration? = null
        private set
    private var boundingBox: BoundingBoxE6 = BoundingBoxE6()

    protected var editorView: OsmApiEditorView? = null

    protected val theme: UiTheme = AppTheme.search

    private val onFileTaskChanged = BroadcastReceiver { setDownloadStatus() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        boundingBox = AppIntent.getBoundingBox(intent)
        configuration = createApiConfiguration()
        setContentView(createContentView())

        val configuration = configuration
        val list = list

        if (configuration is OsmApiConfiguration && list is NodeListView) {
            dispatcher.addSource(FileViewSource(appContext, UsageTrackerAlwaysEnabled()).apply {
                setFile(configuration.resultFile)
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
        if (configuration!!.isTaskRunning(serviceContext)) {
            downloadBusy!!.startWaiting()
        } else {
            downloadBusy!!.stopWaiting()
        }
    }

    private fun addButtons(bar: MainControlBar) {
        fileMenu = bar.addImageButton(R.drawable.edit_select_all_inverse)
    }

    protected open fun createMainContentView(contentView: ContentView): View {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(createEditorView())
        layout.addView(createNodeListView(contentView))
        return layout
    }

    protected open fun createNodeListView(contentView: ContentView): View {
        val list = NodeListView(this)
        this.list = list
        return list
    }

    private fun createEditorView(): View {
        val editorView = OsmApiEditorView(
            this,
            boundingBox,
            configuration!!,
            theme
        )
        this.editorView = editorView
        return editorView
    }

    private fun createControlBar(): MainControlBar {
        val bar = MainControlBar(this)
        bar.addOnClickListener(this)
        return bar
    }

    protected abstract fun createApiConfiguration(): OsmApiConfiguration
    protected abstract fun addCustomButtons(bar: MainControlBar)
    override fun onClick(view: View) {
        if (view === download) {
            download()
        } else if (view === fileMenu) {
            showFileMenu(view)
        }
    }

    private fun download() {
        configuration?.apply {
            if (isTaskRunning(serviceContext)) {
                stopTask(serviceContext)
            } else {
                startTask(appContext, boundingBox)
            }
        }
    }

    private fun showFileMenu(parent: View) {
        val targetPrefix = targetFilePrefix

        configuration?.apply {
            val targetExtension = fileExtension
            ResultFileMenu(this@AbsOsmApiActivity, resultFile, targetPrefix, targetExtension).showAsPopup(this@AbsOsmApiActivity, parent)
        }
    }

    private val targetFilePrefix: String
        get() = try {
            val query = TextBackup.read(configuration!!.queryFile)
            OsmApiConfiguration.getFilePrefix(query)

        } catch (e: Exception) {
            OsmApiConfiguration.getFilePrefix("")
        }

    protected fun insertLine(line: String) {
        editorView?.insertLine(line)
    }

    override fun onDestroy() {
        appContext.broadcaster.unregister(onFileTaskChanged)
        super.onDestroy()
    }
}
