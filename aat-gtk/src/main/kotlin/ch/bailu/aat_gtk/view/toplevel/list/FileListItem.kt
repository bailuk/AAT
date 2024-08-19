package ch.bailu.aat_gtk.view.toplevel.list

import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.description.ContentDescription
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.ListItem
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.type.Str

class FileListItem(appContext: AppContext, listItem: ListItem, private val descriptions: Array<ContentDescription>) {
    private val labels = ArrayList<Label>()
    private val previewImageView = PreviewImageView(appContext)
    private var index = -1

    init {
        val hbox = Box(Orientation.HORIZONTAL, 5)
        val vbox = Box(Orientation.VERTICAL, 5)

        vbox.hexpand = true
        hbox.append(vbox)
        hbox.append(previewImageView.image)
        hbox.margin(5)

        val title = createLabel()
        title.useMarkup = true
        vbox.append(title)
        vbox.append(createLabel())
        vbox.append(createLabel())

        listItem.child = hbox
    }

    private fun createLabel(): Label {
        val result = Label(Str.NULL)
        result.wrap = true
        result.xalign = 0f
        result.widthChars = 7
        result.marginEnd = 10
        labels.add(result)
        return result
    }

    fun bind(info: GpxInformation, index: Int) {
        this.index = index
        var title = ""
        var infoText = "#${index+1}"

        descriptions.forEachIndexed { i, d ->
            d.onContentUpdated(InfoID.ALL, info)
            if (i == 0) {
                title = "<b>${d.getValueAsString()}</b>"
            }  else {
                infoText = "$infoText - ${d.getValueAsString()}"
            }
        }

        labels[0].setLabel(title)
        labels[1].setText(infoText)
        labels[2].setText(info.getFile().name)

        previewImageView.setFilePath(info.getFile())
        previewImageView.onAttached()
    }

    fun teardown() {
        previewImageView.onDetached()
    }
}
