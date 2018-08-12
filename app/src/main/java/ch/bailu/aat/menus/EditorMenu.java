package ch.bailu.aat.menus;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import ch.bailu.aat.services.editor.EditorInterface;
import ch.bailu.aat.util.ToDo;
import ch.bailu.aat.views.preferences.AbsSelectOverlayDialog;
import ch.bailu.util_java.foc.Foc;

public class EditorMenu extends AbsMenu {
    private final EditorInterface editor;
    private final Context context;

    private MenuItem save, saveCopy, saveCopyTo, inverse, changeType, simplify, attach, fix;

    public EditorMenu(Context c, EditorInterface e) {
        editor = e;
        context = c;
    }


    @Override
    public void inflate(Menu menu) {
        save = menu.add(ToDo.translate("Save"));
        saveCopy = menu.add(ToDo.translate("Save Copy"));
        saveCopyTo = menu.add(ToDo.translate("Save Copy To"));
        inverse = menu.add(ToDo.translate("Inverse"));
        changeType = menu.add(ToDo.translate("Change Type"));
        simplify = menu.add(ToDo.translate("Simplify"));
        attach = menu.add(ToDo.translate("Attach File"));
        fix = menu.add(ToDo.translate("Fix"));
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
            editor.saveAs();

        }else if (item == inverse) {
            editor.inverse();

        } else if (item == attach) {
            new AbsSelectOverlayDialog(context) {
                @Override
                protected void onFileSelected(Foc file) {
                    editor.attach(file);
                }
            };

        } else if (item == fix) {
            editor.fix();

        } else if (item == simplify) {
            editor.simplify();

        } else {
            return false;

        }

        return true;
    }
}
