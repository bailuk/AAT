package ch.bailu.aat_lib.resources

import ch.bailu.aat_lib.resources.generated.Strings
import ch.bailu.aat_lib.resources.generated.Strings_cs
import ch.bailu.aat_lib.resources.generated.Strings_de
import ch.bailu.aat_lib.resources.generated.Strings_fr
import ch.bailu.aat_lib.resources.generated.Strings_nl
import java.util.Locale

object Res {
    private val STR: Strings by lazy {  initStrings(Locale.getDefault().language) }

    @JvmStatic
    fun str(): Strings {
        return STR
    }

    private fun initStrings(lang: String): Strings {
        if (isLang(lang, "de")) {
            return Strings_de()
        }
        if (isLang(lang, "fr")) {
            return Strings_fr()
        }
        if (isLang(lang, "cs")) {
            return Strings_cs()
        }
        return if (isLang(lang, "nl")) {
            Strings_nl()
        } else Strings()
    }

    private fun isLang(lang: String, code: String): Boolean {
        return lang == Locale(code).language
    }
}
