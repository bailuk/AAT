package ch.bailu.aat.views.bar

import ch.bailu.aat.R
import ch.bailu.aat.activities.AbsDispatcher
import ch.bailu.aat.activities.ActivityContext
import ch.bailu.aat.activities.MainActivity
import ch.bailu.aat.app.ActivitySwitcher
import ch.bailu.aat.menus.OptionsMenu
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.util.ui.theme.AppTheme
import ch.bailu.aat.views.image.ImageButtonViewGroup
import ch.bailu.aat.views.description.GPSStateButton
import ch.bailu.aat.views.description.SensorStateButton
import ch.bailu.aat.views.description.TrackerStateButton
import ch.bailu.aat.views.description.mview.MultiView
import ch.bailu.aat.views.description.mview.MultiViewNextButton
import ch.bailu.aat.views.description.mview.MultiViewSelector
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.util.Objects

class MainControlBar(acontext: AbsDispatcher, orientation: Int = HORIZONTAL, button: Int = AppLayout.DEFAULT_VISIBLE_BUTTON_COUNT) :
    ControlBar(acontext, orientation, AppTheme.bar, button) {
    constructor(acontext: AbsDispatcher, mv: MultiView) : this(acontext) {
        addAll(mv)
    }

    init {
        if (Objects.equals(
                acontext.javaClass.simpleName,
                MainActivity::class.java.simpleName
            )
        ) addMenuButton(acontext) else addBackButton(acontext)
    }

    private fun addBackButton(acontext: AbsDispatcher) {
        val b = addImageButton(R.drawable.edit_undo_inverse, controlSize)
        b.setOnClickListener { acontext.onBackPressedMenuBar() }
    }

    private fun addMenuButton(acontext: AbsDispatcher): ImageButtonViewGroup {
        val menu = addImageButton(R.drawable.open_menu_inverse)
        menu.setOnClickListener {
            OptionsMenu(acontext.serviceContext).showAsPopup(
                context, menu
            )
        }
        return menu
    }

    fun addAll(mv: MultiView) {
        addMvPrevious(mv, controlSize / 2)
        add(MultiViewSelector(mv), controlSize * 2)
        addMvNext(mv, controlSize / 2)
    }

    @JvmOverloads
    fun addMvNext(mv: MultiView, size: Int = controlSize) {
        add(MultiViewNextButton(mv, theme), size)
    }

    private fun addMvPrevious(mv: MultiView, size: Int) {
        addImageButton(
            R.drawable.go_previous_inverse,
            size
        ).setOnClickListener { mv.setPrevious() }
    }

    fun addGpsState(acontext: AbsDispatcher): MainControlBar {
        val gps = GPSStateButton(acontext)
        add(gps)
        acontext.addTarget(gps, InfoID.LOCATION)
        return this
    }

    fun addTrackerState(acontext: AbsDispatcher): MainControlBar {
        val ts = TrackerStateButton(acontext.serviceContext)
        add(ts)
        acontext.addTarget(ts, InfoID.TRACKER)
        return this
    }

    fun addSensorState(acontext: ActivityContext) {
        val s = SensorStateButton(acontext.serviceContext)
        add(s)
        acontext.addTarget(s, InfoID.SENSORS)
    }

    fun addActivityCycle(acontext: ActivityContext) {
        if (ActivitySwitcher.DISABLE_ACTIVITY_CYCLING) {
            addSpace()
        } else {
            val cb = addImageButton(R.drawable.go_down_inverse, controlSize)
            cb.setOnClickListener {
                ActivitySwitcher(
                    acontext
                ).cycle()
            }
        }
    }
}
