package ch.bailu.aat_gtk.ui.view.list

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_lib.description.TimeDescription
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery
import ch.bailu.aat_lib.preferences.presets.SolidPreset
import ch.bailu.aat_lib.service.directory.IteratorSimple
import ch.bailu.gtk.GTK
import ch.bailu.gtk.bridge.ListIndex
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.helper.LabelHelper
import ch.bailu.gtk.type.Str

class FileList {
    val vbox = Box(Orientation.VERTICAL,12)
    val label = Label(Str("Track List"))
    private val listIndex = ListIndex()

    init {
        val timeDescription = TimeDescription()

        try {
            val directory = SolidPreset(GtkAppContext.storage).getDirectory(GtkAppContext.dataDirectory)
            SolidDirectoryQuery(GtkAppContext.storage, GtkAppContext).setValue(directory.path)

            GtkAppContext.services.directoryService.rescan()
            val iteratorSimple = IteratorSimple(GtkAppContext)

            listIndex.size = iteratorSimple.count

            iteratorSimple.setOnCursorChangedLinsener {
                listIndex.size = iteratorSimple.count
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

                iteratorSimple.moveToPosition(idx)

                timeDescription.onContentUpdated(InfoID.ALL, iteratorSimple.info)
                LabelHelper.setLabel(word, iteratorSimple.info.file.name)
                LabelHelper.setLabel(count, timeDescription.value)
                LabelHelper.setLabel(index, idx.toString())
            }

            val list = ListView(listIndex.inSelectionModel(), factory)

            val scrolled = ScrolledWindow()
            scrolled.setChild(list)
            scrolled.hexpand = GTK.TRUE
            scrolled.vexpand = GTK.TRUE

            vbox.append(label)
            vbox.append(scrolled)
        } catch (e: Exception) {
            System.out.println("catched")
            e.printStackTrace()
        }
    }

    private fun createLabel(): Label {
        val result = Label(Str(""))
        result.xalign = 0f
        result.widthChars = 7
        result.marginEnd = 10
        return result
    }
}
