package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.util.IconMap
import ch.bailu.aat_gtk.view.map.control.Bar
import ch.bailu.aat_gtk.view.share.ComboBoxString
import ch.bailu.aat_gtk.view.util.margin
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileList
import ch.bailu.aat_lib.util.IndexedMap
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

class ContextBar(contextCallback: UiController, private val storage: StorageInterface) : OnContentUpdatedInterface {
    val revealer = Revealer()

    private val row1 = Box(Orientation.HORIZONTAL, 0)
    private val row2 = Box(Orientation.HORIZONTAL, 0)

    private val combo = ComboBoxString()
    private var cache = IndexedMap<Int, GpxInformation>().apply {
        put(InfoID.TRACKER, GpxInformation.NULL)
        put(InfoID.FILEVIEW, GpxInformation.NULL)

        for (i in 0 until SolidOverlayFileList.MAX_OVERLAYS) {
            put(InfoID.OVERLAY + i, GpxInformation.NULL)
        }
    }

    private val buttons = ArrayList<ToggleButton>().apply {
        add(createButton("Map") { contextCallback.showMap() })
        add(createButton("List") { contextCallback.showInList() })
        add(createButton("Detail") { contextCallback.showDetail() })
        add(createButton("Cockpit") { contextCallback.showCockpit() })
    }

    init {
        val layout = Box(Orientation.VERTICAL,0)

        row1.append(createImageButton("zoom-fit-best-symbolic") { contextCallback.showInMap(selectedGpx()) } )
        row1.append(combo.combo.apply { margin(3) })

        buttons.forEach {
            it.margin(3)
            row2.append(it)
        }

        layout.append(row1)
        layout.append(row2)

        updateCombo()

        revealer.child = layout
        revealer.revealChild = GTK.FALSE

        storage.register { _, key ->
            if (key == MainStackView.KEY) {
                updateToggle()
            }
        }
        updateToggle()
    }

    private fun updateCombo() {
        var index = 0
        cache.forEach { key, value ->
            if (key == InfoID.TRACKER) {
                combo.insert(index, "log.gpx")
            } else {
                combo.insert(index, value.file.name)
            }
            index++
        }
    }


    private fun createButton(label: String, onClicked: Button.OnClicked) : ToggleButton {
        val result = ToggleButton()
        result.label = Str(label)
        result.onClicked(onClicked)
        result.marginTop = 3
        result.marginBottom = 3
        return result
    }

    private fun createImageButton(resource: String, onClicked: Button.OnClicked) : Button {
        val result = Button()
        result.onClicked(onClicked)
        result.child = IconMap.getImage(resource, Bar.ICON_SIZE)
        result.margin(3)
        return result
    }

    private fun selectedGpx(): GpxInformation {
        val info = cache.getAt(combo.combo.active)

        if (info is GpxInformation) {
            return info
        }
        return GpxInformation.NULL
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (cache.get(iid) is GpxInformation) {
            cache.put(iid, info)

            if (iid != InfoID.TRACKER) {
                combo.insert(cache.indexOf(iid), info.file.name)
                if (iid == InfoID.FILEVIEW) {
                    combo.combo.active = cache.indexOf(iid)
                    revealer.show()
                }
            }
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
