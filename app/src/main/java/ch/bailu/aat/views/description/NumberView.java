package ch.bailu.aat.views.description;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.TextView;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;

public class NumberView extends TrackDescriptionView {

    private final TextView    label, number, unit;
    private final ContentDescription description;

    private final float defaultTextSize;


    public NumberView(ContentDescription data, int filter) {
        super(data, TrackDescriptionView.DEFAULT_SOLID_KEY,filter);


        number = createLabel();
        number.setIncludeFontPadding(false);
        number.setTextColor(Color.WHITE);
        number.setTypeface(Typeface.create((String) null, Typeface.BOLD));


        label = createLabel();
        label.setTextColor(Color.LTGRAY);
        defaultTextSize = label.getTextSize();

        unit = createLabel();
        unit.setBackgroundColor(Color.DKGRAY);
        unit.setTextColor(Color.LTGRAY);
        
        description=data;

        updateAllText();
    }

    
    public NumberView(ContentDescription data) {
        this(data, InfoID.ALL);
    }




    private TextView createLabel() {
        TextView view = new TextView(getContext());

        view.setLines(1);
        view.setPadding(0, 0, 0, 0);
        addView(view);
        return view;
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {


            int height=(b-t);
            int width=(r-l);

            int margin=height/10;

            l=t=0; b=height; r=width;

            height-=margin*2;

            final float textSizeLimit = height /4;
            final float textSize = Math.min(defaultTextSize, textSizeLimit);

            setTextSize(label, textSize);
            setTextSize(unit, textSize);


            label.measure(width,height);
            label.layout(l, t+margin, r, label.getMeasuredHeight()+margin);
            height-=label.getMeasuredHeight();

            unit.measure(width,height);    
            unit.layout(l, b-unit.getMeasuredHeight()-margin, r, b-margin);
            height-=unit.getMeasuredHeight();

            setTextSize(number, height);

            number.measure(width, height);
            number.layout(l, label.getMeasuredHeight()+margin, r, b-unit.getMeasuredHeight()-margin);
        }
    }


    private static void setTextSize(TextView v, float s) {
        if (s > 0) {
            v.setTextSize(TypedValue.COMPLEX_UNIT_PX, s);
        }
    }


    @Override
    public void onContentUpdated(GpxInformation info) {
        if (filter.pass(info)) {
            description.onContentUpdated(info);
            updateAllText();
        }
    }


    public void updateAllText() {
        number.setText(description.getValue());
        label.setText(description.getLabel());
        unit.setText(description.getUnit());
    }
}
