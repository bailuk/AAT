package ch.bailu.aat_lib.service.directory

interface MapPreviewInterface {
    fun isReady(): Boolean
    fun generateBitmapFile()
    fun onDestroy()
}
