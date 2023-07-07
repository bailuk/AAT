package ch.bailu.aat_lib.description;


import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.resources.Res;

public class NameDescription extends ContentDescription {
    private String name = "";

    @Override
    public String getLabel() {
        return Res.str().d_name();
    }

    @Override
    public String getUnit() {
        return "";
    }

    public String getValue() {
        return name;
    }

    public boolean updateName(String s) {
        boolean r = !name.equals(s);
        name=s;
        return r;
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        updateName(info.getFile().getName());
    }

}
