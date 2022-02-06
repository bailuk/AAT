package ch.bailu.aat_lib.preferences;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.resources.Res;
import ch.bailu.foc.Foc;
import ch.bailu.foc.FocFactory;

public abstract class SolidFile extends SolidString {

    private final FocFactory focFactory;

    public SolidFile(StorageInterface s, String k, FocFactory focFactory) {
        super(s, k);
        this.focFactory = focFactory;
    }

    public Foc getValueAsFile() {
        return focFactory.toFoc(getValueAsString());
    }

    @Nonnull
    @Override
    public String toString() {
        return getValueAsFile().getPathName();
    }

    public String getIconResource() {return "folder_inverse";}

    public abstract ArrayList<String> buildSelection(ArrayList<String> list);


    @Override
    public String getToolTip() {
        return  getPermissionText(getValueAsFile());
    }

    private static String getPermissionText(Foc f) {

        if (f.exists() == false) {
            if (f.hasParent()) {
                return getPermissionText(f.parent());
            } else {
                return f.getPathName() + Res.str().file_is_missing();
            }
        } else if (f.canWrite()) {
            if (f.canRead()) {
                return f.getPathName() + Res.str().file_is_writeable();
            } else {
                return f.getPathName() + Res.str().file_is_writeonly();
            }
        } else if (f.canRead()) {
            return f.getPathName() + Res.str().file_is_readonly();
        } else if (f.hasParent()){
            return getPermissionText(f.parent());
        } else  {
            return f.getPathName() + Res.str().file_is_denied();
        }
    }


    public static ArrayList<String> add_extInSubdirectories(final ArrayList<String> list,
                                                            Foc directory, String ext) {
        directory.foreachDir(child -> add_ext(list, child, ext));
        return list;
    }

    public static ArrayList<String> add_ext(final ArrayList<String> list, Foc directory, String ext) {
        directory.foreachFile(child -> {
            if (child.getName().endsWith(ext)) SelectionList.add_r(list, child);
        });
        return list;
    }

}
