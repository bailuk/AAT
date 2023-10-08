package ch.bailu.aat_lib.preferences.presets

import ch.bailu.aat_lib.exception.ValidationException
import ch.bailu.aat_lib.preferences.SolidString
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import java.util.Collections
import javax.annotation.Nonnull

class SolidMET(storage: StorageInterface, private val preset: Int) : SolidString(
    storage, SolidMET::class.java.simpleName + "_" + preset
) {
    @Nonnull
    override fun getLabel(): String {
        return Res.str().p_met()
    }

    @Nonnull
    override fun getValueAsString(): String {
        var result = super.getValueAsString()
        if (getStorage().isDefaultString(result)) {
            result = defaultValue
        }
        return result
    }

    private val defaultValue: String
        get() {
            val metList = Res.str().p_met_list()
            return if (preset < metList.size) {
                metList[preset]
            } else {
                metList[0]
            }
        }

    val metValue: Float
        get() {
            val `val` = getValueAsString()
            val from = 0
            val to = `val`.indexOf(' ')
            var r = 0f
            if (to > from) {
                r = try {
                    val met = `val`.substring(from, to)
                    met.toFloat()
                } catch (e: NumberFormatException) {
                    0f
                }
            }
            if (r > 20f || r < 0f) {
                r = 0f
            }
            return r
        }

    @Throws(ValidationException::class)
    override fun setValueFromString(string: String) {
        val trimmedString = string.trim { it <= ' ' }
        if (validate(trimmedString)) {
            setValue(trimmedString)
        } else {
            throw ValidationException(Res.str().error_met())
        }
    }

    override fun buildSelection(list: ArrayList<String>): ArrayList<String> {
        val array = Res.str().p_met_list()
        Collections.addAll(list, *array)
        return list
    }

    override fun validate(s: String): Boolean {
        // entering 0.0 is still possible
        return s.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[0].matches("1?[0-9].[0-9]|20.0".toRegex())
    }
}
