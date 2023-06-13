package ch.bailu.aat.activities

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import ch.bailu.aat.R
import ch.bailu.aat.menus.ResultFileMenu
import ch.bailu.aat.util.AppIntent
import ch.bailu.aat.util.TextBackup
import ch.bailu.aat.util.ui.AppTheme
import ch.bailu.aat.util.ui.ToolTip
import ch.bailu.aat.util.ui.UiTheme
import ch.bailu.aat.views.BusyViewControl
import ch.bailu.aat.views.ContentView
import ch.bailu.aat.views.ImageButtonViewGroup
import ch.bailu.aat.views.NodeListView
import ch.bailu.aat.views.OsmApiEditorView
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.msg.ErrorMsgView
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.dispatcher.FileViewSource
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.search.poi.OsmApiConfiguration

abstract class AbsOsmApiActivity : ActivityContext(), View.OnClickListener {
    private var download: ImageButtonViewGroup? = null
    private var downloadBusy: BusyViewControl? = null
    private var fileMenu: View? = null
    private var list: NodeListView? = null
    protected var configuration: OsmApiConfiguration? = null
        private set

    protected var editorView: OsmApiEditorView? = null
    private var downloadError: ErrorMsgView? = null

    protected val theme: UiTheme = AppTheme.search

    private val onFileTaskChanged: (Array<String>)->Unit = {
        setDownloadStatus()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configuration = createApiConfiguration(AppIntent.getBoundingBox(intent))
        setContentView(createContentView())
        addSource(FileViewSource(appContext, configuration!!.resultFile))
        addTarget(list!!, InfoID.FILEVIEW)

        appContext.broadcaster.register(
            onFileTaskChanged,
            AppBroadcaster.FILE_BACKGROND_TASK_CHANGED
        )
    }

    private fun createContentView(): View {
        val bar = createControlBar()
        val contentView = ContentView(this, theme)
        contentView.add(bar)
        contentView.add(downloadErrorView())
        contentView.add(fileErrorView())
        contentView.add(errorView)
        contentView.add(createMainContentView(contentView))
        addDownloadButton(bar)
        addCustomButtons(bar)
        addButtons(bar)
        return contentView
    }

    private fun downloadErrorView(): View {
        downloadError = ErrorMsgView(this)
        return downloadError!!
    }

    private fun fileErrorView(): View {
        val fileError = ErrorMsgView(this)
        addTarget(
            { iid: Int, info: GpxInformation -> fileError.displayError(serviceContext, info.file) },
            InfoID.FILEVIEW
        )
        return fileError
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
        downloadError!!.displayError(configuration!!.exception)
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
        val editorView = OsmApiEditorView(this, configuration, theme)
        this.editorView = editorView
        return editorView
    }

    private fun createControlBar(): MainControlBar {
        val bar = MainControlBar(this)
        bar.setOnClickListener1(this)
        return bar
    }

    protected abstract fun createApiConfiguration(boundingBox: BoundingBoxE6): OsmApiConfiguration
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
                startTask(appContext)
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
