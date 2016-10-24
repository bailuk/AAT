package ch.bailu.aat.views.map.overlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

import org.osmdroid.views.MapView;

import ch.bailu.aat.helpers.AppDensity;

public class MapPainter {

    public final MapProjection  projection;
    public final MapTwoNodes    nodes;
    public final MapCanvas      canvas;
    public final BitmapDrawable nodeBitmap;

    
    public MapPainter(Context context, AppDensity res) {
        projection=new MapProjection();
        nodes=new MapTwoNodes(this);
        canvas=new MapCanvas(context, projection, res);
        nodeBitmap = NodePainter.createNode(res, context.getResources());
    }

    public void init(Canvas c, MapView map) {
        projection.init(map);
        canvas.init(c);
    }
    
    
    
    
}
