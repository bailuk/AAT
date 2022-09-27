package ch.bailu.aat_lib.html;


import java.util.Locale;

import ch.bailu.aat_lib.description.AltitudeDescription;
import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.description.CurrentSpeedDescription;
import ch.bailu.aat_lib.description.DistanceDescription;
import ch.bailu.aat_lib.description.SpeedDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;
import ch.bailu.aat_lib.preferences.StorageInterface;


public class MarkupBuilderGpx extends MarkupBuilder {

    private final DistanceDescription distance;
    private final SpeedDescription speed;
    private final AltitudeDescription altitude;

    public MarkupBuilderGpx(StorageInterface storage) {
        this(storage, MarkupConfig.HTML);
    }

    public MarkupBuilderGpx(StorageInterface storage, MarkupConfig config) {
        super(config);
        distance = new DistanceDescription(storage);
        speed = new CurrentSpeedDescription(storage);
        altitude = new AltitudeDescription(storage);
    }

    public void appendInfo(GpxInformation info, int index) {
        final int count=index+1, total = info.getGpxList().getPointList().size();

        appendHeader(info.getFile().getName());
        appendNl(config.getBoldOpen() + count + config.getBoldClose() + "/" + total);
    }

    public void appendNode(GpxPointNode n, GpxInformation i) {
        if (i.getType() == GpxType.TRACK && n.getTimeStamp() != 0 ) {
            appendNl(speed.getLabel(), speed.getSpeedDescription(n.getSpeed()));
        }
        appendNl(altitude.getLabel(), distance.getAltitudeDescription(n.getAltitude()));
    }

    public void appendNl(ContentDescription d) {
        appendNl(d.getLabel(), d.getValueAsString());
    }

    public void appendAttributes(GpxAttributes a) {
        if (a.size() > 0) {
            for (int i = 0; i < a.size(); i++) {
                String kString = a.getSKeyAt(i);
                String v = a.getAt(i);

                if (kString.toLowerCase(Locale.ROOT).contains("name")) {
                    appendBoldNl(kString, v);
                }  else {
                    appendKeyValueNl(kString, v);
                }
            }
        }
    }
}
