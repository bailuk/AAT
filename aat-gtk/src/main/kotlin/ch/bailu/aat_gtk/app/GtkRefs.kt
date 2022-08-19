package ch.bailu.aat_gtk.app

import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.type.Str
/*
object GtkRefs {

    private val map = HashMap<Any, Str>()
    private var lastLog = System.currentTimeMillis()
    private var lastSize = 0

    fun text(label: Label, str: String) {
        val s = Str(str)
        label.text = s
        bind(label, s)
        log()
    }

    fun label(label: Label, str: String) {
        val s = Str(str)
        label.label = s
        bind(label, s)
        log()
    }

    fun label(button: Button, str: String) {
        val s = Str(str)
        button.label = s
        bind(button, s)
        log()
    }

    private fun bind(obj: Any, str: Str) {
        val old = map[obj]
        map[obj] = str
        old?.destroy()
    }

    private fun log() {
        val now = System.currentTimeMillis()
        if (now - lastLog > 5000) {
            val size = map.size
            if (lastSize != size) {
                println("REFS (Str): " + map.size)
            }
            lastLog = now
            lastSize = size
        }
    }

    fun release(obj: Any) {
        map[obj]?.destroy()
        map.remove(obj)
    }

}
*/
