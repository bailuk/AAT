package ch.bailu.aat.activities;

import java.io.File;

import android.os.Bundle;
import android.view.View;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.description.PathDescription;
import ch.bailu.aat.description.TrackSizeDescription;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppDialog;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.services.editor.EditorInterface;
import ch.bailu.aat.views.MultiView;
import ch.bailu.aat.views.NodeListView;
import ch.bailu.aat.views.SummaryListView;
import ch.bailu.aat.views.TrackDescriptionView;
import ch.bailu.aat.views.VerticalView;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.CurrentLocationOverlay;
import ch.bailu.aat.views.map.overlay.OsmOverlay;
import ch.bailu.aat.views.map.overlay.control.EditorOverlay;
import ch.bailu.aat.views.map.overlay.control.InformationBarOverlay;
import ch.bailu.aat.views.map.overlay.control.NavigationBarOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxDynOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxOverlayListOverlay;
import ch.bailu.aat.views.map.overlay.grid.GridDynOverlay;

public class GpxEditorActivity extends AbsFileContentActivity {

    private static final String SOLID_KEY="gpx_editor";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editor_helper = new EditorHelper(getServiceContext());

        createViews(SOLID_KEY);
        createDispatcher();
    }




    @Override
    protected MultiView createMultiView(final String SOLID_KEY) {
        map = new OsmInteractiveView(getServiceContext(), SOLID_KEY);

        OsmOverlay overlayList[] = {
                new GpxOverlayListOverlay(map, getServiceContext()),
                new GpxDynOverlay(map, getServiceContext(), GpxInformation.ID.INFO_ID_TRACKER), 
                new GridDynOverlay(map, getServiceContext()),
                new CurrentLocationOverlay(map),
                new EditorOverlay(map,
                        getServiceContext(),
                        GpxInformation.ID.INFO_ID_EDITOR_OVERLAY,
                        editor_helper),
                new NavigationBarOverlay(map),
                new InformationBarOverlay(map)
        };

        map.setOverlayList(overlayList);


        ContentDescription summaryData[] = {
                new NameDescription(this),
                new PathDescription(this),
                new DistanceDescription(this),
                new TrackSizeDescription(this),
        };


        NodeListView wayList = new NodeListView(getServiceContext(),
                SOLID_KEY,
                GpxInformation.ID.INFO_ID_EDITOR_OVERLAY
                );

        TrackDescriptionView viewData[] = {

                wayList,
                map,

                new VerticalView(this, SOLID_KEY, GpxInformation.ID.INFO_ID_EDITOR_OVERLAY, new TrackDescriptionView[] {
                        new SummaryListView(this, SOLID_KEY, GpxInformation.ID.INFO_ID_EDITOR_OVERLAY, summaryData),
                        new DistanceAltitudeGraphView(this, SOLID_KEY),
                        })
        };   

        return new MultiView(this, SOLID_KEY, GpxInformation.ID.INFO_ID_ALL, viewData);
    }


 

    @Override
    public void updateGpxContent(GpxInformation info) {
        if (info.getID()== GpxInformation.ID.INFO_ID_FILEVIEW) {
            editor_helper.edit(new File(currentFile.getInfo().getPath()));
            editor_source.forceUpdate();
        }
    }


    @Override
    public void onBackPressed() {
        try {
            if (editor_helper.getEditor().isModified()) {
                new AppDialog() {
                    @Override
                    protected void onPositiveClick() {

                        editor_helper.getEditor().save();
                        closeActivity();
                    }

                    @Override
                    public void onNeutralClick() {
                        editor_helper.getEditor().discard();
                        closeActivity();
                    }


                }.displaySaveDiscardDialog(this, editor_helper.getInformation().getName());
            } else {
                closeActivity();
            }

        } catch (Exception e) {
            AppLog.e(GpxEditorActivity.this, e);
            closeActivity();
        }

    }


    private void closeActivity() {
        super.onBackPressed();
    }


    @Override
    public void onClick(final View v) {
        final EditorInterface editor = editor_helper.getEditor();

        if (v == previousFile || v ==nextFile) {
            if (editor.isModified()) {
                new AppDialog() {
                    @Override
                    protected void onPositiveClick() {
                        editor.save();
                        switchFile(v);
                    }

                    @Override
                    public void onNeutralClick() {
                        editor.discard();
                        switchFile(v);
                    }


                }.displaySaveDiscardDialog(this, editor_helper.getInformation().getName());
            } else {
                switchFile(v);
            }

        } else {
            super.onClick(v);
        }
    }
}
