package ch.bailu.aat.services.cache.osm_features

import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.xml.parser.util.Stream
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFactory
import java.io.IOException

class MapFeaturesParser(
    assets: FocFactory,
    onParseFile: (String) -> Boolean,
    private val onHaveFeature: (MapFeaturesParser)->Unit
) {
    private val out = StringBuilder()
    private val outName = StringBuilder()
    private val outKey = StringBuilder()
    private val outValue = StringBuilder()

    var summarySearchKey = ""
        private set

    var summaryKey = ""
        private set

    var id = 0
        private set

    init {
        assets.toFoc(MAP_FEATURES_ASSET).foreach { child: Foc ->
            if (onParseFile(child.name)) {
                try {
                    parseFeatures(child)
                } catch (e: IOException) {
                    AppLog.e(this@MapFeaturesParser, e)
                }
            }
        }
    }

    fun addHtml(b: StringBuilder): StringBuilder {
        b.append(out)
        return b
    }

    val name: String
        get() = outName.toString()
    val key: String
        get() = outKey.toString()
    val value: String
        get() = outValue.toString()

    @Throws(IOException::class)
    private fun parseFeatures(file: Foc) {
        val stream = Stream(file)
        parseSummary(stream)
        haveSummary()
        while (!stream.haveEOF()) {
            parseFeature(stream)
            haveFeature()
        }
        stream.close()
    }

    @Throws(IOException::class)
    private fun parseFeature(stream: Stream) {
        parseKeyValue(stream)
        parseToEndOfParagraph(stream)
    }

    @Throws(IOException::class)
    private fun parseKeyValue(`in`: Stream) {
        // <b><a ...>key</a></b>=<a ...>value</a>
        parseBoldName(`in`, outKey)
        parseBoldName(`in`, outValue)
    }

    @Throws(IOException::class)
    private fun parseBoldName(`in`: Stream, outString: StringBuilder) {
        var state = 0
        while (state < 4) {
            `in`.read()
            if (`in`.haveEOF()) {
                break
            } else if (`in`.haveA('<'.code)) {
                state++
            } else if (`in`.haveA('b'.code) && state == 1) {
                state++
            } else if (`in`.haveA('>'.code) && state == 2) {
                state++
            } else if (state == 3) {
                parseName(`in`, outString)
                state++
            } else {
                state = 0
            }
            out.append(`in`.get().toChar())
        }
    }

    @Throws(IOException::class)
    private fun parseName(stream: Stream, outString: StringBuilder) {
        var state = 0
        var lock = 0
        while (state < 3) {
            if (stream.haveEOF()) {
                break
            } else if (stream.haveA('<'.code) && state == 0) {
                state++
                lock++
            } else if (stream.haveA('/'.code) && state == 1) {
                state++
            } else if (stream.haveA('b'.code) && state == 2) {
                state++
            } else {
                if (stream.haveA('>'.code)) {
                    lock--
                }
                state = 0
            }
            if (lock < 1 && !stream.haveA('>'.code)) outString.append(stream.get().toChar())
            out.append(stream.get().toChar())
            stream.read()
        }
    }

    @Throws(IOException::class)
    private fun parseString(`in`: Stream, outString: StringBuilder) {
        while (true) {
            if (`in`.haveEOF() || `in`.haveA('<'.code)) {
                break
            } else if (`in`.haveCharacter()) {
                outString.append(`in`.get().toChar())
            }
            out.append(`in`.get().toChar())
            `in`.read()
        }
    }

    @Throws(IOException::class)
    private fun parseSummary(`in`: Stream) {
        parseSummaryHeading(`in`)
        parseToEndOfParagraph(`in`)
    }

    private fun haveSummary() {
        summaryKey = outName.toString().lowercase()
        summarySearchKey = "_$summaryKey"
        onHaveFeature(this)
        resetFeature()
    }

    private fun haveFeature() {
        if (outKey.isNotEmpty()) onHaveFeature(this)
        resetFeature()
    }

    private fun resetFeature() {
        id++
        out.setLength(0)
        outKey.setLength(0)
        outValue.setLength(0)
        outName.setLength(0)
    }

    @Throws(IOException::class)
    private fun parseToEndOfParagraph(stream: Stream) {
        var state = 0
        while (state < 4) {
            stream.read()
            if (stream.haveEOF()) {
                break
            } else if (stream.haveA('<'.code) && state == 0) {
                state++
            } else if (stream.haveA('/'.code) && state == 1) {
                state++
            } else if (stream.haveA('p'.code) && state == 2) {
                state++
            } else if (stream.haveA('>'.code) && state == 3) {
                state++
            } else {
                state = 0
            }
            out.append(stream.get().toChar())
        }
    }

    @Throws(IOException::class)
    private fun parseSummaryHeading(`in`: Stream) {
        var state = 0
        while (state < 2) {
            `in`.read()
            if (`in`.haveEOF()) {
                break
            } else if (`in`.haveA('>'.code)) {
                state = 1
            } else if (state == 1) {
                parseString(`in`, outName)
                state = 2
            }
            out.append(`in`.get().toChar())
        }
    }

    companion object {
        private const val MAP_FEATURES_ASSET = "map-features"
    }
}
