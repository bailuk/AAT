package ch.bailu.aat_gtk.ui.view.solid

import ch.bailu.aat_gtk.ui.view.menu.PopupButton
import ch.bailu.aat_gtk.ui.view.menu.SolidCheckMenu
import ch.bailu.aat_gtk.ui.view.menu.SolidIndexMenu
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.preferences.*
import ch.bailu.gtk.helper.ActionHelper

class SolidMenuButton
    : PopupButton, OnPreferencesChanged, Attachable {


    private val solid: AbsSolidType

    constructor(actionHelper: ActionHelper, solidIndexList: SolidIndexList) : super(actionHelper, SolidIndexMenu(solidIndexList)) {
        solid = solidIndexList
        solid.register(this)
        setIcon(solid.iconResource)

    }

    constructor(actionHelper: ActionHelper, solidCheckList: SolidCheckList) : super(actionHelper, SolidCheckMenu(solidCheckList)) {
        solid = solidCheckList
        solid.register(this)
        setIcon(solid.iconResource)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solid.hasKey(key)) {
            setIcon(solid.iconResource)
            AppLog.i(this, solid.valueAsString)
        }
    }

    override fun onAttached() {
        solid.register(this)
    }

    override fun onDetached() {
        solid.unregister(this)
    }
}
