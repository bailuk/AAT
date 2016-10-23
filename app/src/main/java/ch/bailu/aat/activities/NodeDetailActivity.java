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
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxListArray;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.HtmlScrollTextView;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.description.VerticalView;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.CurrentLocationOverlay;
import ch.bailu.aat.views.map.overlay.OsmOverlay;
import ch.bailu.aat.views.map.overlay.control.InformationBarOverlay;
import ch.bailu.aat.views.map.overlay.control.NavigationBarOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxDynOverlay;
import ch.bailu.aat.views.map.overlay.grid.GridDynOverlay;

public class NodeDetailActivity extends AbsDispatcher
        implements OnClickListener, OnContentUpdatedInterface {


    private static final String SOLID_KEY=NodeDetailActivity.class.getSimpleName();

    private ImageButton nextNode, previousNode;


    private VerticalView       verticalView;
    private OsmInteractiveView map;
    private HtmlScrollTextView           text;

    private String fileID="";


    private GpxListArray       array = new GpxListArray(GpxList.NULL_ROUTE);



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        fileID = getIntent().getStringExtra("ID");

        final LinearLayout contentView = new ContentView(this);
        contentView.addView(createButtonBar());
        verticalView = createVerticalView();
        contentView.addView(verticalView);

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


    private VerticalView createVerticalView() {
        map = new OsmInteractiveView(getServiceContext(), SOLID_KEY);


        text=new HtmlScrollTextView(this);


        View views[] = {
                text,
                map,
        };

        OnContentUpdatedInterface targets[]  = {
                OnContentUpdatedInterface.NULL,
                map
        };

        return new VerticalView(this, SOLID_KEY, InfoID.ALL, views, targets);
    }


    private void createDispatcher() {
        OsmOverlay overlayList[] = {
                new GpxDynOverlay(map, getServiceContext(), InfoID.TRACKER),
                new GpxDynOverlay(map, getServiceContext(), InfoID.FILEVIEW),
                new CurrentLocationOverlay(map),
                new GridDynOverlay(map, getServiceContext()),
                new NavigationBarOverlay(map),
                new InformationBarOverlay(map),

        };
        map.setOverlayList(overlayList);

        addTarget(verticalView);
        addTarget(this, InfoID.FILEVIEW);
        addTarget(map);
        addSource(new CurrentLocationSource(getServiceContext()));
        addSource(new CustomFileSource(getServiceContext(), fileID));
    }


    @Override
    public void onContentUpdated(GpxInformation info) {

        array = new GpxListArray(info.getGpxList());

        int index = getIntent().getIntExtra("I", 0);
        updateToIndex(index);
    }


    private void updateToIndex(int i) {
        if (array.size()>0) {
            final StringBuilder builder=new StringBuilder();

            if (i<0) i = array.size()-1;
            if (i>=array.size()) i=0;

            map.frameBoundingBox(array.get(i).getBoundingBox());

            array.get(i).toHtml(this, builder);
            text.setHtmlText(builder.toString());
        }
    }

    @Override
    public void onClick(View v) {
        if (array.size()>0) {
            int i = array.getIndex();

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
