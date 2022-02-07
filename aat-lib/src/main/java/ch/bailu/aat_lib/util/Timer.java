package ch.bailu.aat_lib.util;

/**
 * Interface for UI Thread Timer
 */
public interface Timer {
    /**
     * Call the provided lambda after a specific interval.
     * The lambda gets called from the UI Thread
     * @param run  function to call
     * @param interval delay beovore calling function
     */
    void kick(Runnable run, long interval);
    void cancel();
}
