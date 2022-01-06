package ch.bailu.aat_gtk.ui.view.list

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_lib.description.AverageSpeedDescription
import ch.bailu.aat_lib.description.DateDescription
import ch.bailu.aat_lib.description.DistanceDescription
import ch.bailu.aat_lib.description.TimeDescription
import ch.bailu.aat_lib.dispatcher.CustomFileSource
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.preferences.SolidDirectoryQuery
import ch.bailu.aat_lib.preferences.presets.SolidPreset
import ch.bailu.aat_lib.service.directory.IteratorSimple
import ch.bailu.gtk.GTK
import ch.bailu.gtk.bridge.ListIndex
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.helper.LabelHelper
import ch.bailu.gtk.type.Str

class FileList(dispatcher: DispatcherInterface) {
    val vbox = Box(Orientation.VERTICAL,12)
    private val listIndex = ListIndex()

    init {

        val dateDescription = DateDescription()
        val distanceDescription = DistanceDescription(GtkAppContext.storage)
        val averageSpeedDistanceDescription = AverageSpeedDescription(GtkAppContext.storage)
        val timeDescription = TimeDescription()


        try {
            val customFileSource = CustomFileSource(GtkAppContext)
            dispatcher.addSource(customFileSource)

            val sdirectory = SolidDirectoryQuery(GtkAppContext.storage, GtkAppContext)
            val directory = SolidPreset(GtkAppContext.storage).getDirectory(GtkAppContext.dataDirectory)
            sdirectory.setValue(directory.path)

            GtkAppContext.services.directoryService.rescan()
            val iteratorSimple = IteratorSimple(GtkAppContext)

            listIndex.size = iteratorSimple.count

            iteratorSimple.setOnCursorChangedLinsener {
                listIndex.size = iteratorSimple.count
            }

            val factory = SignalListItemFactory()

            factory.onSetup { item: ListItem ->
                val hbox = Box(Orientation.HORIZONTAL, 5)
                val vbox = Box(Orientation.VERTICAL, 5)
                val menu = MenuButton()

                menu.marginBottom = 10
                menu.marginTop = 10
                menu.marginEnd = 10

                vbox.hexpand = GTK.TRUE
                hbox.append(vbox)
                hbox.append(menu)

                vbox.marginTop = 5
                vbox.marginStart = 5
                vbox.marginBottom = 5
                var title = createLabel()
                title.useMarkup = GTK.TRUE
                vbox.append(title)
                vbox.append(createLabel())
                vbox.append(createLabel())


                item.child = hbox
            }

            factory.onBind { item: ListItem ->
                val hbox = Box(item.child.cast())
                val vbox = Box(hbox.firstChild.cast())

                val label1 = Label(vbox.firstChild.cast())
                val label2 = Label(label1.nextSibling.cast())
                val label3 = Label(label2.nextSibling.cast())


                val idx = ListIndex.toIndex(item)

                iteratorSimple.moveToPosition(idx)

                timeDescription.onContentUpdated(InfoID.ALL, iteratorSimple.info)
                dateDescription.onContentUpdated(InfoID.ALL, iteratorSimple.info)
                distanceDescription.onContentUpdated(InfoID.ALL, iteratorSimple.info)
                averageSpeedDistanceDescription.onContentUpdated(InfoID.ALL, iteratorSimple.info)
                dateDescription.onContentUpdated(InfoID.ALL, iteratorSimple.info)

                LabelHelper.setLabel(label1, "<b>${dateDescription.valueAsString}</b>")
                LabelHelper.setLabel(label2, "${distanceDescription.valueAsString} - ${averageSpeedDistanceDescription.valueAsString} - ${timeDescription.valueAsString}")
                LabelHelper.setLabel(label3, iteratorSimple.info.file.name)
            }

            val list = ListView(listIndex.inSelectionModel(), factory)

            list.onActivate {
                sdirectory.position.value = it
                customFileSource.setFileID(iteratorSimple.info.file.toString())
            }

            val scrolled = ScrolledWindow()
            scrolled.setChild(list)
            scrolled.hexpand = GTK.TRUE
            scrolled.vexpand = GTK.TRUE

            vbox.append(scrolled)
        } catch (e: Exception) {
            System.out.println("catched")
            e.printStackTrace()
        }
    }

    private fun createLabel(): Label {
        val result = Label(Str(""))
        result.wrap = GTK.TRUE
        result.xalign = 0f
        result.widthChars = 7
        result.marginEnd = 10
        return result
    }
}
