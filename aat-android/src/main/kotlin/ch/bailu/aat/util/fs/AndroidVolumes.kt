package ch.bailu.aat.util.fs

import android.content.Context
import android.os.Environment
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFile
import java.io.File

class AndroidVolumes(context: Context) {
    private var initialized = false

    init {
        if (!initialized) {
            initList(context)
            initialized = true
        }
    }

    val volumes: List<Foc>
        get() = Companion.volumes

    val caches: List<Foc>
        get() = Companion.caches

    val files: List<Foc>
        get() = Companion.files

    companion object {
        private const val MAX_DEPTH = 4

        private var volumes = ArrayList<Foc>()
        private var files = ArrayList<Foc>()
        private var caches = ArrayList<Foc>()

        fun initList(context: Context) {
            val internalCache = context.cacheDir
            val internalFile = context.filesDir
            val externalVolume = Environment.getExternalStorageDirectory()
            val externalFiles: Array<File?> = context.getExternalFilesDirs(null)
            val externalCaches: Array<File?> = context.externalCacheDirs

            files = getMounted(internalFile, externalFiles)
            caches = getMounted(internalCache, externalCaches)
            volumes = volumesFromFiles(externalVolume, externalFiles)
        }

        private fun getMounted(file: File?, files: Array<File?>): ArrayList<Foc> {
            val result = ArrayList<Foc>()
            if (file != null) {
                result.add(toFoc(file))
            }
            for (f in files) {
                if (f != null) {
                    result.add(toFoc(f))
                }
            }
            return result
        }

        private fun toFoc(file: File): Foc {
            return FocFile(file)
        }

        private fun volumesFromFiles(externalVolume: File, files: Array<File?>): ArrayList<Foc> {
            var extVolume: File? = externalVolume

            val volumes = arrayOfNulls<File>(files.size)
            for (i in volumes.indices) {
                volumes[i] = getParent(files[i])
                if (volumes[i] != null && volumes[i] == extVolume) {
                    extVolume = null
                }
            }
            return getMounted(extVolume, volumes)
        }

        private fun getParent(file: File?): File? {
            var result = file
            var i = MAX_DEPTH
            while (i > 0) {
                i--
                if (result != null) result = result.parentFile
            }
            return result
        }
    }
}
