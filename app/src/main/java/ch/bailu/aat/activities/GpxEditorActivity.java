package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;

import java.io.File;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.description.PathDescription;
import ch.bailu.aat.description.TrackSizeDescription;
import ch.bailu.aat.dispatcher.EditorSource;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.helpers.AppDialog;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.services.editor.EditorInterface;
import ch.bailu.aat.views.NodeListView;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.description.VerticalView;
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
import ch.bailu.aat.views.preferences.VerticalScrollView;

public class GpxEditorActivity extends AbsFileContentActivity
        implements OnContentUpdatedInterface {

    private static final String SOLID_KEY="gpx_editor";




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, SOLID_KEY);


        addTarget(this, InfoID.FILEVIEW);
    }

    @Override
    protected EditorHelper createEditorHelper() {
        return new EditorHelper(getServiceContext());
    }

    @Override
    protected MultiView createMultiView(final String SOLID_KEY) {
        map = new OsmInteractiveView(getServiceContext(), SOLID_KEY);

        OsmOverlay overlayList[] = {
                new GpxOverlayListOverlay(map, getServiceContext()),
                new GpxDynOverlay(map, getServiceContext(), InfoID.TRACKER),
                new GridDynOverlay(map, getServiceContext()),
                new CurrentLocationOverlay(map),
                new EditorOverlay(map,
                        getServiceContext(),
                        InfoID.EDITOR_OVERLAY,
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
                InfoID.EDITOR_OVERLAY
                );

        VerticalScrollView summary = new VerticalScrollView(this);
        DistanceAltitudeGraphView graph =new DistanceAltitudeGraphView(this, SOLID_KEY);


        MultiView mv = new MultiView(this, SOLID_KEY, InfoID.ALL);

        mv.addT(wayList);
        mv.addT(map);
        mv.addT(new VerticalView(this, SOLID_KEY, InfoID.EDITOR_OVERLAY,
                new View[] {summary, graph},
                new OnContentUpdatedInterface[]
                        {summary.addAllContent(summaryData, InfoID.EDITOR_OVERLAY), graph}
        ));
        return mv;
    }




    @Override
    public void onContentUpdated(GpxInformation info) {
        editor_helper.edit(new File(currentFile.getInfo().getPath()));
        editor_source.requestUpdate();
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
