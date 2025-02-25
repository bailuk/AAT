package ch.bailu.aat_lib.service.background

import ch.bailu.foc.Foc
import javax.annotation.Nonnull

abstract class FileTask(private val file: Foc) : BackgroundTask() {
    private var tasks: Tasks? = null


    @Nonnull
    override fun toString(): String {
        return file.toString()
    }

    fun getFile(): Foc {
        return file
    }

    fun getID(): String {
        return file.path
    }

    fun register(tasks: Tasks) {
        if (this.tasks == null) {
            this.tasks = tasks
        }
    }

    override fun onInsert() {
        tasks?.add(this)
    }

    override fun onRemove() {
        this.tasks?.remove(this)
    }
}
