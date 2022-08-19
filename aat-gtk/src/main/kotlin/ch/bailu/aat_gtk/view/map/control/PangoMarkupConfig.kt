package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_lib.html.MarkupConfig

object PangoMarkupConfig : MarkupConfig {
    override fun getNewLine(): String {
        return "\n"
    }

    override fun getBoldOpen(): String {
        return MarkupConfig.HTML.boldOpen
    }

    override fun getBoldClose(): String {
        return MarkupConfig.HTML.boldClose
    }

    override fun getBigOpen(): String {
        return "<big><b>"
    }

    override fun getBigClose(): String {
        return "</b></big>"
    }
}
