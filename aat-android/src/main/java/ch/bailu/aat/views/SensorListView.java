package ch.bailu.aat.views;

import android.widget.LinearLayout;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.sensor.SensorService;
import ch.bailu.aat.services.sensor.list.SensorList;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;

public class SensorListView  extends LinearLayout implements OnContentUpdatedInterface {

    private final ServiceContext scontext;

    private final ArrayList<SensorListItemView> children = new ArrayList<>(10);

    private final UiTheme theme;

    public SensorListView(ServiceContext sc, UiTheme theme) {
        super(sc.getContext());
        setOrientation(VERTICAL);
        scontext = sc;

        this.theme = theme;
        updateViews();
    }

    @Override
    public void onContentUpdated(int iid, @Nonnull GpxInformation info) {
        if (iid == InfoID.SENSORS) {
            updateViews();
        }
    }

    private void updateViews() {
        new InsideContext(scontext) {

            @Override
            public void run() {
                SensorList sensorList = ((SensorService)scontext.getSensorService()).getSensorList();

                for (int i=0; i<sensorList.size(); i++) {
                    if (children.size() <= i) {
                        children.add(new SensorListItemView(scontext, sensorList.get(i), theme));
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
