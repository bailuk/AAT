package ch.bailu.aat_gtk.ui.view.list

import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class FileList {
    val vbox = Box(Orientation.VERTICAL,12)
    val label = Label(Str("Track List"))
    val scrolled = ScrolledWindow(null, null)
    val listbox = ListBox()

    init {
        vbox.packStart(label, GTK.FALSE, GTK.FALSE, 0)

        scrolled.setPolicy(PolicyType.NEVER, PolicyType.AUTOMATIC)
        vbox.packStart(scrolled, GTK.TRUE, GTK.TRUE, 0)

        scrolled.add(listbox)

        for (i in 1 .. 2000) {
            val row = Label(Str("Item $i"))
            row.xalign = 0f
            listbox.add(row)
        }
    }
}
