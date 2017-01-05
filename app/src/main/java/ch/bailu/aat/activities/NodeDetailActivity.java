package ch.bailu.aat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.CustomFileSource;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListArray;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MFactory;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.util.HtmlBuilderGpx;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.HtmlScrollTextView;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.description.VSplitView;

public class NodeDetailActivity extends AbsDispatcher
        implements OnClickListener, OnContentUpdatedInterface {


    private static final String SOLID_KEY=NodeDetailActivity.class.getSimpleName();

    private ImageButton nextNode, previousNode;


    private MapViewInterface mapView;
    private HtmlScrollTextView htmlView;

    private String fileID="";


    private GpxListArray arrayCache = new GpxListArray(GpxList.NULL_ROUTE);
    private GpxInformation infoCache = GpxInformation.NULL;


    private HtmlBuilderGpx htmlBuilder;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        htmlBuilder = new HtmlBuilderGpx(this);
        fileID = getIntent().getStringExtra("ID");

        final LinearLayout contentView = new ContentView(this);
        contentView.addView(createButtonBar());
        contentView.addView(createVerticalView());

        createDispatcher();

        setContentView(contentView);
    }



    private ControlBar createButtonBar() {
        ControlBar bar = new MainControlBar(getServiceContext());

        previousNode =  bar.addImageButton(R.drawable.go_up_inverse);
        nextNode = bar.addImageButton(R.drawable.go_down_inverse);

        bar.setOrientation(AppLayout.getOrientationAlongSmallSide(this));
        bar.setOnClickListener1(this);
        return bar;
    }


    private VSplitView createVerticalView() {
        mapView = MFactory.DEF(this, SOLID_KEY).node();

        htmlView =new HtmlScrollTextView(this);
        htmlView.enableAutoLink();

        return new VSplitView(this, new View[] {htmlView, mapView.toView()});
    }


    private void createDispatcher() {
        addTarget(this, InfoID.FILEVIEW);
        addSource(new CurrentLocationSource(getServiceContext()));
        addSource(new CustomFileSource(getServiceContext(), fileID));


    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {

        arrayCache = new GpxListArray(info.getGpxList());
        infoCache = info;

        int index = getIntent().getIntExtra("I", 0);
        updateToIndex(index);
    }


    private void updateToIndex(int i) {
        if (arrayCache.size()>0) {

            if (i < 0) i = arrayCache.size()-1;
            if (i >= arrayCache.size()) i=0;

            mapView.frameBounding(arrayCache.get(i).getBoundingBox());

            htmlBuilder.clear();
            htmlBuilder.appendInfo(infoCache, i);
            htmlBuilder.appendNode(arrayCache.get(i), infoCache);
            htmlBuilder.appendAttributes(arrayCache.get(i).getAttributes());
            htmlView.setHtmlText(htmlBuilder.toString());
        }
    }

    @Override
    public void onClick(View v) {
        if (arrayCache.size()>0) {
            int i = arrayCache.getIndex();

            if (v == previousNode) {
                i--;

            } else if (v ==nextNode) {
                i++;
            }

            updateToIndex(i);
        }
    }


    public static void start(Context context, String fileId, int index) {
        final Intent intent = new Intent();
        intent.putExtra("I", index);
        intent.putExtra("ID", fileId);
        ActivitySwitcher.start(context, NodeDetailActivity.class, intent);
    }

}
