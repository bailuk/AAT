package ch.bailu.aat_lib.lib.filter_list;

public abstract class ListEntry {
    public abstract boolean isSelected();
    public abstract boolean isSummary();
    public abstract KeyList getKeys();
    public abstract String getSummaryKey();

    public abstract int getID();

    public abstract void select();
}
