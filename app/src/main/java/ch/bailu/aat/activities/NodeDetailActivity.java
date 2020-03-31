package ch.bailu.aat.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.CustomFileSource;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListArray;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.util.HtmlBuilderGpx;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ImageButtonViewGroup;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.SVGAssetView;
import ch.bailu.aat.views.bar.ControlBar;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.html.HtmlScrollTextView;

public class NodeDetailActivity extends ActivityContext
        implements OnClickListener, OnContentUpdatedInterface, SeekBar.OnSeekBarChangeListener {


    private static final String SOLID_KEY=NodeDetailActivity.class.getSimpleName();

    private ImageButtonViewGroup nextNode, previousNode;
    private SVGAssetView icon;

    private MapViewInterface mapView;
    private HtmlScrollTextView htmlView;
    private DistanceAltitudeGraphView graph;
    private SeekBar seekBar;

    private String fileID="";

    private GpxListArray arrayCache = new GpxListArray(GpxList.NULL_ROUTE);
    private GpxInformation infoCache = GpxInformation.NULL;


    private HtmlBuilderGpx htmlBuilder;

    private final UiTheme theme = AppTheme.trackContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        htmlBuilder = new HtmlBuilderGpx(this);
        fileID = getIntent().getStringExtra("ID");

        final ContentView contentView = new ContentView(this, theme);
        contentView.add(createButtonBar());
        contentView.add(createSeekBar());
        contentView.add(createVerticalView());

        createDispatcher();

        setContentView(contentView);
    }



    private ControlBar createButtonBar() {
        ControlBar bar = new MainControlBar(this);

        previousNode =  bar.addImageButton(R.drawable.go_up_inverse);
        nextNode = bar.addImageButton(R.drawable.go_down_inverse);

        icon = new SVGAssetView(getServiceContext(), 0);
        bar.add(icon);

        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setOnClickListener1(this);
        return bar;
    }


    private View createVerticalView() {

        PercentageLayout viewA =
                new PercentageLayout(this);

        PercentageLayout viewB =
                new PercentageLayout(this);


        viewB.setOrientation(AppLayout.getOrientationAlongLargeSide(this));

        mapView = MapFactory.DEF(this, SOLID_KEY).node();

        htmlView =new HtmlScrollTextView(this);
        htmlView.enableAutoLink();
        htmlView.themify(theme);

        viewB.add(htmlView, 40);
        viewB.add(mapView.toView(), 60);

        graph = new DistanceAltitudeGraphView(this, theme);
        viewA.add(graph, 20);
        viewA.add(viewB, 80);
        return viewA;
    }


    private SeekBar createSeekBar() {
        seekBar = new SeekBar(this);
        seekBar.setOnSeekBarChangeListener(this);
        return seekBar;
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

        graph.setVisibility(info);
        initSeekBar(info);

        int index = getIntent().getIntExtra("I", 0);
        updateToIndex(index);
    }

    private void initSeekBar(GpxInformation info) {
        int max = 0;

        if (arrayCache.size() > 0)
            max = arrayCache.size() -1;

        seekBar.setMax(max);
    }


    private void updateToIndex(int i) {
        if (arrayCache.size()>0) {

            if (i < 0) i = arrayCache.size()-1;
            if (i >= arrayCache.size()) i=0;



            mapView.setCenter(arrayCache.get(i).getBoundingBox().getCenter().toLatLong());

            htmlBuilder.clear();
            htmlBuilder.appendInfo(infoCache, i);
            htmlBuilder.appendNode(arrayCache.get(i), infoCache);
            htmlBuilder.appendAttributes(arrayCache.get(i).getAttributes());
            htmlView.setHtmlText(htmlBuilder.toString());

            graph.onContentUpdated(InfoID.ALL, infoCache, i);

            seekBar.setProgress(i);

            icon.setImageObject(arrayCache.get(i));
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
        if (fromUser) updateToIndex(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}
