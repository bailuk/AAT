package ch.bailu.aat_lib.file

import ch.bailu.aat_lib.file.xml.parser.util.Stream
import ch.bailu.foc.Foc

class FileType(file: Foc)  {
    val isJSON: Boolean
    val isXML: Boolean

    init {
        BOM.open(file).use {
            val stream = Stream(it)
            stream.read()
            stream.skipWhitespace()
            isJSON = stream.haveA('{'.code) || stream.haveA('['.code)
            isXML = stream.haveA('<'.code)
        }
    }
}
