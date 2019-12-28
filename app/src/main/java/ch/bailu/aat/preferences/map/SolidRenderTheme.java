package ch.bailu.aat.preferences.map;

import android.content.Context;

import org.mapsforge.map.rendertheme.ExternalRenderTheme;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidFile;
import ch.bailu.util_java.foc.Foc;

public class SolidRenderTheme extends SolidFile {


    public SolidRenderTheme(Context c) {
        super(c, SolidRenderTheme.class.getSimpleName());
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
        return new File(themeFile).getName().replace(".xml", "");
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
        add_xmlInSubdirectories(list, maps);

        ArrayList<Foc> dirs = new SolidMapsForgeDirectory(getContext()).getWellKnownMapDirs();
        for (Foc dir: dirs) {
             add_xmlInSubdirectories(list, dir);
        }

        return list;
    }

    public static ArrayList<String> add_xmlInSubdirectories(final ArrayList<String> list, Foc directory) {
        directory.foreachDir(new Foc.Execute() {
            @Override
            public void execute(Foc child) {
                add_xml(list, child);
            }
        });
        return list;
    }

    public static ArrayList<String> add_xml(final ArrayList<String> list, Foc directory) {
        directory.foreachFile(new Foc.Execute() {
            @Override
            public void execute(Foc child) {
                if (child.getName().endsWith(".xml")) add_r(list, child);
            }
        });
        return list;
    }
}
