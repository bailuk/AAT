package ch.bailu.aat.activities;


import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ch.bailu.aat.R;
import ch.bailu.aat.description.AccelerationDescription;
import ch.bailu.aat.description.AccuracyDescription;
import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.description.AverageSpeedDescription;
import ch.bailu.aat.description.BearingDescription;
import ch.bailu.aat.description.CH1903EastingDescription;
import ch.bailu.aat.description.CH1903NorthingDescription;
import ch.bailu.aat.description.CaloriesDescription;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.CurrentSpeedDescription;
import ch.bailu.aat.description.DateDescription;
import ch.bailu.aat.description.DescriptionInterface;
import ch.bailu.aat.description.EndDateDescription;
import ch.bailu.aat.description.GpsStateDescription;
import ch.bailu.aat.description.LatitudeDescription;
import ch.bailu.aat.description.LongitudeDescription;
import ch.bailu.aat.description.MaximumSpeedDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.description.PathDescription;
import ch.bailu.aat.description.PauseDescription;
import ch.bailu.aat.description.TimeDescription;
import ch.bailu.aat.description.TrackSizeDescription;
import ch.bailu.aat.description.TrackerStateDescription;
import ch.bailu.aat.dispatcher.ContentDispatcher;
import ch.bailu.aat.dispatcher.ContentSource;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.EditorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.services.ServiceLink;
import ch.bailu.aat.test.TestCoordinates;
import ch.bailu.aat.test.TestGpx;
import ch.bailu.aat.test.TestGpxLogRecovery;
import ch.bailu.aat.test.TestTest;
import ch.bailu.aat.test.UnitTest;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.MultiView;
import ch.bailu.aat.views.NodeListView;
import ch.bailu.aat.views.SummaryListView;
import ch.bailu.aat.views.TrackDescriptionView;
import ch.bailu.aat.views.ViewWrapper;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.CurrentLocationOverlay;
import ch.bailu.aat.views.map.overlay.Dem3NameOverlay;
import ch.bailu.aat.views.map.overlay.OsmOverlay;
import ch.bailu.aat.views.map.overlay.RefreshLogOverlay;
import ch.bailu.aat.views.map.overlay.ZoomLevelOverlay;
import ch.bailu.aat.views.map.overlay.control.InformationBarOverlay;
import ch.bailu.aat.views.map.overlay.control.NavigationBarOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxDynOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxOverlayListOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxTestOverlay;
import ch.bailu.aat.views.map.overlay.grid.GridDynOverlay;

public class TestActivity extends AbsDispatcher implements OnClickListener {
    private static final String SOLID_KEY="test";


    private LinearLayout         contentView;
    private ImageButton          multiCycleP, multiCycleN;
    private MultiView            multiView;

    private OsmInteractiveView   map;

    private NodeListView          wayList;
    private SummaryListView gpsSummary, trackSummary;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentView = new ContentView(this);

        contentView.addView(createButtonBar());
        multiView = createMultiView();
        contentView.addView(multiView);


        setContentView(contentView);
        
        createDispatcher();
        
