package ch.bailu.aat_lib.description;


import ch.bailu.aat_lib.resources.Res;

public class TrackerStateDescription extends StateDescription {

    @Override
    public String getLabel() {
        return Res.str().tracker();
    }

}
