package ch.bailu.aat_lib.util.fs

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.file_list.SolidDirectoryQuery
import ch.bailu.aat_lib.preferences.location.SolidMockLocationFile
import ch.bailu.aat_lib.util.fs.FileAccess.logErrorExists
import ch.bailu.aat_lib.util.fs.FileAccess.logErrorNoAccess
import ch.bailu.foc.Foc
import java.io.IOException

/**
 * High level file actions for UI (Menu) file operations
 */
object FileAction {
    fun rescanDirectory(context: AppContext, file: Foc) {
        if (isParentActive(context, file)) {
            context.services.getDirectoryService().rescan()
        }
    }

    private fun isParentActive(context: AppContext, file: Foc): Boolean {
        val currentDir = SolidDirectoryQuery(context.storage, context).getValueAsFile()
        val dir = file.parent()
        return dir != null && dir == currentDir
    }

    fun reloadPreview(context: AppContext, file: Foc) {
        if (isParentActive(context, file)) {
            context.services.getDirectoryService().deleteEntry(file)
        }
    }

    fun useForMockLocation(context: AppContext, file: Foc) {
        if (file.canRead()) SolidMockLocationFile(context.storage).setValue(file.path) else logErrorNoAccess(
            file
        )
    }

    /**
     * Copy source to destination.
     * If destination already exists, try creating a new file name
     */
    fun copyToDir(context: AppContext, src: Foc, destDir: Foc) {
        val splitter = FileNameSplitter(src)
        copyToDir(context, src, destDir, splitter.prefix, splitter.extension)
    }

    /**
     * Copy source to destination.
     * Try creating a unique file name using prefix and extension
     */
    private fun copyToDir(context: AppContext, src: Foc, destDir: Foc, prefix: String, extension: String) {
        try {
            val file = FileUtil.generateUniqueFilePath(destDir, prefix, extension)
            copyToDest(context, src, file)
            AppLog.i(this, file.pathName)
        } catch (e: IOException) {
            // Android SAF backend can throw any exception (like IllegalArgumentException)
            AppLog.e(context, e)
        }
    }

    @Throws(IOException::class)
    private fun copyToDest(context: AppContext, src: Foc, dest: Foc) {
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
