package ch.bailu.aat_lib.service.elevation.tile

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.service.background.FileTask
import ch.bailu.aat_lib.service.elevation.Dem3Status
import ch.bailu.foc.Foc
import java.util.zip.ZipInputStream

class Dem3LoaderTask(f: Foc, private val array: Dem3Array, private val status: Dem3Status) :
    FileTask(f) {
    override fun bgOnProcess(appContext: AppContext): Long {
        synchronized(array) {
            try {
                ZipInputStream(getFile().openR()).use {
                    var total = 0
                    it.nextEntry

                    do {
                        val count = it.read(array.data, total, array.data.size - total)
                        total += count
                    } while (count > 0 && total < array.data.size && canContinue())

                    if (canContinue()) {
                        status.status = Dem3Status.VALID
                    }
                }
            } catch (e: Exception) {
                AppLog.w(this, e.message)
                status.status = Dem3Status.EMPTY
            }
        }

        if (status.status == Dem3Status.VALID || status.status == Dem3Status.EMPTY) {
            appContext.broadcaster.broadcast(
                AppBroadcaster.FILE_CHANGED_INCACHE, getFile().toString()
            )

            return array.data.size.toLong()
        }

        return 0
    }
}
