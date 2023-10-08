package ch.bailu.aat_lib.map;


import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;

import java.util.HashMap;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.app.AppGraphicFactory;
import ch.bailu.aat_lib.map.tile.MapTileInterface;
import ch.bailu.aat_lib.lib.color.ARGB;

public final class NodeBitmap {

    private static final int STROKE_WIDTH= MapPaint.EDGE_WIDTH_LINE;
    private static final int RADIUS=6;

    private static final HashMap<Integer, NodeBitmap> nodes = new HashMap<>(10);

    public static NodeBitmap get(AppDensity res, AppContext context) {
        int radius = res.toPixelInt(RADIUS);

        NodeBitmap node = nodes.get(radius);

        if (node == null) {
            node = new NodeBitmap(radius, res, context);
            nodes.put(radius, node);
        }

        return node;
    }

    private final MapTileInterface bitmap;

    private NodeBitmap(int radius, AppDensity res, AppContext context) {

        bitmap = context.createMapTile();

        int stroke_width = res.toPixelInt(STROKE_WIDTH);
        int hsize = (radius+ stroke_width);
        int size = hsize * 2;



        bitmap.set(size, true);

        Canvas canvas = bitmap.getCanvas();
        Paint stroke = MapPaint.createEdgePaintLine(res);

        Paint fill = AppGraphicFactory.instance().createPaint();
        fill.setStyle(Style.FILL);

        fill.setColor(new ARGB(150, ARGB.WHITE).toInt());

        canvas.drawCircle(hsize,hsize, radius, fill);
        canvas.drawCircle(hsize, hsize, radius, stroke);
    }

    public MapTileInterface getTileBitmap() {
        return bitmap;
    }
}
