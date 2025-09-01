package ch.bailu.aat_lib.resources

import ch.bailu.aat_lib.resources.generated.Strings
import ch.bailu.aat_lib.resources.generated.Strings_cs
import ch.bailu.aat_lib.resources.generated.Strings_de
import ch.bailu.aat_lib.resources.generated.Strings_fr
import ch.bailu.aat_lib.resources.generated.Strings_nl
import java.util.Locale

object Res {
    private val STR: Strings by lazy {  initStrings(Locale.getDefault()) }

    @JvmStatic
    fun str(): Strings {
        return STR
    }

    private fun initStrings(locale: Locale): Strings {

        val language = locale.language.lowercase()
        if (language == "de") {
            return Strings_de()
        }
        if (language == "fr") {
            return Strings_fr()
        }
        if (language == "cs") {
            return Strings_cs()
        }
        if (language == "nl") {
            return Strings_nl()
        }
        return Strings()
    }
}
