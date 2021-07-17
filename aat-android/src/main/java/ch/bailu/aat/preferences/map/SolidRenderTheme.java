package ch.bailu.aat.preferences.map;

import android.content.Context;

import org.mapsforge.map.rendertheme.ExternalRenderTheme;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.factory.AndroidFocFactory;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.preferences.SolidFile;
import ch.bailu.foc.Foc;

public class SolidRenderTheme extends SolidFile {

    private final static String EXTENSION = ".xml";

    private final Context context;

    public SolidRenderTheme(Context c) {
        super(new Storage(c), SolidRenderTheme.class.getSimpleName(), new AndroidFocFactory(c));
        context = c;
    }

    public Context getContext() {
        return context;
    }


    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_mapsforge_theme);
    }


    public XmlRenderTheme getValueAsRenderTheme() {
        return toRenderTheme(getValueAsString());
    }



    public String getValueAsThemeID() {
        return toThemeID(getValueAsString());
    }


    public String getValueAsThemeName() {
        return toThemeName(getValueAsThemeID());
    }

    public static String toThemeName(String themeFile) {
        return new File(themeFile).getName().replace(EXTENSION, "");
    }

    private static String toThemeID(String name) {
        if (name.equals(InternalRenderTheme.DEFAULT.toString())) {
            return name;
        } else if (name.equals(InternalRenderTheme.OSMARENDER.toString())) {
            return name;
        } else {
            try {
                new ExternalRenderTheme(new File(name));
                return name;
            } catch (FileNotFoundException e1) {
                return InternalRenderTheme.DEFAULT.toString();
            }
        }
    }

    public static XmlRenderTheme toRenderTheme(String name) {

        XmlRenderTheme theme;

        if (name.equals(InternalRenderTheme.DEFAULT.toString())) {
            theme = InternalRenderTheme.DEFAULT;
        } else if (name.equals(InternalRenderTheme.OSMARENDER.toString())) {
            theme = InternalRenderTheme.OSMARENDER;
        } else {
            try {
                theme = new ExternalRenderTheme(new File(name));
            } catch (FileNotFoundException e1) {
                theme = InternalRenderTheme.DEFAULT;
            }
        }

        return theme;

    }


    @Override
    public ArrayList<String> buildSelection(ArrayList<String> list) {
        list.add(InternalRenderTheme.DEFAULT.toString());
        list.add(InternalRenderTheme.OSMARENDER.toString());

        Foc maps = new SolidMapsForgeDirectory(getContext()).getValueAsFile();
        add_ext(list, maps, EXTENSION);
        add_extInSubdirectories(list, maps, EXTENSION);

        ArrayList<Foc> dirs = new SolidMapsForgeDirectory(getContext()).getWellKnownMapDirs();
        for (Foc dir: dirs) {
            add_ext(list, dir, EXTENSION);
            add_extInSubdirectories(list, dir, EXTENSION);
        }

        return list;
    }
}
