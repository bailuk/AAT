package ch.bailu.aat.menus;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;

import ch.bailu.aat.R;
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileList;
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectory;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.Obj;
import ch.bailu.aat.services.cache.ObjGpx;
import ch.bailu.aat.services.editor.EditorInterface;
import ch.bailu.aat.util.ui.AppSelectDirectoryDialog;
import ch.bailu.aat.views.preferences.AbsSelectOverlayDialog;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;

public final class EditorMenu extends AbsMenu {
    private final EditorInterface editor;
    private final Foc file;
    private final ServiceContext scontext;
    private final Context context;

    public EditorMenu(ServiceContext sc, EditorInterface e, Foc f) {
        editor = e;
        scontext = sc;
        context = sc.getContext();
        file = f;
    }


    @Override
    public void inflate(Menu menu) {
        add(menu, R.string.edit_save, editor::save);
        add(menu, R.string.edit_save_copy, this::saveCopy);
        add(menu, R.string.edit_save_copy_to, this::saveCopyTo);
        add(menu, R.string.edit_inverse, editor::inverse);
        add(menu, R.string.edit_change_type, this::changeType);
        add(menu, R.string.edit_simplify, editor::simplify);
        add(menu, R.string.edit_attach, this::attach);
        add(menu, R.string.edit_fix, editor::fix);
        add(menu, R.string.edit_clear, editor::clear);
        add(menu, R.string.edit_cut_remaining, editor::cutRemaining);
        add(menu, R.string.edit_cut_preceding, editor::cutPreceding);
    }


    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public Drawable getIcon() {
        return null;
    }

    @Override
    public void prepare(Menu menu) {

    }

    private void saveCopy() {
        if (file.equals(AppDirectory.getEditorDraft(new AndroidSolidDataDirectory(context)))) {
            editor.saveTo(
                    AppDirectory.getDataDirectory(new AndroidSolidDataDirectory(context), AppDirectory.DIR_OVERLAY));

        } else if (file.hasParent())
            editor.saveTo(file.parent());

    }

    private void saveCopyTo() {
        new AppSelectDirectoryDialog(context, file) {
            @Override
            public void copyTo(Context context, Foc srcFile, Foc destDirectory) {
                editor.saveTo(destDirectory);
            }
        };
    }


    private void attach() {
        new AbsSelectOverlayDialog(context) {
            @Override
            protected void onFileSelected(SolidOverlayFileList slist, int index, Foc file) {
                new InsideContext(scontext) {
                    @Override
                    public void run() {
                        Obj handle = scontext.getCacheService().getObject(file.getPath(),
                                new ObjGpx.Factory());

                        if (handle instanceof ObjGpx) {
                            ObjGpx gpxObject = (ObjGpx) handle;

                            if (gpxObject.isReadyAndLoaded()) {
                                editor.attach(gpxObject.getGpxList());
                            }
                        }

                        handle.free();

                        slist.setEnabled(index, false);
                    }
                };
            }
        };
    }


    private void changeType() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle(R.string.edit_change_type);
        dialog.setItems(GpxType.toStrings(), (dialogInterface, i) -> editor.setType(GpxType.fromInteger(i)));

        dialog.show();
    }
}
