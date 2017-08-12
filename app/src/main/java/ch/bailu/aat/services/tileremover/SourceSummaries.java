package ch.bailu.aat.services.tileremover;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;

import ch.bailu.aat.R;
import ch.bailu.util_java.foc.Foc;

public class SourceSummaries {
    public final static int SUMMARY_SIZE = 20;

    private final ArrayList<SourceSummary>
            sourceSummaries = new ArrayList<>(SUMMARY_SIZE);

    private final static SourceSummary NULL_SUMMARY = new SourceSummary("NULL");

    public SourceSummaries(Context c) {
        reset(c);
    }

    private void reset(Context c) {
        sourceSummaries.clear();
        sourceSummaries.add(new SourceSummary(c.getString(R.string.p_trim_total)));
    }

    public void rescanKeep(Context c, Foc tileCacheDirectory) throws IOException {
        ArrayList<SourceSummary> old = new ArrayList<>(sourceSummaries);

        rescan(c, tileCacheDirectory);
        replaceFromList(old);
    }

    private void replaceFromList(ArrayList<SourceSummary> list) {
        for (int index=0; index < size(); index++) {
            int foundIndex = findInList(list, get(index).getName());

            if (foundIndex > -1) {
                sourceSummaries.set(index, list.get(foundIndex));
            }
        }
    }

    public int findIndex(String name) {
        return findInList(sourceSummaries, name);
    }


    private static int findInList(ArrayList<SourceSummary> old, String name) {
        for (int i=0; i<old.size(); i++) {
            if (old.get(i).getName().equals(name)) return i;
        }
        return -1;
    }


    public void rescan(Context c, Foc tileCacheDirectory) throws IOException {
        //tileCacheDirectory = tileCacheDirectory.getCanonicalFile();

        reset(c);

        tileCacheDirectory.foreachDir(new Foc.Execute() {
            @Override
            public void execute(Foc child) {
                sourceSummaries.add(new SourceSummary(child.getName()));
            }
        });

    }


    public void addFile(TileFile file) {
        long length = file.length();

        get(0).addFile(length);
        get(file.getSource()).addFile(length);
    }


    public void resetToRemove() {
        for (SourceSummary summary : sourceSummaries) {
            summary.clear_rm();
        }
    }

    public void addFileToRemove(TileFile f) {
        final long l = f.length();

        get(0).addFileToRemove(f.length());
        get(f.getSource()).addFileToRemove(l);
    }


    public void addFileRemoved(TileFile f) {
        final long length = f.length();

        get(0).addFileRemoved(length);
        get(f.getSource()).addFileRemoved(length);
    }

    public SourceSummary get(int index) {
        if (index < sourceSummaries.size())
            return sourceSummaries.get(index);

        return NULL_SUMMARY;
    }

    public int size() {
        return sourceSummaries.size();
    }


    public Foc toFile(Foc baseDirectory, TileFile t) {
        return t.toFile(baseDirectory.child(get(t.getSource()).getName()));
    }
}