        setServiceDependencies(ServiceLink.ALL_SERVICES);
        
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }




    private MultiView createMultiView() {
        wayList = new NodeListView(getServiceContext(), SOLID_KEY, INFO_ID_OVERLAY);

        map=new OsmInteractiveView(getServiceContext(), SOLID_KEY);
        ViewWrapper list = new ViewWrapper(new TestList(this));

        ContentDescription gpsData[] = new ContentDescription[] {
                new NameDescription(this),
                new GpsStateDescription(this),
                new AltitudeDescription(this),
                new LongitudeDescription(this),
                new LatitudeDescription(this),
                new CH1903EastingDescription(this),
                new CH1903NorthingDescription(this),
                new AccuracyDescription(this),
                new CurrentSpeedDescription(this),
                new AccelerationDescription(this),
                new BearingDescription(this),
        };   

        ContentDescription trackData[] = new ContentDescription[] {
                new NameDescription(this),
                new PathDescription(this),
                new TrackerStateDescription(this),
                new AverageSpeedDescription(this),
                new MaximumSpeedDescription(this),
                new CaloriesDescription(this),
                new DateDescription(this),
                new EndDateDescription(this),
                new TimeDescription(this),
                new PauseDescription(this),
                new TrackSizeDescription(this),
        };   

        gpsSummary= new SummaryListView(this, SOLID_KEY, INFO_ID_LOCATION, gpsData);
        trackSummary = new SummaryListView(this, SOLID_KEY, INFO_ID_TRACKER, trackData);

        TrackDescriptionView viewData[] = {
                map,
                gpsSummary,
                trackSummary,
                list,
                wayList,
        };   


        return new MultiView(this, SOLID_KEY, INFO_ID_ALL, viewData);
    }






    private ControlBar createButtonBar() {
        ControlBar bar = new ControlBar(
                this, 
                AppLayout.getOrientationAlongSmallSide(this));

        multiCycleP = bar.addImageButton(R.drawable.go_previous_inverse);
        multiCycleN = bar.addImageButton(R.drawable.go_next_inverse);


        bar.setOnClickListener1(this);

        return bar;
    }

    @Override
    public void onClick(View v) {
        if (v ==multiCycleN) {
            multiView.setNext();
        } else if (v == multiCycleP) {
            multiView.setPrevious();
        }
    }



    
    private void createDispatcher() {
        OsmOverlay overlayList[] = {
                new GpxOverlayListOverlay(map,getServiceContext()),
                new GpxDynOverlay(map, getServiceContext(), GpxInformation.ID.INFO_ID_TRACKER), 
                new GpxTestOverlay(map, GpxInformation.ID.INFO_ID_OVERLAY),
                new GridDynOverlay(map, getServiceContext()),
                new CurrentLocationOverlay(map),
                new NavigationBarOverlay(map),
                new InformationBarOverlay(map),
                new ZoomLevelOverlay(map),
                new RefreshLogOverlay(map),
                new Dem3NameOverlay(map)
        };
        map.setOverlayList(overlayList);


        DescriptionInterface[] target = new DescriptionInterface[] {
                multiView,this
        };

        ContentSource[] source = new ContentSource[] {
                new EditorSource(getServiceContext(),GpxInformation.ID.INFO_ID_EDITOR_DRAFT),
                new TrackerSource(getServiceContext()),
                new CurrentLocationSource(getServiceContext()),
                new OverlaySource(getServiceContext()),
        };
        setDispatcher(new ContentDispatcher(this,source, target));
    }





    private class TestList extends ListView 
    implements ListAdapter, android.widget.AdapterView.OnItemClickListener {
        private final ArrayList<UnitTest>  tests = new ArrayList<UnitTest>();

        public TestList(Context context) {
            super(context);
            AppTheme.themify(this, AppTheme.getHighlightColor());
            tests.add(new TestCoordinates(context));
            tests.add(new TestGpx(context));
            tests.add(new TestGpxLogRecovery(context));
            tests.add(new TestTest(context));


            setAdapter(this);
            setOnItemClickListener(this);
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getCount() {
            return tests.size();
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View recycle, ViewGroup parent) {
            TextView view = (TextView) recycle;

            if (view == null) {
                view = new TextView(getContext());
                view.setTextSize(25);
            }


            view.setText(tests.get(position).getClass().getSimpleName());

            return view;
        }


        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {}

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {}

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int arg0) {
            return true;
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int i,
                long arg3) {
            if (i < tests.size()) {
                try {
                    tests.get(i).test();
                    AppLog.i(getContext(), "Test Sucessfull");

                } catch (AssertionError e) {
                    AppLog.e(getContext(), e);
                } catch (Exception e) {
                    AppLog.e(getContext(), e);
                }
            }

        }

    }
}
