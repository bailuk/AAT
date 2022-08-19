package ch.bailu.aat_lib.lib.filter_list;

import java.util.ArrayList;

public abstract class AbsFilterList<T> {
    private final ArrayList<T> visible = new ArrayList<>(100);
    private final ArrayList<T> all = new ArrayList<>(100);

    private KeyList filterKeys = new KeyList();

    public void filter(String s) {
        filterKeys = new KeyList(s);
        filterAll();
    }

    public void filterAll() {
        visible.clear();

        for (T t: all) {
            if (showElement(t, filterKeys))
                visible.add(t);
        }
    }

    public void filterMore() {
        for (int i = visible.size()-1; i > -1; i--) {
            if (!showElement(visible.get(i), filterKeys))
                visible.remove(i);
        }
    }

    private void showAll() {
        if (visible.size() != all.size()) {
            visible.clear();
            visible.addAll(all);
        }
    }

    public abstract boolean showElement(T t, KeyList keyList);

    public void add(T t) {
        all.add(t);

        if (showElement(t, filterKeys))
            visible.add(t);
    }

    public T getFromAll(int index) {
        return all.get(index);
    }

    public T getFromVisible(int index) {
        return visible.get(index);
    }


    public void clear() {
        visible.clear();
        all.clear();
    }

    public int sizeVisible() {
        return visible.size();
    }
    public int sizeAll() {
        return all.size();
    }
}
