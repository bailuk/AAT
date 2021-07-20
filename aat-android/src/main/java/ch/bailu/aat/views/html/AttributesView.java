package ch.bailu.aat.views.html;

import android.content.Context;
import android.graphics.Color;

import ch.bailu.aat_lib.map.MapColor;
import ch.bailu.aat.util.HtmlBuilderGpx;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;

public class AttributesView extends HtmlTextView implements OnContentUpdatedInterface {
    private final HtmlBuilderGpx htmlBuilder;


    public AttributesView(Context context) {
        super(context);
        htmlBuilder = new HtmlBuilderGpx(context);

        enableAutoLink();
        setPadding(10,10,10,10);
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {

        setBackgroundColor(MapColor.toLightTransparent(MapColor.getColorFromIID(iid)));
        setTextColor(Color.BLACK);

        //htmlBuilder.clear();
        htmlBuilder.appendAttributes(info.getAttributes());
        setHtmlText(htmlBuilder.toString());
        htmlBuilder.clear();
    }
}
