package ch.bailu.aat_lib.description;


import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.StateID;
import ch.bailu.aat_lib.resources.Res;

public abstract class StateDescription extends ContentDescription {




    private int state = StateID.OFF;

    @Override
    public String getLabel() {
        return "R.string.d_state";
    }

    public String getValue() {
        String result;

        switch (state) {
            case StateID.NOACCESS: result = Res.str().gps_noaccess(); break;
            case StateID.NOSERVICE: result = Res.str().gps_nogps(); break;
            case StateID.ON: result = Res.str().on(); break;
            case StateID.OFF: result = Res.str().off(); break;
            case StateID.PAUSE: result = Res.str().status_paused(); break;
            case StateID.AUTOPAUSED: result = Res.str().status_autopaused(); break;
            default: result = Res.str().gps_wait(); break;
        }
        return result;
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        state=info.getState();
    }
}
