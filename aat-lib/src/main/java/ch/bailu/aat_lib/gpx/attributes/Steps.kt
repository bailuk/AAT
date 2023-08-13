package ch.bailu.aat_lib.gpx.attributes

import ch.bailu.aat_lib.gpx.GpxPointNode

class Steps : GpxSubAttributes(
    Keys(
        StepCounterAttributes.KEY_INDEX_STEPS_RATE,
        StepCounterAttributes.KEY_INDEX_STEPS_TOTAL
    )
) {
    private var total = 0
    private var counting = 0
    override fun update(point: GpxPointNode, autoPause: Boolean): Boolean {
        val attr = point.getAttributes()
        if (attr.hasKey(StepCounterAttributes.KEY_INDEX_STEPS_TOTAL)) {
            val value = attr.getAsInteger(StepCounterAttributes.KEY_INDEX_STEPS_TOTAL)
            if (value < counting) {
                total += counting
            }
            counting = value
        }
        return autoPause
    }

    override fun get(keyIndex: Int): String {
        return getAsInteger(keyIndex).toString()
    }

    override fun getAsInteger(keyIndex: Int): Int {
        if (keyIndex == StepCounterAttributes.KEY_INDEX_STEPS_TOTAL) {
            return counting + total
        } else if (keyIndex == StepCounterAttributes.KEY_INDEX_STEPS_RATE) {
            return 0
        }
        return 0
    }
}
