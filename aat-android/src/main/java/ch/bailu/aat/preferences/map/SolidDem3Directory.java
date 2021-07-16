package ch.bailu.aat.preferences.map;

import android.content.Context;

import java.util.ArrayList;

import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.ToDo;
import ch.bailu.aat.util.fs.AndroidVolumes;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.foc.Foc;

public class SolidDem3Directory extends SolidFile {

    public final static String DEM3_DIR = "dem3";

    public SolidDem3Directory(Context c) {
        super(c, SolidDem3Directory.class.getSimpleName());
    }


    @Override
    public String getLabel() {
        return ToDo.translate("Location of dem3 tiles");
    }

    @Override
    public String getValueAsString() {
        String r = super.getValueAsString();

        if (r.equals(Storage.DEF_VALUE)) {
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


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        AndroidVolumes volumes = new AndroidVolumes(getContext());

        // exists and is writeable
        for (Foc vol : volumes.getVolumes()) {
            final Foc dem3 =  vol.child(DEM3_DIR);
            final Foc aat = vol.child(AppDirectory.DIR_AAT_DATA + "/" + DEM3_DIR);

            add_w(list, aat);
            add_w(list, dem3);
        }


        // exists not but parent is writeable
        for (Foc vol : volumes.getVolumes()) {
            final Foc aat_dem3 = vol.child(AppDirectory.DIR_AAT_DATA + "/" + DEM3_DIR);
            final Foc dem3 =  vol.child(DEM3_DIR);


            if (!aat_dem3.exists())  add_w(list, aat_dem3.parent(), aat_dem3);
            if (!dem3.exists()) add_w(list, dem3.parent(), dem3);
        }

        // exists and is read only
        for (Foc vol : volumes.getVolumes()) {
            final Foc aat_dem3 = vol.child(AppDirectory.DIR_AAT_DATA + "/" + DEM3_DIR);
            final Foc dem3 =  vol.child(DEM3_DIR);

            add_ro(list, aat_dem3);
            add_ro(list, dem3);
        }

        // official writeable cache directory
        for (Foc cache : volumes.getCaches()) {
            final Foc dem3 = cache.child(DEM3_DIR);
            add_w(list, cache, dem3);
        }


        return list;
    }
}
