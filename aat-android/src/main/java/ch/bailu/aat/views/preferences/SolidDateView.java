package ch.bailu.aat.views.preferences;

import android.app.DatePickerDialog;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.bailu.aat.preferences.SolidDate;
import ch.bailu.aat.util.ui.UiTheme;

public class SolidDateView extends AbsSolidView {
    private final SolidDate sdate;

    public SolidDateView(SolidDate s, UiTheme theme) {
        super(s, theme);
        sdate = s;
    }

    @Override
    public void onRequestNewValue() {
        final GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTimeInMillis(sdate.getValue());

        DatePickerDialog picker=new DatePickerDialog(getContext(),
                (view, y, m, d) -> {
                    calendar.set(Calendar.YEAR, y);
                    calendar.set(Calendar.MONTH, m);
                    calendar.set(Calendar.DAY_OF_MONTH, d);
                    sdate.setValue(calendar.getTimeInMillis());
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        picker.show();
    }
}
