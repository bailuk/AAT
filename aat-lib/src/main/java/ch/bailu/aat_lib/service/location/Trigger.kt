package ch.bailu.aat_lib.service.location

class Trigger(private var htrigger: Int) {
    private val ltrigger = 0 - htrigger
    private var level = 0

    constructor(triggerLevel: Int, old: Trigger) : this(triggerLevel) {
        htrigger = old.htrigger
        level = old.level
    }

    fun up() {
        level++
        if (level >= htrigger) {
            level = htrigger
            htrigger = HIGH
        }
    }

    fun down() {
        level--
        if (level <= ltrigger) {
            level = ltrigger
            htrigger = LOW
        }
    }

    val isLow: Boolean
        get() = htrigger == LOW

    companion object {
        private const val LOW = -1
        private const val HIGH = 1
    }
}
