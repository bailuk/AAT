package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;

import java.io.File;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.description.PathDescription;
import ch.bailu.aat.description.TrackSizeDescription;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.helpers.AppDialog;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.services.editor.EditorInterface;
import ch.bailu.aat.views.NodeListView;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.description.VSplitView;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.map.MapFactory;
import ch.bailu.aat.views.preferences.VerticalScrollView;

public class GpxEditorActivity extends AbsFileContentActivity
        implements OnContentUpdatedInterface {

    private static final String SOLID_KEY="gpx_editor";




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(SOLID_KEY);

        addTarget(this, InfoID.FILEVIEW);
    }

    @Override
    protected EditorHelper createEditorHelper() {
        return new EditorHelper(getServiceContext());
    }

    @Override
    protected MultiView createMultiView(final String SOLID_KEY) {
        map = new MapFactory(this, SOLID_KEY).editor(editor_helper);

        ContentDescription summaryData[] = {
                new NameDescription(this),
                new PathDescription(this),
                new DistanceDescription(this),
                new TrackSizeDescription(this),
        };


        NodeListView nodeList = new NodeListView(getServiceContext());
        addTarget(nodeList, InfoID.EDITOR_OVERLAY);

        VerticalScrollView summary = new VerticalScrollView(this);
        summary.addAllContent(this, summaryData, InfoID.EDITOR_OVERLAY);

        DistanceAltitudeGraphView graph = new DistanceAltitudeGraphView(this, this, InfoID.EDITOR_OVERLAY);

        MultiView multiView = new MultiView(this, SOLID_KEY);

        multiView.add(nodeList);
        multiView.add(map);
        multiView.add(new VSplitView(this, new View[] {summary, graph}));

        return multiView;
    }




    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
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
