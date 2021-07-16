package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.gpx.interfaces.GpxDeltaInterface;
import ch.bailu.aat.services.sensor.attributes.CadenceSpeedAttributes;
import ch.bailu.aat.services.sensor.list.SensorState;

public class CurrentSpeedDescription extends SpeedDescription {

    private int currentIID=-1;
    private String label;


    public CurrentSpeedDescription(Context context) {
        super(context);
        setLabel();
    }

    private void setLabel() {

        if (currentIID == InfoID.SPEED_SENSOR) {
            label = getContext().getString(R.string.speed) + " S";

        } else if (currentIID == InfoID.LOCATION) {
            label = getContext().getString(R.string.speed) + " GPS";
        } else {
            label = getContext().getString(R.string.speed);
        }
    }


    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {

        if (configure(iid, info.getAttributes())) {
            if (setSpeedFromLastPoint(info) == false) {
                setCache(info.getSpeed());
            }
        }



    }

    private boolean configure(int iid, GpxAttributes attr) {
        if (currentIID != iid) {
            if (changeSource(iid, attr)) {
                setLabel();
            }
        }
        return currentIID == iid;
    }


    private boolean changeSource(int iid, GpxAttributes attr) {
        boolean haveSensor = SensorState.isConnected(InfoID.SPEED_SENSOR);

        if (currentIID == InfoID.SPEED_SENSOR && haveSensor) {
            return false;

        } else if (iid == InfoID.SPEED_SENSOR) {
            boolean isSensorReady = haveSensor && attr.getAsBoolean(CadenceSpeedAttributes.KEY_INDEX_CONTACT);

            if (isSensorReady) {
                currentIID = iid;
                return true;

            } else {
                return false;
            }

        } else {
            currentIID = iid;
            return true;

        }

    }

    private boolean setSpeedFromLastPoint(GpxInformation info) {
        final GpxList track = info.getGpxList();

        if (track != null) {
            if (track.getPointList().size() > 0) {
                final GpxDeltaInterface delta = ((GpxDeltaInterface) info.getGpxList().getPointList().getLast());
                if (delta != null) {
                    setCache(delta.getSpeed());
                    return true;
                }
            }

        }
        return false;
    }
}