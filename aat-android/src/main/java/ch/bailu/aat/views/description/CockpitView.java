package ch.bailu.aat.views.description;


import android.content.Context;
import android.view.ViewGroup;

import java.util.ArrayList;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.location.SolidProvideAltitude;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat_lib.description.AltitudeConfigurationDescription;
import ch.bailu.aat_lib.description.CadenceDescription;
import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.description.HeartRateDescription;
import ch.bailu.aat_lib.description.PowerDescription;
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.view.cockpit.Layouter;


public class CockpitView extends ViewGroup {

    private final Layouter layouter;

    private final UiTheme theme;
    private final StorageInterface storage;

    private final ArrayList<ContentDescription> contentDescriptions = new ArrayList<>();

    public CockpitView(Context context, UiTheme theme) {
        super(context);
        this.theme = theme;
        this.storage = new Storage((context));
        theme.background(this);

        layouter = new Layouter(contentDescriptions,
                (index, x, y, x2, y2) -> getChildAt(index).layout(x,y,x2,y2));
    }


    public NumberView add(DispatcherInterface di, ContentDescription de) {
        return add(di, de, InfoID.TRACKER);
    }


    public NumberView addC(DispatcherInterface di, ContentDescription de, int... iid) {
        return _addView(di, new ColorNumberView(getContext(), de, theme), iid);
    }


    public NumberView add(DispatcherInterface di, ContentDescription de, int... iid) {
        return _addView(di, new NumberView(getContext(),de, theme), iid);
    }


    private NumberView _addView(DispatcherInterface di, NumberView v, int... iid) {
        addView(v);
        di.addTarget(v, iid);
        contentDescriptions.add(v.getDescription());
        return v;
    }


    public void addAltitude(DispatcherInterface di) {
        NumberView v = add(di, new AltitudeConfigurationDescription(storage), InfoID.LOCATION);
        SolidProvideAltitude.requestOnClick(v);
    }

    public void addHeartRate(DispatcherInterface di) {
        NumberView v = add(di, new HeartRateDescription(), InfoID.HEART_RATE_SENSOR);
        v.requestOnClickSensorReconnect();
    }

    public void addPower(DispatcherInterface di) {
        NumberView v = add(di, new PowerDescription(), InfoID.POWER_SENSOR);
        v.requestOnClickSensorReconnect();
    }

    public void addCadence(DispatcherInterface di) {
        NumberView v = add(di, new CadenceDescription(), InfoID.CADENCE_SENSOR);
        v.requestOnClickSensorReconnect();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            layouter.layout(r-l,b-t);
        }
    }
}
