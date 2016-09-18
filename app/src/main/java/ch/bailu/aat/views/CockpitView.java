package ch.bailu.aat.views;


import android.annotation.SuppressLint;
import android.content.Context;

import ch.bailu.aat.description.*;
import ch.bailu.aat.gpx.GpxInformation;


public class CockpitView extends DescriptionViewGroup {


    public CockpitView(Context context, String key,  ContentDescription d[]) {
        this(context, key, GpxInformation.ID.INFO_ID_ALL, d);
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
    
    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            new Layouter(r,b);
        }

    }
    
    private class Layouter {
        private final int MAX_CHARS_PER_LINE=50;
        private final int parent_width;
        private final int parent_height;
        
        public Layouter(int w, int h) {
            int chars=1;
            
            parent_width=w; parent_height=h;
            
            while (chars<MAX_CHARS_PER_LINE) {
                if (tryPlacement(chars)) {
                    doPlacement(chars);
                    break;
                }
                else chars++;
            }
        }
        
        private boolean tryPlacement(int charsPerLine) {
            return placeItems(new Placer(charsPerLine));
        }
        
        private boolean doPlacement(int charsPerLine) {
            return placeItems(new RealPlacer(charsPerLine));
        }

        private boolean placeItems(Placer p) {
            boolean works=true;
        
            for (int i=0; i<getDescriptionCount() && works; i++) {
                works = p.placeItem(i);
            }
            return works;
        }
        
        private class Placer {
            private final int MIN_CHARS = 4;
            private final int RATIO=3;
            private int char_width, char_height, xpos, ypos;
            
            
            public Placer(int charsPerLine) {
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
            public RealPlacer(int charsPerLine) {
                super(charsPerLine);
            }

            @Override
            protected void setGeometry(int index, int width, int height) {
                getDescriptionView(index).layout(getXPos(),getYPos(), getXPos()+width, getYPos()+height);
            }
        }

    }
    
  
}
