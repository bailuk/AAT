package ch.bailu.aat.services.tileremover;

import java.io.File;

public class SelectedTileDirectoryInfo {

    public final String name;

    public final File baseDirectory;
    public final File directory;


    public final int index;
    public final int scannedFiles;


    public SelectedTileDirectoryInfo(File bd, File d, String n, int i) {
        name = n;
        baseDirectory = bd;
        directory = d;
        index = i;
        scannedFiles = 0;
    }
}

