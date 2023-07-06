package ch.bailu.aat.services.tileremover

interface State {
    /** firstPixelIndex scanning  */
    fun scan()

    /** chancel  */
    fun stop()

    /** setItem to initial state  */
    fun reset()

    /** firstPixelIndex removing  */
    fun remove()

    /** rebuild list of files to remove
     * (trim settings have been changed)  */
    fun rescan()

    /** remove all tiles in selected directory  */
    fun removeAll()
}
