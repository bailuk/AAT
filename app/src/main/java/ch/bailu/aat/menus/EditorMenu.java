package ch.bailu.aat.menus;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;

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

    public EditorMenu(ServiceContext sc, EditorInterface e, Foc f) {
        editor = e;
        scontext = sc;
        context = sc.getContext();
        file = f;
    }


    @Override
    public void inflate(Menu menu) {
        add(new Item(menu, R.string.edit_save) {
            @Override
            public void onClick() {
                editor.save();
            }
        });

        add(new Item(menu, R.string.edit_save_copy) {
            @Override
            public void onClick() {
                saveCopy();
            }
        });

        add(new Item(menu, R.string.edit_save_copy_to) {
            @Override
            public void onClick() {
                saveCopyTo();
            }
        });

        add(new Item(menu, R.string.edit_inverse) {
            @Override
            public void onClick() {
                editor.inverse();
            }
        });

        add(new Item(menu, R.string.edit_change_type) {
            @Override
            public void onClick() {
                changeType();
            }
        });

        add(new Item(menu, R.string.edit_simplify) {
            @Override
            public void onClick() {
                editor.simplify();
            }
        });
        add(new Item(menu, R.string.edit_attach) {
            @Override
            public void onClick() {
                attach();
            }
        });
        add(new Item(menu, R.string.edit_fix) {
            @Override
            public void onClick() {
                editor.fix();
            }
        });
        add(new Item(menu, R.string.edit_clear) {
            @Override
            public void onClick() {
                editor.clear();
            }
        });
        add(new Item(menu, R.string.edit_cut_remaining) {
            @Override
            public void onClick() {
                editor.cutRemaining();
            }
        });
        add(new Item(menu, R.string.edit_cut_preceding) {
            @Override
            public void onClick() {
                editor.cutPreceding();
            }
        });
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

        dialog.setTitle(R.string.edit_change_type);
        dialog.setItems(GpxType.toStrings(), (dialogInterface, i) -> editor.setType(GpxType.fromInteger(i)));

        dialog.show();
    }
}
