package ch.bailu.aat.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ch.bailu.aat.description.AscendDescription;
import ch.bailu.aat.description.ContentDescription;
import ch.bailu.aat.description.DescendDescription;
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
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.PercentageLayout;
import ch.bailu.aat.views.description.MultiView;
import ch.bailu.aat.views.graph.DistanceAltitudeGraphView;
import ch.bailu.aat.views.preferences.VerticalScrollView;

public class GpxEditorActivity extends AbsFileContentActivity implements OnContentUpdatedInterface {

    private static final String SOLID_KEY="gpx_editor";




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addTarget(this, InfoID.FILEVIEW);
    }



    @Override
    protected ViewGroup createLayout(MainControlBar bar) {
        map = MapFactory.DEF(this, SOLID_KEY).editor(editor_helper);


        ContentDescription summaryData[] = {
                new NameDescription(this),
                new PathDescription(this),
                new DistanceDescription(this),
                new AscendDescription(this),
                new DescendDescription(this),
                new TrackSizeDescription(this),
        };


        VerticalScrollView summary = new VerticalScrollView(this);
        summary.addAllContent(this, summaryData, InfoID.EDITOR_OVERLAY);

        DistanceAltitudeGraphView graph = new DistanceAltitudeGraphView(this, this, InfoID.EDITOR_OVERLAY);


        if (AppLayout.isTablet(this)) {
            return createPercentageLayout(summary, graph);
        } else {
            return createMultiView(bar, summary, graph);
        }

    }

    protected ViewGroup createMultiView(MainControlBar bar,
                                   View summary, View graph) {

        MultiView mv = new MultiView(this, SOLID_KEY);

        mv.add(map.toView());

        PercentageLayout p = new PercentageLayout(this);
        p.add(summary,60);
        p.add(graph,40);
        mv.add(p);
        bar.addMvNext(mv);
        return mv;
    }


    private ViewGroup createPercentageLayout(
            View summary, View graph) {

        if (AppLayout.getOrientation(this) == Configuration.ORIENTATION_LANDSCAPE) {
            PercentageLayout a = new PercentageLayout(this);
            a.setOrientation(AppLayout.getOrientationAlongLargeSide(this));

            a.add(map.toView(), 60);
            a.add(summary, 40);

            PercentageLayout b = new PercentageLayout(this);
            b.add(a, 85);
            b.add(graph, 15);

            return b;
        } else {
            PercentageLayout a = new PercentageLayout(this);
            a.setOrientation(LinearLayout.HORIZONTAL);
            a.add(map.toView(),100);

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
        editor_helper.edit(currentFile.getInfo().getFile());
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


                }.displaySaveDiscardDialog(this, editor_helper.getInformation().getFile().getName());
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
                        GpxEditorActivity.super.onClick(v);
                    }

                    @Override
                    public void onNeutralClick() {
                        editor.discard();
                        GpxEditorActivity.super.onClick(v);
                    }


                }.displaySaveDiscardDialog(this, editor_helper.getInformation().getFile().getName());
            } else {
                super.onClick(v);
            }

        } else {
            super.onClick(v);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
