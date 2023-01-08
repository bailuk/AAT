package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.lib.extensions.ellipsize
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.lib.icons.IconMap
import ch.bailu.aat_gtk.dispatcher.SelectedSource
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.gpx.StateID
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.gtk.gtk.*

class ContextBar(contextCallback: UiController,
                 private val storage: StorageInterface,
                 private val selectedSource: SelectedSource
)
    : OnContentUpdatedInterface {

    val revealer = Revealer()

    private val combo = ComboBoxText().apply {
        hexpand = true
        onChanged {
            selectedSource.selectIndexAndUpdate(active)
        }
    }

    private var trackerState = StateID.NOSERVICE

    private val row = Box(Orientation.HORIZONTAL, 0).apply {
        addCssClass(Strings.linked)
        append(combo)
        margin(3)
    }

    private val buttons = ArrayList<ToggleButton>().apply {
        add(createButton("inc_map") {
            if (storage.readInteger(MainStackView.KEY) == MainStackView.INDEX_MAP) {
                contextCallback.showInMap(selectedSource.cache.getValueAt(combo.active))
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

    private fun createButton(icon: String, onClicked: Button.OnClicked) : ToggleButton {
        return ToggleButton().apply {
            child = IconMap.getImage(icon, Layout.ICON_SIZE)
            onClicked(onClicked)
        }
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        selectedSource.cache.onContentUpdated(iid, info)

        if (selectedSource.cache.isInCache(iid) && iid == InfoID.TRACKER) {
            if (info.state != trackerState) {
                trackerState = info.state
                updateCombo(selectedSource.cache.indexOf(iid))
                selectedSource.selectedIID = iid
            }
        } else {
            updateCombo(selectedSource.cache.indexOf(iid))
            selectedSource.selectedIID = iid
        }

        selectedSource.requestUpdate(iid)
    }


    private fun updateCombo(index: Int = 0) {
        combo.removeAll()
        selectedSource.cache.forEach { info ->
            combo.appendText(info.file.name.ellipsize(15))
        }
        combo.active = index
    }

    private fun updateToggle() {
        val index = storage.readInteger(MainStackView.KEY)

        buttons.forEachIndexed { i, it ->
            it.active = index == i
        }
    }
}
