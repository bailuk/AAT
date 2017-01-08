package ch.bailu.aat.services.render;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.reader.header.MapFileException;
import org.mapsforge.map.reader.header.MapFileInfo;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ch.bailu.aat.util.ui.AppLog;

public class MapList {
    private final static int MIN_SIZE=1024*1024;

    private final ArrayList<Entry> mapFiles = new ArrayList(10);


    private static  class Entry {
        public final File file;
        public final MapFileInfo info;

        public final boolean isWorld;


        public Entry(File f, MapFileInfo i) {
            file = f;
            info = i;

            isWorld = info.zoomLevelMin == 0 && info.zoomLevelMax == 7;

            AppLog.d(this, f.toString() + " " +info.zoomLevelMin + " " + info.zoomLevelMax);
        }


        public boolean contains(BoundingBox b) {
            return info.boundingBox.contains(b.maxLatitude, b.maxLongitude) &&
                    info.boundingBox.contains(b.minLatitude, b.minLongitude);
        }

        public boolean intersects(BoundingBox b) {
            return info.boundingBox.intersects(b);
        }

        public boolean includesZoom(byte zoom) {
            return (info.zoomLevelMax >= zoom && info.zoomLevelMin <= zoom);
        }

    }


    public MapList(File directory) {
        fillList(getFiles(directory));
        sortList();
    }

    private File[] getFiles(File d) {
        File[] files;

        try {
            files = d.listFiles(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return (
                            f.exists() &&
                                    f.isFile() &&
                                    f.length() > MIN_SIZE &&
                                    f.getName().endsWith(".map"));
                }
            });
        } catch (SecurityException e) {
            files = null;
        }

        if (files == null) {
            files = new File[0];
        }
        return files;
    }

    private void fillList(File[] files) {
        for (File f : files) {
            MapFile map = null;
            try {
                map = new MapFile(f);
                MapFileInfo info = map.getMapFileInfo();
                mapFiles.add(new Entry(f, info));

            } catch (MapFileException e) {
                AppLog.d(this, f.toString() + ": not valid!");

            } finally {
                if (map != null)
                    map.close();
            }
        }
    }


    private void sortList() {
        Collections.sort(mapFiles, new Comparator<Entry>() {

            @Override
            public int compare(Entry a, Entry b) {
                if (a.info.zoomLevelMax < b.info.zoomLevelMax) {
                    return -1;
                } else if (a.info.zoomLevelMax > b.info.zoomLevelMax) {
                    return 1;
                }

                return 0;
            }


        });
    }



    public ArrayList<File> getFiles(Tile tile) {
        BoundingBox b = tile.getBoundingBox();

        ArrayList<File> files = new ArrayList(4);

        String log = "?-> ";

        for (Entry e: mapFiles) {
            boolean Z = e.includesZoom(tile.zoomLevel);
            boolean C = e.contains(b);
            boolean E = e.intersects(b);


            if (Z) log += "Z";
            if (C) log += "C";
            if (E) log += "E";

            log += "  | ";

            if (Z && (C || E)) {



                if (e.isWorld) {

                    if (files.size() == 0) {
                        files.add(e.file);

                        log += " W " + files.size();
                        //AppLog.d(this, log);
                        return files;
                    }
                    log += " wrong order!!";


                } else {
                    files.add(e.file);

                }
            }



        }

        log += files.size();
        //AppLog.d(this, log);
        return files;
    }
}
