package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.lib.extensions.ellipsize
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.lib.icons.IconMap
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileList
import ch.bailu.aat_lib.util.IndexedMap
import ch.bailu.gtk.gtk.*

class ContextBar(contextCallback: UiController, private val storage: StorageInterface
                 ) : OnContentUpdatedInterface {
    val revealer = Revealer()

    private val combo = ComboBoxText().apply {
        hexpand = true
    }
    private val cache = IndexedMap<Int, GpxInformation>()
    private var trackerState = StateID.NOSERVICE
    private var selectInfoID = InfoID.TRACKER

    private val row = Box(Orientation.HORIZONTAL, 0).apply {
        addCssClass(Strings.linked)
        append(combo)
        margin(3)
    }

    private val buttons = ArrayList<ToggleButton>().apply {
        add(createButton("inc_map") {
            if (storage.readInteger(MainStackView.KEY) == MainStackView.INDEX_MAP) {
                contextCallback.showInMap(selectedGpx())
            } else {
                contextCallback.showMap()
            }
            updateToggle()
        })
        add(createButton("view-list-symbolic") {
            contextCallback.showInList()
            updateToggle()
        })
        add(createButton("help-about-symbolic") {
            contextCallback.showDetail()
            updateToggle()
        })
        add(createButton("inc_cockpit") {
            contextCallback.showCockpit()
            updateToggle()
        })

        forEach {
            row.append(it)
        }
    }

    init {
        revealer.child = Box(Orientation.HORIZONTAL,0).apply {
            append(row)
        }
        revealer.revealChild = true

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
            combo.appendText(value.file.name.ellipsize(15))
        }
        combo.active = index
    }


    private fun createButton(icon: String, onClicked: Button.OnClicked) : ToggleButton {
        return ToggleButton().apply {
            child = IconMap.getImage(icon, Layout.ICON_SIZE)
            onClicked(onClicked)
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
        if (iid == InfoID.TRACKER || iid == InfoID.FILEVIEW || (iid >= InfoID.OVERLAY && iid < InfoID.OVERLAY + SolidOverlayFileList.MAX_OVERLAYS)) {
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

        buttons.forEachIndexed { i, it ->
            it.active = index == i
        }
    }
}
