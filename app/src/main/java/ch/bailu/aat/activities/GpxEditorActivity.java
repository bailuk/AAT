package ch.bailu.aat.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;

import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.description.NameDescription;
import ch.bailu.aat.description.PathDescription;
import ch.bailu.aat.description.TrackSizeDescription;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapFactory;
import ch.bailu.aat.services.editor.EditorInterface;
import ch.bailu.aat.util.ui.AppDialog;
import ch.bailu.aat.util.ui.AppLayout;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.views.MainControlBar;
import ch.bailu.aat.views.NodeListView;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.preferences.VerticalScrollView;

public class GpxEditorActivity extends AbsFileContentActivity
        implements OnContentUpdatedInterface {

    private static final String SOLID_KEY="gpx_editor";




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addTarget(this, InfoID.FILEVIEW);
    }



    @Override
    protected View createLayout(MainControlBar bar) {
        map = MapFactory.DEF(this, SOLID_KEY).editor(editor_helper);


        ContentDescription summaryData[] = {
                new NameDescription(this),
                new PathDescription(this),
                new DistanceDescription(this),
                new TrackSizeDescription(this),
        };

        NodeListView nodeList = new NodeListView(getServiceContext(), this);
        addTarget(nodeList, InfoID.EDITOR_OVERLAY);

        VerticalScrollView summary = new VerticalScrollView(this);
        summary.addAllContent(this, summaryData, InfoID.EDITOR_OVERLAY);

        DistanceAltitudeGraphView graph = new DistanceAltitudeGraphView(this, this, InfoID.EDITOR_OVERLAY);


        if (AppLayout.isTablet(this)) {
            return createPercentageLayout(summary, graph, nodeList);
        } else {
            return createMultiView(bar, summary, graph, nodeList);
        }

    }

    protected View createMultiView(MainControlBar bar,
                                   View summary, View graph, View nodeList) {

        MultiView mv = new MultiView(this, SOLID_KEY);

        mv.add(nodeList);
        mv.add(map.toView());

        PercentageLayout p = new PercentageLayout(this);
        p.add(summary,60);
        p.add(graph,40);
        mv.add(p);
        bar.addMvNext(mv);
        return mv;
    }


    private View createPercentageLayout(
            View summary, View graph, View nodeList) {

        if (AppLayout.getOrientation(this) == Configuration.ORIENTATION_LANDSCAPE) {
            PercentageLayout a = new PercentageLayout(this);
            a.setOrientation(AppLayout.getOrientationAlongLargeSide(this));

            a.add(map.toView(), 40);
            a.add(summary, 30);
            a.add(nodeList, 30);

            PercentageLayout b = new PercentageLayout(this);
            b.add(a, 85);
            b.add(graph, 15);

            return b;
        } else {
            PercentageLayout a = new PercentageLayout(this);
            a.setOrientation(LinearLayout.HORIZONTAL);
            a.add(map.toView(),70);
            a.add(nodeList, 30);

            PercentageLayout b = new PercentageLayout(this);
            b.add(a, 70);
            b.add(summary, 30);


            PercentageLayout c = new PercentageLayout(this);
            c.add(b, 85);
            c.add(graph,15);

            return c;
        }
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
