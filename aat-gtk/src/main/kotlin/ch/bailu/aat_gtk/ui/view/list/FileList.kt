package ch.bailu.aat_gtk.ui.view.list

import ch.bailu.gtk.GTK
import ch.bailu.gtk.bridge.ListIndex
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.helper.LabelHelper
import ch.bailu.gtk.type.Str

class FileList {
    val vbox = Box(Orientation.VERTICAL,12)
    val label = Label(Str("Track List"))

    init {
        var stringList = ArrayList<String>()

        for (i in 1 .. 2000) {
            stringList.add("Item $i")
        }

        val factory = SignalListItemFactory()

        factory.onSetup { item: ListItem ->
            val box = Box(Orientation.HORIZONTAL, 5)
            box.append(createLabel())
            box.append(createLabel())
            box.append(createLabel())
            item.child = box
        }

        factory.onBind { item: ListItem ->
            val index = Label(item.child.firstChild.cast())
            val count = Label(index.nextSibling.cast())
            val word = Label(count.nextSibling.cast())
            val idx = ListIndex.toIndex(item)

            val key: String = stringList.get(idx)

            LabelHelper.setLabel(word, key)
            LabelHelper.setLabel(count, "...")
            LabelHelper.setLabel(index, idx.toString())
        }

        val listIndex = ListIndex()
        listIndex.size = stringList.size
        val list = ListView(listIndex.inSelectionModel(), factory)

        val scrolled = ScrolledWindow()
        scrolled.setChild(list)
        scrolled.hexpand = GTK.TRUE
        scrolled.vexpand = GTK.TRUE

        vbox.append(label)
        vbox.append(scrolled)
    }

    private fun createLabel(): Label {
        val result = Label(Str(""))
        result.xalign = 0f
        result.widthChars = 7
        result.marginEnd = 10
        return result
    }
}
