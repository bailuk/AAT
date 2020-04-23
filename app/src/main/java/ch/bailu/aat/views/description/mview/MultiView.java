package ch.bailu.aat.views.description.mview;

import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import org.mapsforge.map.model.common.ObservableInterface;
import org.mapsforge.map.model.common.Observer;

import java.util.ArrayList;

import ch.bailu.aat.preferences.Storage;


public class MultiView extends ViewGroup implements ObservableInterface{

    private final String solidKey;

    private final ArrayList<Observer> observers = new ArrayList(2);

    @Override
    public void addObserver(Observer o) {
        observers.remove(o);
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public String getLabel() {
        if (pages.size()>0)
            return pages.get(active).label;

        return "";
    }


    public class Page {
        final public View view;
        final public String label;

        public Page(View v, String l) {
            view = v; label = l;

            view.setVisibility(GONE);
            addView(view);
        }
    }



    private final ArrayList<Page> pages = new ArrayList<>(5);
    private int active=0;


    public MultiView(Context context, String key) {
        super(context);
        solidKey = key;
    }


    public void inflateMenu(Menu menu) {
        for (int i = 0 ; i< pages.size(); i++) {
            menu.add(Menu.NONE, i, Menu.NONE, pages.get(i).label);
        }
    }

    public void add(View view, String label) {
        pages.add(new Page(view, label));
    }


    public void add(View view ) {
        pages.add(new Page(view, ""));
    }


    public void remove(int i) {
        if (i < pages.size() && i > -1) {
            pages.remove(i);
        }
    }

    public void remove(View view) {
        for (int i = pages.size()-1 ; i>=0; i--) {
            if (pages.get(i).view == view) {
                pages.remove(i);
            }
        }
    }


    public int pageCount() {
        return pages.size();
    }

    public void setNext() {
        setActive(active+1);
    }

    public void setPrevious() {
        setActive(active-1);
    }


    public void setActive(int a) {
        if (a != active) {
            pages.get(active).view.setVisibility(GONE);
        }

        active=a;
        if (active >= pages.size()) active=0;
        else if (active < 0) active= pages.size()-1;


        pages.get(active).view.setVisibility(VISIBLE);
        pages.get(active).view.bringToFront();

        for (Observer observer : observers) {
            observer.onChange();
        }
    }


    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        onMeasureBiggestWins(wSpec, hSpec);
    }


    private void onMeasureAsBigAsPossible(int wSpec, int hSpec) {
        int width = MeasureSpec.getSize(wSpec);
        int height = MeasureSpec.getSize(hSpec);

        wSpec  = MeasureSpec.makeMeasureSpec (width,  MeasureSpec.EXACTLY);
        hSpec  = MeasureSpec.makeMeasureSpec (height,  MeasureSpec.EXACTLY);

        for (Page p : pages) {
            p.view.measure(wSpec, hSpec);
        }
        setMeasuredDimension(width, height);
    }


    private void onMeasureBiggestWins(int wSpec, int hSpec) {
        int width = MeasureSpec.getSize(wSpec);
        int height = MeasureSpec.getSize(hSpec);
        int p_width=0, p_height=0;

        for (Page p : pages) {
            p.view.measure(wSpec, hSpec);
            p_width  = Math.max(p_width,  p.view.getMeasuredWidth());
            p_height = Math.max(p_height, p.view.getMeasuredHeight());
        }

        width = Math.min(width, p_width);
        height = Math.min(height, p_height);
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (Page p : pages) {
            p.view.layout(0, 0, r-l, b-t);
        }
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setActive(new Storage(getContext()).readInteger(solidKey + "_index"));
    }


    @Override
    public void onDetachedFromWindow() {
        storeActive(getContext(), solidKey, active);
        super.onDetachedFromWindow();
    }


    public int getActive() {
        return active;
    }

    public static void storeActive(Context context, String key, int active) {
        new Storage(context).writeInteger(key + "_index",active);
    }
}
