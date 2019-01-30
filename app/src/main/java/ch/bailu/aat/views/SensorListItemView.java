package ch.bailu.aat.views;

import android.widget.CheckBox;
import android.widget.LinearLayout;

import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.list.SensorListItem;

public class SensorListItemView extends LinearLayout {

    private final CheckBox checkBox;

    private SensorListItem item;

    private final ServiceContext scontext;

    public SensorListItemView(ServiceContext sc, SensorListItem i) {
        super(sc.getContext());
        scontext = sc;

        setOrientation(VERTICAL);

        checkBox = new CheckBox(getContext());
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
    }


    public void setItem(SensorListItem i) {
        item = i;

        checkBox.setEnabled(item.isSupported());
        checkBox.setChecked(item.isEnabled());
        checkBox.setText(item.toString());
    }
}