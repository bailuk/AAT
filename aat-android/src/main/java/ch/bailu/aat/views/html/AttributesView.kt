package ch.bailu.aat.views.html;

import android.content.Context;
import android.graphics.Color;

import ch.bailu.aat_lib.map.MapColor;
import ch.bailu.aat_lib.html.MarkupBuilderGpx;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class AttributesView extends HtmlTextView implements OnContentUpdatedInterface {
    private final MarkupBuilderGpx markupBuilder;


    public AttributesView(Context context, StorageInterface storage) {
        super(context);
        markupBuilder = new MarkupBuilderGpx(storage);

        enableAutoLink();
        setPadding(10,10,10,10);
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {

        setBackgroundColor(MapColor.toLightTransparent(MapColor.getColorFromIID(iid)));
        setTextColor(Color.BLACK);

        markupBuilder.appendAttributes(info.getAttributes());
        setHtmlText(markupBuilder.toString());
        markupBuilder.clear();
    }
}
