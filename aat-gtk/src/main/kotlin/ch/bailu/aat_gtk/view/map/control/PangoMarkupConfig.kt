package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_lib.html.MarkupConfig

object PangoMarkupConfig : MarkupConfig {

    override val newLine: String
        get() = "\n"

    override val boldOpen: String
        get() = MarkupConfig.HTML.boldOpen

    override val boldClose: String
        get() = MarkupConfig.HTML.boldClose

    override val bigOpen: String
        get() = "<big><b>"

    override val bigClose: String
        get() = "</b></big>\n"
}
