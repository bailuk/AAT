package ch.bailu.aat_lib.preferences.map;

import java.util.ArrayList;

import ch.bailu.aat_lib.coordinates.Dem3Coordinates;
import ch.bailu.aat_lib.factory.FocFactory;
import ch.bailu.aat_lib.preferences.SolidFile;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.ToDo;
import ch.bailu.foc.Foc;

public class SolidDem3Directory extends SolidFile {

    public final static String DEM3_DIR = "dem3";

    public SolidDem3Directory(StorageInterface storageInterface, FocFactory focFactory) {
        super(storageInterface, SolidDem3Directory.class.getSimpleName(), focFactory);
    }


    @Override
    public String getLabel() {
        return ToDo.translate("Location of dem3 tiles");
    }

    @Override
    public String getValueAsString() {
        String r = super.getValueAsString();

        if (getStorage().isDefaultString(r)) {
            r = getDefaultValue();

            setValue(r);
        }
        return r;
    }



    public String getDefaultValue() {
        ArrayList<String> list = new ArrayList<>(5);

        list = buildSelection(list);

        if (list.size()>0) {
            return list.get(0);
        }
        return "";
    }




    /**
     *
     * @return a complete file path for a dem3 tile. The base path is taken from configuration.
     * Example: /sdcard/aat_data/dem3/N16/N16E077.hgt.zip
     */
    public Foc toFile(Dem3Coordinates dem3Coordinates) {
        return toFile(dem3Coordinates, getValueAsFile());
    }


    private Foc toFile(Dem3Coordinates dem3Coordinates, Foc base) {
        return base.descendant(dem3Coordinates.toExtString() + ".hgt.zip");
    }

    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        return list;
    }
}
