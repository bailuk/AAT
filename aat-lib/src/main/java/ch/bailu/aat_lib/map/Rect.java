package ch.bailu.aat_lib.map;

public class Rect {
    public int left, right, top, bottom;

    public void offsetTo(int x, int y) {
        left-=x;
        right-=x;
        top-=y;
        bottom-=y;
    }

    public void offset(int x, int y) {
        left+=x;
        right+=x;
        top+=y;
        bottom+=y;
    }
}
