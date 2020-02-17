package ch.bailu.aat.views.osm_features;

import ch.bailu.aat.util.filter_list.ListEntry;

public interface OnSelected {
    int EDIT = 0;
    int SHOW = 1;
    int FILTER = 2;
    int SELECT = 3;

    OnSelected NULL = (e, action, variant) -> {

    };


    void onSelected(ListEntry e, int action, String variant);
}
