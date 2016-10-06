package ch.bailu.aat.views.preferences;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.bailu.aat.preferences.SolidDate;

public class SolidDateView extends AbsSolidView {
    private final SolidDate sdate;

    public SolidDateView(SolidDate s) {
        super(s);
        sdate = s;
    }

    @Override
    public void onRequestNewValue() {
        final GregorianCalendar calendar = new GregorianCalendar();

        calendar.setTimeInMillis(sdate.getValue());

        DatePickerDialog picker=new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int y, int m, int d) {
                        calendar.set(Calendar.YEAR, y);
                        calendar.set(Calendar.MONTH, m);
                        calendar.set(Calendar.DAY_OF_MONTH, d);
                        sdate.setValue(calendar.getTimeInMillis());
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        picker.show();
    }
}
