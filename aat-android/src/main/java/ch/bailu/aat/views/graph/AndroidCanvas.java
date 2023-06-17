package ch.bailu.aat.views.graph;

import android.graphics.Canvas;
import android.graphics.Paint;

import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.map.BitmapDraw;
import ch.bailu.aat.util.ui.theme.UiTheme;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.map.AppDensity;
import ch.bailu.aat_lib.map.NodeBitmap;
import ch.bailu.aat_lib.util.Point;
import ch.bailu.aat_lib.view.graph.GraphCanvas;

public class AndroidCanvas implements GraphCanvas {
    private static final int TEXT_SIZE=15;

    private final Paint paintFont;
    private final Paint paintPlotLines;
    private final Paint paintLines;

    private final Canvas canvas;
    private final BitmapDraw bitmapCanvas = new BitmapDraw();

    private final NodeBitmap nodeBitmap;


    public AndroidCanvas(Canvas c, AppContext context, AppDensity res, UiTheme theme) {
        canvas = c;

        paintFont = new Paint();
        paintFont.setAntiAlias(true);
        paintFont.setDither(false);
        paintFont.setColor(theme.getGraphTextColor());
        paintFont.setTextSize(res.toPixelScaled_f(TEXT_SIZE));


        paintPlotLines = new Paint();
        paintPlotLines.setAntiAlias(true);
        paintPlotLines.setDither(false);
        paintPlotLines.setStrokeWidth(res.toPixel_f(2));

        paintLines = new Paint();
        paintLines.setAntiAlias(true);
        paintLines.setDither(false);
        paintLines.setStrokeWidth(0);
        paintLines.setColor(theme.getGraphLineColor());
        nodeBitmap = NodeBitmap.get(res, context);
    }


    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        canvas.drawLine(x1, y1 , x2, y2,  paintLines);
    }

    @Override
    public void drawLine(Point pA, Point pB, int color) {
        paintPlotLines.setColor(color);
        canvas.drawLine(pA.x, pA.y, pB.x, pB.y, paintPlotLines);
    }

    @Override
    public void drawText(String text, int x, int y) {
        canvas.drawText(text, x, y, paintFont);
    }

    @Override
    public void drawBitmap(Point point, int color) {
        var mfBitmap = nodeBitmap.getTileBitmap().getBitmap();
        if (mfBitmap != null) {
            var androidBitmap = AndroidGraphicFactory.getBitmap(mfBitmap);
            if (androidBitmap != null) {
                bitmapCanvas.draw(canvas, androidBitmap, point, color);
            }
        }
    }

    @Override
    public int getTextSize() {
        return Math.max(Math.round(paintFont.getTextSize()), 1);
    }
}
