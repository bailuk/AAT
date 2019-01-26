package ch.bailu.aat.views;

import android.widget.LinearLayout;

import java.util.ArrayList;

import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.list.SensorList;

public class SensorListView  extends LinearLayout implements OnContentUpdatedInterface {


    private final ServiceContext scontext;


    private final ArrayList<SensorListItemView> children = new ArrayList<>(10);

    public SensorListView(ServiceContext sc) {
        super(sc.getContext());
        setOrientation(VERTICAL);
        scontext = sc;

        updateViews();

    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        if (iid == InfoID.SENSORS) {
            updateViews();
        }
    }

    private void updateViews() {
        new InsideContext(scontext) {

            @Override
            public void run() {
                SensorList sensorList = scontext.getSensorService().getSensorList();

                for (int i=0; i<sensorList.size(); i++) {
                    if (children.size() <= i) {
                        children.add(new SensorListItemView(scontext, sensorList.get(i)));
                        addView(children.get(i));
                    } else {
                        children.get(i).setItem(sensorList.get(i));
                    }


                }

                for (int i = children.size() -1; i >= sensorList.size(); i--) {
                    removeView(children.get(i));
                    children.remove(i);
                }
            }
        };
    }
}

