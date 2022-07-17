package ch.bailu.aat_gtk.view.toplevel.list

import ch.bailu.aat_gtk.app.GtkRefs
import ch.bailu.aat_gtk.view.toplevel.list.menu.FileContextMenu
import ch.bailu.aat_gtk.view.util.margin
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class FileListItem(listItem: ListItem, private val fileContextMenu: FileContextMenu, private val descriptions: Array<ContentDescription>) {
    private val labels = ArrayList<Label>()
    private var index = -1

    init {
        val hbox = Box(Orientation.HORIZONTAL, 5)
        val vbox = Box(Orientation.VERTICAL, 5)


        val menu = MenuButton().apply {
            fileContextMenu.addToButton(this)
        }

        menu.margin(10)

        vbox.hexpand = GTK.TRUE
        hbox.append(vbox)
        hbox.append(menu)
        vbox.margin(5)

        val title = createLabel()
        title.useMarkup = GTK.TRUE
        vbox.append(title)
        vbox.append(createLabel())
        vbox.append(createLabel())

        listItem.child = hbox
    }

    private fun createLabel(): Label {
        val result = Label(Str.NULL)
        result.wrap = GTK.TRUE
        result.xalign = 0f
        result.widthChars = 7
        result.marginEnd = 10
        labels.add(result)
        return result
    }

    fun bind(info: GpxInformation, index: Int) {
        this.index = index
        fileContextMenu.index = index

        var title = ""
        var infos = "[${index}]"
        val del = " - "

        descriptions.forEachIndexed { i, d ->
            d.onContentUpdated(InfoID.ALL, info)
            if (i == 0) {
                title = "<b>${d.valueAsString}</b>"
            } else {
                infos = "${infos}${del}${d.valueAsString}"
            }
        }


        GtkRefs.label(labels[0], title)
        GtkRefs.text(labels[1], infos)
        GtkRefs.text(labels[2], info.file.name)
    }

    fun release() {
        labels.forEach {
            GtkRefs.release(it)
        }
    }
}
