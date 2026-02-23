package ch.bailu.aat_lib.service.icons

import ch.bailu.aat_lib.util.WithStatusText

class IconMap : WithStatusText {

    class Icon(fileName: String) {
        val svg = IconMapService.SVG_DIRECTORY + fileName + IconMapService.SVG_SUFFIX
    }

    private val keyList = HashMap<Int, HashMap<String, Icon>>()

    fun add(key: Int, value: String, fileName: String) {
        val valueList = keyList.get(key) ?: HashMap()
        valueList.put(value, Icon(fileName))
        keyList.put(key, valueList)
    }

    fun get(keyIndex: Int, value: String): Icon? {
        val valueList = keyList.get(keyIndex)

        if (valueList == null) {
            return null
        }
        return valueList[value]
    }

    override fun appendStatusText(builder: StringBuilder) {
        builder.append("IconMap (key_list) size: ").append(keyList.size).append("<br>")
    }
}
