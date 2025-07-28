package ch.bailu.aat_lib.file.xml.parser.scanner

import ch.bailu.aat_lib.file.xml.parser.util.DateScanner
import ch.bailu.aat_lib.file.xml.parser.util.DoubleScanner
import ch.bailu.aat_lib.file.xml.parser.util.OnParsedInterface

class Scanner(time: Long) {
    val latitude: DoubleScanner = DoubleScanner(6)
    val longitude: DoubleScanner = DoubleScanner(6)
    val elevation: DoubleScanner = DoubleScanner(1)
    val id: DoubleScanner = DoubleScanner(0)

    @JvmField
    val dateTime = DateScanner(time)
    val referencer = References()
    val tags = Tags()

    @JvmField
    var wayParsed = OnParsedInterface.NULL

    @JvmField
    var routeParsed = OnParsedInterface.NULL

    @JvmField
    var trackParsed = OnParsedInterface.NULL
}
