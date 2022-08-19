package ch.bailu.aat_gtk.lib.css

import ch.bailu.gtk.gdk.Display
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str
import java.util.*

object CSS {
    private val styleProviderMap = HashMap<String, StyleProvider>()

    fun addProviderForDisplay(display: Display, cssResource: String) {
        val styleProvider = getStyleProvider(cssResource)
        StyleContext.addProviderForDisplay(display, styleProvider, GtkConstants.STYLE_PROVIDER_PRIORITY_USER)
    }

    private fun getStyleProvider(cssResource: String): StyleProvider {
        return styleProviderMap.getOrPut(cssResource) {
            StyleProvider(CssProvider().apply {
                val str = Str(resourceToString(cssResource))
                loadFromData(str, -1)
                str.destroy()
            }.cast())
        }
    }

    private fun resourceToString(name: String): String {
        try {
            javaClass.classLoader.getResourceAsStream(name)?.let { inputStream->
                Scanner(inputStream, "UTF-8")
                    .useDelimiter("\\A").use { scanner->
                        return scanner.next()
                    }
            }
        } catch(e: Exception) {
            println(e.message)
        }
        return ""
    }
}
