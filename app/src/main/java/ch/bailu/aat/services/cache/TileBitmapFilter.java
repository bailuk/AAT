//package ch.bailu.aat.services.cache;
//
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.ColorMatrix;
//import android.graphics.ColorMatrixColorFilter;
//import android.graphics.Paint;
//import android.graphics.Rect;
//
//public abstract class TileBitmapFilter {
//    public abstract void applayFilter(Canvas dst, Bitmap src, int alpha);
//
//    public void applayFilter(Canvas dst, Bitmap src, Paint paint) {
//        Rect s = new Rect(0,0, src.getWidth(), src.getHeight());
//        Rect d = new Rect(0,0, dst.getWidth(), dst.getHeight());
//        dst.drawBitmap(src, s, d, paint);
//    }
//
//    @Override
//    public String toString() {
//        return getClass().getSimpleName();
//    }
//
//    public static class GrayscaleFilter extends OverlayFilter {
//        public GrayscaleFilter() {
//            super(createGrayScalePaint());
//        }
//
//        private static Paint createGrayScalePaint() {
//            Paint paint = new Paint();
//            ColorMatrix cm = new ColorMatrix();
//            cm.setSaturation(0);
//
//            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
//            paint.setColorFilter(f);
//            return paint;
//        }
//    }
//
//
//    public static final TileBitmapFilter GRAYSCALE_FILTER=new GrayscaleFilter();
//
//
//    public static class InverseFilter extends OverlayFilter {
//        public InverseFilter() {
//            super(createInversePaint());
//        }
//
//
//        private static Paint createInversePaint() {
//            Paint paint = new Paint();
//            ColorMatrix cm = new ColorMatrix(new float[]
//                    {
//                    -1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
//                    0.0f, -1.0f, 0.0f, 1.0f, 1.0f,
//                    0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
//                    0.0f, 0.0f, 0.0f, 1.0f, 0.0f
//                    });
//
//
//            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
//            paint.setColorFilter(f);
//            return paint;
//        }
//    }
//    public static final TileBitmapFilter INVERSE_FILTER=new InverseFilter();
//
//
//    public static class AlphaFilter extends CopyFilter {
//        public AlphaFilter() {
//            super(createAlphaPaint());
//        }
//
//
//        private static Paint createAlphaPaint() {
//            Paint paint = new Paint();
//            ColorMatrix cm = new ColorMatrix(new float[]
//                    {
//                    1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
//                    0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
//                    0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
//                    -0.1f, -0.1f, -0.1f, 0.0f, 255.0f
//                    });
//
//
//            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
//            paint.setColorFilter(f);
//            return paint;
//        }
//    }
//    public static final TileBitmapFilter ALPHA_FILTER=new AlphaFilter();
//
//
//
//
//    public static class OverlayFilter extends TileBitmapFilter {
//
//        private final Paint paint;
//
//
//        public OverlayFilter(Paint p) {
//            paint = p;
//        }
//
//        public OverlayFilter() {
//            paint =  new Paint();
//        }
//
//        public void applayFilter(Canvas canvas, Bitmap overlayBitmap, int alpha) {
//            paint.setAlpha(alpha);
//            super.applayFilter(canvas, overlayBitmap, paint);
//
//        }
//    }
//    public static final OverlayFilter OVERLAY_FILTER=new OverlayFilter();
//
//
//
//    public static class CopyFilter extends TileBitmapFilter {
//
//        private final Paint paint;
//
//
//        public CopyFilter(Paint p) {
//            paint = p;
//        }
//
//        public CopyFilter() {
//            paint =  new Paint();
//            //paint.setAlpha(180);
//        }
//
//        public void applayFilter(Canvas canvas, Bitmap overlayBitmap, int alpha) {
//            /*Bitmap inter = Bitmap.createBitmap(overlayBitmap.getWidth(), overlayBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//            inter.eraseColor(Color.TRANSPARENT);
//            Canvas interc = new Canvas(inter);
//            interc.drawBitmap(overlayBitmap, 0, 0, paint);*/
//
//            super.applayFilter(canvas, overlayBitmap, paint);
//
//        }
//
//
//    }
//    public static final CopyFilter COPY_FILTER=new CopyFilter();
//
//}
