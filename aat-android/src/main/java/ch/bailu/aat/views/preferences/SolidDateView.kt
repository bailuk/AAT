package ch.bailu.aat.views.preferences

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat_lib.preferences.SolidDate
import java.util.Calendar
import java.util.GregorianCalendar

class SolidDateView(context: Context, private val sdate: SolidDate, theme: UiTheme) :
    AbsSolidView(context, sdate, theme) {
    override fun onRequestNewValue() {
        val calendar = GregorianCalendar()
        calendar.timeInMillis = sdate.value
        val picker = DatePickerDialog(
            context,
            { _: DatePicker, y: Int, m: Int, d: Int ->
                calendar[Calendar.YEAR] = y
                calendar[Calendar.MONTH] = m
                calendar[Calendar.DAY_OF_MONTH] = d
                sdate.value = calendar.timeInMillis
            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        picker.show()
    }
}
