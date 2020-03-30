package ch.bailu.aat.views.description;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.preferences.ConnectToSensorsView;

public class NumberView extends ViewGroup implements OnContentUpdatedInterface {

    private final TextView    label, number, unit;
    private final ContentDescription description;

    private final float defaultTextSize;

    private final UiTheme theme;


    public NumberView(ContentDescription data, UiTheme theme) {
        super(data.getContext());

        this.theme = theme;

        number = createLabel();
        number.setIncludeFontPadding(false);

        theme.header(number);

        number.setTypeface(Typeface.create((String) null, Typeface.BOLD));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            number.setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NONE);
        }

        label = createLabel();
        theme.content(label);
        defaultTextSize = label.getTextSize();

        unit = createLabel();
        setDefaultUnitLabelColor();

        description=data;

        updateAllText();
    }

    public void setDefaultUnitLabelColor() {
        theme.contentAlt(unit);
        theme.backgroundAlt(unit);
    }


    public void setHighlightUnitLabelColor() {
        setHighlightUnitLabelColor(theme.getHighlightColor());
    }


    public void setHighlightUnitLabelColor(int color) {
        unit.setBackgroundColor(color);
        unit.setTextColor(Color.BLACK);
    }

    public ContentDescription getDescription() {
        return description;
    }


    private TextView createLabel() {
        TextView view = new TextView(getContext());

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



            final float textSizeLimit = height /4f;
            final float textSize = Math.min(defaultTextSize, textSizeLimit);

            setTextSize(label, textSize);
            setTextSize(unit, textSize);


            label.measure(width,height);
            label.layout(l, t+margin, r, label.getMeasuredHeight()+margin);
            height-=label.getMeasuredHeight();

            unit.measure(width,height);
            unit.layout(l, b-unit.getMeasuredHeight()-margin, r, b-margin);
            height-=unit.getMeasuredHeight();

            height -= margin*2;
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
        label.setText(description.getLabelShort());
        unit.setText(description.getUnit());
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {

        super.setOnClickListener(listener);

        theme.button(this);
        setPadding(0,0,0,0);
    }



    public View requestOnClickSensorReconnect() {
        ToolTip.set(this, ConnectToSensorsView.LABEL);

        setOnClickListener(v -> {
            AppLog.i(v.getContext(), ConnectToSensorsView.LABEL);
            AppBroadcaster.broadcast(v.getContext(), AppBroadcaster.SENSOR_RECONNECT + InfoID.SENSORS);
        });
        return this;
    }
}
