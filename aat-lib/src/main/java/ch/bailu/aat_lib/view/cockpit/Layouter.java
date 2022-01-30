package ch.bailu.aat_lib.view.cockpit;

import java.util.List;

import ch.bailu.aat_lib.description.ContentDescription;

public class Layouter {
    private static final int MAX_CHARS_PER_LINE=50;
    private int parent_width=0;
    private int parent_height=0;

    private final Placer placer = new Placer((index, x, y, w, h) -> {});
    private final Placer realPlacer;

    private final List<ContentDescription> items;


    public Layouter(List<ContentDescription> items, DoPlacement doPlacement) {
        this.items = items;
        this.realPlacer = new Placer(doPlacement);
    }

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

        final int size = items.size();
        for (int i=0; i< size && works; i++) {
            works = p.placeItem(i);
        }
        return works;
    }

    private class Placer {
        private static final int MIN_CHARS = 4;
        private static final int RATIO=3;
        private int char_width, char_height, xpos, ypos;

        private final DoPlacement doPlacement;

        public Placer(DoPlacement doPlacement) {
            this.doPlacement = doPlacement;
        }

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

        protected void setGeometry(int index, int width, int height) {
            doPlacement.setGeometry(index, getXPos(),getYPos(), getXPos()+width, getYPos()+height);
        }

        protected int getXPos() {return xpos;}
        protected int getYPos() {return ypos;}

        private boolean addLine() {
            ypos+=char_height;
            xpos=0;
            return ((char_height + ypos) <= parent_height);
        }


        private int getWidthOfView(int index) {
            int len= items.get(index).getValue().length();
            len = Math.max(len, MIN_CHARS);
            return len*char_width;
        }
    }

    public interface DoPlacement {
        void setGeometry(int index, int x, int y, int x2, int y2);
    }
}
