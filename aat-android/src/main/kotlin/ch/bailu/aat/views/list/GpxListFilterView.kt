package ch.bailu.aat.views.list

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.preferences.file_list.SolidDirectoryQuery
import ch.bailu.aat_lib.resources.ToDo

class GpxListFilterView(
    context: Context,
    dispatcher: Dispatcher,
    sdirectory: SolidDirectoryQuery,
    theme: UiTheme
) {
    private val fileCountLabel = TextView(context).apply {
        theme.header(this)
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(20, 0, 20, 0)
        }
        dispatcher.addTarget({ _, info ->
            text = info.getGpxList().pointList.size().toString()
        }, InfoID.LIST_SUMMARY)
    }

    val layout = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        theme.background(this)

        addView(EditText(context).apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            hint = ToDo.translate("Filter list...")
            setText(sdirectory.solidNameFilter.getValueAsStringNonDef())
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s is Editable) {
                        sdirectory.solidNameFilter.setValueFromString(s.toString())
                    }
                }
            })
        })

        addView(fileCountLabel)
    }

    fun themify(theme: UiTheme) {
        theme.header(fileCountLabel)
    }

}
