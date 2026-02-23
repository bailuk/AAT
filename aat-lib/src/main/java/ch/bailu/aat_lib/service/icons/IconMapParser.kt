package ch.bailu.aat_lib.service.icons

import ch.bailu.aat_lib.file.xml.parser.util.Stream
import ch.bailu.aat_lib.gpx.attributes.Keys
import ch.bailu.foc.Foc
import java.io.IOException

class IconMapParser(file: Foc, map: IconMap) {
    private val entries = arrayOfNulls<String>(MAX)
    private var entry = 0
    private val buffer = StringBuilder()

    init {
        val stream = Stream(file)
        stream.read()

        while (!stream.haveEOF()) {
            if (stream.haveA('#'.code)) {
                stream.to('\n'.code)
            } else if  (stream.haveA('\n'.code)) {
                addEntry(map)
                stream.read()
            } else if (stream.haveCharacter()) {
                parseSubEntry(stream)
            } else {
                stream.read()
            }
        }
    }

    @Throws(IOException::class)
    private fun parseSubEntry(stream: Stream) {
        buffer.setLength(0)

        while (stream.haveA('_'.code) || stream.haveA('/'.code) || stream.haveCharacter() || stream.haveDigit()) {
            buffer.append(stream.get().toChar())
            stream.read()
        }

        entries[entry] = buffer.toString()

        if (entry < END) {
            entry++
        }
    }

    private fun addEntry(map: IconMap) {
        if (entry == END) {
            val key = entries[KEY]
            val value =  entries[VALUE]
            val icon = entries[ICON]
            if (key != null && value != null && icon != null) {
                map.add(Keys.toIndex(key), value, icon)
            }
        }
        entry = 0
    }

    companion object {
        private const val ICON = 0
        private const val KEY = 1
        private const val VALUE = 2
        private const val END = 3
        private const val MAX = 4
    }
}
