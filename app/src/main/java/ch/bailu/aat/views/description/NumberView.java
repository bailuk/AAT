package ch.bailu.aat.views.description;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.util.ui.AppTheme;

public class NumberView extends ViewGroup implements OnContentUpdatedInterface {

    private final TextView    label, number, unit;
    private final ContentDescription description;

    private final float defaultTextSize;


    public NumberView(ContentDescription data) {
        super(data.getContext());

        number = createLabel();
        number.setIncludeFontPadding(false);
        number.setTextColor(Color.WHITE);
        number.setTypeface(Typeface.create((String) null, Typeface.BOLD));


        label = createLabel();
        label.setTextColor(Color.LTGRAY);
        defaultTextSize = label.getTextSize();

        unit = createLabel();
        setDefaultUnitLabelColor();

        description=data;

        updateAllText();
    }

    public void setDefaultUnitLabelColor() {
        unit.setBackgroundColor(Color.DKGRAY);
        unit.setTextColor(Color.LTGRAY);
    }


    public void setHighlightUnitLabelColor() {
        unit.setBackgroundColor(AppTheme.getHighlightColor());
        unit.setTextColor(Color.BLACK);
    }

    public ContentDescription getDescription() {
        return description;
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
    public void onContentUpdated(int iid, GpxInformation info) {
        description.onContentUpdated(iid, info);
        updateAllText();
    }


    public void updateAllText() {
        number.setText(description.getValue());
        label.setText(description.getLabel());
        unit.setText(description.getUnit());
    }
}
