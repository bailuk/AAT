package ch.bailu.aat.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class PercentageLayout  extends ViewGroup {

    private final static int DEFAULT_SPACE=2;

    private final int space;

    private static class Entry {

        public final View view;
        private final int percentage;

        public Entry(View v, int p) {
            view = v;
            percentage = Math.max(p, 5);
        }

        public int getSize(int parent_size, int p100) {
            return ((parent_size * percentage) / p100);
        }

        public boolean isVisible() {
            return view.getVisibility() != GONE;
        }
    }

    private final ArrayList<Entry> list = new ArrayList<>(5);

    private int orientation= LinearLayout.VERTICAL;

    public PercentageLayout(Context context) {
        this(context, DEFAULT_SPACE);
    }

    public PercentageLayout(Context context, int space) {
        super(context);
        this.space = space;
    }

    public void setOrientation(int o) {
        orientation = o;
    }


    public PercentageLayout add(View v, int p) {
        list.add(new Entry(v, p));
        addView(v);
        return this;
    }

    public PercentageLayout add(View... views) {
        if (views.length >0) {
            int p = 100 / views.length;
            for (View v : views) add(v, p);
        }
        return this;
    }


    public static PercentageLayout add(Context c, View... views) {
        return new PercentageLayout(c).add(views);
    }


    @Override
    protected void onMeasure(int wSpec, int hSpec) {

        if (orientation == LinearLayout.VERTICAL)
            vMeasure(wSpec, hSpec);
        else
            hMeasure(wSpec, hSpec);
    }


    private void vMeasure(int wSpec, int hSpec) {
        final int width = MeasureSpec.getSize(wSpec);
        final int height = MeasureSpec.getSize(hSpec);
        final int p100 = get100Percent();

        if (list.size() > 0) {
            wSpec = MeasureSpec.makeMeasureSpec (width,  MeasureSpec.EXACTLY);

            for (Entry e : list) {
                if (e.isVisible()) {
                    hSpec = MeasureSpec.makeMeasureSpec(e.getSize(height, p100), MeasureSpec.EXACTLY);
                    e.view.measure(wSpec, hSpec);
                }
            }
        }

        setMeasuredDimension(width, height);
    }


    private void hMeasure(int wSpec, int hSpec) {
        final int width = MeasureSpec.getSize(wSpec);
        final int height = MeasureSpec.getSize(hSpec);
        final int p100 = get100Percent();

        if (list.size() > 0) {

            hSpec = MeasureSpec.makeMeasureSpec (height,  MeasureSpec.EXACTLY);


            for (Entry e : list) {
                if (e.isVisible()) {
                    wSpec = MeasureSpec.makeMeasureSpec(e.getSize(width, p100), MeasureSpec.EXACTLY);
                    e.view.measure(wSpec, hSpec);
                }
            }
        }

        setMeasuredDimension(width, height);
    }


    private int get100Percent() {
        int p=0;
        for (Entry e : list) {
            if (e.isVisible()) {
                p += e.percentage;
            }
        }
        return p;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (orientation == LinearLayout.VERTICAL)
            vLayout(l,t,r,b);
        else hLayout(l,t,r,b);
    }

    private void vLayout(int l, int t, int r, int b) {
        int p100 = get100Percent();

        if (list.size() > 0) {
            int parent_height=b-t;
            int parent_width =r-l;
            int s=0;

            r=parent_width;
            l=t=0;

            for (Entry e : list) {
                if (e.isVisible()) {
                    int view_height = e.getSize(parent_height, p100);

                    b = t + view_height;

                    e.view.layout(l, t + s, r, b);

                    t += view_height;
                    s = space;
                }
            }
        }
    }

    private void hLayout(int l, int t, int r, int b) {
        int p100 = get100Percent();

        if (list.size() > 0) {
            int parent_height=b-t;
            int parent_width =r-l;
            int s = 0;


            t=l=0;
            b = parent_height;

            for (Entry e : list) {
                if (e.isVisible()) {
                    int view_width = e.getSize(parent_width, p100);

                    r = l + view_width;

                    e.view.layout(l + s, t, r, b);

                    l += view_width;
                    s = space;
                }
            }
        }
    }
}
