package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ch.bailu.aat.R;
import ch.bailu.aat_lib.dispatcher.EditorOrBackupSource;
import ch.bailu.aat.menus.FileMenu;
import ch.bailu.aat.util.ui.AppDialog;
import ch.bailu.aat.util.ui.AppTheme;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.BusyViewContainer;
import ch.bailu.aat.views.BusyViewControlIID;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.ImageButtonViewGroup;
import ch.bailu.aat.views.PreviewView;
import ch.bailu.aat.views.bar.MainControlBar;
import ch.bailu.aat.views.html.AttributesView;
import ch.bailu.aat.views.msg.ErrorMsgView;
import ch.bailu.aat_lib.dispatcher.CurrentLocationSource;
import ch.bailu.aat_lib.dispatcher.IteratorSource;
import ch.bailu.aat_lib.dispatcher.OverlaysSource;
import ch.bailu.aat_lib.dispatcher.TrackerSource;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.map.MapViewInterface;
import ch.bailu.aat_lib.util.Objects;

public abstract class AbsFileContentActivity extends ActivityContext implements OnClickListener {

    protected final static UiTheme THEME = AppTheme.trackContent;

    private IteratorSource  currentFile;
    protected ImageButtonViewGroup nextFile, previousFile;
    protected PreviewView fileOperation;

    private ErrorMsgView fileError;

    private BusyViewControlIID busyControl;
    protected MapViewInterface map;

    protected EditorOrBackupSource editorSource = null;

    private String currentFileID;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentFile = new IteratorSource.FollowFile(getAppContext());
        editorSource = new EditorOrBackupSource(getAppContext(), currentFile);

        createViews();
        createDispatcher();
    }

    private void createViews() {
        final ContentView contentView = new ContentView(this, THEME);

        MainControlBar bar = new MainControlBar(this,5);

        ViewGroup layout = createLayout(bar, contentView);

        contentView.add(bar);

        fileError = new ErrorMsgView(this);
        contentView.add(fileError);

        contentView.add(getErrorView());

        busyControl = new BusyViewControlIID(contentView);
        busyControl.busy.setOrientation(BusyViewContainer.BOTTOM_RIGHT);

        contentView.add(layout);

        initButtonBar(bar);

        setContentView(contentView);

    }

     protected View createAttributesView() {
        final AttributesView v = new AttributesView(this, getAppContext().getStorage());
        addTarget(v, InfoID.FILEVIEW, InfoID.EDITOR_OVERLAY);
        return v;
    }

    private void initButtonBar(MainControlBar bar) {
        previousFile =  bar.addImageButton(R.drawable.go_up_inverse);
        nextFile = bar.addImageButton(R.drawable.go_down_inverse);

        fileOperation = new PreviewView(getServiceContext(),getAppContext().getSummaryConfig());
        bar.addButton(fileOperation);

        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setOnClickListener1(this);
    }

    protected abstract ViewGroup createLayout(MainControlBar bar, ContentView contentView);

    private void createDispatcher() {
        addSource(new TrackerSource(getServiceContext(), getAppContext().getBroadcaster()));
        addSource(new CurrentLocationSource(getServiceContext(), getAppContext().getBroadcaster()));
        addSource(new OverlaysSource(getAppContext()));

        addSource(editorSource);

        addTarget(busyControl,
                InfoID.FILEVIEW,
                InfoID.OVERLAY,
                InfoID.OVERLAY+1,
                InfoID.OVERLAY+2,
                InfoID.OVERLAY+3);
        addTarget(fileOperation, InfoID.FILEVIEW);

        addTarget((iid, info) -> {
            String newFileID = info.getFile().toString();

            if (!Objects.equals(currentFileID, newFileID)) {
                currentFileID = newFileID;
                map.frameBounding(info.getBoundingBox());
                AppLog.i(AbsFileContentActivity.this, info.getFile().getName());
            }
        }, InfoID.FILEVIEW);


        addTarget((iid, info) -> fileError.displayError(
                getServiceContext(),
                info.getFile()), InfoID.FILEVIEW);
    }



    @Override
    public void onClick(final View v) {
        if (v == previousFile || v ==nextFile) {
            changeFileAsk(v);
        } else if (v == fileOperation) {
            new FileMenu(this, currentFile.getInfo().getFile()).showAsPopup(this, v);
        }
    }

    private void changeFileAsk(View v) {
        if (editorSource.isModified()) {
            new AppDialog() {
                @Override
                protected void onPositiveClick() {
                    editorSource.releaseEditorSave();
                    changeFile(v);
                }

                @Override
                public void onNeutralClick() {
                    editorSource.releaseEditorDiscard();
                    changeFile(v);
                }


            }.displaySaveDiscardDialog(this, editorSource.getFile().getName());
        } else {
            changeFile(v);
        }
    }

    private void changeFile(View v) {
        if (v == previousFile) {
            editorSource.releaseEditorDiscard();
            currentFile.moveToPrevious();
        } else if (v ==nextFile) {
            editorSource.releaseEditorDiscard();
            currentFile.moveToNext();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (editorSource.isModified()) {
                new AppDialog() {
                    @Override
                    protected void onPositiveClick() {

                        editorSource.releaseEditorSave();
                        closeActivity();
                    }

                    @Override
                    public void onNeutralClick() {
                        editorSource.releaseEditorDiscard();
                        closeActivity();
                    }
                }.displaySaveDiscardDialog(this, editorSource.getFile().getName());
            } else {
                closeActivity();
            }

        } catch (Exception e) {
            AppLog.e(AbsFileContentActivity.this, e);
            closeActivity();
        }
    }

    private void closeActivity() {
        super.onBackPressed();
    }
}
