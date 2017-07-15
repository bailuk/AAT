package ch.bailu.aat.services.tileremover;

import ch.bailu.simpleio.foc.Foc;

public class SelectedTileDirectoryInfo {

    public final String name;

    public final Foc baseDirectory;
    public final Foc directory;


    public final int index;
    public final int scannedFiles;


    public SelectedTileDirectoryInfo(Foc bd, Foc d, String n, int i) {
        name = n;
        baseDirectory = bd;
        directory = d;
        index = i;
        scannedFiles = 0;
    }
}

