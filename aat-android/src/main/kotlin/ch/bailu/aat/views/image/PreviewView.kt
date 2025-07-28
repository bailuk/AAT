package ch.bailu.aat.views.image

import ch.bailu.aat.R
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.util.ui.tooltip.ToolTip
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.service.cache.ObjBitmap
import ch.bailu.aat_lib.service.directory.SummaryConfig
import ch.bailu.foc.Foc
import java.io.IOException

// TODO merge PreviewImageView and PreviewView
class PreviewView(sc: ServiceContext, private val summaryConfig: SummaryConfig) : ImageObjectView(sc, R.drawable.open_menu_light), TargetInterface {
    init {
        ToolTip.set(this, R.string.tt_menu_file)
    }

    fun setFilePath(fileID: Foc) {
        try {
            val file = summaryConfig.getPreviewFile(fileID)
            setPreviewPath(file)
        } catch (e: IOException) {
            AppLog.e(this, e)
        }
    }

    private fun setPreviewPath(file: Foc) {
        setImageObject(file.path, ObjBitmap.Factory())
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        setFilePath(info.getFile())
    }
}
