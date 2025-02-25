package ch.bailu.aat_lib.service.background

import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.Broadcaster
import ch.bailu.aat_lib.util.Objects.equals
import ch.bailu.foc.Foc


class Tasks(private val broadcaster: Broadcaster) {
    private val downloads = ArrayList<FileTask>(10)

    @Synchronized
    fun add(task: FileTask) {
        if (!contains(task)) {
            downloads.add(task)
            changed(task)
        }
    }


    fun contains(task: FileTask): Boolean {
        return contains(task.getFile())
    }

    @Synchronized
    fun get(file: Foc): FileTask? {
        for (task in downloads) {
            if (equals(task.getFile(), file)) {
                return task
            }
        }
        return null
    }

    @Synchronized
    fun contains(file: Foc): Boolean {
        return get(file) != null
    }

    @Synchronized
    fun remove(task: FileTask) {
        if (downloads.remove(task)) {
            changed(task)
        }
    }

    @Synchronized
    private fun changed(task: FileTask) {
        if (task is DownloadTask) {
            broadcaster.broadcast(
                AppBroadcaster.FILE_BACKGROUND_TASK_CHANGED,
                task.getFile().toString(),
                task.source.toString()
            )
        } else {
            broadcaster.broadcast(
                AppBroadcaster.FILE_BACKGROUND_TASK_CHANGED,
                task.getFile().toString()
            )
        }
    }
}
