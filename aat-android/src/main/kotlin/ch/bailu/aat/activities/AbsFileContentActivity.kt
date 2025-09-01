package ch.bailu.aat.activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import ch.bailu.aat.R
import ch.bailu.aat.menus.FileMenu
import ch.bailu.aat.util.ui.AppDialog
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.busy.BusyViewContainer
import ch.bailu.aat.views.busy.BusyViewControlIID
import ch.bailu.aat.views.html.AttributesView
import ch.bailu.aat.views.image.ImageButtonViewGroup
import ch.bailu.aat.views.image.PreviewView
import ch.bailu.aat.views.layout.ContentView
import ch.bailu.aat_lib.dispatcher.source.EditorOrBackupSource
import ch.bailu.aat_lib.dispatcher.source.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.source.IteratorSource
import ch.bailu.aat_lib.dispatcher.source.IteratorSource.FollowFile
import ch.bailu.aat_lib.dispatcher.source.TrackerSource
import ch.bailu.aat_lib.dispatcher.source.addOverlaySources
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerAlwaysEnabled
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackers
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.information.InformationUtil
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapViewInterface
import ch.bailu.aat_lib.util.Objects

abstract class AbsFileContentActivity : ActivityContext(), View.OnClickListener {
    private var currentFile: IteratorSource? = null
    private var nextFile: ImageButtonViewGroup? = null
    private var previousFile: ImageButtonViewGroup? = null
    private var fileOperation: PreviewView? = null
    private var busyControl: BusyViewControlIID? = null

    private var currentFileID: String? = null

    companion object {
        val THEME: UiTheme = AppTheme.trackContent
    }

    protected var map: MapViewInterface? = null

    private var editorSourcePrivate: EditorOrBackupSource? = null

    val editorSource: EditorOrBackupSource
        get() = editorSourcePrivate!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentFile = FollowFile(appContext)
        this.currentFile = currentFile
        editorSourcePrivate = EditorOrBackupSource(appContext, currentFile)
        createViews()
        createDispatcher()
    }

    private fun createViews() {
        val contentView = ContentView(this, THEME)
        val bar = MainControlBar(this, button = 5)
        val layout = createLayout(bar, contentView)

        contentView.add(bar)
         busyControl = BusyViewControlIID(contentView).apply {
            busy.setOrientation(BusyViewContainer.BOTTOM_RIGHT)
        }
        contentView.add(layout)
        initButtonBar(bar)
        setContentView(contentView)
    }

    protected fun createAttributesView(): View {
        val v = AttributesView(this, appContext.storage)
        dispatcher.addTarget(v, InfoID.FILE_VIEW, InfoID.EDITOR_OVERLAY)
        return v
    }

    private fun initButtonBar(bar: MainControlBar) {
        previousFile = bar.addImageButton(R.drawable.go_up_inverse)
        nextFile = bar.addImageButton(R.drawable.go_down_inverse)

        val fileOperation = PreviewView(
            serviceContext,
            appContext.summaryConfig
        )
        this.fileOperation = fileOperation
        bar.addButton(fileOperation)
        bar.orientation = LinearLayout.HORIZONTAL
        bar.addOnClickListener(this)
    }

    protected abstract fun createLayout(bar: MainControlBar, contentView: ContentView): ViewGroup

    private fun createDispatcher() {
        dispatcher.addSource(TrackerSource(serviceContext, appContext.broadcaster, UsageTrackerAlwaysEnabled()))
        dispatcher.addSource(CurrentLocationSource(serviceContext, appContext.broadcaster))
        dispatcher.addOverlaySources(appContext, UsageTrackers().createOverlayUsageTracker(appContext.storage, *InformationUtil.getOverlayInfoIdList().toIntArray()))
        dispatcher.addSource(editorSource)

        busyControl?.apply {
            dispatcher.addTarget(this,InfoID.FILE_VIEW,*InformationUtil.getOverlayInfoIdList().toIntArray())
        }
        fileOperation?.apply { dispatcher.addTarget(this, InfoID.FILE_VIEW) }

        dispatcher.addTarget({ _, info ->
            val newFileID = info.getFile().toString()
            if (!Objects.equals(currentFileID, newFileID)) {
                currentFileID = newFileID
                map?.frameBounding(info.getBoundingBox())
                AppLog.i(this@AbsFileContentActivity, info.getFile().name)
            }
        }, InfoID.FILE_VIEW)
    }

    override fun onClick(v: View) {
        if (v === previousFile || v === nextFile) {
            changeFileAsk(v)
        } else if (v === fileOperation) {
            currentFile?.apply { FileMenu(this@AbsFileContentActivity, getInfo().getFile()).showAsPopup(this@AbsFileContentActivity, v) }
        }
    }

    private fun changeFileAsk(view: View) {
        editorSourcePrivate?.apply {
            if (isModified) {
                object : AppDialog() {
                    override fun onPositiveClick() {
                        releaseEditorSave()
                        changeFile(view)
                    }

                    override fun onNeutralClick() {
                        releaseEditorDiscard()
                        changeFile(view)
                    }
                }.displaySaveDiscardDialog(this@AbsFileContentActivity, file.name)
            } else {
                changeFile(view)
            }

        }
    }

    private fun changeFile(v: View) {
        if (v === previousFile) {
            editorSourcePrivate?.apply { releaseEditorDiscard() }
            currentFile?.apply { moveToPrevious() }
        } else if (v === nextFile) {
            editorSourcePrivate?.apply { releaseEditorDiscard() }
            currentFile?.apply { moveToNext() }
        }
    }

    @Deprecated("Android API")
    override fun onBackPressed() {
        editorSourcePrivate?.apply {
            try {
                if (isModified) {
                    object : AppDialog() {
                        override fun onPositiveClick() {
                            releaseEditorSave()
                            closeActivity()
                        }

                        override fun onNeutralClick() {
                            releaseEditorDiscard()
                            closeActivity()
                        }
                    }.displaySaveDiscardDialog(this@AbsFileContentActivity, file.name)
                } else {
                    closeActivity()
                }
            } catch (e: Exception) {
                AppLog.e(this@AbsFileContentActivity, e)
                closeActivity()
            }
        }
    }

    private fun closeActivity() {
        @Suppress("DEPRECATION")
        super.onBackPressed()
    }
}
