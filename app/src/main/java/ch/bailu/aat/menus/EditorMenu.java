package ch.bailu.aat.menus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.preferences.SolidOverlayFileList;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.GpxObject;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.editor.EditorInterface;
import ch.bailu.aat.util.ToDo;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.ui.AppSelectDirectoryDialog;
import ch.bailu.aat.views.preferences.AbsSelectOverlayDialog;
import ch.bailu.util_java.foc.Foc;

public class EditorMenu extends AbsMenu {
    private final EditorInterface editor;
    private final Foc file;
    private final ServiceContext scontext;
    private final Context context;

    private MenuItem save, saveCopy, saveCopyTo,
            inverse, changeType, simplify, attach, fix, clearAll;

    public EditorMenu(ServiceContext sc, EditorInterface e, Foc f) {
        editor = e;
        scontext = sc;
        context = sc.getContext();
        file = f;
    }


    @Override
    public void inflate(Menu menu) {
        save = menu.add(ToDo.translate("Save"));
        saveCopy = menu.add(ToDo.translate("Save copy"));
        saveCopyTo = menu.add(ToDo.translate("Save copy to"));
        inverse = menu.add(ToDo.translate("Inverse"));
        changeType = menu.add(ToDo.translate("Set GPX type"));
        simplify = menu.add(ToDo.translate("Simplify"));
        attach = menu.add(ToDo.translate("Attach file"));
        fix = menu.add(ToDo.translate("Fix"));
        clearAll = menu.add(ToDo.translate("Clear all"));
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
                        ObjectHandle handle = scontext.getCacheService().getObject(file.getPath(),
                                new GpxObject.Factory());

                        if (handle instanceof GpxObject) {
                            GpxObject gpxObject = (GpxObject) handle;

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
