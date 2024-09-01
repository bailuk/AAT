package ch.bailu.aat_lib.html

interface MarkupConfig {
    val newLine: String
    val boldOpen: String
    val boldClose: String
    val bigOpen: String
    val bigClose: String

    companion object {
        val HTML: MarkupConfig = object : MarkupConfig {
            override val newLine: String
                get() = "<br>"

            override val boldOpen: String
                get() = "<b>"

            override val boldClose: String
                get() = "</b>"

            override val bigOpen: String
                get() = "<h3>"

            override val bigClose: String
                get() = "</h3>"
        }
    }
}
