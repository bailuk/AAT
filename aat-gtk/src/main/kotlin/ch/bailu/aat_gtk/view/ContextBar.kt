package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.lib.extensions.appendText
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.lib.extensions.setLabel
import ch.bailu.aat_gtk.lib.icons.IconMap
import ch.bailu.aat_gtk.view.map.control.Bar
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.aat_lib.util.IndexedMap
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*

class ContextBar(contextCallback: UiController, private val storage: StorageInterface) : OnContentUpdatedInterface {
    val revealer = Revealer()

    private val combo = ComboBoxText()
    private val cache = IndexedMap<Int, GpxInformation>()
    private var trackerState = StateID.NOSERVICE
    private var selectInfoID = InfoID.TRACKER

    private val row1 = Box(Orientation.HORIZONTAL, 0).apply {
        append(createImageButton("zoom-fit-best-symbolic") { contextCallback.showInMap(selectedGpx()) } )
        append(combo)
        margin(3)
        addCssClass(Strings.linked)

    }
    private val row2 = Box(Orientation.HORIZONTAL, 0).apply {
        addCssClass(Strings.linked)
        margin(3)
    }

    private val buttons = ArrayList<ToggleButton>().apply {
        add(createButton(ToDo.translate("Map")) {
            contextCallback.showMap()
            updateToggle()
        })
        add(createButton(ToDo.translate("List")) {
            contextCallback.showInList()
            updateToggle()
        })
        add(createButton(ToDo.translate("Detail")) {
            contextCallback.showDetail()
            updateToggle()
        })
        add(createButton(ToDo.translate("Cockpit")) {
            contextCallback.showCockpit()
            updateToggle()
        })

        forEach {
            row2.append(it)
        }
    }

    init {
        val layout = Box(Orientation.VERTICAL,0).apply {
            append(row1)
            append(row2)
        }

        revealer.child = layout
        revealer.revealChild = GTK.FALSE

        storage.register { _, key ->
            if (key == MainStackView.KEY) {
                updateToggle()
            }
        }
        updateToggle()
    }

    private fun updateCombo(index: Int = 0) {
        combo.removeAll()
        cache.forEach { _, value ->
            combo.appendText(value.file.name)
        }
        combo.active = index
    }


    private fun createButton(label: String, onClicked: Button.OnClicked) : ToggleButton {
        return ToggleButton().apply {
            setLabel(label)
            onClicked(onClicked)
        }
    }

    private fun createImageButton(resource: String, onClicked: Button.OnClicked) : Button {
        return Button().apply {
            onClicked(onClicked)
            child = IconMap.getImage(resource, Bar.ICON_SIZE)
        }
    }

    private fun selectedGpx(): GpxInformation {
        val info = cache.getValueAt(combo.active)
        if (info is GpxInformation) {
            return info
        }
        return GpxInformation.NULL
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        val isInCache = cache.indexOfKey(iid) > -1

        if (isInCache && iid == InfoID.TRACKER) {
            if (info.state != trackerState) {
                trackerState = info.state
                updateCacheAndCombo(iid, info, isInCache)
            }
        } else {
            updateCacheAndCombo(iid, info, isInCache)
        }
    }

    private fun updateCacheAndCombo(iid: Int, info: GpxInformation, isInCache: Boolean) {
        if (info.isLoaded && info.gpxList.pointList.size() > 0) {
            cache.put(iid, info)
            selectInfoID = iid
            updateCombo(cache.indexOfKey(selectInfoID))

        } else if (isInCache) {
            cache.remove(iid)
            updateCombo(cache.indexOfKey(selectInfoID))
        }
    }

    private fun updateToggle() {
        val index = storage.readInteger(MainStackView.KEY)

        AppLog.d(this, "update toggle $index")
        buttons.forEachIndexed { i, it ->
            it.active = GTK.IS(index == i)
        }
    }
}
