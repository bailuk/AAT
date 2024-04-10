package ch.bailu.aat_lib.service.directory;

public interface MapPreviewInterface {
    boolean isReady();

    void generateBitmapFile();

    void onDestroy();
}
