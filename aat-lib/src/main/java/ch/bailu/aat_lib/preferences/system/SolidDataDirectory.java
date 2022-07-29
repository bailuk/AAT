package ch.bailu.aat_lib.preferences.system;

import java.util.ArrayList;

import ch.bailu.aat_lib.preferences.SolidFile;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.foc.FocFactory;

public class SolidDataDirectory extends SolidFile {

    private final SolidDataDirectoryDefault defaultDirectory;

    public SolidDataDirectory(SolidDataDirectoryDefault defaultDirectory, FocFactory focFactory) {
        super(defaultDirectory.getStorage(), SolidDataDirectory.class.getSimpleName(), focFactory);
        this.defaultDirectory = defaultDirectory;
    }

    @Override
    public String getLabel() {
        return Res.str().p_directory_data();
    }

    @Override
    public String getValueAsString() {
        String r = super.getValueAsString();


        if (getStorage().isDefaultString(r))
            return defaultDirectory.getValueAsString();

        return r;
    }

    @Override
    public boolean hasKey(String s) {
        return super.hasKey(s) || defaultDirectory.hasKey(s);
    }

    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        return defaultDirectory.buildSelection(list);
    }
}
