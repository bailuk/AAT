package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID

/**
 * Composite that delegates to two [ContentDescription] instances.
 *
 * [getValue]/[getLabel] expose the first description; [getUnit] merges the
 * second description's label and value with the first's unit
 * ("secondLabel: secondValue firstUnit"). Both descriptions receive every
 * [onContentUpdated] call so they stay in sync.
 *
 * When the two sub-descriptions need different [InfoID] updates (e.g.
 * [PowerDescription] needs POWER_SENSOR while a window-power description
 * needs TRACKER), pass [firstIid]/[secondIid] to filter updates per
 * sub-description. [InfoID.ALL] (the default) disables filtering.
 */
class TwoContentDescriptions(
    private val first: ContentDescription,
    private val second: ContentDescription,
    private val firstIid: Int = InfoID.ALL,
    private val secondIid: Int = InfoID.ALL
) : ContentDescription() {
    override fun getValue(): String {
        return first.getValue()
    }

    override fun getLabel(): String {
        return first.getLabel()
    }

    override fun getUnit(): String {
        val value = second.getValue()
        val unit = first.getUnit()

        if (value == "")
            /* there is no secondary value: show just the unit */
            return unit

        /* there is a secondary value: show the second label, value and
           the unit in the bottom row */
        var label = second.getLabel()
        return "$label: $value $unit" }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (firstIid == InfoID.ALL || iid == firstIid)
            first.onContentUpdated(iid, info)

        if (secondIid == InfoID.ALL || iid == secondIid)
            second.onContentUpdated(iid, info)
    }
}
