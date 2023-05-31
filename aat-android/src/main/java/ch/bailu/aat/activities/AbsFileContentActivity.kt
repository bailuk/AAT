package ch.bailu.aat.activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import ch.bailu.aat.R
import ch.bailu.aat.menus.FileMenu
import ch.bailu.aat.util.ui.AppDialog
import ch.bailu.aat.util.ui.AppTheme
import ch.bailu.aat.util.ui.UiTheme
import ch.bailu.aat.views.BusyViewContainer
import ch.bailu.aat.views.BusyViewControlIID
import ch.bailu.aat.views.ContentView
import ch.bailu.aat.views.ImageButtonViewGroup
import ch.bailu.aat.views.PreviewView
import ch.bailu.aat.views.bar.MainControlBar
import ch.bailu.aat.views.html.AttributesView
import ch.bailu.aat.views.msg.ErrorMsgView
import ch.bailu.aat_lib.dispatcher.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.EditorOrBackupSource
import ch.bailu.aat_lib.dispatcher.IteratorSource
import ch.bailu.aat_lib.dispatcher.IteratorSource.FollowFile
import ch.bailu.aat_lib.dispatcher.OverlaysSource
import ch.bailu.aat_lib.dispatcher.TrackerSource
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.MapViewInterface
import ch.bailu.aat_lib.util.Objects

abstract class AbsFileContentActivity : ActivityContext(), View.OnClickListener {
    private var currentFile: IteratorSource? = null
    private var nextFile: ImageButtonViewGroup? = null
    private var previousFile: ImageButtonViewGroup? = null
    private var fileOperation: PreviewView? = null
    private var fileError: ErrorMsgView? = null
    private var busyControl: BusyViewControlIID? = null

    private var currentFileID: String? = null

    companion object {
        @JvmField
        protected val THEME: UiTheme = AppTheme.trackContent
    }

    @JvmField
    protected var map: MapViewInterface? = null

    @JvmField
    protected var editorSource: EditorOrBackupSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentFile = FollowFile(appContext)
        editorSource = EditorOrBackupSource(appContext, currentFile)
        createViews()
        createDispatcher()
    }

    private fun createViews() {
        val contentView = ContentView(this, THEME)
        val bar = MainControlBar(this, 5)
        val layout = createLayout(bar, contentView)

        contentView.add(bar)
        fileError = ErrorMsgView(this)
        contentView.add(fileError)
        contentView.add(errorView)
        busyControl = BusyViewControlIID(contentView).apply {
            busy.setOrientation(BusyViewContainer.BOTTOM_RIGHT)
        }
        contentView.add(layout)
        initButtonBar(bar)
        setContentView(contentView)
    }

    protected fun createAttributesView(): View {
        val v = AttributesView(this, appContext.storage)
        addTarget(v, InfoID.FILEVIEW, InfoID.EDITOR_OVERLAY)
        return v
    }

    private fun initButtonBar(bar: MainControlBar) {
        previousFile = bar.addImageButton(R.drawable.go_up_inverse)
        nextFile = bar.addImageButton(R.drawable.go_down_inverse)
        fileOperation = PreviewView(serviceContext, appContext.summaryConfig)
        bar.addButton(fileOperation)
        bar.orientation = LinearLayout.HORIZONTAL
        bar.setOnClickListener1(this)
    }

    protected abstract fun createLayout(bar: MainControlBar?, contentView: ContentView?): ViewGroup

    private fun createDispatcher() {
        addSource(TrackerSource(serviceContext, appContext.broadcaster))
        addSource(CurrentLocationSource(serviceContext, appContext.broadcaster))
        addSource(OverlaysSource(appContext))
        addSource(editorSource!!)
        addTarget(
            busyControl!!,
            InfoID.FILEVIEW,
            InfoID.OVERLAY,
            InfoID.OVERLAY + 1,
            InfoID.OVERLAY + 2,
            InfoID.OVERLAY + 3
        )
        addTarget(fileOperation!!, InfoID.FILEVIEW)
        addTarget({ _: Int, info: GpxInformation ->
            val newFileID = info.file.toString()
            if (!Objects.equals(currentFileID, newFileID)) {
                currentFileID = newFileID
                map?.frameBounding(info.boundingBox)
                AppLog.i(this@AbsFileContentActivity, info.file.name)
            }
        }, InfoID.FILEVIEW)
        addTarget({ _: Int, info: GpxInformation -> fileError?.displayError(serviceContext, info.file)
        }, InfoID.FILEVIEW)
    }

    override fun onClick(v: View) {
        if (v === previousFile || v === nextFile) {
            changeFileAsk(v)
        } else if (v === fileOperation) {
            currentFile?.apply { FileMenu(this@AbsFileContentActivity, info.file).showAsPopup(this@AbsFileContentActivity, v) }
        }
    }

    private fun changeFileAsk(view: View) {
        editorSource?.apply {
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
            editorSource?.apply { releaseEditorDiscard() }
            currentFile?.apply { moveToPrevious() }
        } else if (v === nextFile) {
            editorSource?.apply { releaseEditorDiscard() }
            currentFile?.apply { moveToNext() }
        }
    }

    override fun onBackPressed() {
        editorSource?.apply {
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
        super.onBackPressed()
    }
}
