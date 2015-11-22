package ch.bailu.aat.views;

import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.bailu.aat.preferences.SolidFilterFrom;
import ch.bailu.aat.preferences.SolidFilterTo;
import ch.bailu.aat.preferences.SolidLong;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.preferences.SolidTrackListFilter;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;


public class DateFilterView extends LinearLayout implements OnClickListener {
    
    private SolidTrackListFilter    sfilter;
    private Button         filter;
    
    public DateFilterView(Context context) {
        super(context);
        
        sfilter = new SolidTrackListFilter(context, new SolidPreset(context).getIndex());
        new DateSet(context, new SolidFilterFrom(context, new SolidPreset(context).getIndex()));
        new DateSet(context, new SolidFilterTo(context, new SolidPreset(context).getIndex()));

        filter = new Button(context);
        filter.setOnClickListener(this);
        setButtonText();
        addView(filter);
    }

    private void setButtonText() {
        filter.setText(sfilter.getLabel());
    }
    
    @Override
    public void onClick(View view) {
        if (sfilter.getIndex()==1) sfilter.setIndex(0);
        else sfilter.setIndex(1);
        
        setButtonText();
    }

    
    public class DateSet implements OnClickListener, OnDateSetListener {
        private Button        button;
        private java.text.DateFormat  formater;
        
        private GregorianCalendar calendar=new GregorianCalendar();
        
        private SolidLong slong;
        
        public DateSet(Context context, SolidLong l) {
            slong = l;
            formater = DateFormat.getDateFormat(context);
            calendar.setTimeInMillis(slong.getValue());
            
            button = new Button(context);
            button.setOnClickListener(this);
            updateButtonText();
            addView(button);
            
        }
        
        private void updateButtonText() {
            button.setText(formater.format(calendar.getTime()));
        }
        
        @Override
        public void onClick(View v) {
            DatePickerDialog picker=new DatePickerDialog(getContext(),
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
                    
            picker.show();
            
        }

        @Override
        public void onDateSet(DatePicker view, int y, int m, int d) {
            calendar.set(Calendar.YEAR, y);
            calendar.set(Calendar.MONTH, m);
            calendar.set(Calendar.DAY_OF_MONTH, d);
            slong.setValue(calendar.getTimeInMillis());
            updateButtonText();
        }

    }
}
