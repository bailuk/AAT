package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.gpx.GpxInformation

class ContentDescriptions(vararg d: ContentDescription) : ContentDescription() {
    private val descriptions = arrayOf(*d)


    override fun getValue(): String {
        val value = StringBuilder()
        var del = ""
        for (d in descriptions) {
            value.append(del).append(d.getValue())
            val unit = d.getUnit()
            if (unit.isNotEmpty()) value.append(" ").append(unit)
            del = ", "
        }
        return value.toString()
    }

    override fun getLabel(): String {
        val label = StringBuilder()
        var del = ""
        for (d in descriptions) {
            label.append(del).append(d.getLabel())
            del = ", "
        }
        return label.toString()
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        for (d in descriptions) {
            d.onContentUpdated(iid, info)
        }
    }
}
