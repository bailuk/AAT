package ch.bailu.aat.util.filter_list;

import java.util.ArrayList;

public abstract class FilterList<T> {
    private final ArrayList<T> show = new ArrayList<>(100);
    private final ArrayList<T> all = new ArrayList<>(100);


    private KeyList filterKeys = new KeyList();


    public void filterAll() {
        show.clear();

        for (T t: all) {
            if (showElement(t, filterKeys))
                show.add(t);
        }
    }


    public void filterMore() {
        for (int i = show.size()-1; i > -1; i--) {
            if (showElement(show.get(i), filterKeys) == false)
                show.remove(i);
        }
    }


    public void filter(String s) {
        KeyList old = filterKeys;
        filterKeys = new KeyList(s);

        if (filterKeys.isEmpty()) {
            showAll();
        } else if (filterKeys.fits(old)) {
            filterMore();
        } else {
            filterAll();
        }

    }
    private void showAll() {
        if (show.size() != all.size()) {
            show.clear();
            for (T t : all) {
                show.add(t);
            }
        }
    }


    public abstract boolean showElement(T t, KeyList keyList);


    public void add(T t) {
        all.add(t);

        if (showElement(t, filterKeys))
            show.add(t);
    }

    public int size() {
        return show.size();
    }

    public T get(int index) {
        return show.get(index);
    }


    public void clear() {
        show.clear();
        all.clear();
    }
}
