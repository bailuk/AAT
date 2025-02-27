package ch.bailu.aat.views.image

import ch.bailu.aat.R
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.util.ui.tooltip.ToolTip
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.service.cache.ObjBitmap
import ch.bailu.aat_lib.service.directory.SummaryConfig
import ch.bailu.foc.Foc

class PreviewView(sc: ServiceContext, private val summaryConfig: SummaryConfig) : ImageObjectView(sc, R.drawable.open_menu_light), TargetInterface {
    init {
        ToolTip.set(this, R.string.tt_menu_file)
    }

    fun setFilePath(fileID: Foc) {
        val file = summaryConfig.getPreviewFile(fileID)
        setPreviewPath(file)
    }

    private fun setPreviewPath(file: Foc) {
        setImageObject(file.path, ObjBitmap.Factory())
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setFilePath(info.getFile())
    }
}
