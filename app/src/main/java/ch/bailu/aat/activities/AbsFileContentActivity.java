package ch.bailu.aat.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.File;

import ch.bailu.aat.R;
import ch.bailu.aat.dispatcher.CurrentLocationSource;
import ch.bailu.aat.dispatcher.EditorSource;
import ch.bailu.aat.dispatcher.IteratorSource;
import ch.bailu.aat.dispatcher.OverlaySource;
import ch.bailu.aat.dispatcher.TrackerSource;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapViewInterface;
import ch.bailu.aat.menus.FileMenu;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.aat.util.ui.ToolTip;
import ch.bailu.aat.views.BusyButton;
import ch.bailu.aat.views.ContentView;
import ch.bailu.aat.views.MainControlBar;

public abstract class AbsFileContentActivity extends AbsDispatcher implements OnClickListener {

    protected IteratorSource  currentFile;
    protected ImageButton nextFile, previousFile, fileOperation;

    private boolean            firstRun = true;

    private BusyButton         busyButton;
    protected MapViewInterface map;


    protected EditorHelper editor_helper = null;
    protected EditorSource editor_source= null;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        firstRun = true;

        editor_helper = new EditorHelper(getServiceContext());

        createViews();
        createDispatcher();
    }



    private void createViews() {
        final ViewGroup contentView = new ContentView(this);

        MainControlBar bar = new MainControlBar(this);
        contentView.addView(bar);
        contentView.addView(createLayout(bar));

        initButtonBar(bar);

        setContentView(contentView);

    }


    private void initButtonBar(MainControlBar bar) {
        previousFile =  bar.addImageButton(R.drawable.go_up_inverse);
        nextFile = bar.addImageButton(R.drawable.go_down_inverse);
        fileOperation = bar.addImageButton(R.drawable.edit_select_all_inverse);

        ToolTip.set(fileOperation, R.string.tt_menu_file);

        busyButton = bar.getMenu();
        busyButton.startWaiting();

        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setOnClickListener1(this);
    }


    protected abstract View createLayout(MainControlBar bar);





    private void createDispatcher() {
        currentFile = new IteratorSource.FollowFile(getServiceContext());
        editor_source = new EditorSource(getServiceContext(), editor_helper);

        addSource(new TrackerSource(getServiceContext()));
        addSource(new CurrentLocationSource(getServiceContext()));
        addSource(new OverlaySource(getServiceContext()));
        addSource(currentFile);
        addSource(editor_source);

        addTarget(busyButton.getBusyControl(InfoID.FILEVIEW), InfoID.FILEVIEW);

    }


    @Override
    public void onResumeWithService() {
        super.onResumeWithService();

        if (firstRun) {
            frameCurrentFile();
            firstRun = false;
        }
    }


    private void frameCurrentFile() {
        map.frameBounding(currentFile.getInfo().getBoundingBox());
    }



    @Override
    public void onClick(View v) {

        if (v == previousFile) {
            switchFile(v);

        } else if (v ==nextFile) {
            switchFile(v);

        } else if (v == fileOperation) {
            new FileMenu(this, FocAndroid.factory(this, currentFile.getInfo().getPath())).showAsPopup(this, v);
        }

    }

    protected void switchFile(View v) {
        busyButton.startWaiting();

        if (v==nextFile)
            currentFile.moveToNext();
        else if (v==previousFile)
            currentFile.moveToPrevious();

        frameCurrentFile();
    }




}
