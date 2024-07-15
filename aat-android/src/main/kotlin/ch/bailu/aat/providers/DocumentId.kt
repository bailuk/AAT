package ch.bailu.aat.providers

class DocumentId (private val documentId: String) {
    val isRoot = documentId == Constants.ROOT
    val isDirectory = documentId.startsWith(Constants.DIR_PREFIX)
    val isFile = documentId.startsWith(Constants.GPX_PREFIX)

    private val sep = documentId.indexOf(Constants.GPX_INFIX)
    val fileName = parseFileName(sep)
    val preset = parsePreset(sep)

    fun isValid(): Boolean {
        if (isRoot) {
            return !isDirectory && !isFile
        } else if (isFile) {
            return fileName.isNotEmpty() && !isDirectory && !isRoot
        } else if (isDirectory) {
            return !isRoot && !isFile
        }
        return false
    }

    private fun parseFileName(sep: Int): String {
        if (sep > -1) {
            return documentId.substring(sep + Constants.GPX_INFIX.length)
        }
        return ""
    }

    private fun parsePreset(sep: Int): Int {
        try {
            if (isDirectory) {
                return documentId.substring(Constants.DIR_PREFIX.length).toInt()
            } else if (isFile && sep > -1) {
                return documentId.substring(Constants.GPX_PREFIX.length, sep).toInt()
            }
        } catch (e: NumberFormatException) {
            return 0
        }
        return 0
    }
}
