package ch.bailu.aat.menus;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.preferences.map.SolidOverlayFileList;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.Obj;
import ch.bailu.aat.services.cache.ObjGpx;
import ch.bailu.aat.services.editor.EditorInterface;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.ui.AppSelectDirectoryDialog;
import ch.bailu.aat.views.preferences.AbsSelectOverlayDialog;
import ch.bailu.util_java.foc.Foc;

public final class EditorMenu extends AbsMenu {
    private final EditorInterface editor;
    private final Foc file;
    private final ServiceContext scontext;
    private final Context context;

    private MenuItem save, saveCopy, saveCopyTo,
            inverse, changeType, simplify, attach, fix, clearAll, cutRemaining, cutPreceding;

    public EditorMenu(ServiceContext sc, EditorInterface e, Foc f) {
        editor = e;
        scontext = sc;
        context = sc.getContext();
        file = f;
    }


    @Override
    public void inflate(Menu menu) {
        save = menu.add(R.string.edit_save);
        saveCopy = menu.add(R.string.edit_save_copy);
        saveCopyTo = menu.add(R.string.edit_save_copy_to);
        inverse = menu.add(R.string.edit_inverse);
        changeType = menu.add(R.string.edit_change_type);
        simplify = menu.add(R.string.edit_simplify);
        attach = menu.add(R.string.edit_attach);
        fix = menu.add(R.string.edit_fix);
        clearAll = menu.add(R.string.edit_clear);
        cutRemaining = menu.add(R.string.edit_cut_remaining);
        cutPreceding = menu.add(R.string.edit_cut_preceding);
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

    @Override
    public boolean onItemClick(MenuItem item) {
        if (item == save) {
            editor.save();

        } else if (item == saveCopy) {
            saveCopy();

        } else if (item == saveCopyTo) {
           saveCopyTo();


        }else if (item == inverse) {
            editor.inverse();

        } else if (item == attach) {
            attach();

        } else if (item == fix) {
            editor.fix();

        } else if (item == simplify) {
            editor.simplify();

        } else if (item == clearAll) {
            editor.clear();

        } else if (item == cutPreceding) {
            editor.cutPreceding();

        } else if (item == cutRemaining) {
            editor.cutRemaining();

        } else if (item == changeType) {
            changeType();

        } else {
            return false;

        }

        return true;
    }


    private void saveCopy() {
        if (file.equals(AppDirectory.getEditorDraft(context))) {
            editor.saveTo(
                    AppDirectory.getDataDirectory(context, AppDirectory.DIR_OVERLAY));

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

        dialog.setTitle(changeType.getTitle());
        dialog.setItems(GpxType.toStrings(), (dialogInterface, i) -> editor.setType(GpxType.fromInteger(i)));

        dialog.show();
    }
}
