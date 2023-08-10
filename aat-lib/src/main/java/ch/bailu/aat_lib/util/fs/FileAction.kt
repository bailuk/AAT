package ch.bailu.aat_lib.util.fs

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery
import ch.bailu.aat_lib.preferences.location.SolidMockLocationFile
import ch.bailu.aat_lib.util.fs.AFile.logErrorExists
import ch.bailu.aat_lib.util.fs.AFile.logErrorNoAccess
import ch.bailu.foc.Foc
import java.io.IOException

object FileAction {
    fun rescanDirectory(context: AppContext, file: Foc) {
        if (isParentActive(context, file)) {
            context.services.directoryService.rescan()
        }
    }

    private fun isParentActive(context: AppContext, file: Foc): Boolean {
        val currentDir = SolidDirectoryQuery(context.storage, context).valueAsFile
        val dir = file.parent()
        return dir != null && dir == currentDir
    }

    fun reloadPreview(context: AppContext, file: Foc) {
        if (isParentActive(context, file)) {
            context.services.directoryService.deleteEntry(file)
        }
    }

    fun useForMockLocation(context: AppContext, file: Foc) {
        if (file.canRead()) SolidMockLocationFile(context.storage).setValue(file.path) else logErrorNoAccess(
            file
        )
    }

    fun copyToDir(context: AppContext, src: Foc?, destDir: Foc?, p: String?, ext: String?) {
        try {
            copyToDest(context, src, AppDirectory.generateUniqueFilePath(destDir, p, ext))
        } catch (e: IOException) {
            AppLog.e(context, e)
        }
    }

    @Throws(IOException::class)
    private fun copyToDest(context: AppContext, src: Foc?, dest: Foc?) {
        if (src != null && dest != null) {
            if (dest.exists()) {
                logErrorExists(dest)
            } else {
                src.copy(dest)
                context.broadcaster.broadcast(
                    AppBroadcaster.FILE_CHANGED_ONDISK,
                    dest.path,
                    src.path
                )
            }
        }
    }

    fun copyToDir(context: AppContext, src: Foc, destDir: Foc) {
        try {
            copyToDest(context, src, destDir.child(src.name))
        } catch (e: IOException) {
            AppLog.e(context, e)
        }
    }

    fun rename(context: AppContext, source: Foc, target: Foc) {
        if (source.exists()) {
            if (target.exists()) {
                logErrorExists(target)
            } else {
                source.mv(target)
                rescanDirectory(context, source)
            }
        }
    }
}
