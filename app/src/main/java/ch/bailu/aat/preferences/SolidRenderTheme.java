package ch.bailu.aat.preferences;

import android.content.Context;

import org.mapsforge.map.rendertheme.ExternalRenderTheme;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SolidRenderTheme extends SolidFile {

    private static final String ORUX_THEMES="../mapstyles";

    public SolidRenderTheme(Context c) {
        super(Storage.global(c), SolidRenderTheme.class.getSimpleName());
    }


    @Override
    public String getLabel() {
        return "Offline map theme*";
    }


    public XmlRenderTheme getValueAsRenderTheme() {
        return toRenderTheme(getValueAsString());
    }



    public String getValueAsThemeID() {
        return toThemeID(getValueAsString());
    }


    public static String toThemeID(String name) {
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

        XmlRenderTheme theme = InternalRenderTheme.DEFAULT;

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

        final File maps = new SolidMapsForgeDirectory(getContext()).getValueAsFile();
        final File orux = new File(maps, ORUX_THEMES);
        add_xmlInSubdirectories(list,maps);
        add_xmlInSubdirectories(list,orux);


        return list;
    }

    public static ArrayList<String> add_xmlInSubdirectories(final ArrayList<String> list, File directory) {
        directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) add_xml(list, f);
                return false;
            }
        });
        return list;
    }
    public static ArrayList<String> add_xml(final ArrayList<String> list, File directory) {
        directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isFile() && f.toString().endsWith(".xml")) {
                    add_r(list, f);
                }
                return false;
            }
        });
        return list;
    }
}
