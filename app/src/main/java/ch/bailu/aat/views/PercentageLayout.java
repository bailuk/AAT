package ch.bailu.aat.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class PercentageLayout  extends ViewGroup {


    private static class Entry {
        public final View view;
        private final int percentage;

        public Entry(View v, int p) {
            view = v;
            percentage = Math.max(p, 5);
        }

        public int getSize(int parent_size) {
            return (parent_size * percentage) / 100;
        }
    }

    private final ArrayList<Entry> list = new ArrayList(5);

    private int orientation= LinearLayout.VERTICAL;

    public PercentageLayout(Context context) {
        super(context);
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

        if (list.size() > 0) {
            wSpec = MeasureSpec.makeMeasureSpec (width,  MeasureSpec.EXACTLY);


            for (Entry e : list) {
                hSpec = MeasureSpec.makeMeasureSpec (e.getSize(height), MeasureSpec.EXACTLY);
                e.view.measure(wSpec, hSpec);
            }
        }

        setMeasuredDimension(width, height);
    }


    private void hMeasure(int wSpec, int hSpec) {
        final int width = MeasureSpec.getSize(wSpec);
        final int height = MeasureSpec.getSize(hSpec);

        if (list.size() > 0) {
            hSpec = MeasureSpec.makeMeasureSpec (height,  MeasureSpec.EXACTLY);


            for (Entry e : list) {
                wSpec = MeasureSpec.makeMeasureSpec (e.getSize(width), MeasureSpec.EXACTLY);
                e.view.measure(wSpec, hSpec);
            }
        }

        setMeasuredDimension(width, height);
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (orientation == LinearLayout.VERTICAL)
            vLayout(l,t,r,b);
        else hLayout(l,t,r,b);
    }

    private void vLayout(int l, int t, int r, int b) {
        if (list.size() > 0) {
            int parent_height=b-t;
            int parent_width =r-l;

            r=parent_width;
            l=t=0;

            for (Entry e : list) {
                int view_height=e.getSize(parent_height);

                b=t+view_height;

                e.view.layout(l, t, r, b);

                t+=view_height;
            }
        }
    }

    private void hLayout(int l, int t, int r, int b) {
        if (list.size() > 0) {
            int parent_height=b-t;
            int parent_width =r-l;


            t=l=0;
            b = parent_height;


            for (Entry e : list) {
                int view_width=e.getSize(parent_width);

                r=l+view_width;

                e.view.layout(l, t, r, b);

                l+=view_width;
            }
        }
    }



}
