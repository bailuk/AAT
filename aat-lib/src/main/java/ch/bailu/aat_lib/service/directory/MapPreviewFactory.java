package ch.bailu.aat_lib.service.directory;

import ch.bailu.aat_lib.gpx.information.GpxInformation;
import ch.bailu.foc.Foc;

public interface MapPreviewFactory {
    MapPreviewInterface createMapPreview(GpxInformation info, Foc previewImageFile);
}
