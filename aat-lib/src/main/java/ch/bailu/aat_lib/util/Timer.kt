package ch.bailu.aat_lib.util

/**
 * Interface for UI Thread Timer
 */
interface Timer {
    /**
     * Call the provided lambda after a specific interval.
     * The lambda gets called from the UI Thread
     * @param run  function to call
     * @param interval delay before calling function
     */
    fun kick(interval: Long, run: Runnable)
    fun cancel()
}
