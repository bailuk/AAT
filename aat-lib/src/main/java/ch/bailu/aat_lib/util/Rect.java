package ch.bailu.aat_lib.util;

/**
 * Simple rectangle util class.
 * To keep some code independent from UI libraries.
 */
public class Rect {

    public int left, right, top, bottom;


    /**
     * Set new left and top coordinates while keeping width and height
     * @param x new left
     * @param y new top
     */
    public void offsetTo(int x, int y) {
        final int width = right - left;
        final int height = bottom - top;

        left = x;
        right = x + width;
        top = y;
        bottom = y + height;
    }

    /**
     * Add offset to left and top coordinates while keeping width and height
     * @param x add to left
     * @param y add to top
     */
    public void offset(int x, int y) {
        left+=x;
        right+=x;
        top+=y;
        bottom+=y;
    }

    public int width() {
        return right - left + 1;
    }

    public int height() {
        return bottom - top + 1;
    }

    @Override
    public String toString() {
        return "Rect{" +
                "left=" + left +
                ", right=" + right +
                ", top=" + top +
                ", bottom=" + bottom +
                ", width=" + width() +
                ", height=" + height() +
                '}';
    }
}
