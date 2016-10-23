package ch.bailu.aat.views.description;


import android.content.Context;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;


public class CockpitView extends DescriptionViewGroup {

    private final Layouter layouter = new Layouter();

    public CockpitView(Context context, String key,  ContentDescription d[]) {
        this(context, key, InfoID.ALL, d);
    }

    public CockpitView(Context context, String key, int filter, ContentDescription d[]) {
        super(context, key, filter);

        TrackDescriptionView v[] = new TrackDescriptionView[d.length];

        for (int i=0; i<d.length; i++) {
            v[i] = new NumberView(d[i]);
            addView(v[i]);
        }

        init(d,v);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            layouter.layout(r,b);
        }

    }

    private class Layouter {
        private static final int MAX_CHARS_PER_LINE=50;
        private int parent_width=0;
        private int parent_height=0;

        private final Placer placer = new Placer();
        private final Placer realPlacer = new RealPlacer();


        public void layout(int w, int h) {
            int chars = 1;

            parent_width = w;
            parent_height = h;

            while (chars < MAX_CHARS_PER_LINE) {
                if (tryPlacement(chars)) {
                    doPlacement(chars);
                    break;
                } else chars++;
            }
        }




        private boolean tryPlacement(int charsPerLine) {
            placer.place(charsPerLine);
            return placeItems(placer);
        }

        private boolean doPlacement(int charsPerLine) {
            realPlacer.place(charsPerLine);
            return placeItems(realPlacer);
        }

        private boolean placeItems(Placer p) {
            boolean works=true;

            for (int i=0; i<getDescriptionCount() && works; i++) {
                works = p.placeItem(i);
            }
            return works;
        }

        private class Placer {
            private static final int MIN_CHARS = 4;
            private static final int RATIO=3;
            private int char_width, char_height, xpos, ypos;


            public void place(int charsPerLine) {
                xpos=ypos=0;
                calculateCharGeometry(charsPerLine);
            }

            private void calculateCharGeometry(int charsPerLine) {
                char_width=parent_width/charsPerLine;

                char_height=char_width*RATIO;
                if (char_height > parent_height) {
                    char_height=parent_height;
                    char_width=char_height/RATIO;
                }
            }


            public boolean placeItem(int index) {
                boolean works=true;
                int width=getWidthOfView(index);

                if (width>parent_width) {
                    works=false;
                } else if (width+xpos > parent_width) {
                    works=addLine();
                    if (works) works = placeItem(index);
                } else {
                    setGeometry(index, width, char_height);
                    xpos += width;
                }
                return works;
            }

            protected void setGeometry(int index, int width, int height) {}

            protected int getXPos() {return xpos;}
            protected int getYPos() {return ypos;}

            private boolean addLine() {
                ypos+=char_height;
                xpos=0;
                return ((char_height + ypos) <= parent_height);
            }

            private int getWidthOfView(int index) {
                int len= getDescription(index).getValue().length();
                len = Math.max(len, MIN_CHARS);
                return len*char_width;
            }
        }


        private class RealPlacer extends Placer {


            @Override
            protected void setGeometry(int index, int width, int height) {
                getDescriptionView(index).layout(getXPos(),getYPos(), getXPos()+width, getYPos()+height);
            }
        }

    }


}
