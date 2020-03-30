package ch.bailu.aat.views;

import android.graphics.Color;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.list.SensorListItem;
import ch.bailu.aat.util.ui.UiTheme;

public class SensorListItemView extends LinearLayout {

    private final CheckBox checkBox;

    private SensorListItem item;

    private final ServiceContext scontext;

    public SensorListItemView(ServiceContext sc, SensorListItem i, UiTheme theme) {
        super(sc.getContext());
        scontext = sc;

        setOrientation(VERTICAL);

        checkBox = new CheckBox(getContext());
        checkBox.setTextColor(Color.LTGRAY);
        addView(checkBox);

        setItem(i);


        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setEnabled(isChecked);

            new InsideContext(scontext) {
                @Override
                public void run() {
                    scontext.getSensorService().updateConnections();
                }
            } ;
        });

        theme.content(checkBox);
    }


    public void setItem(SensorListItem i) {
        item = i;

        checkBox.setEnabled(item.isSupported());
        checkBox.setChecked(item.isEnabled());
        checkBox.setText(item.toString());
    }
}