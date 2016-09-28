package ch.bailu.aat.services.tileremover;

import java.io.File;

public class TilesSummaries {
    public final static int SUMMARY_SIZE = 20;
    private TilesSummary[] summaries = new TilesSummary[SUMMARY_SIZE];


    public void reset() {
        for(int i = 0; i< summaries.length; i++) {
            summaries[i] = new TilesSummary();
            summaries[0].setName("Total*");
        }
    }

    public void setName(int s, String name) {
        summaries[s].setName(name);
    }

    public void inc(int s, TileFile file) {
        long length = file.length();
        summaries[0].inc(length);
        summaries[s].inc(length);
    }

    public void reset_rm() {
        for (int i = 0; i< summaries.length; i++) {
            summaries[i].clear_rm();
        }
    }

    public void inc_rm(TileFile file) {
        final long length = file.length();

        summaries[0].inc_rm(length);
        summaries[indexFromHashCode(file.hashCode())].inc_rm(length);
    }


    public void inc_removed(TileFile file) {
        final long length = file.length();

        summaries[0].inc_removed(length);
        summaries[indexFromHashCode(file.hashCode())].inc_removed(length);
    }

    public int indexFromHashCode(int hashCode) {
        for (int i = summaries.length-1; i>=0; i--) {
            if (summaries[i].hashCode() == hashCode) {
                return i;
            }
        }
        return 0;
    }

    public long getNewSize() {
        return summaries[0].size_new;
    }


    public TilesSummaryInterface[] getSummaries() {
        return summaries;
    }

    public long getRemoveCount() {
        return summaries[0].count_rm;
    }


    public int hashCode(int i) {
        return summaries[i].hashCode();
    }

    public File toFile(File tileDirectory, TileFile t) {
        final String subDir = summaries[indexFromHashCode(t.hashCode())].getName();

        return t.toFile(new File(tileDirectory, subDir));
    }
}
