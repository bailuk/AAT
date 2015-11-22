package ch.bailu.aat.views;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.TextView;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.gpx.GpxInformation;

public class NumberView extends TrackDescriptionView {

    private final TextView    label, number, unit;
    private final ContentDescription description;


    public NumberView(ContentDescription data, int filter) {
        super(data, TrackDescriptionView.DEFAULT_SOLID_KEY,filter);
        number = createLabel();
        number.setIncludeFontPadding(false);

        label = createLabel();
        unit = createLabel();
        unit.setBackgroundColor(Color.DKGRAY);

        description=data;

        updateAllText();
    }

    
    public NumberView(ContentDescription data) {
        this(data, INFO_ID_ALL);
    }

    private void setNumberFont(int size) {
        if (size != 0) {
            number.setTextColor(Color.WHITE);
            number.setTypeface(Typeface.create((String)null, Typeface.BOLD));
            number.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }
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

            label.measure(width,height); 
            label.layout(l, t+margin, r, label.getMeasuredHeight()+margin);
            height-=label.getMeasuredHeight();

            unit.measure(width,height);    
            unit.layout(l, b-unit.getMeasuredHeight()-margin, r, b-margin);
            height-=unit.getMeasuredHeight();

            setNumberFont(height);

            number.measure(width, height);
            number.layout(l, label.getMeasuredHeight()+margin, r, b-unit.getMeasuredHeight()-margin);
        }
    }


    
    @Override
    public void updateGpxContent(GpxInformation info) {
        if (filter.pass(info)) {
            description.updateGpxContent(info);
            updateAllText();
        }
    }

    private void updateAllText() {
        number.setText(description.getValue());
        label.setText(description.getLabel());
        unit.setText(description.getUnit());
    }

    @Override
    public void cleanUp() {}
}
