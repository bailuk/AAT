package ch.bailu.aat.map.layer

import android.content.Context
import android.view.View
import ch.bailu.aat.R
import ch.bailu.aat.activities.AbsGpxListActivity
import ch.bailu.aat.menus.FileMenu
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.fs.AndroidFileAction
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.util.ui.tooltip.ToolTip
import ch.bailu.aat.views.bar.ControlBar
import ch.bailu.aat.views.image.PreviewView
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.description.AverageSpeedDescription
import ch.bailu.aat_lib.description.CaloriesDescription
import ch.bailu.aat_lib.description.DateDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.description.MaximumSpeedDescription
import ch.bailu.aat_lib.description.TimeDescription
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidCustomOverlay
import ch.bailu.aat_lib.service.directory.Iterator
import ch.bailu.aat_lib.service.directory.SummaryConfig
import ch.bailu.aat_lib.util.Point
import ch.bailu.aat_lib.util.fs.FileAction
import ch.bailu.foc.Foc
import ch.bailu.foc_android.FocAndroidFactory

class FileControlBarLayer(
    appContext: AppContext,
    mc: MapContext,
    private val acontext: AbsGpxListActivity,
    config: SummaryConfig
) : ControlBarLayer(
    mc, ControlBar(
        acontext,
        getOrientation(Position.LEFT), AppTheme.bar
    ), Position.LEFT
) {
    private val preview =
        PreviewView(acontext.serviceContext, config)
    private val selector = FileViewLayer(appContext, acontext, mc)
    private val overlay: View = bar.addImageButton(R.drawable.view_paged)
    private val reloadPreview: View = bar.addImageButton(R.drawable.view_refresh)
    private val delete: View = bar.addImageButton(R.drawable.user_trash)
    private var iterator = Iterator.NULL
    private var selectedFile: Foc? = null

    init {
        bar.add(preview)
        preview.setOnClickListener(this)
        ToolTip.set(preview, R.string.tt_menu_file)
        ToolTip.set(overlay, R.string.file_overlay)
        ToolTip.set(reloadPreview, R.string.file_reload)
        ToolTip.set(delete, R.string.file_delete)
        acontext.addTarget(selector, InfoID.LIST_SUMMARY)
    }

    fun setIterator(i: Iterator) {
        iterator = i
    }

    override fun onShowBar() {
        selector.showAtRight()
    }

    override fun onAttached() {}
    override fun onDetached() {}
    override fun drawForeground(mcontext: MapContext) {
        if (isBarVisible) {
            selector.drawForeground(mcontext)
        }
    }

    override fun drawInside(mcontext: MapContext) {
        if (isBarVisible) {
            selector.drawInside(mcontext)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        selector.onLayout(changed, l, t, r, b)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        val node = selector.getSelectedNode()
        if (node != null && selectedFile != null) {
            val file = selectedFile
            if (file is Foc && file.exists()) {
                if (v === preview) {
                    FileMenu(acontext, file).showAsPopup(acontext, v)
                } else if (v === overlay) {
                    AndroidFileAction.useAsOverlay(acontext, file)
                } else if (v === reloadPreview) {
                    FileAction.reloadPreview(acontext.appContext, file)
                } else if (v === delete) {
                    AndroidFileAction.delete(acontext.appContext, acontext, file)
                }
            }
        }
    }

    override fun onHideBar() {
        selector.hide()
    }

    private inner class FileViewLayer(appContext: AppContext, context: Context, mc: MapContext) :
        AbsNodeViewLayer(appContext, context, mc) {

        private val storage = appContext.storage

        val summaryData = arrayOf(
            DateDescription(),
            TimeDescription(),
            DistanceDescription(storage),
            AverageSpeedDescription(storage),
            MaximumSpeedDescription(storage),
            CaloriesDescription(storage)
        )

        override fun setSelectedNode(iid: Int, info: GpxInformation, node: GpxPointNode, index: Int) {
            super.setSelectedNode(iid, info, node, index)
            SolidDirectoryQuery(Storage(acontext), FocAndroidFactory(acontext)).position.setValue(index)
            iterator.moveToPosition(index)
            selectedFile = iterator.info.file
            val file = selectedFile
            if (file is Foc) {
                preview.setFilePath(file)
            }
            markupBuilder.appendHeader(iterator.info.file.name)
            for (d in summaryData) {
                d.onContentUpdated(iterator.infoID, iterator.info)
                markupBuilder.appendNl(d)
            }
            setHtmlText(markupBuilder)
        }

        override fun onClick(v: View) {
            acontext.displayFile()
        }

        override fun onPreferencesChanged(storage: StorageInterface, key: String) {
            selector.onPreferencesChanged(storage, key)
        }

        override fun onAttached() {}
        override fun onDetached() {}
        override fun onLongClick(view: View): Boolean {
            val file = selectedFile
            if (file is Foc) {
                SolidCustomOverlay(
                    Storage(acontext),
                    FocAndroidFactory(acontext),
                    0
                ).setValueFromFile(file)
                return true
            }
            return false
        }

        override fun onTap(tapPos: Point): Boolean {
            return false
        }
    }
}
