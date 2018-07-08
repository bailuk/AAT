package ch.bailu.aat.services.cache.osm_features;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;

import ch.bailu.aat.services.icons.IconMapService;
import ch.bailu.aat.util.graphic.SyncTileBitmap;
import ch.bailu.aat.util.ui.AppDensity;
import ch.bailu.aat.util.ui.AppLog;


class Icon {
    public final Drawable way, node, area, relation;
    private final int icon_size;


    public Icon(Context context) {
        int sicon_size = new AppDensity(context).toPixel_i(IconMapService.SMALL_ICON_SIZE);
        icon_size =new AppDensity(context).toPixel_i(IconMapService.BIG_ICON_SIZE);

        way = toDrawable(context,"way.svg",sicon_size,Color.WHITE);
        node = toDrawable(context,"symbols/node.svg",sicon_size,Color.WHITE);
        area = toDrawable(context,"symbols/area.svg",sicon_size,Color.WHITE);
        relation = toDrawable(context,"symbols/relation.svg",sicon_size,Color.WHITE);
    }

    public Drawable toDrawable(Context context, String asset) {
        return Icon.toDrawable(context, asset, icon_size, Color.TRANSPARENT);
    }


    public static Drawable toDrawable(Context context, String asset, int size, int color) {
        AssetManager assets = context.getAssets();
        Resources res = context.getResources();

        try {
            SVG svg = SVG.getFromAsset(assets, asset);
            return SyncTileBitmap.toDrawable(svg, size, res, color);

        } catch (SVGParseException | IOException e) {
            AppLog.d(asset,asset);
        }
        return null;
    }
}
