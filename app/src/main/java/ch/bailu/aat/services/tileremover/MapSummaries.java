package ch.bailu.aat.services.tileremover;

import android.content.Context;

import java.io.File;

import ch.bailu.aat.R;

public class MapSummaries {


    public final static int SUMMARY_SIZE = 20;
    private final MapSummary[] mapSummaries = new MapSummary[SUMMARY_SIZE];


    public void reset(Context c) {
        for(int i = 0; i< mapSummaries.length; i++) {
            mapSummaries[i] = new MapSummary();
        }
        mapSummaries[0].setName(c.getString(R.string.p_trim_total));
    }

    public void setName(int s, String name) {
        mapSummaries[s].setName(name);
    }

    public void addFile(int s, TileFile file) {
        long length = file.length();
        mapSummaries[0].addFile(length);
        mapSummaries[s].addFile(length);
    }

    public void resetToRemove() {
        for (MapSummary mapSummary : mapSummaries) {
            mapSummary.clear_rm();
        }
    }

    public void addFileToRemove(TileFile f) {
        final long l = f.length();

        mapSummaries[0].addFileToRemove(f.length());
        mapSummaries[indexFromHashCode(f.directoryHashCode())].addFileToRemove(l);
    }


    public void addFileRemoved(TileFile f) {
        final long length = f.length();

        mapSummaries[0].addFileRemoved(length);
        mapSummaries[indexFromHashCode(f.directoryHashCode())].addFileRemoved(length);
    }

    public int indexFromHashCode(int hashCode) {
        for (int i = 1; i<mapSummaries.length; i++) {
            if (mapSummaries[i].hashCode() == hashCode) {
                return i;
            }
        }
        return 0;
    }

    public long getNewSize(int i) {
        return mapSummaries[i].newSize;
    }


    public MapSummaryInterface[] getMapSummaries() {
        return mapSummaries;
    }

    public long getRemoveCount() {
        return mapSummaries[0].countToRemove;
    }


    public int hashCode(int i) {
        return mapSummaries[i].hashCode();
    }


    public File toFile(File tileDirectory, TileFile t) {
        return t.toFile(new File(tileDirectory, getMapDirectory(t.hashCode())));
    }

    public String getMapDirectory(int hashCode) {
        return mapSummaries[indexFromHashCode(hashCode)].getName();
    }

}
