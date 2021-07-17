package ch.bailu.aat_lib.preferences;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.factory.FocFactory;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.foc.Foc;

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



    public int getIconResource() {return Res.getIconResource("R.drawable.folder_inverse");}

    public abstract ArrayList<String> buildSelection(ArrayList<String> list);


    public static void add_ro(ArrayList<String> list, Foc file) {
        add_ro(list, file, file);
    }

    public static void add_ro(ArrayList<String> list, Foc check, Foc file) {
        if (check.canOnlyRead()) {
            list.add(file.toString());
        }
    }

    public static void add_r(ArrayList<String> list, Foc file) {
        if (file.canRead())
            list.add(file.toString());
    }

    public static void add_subdirectories_r(final ArrayList<String> list, Foc directory) {
        directory.foreachDir(new Foc.Execute() {
            @Override
            public void execute(Foc child) {
                add_r(list, child);
            }
        });
    }


    public static void add_w(ArrayList<String> list, Foc file) {
        add_w(list, file, file);

    }


    public static void add_w(ArrayList<String> list, Foc check, Foc file) {
        if (file != null && check.canWrite())  {
            list.add(file.toString());
        }
    }



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
        directory.foreachDir(new Foc.Execute() {
            @Override
            public void execute(Foc child) {
                add_ext(list, child, ext);
            }
        });
        return list;
    }

    public static ArrayList<String> add_ext(final ArrayList<String> list, Foc directory, String ext) {
        directory.foreachFile(new Foc.Execute() {
            @Override
            public void execute(Foc child) {
                if (child.getName().endsWith(ext)) add_r(list, child);
            }
        });
        return list;
    }

}
