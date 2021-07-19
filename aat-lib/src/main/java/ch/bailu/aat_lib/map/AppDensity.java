package ch.bailu.aat_lib.map;

public class AppDensity {

        public float toPixel_f(float diPixel) {
            return diPixel;
        }
        public float toPixelScaled_f(float diPixel) {
            return diPixel;
        }


        public int toPixel_i(float diPixel) {
            return (int) (toPixel_f(diPixel)+0.5f);
        }
        public int toPixel_i(float diPixel, int min) {
            return Math.max(min, toPixel_i(diPixel));
        }

        public int toDensityIndependentPixel(float pixel) {
            return (int) (pixel);
        }
}
