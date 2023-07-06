package ch.bailu.aat_lib.lib.filter_list;

public abstract class AbsListItem {
    public abstract boolean isSelected();
    public abstract void setSelected(boolean selected);
    public abstract boolean isSummary();
    public abstract KeyList getKeys();
    public abstract String getSummaryKey();
    public abstract int getID();
}
