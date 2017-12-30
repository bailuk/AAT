package ch.bailu.aat.views.description;

import android.content.Context;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.preferences.Storage;


public class MultiView extends ViewGroup {

    private final String solidKey;

    private final SparseArray<GpxInformation> informationMap =
            new SparseArray<>(5);



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


    public void setNext() {
        setActive(active+1);
    }
    
    public void setPrevious() {
        setActive(active-1);
    }


    public int getActive() {
        return active;
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
    }
    
    
    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        int width = MeasureSpec.getSize(wSpec);
        int height = MeasureSpec.getSize(hSpec);


        // As big as possible
        wSpec  = MeasureSpec.makeMeasureSpec (width,  MeasureSpec.EXACTLY);
        hSpec  = MeasureSpec.makeMeasureSpec (height,  MeasureSpec.EXACTLY);

        //int width=0,height=0;
        for (Page p : pages) {
            p.view.measure(wSpec, hSpec);
        }
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
        setActive(Storage.activity(getContext()).readInteger(solidKey + "_index"));
    }


    @Override
    public void onDetachedFromWindow() {
        Storage.activity(getContext()).writeInteger(solidKey + "_index",active);
        super.onDetachedFromWindow();
    }

}
